/*
 * $Id: DomCharacterData.java,v 1.3 2001-06-24 04:12:23 db Exp $
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
import org.w3c.dom.events.MutationEvent;


// $Id: DomCharacterData.java,v 1.3 2001-06-24 04:12:23 db Exp $

/**
 * <p> Abstract "CharacterData" implementation.  This
 * facilitates reusing code in classes implementing subtypes of that DOM
 * interface (Text, Comment, CDATASection).  </p>
 *
 * <p> Representational efficiency wasn't exactly a goal here.  Strings
 * are not really an efficient internal representation, and aren't how parsers
 * generally deliver data, but they're the only API exposed by DOM.  To
 * use a more efficient representation would involve either conversion
 * costs, or defining some extension to DOM to permit faster construction. 
 * (For example, it could be defined through a new "feature" module.)
 *
 * @author David Brownell
 * @version $Date: 2001-06-24 04:12:23 $
 */
public abstract class DomCharacterData extends DomNode
    implements CharacterData
{
    private String		data;

    // package private
    DomCharacterData (Document doc, short type, String value)
    {
	super (doc, type);
	if (value != null)
	    data = value;
	else
	    data = "";
    }

    
    /**
     * <b>DOM L1</b>
     * Appends the specified data to the value of this node.
     * Causes a DOMCharacterDataModified mutation event to be reported.
     */
    public void appendData (String arg)
    {
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	arg = data + arg;
	mutating (arg);
	data = arg;
    }
    

    /**
     * <b>DOM L1</b>
     * Modifies the value of this node.
     * Causes a DOMCharacterDataModified mutation event to be reported.
     */
    public void deleteData (int offset, int count)
    {
	char		chars [];
	StringBuffer	buf;
	String		newValue;

	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	else if (count < 0)
	    throw new DomEx (DomEx.INDEX_SIZE_ERR);
	buf = new StringBuffer ();
	try {
	    chars = data.toCharArray ();
	    if (offset > chars.length)
		throw new DomEx (DomEx.INDEX_SIZE_ERR);
	    buf.append (chars, 0, offset);
	    buf.append (chars, offset + count,
		    chars.length - (offset + count));
	    newValue = new String (buf);
	    mutating (newValue);
	    data = newValue;
	} catch (IndexOutOfBoundsException x) {
	    if (offset >= 0 && count >= 0) {
		int len = data.length ();
		if ((offset + count) > len) {
		    newValue = data.substring (0, offset);
		    mutating (newValue);
		    data = newValue;
		    return;
		}
	    }
	    throw new DomEx (DomEx.INDEX_SIZE_ERR);
	}
    }
    

    /**
     * <b>DOM L1</b>
     * Returns the value of this node.
     */
    public String getNodeValue ()
    {
	return data;
    }

    
    /**
     * <b>DOM L1</b>
     * Returns the value of this node; same as getNodeValue.
     */
    final public String getData ()
    {
	return getNodeValue ();
    }


    /**
     * <b>DOM L1</b>
     * Returns the length of the data.
     */
    public int getLength ()
    {
	return data.length ();
    }


    static final class EmptyNodeList implements NodeList
    {
	public int getLength () { return 0; }
	public Node item (int i) { return null; }
    }
    
    static final EmptyNodeList	theEmptyNodeList = new EmptyNodeList ();


    /**
     * <b>DOM L1</b>
     * Returns an empty list of children.
     */
    final public NodeList getChildNodes ()
    {
	return theEmptyNodeList;
    }


    /**
     * <b>DOM L1</b>
     * Modifies the value of this node.
     */
    public void insertData (int offset, String arg)
    {
	StringBuffer buf;
	String newValue;

	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	buf = new StringBuffer (data);
	try {
	    buf.insert (offset, arg);
	    newValue = new String (buf);
	    mutating (newValue);
	    data = newValue;
	} catch (IndexOutOfBoundsException x) {
	    throw new DomEx (DomEx.INDEX_SIZE_ERR);
	}
    }
    

    /**
     * <b>DOM L1</b>
     * Modifies the value of this node.  Causes DOMCharacterDataModified
     * mutation events to be reported (at least one).
     */
    public void replaceData (int offset, int count, String arg)
    {
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);

	// this could be rewritten to be faster,
	// and to report only one mutation event
	deleteData (offset, count);
	insertData (offset, arg);
    }
    

    /**
     * <b>DOM L1</b>
     * Assigns the value of this node.
     * Causes a DOMCharacterDataModified mutation event to be reported.
     */
    public void setNodeValue (String value)
    {
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (value == null)
	    value = "";
	mutating (value);
	data = value;
    }

    
    /**
     * <b>DOM L1</b>
     * Assigns the value of this node; same as setNodeValue.
     */
    final public void setData (String data)
    {
	setNodeValue (data);
    }

    
    /**
     * <b>DOM L1</b>
     * Returns the specified substring.
     */
    public String substringData (int offset, int count)
    {
	try {
	    return data.substring (offset, offset + count);
	} catch (IndexOutOfBoundsException e) {
	    if (offset >= 0 && count >= 0) {
		int len = data.length ();
		if (offset < len && (offset + count) > len)
		    return data.substring (offset);
	    }
	    throw new DomEx (DomEx.INDEX_SIZE_ERR);
	}
    }

    private void mutating (String newValue)
    {
	if (!reportMutations)
	    return;

	// EVENT:  DOMCharacterDataModified, target = this,
	//	prev/new values provided
	MutationEvent	event;

	event = (MutationEvent) createEvent ("DOMCharacterDataModified");
	event.initMutationEvent ("DOMCharacterDataModified",
		true /* bubbles */, false /* nocancel */,
		null, data, newValue, null, (short) 0);
	dispatchEvent (event);
    }
}

