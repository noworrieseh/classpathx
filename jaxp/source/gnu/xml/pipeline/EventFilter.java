/*
 * $Id: EventFilter.java,v 1.2 2001-07-05 01:43:02 db Exp $
 * Copyright (C) 1999-2001 David Brownell
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gnu.xml.pipeline;

import java.util.Hashtable;

import org.xml.sax.*;
import org.xml.sax.ext.*;

import gnu.xml.util.DefaultHandler;


/**
 * A customizable event consumer, used to assemble various kinds of filters
 * using SAX handlers and an optional second consumer.  It can be initialized
 * in two ways: <ul>
 *
 *  <li> To serve as a passthrough, sending all events to a second consumer.
 *
 *  <li> To serve as a dead end, with all handlers null;
 *
 * </ul>
 *
 * <p> SAX handlers given to these objects will often consume events
 * completely, or chain them to the second consumer after filtering.  That
 * second consumer may be acquired through {@link #getNext}.
 *
 * <p> The simplest way to build such handlers is to create a subclass
 * which overrides one or more handler methods, and register an instance
 * of that subclass to serve as one or more of the SAX handlers.  Methods
 * which aren't overridden will automatically chain to the appropriate
 * method in the next event consumer.  Subclass methods may invoke superclass
 * methods to do similar chaining, perhaps after modifying parameters.
 * Event filter subclasses should use the ErrorHandler to report most
 * errors.  Record and use the locator, for more useful diagnostics.
 *
 * <p> Another important technique is to construct a filter consisting
 * of only a few specific types of handler.  For example, one could easily
 * prune out lexical events or various declarations by providing handlers
 * which don't pass the events on, such as an instance of the
 * <a href="../DefaultHandler.html">DefaultHandler</a> class.
 *
 * <p> This may be viewed as the consumer side analogue of the
 * {@link org.xml.sax.helpers.XMLFilterImpl XMLFilterImpl} class, introduced
 * in SAX2.  Events are pushed through an EventFilter (like normal method
 * calls), not pulled through like data through a parser.
 * Also, there is no policy that the producer must look like a SAX parser;
 * application modules can send events directly.  Implementations of that
 * "XMLFilter" interface may be used with an {@link EventProducer} on the
 * event production part of a pipeline.
 *
 * @author David Brownell
 * @version $Date: 2001-07-05 01:43:02 $
 */
