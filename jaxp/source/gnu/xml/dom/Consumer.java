/*
 * $Id: Consumer.java,v 1.3 2001-10-18 00:48:36 db Exp $
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


package gnu.xml.dom;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;

import gnu.xml.pipeline.DomConsumer;


/**
 * Event consumer which constructs DOM documents using the implementation
 * in this package, using SAX2 events.  This packages various backdoors
 * into this DOM implementation, as needed to address DOM requirements
 * that can't be met by strictly conforming implementations of DOM.
 *
 * <p> These requirements all relate to {@link DocumentType} nodes and
 * features of that node type.  These features are normally not used,
 * because that interface only exposes a subset of the information found
 * in DTDs.  More, that subset does not include the most important typing
 * information.  For example, it excludes element content models and
 * attribute typing.  It does expose some entity management issues,
 * although entity management doesn't relate to document typing.
 *
 * <p> Note that SAX2 does not expose the literal text of the DTD's
 * internal subset, so it will not be present in DOM trees constructed
 * using this API.
 *
 * @author David Brownell
 * @version $Date: 2001-10-18 00:48:36 $
 */
public class Consumer extends DomConsumer
{
    /**
     * Constructs an unconfigured event consumer.
     */
    public Consumer ()
    throws SAXException
    {
	super (DomDocument.class);
	setHandler (new Backdoor (this));
    }

    /**
     * Implements the backdoors needed by DOM.
     * All methods in this class use implementation-specific APIs that are
     * implied by the DOM specification (needed to implement testable
     * behavior) but which are excluded from the DOM specification.
     */
    static final class Backdoor extends DomConsumer.Handler
    {
	Backdoor (DomConsumer c)
	throws SAXException
	    { super (c); }

	// helper routine
	private DomDoctype getDoctype ()
	throws SAXException
	{
	    DomDocument		doc = (DomDocument) getDocument ();
	    DocumentType	dt = doc.getDoctype ();

	    if (dt == null)
		throw new SAXException ("doctype missing!");
	    return (DomDoctype) dt;
	}

	// SAX2 "lexical" event
	public void startDTD (String name, String pubid, String sysid)
	throws SAXException
	{
	    DomDocument		doc = (DomDocument) getDocument ();

	    super.startDTD (name, pubid, sysid);
	    // DOM L2 doctype creation model is bizarre
	    doc.appendChild (new DomDoctype (doc, name, pubid, sysid));
	}

	// SAX2 "lexical" event
	public void endDTD ()
	throws SAXException
	{
	    super.endDTD ();
	    // DOM L2 has no way to make things readonly
	    getDoctype ().makeReadonly ();
	}

	// SAX1 DTD event
	public void notationDecl (
	    String name,
	    String pubid, String sysid
	) throws SAXException
	{
	    // DOM L2 can't create/save notation nodes
	    getDoctype ().declareNotation (name, pubid, sysid);
	}

	// SAX1 DTD event
	public void unparsedEntityDecl (
	    String name,
	    String pubid, String sysid,
	    String notation
	) throws SAXException
	{
	    // DOM L2 can't create/save entity nodes
	    getDoctype ().declareEntity (name, pubid, sysid, notation);
	}

	// SAX2 declaration event
	public void internalEntityDecl (String name, String value)
	throws SAXException
	{
	    // DOM L2 can't create/save entity nodes
	    // NOTE:  this doesn't save the value as a child of this
	    // node, though it could realistically do so.
	    getDoctype ().declareEntity (name, null, null, null);
	}

	// SAX2 declaration event
	public void externalEntityDecl (String name, String pubid, String sysid)
	throws SAXException
	{
	    // DOM L2 can't create/save entity nodes
	    // NOTE:  DOM allows for these to have children, if
	    // they don't have unbound namespace references.
	    getDoctype ().declareEntity (name, pubid, sysid, null);
	}

	// SAX2 element
	public void startElement (
	    String uri,
	    String local,
	    String name,
	    Attributes attrs
	) throws SAXException
	{
	    Node		top;

	    super.startElement (uri, local, name, attrs);

	    // might there be more work?
	    top = getTop ();
	    if (!top.hasAttributes () || !(attrs instanceof Attributes2))
		return;

	    // remember any attributes that got defaulted
	    DomNamedNodeMap	map = (DomNamedNodeMap) top.getAttributes ();
	    Attributes2		atts = (Attributes2) attrs;
	    int			length = atts.getLength ();

	    for (int i = 0; i < length; i++) {
		if (atts.isSpecified (i))
		    continue;
		
		// value was defaulted.
		String		temp = atts.getQName (i);
		DomAttr		attr;

		if ("".equals (temp))
		    attr = (DomAttr) map.getNamedItemNS (atts.getURI (i),
			    atts.getLocalName (i));
		else
		    attr = (DomAttr) map.getNamedItem (temp);

		// DOM L2 can't write this flag, only read it
		attr.setSpecified (false);
	    }

	    // FIXME: associate with the list of attributes that
	    // DOM may have to default later (by hand)
	}

	// FIXME:  remember attributes declared with default values,
	// and arrange to recreate them if they're ever deleted 
	// from a NamedNodeMap of attributes.
	// DOM L2 has no way to create or access such declarations,
	// except implicitly.

// FIXME:

// override clearDocument(), delegate then:
// doc.setCheckingCharacters (true);

    }
}
