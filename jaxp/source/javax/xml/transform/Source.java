/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 Andrew Selkirk

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

package javax.xml.transform;

/**
 * Identifies the URI for either a transformation (XSLT stylesheet)
 * or an input to a transformation (XML document to be transformed).
 *
 * @author	Andrew Selkirk, David Brownell
 * @version	1.0
 */
public interface Source {

	//-------------------------------------------------------------
	// Interface: Source ------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Returns the URI for this source.  Some sources may not need URIs,
	 * for example ones provided as an input stream, but such URIs
	 * are important for resolving relative URIs and for providing
	 * usable diagnostics.
	 */
	public String getSystemId();

	/**
	 * Associates a URI with this source.
	 *
	 * @param systemID the URI
	 */
	public void setSystemId(String systemID);


} // Source

