/*
 * $Id: TeeConsumer.java,v 1.4 2001-10-23 17:42:25 db Exp $
 * Copyright (C) 1999-2001 David Brownell
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License. 
 */

package gnu.xml.pipeline;

import java.io.IOException;
import org.xml.sax.*;
import org.xml.sax.ext.*;
//import gnu.xml.util;


/**
 * Fans its events out to two other consumers, a "tee" filter stage in an
 * event pipeline.  Networks can be assembled with multiple output points.
 *
 * <p> Error handling should be simple if you remember that exceptions
 * you throw will cancel later stages in that callback's pipeline, and
 * generally the producer will stop if it sees such an exception.  You
 * may want to protect your pipeline against such backflows, making a
 * kind of reverse filter (or valve?) so that certain exceptions thrown by
 * your pipeline will caught and handled before the producer sees them.
 * Just use a "try/catch" block, rememebering that really important
 * cleanup tasks should be in "finally" clauses.
 *
 * <p> That issue isn't unique to "tee" consumers, but tee consumers have
 * the additional twist that exceptions thrown by the first consumer
 * will cause the second consumer not to see the callback (except for
 * the endDocument callback, which signals state cleanup).
 *
 * @author David Brownell
 * @version $Date: 2001-10-23 17:42:25 $
 */
