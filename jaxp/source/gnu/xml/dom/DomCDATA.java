/*
 * $Id: DomCDATA.java,v 1.1 2001-06-20 21:30:05 db Exp $
 * Copyright (C) 1999-2000 David Brownell
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

import org.w3c.dom.*;


// $Id: DomCDATA.java,v 1.1 2001-06-20 21:30:05 db Exp $

/**
 * <p> "CDATASection" implementation.
 * This is a non-core DOM class, supporting the "XML" feature.
 * CDATA sections are just ways to represent text using different
 * delimeters. </p>
 *
 * <p> <em>You are strongly advised not to use CDATASection nodes.</em>
 * The advantage of having slightly prettier ways to print text that may
 * have lots of embedded XML delimiters, such as "&amp;" and "&lt;",
 * can be dwarfed by the cost of dealing with multiple kinds of text
 * nodes in all your algorithms. </p>
 *
 * @author David Brownell
 * @version $Date: 2001-06-20 21:30:05 $
 */
public class DomCDATA extends DomText implements CDATASection
{
    /**
     * Constructs a CDATA section node associated with the specified
     * document and holding the specified data.
     *
     * <p>This constructor should only be invoked by a Document as part of
     * its createCDATASection functionality, or through a subclass which is
     * similarly used in a "Sub-DOM" style layer.
     *
     */
    protected DomCDATA (Document owner, String value)
    {
	super (owner, CDATA_SECTION_NODE, value);
    }


    /**
     * <b>DOM L1</b>
     * Returns the string "#cdata-section".
     */
    final public String getNodeName ()
    {
	return "#cdata-section";
    }
}
