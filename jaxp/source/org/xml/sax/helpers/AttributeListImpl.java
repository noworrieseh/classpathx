// SAX default implementation for AttributeList.
// http://www.saxproject.org
// No warranty; no copyright -- use this as you will.
// $Id: AttributeListImpl.java,v 1.11 2004-12-11 15:41:10 dog Exp $

package org.xml.sax.helpers;

import org.xml.sax.AttributeList;

import java.util.Vector;


/**
 * Default implementation for AttributeList.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * See <a href='http://www.saxproject.org'>http://www.saxproject.org</a>
 * for further information.
 * </blockquote>
 *
 * <p>AttributeList implements the deprecated SAX1 {@link
 * org.xml.sax.AttributeList AttributeList} interface, and has been
 * replaced by the new SAX2 {@link org.xml.sax.helpers.AttributesImpl
 * AttributesImpl} interface.</p>
 *
 * <p>This class provides a convenience implementation of the SAX
 * {@link org.xml.sax.AttributeList AttributeList} interface.  This 
 * implementation is useful both for SAX parser writers, who can use 
 * it to provide attributes to the application, and for SAX application 
 * writers, who can use it to create a persistent copy of an element's 
 * attribute specifications:</p>
 *
 * <pre>
 * private AttributeList myatts;
 *
 * public void startElement (String name, AttributeList atts)
 * {
 *              // create a persistent copy of the attribute list
 *              // for use outside this method
 *   myatts = new AttributeListImpl(atts);
 *   [...]
 * }
 * </pre>
 *
 * <p>Please note that SAX parsers are not required to use this
 * class to provide an implementation of AttributeList; it is
 * supplied only as an optional convenience.  In particular, 
 * parser writers are encouraged to invent more efficient
 * implementations.</p>
 *
 * @deprecated This class implements a deprecated interface,
 *             {@link org.xml.sax.AttributeList AttributeList};
 *             that interface has been replaced by
 *             {@link org.xml.sax.Attributes Attributes},
 *             which is implemented in the
 *             {@link org.xml.sax.helpers.AttributesImpl 
 *            AttributesImpl} helper class.
 * @since SAX 1.0
 * @author David Megginson
 * @version 2.0.1 (sax2r2)
 * @see org.xml.sax.AttributeList
 * @see org.xml.sax.DocumentHandler#startElement 
 */
public class AttributeListImpl implements AttributeList
{
    
    /**
     * Create an empty attribute list.
     *
     * <p>This constructor is most useful for parser writers, who
     * will use it to create a single, reusable attribute list that
     * can be reset with the clear method between elements.</p>
     *
     * @see #addAttribute
     * @see #clear
     */
    public AttributeListImpl ()
    {
    }
    
    
    /**
     * Construct a persistent copy of an existing attribute list.
     *
     * <p>This constructor is most useful for application writers,
     * who will use it to create a persistent copy of an existing
     * attribute list.</p>
     *
     * @param atts The attribute list to copy
     * @see org.xml.sax.DocumentHandler#startElement
     */
    public AttributeListImpl (AttributeList atts)
    {
	setAttributeList(atts);
    }
    
    

