/*
 * $Id: WellFormednessFilter.java,v 1.3 2001-07-10 22:56:33 db Exp $
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

import java.util.EmptyStackException;
import java.util.Stack;

import org.xml.sax.*;
import org.xml.sax.ext.*;

import gnu.xml.util.DefaultHandler;


/**
 * This filter reports fatal exceptions in the case of event streams that
 * are not well formed.  The rules currently tested include: <ul>
 *
 *	<li>setDocumentLocator ... may be called only before startDocument
 *
 *	<li>startDocument/endDocument ... must be paired, and all other
 *	calls (except setDocumentLocator) must be nested within these.
 *
 *	<li>startElement/endElement ... must be correctly paired, and
 *	may never appear within CDATA sections.
 *
 *	<li>comment ... can't contain "--"
 *
 *	<li>character data ... can't contain "]]&gt;"
 *
 *	<li>whitespace ... can't contain CR
 *
 *	<li>whitespace and character data must be within an element
 *
 *	<li>processing instruction ... can't contain "?&gt;" or CR
 *
 *	<li>startCDATA/endCDATA ... must be correctly paired.
 *
 *	</ul>
 *
 * <p> Other checks for event stream correctness may be provided in
 * the future.  For example, insisting that
 * entity boundaries nest correctly,
 * namespace scopes nest correctly,
 * namespace values never contain relative URIs,
 * attributes don't have "&lt;" characters;
 * and more.
 *
 * @author David Brownell
 * @version $Date: 2001-07-10 22:56:33 $
 */
public final class WellFormednessFilter extends EventFilter
{
    private boolean		startedDoc;
    private Stack		elementStack = new Stack ();
    private boolean		startedCDATA;
    private String		dtdState = "before";

    
    /**
     * Swallows all events after performing well formedness checks.
     */
	// constructor used by PipelineFactory
    public WellFormednessFilter ()
	{ this (null); }


    /**
     * Passes events through to the specified consumer, after first
     * processing them.
     */
	// constructor used by PipelineFactory
    public WellFormednessFilter (EventConsumer consumer)
    {
	super (consumer);

	setContentHandler (this);
	setDTDHandler (this);
	
	try {
	    setProperty (LEXICAL_HANDLER, this);
	} catch (SAXException e) { /* can't happen */ }
    }

    /**
     * Resets state as if any preceding event stream was well formed.
     * Particularly useful if it ended through some sort of error,
     * and the endDocument call wasn't made.
     */
    public void reset ()
    {
	startedDoc = false;
	startedCDATA = false;
	elementStack.removeAllElements ();
    }


    private SAXParseException getException (String message)
    {
	SAXParseException	e;
	Locator			locator = getDocumentLocator ();

	if (locator == null)
	    return new SAXParseException (message, null, null, -1, -1);
	else
	    return new SAXParseException (message, locator);
    }

    private void fatalError (String message)
    throws SAXException
    {
	SAXParseException	e = getException (message);
	ErrorHandler		handler = getErrorHandler ();

	if (handler != null)
	    handler.fatalError (e);
	throw e;
    }

    /**
     * Throws an exception when called after startDocument.
     *
     * @param l the locator, to be used in error reporting or relative
     *	URI resolution.
     *
     * @exception IllegalStateException when called after the document
     *	has already been started
     */
    public void setDocumentLocator (Locator l)
    {
	if (startedDoc)
	    throw new IllegalStateException (
		    "setDocumentLocator called after startDocument");
	super.setDocumentLocator (l);
    }

    public void startDocument () throws SAXException
    {
	if (startedDoc)
	    fatalError ("startDocument called more than once");
	startedDoc = true;
	startedCDATA = false;
	elementStack.removeAllElements ();
	super.startDocument ();
    }

