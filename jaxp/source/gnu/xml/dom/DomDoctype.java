/*
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

import org.w3c.dom.*;

import java.util.Hashtable;


/**
 * <p> "DocumentType" implementation (with no extensions for supporting
 * any document typing information).  This is a non-core DOM class,
 * supporting the "XML" feature. </p>
 *
 * <p> <em>Few XML applications will actually care about this partial
 * DTD support</em>, since it doesn't expose any (!) of the data typing
 * facilities which can motivate applications to use DTDs.  It does not
 * expose element content models, or information about attribute typing
 * rules.  Plus the information it exposes isn't very useful; as one example,
 * DOM exposes information about unparsed ENTITY objects, which is only used
 * with certain element attributes, but does not expose the information about
 * those attributes which is needed to apply that data! </p>
 *
 * <p> Also, note that there are no nonportable ways to associate even the
 * notation and entity information exposed by DOM with a DocumentType.  While
 * there is a DOM L2 method to construct a DocumentType, it only gives access
 * to the textual content of the &lt;!DOCTYPE ...&gt; declaration.  </p>
 *
 * <p> In short, <em>you are strongly advised not to rely on this incomplete
 * DTD functionality</em> in your application code.</p>
 *
 * @see DomEntity
 * @see DomEntityReference
 * @see DomNotation
 *
 * @author David Brownell 
 */
public class DomDoctype extends DomExtern implements DocumentType
{
    private DomNamedNodeMap	notations;
    private DomNamedNodeMap	entities;
    private DOMImplementation	implementation;
    private String		subset;

    private Hashtable		elements = new Hashtable ();
    private boolean		ids;


    /**
     * Constructs a DocumentType node associated with the specified
     * implementation, with the specified name.
     *
     * <p>This constructor should only be invoked by a DOMImplementation as
     * part of its createDocumentType functionality, or through a subclass
     * which is similarly used in a "Sub-DOM" style layer.
     *
     * <p> Note that at this time there is no standard SAX API granting
     * access to the internal subset text, so that relying on that value
     * is not currently portable.
     *
     * @param impl The implementation with which this object is associated
     * @param name Name of this root element
     * @param publicId If non-null, provides the external subset's
     *	PUBLIC identifier
     * @param systemId If non-null, provides the external subset's
     *	SYSTEM identifier
     * @param internalSubset Provides the literal value (unparsed, no
     *	entities expanded) of the DTD's internal subset.
     */
    protected DomDoctype (
	DOMImplementation impl,
	String name,
	String publicId,
	String systemId,
	String internalSubset
    )
    {
	super (null, name, publicId, systemId);
	implementation = impl;
	subset = internalSubset;
    }

    // package private
    // for JAXP-style builder backdoors
    DomDoctype (
	DomDocument	doc,
	String		name,
	String		publicId,
	String		systemId
    )
    {
	super (doc, name, publicId, systemId);
	implementation = doc.getImplementation ();
    }


    /**
     * <b>DOM L1</b>
     * Returns the root element's name (just like getNodeName).
     */
    final public String getName () { return getNodeName (); }

    /**
     * <b>DOM L1</b>
     * Returns the constant DOCUMENT_TYPE_NODE.
     */
    final public short getNodeType ()
	{ return DOCUMENT_TYPE_NODE; }


    /**
     * <b>DOM L1</b>
     * Returns information about any general entities declared
     * in the DTD.
     *
     * <p><em>Note:  DOM L1 doesn't throw a DOMException here, but
     * then it doesn't have the strange construction rules of L2.</em>
     *
     * @exception DOMException HIERARCHY_REQUEST_ERR if the DocumentType
     *	is not associated with a document.
     */
    public NamedNodeMap getEntities ()
    {
	if (entities == null) {
	    if (getOwnerDocument () == null)
		throw new DomEx (DomEx.HIERARCHY_REQUEST_ERR);
	    entities = new DomNamedNodeMap (getOwnerDocument ());
	}
	return entities;
    }


    /**
     * Records the declaration of a general entity in this DocumentType.
     *
     * @param name Name of the entity
     * @param publicId If non-null, provides the entity's PUBLIC identifier
     * @param systemId Provides the entity's SYSTEM identifier
     * @param notation If non-null, provides the entity's notation
     *	(indicating an unparsed entity)
     * @return The Entity that was declared, or null if the entity wasn't
     *	recorded (because it's a parameter entity or because an entity with
     *	this name was already declared).
     *
     * @exception DOMException NO_MODIFICATION_ALLOWED_ERR if the
     *	DocumentType is no longer writable.
     * @exception DOMException HIERARCHY_REQUEST_ERR if the DocumentType
     *	is not associated with a document.
     */
    public Entity declareEntity (
	String name,
	String publicId,
	String systemId,
	String notation
    )
    {
	DomEntity entity;

	if (name.charAt (0) == '%' || "[dtd]".equals (name))
	    return null;
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	getEntities ();

	DomDocument.verifyXmlName (name);
	if (entities.getNamedItem (name) != null)
	    return null;

	entity = new DomEntity (getOwnerDocument (),
		name, publicId, systemId, notation);
	entities.setNamedItem (entity);
	return entity;
    }

