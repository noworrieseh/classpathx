/*
 * $Id: SAXDriver.java,v 1.12 2001-07-31 06:42:30 db Exp $
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

//
// Copyright (c) 1998 by Microstar Software Ltd.
// From Microstar's README (the entire original license):
//
// AElfred is free for both commercial and non-commercial use and
// redistribution, provided that Microstar's copyright and disclaimer are
// retained intact.  You are free to modify AElfred for your own use and
// to redistribute AElfred with your modifications, provided that the
// modifications are clearly documented.
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// merchantability or fitness for a particular purpose.  Please use it AT
// YOUR OWN RISK.
//


package gnu.xml.aelfred2;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.Stack;

// maintaining 1.1 compatibility for now ...
// Iterator, Hashmap and ArrayList ought to be faster
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.NamespaceSupport;

import gnu.xml.util.DefaultHandler;


// $Id: SAXDriver.java,v 1.12 2001-07-31 06:42:30 db Exp $

/**
 * An enhanced SAX2 version of Microstar's &AElig;lfred XML parser.
 * The enhancements primarily relate to significant improvements in
 * conformance to the XML specification, and SAX2 support.  Performance
 * has been improved.  However, the &AElig;lfred proprietary APIs are
 * no longer public.  See the package level documentation for more
 * information.
 *
 * <table border="1" width='100%' cellpadding='3' cellspacing='0'>
 * <tr bgcolor='#ccccff'>
 *	<th><font size='+1'>Name</font></th>
 *	<th><font size='+1'>Notes</font></th></tr>
 *
 * <tr><td colspan=2><center><em>Features ... URL prefix is
 * <b>http://xml.org/sax/features/</b></em></center></td></tr>
 *
 * <tr><td>(URL)/external-general-entities</td>
 *	<td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/external-parameter-entities</td>
 *	<td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/namespace-prefixes</td>
 *	<td>Value defaults to <em>false</em> (but XML 1.0 names are
 *		always reported)</td></tr>
 * <tr><td>(URL)/lexical-handler/parameter-entities</td>
 *	<td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/namespaces</td>
 *	<td>Value defaults to <em>true</em></td></tr>
 * <tr><td>(URL)/string-interning</td>
 *	<td>Value is fixed at <em>true</em></td></tr>
 * <tr><td>(URL)/validation</td>
 *	<td>Value is fixed at <em>false</em></td></tr>
 *
 * <tr><td colspan=2><center><em>Handler Properties ... URL prefix is
 * <b>http://xml.org/sax/properties/</b></em></center></td></tr>
 *
 * <tr><td>(URL)/declaration-handler</td>
 *	<td>A declaration handler may be provided.  Declaration of general
 *	entities is exposed, but not parameter entities; none of the entity
 *	names reported here will begin with "%". </td></tr>
 * <tr><td>(URL)/lexical-handler</td>
 *	<td>A lexical handler may be provided.  Entity boundaries and
 *	comments are not exposed; only CDATA sections and the start/end of
 *	the DTD (the internal subset is not detectible). </td></tr>
 * </table>
 *
 * <p>This parser currently implements the SAX1 Parser API, but
 * it may not continue to do so in the future.
 *
 * @author Written by David Megginson &lt;dmeggins@microstar.com&gt;
 *	(version 1.2a from Microstar)
 * @author Updated by David Brownell &lt;dbrownell@users.sourceforge.net&gt;
 * @version $Date: 2001-07-31 06:42:30 $
 * @see org.xml.sax.Parser
 */
