/*
 * $Id: XIncludeFilter.java,v 1.1 2001-10-15 02:12:57 db Exp $
 * Copyright (C) 2001 David Brownell
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL; 
import java.net.URLConnection; 
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import gnu.xml.util.Resolver;



// $Id: XIncludeFilter.java,v 1.1 2001-10-15 02:12:57 db Exp $

/**
 * Filter to process an XPointer-free subset of
 * <a href="http://www.w3.org/TR/xinclude">XInclude</a>, supporting its
 * use as a kind of replacement for parsed general entities.
 * XInclude works much like the <code>#include</code> of C/C++ but
 * works for XML documents as well as unparsed text files.
 * Restrictions from the 16-May-2001 draft of XInclude are as follows:
 *
 * <ul>
 *
 * <li> URIs must not include fragment identifiers.  XInclude (current WDs)
 * specifies the use of XPointer, which is a high overhead non-streaming API.
 *
 * <li> DTDs are not supported in included files, since the SAX DTD events
 * must have completely preceded any included file.  Also, the XInclude draft
 * is incomplete in that area, because it doesn't say how it expects to
 * "merge" DTD portions of the infoset with conflicting declarations.
 *
 * </ul>
 *
 * <p> XML documents that are included will normally be processed using
 * the default SAX namespace rules, meaning that prefix information may
 * be discarded.  This may be changed with {@link #setSavingPrefixes
 * setSavingPrefixes()}.
 *
 * <p>TBD: "IURI" handling.
 *
 * @author David Brownell
 * @version $Date: 2001-10-15 02:12:57 $
 */
public class XIncludeFilter extends EventFilter implements Locator
{
    private Hashtable		extEntities = new Hashtable (5, 5);
    private int			ignoreCount;
    private Locator		locator;
    private Stack		uris = new Stack ();
    private Vector		inclusions = new Vector (5, 5);
    private boolean		savingPrefixes;

    /**
     */
    public XIncludeFilter (EventConsumer next)
    throws SAXException
    {
	super (next);
	setContentHandler (this);
	// DTDHandler callbacks pass straight through
	setProperty (DECL_HANDLER, this);
	setProperty (LEXICAL_HANDLER, this);
    }

    private void fatal (SAXParseException e) throws SAXException
    {
	ErrorHandler		eh;
	
	eh = getErrorHandler ();
	if (eh != null)
	    eh.fatalError (e);
	throw e;
    }

    /**
     * Passes "this" down the filter chain as a proxy locator.
     */
    public void setDocumentLocator (Locator locator)
    {
	this.locator = locator;
	super.setDocumentLocator (this);
    }

    public String getSystemId ()
	{ return (locator == null) ? null : locator.getSystemId (); }
    public String getPublicId ()
	{ return (locator == null) ? null : locator.getPublicId (); }
    public int getLineNumber ()
	{ return (locator == null) ? -1 : locator.getLineNumber (); }
    public int getColumnNumber ()
	{ return (locator == null) ? -1 : locator.getColumnNumber (); }

    /**
     * Assigns the flag controlling the setting of the SAX2
     * <em>namespace-prefixes</em> flag.
     */
    public void setSavingPrefixes (boolean flag)
	{ savingPrefixes = flag; }

    /**
     * Returns the flag controlling the setting of the SAX2
     * <em>namespace-prefixes</em> flag when parsing included documents.
     * The default value is the SAX2 default (false), which discards
     * information that can be useful.
     */
    public boolean isSavingPrefixes ()
	{ return savingPrefixes; }

    //
    // Two mechanisms are interacting here.
    // 
    //	- XML Base implies a stack of base URIs, updated both by
    //	  "real entity" boundaries and element boundaries.
    //
    //	- Active "Real Entities" (for document and general entities,
    //	  and by xincluded files) are tracked to prevent circular
    //	  inclusions.
    //
    private String addMarker (String uri)
    throws SAXException
    {
	if (locator != null && locator.getSystemId () != null)
	    uri = locator.getSystemId ();

	// guard against InputSource objects without system IDs
	if (uri == null)
	    fatal (new SAXParseException ("Entity URI is unknown", locator));

	try {
	    URL	url = new URL (uri);

	    uri = url.toString ();
	    if (inclusions.contains (uri))
		fatal (new SAXParseException (
			"XInclude, circular inclusion", locator));
	    inclusions.addElement (uri);
	    uris.push (url);
	} catch (IOException e) {
	    // guard against illegal relative URIs (Xerces)
	    fatal (new SAXParseException ("parser bug: relative URI",
		locator, e));
	}
	return uri;
    }

