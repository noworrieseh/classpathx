/*
 * $Id: DomConsumer.java,v 1.5 2001-07-10 22:29:04 db Exp $
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

import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

import gnu.xml.util.DomParser;


/**
 * This consumer builds a DOM Document from its input, acting either as a
 * pipeline terminus or as an intermediate buffer.  When a document's worth
 * of events has been delivered to this consumer, that document is read with
 * a {@link DomParser} and sent to the next consumer.  It is also available
 * as a read-once property.
 *
 * <p>The DOM tree is constructed as faithfully as possible.  There are some
 * complications since a DOM should expose behaviors that can't be implemented
 * without API backdoors into that DOM, and because some SAX parsers don't
 * report all the information that DOM permits to be exposed.  The general
 * problem areas involve information from the Document Type Declaration (DTD).
 * DOM only represents a limited subset, but has some behaviors that depend
 * on much deeper knowledge of a document's DTD.  You shouldn't have much to
 * worry about unless you change handling of "extra" nodes from its default
 * setting (which ignores them all); note if you use JAXP to populate your
 * DOM trees, it wants to save "extra" nodes by default.  Otherwise, your
 * main worry will be if you use a SAX parser that doesn't flag ignorable
 * whitespace unless it's validating (few don't).
 *
 * <p> The SAX2 events used as input must contain XML Names for elements
 * and attributes, with original prefixes.  In SAX2,
 * this is optional unless the "namespace-prefixes" parser feature is set.
 * Moreover, many application components won't provide completely correct
 * structures anyway.  <em>Before you convert a DOM to an output document,
 * you should plan to postprocess it to create or repair such namespace
 * information.</em> The {@link NSFilter} pipeline stage does such work.
 *
 * <p> <em>Note:  changes late in DOM L2 process made it impractical to
 * attempt to create the DocumentType node in any implementation-neutral way,
 * much less to populate it (L1 didn't support even creating such nodes).
 * To create and populate such a node, subclass the inner
 * {@link DomConsumer.Handler} class and teach it about the backdoors into
 * whatever DOM implementation you want.  It's possible that some revised
 * DOM API will finally resolve this problem. </em>
 *
 * @see DomParser
 *
 * @author David Brownell
 * @version $Date: 2001-07-10 22:29:04 $
 */
public class DomConsumer implements EventConsumer
{
    private Class		domImpl;

    private boolean		saveExtra;
    // FIXME:  for better JAXP integration, split out:
    //	- whether to hide CDATA boundaries
    //	- doctype stuff

    private boolean		hidingComments;
    private boolean		hidingWhitespace;

    private boolean		usingNamespaces = true;
    private boolean		expandingReferences = true;
    private Handler		handler;
    private ErrorHandler	errHandler;

    private EventConsumer	next;


    /**
     * Configures this consumer to use the specified implementation
     * of DOM when constructing its result value.
     *
     * @param impl class implementing {@link org.w3c.dom.Document Document}
     *	which publicly exposes a default constructor
     *
     * @exception SAXException when there is a problem creating an
     *	empty DOM document using the specified implementation
     */
	// constructor used by PipelineFactory
    public DomConsumer (Class impl)
    throws SAXException
    {
	domImpl = impl;
	handler = new Handler (this);
    }

    /**
     * This is the hook through which a subclass provides a handler
     * which knows how to access DOM extensions, specific to some
     * implementation, to record additional data in a DOM.
     * Treat this as part of construction; don't call it except
     * before (or between) parses.
     */
    protected void setHandler (Handler h)
    {
	handler = h;
    }


    private Document emptyDocument ()
    throws SAXException
    {
	try {
	    return (Document) domImpl.newInstance ();
	} catch (IllegalAccessException e) {
	    throw new SAXException ("can't access constructor: "
		    + e.getMessage ());
	} catch (InstantiationException e) {
	    throw new SAXException ("can't instantiate Document: "
		    + e.getMessage ());
	}
    }


