/*
 * $Id: WellFormednessFilter.java,v 1.2 2001-07-05 01:43:02 db Exp $
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
 * are not well formed.  The following rules are currently tested: <ul>
 *
 *	<li>setDocumentLocator ... may be called only before startDocument
 *
 *	<li>startDocument/endDocument ... must be paired, and all other
 *	calls (except setDocumentLocator) must be nested within these.
 *
 *	<li>startElement/endElement ... must be correctly paired, and
 *	may never appear within CDATA sections.
 *
 *	<li>startCDATA/endCDATA ... must be correctly paired.
 *
 *	</ul>
 *
 * <p> Other checks for event stream correctness may be provided in
 * the future.
 *
 * @author David Brownell
 * @version $Date: 2001-07-05 01:43:02 $
 */
public class WellFormednessFilter extends EventFilter
{
    private Locator		locator;
    private boolean		startedDoc;
    private Stack		elementStack = new Stack ();
    private boolean		startedCDATA;

    
    /**
     * Swallows all events after performing well formedness checks.
     */
	// constructor used by PipelineFactory
    public WellFormednessFilter ()
	{ super (null); }


    /**
     * Passes events through to the specified consumer, after first
     * processing them.
     */
	// constructor used by PipelineFactory
    public WellFormednessFilter (EventConsumer consumer)
    {
	super (consumer);

	setContentHandler (this);
	
	try {
	    setProperty (PROPERTY_URI + "lexical-handler", this);
	} catch (SAXException e) { /* can't happen */ }
    }

    /**
     * Resets state as if any preceding event stream was well formed.
     * Particularly useful if it ended through some sort of error,
     * and the endDocument call wasn't made.
     */
    public void reset ()
    {
	locator = null;
	startedDoc = false;
	startedCDATA = false;
	elementStack.removeAllElements ();
    }


    private void fatalError (String message)
    throws SAXException
    {
	SAXParseException	e;
	ErrorHandler		handler = getErrorHandler ();

	if (locator == null)
	    e = new SAXParseException (message, null, null, -1, -1);
	else
	    e = new SAXParseException (message, locator);
	if (handler != null)
	    handler.fatalError (e);
	throw e;
    }


    /**
     * Throws a <em>RuntimeException</em> when called after startDocument.
     * The SAX APIs do not permit a SAXException to be reported from this
     * method.
     *
     * @param l the locator, to be used in error reporting or relative
     *	URI resolution.
     */
    public void setDocumentLocator (Locator l)
    {
	if (startedDoc)
	    throw new RuntimeException (
		    "setDocumentLocator called after startDocument");
	locator = l;
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
	startedDoc = false;
	super.endDocument ();
    }


    public void startDTD (String root, String p, String s)
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	super.startDTD (root, p, s);
    }

    public void endDTD ()
    throws SAXException
    {
	if (!startedDoc)
	    fatalError ("callback outside of document?");
	super.endDTD ();
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