    private void pop (String uri)
    {
	inclusions.removeElement (uri);
	uris.pop ();
    }

    //
    // Document entity boundaries get both treatments.
    //
    public void startDocument () throws SAXException
    {
	ignoreCount = 0;
	addMarker (null);
	super.startDocument ();
    }

    public void endDocument () throws SAXException
    {
	inclusions.clear ();
	extEntities.clear ();
	uris.clear ();
	super.endDocument ();
    }

    //
    // External general entity boundaries get both treatments.
    //
    public void externalEntityDecl (String name, String pubId, String sysId)
    throws SAXException
    {
	if (name.charAt (0) == '%')
	    return;
	try {
	    URL	url = new URL (locator.getSystemId ());
	    sysId = new URL (url, sysId).toString ();
	} catch (IOException e) {
	    // what could we do?
	}
	extEntities.put (name, sysId);
    }

    public void startEntity (String name)
    throws SAXException
    {
	if (ignoreCount != 0) {
	    ignoreCount++;
	    return;
	}

	String	uri = (String) extEntities.get (name);
	if (uri != null)
	    addMarker (uri);
	super.startEntity (name);
    }

    public void endEntity (String name)
    throws SAXException
    {
	if (ignoreCount != 0) {
	    if (--ignoreCount != 0)
		return;
	}

	String	uri = (String) extEntities.get (name);

	if (uri != null)
	    pop (uri);
	super.endEntity (name);
    }
    
    //
    // element boundaries only affect the base URI stack,
    // unless they're XInclude elements.
    //
    public void
    startElement (String uri, String local, String qName, Attributes atts)
    throws SAXException
    {
	if (ignoreCount != 0) {
	    ignoreCount++;
	    return;
	}

	URL	baseURI = (URL) uris.peek ();
	String	base;

	base = atts.getValue ("http://www.w3.org/XML/1998/namespace", "base");
	if (base == null)
	    uris.push (baseURI);
	else {
	    URL		url;

	    if (base.indexOf ('#') != -1)
		fatal (new SAXParseException (
		    "xml:base with fragment: " + base,
		    locator));

	    try {
		baseURI = new URL (baseURI, base);
		uris.push (baseURI);
	    } catch (Exception e) {
		fatal (new SAXParseException (
		    "xml:base with illegal uri: " + base,
		    locator, e));
	    }
	}

	if ("http://www.w3.org/2001/XInclude".equals (uri)
		&& "include".equals (local)) {
	    String	href = atts.getValue ("href");
	    String	parse = atts.getValue ("parse");
	    String	encoding = atts.getValue ("encoding");
	    URL		url = (URL) uris.peek ();

	    if (href == null)
		fatal (new SAXParseException (
		    "XInclude missing href",
		    locator));
	    if (href.indexOf ('#') != -1)
		fatal (new SAXParseException (
		    "XInclude with fragment: " + href,
		    locator));

	    if (parse == null || "xml".equals (parse))
		xinclude (url, href);
	    else if ("text".equals (parse))
		readText (url, href, encoding);
	    else {
		ErrorHandler	eh = getErrorHandler ();

		if (eh != null)
		    eh.error (new SAXParseException (
			"unknown XInclude parsing rule: " + parse,
			locator));
	    }

	    // strip out all included content
	    ignoreCount++;

	} else
	    super.startElement (uri, local, qName, atts);
    }

    public void endElement (String uri, String local, String qName)
    throws SAXException
    {
	if (ignoreCount != 0) {
	    if (--ignoreCount != 0)
		return;
	}

	uris.pop ();
	if (!("http://www.w3.org/2001/XInclude".equals (uri)
		&& "include".equals (local)))
	    super.endElement (uri, local, qName);
    }

    //
    // ignore all content within non-empty xi:include elements
    //
    public void characters (char buf [], int offset, int len)
    throws SAXException
    {
	if (ignoreCount == 0)
	    super.characters (buf, offset, len);
    }

    public void processingInstruction (String target, String value)
    throws SAXException
    {
	if (ignoreCount == 0)
	    super.processingInstruction (target, value);
    }

    public void ignorableWhitespace (char buf [], int offset, int len)
    throws SAXException
    {
	if (ignoreCount == 0)
	    super.ignorableWhitespace (buf, offset, len);
    }

