/*
 * $Id: DomFragment.java,v 1.1 2001-06-20 21:30:05 db Exp $
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


// $Id: DomFragment.java,v 1.1 2001-06-20 21:30:05 db Exp $

/**
 * <p> "DocumentFragment" implementation.  </p>
 *
 * @author David Brownell 
 * @version $Date: 2001-06-20 21:30:05 $
 */
public class DomFragment extends DomNode implements DocumentFragment
{
    /**
     * Constructs a DocumentFragment node associated with the
     * specified document.
     *
     * <p>This constructor should only be invoked by a Document as part of
     * its createDocumentFragment functionality, or through a subclass which
     * is similarly used in a "Sub-DOM" style layer.
     */
    protected DomFragment (Document owner)
    {
	super (owner, DOCUMENT_FRAGMENT_NODE);
    }


    /**
     * <b>DOM L1</b>
     * Returns the string "#document-fragment".
     */
    final public String getNodeName ()
    {
	return "#document-fragment";
    }
}
