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
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import javax.xml.transform.Source;

/**
 * SAX Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SAXSource implements Source {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.sax.SAXSource/feature";

	private XMLReader	reader		= null;
	private InputSource	inputSource	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public SAXSource() {
	} // SAXSource()

	public SAXSource(XMLReader reader, InputSource source) {
		this.reader = reader;
		this.inputSource = source;
	} // SAXSource()

	public SAXSource(InputSource source) {
		this.inputSource = source;
	} // SAXSource()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public void setXMLReader(XMLReader reader) {
		this.reader = reader;
	} // setXMLReader()

	public XMLReader getXMLReader() {
		return reader;
	} // getXMLReader()

	public void setInputSource(InputSource source) {
		this.inputSource = source;
	} // setInputSource()

	public InputSource getInputSource() {
		return inputSource;
	} // inputSource()

	public void setSystemId(String systemID) {
		if (inputSource != null) {
			inputSource.setSystemId(systemID);
		}
	} // setSystemId()

	public String getSystemId() {
		if (inputSource != null) {
			return inputSource.getSystemId();
		} // if
		return null;
	} // getSystemId()

	public static InputSource sourceToInputSource(Source source) {
		return null; // TODO
	} // sourceToInputSource()


} // SAXSource