    public void comment (char buf [], int offset, int len)
    throws SAXException
    {
	if (ignoreCount == 0)
	    super.comment (buf, offset, len);
    }

    public void startCDATA () throws SAXException
    {
	if (ignoreCount == 0)
	    super.startCDATA ();
    }

    public void endCDATA () throws SAXException
    {
	if (ignoreCount == 0)
	    super.endCDATA ();
    }

    public void startPrefixMapping (String prefix, String uri)
    throws SAXException
    {
	if (ignoreCount == 0)
	    super.startPrefixMapping (prefix, uri);
    }

    public void endPrefixMapping (String prefix) throws SAXException
    {
	if (ignoreCount == 0)
	    super.endPrefixMapping (prefix);
    }

    public void skippedEntity (String name) throws SAXException
    {
	if (ignoreCount == 0)
	    super.skippedEntity (name);
    }

    //
    // for XIncluded entities, manage the current locator and
    // filter out events that would be incorrect to report
    //
    private class Scrubber extends EventFilter
    {
	Scrubber (EventFilter f)
	throws SAXException
	{
	    // delegation passes to next in chain
	    super (f);

	    // process all content events
	    setContentHandler (this);
	    setProperty (LEXICAL_HANDLER, this);

	    // drop all DTD events
	    setDTDHandler (null);
	    setProperty (DECL_HANDLER, null);
	}

	// maintain proxy locator
	// only one startDocument()/endDocument() pair per event stream
	public void setDocumentLocator (Locator l)
	    { locator = l; }
	public void startDocument ()
	    { }
	public void endDocument ()
	    { }
	
	private void reject (String message) throws SAXException
	    { fatal (new SAXParseException (message, locator)); }
	
	// only the DTD from the "base document" gets reported
	public void startDTD (String root, String pubId, String sysId)
	throws SAXException
	    { reject ("XIncluded DTD: " + sysId); }
	public void endDTD ()
	throws SAXException
	    { reject ("XIncluded DTD"); }
	// ... so this should never happen
	public void skippedEntity (String name) throws SAXException
	    { reject ("XInclude skipped entity: " + name); }

	// since we rejected DTDs, only builtin entities can be reported
    }

    // <xi:include parse='xml' ...>
    // relative to the base URI passed
    private void xinclude (URL url, String href)
    throws SAXException
    {
	XMLReader	helper;
	Scrubber	scrubber;
	Locator		savedLocator = locator;

	// start with a parser acting just like our input
	// modulo DTD-ish stuff (validation flag, entity resolver)
	helper = XMLReaderFactory.createXMLReader ();
	helper.setErrorHandler (getErrorHandler ());
	helper.setFeature (FEATURE_URI + "namespace-prefixes", true);

	// Set up the proxy locator and event filter.
	scrubber = new Scrubber (this);
	locator = null;
	bind (helper, scrubber);

	// Merge the included document, except its DTD
	try {
	    url = new URL (url, href);
	    href = url.toString ();

	    if (inclusions.contains (href))
		fatal (new SAXParseException (
			"XInclude, circular inclusion", locator));

	    inclusions.addElement (href);
	    uris.push (url);
	    helper.parse (new InputSource (href));
	} catch (java.io.IOException e) {
// FIXME: maybe nonfatal
	    fatal (new SAXParseException (href, locator, e));
	} finally {
	    pop (href);
	    locator = savedLocator;
	}
    }

    // <xi:include parse='text' ...>
    // relative to the base URI passed
    private void readText (URL url, String href, String encoding)
    throws SAXException
    {
	InputStream	in = null;

	try {
	    URLConnection	conn;
	    InputStreamReader	reader;
	    char		buf [] = new char [4096];
	    int			count;

	    url = new URL (url, href);
	    conn = url.openConnection ();
	    in = conn.getInputStream ();
	    if (encoding == null)
		encoding = Resolver.getEncoding (conn.getContentType ());
	    if (encoding == null) {
		ErrorHandler	eh = getErrorHandler ();
		if (eh != null)
		    eh.warning (new SAXParseException (
			"guessing text encoding for URL: " + url,
			locator));
		reader = new InputStreamReader (in);
	    } else
		reader = new InputStreamReader (in, encoding);

	    while ((count = reader.read (buf, 0, buf.length)) != -1)
		super.characters (buf, 0, count);
	    in.close ();
	} catch (IOException e) {
// FIXME: maybe nonfatal
	    fatal (new SAXParseException (
		"can't XInclude text",
		locator, e));
	}
    }
}
