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

// Imports
import java.util.Properties;

/**
 * Transformer
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class Transformer {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	protected Transformer() {
	} // Transformer()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract void transform(Source source, Result result) 
		throws TransformerException;

	public abstract void setParameter(String name, Object value);

	public abstract Object getParameter(String name);

	public abstract void clearParameters();

	public abstract void setURIResolver(URIResolver resolver);

	public abstract URIResolver getURIResolver();

	public abstract void setOutputProperties(Properties outputformat) 
		throws IllegalArgumentException;

	public abstract Properties getOutputProperties();

	public abstract void setOutputProperty(String name, String value) 
		throws IllegalArgumentException;

	public abstract String getOutputProperty(String name) 
		throws IllegalArgumentException;

	public abstract void setErrorListener(ErrorListener listener) 
		throws IllegalArgumentException;

	public abstract ErrorListener getErrorListener();


} // Transformer