    /**
     * Configures this consumer as a buffer/filter, using the system default
     * DOM implementation when constructing its result value.
     *
     * <p> This event consumer acts as a buffer and filter, in that it
     * builds a DOM tree and then writes it out when <em>endDocument</em>
     * is invoked.  Because of the limitations of DOM, much information
     * will as a rule not be seen in that replay.  To get a full fidelity
     * copy of the input event stream, use a {@link TeeConsumer}.
     *
     * @param impl class implementing {@link org.w3c.dom.Document Document}
     *	which publicly exposes a default constructor
     * @param next receives a "replayed" sequence of parse events when
     *	the <em>endDocument</em> method is invoked.
     *
     * @exception SAXException when there is a problem creating an
     *	empty DOM document using the specified DOM implementation
     */
	// constructor used by PipelineFactory
    public DomConsumer (Class impl, EventConsumer n)
    throws SAXException
    {
	this (impl);
	next = n;
    }


    /**
     * Returns the document constructed from the preceding
     * sequence of events.  This method should not be
     * used again until another sequence of events has been
     * given to this EventConsumer.  
     */
    final public Document getDocument ()
    {
	return handler.clearDocument ();
    }

    public void setErrorHandler (ErrorHandler handler)
    {
	errHandler = handler;
    }


    /**
     * Returns true if the consumer is expanding entity references in place
     * (the default), and false if childless EntityReference nodes should
     * instead be created.
     *
     * @see #setExpandingReferences
     */
    final public boolean	isExpandingReferences ()
	{ return expandingReferences; }

    /**
     * Controls whether the consumer will expand entity references in place,
     * or will instead replace them with childless entity reference nodes.
     *
     * @see #isExpandingReferences
     * @param flag True iff extra nodes should be saved; false otherwise.
     */
    final public void		setExpandingReferences (boolean flag)
	{ expandingReferences = flag; }
    

    /**
     * Returns true if the consumer is hiding comments (the default),
     * and false if they should be placed into the output document.
     *
     * @see #setHidingComments
     */
    public final boolean isHidingComments ()
	{ return hidingComments; }

    /**
     * Controls whether the consumer is hiding comments.
     *
     * @see #isHidingComments
     */
    public final void setHidingComments (boolean flag)
	{ hidingComments = flag; }


    /**
     * Returns true if the consumer is hiding ignorable whitespace
     * (the default), and false if such whitespace should be placed
     * into the output document as children of element nodes.
     *
     * @see #setHidingWhitespace
     */
    public final boolean isHidingWhitespace ()
	{ return hidingWhitespace; }

    /**
     * Controls whether the consumer hides ignorable whitespace
     *
     * @see #isHidingComments
     */
    public final void setHidingWhitespace (boolean flag)
	{ hidingWhitespace = flag; }


    /**
     * Returns true if the consumer is saving "extra" nodes, and false (the
     * default) otherwise.  "Extra" nodes are currently defined to be
     * CDATA nodes (instead of normal
     * text nodes), DocumentType and EntityReference nodes.  (Notation and
     * Entity nodes can't be portably created, and won't show up regardless
     * of the setting of this flag.)
     *
     * <p> You may not consistently see all these node types even if you
     * set this flag to true.  Only Level 2 DOM implementations can create
     * DocumentType nodes portably, but they can't be populated with any
     * portable APIs.  No DOM implementation can populate EntityReference
     * nodes with any portable APIs.  Not all parsers expose comment and
     * CDATA nodes, but if they do than most DOM implementations are able
     * to expose those nodes.  Any SAX parser may expose ignorable whitespace,
     * and most do so, so stripping out such whitespace is the most reliable
     * of this set of inconsistently supportable DOM features.
     *
     * @see #setSavingExtraNodes
     */
    final public boolean	isSavingExtraNodes ()
	{ return saveExtra; }

    /**
     * Controls whether the consumer will save "extra" nodes.
     *
     * @see #isSavingExtraNodes
     * @param flag True iff extra nodes should be saved; false otherwise.
     */
    final public void		setSavingExtraNodes (boolean flag)
	{ saveExtra = flag; }
    
    /**
     * Returns true (the default for L2 DOM implementations) if the
     * consumer is using an "XML + Namespaces" style DOM construction,
     * which will cause fatal errors on some legal XML 1.0 documents.
     *
     * @see #setUsingNamespaces
     */
    public boolean	isUsingNamespaces ()
	{ return usingNamespaces; }


    /**
     * Controls whether the consumer uses an "XML + Namespaces" style
     * DOM construction.
     *
     * @see #isUsingNamespaces
     * @param flag True iff namespaces should be enforced; else false.
     */
    public void		setUsingNamespaces (boolean flag)
	{ usingNamespaces = flag; }
    