    public void startElement (
	String ns, String l,
	String name, Attributes atts
    ) throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if ("inside".equals (dtdState))
	    fatalError ("element inside DTD?");
	else
	    dtdState = "after";
	if (startedCDATA)
	    fatalError ("element inside CDATA section");
	if (name == null || "".equals (name))
	    fatalError ("startElement name missing");
	elementStack.push (name);
	super.startElement (ns, l, name, atts);
    }

    public void endElement (String ns, String l, String name)
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if (startedCDATA)
	    fatalError ("element inside CDATA section");
	if (name == null || "".equals (name))
	    fatalError ("endElement name missing");
	
	try {
	    String	top = (String) elementStack.pop ();

	    if (!name.equals (top))
		fatalError ("<" + top + " ...>...</" + name + ">");
	    // XXX could record/test namespace info
	} catch (EmptyStackException e) {
	    fatalError ("endElement without startElement:  </" + name + ">");
	}
	super.endElement (ns, l, name);
    }

    public void endDocument () throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	dtdState = "before";
	startedDoc = false;
	super.endDocument ();
    }


    public void startDTD (String root, String p, String s)
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if ("before" != dtdState)
	    fatalError ("two DTDs?");
	if (!elementStack.empty ())
	    fatalError ("DTD must precede root element");
	dtdState = "inside";
	super.startDTD (root, p, s);
    }

    public void notationDecl (String name, String pub, String sys)
    throws SAXException
    {
// FIXME: not all parsers will report startDTD() ...
// we'd rather insist we're "inside".
	if ("after" == dtdState)
	    fatalError ("not inside DTD");
	super.notationDecl (name, pub, sys);
    }

    public void unparsedEntityDecl (String name, String p, String s, String n)
    throws SAXException
    {
// FIXME: not all parsers will report startDTD() ...
// we'd rather insist we're "inside".
	if ("after" == dtdState)
	    fatalError ("not inside DTD");
	super.unparsedEntityDecl (name, p, s, n);
    }

    // FIXME:  add the four DeclHandler calls too

    public void endDTD ()
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if ("inside" != dtdState)
	    fatalError ("DTD ends without start?");
	dtdState = "after";
	super.endDTD ();
    }

    public void characters (char buf [], int off, int len)
    throws SAXException
    {
	int here = off, end = off + len;
	if (elementStack.empty ())
	    fatalError ("characters must be in an element");
	while (here < end) {
	    if (buf [here++] != ']')
		continue;
	    if (here == end)	// potential problem ...
		continue;
	    if (buf [here++] != ']')
		continue;
	    if (here == end)	// potential problem ...
		continue;
	    if (buf [here++] == '>')
		fatalError ("character data can't contain \"]]>\"");
	}
	super.characters (buf, off, len);
    }

    public void ignorableWhitespace (char buf [], int off, int len)
    throws SAXException
    {
	int here = off, end = off + len;
	if (elementStack.empty ())
	    fatalError ("characters must be in an element");
	while (here < end) {
	    if (buf [here++] == '\r')
		fatalError ("whitespace can't contain CR");
	}
	super.ignorableWhitespace (buf, off, len);
    }

    public void processingInstruction (String target, String data)
    throws SAXException
    {
	if (data.indexOf ('\r') > 0)
	    fatalError ("PIs can't contain CR");
	if (data.indexOf ("?>") > 0)
	    fatalError ("PIs can't contain \"?>\"");
    }

    public void comment (char buf [], int off, int len)
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if (startedCDATA)
	    fatalError ("comments can't nest in CDATA");
	int here = off, end = off + len;
	while (here < end) {
	    if (buf [here] == '\r')
		fatalError ("comments can't contain CR");
	    if (buf [here++] != '-')
		continue;
	    if (here == end)
		fatalError ("comments can't end with \"--->\"");
	    if (buf [here++] == '-')
		fatalError ("comments can't contain \"--\"");
	}
	super.comment (buf, off, len);
    }

    public void startCDATA ()
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if (startedCDATA)
	    fatalError ("CDATA starts can't nest");
	startedCDATA = true;
	super.startCDATA ();
    }

    public void endCDATA ()
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	if (!startedCDATA)
	    fatalError ("CDATA end without start?");
	startedCDATA = false;
	super.endCDATA ();
    }
}
