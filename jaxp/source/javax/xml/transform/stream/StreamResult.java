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
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import javax.xml.transform.Result;

/**
 * Stream Result
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class StreamResult implements Result {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final String FEATURE =
		"http://javax.xml.transform.stream.StreamResult/feature";

	private String		systemId	= null;
	private OutputStream	outputStream	= null;
	private Writer		writer		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public StreamResult() {
	} // StreamResult()

	public StreamResult(OutputStream stream) {
		this.outputStream = stream;
	} // StreamResult()

	public StreamResult(Writer writer) {
		this.writer = writer;
	} // StreamResult()

	public StreamResult(String systemID) {
		this.systemId = systemID;
	} // StreamResult()

	public StreamResult(File file) {
		this(file.getName());
	} // StreamResult()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public OutputStream getOutputStream() {
		return outputStream;
	} // getOutputStream()

	public void setOutputStream(OutputStream stream) {
		this.outputStream = stream;
	} // setOutputStream()

	public void setWriter(Writer writer) {
		this.writer = writer;
	} // setWriter()

	public Writer getWriter() {
		return writer;
	} // getWriter()

	public void setSystemId(String systemID) {
		this.systemId = systemID;
	} // setSystemId()

	public void setSystemId(File file) {
		this.systemId = file.getName();
	} // setSystemId()

	public String getSystemId() {
		return systemId;
	} // getSystemId()


} // StreamResult