final public class SAXDriver
    implements Locator, Attributes, XMLReader, Parser, AttributeList
{
    private final DefaultHandler	base = new DefaultHandler ();
    private XmlParser			parser;

    private EntityResolver		entityResolver = base;
    private ContentHandler		contentHandler = base;
    private DTDHandler			dtdHandler = base;
    private ErrorHandler 		errorHandler = base;
    private DeclHandler			declHandler = base;
    private LexicalHandler		lexicalHandler = base;

    private String			elementName = null;
    private Stack			entityStack = new Stack ();

    private Vector			attributeNames = new Vector ();
    private Vector			attributeNamespaces = new Vector ();
    private Vector			attributeLocalNames = new Vector ();
    private Vector			attributeValues = new Vector ();

    private boolean			namespaces = true;
    private boolean			xmlNames = false;

    private int				attributeCount = 0;
    private boolean			attributes;
    private String			nsTemp [] = new String [3];
    private NamespaceSupport		prefixStack = new NamespaceSupport ();

    //
    // Constructor.
    //

    /** Constructs a SAX Parser.  */
    public SAXDriver () {}


    //
    // Implementation of org.xml.sax.Parser.
    //

    /**
     * <b>SAX1</b>: Sets the locale used for diagnostics; currently,
     * only locales using the English language are supported.
     * @param locale The locale for which diagnostics will be generated
     */
    public void setLocale (Locale locale)
    throws SAXException
    {
	if ("en".equals (locale.getLanguage ()))
	    return ;

	throw new SAXException ("AElfred only supports English locales.");
    }


    /**
     * <b>SAX2</b>: Returns the object used when resolving external
     * entities during parsing (both general and parameter entities).
     */
    public EntityResolver getEntityResolver ()
    {
	return entityResolver;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the entity resolver for this parser.
     * @param handler The object to receive entity events.
     */
    public void setEntityResolver (EntityResolver resolver)
    {
	if (resolver == null)
	    resolver = base;
	this.entityResolver = resolver;
    }


    /**
     * <b>SAX2</b>: Returns the object used to process declarations related
     * to notations and unparsed entities.
     */
    public DTDHandler getDTDHandler ()
    {
	return dtdHandler;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the DTD handler for this parser.
     * @param handler The object to receive DTD events.
     */
    public void setDTDHandler (DTDHandler handler)
    {
	if (handler == null)
	    handler = base;
	this.dtdHandler = handler;
    }


    /**
     * <b>SAX1</b>: Set the document handler for this parser.  If a
     * content handler was set, this document handler will supplant it.
     * The parser is set to report all XML 1.0 names rather than to
     * filter out "xmlns" attributes (the "namespace-prefixes" feature
     * is set to true).
     *
     * @deprecated SAX2 programs should use the XMLReader interface
     *	and a ContentHandler.
     *
     * @param handler The object to receive document events.
     */
    public void setDocumentHandler (DocumentHandler handler)
    {
	contentHandler = new Adapter (handler);
	xmlNames = true;
    }

    /**
     * <b>SAX2</b>: Returns the object used to report the logical
     * content of an XML document.
     */
    public ContentHandler getContentHandler ()
    {
	return contentHandler;
    }

    /**
     * <b>SAX2</b>: Assigns the object used to report the logical
     * content of an XML document.  If a document handler was set,
     * this content handler will supplant it (but XML 1.0 style name
     * reporting may remain enabled).
     */
    public void setContentHandler (ContentHandler handler)
    {
	if (handler == null)
	    handler = base;
	contentHandler = handler;
    }

    /**
     * <b>SAX1, SAX2</b>: Set the error handler for this parser.
     * @param handler The object to receive error events.
     */
    public void setErrorHandler (ErrorHandler handler)
    {
	if (handler == null)
	    handler = base;
	this.errorHandler = handler;
    }

    /**
     * <b>SAX2</b>: Returns the object used to receive callbacks for XML
     * errors of all levels (fatal, nonfatal, warning); this is never null;
     */
    public ErrorHandler getErrorHandler ()
	{ return errorHandler; }


    /**
     * <b>SAX1, SAX2</b>: Auxiliary API to parse an XML document, used mostly
     * when no URI is available.
     * If you want anything useful to happen, you should set
     * at least one type of handler.
     * @param source The XML input source.  Don't set 'encoding' unless
     *	you know for a fact that it's correct.
     * @see #setEntityResolver
     * @see #setDTDHandler
     * @see #setContentHandler
     * @see #setErrorHandler
     * @exception SAXException The handlers may throw any SAXException,
     *	and the parser normally throws SAXParseException objects.
     * @exception IOException IOExceptions are normally through through
     *	the parser if there are problems reading the source document.
     */
    public void parse (InputSource source)
    throws SAXException, IOException
    {
	synchronized (base) {
	    parser = new XmlParser ();
	    parser.setHandler (this);

	    try {
		String	systemId = source.getSystemId ();

		if (source.getByteStream () != null
			&& source.getCharacterStream () != null)
		    fatal ("resolveEntity() returned two streams");
		parser.doParse (systemId,
			      source.getPublicId (),
			      source.getCharacterStream (),
			      source.getByteStream (),
			      source.getEncoding ());
	    } catch (SAXException e) {
		throw e;
	    } catch (IOException e) {
		throw e;
	    } catch (RuntimeException e) {
		throw e;
	    } catch (Exception e) {
		throw new SAXParseException (e.getMessage (), this, e);
	    } finally {
		contentHandler.endDocument ();
		entityStack.removeAllElements ();
		parser = null;
	    }
	}
    }


    /**
     * <b>SAX1, SAX2</b>: Preferred API to parse an XML document, using a
     * system identifier (URI).
     */
    public void parse (String systemId)
    throws SAXException, IOException
    {
	parse (new InputSource (systemId));
    }

    //
    // Implementation of SAX2 "XMLReader" interface
    //
    static final String	FEATURE = "http://xml.org/sax/features/";
    static final String	PROPERTY = "http://xml.org/sax/properties/";

    /**
     * <b>SAX2</b>: Tells the value of the specified feature flag.
     *
     * @exception SAXNotRecognizedException thrown if the feature flag
     *	is neither built in, nor yet assigned.
     */
    public boolean getFeature (String featureId)
    throws SAXNotRecognizedException
    {
	if ((FEATURE + "validation").equals (featureId))
	    return false;

	// external entities (both types) are currently always included
	if ((FEATURE + "external-general-entities").equals (featureId)
		|| (FEATURE + "external-parameter-entities")
		    .equals (featureId))
	    return true;

	// element/attribute names are as written in document; no mangling
	if ((FEATURE + "namespace-prefixes").equals (featureId))
	    return xmlNames;

	// report element/attribute namespaces?
	if ((FEATURE + "namespaces").equals (featureId))
	    return namespaces;

	// all PEs and GEs are reported
	if ((FEATURE + "lexical-handler/parameter-entities").equals (featureId))
	    return true;

	// always interns
	if ((FEATURE + "string-interning").equals (featureId))
	    return true;

	throw new SAXNotRecognizedException (featureId);
    }

    // package private
    DeclHandler getDeclHandler () { return declHandler; }

    /**
     * <b>SAX2</b>:  Returns the specified property.
     *
     * @exception SAXNotRecognizedException thrown if the property value
     *	is neither built in, nor yet stored.
     */
    public Object getProperty (String propertyId)
    throws SAXNotRecognizedException
    {
	if ((PROPERTY + "declaration-handler").equals (propertyId))
	    return declHandler;

	if ((PROPERTY + "lexical-handler").equals (propertyId))
	    return lexicalHandler;
	
	// unknown properties
	throw new SAXNotRecognizedException (propertyId);
    }

    /**
     * <b>SAX2</b>:  Sets the state of feature flags in this parser.  Some
     * built-in feature flags are mutable.
     */
    public void setFeature (String featureId, boolean state)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
	boolean	value;
	
	// Features with a defined value, we just change it if we can.
	value = getFeature (featureId);

	if (state == value)
	    return;
	if (parser != null)
	    throw new SAXNotSupportedException ("not while parsing");

	if ((FEATURE + "namespace-prefixes").equals (featureId)) {
	    // in this implementation, this only affects xmlns reporting
	    xmlNames = state;
	    // forcibly prevent illegal parser state
	    if (!xmlNames)
		namespaces = true;
	    return;
	}

	if ((FEATURE + "namespaces").equals (featureId)) {
	    namespaces = state;
	    // forcibly prevent illegal parser state
	    if (!namespaces)
		xmlNames = true;
	    return;
	}

	throw new SAXNotSupportedException (featureId);
    }

    /**
     * <b>SAX2</b>:  Assigns the specified property.  Like SAX1 handlers,
     * these may be changed at any time.
     */
    public void setProperty (String propertyId, Object property)
    throws SAXNotRecognizedException, SAXNotSupportedException
    {
	// see if the property is recognized
	getProperty (propertyId);

	// Properties with a defined value, we just change it if we can.

	if ((PROPERTY + "declaration-handler").equals (propertyId)) {
	    if (property == null)
		declHandler = base;
	    else if (! (property instanceof DeclHandler))
		throw new SAXNotSupportedException (propertyId);
	    else
		declHandler = (DeclHandler) property;
	    return ;
	}

	if ((PROPERTY + "lexical-handler").equals (propertyId)) {
	    if (property == null)
		lexicalHandler = base;
	    else if (! (property instanceof LexicalHandler))
		throw new SAXNotSupportedException (propertyId);
	    else
		lexicalHandler = (LexicalHandler) property;
	    return ;
	}

	throw new SAXNotSupportedException (propertyId);
    }


    //
    // This is where the driver receives XmlParser callbacks and translates
    // them into SAX callbacks.  Some more callbacks have been added for
    // SAX2 support.
    //

    void startDocument ()
    throws SAXException
    {
	contentHandler.setDocumentLocator (this);
	contentHandler.startDocument ();
	attributeNames.removeAllElements ();
	attributeValues.removeAllElements ();
    }

    Object resolveEntity (String publicId, String systemId)
    throws SAXException, IOException
    {
	InputSource source = entityResolver.resolveEntity (publicId,
			     systemId);

	if (source == null) {
	    return null;
	} else if (source.getCharacterStream () != null) {
	    if (source.getByteStream () != null)
		fatal ("resolveEntity() returned two streams");
	    return source.getCharacterStream ();
	} else if (source.getByteStream () != null) {
	    if (source.getEncoding () == null)
		return source.getByteStream ();
	    else try {
		return new InputStreamReader (
		    source.getByteStream (),
		    source.getEncoding ()
		    );
	    } catch (IOException e) {
		return source.getByteStream ();
	    }
	} else {
	    return source.getSystemId ();
	}
	// XXX no way to tell AElfred about new public
	// or system ids ... so relative URL resolution
	// through that entity could be less than reliable.
    }


    void startExternalEntity (String name, String systemId)
    throws SAXException
    {
	if (!"[document]".equals (name))
	    lexicalHandler.startEntity (name);
	else if (systemId == null)
	    warn ("document URI was not reported to parser");
	entityStack.push (systemId);
    }

    void endExternalEntity (String name)
    throws SAXException
    {
	entityStack.pop ();
	if (!"[document]".equals (name))
	    lexicalHandler.endEntity (name);
    }

    void startInternalEntity (String name)
    throws SAXException
    {
	lexicalHandler.startEntity (name);
    }

    void endInternalEntity (String name)
    throws SAXException
    {
	lexicalHandler.endEntity (name);
    }

    void doctypeDecl (String name, String publicId, String systemId)
    throws SAXException
    {
	lexicalHandler.startDTD (name, publicId, systemId);
	
	// ... the "name" is a declaration and should be given
	// to the DeclHandler (but sax2 doesn't).

	// the IDs for the external subset are lexical details,
	// as are the contents of the internal subset; but sax2
	// doesn't provide the internal subset "pre-parse"
    }

    void endDoctype ()
    throws SAXException
    {
	lexicalHandler.endDTD ();
    }

    private void declarePrefix (String prefix, String uri)
    throws SAXException
    {
	int index = uri.indexOf (':');

	// many versions of nwalsh docbook stylesheets 
	// have bogus URLs; so this can't be an error...
	if (index < 1 && uri.length () != 0)
	    warn ("relative URI for namespace: " + uri);

	// FIXME:  char [0] must be ascii alpha; chars [1..index]
	// must be ascii alphanumeric or in "+-." [RFC 2396]

	uri = uri.intern ();
	prefixStack.declarePrefix (prefix, uri);
	contentHandler.startPrefixMapping (prefix, uri);
    }

// FIXME  SAX2 has no way to say which attributes are specified

    void attribute (String qname, String value, boolean isSpecified)
    throws SAXException
    {
	if (!attributes) {
	    attributes = true;
	    if (namespaces)
		prefixStack.pushContext ();
	}

	// process namespace decls immediately;
	// then maybe forget this as an attribute
	if (namespaces) {
	    int	index;

	    // default NS declaration?
	    if ("xmlns" == qname) {
		declarePrefix ("", value);
		if (!xmlNames)
		    return;
	    }

	    // NS prefix declaration?
	    else if ((index = qname.indexOf (':')) == 5
		    && qname.startsWith ("xmlns")) {
		String		prefix = qname.substring (index + 1);

		if (value.length () == 0) {
		    verror ("missing URI in namespace decl attribute: "
				+ qname);
		} else
		    declarePrefix (prefix, value);
		if (!xmlNames)
		    return;
	    }
	}

	// remember this attribute ...
	attributeCount++;
	attributeNames.addElement (qname);
	// attribute type comes from querying parser's DTD records
	attributeValues.addElement (value);

	// ... patching {lname, uri} later, if needed
	attributeNamespaces.addElement ("");
	attributeLocalNames.addElement ("");
    }

    void startElement (String elname)
    throws SAXException
    {
	ContentHandler handler = contentHandler;

	//
	// NOTE:  this implementation of namespace support adds something
	// like six percent to parsing CPU time, in a large (~50 MB)
	// document that doesn't use namespaces at all.  (Measured by PC
	// sampling, with a bug where endElement processing was omitted.)
	// [Measurement referred to older implementation, older JVM ...]
	//
	// It ought to become notably faster in such cases.  Most
	// costs are the prefix stack calling Hashtable.get() (2%),
	// String.hashCode() (1.5%) and about 1.3% each for pushing
	// the context, and two chunks of name processing.
	//

	if (!attributes)
	    prefixStack.pushContext ();
	else if (namespaces) {

	    // now we can patch up namespace refs; we saw all the
	    // declarations, so now we'll do the Right Thing
	    for (int i = 0; i < attributeCount; i++) {
		String	qname = (String) attributeNames.elementAt (i);
		int	index;

		// default NS declaration?
		if ("xmlns" == qname)
		    continue;

		index = qname.indexOf (':');

		// NS prefix declaration?
		if (index == 5 && qname.startsWith ("xmlns"))
		    continue;

		// it's not a NS decl; patch namespace info items
		if (prefixStack.processName (qname, nsTemp, true) == null)
		    verror ("undeclared attribute prefix in: " + qname);
		else {
		    attributeNamespaces.setElementAt (nsTemp [0], i);
		    attributeLocalNames.setElementAt (nsTemp [1], i);
		}
	    }
	}

	// save element name so attribute callbacks work
	elementName = elname;
	if (namespaces) {
	    if (prefixStack.processName (elname, nsTemp, false) == null) {
		verror ("undeclared element prefix in: " + elname);
		nsTemp [0] = nsTemp [1] = "";
	    }
	    handler.startElement (nsTemp [0], nsTemp [1], elname, this);
	} else
	    handler.startElement ("", "", elname, this);
	// elementName = null;

	// elements with no attributes are pretty common!
	if (attributes) {
	    attributeNames.removeAllElements ();
	    attributeNamespaces.removeAllElements ();
	    attributeLocalNames.removeAllElements ();
	    attributeValues.removeAllElements ();
	    attributeCount = 0;
	    attributes = false;
	}
    }

    void endElement (String elname)
    throws SAXException
    {
	ContentHandler	handler = contentHandler;

	if (!namespaces) {
	    handler.endElement ("", "", elname);
	    return;
	}
	prefixStack.processName (elname, nsTemp, false);
	handler.endElement (nsTemp [0], nsTemp [1], elname);

	Enumeration	prefixes = prefixStack.getDeclaredPrefixes ();

	while (prefixes.hasMoreElements ())
	    handler.endPrefixMapping ((String) prefixes.nextElement ());
	prefixStack.popContext ();
    }

    void startCDATA ()
    throws SAXException
    {
	lexicalHandler.startCDATA ();
    }

    void charData (char ch[], int start, int length)
    throws SAXException
    {
	contentHandler.characters (ch, start, length);
    }

    void endCDATA ()
    throws SAXException
    {
	lexicalHandler.endCDATA ();
    }

    void ignorableWhitespace (char ch[], int start, int length)
    throws SAXException
    {
	contentHandler.ignorableWhitespace (ch, start, length);
    }

    void processingInstruction (String target, String data)
    throws SAXException
    {
	// XXX if within DTD, perhaps it's best to discard
	// PIs since the decls to which they (usually)
	// apply get significantly rearranged

	contentHandler.processingInstruction (target, data);
    }

    void comment (char ch[], int start, int length)
    throws SAXException
    {
	// XXX if within DTD, perhaps it's best to discard
	// comments since the decls to which they (usually)
	// apply get significantly rearranged

	if (lexicalHandler != base)
	    lexicalHandler.comment (ch, start, length);
    }

    void fatal (String message)
    throws SAXException
    {
	SAXParseException fatal;
	
	fatal = new SAXParseException (message, this);
	errorHandler.fatalError (fatal);

	// Even if the application can continue ... we can't!
	throw fatal;
    }

    // We can safely report a few validity errors that
    // make layered SAX2 DTD validation more conformant
    void verror (String message)
    throws SAXException
    {
	SAXParseException err;
	
	err = new SAXParseException (message, this);
	errorHandler.error (err);
    }

    void warn (String message)
    throws SAXException
    {
	SAXParseException err;
	
	err = new SAXParseException (message, this);
	errorHandler.warning (err);
    }


    //
    // Before the endDtd event, deliver all non-PE declarations.
    //

    //
    // Implementation of org.xml.sax.Attributes.
    //

    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public int getLength ()
    {
	return attributeNames.size ();
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getURI (int index)
    {
	return (String) (attributeNamespaces.elementAt (index));
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getLocalName (int index)
    {
	return (String) (attributeLocalNames.elementAt (index));
    }

    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getQName (int i)
    {
	return (String) (attributeNames.elementAt (i));
    }

    /**
     * <b>SAX1 AttributeList</b> method (don't invoke on parser);
     */
    public String getName (int i)
    {
	return (String) (attributeNames.elementAt (i));
    }

    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getType (int i)
    {
	String	type = parser.getAttributeType (elementName, getQName (i));
	if (type == null)
	    return "CDATA";
	// ... use DeclHandler.attributeDecl to see enumerations
	if (type == "ENUMERATED")
	    return "NMTOKEN";
	return type;
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getValue (int i)
    {
	return (String) (attributeValues.elementAt (i));
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public int getIndex (String uri, String local)
    {
	int length = getLength ();

	for (int i = 0; i < length; i++) {
	    if (!getURI (i).equals (uri))
		continue;
	    if (getLocalName (i).equals (local))
		return i;
	}
	return -1;
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public int getIndex (String xmlName)
    {
	int length = getLength ();

	for (int i = 0; i < length; i++) {
	    if (getQName (i).equals (xmlName))
		return i;
	}
	return -1;
    }


    /**
     * <b>SAX2 Attributes</b> method (don't invoke on parser);
     */
    public String getType (String uri, String local)
    {
	int index = getIndex (uri, local);

	if (index < 0)
	    return null;
	return getType (index);
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getType (String xmlName)
    {
	int index = getIndex (xmlName);

	if (index < 0)
	    return null;
	return getType (index);
    }


    /**
     * <b>SAX Attributes</b> method (don't invoke on parser);
     */
    public String getValue (String uri, String local)
    {
	int index = getIndex (uri, local);

	if (index < 0)
	    return null;
	return getValue (index);
    }


    /**
     * <b>SAX1 AttributeList, SAX2 Attributes</b> method
     * (don't invoke on parser);
     */
    public String getValue (String xmlName)
    {
	int index = getIndex (xmlName);

	if (index < 0)
	    return null;
	return getValue (index);
    }


    //
    // Implementation of org.xml.sax.Locator.
    //

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public String getPublicId ()
    {
	return null; 		// XXX track public IDs too
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public String getSystemId ()
    {
	if (entityStack.empty ())
	    return null;
	else
	    return (String) entityStack.peek ();
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public int getLineNumber ()
    {
	return parser.getLineNumber ();
    }

    /**
     * <b>SAX Locator</b> method (don't invoke on parser);
     */
    public int getColumnNumber ()
    {
	return parser.getColumnNumber ();
    }

    // adapter between content handler and document handler callbacks
    private static class Adapter implements ContentHandler
    {
	private DocumentHandler		docHandler;

	Adapter (DocumentHandler dh)
	    { docHandler = dh; }


	public void setDocumentLocator (Locator l)
	    { docHandler.setDocumentLocator (l); }
	
	public void startDocument () throws SAXException
	    { docHandler.startDocument (); }
	
	public void processingInstruction (String target, String data)
	throws SAXException
	    { docHandler.processingInstruction (target, data); }
	
	public void startPrefixMapping (String prefix, String uri)
	    { /* ignored */ }

	public void startElement (
	    String	namespace,
	    String	local,
	    String	name,
	    Attributes	attrs
	) throws SAXException
	    { docHandler.startElement (name, (AttributeList) attrs); }

	public void characters (char buf [], int offset, int len)
	throws SAXException
	    { docHandler.characters (buf, offset, len); }

	public void ignorableWhitespace (char buf [], int offset, int len)
	throws SAXException
	    { docHandler.ignorableWhitespace (buf, offset, len); }

	public void skippedEntity (String name)
	    { /* ignored */ }

	public void endElement (String u, String l, String name)
	throws SAXException
	    { docHandler.endElement (name); }

	public void endPrefixMapping (String prefix)
	    { /* ignored */ }

	public void endDocument () throws SAXException
	    { docHandler.endDocument (); }
    }
}
