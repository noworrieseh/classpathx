/*
 * $Id: EventFilter.java,v 1.4 2001-07-10 21:22:02 db Exp $
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
 * using SAX handlers and an optional second consumer.  It can be constructed
 * in two ways: <ul>
 *
 *  <li> To serve as a passthrough, sending all events to a second consumer.
 *  The second consumer may be identified through {@link #getNext}.
 *
 *  <li> To serve as a dead end, with all handlers null;
 *  {@link #getNext} returns null.
 *
 * </ul>
 *
 * <p> Additionally, SAX handlers may be provided, which completely replace
 * handlers from the consumer provided through the constructor.  To make
 * it easier to build specialized filter classes, this class implements
 * all the standard SAX consumer handlers, and those implementations
 * will delegate to the consumer accessed by {@link #getNext}.
 *
 * <p> The simplest way to create a custom a filter class is to create a
 * subclass which overrides one or more handler interface methods, and
 * then itself registers as a handler (for those interfaces) to the base
 * class using a call such as <em>setContentHandler(this)</em>.  That way,
 * those overridden methods will intercept those event callbacks, and
 * all other event callbacks will pass events to the next consumer.
 * Overridden methods may invoke superclass methods (perhaps after modifying
 * parameters) should they wish to delegate such calls.  Such subclasses
 * should use shared ErrorHandler to report errors.
 *
 * <p> Another important technique is to construct a filter consisting
 * of only a few specific types of handler.  For example, one could easily
 * prune out lexical events or various declarations by providing handlers
 * which don't pass the events on, or null handlers.
 *
 * <hr />
 *
 * <p> This may be viewed as the consumer oriented analogue of the SAX2
 * {@link org.xml.sax.helpers.XMLFilterImpl XMLFilterImpl} class.
 * Key differences include: <ul>
 *
 *	<li> This fully separates consumer and producer roles:  it
 *	does not implement the producer side <em>XMLReader</em> or
 *	<em>EntityResolver</em> interfaces, so it can only be used
 *	in "push" mode (it has no <em>parse()</em> methods).
 *
 *	<li> "Extension" handlers are fully supported, enabling a
 *	richer set of application requirements.
 *	And it implements {@link EventConsumer}, which groups related
 *	consumer methods together, rather than leaving them separated.
 *
 *	<li> ErrorHandler support is separated, on the grounds that
 *	pipeline stages need to share the same error handling policy.
 *
 *	<li> The chaining which is visible is to the next consumer,
 *	not to the preceding producer.  It supports "fan-in", where
 *	a consumer can be fed by several producers.  (For "fan-out",
 *	see the {@link TeeConsumer} class.)
 *
 *	<li> The chaining is set up differently.  It is intended to be
 *	set up fron terminus towards producer, during filter construction,
 *	as described above.  This is part of an early binding model;
 *	events don't need to pass through stages which ignore them.
 *
 *	</ul>
 *
 * @author David Brownell
 * @version $Date: 2001-07-10 21:22:02 $
 */