    /**
     * <b>DOM L1</b>
     * Returns information about any notations declared in the DTD.
     *
     * <p><em>Note:  DOM L1 doesn't throw a DOMException here, but
     * then it doesn't have the strange construction rules of L2.</em>
     *
     * @exception DOMException HIERARCHY_REQUEST_ERR if the DocumentType
     *	is not associated with a document.
     */
    public NamedNodeMap getNotations ()
    {
	if (notations == null) {
	    if (getOwnerDocument () == null)
		throw new DomEx (DomEx.HIERARCHY_REQUEST_ERR);
	    notations = new DomNamedNodeMap (getOwnerDocument ());
	}
	return notations;
    }


    /**
     * Records the declaration of a notation in this DocumentType.
     *
     * @param name Name of the notation
     * @param publicId If non-null, provides the notation's PUBLIC identifier
     * @param systemId If non-null, provides the notation's SYSTEM identifier
     * @return The notation that was declared.
     *
     * @exception DOMException NO_MODIFICATION_ALLOWED_ERR if the
     *	DocumentType is no longer writable.
     * @exception DOMException HIERARCHY_REQUEST_ERR if the DocumentType
     *	is not associated with a document.
     */
    public Notation declareNotation (
	String name,
	String publicId,
	String systemId
    )
    {
	DomNotation notation;

	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	getNotations ();

	DomDocument.verifyXmlName (name);

	notation = new DomNotation (getOwnerDocument (),
		name, publicId, systemId);
	notations.setNamedItem (notation);
	return notation;
    }


    /**
     * <b>DOM L2</b>
     * Returns the internal subset of the document, as a string of unparsed
     * XML declarations (and comments, PIs, whitespace); or returns null if
     * there is no such subset.  There is no vendor-independent expectation
     * that this attribute be set, or that declarations found in it be
     * reflected in the <em>entities</em> or <em>notations</em> attributes
     * of this Document "Type" object.
     *
     * <p> Some application-specific XML profiles require that documents
     * only use specific PUBLIC identifiers, without an internal subset
     * to modify the interperetation of the declarations associated with
     * that PUBLIC identifier through some standard.
     */
    public String getInternalSubset ()
    {
	return subset;
    }
    

    /**
     * Sets the internal "readonly" flag so the node and its associated
     * data (only lists of entities and notations, no type information
     * at the moment) can't be changed.
     */
    public void makeReadonly ()
    {
	super.makeReadonly ();
	if (entities != null)
	    entities.makeReadonly ();
	if (notations != null)
	    notations.makeReadonly ();
    }


    /**
     * <b>DOM L2</b>
     * Consults the DOM implementation to determine if the requested
     * feature is supported.
     */
    final public boolean supports (String feature, String version)
    {
	return implementation.hasFeature (feature, version);
    }

    
    /**
     * Returns the implementation associated with this document type.
     */
    final public DOMImplementation getImplementation ()
    {
	return implementation;
    }


    // Yeech.  Package-private hooks, I don't like this.
    // For all that it's better than making this stuff a
    // public API...


    // package private
    ElementInfo getElementInfo (String element)
    {
	ElementInfo	info = (ElementInfo) elements.get (element);

	if (info != null)
	    return info;
	info = new ElementInfo (this);
	elements.put (element, info);
	return info;
    }

    void setHasIds () { ids = true; }
    boolean hasIds () { return ids; }

    // package private
    static class ElementInfo extends Hashtable
    {
	private String		idAttrName;
	private DomDoctype	doctype;

	// is-a vs has-a ... just to minimize number of objects.
	// keys in table are attribute names, values are defaults.

	ElementInfo (DomDoctype dt) { super (5, 5); doctype = dt; }

	void setAttrDefault (String attName, String value)
	{
	    if (getAttrDefault (attName) == null)
		put (attName, value);
	}
	String getAttrDefault (String attName)
	    { return (String) get (attName); }

	void setIdAttr (String attName)
	{
	    if (idAttrName == null)
		idAttrName = attName;
	    doctype.setHasIds ();
	}
	String getIdAttr ()
	    { return idAttrName; }
    }

    public boolean isSameNode (Node arg)
      {
        if (equals (arg))
          {
            return true;
          }
        if (!(arg instanceof DocumentType))
          {
            return false;
          }
        DocumentType doctype = (DocumentType) arg;
        if (!equal (getPublicId (), doctype.getPublicId ()))
          {
            return false;
          }
        if (!equal (getSystemId (), doctype.getSystemId ()))
          {
            return false;
          }
        if (!equal (getInternalSubset (), doctype.getInternalSubset ()))
          {
            return false;
          }
        // TODO entities
        // TODO notations
        return true;
      }

}
