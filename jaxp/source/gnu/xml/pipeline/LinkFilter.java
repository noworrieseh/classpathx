/*
 * $Id: LinkFilter.java,v 1.2 2001-10-23 17:42:25 db Exp $
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
import java.net.URL; 
import java.util.Enumeration;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


// $Id: LinkFilter.java,v 1.2 2001-10-23 17:42:25 db Exp $

/**
 * Pipeline filter to remember (X)HTML links found in an (X)HTML document,
 * so they can later be crawled.  Fragments are not counted, and duplicates
 * are ignored.  Callers are responsible for filtering out URLs they aren't
 * interested in.  Events are passed through unmodified.
 *
 * <p> Input MUST include a setDocumentLocator() call, as it's used to
 * resolve relative links in the absence of a "base" element.  Input MUST
 * also include namespace identifiers, since it is the XHTML namespace
 * identifier which is used to identify the relevant elements.
 *
 * <p><em>FIXME:</em> handle xml:base attribute ... in association with
 * a stack of base URIs.  Similarly, recognize/support XLink data.
 *
 * @author David Brownell
 * @version $Date: 2001-10-23 17:42:25 $
 */
public class LinkFilter extends EventFilter
{
    // for storing URIs
    private Vector		vector = new Vector ();

	// struct for "full" link record (tbd)
	// these for troubleshooting original source:
	//	original uri
	//	uri as resolved (base, relative, etc)
	//	URI of originating doc
	//	line #
	//	original element + attrs (img src, desc, etc)

	// XLink model of the link ... for inter-site pairups ?

    private String		baseURI;

    private boolean		siteRestricted = false;

    //
    // XXX leverage blacklist info (like robots.txt)
    //
    // XXX constructor w/param ... pipeline for sending link data
    // probably XHTML --> XLink, providing info as sketched above
    //


    /**
     * Constructs a new event filter, which collects links in private data
     * structure for later enumeration.
     */
	// constructor used by PipelineFactory
    public LinkFilter ()
    {
	super.setContentHandler (this);
    }


    /**
     * Constructs a new event filter, which collects links in private data
     * structure for later enumeration and passes all events, unmodified,
     * to the next consumer.
     */
	// constructor used by PipelineFactory
    public LinkFilter (EventConsumer next)
    {
	super (next);
	super.setContentHandler (this);
    }


    /**
     * Returns an enumeration of the links found since the filter
     * was constructed, or since removeAllLinks() was called.
     *
     * @return enumeration of strings.
     */
    public Enumeration getLinks ()
    {
	return vector.elements ();
    }

    /**
     * Removes records about all links reported to the event
     * stream, as if the filter were newly created.
     */
    public void removeAllLinks ()
    {
	vector = new Vector ();
    }


    /**
     * Collects URIs for (X)HTML content from elements which hold them.
     */
    public void startElement (
	String	namespace,
	String	local,
	String	name,
	Attributes	attrs
    ) throws SAXException
    {
	String	link;

	// Recognize XHTML links.
	if ("http://www.w3.org/1999/xhtml".equals (namespace)) {

	    if ("a".equals (local) || "base".equals (local)
		    || "area".equals (local))
		link = attrs.getValue ("href");
	    else if ("iframe".equals (local) || "frame".equals (local))
		link = attrs.getValue ("src");
	    else if ("blockquote".equals (local) || "q".equals (local)
		    || "ins".equals (local) || "del".equals (local))
		link = attrs.getValue ("cite");
	    else
		link = null;
	    link = maybeAddLink (link);

	    // "base" modifies designated baseURI
	    if ("base".equals (local) && link != null)
		baseURI = link;

	    if ("iframe".equals (local) || "img".equals (local))
		maybeAddLink (attrs.getValue ("longdesc"));
	}
	
	super.startElement (namespace, local, name, attrs);
    }

    private String maybeAddLink (String link)
    {
	int		index;

	// ignore empty links and fragments inside docs
	if (link == null)
	    return null;
	if ((index = link.indexOf ("#")) >= 0)
	    link = link.substring (0, index);
	if (link.equals (""))
	    return null;

	try {
	    // get the real URI
	    URL		base = new URL ((baseURI != null)
				    ? baseURI
				    : getDocumentLocator ().getSystemId ());
	    URL		url = new URL (base, link);

	    link = url.toString ();

	    // ignore duplicates
	    if (vector.contains (link))
		return link;

	    // other than what "base" does, stick to original site:
	    if (siteRestricted) {
		// don't switch protocols
		if (!base.getProtocol ().equals (url.getProtocol ()))
		    return link;
		// don't switch servers
		if (base.getHost () != null
			&& !base.getHost ().equals (url.getHost ()))
		    return link;
	    }

	    vector.addElement (link);

	    return link;
	    
	} catch (IOException e) {
	    // bad URLs we don't want
	}
	return null;
    }

    /**
     * Reports an error if no Locator has been made available.
     */
    public void startDocument ()
    throws SAXException
    {
	if (getDocumentLocator () == null)
	    throw new SAXException ("no Locator!");
    }

    /**
     * Forgets about any base URI information that may be recorded.
     * Applications will often want to call removeAllLinks(), likely
     * after examining the links which were reported.
     */
    public void endDocument ()
    throws SAXException
    {
	baseURI = null;
	super.endDocument ();
    }
}