public class EventFilter
    implements EventConsumer, ContentHandler, DTDHandler,
	    LexicalHandler, DeclHandler
{
    // SAX handlers
    private ContentHandler		docHandler, docNext;
    private DTDHandler			dtdHandler, dtdNext;
    private LexicalHandler		lexHandler, lexNext;
    private DeclHandler			declHandler, declNext;
    // and ideally, one more for the stuff SAX2 doesn't show

    private Hashtable			properties;

    private Locator			locator;
    private EventConsumer		next;
    private ErrorHandler		errHandler;

    
    /** SAX2 URI prefix for standard properties (mostly for handlers). */
    public static final String		PROPERTY_URI
	= "http://xml.org/sax/properties/";

    /** SAX2 property identifier for {@link DeclHandler} events */
    public static final String		DECL_HANDLER
	= PROPERTY_URI + "declaration-handler";
    /** SAX2 property identifier for {@link LexicallHandler} events */
    public static final String		LEXICAL_HANDLER
	= PROPERTY_URI + "lexical-handler";


    /**
     * Binds the standard SAX2 handlers from the specified consumer
     * to the specified producer.  These handlers include the core
     * {@link ContentHandler} and {@link DTDHandler}, plus the extension
     * {@link DeclHandler} and {@link LexicalHandler}.  Any additional
     * application-specific handlers need to be bound separately.
     *
     * <p> Note that this method works with any kind of event consumer,
     * not just event filters.
     *
     * @param producer will deliver events to the specifid consumer 
     * @param consumer supplies event handlers to be associated
     *	with the producer
     */
    public static void bind (XMLReader producer, EventConsumer consumer)
    {
	producer.setContentHandler (consumer.getContentHandler ());
	producer.setDTDHandler (consumer.getDTDHandler ());
	try {
	    producer.setProperty (DECL_HANDLER,
	    	consumer.getProperty (DECL_HANDLER));
	} catch (Exception e) { /* ignore */ }
	try {
	    producer.setProperty (LEXICAL_HANDLER,
	    	consumer.getProperty (LEXICAL_HANDLER));
	} catch (Exception e) { /* ignore */ }
    }
    
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

	// We delegate through the "xxNext" handlers, and
	// report the "xxHandler" ones on our input side.

	// Normally a subclass would both override handler
	// methods and register itself as the "xxHandler".

	docHandler = docNext = consumer.getContentHandler ();
	dtdHandler = dtdNext = consumer.getDTDHandler ();
	try {
	    declHandler = declNext = (DeclHandler)
		    consumer.getProperty (DECL_HANDLER);
	} catch (SAXException e) { /* leave value null */ }
	try {
	    lexHandler = lexNext = (LexicalHandler)
		    consumer.getProperty (LEXICAL_HANDLER);
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
     * This overrides the previous settting for this handler, which was
     * probably pointed to the next consumer by the base class constructor.
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
     * Assigns the DTD handler to use; a null handler indicates
     * that these events will not be forwarded.
     * This overrides the previous settting for this handler, which was
     * probably pointed to the next consumer by the base class constructor.
     */
    final public void setDTDHandler (DTDHandler h)
	{ dtdHandler = h; }

    /** Returns the dtd handler being used. */
    final public DTDHandler getDTDHandler ()
    {
	return dtdHandler;
    }

    /**
     * Stores the property, normally a handler; a null handler indicates
     * that these events will not be forwarded.
     * This overrides the previous handler settting, which was probably
     * pointed to the next consumer by the base class constructor.
     */
    final public void setProperty (String id, Object o)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
	try {
	    Object	value = getProperty (id);

	    if (value == o)
		return;
	    if (DECL_HANDLER.equals (id)) {
		declHandler = (DeclHandler) o;
		return;
	    }
	    if (LEXICAL_HANDLER.equals (id)) {
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
	if (DECL_HANDLER.equals (id)) 
	    return declHandler;
	if (LEXICAL_HANDLER.equals (id))
	    return lexHandler;

	Object	property = null;

	if (properties != null)
	    property = properties.get (id);
	if (property != null)
	    return property;
	throw new SAXNotRecognizedException (id);
    }

    /**
     * Returns any locator provided to the next consumer, if this class
     * (or a subclass) is handling {@link ContentHandler } events.
     */
    public Locator getDocumentLocator ()
	{ return locator; }


    // CONTENT HANDLER DELEGATIONS

    /** <b>SAX2:</b> passes this callback to the next consumer, if any */
    public void setDocumentLocator (Locator l)
    {
	locator = l;
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
	locator = null;
    }


    // DTD HANDLER DELEGATIONS
    
    /** <b>SAX1:</b> passes this callback to the next consumer, if any */
    public void unparsedEntityDecl (String s1, String s2, String s3, String s4)
    throws SAXException
    {
	if (dtdNext != null)
	    dtdNext.unparsedEntityDecl (s1, s2, s3, s4);
    }
    
    /** <b>SAX1:</b> passes this callback to the next consumer, if any */
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