    /** Returns the document handler being used. */
    final public ContentHandler getContentHandler ()
	{ return handler; }

    /** Returns the DTD handler being used. */
    final public DTDHandler getDTDHandler ()
	{ return handler; }

    /**
     * Returns the lexical handler being used.
     * (DOM construction can't really use declaration handlers.)
     */
    final public Object getProperty (String id)
    throws SAXNotRecognizedException
    {
	if ("http://xml.org/sax/properties/lexical-handler".equals (id))
	    return handler;
	if ("http://xml.org/sax/properties/declaration-handler".equals (id))
	    return handler;
	throw new SAXNotRecognizedException (id);
    }

    EventConsumer getNext () { return next; }

    ErrorHandler getErrorHandler () { return errHandler; }

    /**
     * Class used to intercept various parsing events and use them to
     * populate a DOM document.  Subclasses would typically know and use
     * backdoors into specific DOM implementations, used to implement 
     * DTD-related functionality.
     *
     * <p> Note that if this ever throws a DOMException (runtime exception)
     * that will indicate a bug in the DOM (e.g. doesn't support something
     * per specification) or the parser (e.g. emitted an illegal name, or
     * accepted illegal input data). </p>
     */
    public static class Handler
	implements ContentHandler, LexicalHandler,
	    DTDHandler, DeclHandler
    {
	protected DomConsumer		consumer;

	private DOMImplementation	impl;
	private Document 		document;
	private boolean		isL2;

	private Locator		locator;
	private Node		top;
	private boolean		inCDATA;
	private boolean		mergeCDATA;
	private boolean		inDTD;
	private String		currentEntity;

	private boolean		recreatedAttrs;
	private AttributesImpl	attributes = new AttributesImpl ();

	/**
	 * Subclasses may use SAX2 events to provide additional
	 * behaviors in the resulting DOM.
	 */
	protected Handler (DomConsumer consumer)
	throws SAXException
	{
	    this.consumer = consumer;
	    document = consumer.emptyDocument ();
	    impl = document.getImplementation ();
	    isL2 = impl.hasFeature ("XML", "2.0");
	}

	private void fatal (String message, Exception x)
	throws SAXException
	{
	    SAXParseException	e;
	    ErrorHandler	errHandler = consumer.getErrorHandler ();;

	    if (locator == null)
		e = new SAXParseException (message, null, null, -1, -1, x);
	    else
		e = new SAXParseException (message, locator, x);
	    if (errHandler != null)
		errHandler.fatalError (e);
	    throw e;
	}

	/**
	 * Returns and forgets the document produced.  If the handler is
	 * reused, a new document may be created.
	 */
	Document clearDocument ()
	{
	    Document retval = document;
	    document = null;
	    locator = null;
	    return retval;
	}

	/**
	 * Returns the document under construction.
	 */
	protected Document getDocument ()
	    { return document; }


	// SAX1
	public void setDocumentLocator (Locator l)
	{
	    locator = l;
	}

	// SAX1
	public void startDocument ()
	throws SAXException
	{
	    if (document == null)
		try {
		    if (isL2) {
			// couple to original implementation
			document = impl.createDocument (null, "foo", null);
			document.removeChild (document.getFirstChild ());
		    } else {
			document = consumer.emptyDocument ();
		    }
		} catch (Exception e) {
		    fatal ("DOM create document", e);
		}
	    top = document;
	}

	// SAX1
	public void endDocument ()
	throws SAXException
	{
	    try {
		if (consumer.getNext () != null && document != null) {
		    DomParser	parser = new DomParser (document);

		    EventFilter.bind (parser, consumer.getNext ());
		    parser.parse ("ignored");
		}
	    } finally {
		top = null;
	    }
	}

	// SAX1
	public void processingInstruction (String target, String data)
	throws SAXParseException
	{
	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly)
	    if (currentEntity != null)
		return;

	    ProcessingInstruction	pi;

	    if (isL2
		    && consumer.isUsingNamespaces ()
		    && target.indexOf (':') != -1)
		namespaceError (
		    "PI target name is namespace nonconformant: "
			+ target);
	    if (inDTD)
		return;
	    pi = document.createProcessingInstruction (target, data);
	    top.appendChild (pi);
	}