final public class TeeConsumer
	implements EventConsumer,
		ContentHandler, DTDHandler,
		LexicalHandler,DeclHandler
{
    private EventConsumer	first, rest;

    // cached to minimize time overhead
    private ContentHandler	docFirst, docRest;
    private DeclHandler		declFirst, declRest;
    private LexicalHandler	lexFirst, lexRest;


    /**
     * Constructs a consumer which sends all its events to the first
     * consumer, and then the second one.  If the first consumer throws
     * an exception, the second one will not see the event which
     * caused that exception to be reported.
     *
     * @param car The first consumer to get the events
     * @param cdr The second consumer to get the events
     */
    public TeeConsumer (EventConsumer car, EventConsumer cdr)
    {
	if (car == null || cdr == null)
	    throw new NullPointerException ();
	first = car;
	rest = cdr;

	//
	// Cache the handlers.
	//
	docFirst = first.getContentHandler ();
	docRest = rest.getContentHandler ();
	// DTD handler isn't cached (rarely needed)

	try {
	    declFirst = null;
	    declFirst = (DeclHandler) first.getProperty (
			EventFilter.DECL_HANDLER);
	} catch (SAXException e) {}
	try {
	    declRest = null;
	    declRest = (DeclHandler) rest.getProperty (
			EventFilter.DECL_HANDLER);
	} catch (SAXException e) {}

	try {
	    lexFirst = null;
	    lexFirst = (LexicalHandler) first.getProperty (
			EventFilter.LEXICAL_HANDLER);
	} catch (SAXException e) {}
	try {
	    lexRest = null;
	    lexRest = (LexicalHandler) rest.getProperty (
			EventFilter.LEXICAL_HANDLER);
	} catch (SAXException e) {}
    }

/* FIXME
    /**
     * Constructs a pipeline, and is otherwise a shorthand for the
     * two-consumer constructor for this class.
     *
     * @param first Description of the first pipeline to get events,
     *	which will be passed to {@link PipelineFactory#createPipeline}
     * @param rest The second pipeline to get the events
     * /
	// constructor used by PipelineFactory
    public TeeConsumer (String first, EventConsumer rest)
    throws IOException
    {
	this (PipelineFactory.createPipeline (first), rest);
    }
*/

    /** Returns the first pipeline to get event calls. */
    public EventConsumer getFirst ()
	{ return first; }

    /** Returns the second pipeline to get event calls. */
    public EventConsumer getRest ()
	{ return rest; }

    /** Returns the content handler being used. */
    final public ContentHandler getContentHandler ()
    {
	if (docRest == null)
	    return docFirst;
	if (docFirst == null)
	    return docRest;
	return this;
    }

    /** Returns the dtd handler being used. */
    final public DTDHandler getDTDHandler ()
    {
	// not cached (hardly used)
	if (rest.getDTDHandler () == null)
	    return first.getDTDHandler ();
	if (first.getDTDHandler () == null)
	    return rest.getDTDHandler ();
	return this;
    }

    /** Returns the declaration or lexical handler being used. */
    final public Object getProperty (String id)
    throws SAXNotRecognizedException
    {
	//
	// in degenerate cases, we have no work to do.
	//
	Object	firstProp = null, restProp = null;

	try { firstProp = first.getProperty (id); }
	catch (SAXNotRecognizedException e) { /* ignore */ }
	try { restProp = rest.getProperty (id); }
	catch (SAXNotRecognizedException e) { /* ignore */ }

	if (restProp == null)
	    return firstProp;
	if (firstProp == null)
	    return restProp;

	//
	// we've got work to do; handle two builtin cases.
	//
	if (EventFilter.DECL_HANDLER.equals (id))
	    return this;
	if (EventFilter.LEXICAL_HANDLER.equals (id))
	    return this;

	//
	// non-degenerate, handled by both consumers, but we don't know
	// how to handle this.
	//
	throw new SAXNotRecognizedException ("can't tee: " + id);
    }

    /**
     * Provides the error handler to both subsequent nodes of
     * this filter stage.
     */
    public void setErrorHandler (ErrorHandler handler)
    {
	first.setErrorHandler (handler);
	rest.setErrorHandler (handler);
    }


    //
    // ContentHandler
    //
    public void setDocumentLocator (Locator l)
    {
	// this call is not made by all parsers
	docFirst.setDocumentLocator (l);
	docRest.setDocumentLocator (l);
    }

    public void startDocument ()
    throws SAXException
    {
	docFirst.startDocument ();
	docRest.startDocument ();
    }

    public void endDocument ()
    throws SAXException
    {
	try {
	    docFirst.endDocument ();
	} finally {
	    docRest.endDocument ();
	}
    }

    public void startPrefixMapping (String prefix, String uri)
    throws SAXException
    {
	docFirst.startPrefixMapping (prefix, uri);
	docRest.startPrefixMapping (prefix, uri);
    }

    public void endPrefixMapping (String prefix)
    throws SAXException
    {
	docFirst.endPrefixMapping (prefix);
	docRest.endPrefixMapping (prefix);
    }

    public void skippedEntity (String name)
    throws SAXException
    {
	docFirst.skippedEntity (name);
	docRest.skippedEntity (name);
    }

    public void startElement (String uri, String local,
	    String name, Attributes atts)
    throws SAXException
    {
	docFirst.startElement (uri, local, name, atts);
	docRest.startElement (uri, local, name, atts);
    }

    public void endElement (String uri, String local, String name)
    throws SAXException
    {
	docFirst.endElement (uri, local, name);
	docRest.endElement (uri, local, name);
    }

    public void processingInstruction (String target, String data)
    throws SAXException
    {
	docFirst.processingInstruction (target, data);
	docRest.processingInstruction (target, data);
    }

    public void characters (char buf [], int off, int len)
    throws SAXException
    {
	docFirst.characters (buf, off, len);
	docRest.characters (buf, off, len);
    }

    public void ignorableWhitespace (char buf [], int off, int len)
    throws SAXException
    {
	docFirst.ignorableWhitespace (buf, off, len);
	docRest.ignorableWhitespace (buf, off, len);
    }


    //
    // DTDHandler
    //
    public void notationDecl (String name, String pubid, String sysid)
    throws SAXException
    {
	DTDHandler	l1 = first.getDTDHandler ();
	DTDHandler	l2 = rest.getDTDHandler ();

	l1.notationDecl (name, pubid, sysid);
	l2.notationDecl (name, pubid, sysid);
    }

    public void unparsedEntityDecl (String name,
	    String pubid, String sysid,
	    String ndata
    ) throws SAXException
    {
	DTDHandler	l1 = first.getDTDHandler ();
	DTDHandler	l2 = rest.getDTDHandler ();

	l1.unparsedEntityDecl (name, pubid, sysid, ndata);
	l2.unparsedEntityDecl (name, pubid, sysid, ndata);
    }


    //
    // DeclHandler
    //
    public void attributeDecl (String element, String attribute,
	String type,
	String valueDefault, String value)
    throws SAXException
    {
	declFirst.attributeDecl (element, attribute, type, valueDefault, value);
	declRest.attributeDecl (element, attribute, type, valueDefault, value);
    }

    public void elementDecl (String element, String model)
    throws SAXException
    {
	declFirst.elementDecl (element, model);
	declRest.elementDecl (element, model);
    }

    public void externalEntityDecl (String entity,
	String publicId, String systemId)
    throws SAXException
    {
	declFirst.externalEntityDecl (entity, publicId, systemId);
	declRest.externalEntityDecl (entity, publicId, systemId);
    }

    public void internalEntityDecl (String entity, String value)
    throws SAXException
    {
	declFirst.internalEntityDecl (entity, value);
	declRest.internalEntityDecl (entity, value);
    }


    //
    // LexicalHandler
    //
    public void comment (char buf [], int off, int len)
    throws SAXException
    {
	lexFirst.comment (buf, off, len);
	lexRest.comment (buf, off, len);
    }
    
    public void startCDATA ()
    throws SAXException
    {
	lexFirst.startCDATA ();
	lexRest.startCDATA ();
    }
    
    public void endCDATA ()
    throws SAXException
    {
	lexFirst.endCDATA ();
	lexRest.endCDATA ();
    }
    
    public void startEntity (String name)
    throws SAXException
    {
	lexFirst.startEntity (name);
	lexRest.startEntity (name);
    }
    
    public void endEntity (String name)
    throws SAXException
    {
	lexFirst.endEntity (name);
	lexRest.endEntity (name);
    }
    
    public void startDTD (String name, String pubid, String sysid)
    throws SAXException
    {
	lexFirst.startDTD (name, pubid, sysid);
	lexRest.startDTD (name, pubid, sysid);
    }
    
    public void endDTD ()
    throws SAXException
    {
	lexFirst.endDTD ();
	lexRest.endDTD ();
    }
}
