/*
 * $Id: DomNamedNodeMap.java,v 1.3 2001-11-16 20:14:40 db Exp $
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

package gnu.xml.dom;

import java.util.Vector;

import org.w3c.dom.*;

import DomDoctype.ElementInfo;


// $Id: DomNamedNodeMap.java,v 1.3 2001-11-16 20:14:40 db Exp $

/**
 * <p> "NamedNodeMap" implementation. </p>
 *
 * @author David Brownell 
 * @version $Date: 2001-11-16 20:14:40 $
 */
public class DomNamedNodeMap implements NamedNodeMap
{
    private final Document		owner;

	// XXX want a cheaper implementation than Vector.

    private final Vector		v = new Vector ();
    private boolean			readonly;
    private final Element		element;


    /**
     * Constructs an empty map associated with the specified document.
     */
    public DomNamedNodeMap (Document owner)
    {
	this.owner = owner;
	this.element = null;
    }

    // package private
    DomNamedNodeMap (Document owner, Element element)
    {
	this.owner = owner;
	this.element = element;
    }

    /**
     * Exposes the internal "readonly" flag.  In DOM, all NamedNodeMap
     * objects found in a DocumentType object are read-only (after
     * they are fully constructed), and those holding attributes of
     * a readonly element will also be readonly.
     */
    final public boolean isReadonly ()
    {
	return readonly;
    }

    
    /**
     * Sets the internal "readonly" flag so the node and its
     * children can't be changed.
     */
    public void makeReadonly ()
    {
	readonly = true;

	// We can't escape implementation dependencies here; we let the
	// the Java runtime deal with error reporting
	for (int i = 0; i < v.size (); i++)
	    ((DomNode) v.elementAt (i)).makeReadonly ();
    }


    /**
     * <b>DOM L1</b>
     * Returns the named item from the map, or null; names are just
     * the nodeName property.
     */
    public Node getNamedItem (String name)
    {
	int length = v.size ();

	for (int i = 0; i < length; i++) {
	    Node temp = (Node) v.elementAt (i);
	    if (temp.getNodeName ().equals (name))
		return temp;
	}
	return null;
    }


    /**
     * <b>DOM L2</b>
     * Returns the named item from the map, or null; names are the
     * localName and namespaceURI properties, ignoring any prefix.
     */
    public Node getNamedItemNS (String namespaceURI, String localName)
    {
	int length = v.size ();

	for (int i = 0; i < length; i++) {
	    Node	temp = (Node) v.elementAt (i);
	    String	tempName = temp.getLocalName ();
	    String	ns;

	    if (tempName != null && tempName.equals (localName)) {
		ns = temp.getNamespaceURI ();
		if ((ns == null && namespaceURI == null)
			|| ns.equals (namespaceURI)) {
		    return temp;
		}
	    }
	}
	return null;
    }


    private void checkAttr (Attr arg)
    {
	if (element == null)
	    return;

	Element	argOwner = arg.getOwnerElement ();

	if (argOwner != null) {
	    if (argOwner != element)
		throw new DomEx (DomEx.INUSE_ATTRIBUTE_ERR);
	    return;
	}

	// We can't escape implementation dependencies here; we let
	// the Java runtime deal with error reporting
	((DomAttr)arg).setOwnerElement (element);
    }


    /**
     * <b>DOM L1</b>
     * Stores the named item into the map, optionally overwriting
     * any existing node with that name.  The name used is just
     * the nodeName attribute.
     */
    public Node setNamedItem (Node arg)
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (arg.getOwnerDocument () != owner)
	    throw new DomEx (DomEx.WRONG_DOCUMENT_ERR);
	if (arg instanceof Attr)
	    checkAttr ((Attr) arg);

	int	length = v.size ();
	String	name = arg.getNodeName ();

	for (int i = 0; i < length; i++) {
	    Node temp = (Node) v.elementAt (i);
	    if (temp.getNodeName ().equals (name)) {
// maybe attribute ADDITION (?)
		v.setElementAt (arg, i);
		return temp;
	    }
	}
// maybe attribute ADDITION
	v.addElement (arg);
	return null;
    }


    /**
     * <b>DOM L2</b>
     * Stores the named item into the map, optionally overwriting
     * any existing node with that fully qualified name.  The name
     * used incorporates the localName and namespaceURI properties,
     * and ignores any prefix.
     */
    public Node setNamedItemNS (Node arg)
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (arg.getOwnerDocument () != owner)
	    throw new DomEx (DomEx.WRONG_DOCUMENT_ERR);
	if (arg instanceof Attr)
	    checkAttr ((Attr) arg);

	int	length = v.size ();
	String	localName = arg.getLocalName ();
	String	namespaceURI = arg.getNamespaceURI ();

	if (localName == null)
	    throw new DomEx (DomEx.INVALID_ACCESS_ERR);

	for (int i = 0; i < length; i++) {
	    Node	temp = (Node) v.elementAt (i);
	    String	tempName = temp.getLocalName ();
	    String	ns;

	    if (tempName != null && tempName.equals (localName)) {
		ns = temp.getNamespaceURI ();
		if ((ns == null && namespaceURI == null)
			|| ns.equals (namespaceURI)) {
		    v.setElementAt (arg, i);
		    return temp;
		}
	    }
	}
	v.addElement (arg);
	return null;
    }

    private void maybeRestoreDefault (String name)
    {
	DomDoctype	doctype = (DomDoctype)owner.getDoctype ();
	ElementInfo	info;
	String		value;
	DomAttr		attr;

	if (doctype == null)
	    return;
	if ((info = doctype.getElementInfo (element.getNodeName ())) == null)
	    return;
	if ((value = info.getAttrDefault (name)) == null)
	    return;
	attr = (DomAttr) owner.createAttribute (name);
	attr.setNodeValue (value);
	attr.setSpecified (false);
	setNamedItem (attr);
    }

    /**
     * <b>DOM L1</b>
     * Removes the named item from the map, or reports an exception;
     * names are just the nodeName property.
     */
    public Node removeNamedItem (String name)
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);

	int length = v.size ();

	for (int i = 0; i < length; i++) {
	    Node	temp = (Node) v.elementAt (i);
	    if (temp.getNodeName ().equals (name)) {
// maybe attribute REMOVAL
		v.removeElementAt (i);
		if (element != null)
		    maybeRestoreDefault (name);
		return temp;
	    }
	}
	throw new DomEx (DomEx.NOT_FOUND_ERR);
    }


    /**
     * <b>DOM L2</b>
     * Removes the named item from the map, or reports an exception;
     * names are the localName and namespaceURI properties.
     */
    public Node removeNamedItemNS (String namespaceURI, String localName)
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);

	int length = v.size ();

	for (int i = 0; i < length; i++) {
	    Node	temp = (Node) v.elementAt (i);
	    String	tempName = temp.getLocalName ();
	    String	ns;

	    if (tempName != null && tempName.equals (localName)) {
		ns = temp.getNamespaceURI ();
		if ((ns == null && namespaceURI == null)
			|| ns.equals (namespaceURI)) {
		    v.removeElementAt (i);
		    return temp;
		}
	    }
	}
	throw new DomEx (DomEx.NOT_FOUND_ERR);
    }


    /**
     * <b>DOM L1</b>
     * Returns the indexed item from the map, or null.
     */
    public Node item (int index)
    {
	if (index < 0 || index >= v.size ())
	    return null;
	return (Node) v.elementAt (index);
    }


    /**
     * <b>DOM L1</b>
     * Returns the length of the map.
     */
    public int getLength ()
	{ return v.size (); }
}
