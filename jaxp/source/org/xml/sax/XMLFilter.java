/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 David Brownell

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

// XMLFilter.java - filter SAX2 events.
// Written by David Megginson, sax@megginson.com

// $Id: XMLFilter.java,v 1.2 2001-06-08 20:42:17 db Exp $


package org.xml.sax;


/**
 * Interface for an XML filter.
 *
 * <p>An XML filter is like an XML reader, except that it obtains its
 * events from another XML reader rather than a primary source like
 * an XML document or database.  Filters can modify a stream of
 * events as they pass on to the final application.</p>
 *
 * <p>The XMLFilterImpl helper class provides a convenient base
 * for creating SAX2 filters, by passing on all {@link org.xml.sax.EntityResolver
 * EntityResolver}, {@link org.xml.sax.DTDHandler DTDHandler},
 * {@link org.xml.sax.ContentHandler ContentHandler} and {@link org.xml.sax.ErrorHandler
 * ErrorHandler} events automatically.</p>
 *
 * @since SAX 2.0
 * @author David Megginson, 
 *         <a href="mailto:sax@megginson.com">sax@megginson.com</a>
 * @version 2.0
 * @see org.xml.sax.helpers.XMLFilterImpl
 */
public interface XMLFilter extends XMLReader
{

    /**
     * Set the parent reader.
     *
     * <p>This method allows the application to link the filter to
     * a parent reader (which may be another filter).  The argument
     * may not be null.</p>
     *
     * @param parent The parent reader.
     */
    public abstract void setParent (XMLReader parent);


    /**
     * Get the parent reader.
     *
     * <p>This method allows the application to query the parent
     * reader (which may be another filter).  It is generally a
     * bad idea to perform any operations on the parent reader
     * directly: they should all pass through this filter.</p>
     *
     * @return The parent filter, or null if none has been set.
     */
    public abstract XMLReader getParent ();

}

// end of XMLFilter.java
