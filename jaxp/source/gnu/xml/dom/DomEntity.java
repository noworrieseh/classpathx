/*
 * $Id: DomEntity.java,v 1.1 2001-06-20 21:30:05 db Exp $
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


// $Id: DomEntity.java,v 1.1 2001-06-20 21:30:05 db Exp $

/**
 * <p> "Entity" implementation.  This is a non-core DOM class, supporting the
 * "XML" feature.  There are two types of entities, neither of which works
 * particularly well in this API:</p><dl>
 *
 * <dt><em>Unparsed Entities</em></dt>
 *	<dd>Since ENTITY/ENTITIES attributes, the only legal use of unparsed
 *	entities in XML, can't be detected with DOM, there isn't much point in
 *	trying to use unparsed entities in DOM applications.  (XML Linking is
 *	working to provide a better version of this functionality.) </dd>
 *
 * <dt><em>Parsed Entities</em></dt>
 *	<dd> While the DOM specification permits nodes for parsed entities
 *	to have a readonly set of children, this is not required and there
 *	is no portable way to provide such children.  <em>This implementation
 *	currently does not permit children to be added to Entities.</em>
 *	There are related issues with the use of EntityReference nodes.  </dd>
 *
 * </dl>
 *
 * <p> In short, <em>avoid using this DOM functionality</em>.
 *
 * @see DomDoctype
 * @see DomEntityReference
 * @see DomNotation
 *
 * @author David Brownell 
 * @version $Date: 2001-06-20 21:30:05 $
 */
public class DomEntity extends DomExtern implements Entity
{
    private String	notation;


    /**
     * Constructs an Entity node associated with the specified document,
     * with the specified descriptive data.
     *
     * <p>This constructor should only be invoked by a DomDoctype as part
     * of its declareEntity functionality, or through a subclass which is
     * similarly used in a "Sub-DOM" style layer.
     *
     * @param owner The document with which this entity is associated
     * @param name Name of this entity
     * @param publicId If non-null, provides the entity's PUBLIC identifier
     * @param systemId Provides the entity's SYSTEM identifier (URI)
     * @param notation If non-null, provides the unparsed entity's notation.
     */
    protected DomEntity (
	Document owner,
	String name,
	String publicId,
	String systemId,
	String notation
    )
    {
	super (owner, ENTITY_NODE, name, publicId, systemId);
	this.notation = notation;

	// NOTE:  if notation == null, this is a parsed entity
	// which could reasonably be given child nodes ...
	makeReadonly ();
    }


    /**
     * <b>DOM L1</b>
     * Returns the NOTATION identifier associated with this entity, if any.
     */
    final public String getNotationName ()
    {
	return notation;
    }
}