    ////////////////////////////////////////////////////////////////////
    // Methods specific to this class.
    ////////////////////////////////////////////////////////////////////
    
    
    /**
     * Set the attribute list, discarding previous contents.
     *
     * <p>This method allows an application writer to reuse an
     * attribute list easily.</p>
     *
     * @param atts The attribute list to copy.
     */
    public void setAttributeList (AttributeList atts)
    {
	int count = atts.getLength();
	
	clear();
	
	for (int i = 0; i < count; i++) {
	    addAttribute(atts.getName(i), atts.getType(i), atts.getValue(i));
	}
    }
    
    
    /**
     * Add an attribute to an attribute list.
     *
     * <p>This method is provided for SAX parser writers, to allow them
     * to build up an attribute list incrementally before delivering
     * it to the application.</p>
     *
     * @param name The attribute name.
     * @param type The attribute type ("NMTOKEN" for an enumeration).
     * @param value The attribute value (must not be null).
     * @see #removeAttribute
     * @see org.xml.sax.DocumentHandler#startElement
     */
    public void addAttribute (String name, String type, String value)
    {
	names.addElement(name);
	types.addElement(type);
	values.addElement(value);
    }
    
    
    /**
     * Remove an attribute from the list.
     *
     * <p>SAX application writers can use this method to filter an
     * attribute out of an AttributeList.  Note that invoking this
     * method will change the length of the attribute list and
     * some of the attribute's indices.</p>
     *
     * <p>If the requested attribute is not in the list, this is
     * a no-op.</p>
     *
     * @param name The attribute name.
     * @see #addAttribute
     */
    public void removeAttribute (String name)
    {
	int i = names.indexOf(name);
	
	if (i >= 0) {
	    names.removeElementAt(i);
	    types.removeElementAt(i);
	    values.removeElementAt(i);
	}
    }
    
    
    /**
     * Clear the attribute list.
     *
     * <p>SAX parser writers can use this method to reset the attribute
     * list between DocumentHandler.startElement events.  Normally,
     * it will make sense to reuse the same AttributeListImpl object
     * rather than allocating a new one each time.</p>
     *
     * @see org.xml.sax.DocumentHandler#startElement
     */
    public void clear ()
    {
	names.removeAllElements();
	types.removeAllElements();
	values.removeAllElements();
    }
    
    

    ////////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.AttributeList
    ////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return the number of attributes in the list.
     *
     * @return The number of attributes in the list.
     * @see org.xml.sax.AttributeList#getLength
     */
    public int getLength ()
    {
	return names.size();
    }
    
    
    /**
     * Get the name of an attribute (by position).
     *
     * @param i The position of the attribute in the list.
     * @return The attribute name as a string, or null if there
     *         is no attribute at that position.
     * @see org.xml.sax.AttributeList#getName(int)
     */
    public String getName (int i)
    {
	if (i < 0) {
	    return null;
	}
	try {
	    return (String)names.elementAt(i);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }
    
    
    /**
     * Get the type of an attribute (by position).
     *
     * @param i The position of the attribute in the list.
     * @return The attribute type as a string ("NMTOKEN" for an
     *         enumeration, and "CDATA" if no declaration was
     *         read), or null if there is no attribute at
     *         that position.
     * @see org.xml.sax.AttributeList#getType(int)
     */
    public String getType (int i)
    {
	if (i < 0) {
	    return null;
	}
	try {
	    return (String)types.elementAt(i);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }
    
    
    /**
     * Get the value of an attribute (by position).
     *
     * @param i The position of the attribute in the list.
     * @return The attribute value as a string, or null if
     *         there is no attribute at that position.
     * @see org.xml.sax.AttributeList#getValue(int)
     */
    public String getValue (int i)
    {
	if (i < 0) {
	    return null;
	}
	try {
	    return (String)values.elementAt(i);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }
    
    
    /**
     * Get the type of an attribute (by name).
     *
     * @param name The attribute name.
     * @return The attribute type as a string ("NMTOKEN" for an
     *         enumeration, and "CDATA" if no declaration was
     *         read).
     * @see org.xml.sax.AttributeList#getType(java.lang.String)
     */
    public String getType (String name)
    {
	return getType(names.indexOf(name));
    }
    
    
    /**
     * Get the value of an attribute (by name).
     *
     * @param name The attribute name.
     * @see org.xml.sax.AttributeList#getValue(java.lang.String)
     */
    public String getValue (String name)
    {
	return getValue(names.indexOf(name));
    }
    
    

    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////

    Vector names = new Vector();
    Vector types = new Vector();
    Vector values = new Vector();

}

// end of AttributeListImpl.java
