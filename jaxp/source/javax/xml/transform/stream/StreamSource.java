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

package javax.xml.transform.stream;

// Imports
import java.io.InputStream;
import java.io.Reader;
import java.io.File;
import javax.xml.transform.Source;

/**
 * Stream Source
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class StreamSource implements Source {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.stream.StreamSource/feature";

	private String		publicId	= null;
	private String		systemId	= null;
	private InputStream	inputStream	= null;
	private Reader		reader		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public StreamSource() {
	} // StreamSource()

	public StreamSource(InputStream stream) {
		this.inputStream = stream;
	} // StreamSource()

	public StreamSource(InputStream stream, String systemID) {
		this.inputStream = stream;
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(Reader reader) {
		this.reader = reader;
	} // StreamSource()

	public StreamSource(Reader reader, String systemID) {
		this.reader = reader;
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(String systemID) {
		this.systemId = systemID;
	} // StreamSource()

	public StreamSource(File file) {
		this(file.getName());
	} // StreamSource()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public InputStream getInputStream() {
		return inputStream;
	} // getInputStream()

	public void setInputStream(InputStream stream) {
		this.inputStream = stream;
	} // setInputStream()

	public void setReader(Reader reader) {
		this.reader = reader;
	} // setReader()

	public Reader getReader() {
		return reader;
	} // getReader()

	public void setPublicId(String publicID) {
		this.publicId = publicID;
	} // setPublicId()

	public String getPublicId() {
		return publicId;
	} // getPublicId()

	public void setSystemId(String systemID) {
		this.systemId = systemID;
	} // setSystemId()

	public void setSystemId(File file) {
		this.systemId = file.getName();
	} // setSystemId()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // StreamSource

