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

package javax.xml.transform.sax;

// Imports
import org.xml.sax.XMLFilter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

/**
 * SAX Transformer Factory
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class SAXTransformerFactory extends TransformerFactory {

	//-------------------------------------------------------------
	// Constants --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.sax.SAXTransformerFactory/feature";

	public static final String FEATURE_XMLFILTER =
		"http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter";


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------
	protected SAXTransformerFactory() {
	} // SAXTransformerFactory()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public abstract TransformerHandler newTransformerHandler(Source source)
		throws TransformerConfigurationException;

	public abstract TransformerHandler newTransformerHandler(Templates templates)
		throws TransformerConfigurationException;

	public abstract TransformerHandler newTransformerHandler()
		throws TransformerConfigurationException;

	public abstract TemplatesHandler newTemplatesHandler()
		throws TransformerConfigurationException;

	public abstract XMLFilter newXMLFilter(Source source)
		throws TransformerConfigurationException;

	public abstract XMLFilter newXMLFilter(Templates templates)
		throws TransformerConfigurationException;


} // SAXTransformerFactory

