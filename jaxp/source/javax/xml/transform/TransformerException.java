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
import java.io.*;

/**
 * TransformerException
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class TransformerException extends Exception {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	SourceLocator	locator			= null;
	private Exception	containedException	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public TransformerException(String msg) {
		super(msg);
	} // TransformerException()

	public TransformerException(Exception ex) {
		super();
		containedException = ex;
	} // TransformerException()

	public TransformerException(String msg, Exception ex) {
		super(msg);
		containedException = ex;
	} // TransformerException()

	public TransformerException(String msg, SourceLocator locator) {
		super(msg);
		this.locator = locator;
	} // TransformerException()

	public TransformerException(String msg, SourceLocator locator, 
				Exception ex) {
		super(msg);
		this.locator = locator;
		containedException = ex;
	} // TransformerException()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public Exception getException() {
		return containedException;
	} // getException()

	public SourceLocator getLocator() {
		return locator;
	} // getLocator()

	public void printStackTrace() {
		printStackTrace(System.out);
	} // printStackTrace()

	public void printStackTrace(PrintStream stream) {
		printStackTrace(new PrintWriter(
			new OutputStreamWriter(stream)));
	} // printStackTrace()

	public void printStackTrace(PrintWriter writer) {
		if (containedException != null) {
			containedException.printStackTrace(writer);
		} // if
		super.printStackTrace(writer);
	} // printStackTrace()

	public Throwable getCause() {
		return containedException;
	} // getCause()

	public synchronized Throwable initCause(Throwable cause) {
		return null; // TODO
	} // initCause()

	public String getMessageAndLocation() {
		return null; // TODO
	} // getMessageAndLocation()

	public String getLocationAsString() {
		return null; // TODO
	} // getLocationAsString()


} // TranformerException