	// SAX1
	public void characters (char buf [], int off, int len)
	throws SAXException
	{
	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly
	    // at creation time)
	    if (currentEntity != null)
		return;

	    String	value = new String (buf, off, len);
	    Node	lastChild = top.getLastChild ();

	    // merge consecutive text or CDATA nodes if appropriate.
	    if (lastChild instanceof Text) {
		if (!consumer.isSavingExtraNodes ()
			// consecutive Text content ... always merge
			|| (!inCDATA
			    && !(lastChild instanceof CDATASection))
			// consecutive CDATASection content ... don't
			// merge between sections, only within them
			|| (inCDATA && mergeCDATA
			    && lastChild instanceof CDATASection)
			    ) {
		    CharacterData last = (CharacterData) lastChild;
		    
		    last.appendData (value);
		    return;
		}
	    }
	    if (inCDATA && consumer.isSavingExtraNodes ()) {
		top.appendChild (document.createCDATASection (value));
		mergeCDATA = true;
	    } else
		top.appendChild (document.createTextNode (value));
	}

	// SAX2
	public void skippedEntity (String name)
	throws SAXException
	{
	    // this callback is useless except to report errors, since
	    // we can't know if the ref was in content, within an
	    // attribute, within a declaration ... only one of those
	    // cases supports more intelligent action than a panic.
	    fatal ("skipped entity: " + name, null);
	}

	// SAX2
	public void startPrefixMapping (String prefix, String uri)
	throws SAXException
	{
	    // reconstruct "xmlns" attributes deleted by all
	    // SAX2 parsers without "namespace-prefixes" = true
	    if ("".equals (prefix))
		attributes.addAttribute ("", "", "xmlns",
			"CDATA", uri);
	    else
		attributes.addAttribute ("", "", "xmlns:" + prefix,
			"CDATA", uri);
	    recreatedAttrs = true;
	}

	// SAX2
	public void endPrefixMapping (String prefix)
	throws SAXException
	    { }

	// SAX2
	public void startElement (
	    String uri,
	    String local,
	    String name,
	    Attributes attrs
	) throws SAXParseException
	{
	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly)
	    if (currentEntity != null)
		return;

	    // parser discarded basic information; DOM tree isn't writable
	    // without massaging to assign prefixes to all nodes.
	    // the "NSFilter" class does that massaging.
	    if (name.length () == 0)
		name = local;


	    Element	element;
	    int		length = attrs.getLength ();

	    if (!isL2 || !consumer.isUsingNamespaces ()) {
		element = document.createElement (name);

		// first the explicit attributes ...
		length = attrs.getLength ();
		for (int i = 0; i < length; i++)
		    element.setAttribute (attrs.getQName (i),
					    attrs.getValue (i));
		// ... then any recreated ones (DOM deletes duplicates)
		if (recreatedAttrs) {
		    recreatedAttrs = false;
		    length = attributes.getLength ();
		    for (int i = 0; i < length; i++)
			element.setAttribute (attributes.getQName (i),
						attributes.getValue (i));
		    attributes.clear ();
		}

		top.appendChild (element);
		top = element;
		return;
	    }

	    // For an L2 DOM when namespace use is enabled, use
	    // createElementNS/createAttributeNS except when
	    // (a) it's an element in the default namespace, or
	    // (b) it's an attribute with no prefix
	    String	namespace;
	    
	    if (local.length () != 0)
		namespace = (uri.length () == 0) ? null : uri;
	    else
		namespace = getNamespace (getPrefix (name), attrs);

	    if (namespace == null)
		element = document.createElement (name);
	    else
		element = document.createElementNS (namespace, name);

	    populateAttributes (element, attrs);
	    if (recreatedAttrs) {
		recreatedAttrs = false;
		// ... DOM deletes any duplicates
		populateAttributes (element, attributes);
		attributes.clear ();
	    }

