/*
 * $Id: DomText.java,v 1.1 2001-06-20 21:30:05 db Exp $
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


// $Id: DomText.java,v 1.1 2001-06-20 21:30:05 db Exp $

/**
 * <p> "Text" implementation.  </p>
 *
 * @author David Brownell 
 * @version $Date: 2001-06-20 21:30:05 $
 */
public class DomText extends DomCharacterData implements Text
{
    private boolean	ignorable;

    /**
     * Constructs a text node associated with the specified
     * document and holding the specified data.
     *
     * <p>This constructor should only be invoked by a Document object
     * as part of its createTextNode functionality, or through a subclass
     * which is similarly used in a "Sub-DOM" style layer. 
     */
    protected DomText (Document owner, String value)
    {
	super (owner, TEXT_NODE, value);
    }

    // package private
    DomText (Document owner, short code, String value)
    {
	super (owner, code, value);
    }


    /**
     * <b>DOM L1</b>
     * Returns the string "#text".
     */
    // can't be 'final' with CDATA subclassing
    public String getNodeName ()
    {
	return "#text";
    }


    /**
     * <b>DOM L1</b>
     * Splits this text node in two parts at the offset, returning
     * the new text node (the sibling with the second part).
     */
    public Text splitText (int offset)
    {
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	try {
	    String	before = getNodeValue ().substring (0, offset);
	    String	after = getNodeValue ().substring (offset);
	    Text	next;

	    if (getNodeType () == TEXT_NODE)
		next = getOwnerDocument ().createTextNode (after);
	    else // CDATA_SECTION_NODE
		next = getOwnerDocument ().createCDATASection (after);

	    getParentNode ().insertBefore (next, getNextSibling ());
	    setNodeValue (before);
	    return next;

	} catch (IndexOutOfBoundsException x) {
	    throw new DomEx (DomEx.INDEX_SIZE_ERR);
	}
    }


    /**
     * Sets a flag which may be used to record whether the text contains
     * "ignorable whitespace", which can be discarded by most applications.
     *
     * <p> The XML specification requires that validating processors report
     * which whitespace separates elements with "children" content models,
     * and so is "ignorable" (not part of character data).  In Java, most
     * parsers, not just validating ones, make this information available
     * through SAX callbacks. </p>
     *
     * <p> Where possible, configure your DOM construction code to discard
     * ignorable whitespace reported to it by a parser, rather than trying
     * to save it in any way.  This will reduce your memory consumption,
     * simplifies many algorithms, and will provide various savings in
     * execution time as well.</p>
     */
    final public void setIgnorable (boolean value)
	{ ignorable = value; }
    
    
    /**
     * Returns the flag recording whether the text contains "ignorable
     * whitespace".
     */
    final public boolean isIgnorable ()
	{ return ignorable; }

}
