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


/**
 * <p> "Notation" implementation.  This is a non-core DOM class, supporting
 * the "XML" feature. </p>
 *
 * <p> Although unparsed entities using this notation can be detected using
 * DOM, neither NOTATIONS nor ENTITY/ENTITIES attributes can be so detected.
 * More, there is no portable way to construct a Notation node, so there's
 * no way that vendor-neutral DOM construction APIs could even report a
 * NOTATION used to identify the intended meaning of a ProcessingInstruction.
 * </p>
 *
 * <p> In short, <em>avoid using this DOM functionality</em>.
 *
 * @see DomDoctype
 * @see DomEntity
 * @see DomPI
 *
 * @author David Brownell 
 */
public class DomNotation extends DomExtern implements Notation
{
    /**
     * Constructs a Notation node associated with the specified document,
     * with the specified descriptive data.  Note that at least one of
     * the PUBLIC and SYSTEM identifiers must be provided; unlike other
     * external objects in XML, notations may have only a PUBLIC identifier.
     *
     * <p>This constructor should only be invoked by a DomDoctype object
     * as part of its declareNotation functionality, or through a subclass
     * which is similarly used in a "Sub-DOM" style layer. 
     *
     * @param owner The document with which this notation is associated
     * @param name Name of this notation
     * @param publicId If non-null, provides the notation's PUBLIC identifier
     * @param systemId If non-null, rovides the notation's SYSTEM identifier
     */
    protected DomNotation (
	Document owner,
	String name,
	String publicId,
	String systemId
    )
    {
	super (owner, name, publicId, systemId);
	makeReadonly ();
    }

    /**
     * <b>DOM L1</b>
     * Returns the constant NOTATION_NODE.
     */
    final public short getNodeType ()
	{ return NOTATION_NODE; }
}