public class EventFilter
    implements EventConsumer, ContentHandler, DTDHandler,
	    LexicalHandler, DeclHandler

    //
    // The is-a-handler API is only provided for
    // the convenience of filter implementors.
    //
{
    // SAX handlers
    private ContentHandler		docHandler, docNext;
    private DTDHandler			dtdHandler, dtdNext;
    private LexicalHandler		lexHandler, lexNext;
    private DeclHandler			declHandler, declNext;
    // and ideally, one more for the stuff SAX2 doesn't show

    private Hashtable			properties;

    private EventConsumer		next;
    private ErrorHandler		errHandler;

    // XXX robustly support getDocumentLocator

    
    /** SAX2 URI prefix for standard properties (mostly for handlers). */
    public static final String		PROPERTY_URI
	= "http://xml.org/sax/properties/";


    /**
     * Initializes all handlers to null.
     */
	// constructor used by PipelineFactory
    public EventFilter () { }


    /**
     * Handlers that are not otherwise set will default to those from
     * the specified consumer, making it easy to pass events through.
     * If the consumer is null, all handlers are initialzed to null.
     */
	// constructor used by PipelineFactory
    public EventFilter (EventConsumer consumer)
    {
	if (consumer == null)
	    return;

	next = consumer;

	docHandler = docNext = consumer.getContentHandler ();
	dtdHandler = dtdNext = consumer.getDTDHandler ();
	try {
	    declHandler = declNext = (DeclHandler)
		    consumer.getProperty (PROPERTY_URI + "declaration-handler");
	} catch (SAXException e) { /* leave value null */ }
	try {
	    lexHandler = lexNext = (LexicalHandler)
		    consumer.getProperty (PROPERTY_URI + "lexical-handler");
	} catch (SAXException e) { /* leave value null */ }
    }

    /**
     * Records the error handler that should be used by this stage, and
     * passes it on to any subsequent stage.
     */
    final public void setErrorHandler (ErrorHandler handler)
    {
	errHandler = handler;
	if (next != null)
	    next.setErrorHandler (handler);
    }

    /**
     * Returns the error handler assigned this filter stage, or null
     * if no such assigment has been made.
     */
    final public ErrorHandler getErrorHandler ()
    {
	return errHandler;
    }


    /**
     * Returns the next event consumer in sequence; or null if there
     * is no such handler.
     */
    final public EventConsumer getNext ()
	{ return next; }


    /**
     * Assigns the content handler to use; a null handler indicates
     * that these events will not be forwarded.
     * This overrides the previous settting for this handler; if that
     * handler needs to be called, it must be separately saved.
     */
    final public void setContentHandler (ContentHandler h)
    {
	docHandler = h;
    }

    /** Returns the content handler being used. */
    final public ContentHandler getContentHandler ()
    {
	return docHandler;
    }

    /**
     * Assigns the dtd handler to use.
     * This overrides the previous settting for this handler; if that
     * handler needs to be called, it must be separately saved.
     */
    final public void setDTDHandler (DTDHandler h)
	{ dtdHandler = h; }

    /** Returns the dtd handler being used. */
    final public DTDHandler getDTDHandler ()
    {
	return dtdHandler;
    }

    /** Stores a property of unknown intent (usually a handler) */
    final public void setProperty (String id, Object o)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
	try {
	    Object	value = getProperty (id);

	    if (id == o)
		return;
	    if ((PROPERTY_URI + "declaration-handler").equals (id)) {
		declHandler = (DeclHandler) o;
		return;
	    }
	    if ((PROPERTY_URI + "lexical-handler").equals (id)) {
		lexHandler = (LexicalHandler) o;
		return;
	    }

	    // builtin key that's not settable
	    if (properties != null && !properties.containsKey (id))
		throw new SAXNotSupportedException (id);

	} catch (SAXNotRecognizedException e) {
	    if (properties == null)
		properties = new Hashtable (5);
	}
	properties.put (id, o);
    }

    /** Retrieves a property of unknown intent (usually a handler) */
    final public Object getProperty (String id)
    throws SAXNotRecognizedException
    {
	if ((PROPERTY_URI + "declaration-handler").equals (id)) 
	    return declHandler;
	if ((PROPERTY_URI + "lexical-handler").equals (id))
	    return lexHandler;

	Object	property = null;

	if (properties != null)
	    property = properties.get (id);
	if (property != null)
	    return property;
	throw new SAXNotRecognizedException (id);
    }


    // CONTENT HANDLER DELEGATIONS

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void setDocumentLocator (Locator l)
    {
	if (docNext != null)
	    docNext.setDocumentLocator (l);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void startDocument () throws SAXException
    {
	if (docNext != null)
	    docNext.startDocument ();
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void skippedEntity (String n) throws SAXException
    {
	if (docNext != null)
	    docNext.skippedEntity (n);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void processingInstruction (String target, String data)
    throws SAXException
    {
	if (docNext != null)
	    docNext.processingInstruction (target, data);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void characters (char buf [], int off, int len)
    throws SAXException
    {
	if (docNext != null)
	    docNext.characters (buf, off, len);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void ignorableWhitespace (char buf [], int off, int len)
    throws SAXException
    {
	if (docNext != null)
	    docNext.ignorableWhitespace (buf, off, len);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void startPrefixMapping (String p, String u) throws SAXException
    {
	if (docNext != null)
	    docNext.startPrefixMapping (p, u);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void startElement (
	String ns, String l,
	String name, Attributes atts
    ) throws SAXException
    {
	if (docNext != null)
	    docNext.startElement (ns, l, name, atts);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void endElement (String ns, String l, String n)
    throws SAXException
    {
	if (docNext != null)
	    docNext.endElement (ns, l, n);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void endPrefixMapping (String p) throws SAXException
    {
	if (docNext != null)
	    docNext.endPrefixMapping (p);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void endDocument () throws SAXException
    {
	if (docNext != null)
	    docNext.endDocument ();
    }


    // DTD HANDLER DELEGATIONS
    
    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void unparsedEntityDecl (String s1, String s2, String s3, String s4)
    throws SAXException
    {
	if (dtdNext != null)
	    dtdNext.unparsedEntityDecl (s1, s2, s3, s4);
    }
    
    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void notationDecl (String s1, String s2, String s3)
    throws SAXException
    {
	if (dtdNext != null)
	    dtdNext.notationDecl (s1, s2, s3);
    }
    

    // LEXICAL HANDLER DELEGATIONS

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void startDTD (String root, String p, String s)
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.startDTD (root, p, s);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void endDTD ()
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.endDTD ();
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void comment (char buf [], int off, int len)
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.comment (buf, off, len);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void startCDATA ()
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.startCDATA ();
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void endCDATA ()
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.endCDATA ();
    }

    /**
     * <b>SAX2:</b> passes this callback to the next consumer, if any.
     */
    public void startEntity (String name)
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.startEntity (name);
    }

    /**
     * <b>SAX2:</b> passes this callback to the next consumer, if any.
     */
    public void endEntity (String name)
    throws SAXException
    {
	if (lexNext != null)
	    lexNext.endEntity (name);
    }
    

    // DECLARATION HANDLER DELEGATIONS


    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void elementDecl (String name, String model)
    throws SAXException
    {
	if (declNext != null)
	    declNext.elementDecl (name, model);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void attributeDecl (String e, String a,
	    String b, String c, String d)
    throws SAXException
    {
	if (declNext != null)
	    declNext.attributeDecl (e, a, b, c, d);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void externalEntityDecl (String name, String pub, String sys)
    throws SAXException
    {
	if (declNext != null)
	    declNext.externalEntityDecl (name, pub, sys);
    }

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void internalEntityDecl (String name, String value)
    throws SAXException
    {
	if (declNext != null)
	    declNext.internalEntityDecl (name, value);
    }
}