	    top.appendChild (element);
	    top = element;
	}

	private void populateAttributes (Element element, Attributes attrs)
	throws SAXParseException
	{
	    int		length = attrs.getLength ();

	    for (int i = 0; i < length; i++) {
		String	type = attrs.getType (i);
		String	value = attrs.getValue (i);
		String	name = attrs.getQName (i);
		String	local = attrs.getLocalName (i);
		String	uri = attrs.getURI (i);

		// parser discarded basic information, DOM tree isn't writable
		if (name.length () == 0)
		    name = local;

		// all attribute types other than these three may not
		// contain scoped names... enumerated attributes get
		// reported as NMTOKEN, except for NOTATION values
		if (!("CDATA".equals (type)
			|| "NMTOKEN".equals (type)
			|| "NMTOKENS".equals (type))) {
		    if (value.indexOf (':') != -1) {
			namespaceError (
				"namespace nonconformant attribute value: "
				    + "<" + element.getNodeName ()
				    + " " + name + "='" + value + "' ...>");
		    }
		}

		// xmlns="" is legal (undoes default NS)
		// xmlns:foo="" is illegal
		String prefix = getPrefix (name);

		if ("xmlns".equals (prefix) && "".equals (value))
		    namespaceError ("illegal null namespace decl, " + name);

		// no attribute defaulting -- yes, bizarre, but
		// it's what the namespace spec demands.
		String namespace;

		if (prefix == null)
		    namespace = null;
		else if (uri != "" && uri.length () != 0)
		    namespace = uri;
		else
		    namespace = getNamespace (prefix, attrs);

		element.setAttributeNS (namespace, name, value);
	    }
	}

	private String getPrefix (String name)
	{
	    int		temp;

	    if ((temp = name.indexOf (':')) > 0)
		return name.substring (0, temp);
	    return null;
	}

	// used with SAX1-level parser output 
	private String getNamespace (String prefix, Attributes attrs)
	throws SAXParseException
	{
	    String namespace;
	    String decl;

	    // defaulting 
	    if (prefix == null) {
		decl = "xmlns";
		namespace = attrs.getValue (decl);
		if ("".equals (namespace))
		    return null;
		else if (namespace != null)
		    return namespace;

	    // "xmlns" is like a keyword
	    } else if ("xmlns".equals (prefix))
		return null;

	    // "xml" prefix is fixed
	    else if ("xml".equals (prefix))
		return "http://www.w3.org/XML/1998/namespace";

	    // otherwise, expect a declaration
	    else {
		decl = "xmlns:" + prefix;
		namespace = attrs.getValue (decl);
	    }
	    
	    // if we found a local declaration, great
	    if (namespace != null)
		return namespace;


	    // ELSE ... search up the tree we've been building
	    for (Node n = top;
		    n != null && n.getNodeType () != Node.DOCUMENT_NODE;
		    n = (Node) n.getParentNode ()) {
		Element e = (Element) n;
		Attr attr = e.getAttributeNode (decl);
		if (attr != null)
		    return attr.getNodeValue ();
	    }
	    if ("xmlns".equals (decl))
		return null;

	    namespaceError ("Undeclared namespace prefix: " + prefix);
	    return null;
	}

	// SAX2
	public void endElement (String uri, String local, String name)
	throws SAXException
	{
	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly)
	    if (currentEntity != null)
		return;

	    top = top.getParentNode ();
	}

	// SAX1 (mandatory reporting if validating)
	public void ignorableWhitespace (char buf [], int off, int len)
	throws SAXException
	{
	    if (consumer.isHidingWhitespace ())
		return;
	    characters (buf, off, len);
	}

	// SAX2 lexical event
	public void startCDATA ()
	throws SAXException
	{
	    inCDATA = true;
	    // true except for the first fragment of a cdata section
	    mergeCDATA = false;
	}
	
	// SAX2 lexical event
	public void endCDATA ()
	throws SAXException
	{
	    inCDATA = false;
	}
	
	// SAX2 lexical event
	//
	// this SAX2 callback merges two unrelated things:
	//	- Declaration of the root element type ... belongs with
	//    the other DTD declaration methods, NOT HERE.
	//	- IDs for the optional external subset ... belongs here
	//    with other lexical information.
	//
	// ...and it doesn't include the internal DTD subset, desired
	// both to support DOM L2 and to enable "pass through" processing
	//
	public void startDTD (String name, String pubid, String sysid)
	throws SAXException
	{
	    // need to filter out comments and PIs within the DTD
	    inDTD = true;

/*
 * XXX the second DOM L2 "candidate" REC really broke Doctype completely,
 * by saying they may only be used with createDocument, and can't
 * be appended as below ... createDocumentType needs to be a method
 * on Document, since it's an internal typing mechanism not an
 * external typing one.
 *
	    if (isSavingExtraNodes () && isL2) {
		DOMImplementation	impl;
		DocumentType	doctype;

		impl = document.getImplementation ();
		
		// NOTE:  SAX2 isn't currently exposing the internal
		// subset string in a usable form; else we'd really like
		// to store it away here!

		doctype = impl.createDocumentType (name, pubid, sysid, null);
		document.appendChild (doctype);

		// NOTE:  there's no way to attach notation and entity
		// nodes to this DocumentType, or to associate any type
		// information (particularly element and attribute
		// declarations) to it.  Calling it a Document "Type"
		// node, in L1 or L2 of DOM, is an extreme misnomer.
		// Real DTD support could be useful; what DOM has, not.
	    }
*/
	}
	
	// SAX2 lexical event
	public void endDTD ()
	throws SAXException
	{
	    inDTD = false;
	}
	
	// SAX2 lexical event
	public void comment (char buf [], int off, int len)
	throws SAXException
	{
	    Node	comment;

	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly)
	    if (consumer.isHidingComments ()
		    || inDTD
		    || currentEntity != null)
		return;
	    comment = document.createComment (new String (buf, off, len));
	    top.appendChild (comment);
	}

	// SAX2 lexical event
	public void startEntity (String name)
	throws SAXException
	{
	    // Avoid creating entity ref nodes
	    if (consumer.isExpandingReferences ())
		return;

	    // we can't create populated entity ref nodes using
	    // only public DOM APIs (they've got to be readonly)
	    if (currentEntity != null)
		return;

	    // SAX2 shows parameter entities; DOM doesn't care
	    if (name.charAt (0) == '%' || "[dtd]".equals (name))
		return;

	    // Since we can't create a populated entity ref node in any
	    // standard way, we create an unpopulated one and hope that
	    // the DOM at least created it readonly (spec requires r/o).
	    //
	    // NOTE:  if we wanted to try populating it, we'd push it on the
	    // stack and pop it with the matching endEntity call.
	    EntityReference ref = document.createEntityReference (name);
	    top.appendChild (ref);

	    // arrange to ignore all events till end of this entity.
	    currentEntity = name;
	}

	// SAX2 lexical event
	public void endEntity (String name)
	throws SAXException
	{
	    if (consumer.isExpandingReferences () || currentEntity == null)
		return;
	    if (currentEntity.equals (name))
		currentEntity = null;
	}


	// SAX1 DTD event
	public void notationDecl (
	    String name,
	    String pubid, String sysid
	) throws SAXException
	{
	    /* IGNORE -- no public DOM API lets us store these
	     * into the doctype node
	     */
	}

	// SAX1 DTD event
	public void unparsedEntityDecl (
	    String name,
	    String pubid, String sysid,
	    String notation
	) throws SAXException
	{
	    /* IGNORE -- no public DOM API lets us store these
	     * into the doctype node
	     */
	}

	// SAX2 declaration event
	public void elementDecl (String name, String model)
	throws SAXException
	{
	    /* IGNORE -- no content model support in DOM L2 */
	}

	// SAX2 declaration event
	public void attributeDecl (
	    String element,
	    String attr,
	    String x,
	    String y,
	    String z
	) throws SAXException
	{
	    /* IGNORE -- no content model support in DOM L2 */
	}

	// SAX2 declaration event
	public void internalEntityDecl (String name, String value)
	throws SAXException
	{
	    /* IGNORE -- no public DOM API lets us store these
	     * into the doctype node
	     */
	}

	// SAX2 declaration event
	public void externalEntityDecl (String name, String pubid, String sysid)
	throws SAXException
	{
	    /* IGNORE -- no public DOM API lets us store these
	     * into the doctype node
	     */
	}

	//
	// These really should offer the option of nonfatal handling,
	// like other validity errors, though that would cause major
	// chaos in the DOM data structures.  DOM is already spec'd
	// to treat many of these as fatal, so this is consistent.
	//
	private void namespaceError (String description)
	throws SAXParseException
	{
	    SAXParseException err;
	    
	    err = new SAXParseException (description, locator);
	    throw err;
	}
    }
}
