/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk

  For more information on the classpathx please mail:
  nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
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
package javax.activation;

// Imports
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Data handler data source.  Use of this class is currently unknown.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
class DataHandlerDataSource
implements DataSource 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Reference to data handler
   */
  DataHandler dataHandler;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new data handler data source.
   * @param handler Data handler
   */
  public DataHandlerDataSource(DataHandler handler) 
  {
    dataHandler = handler;
  } // DataHandlerDataSource()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get input stream.
   * @return Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    return dataHandler.getInputStream();
  } // getInputStream()

  /**
   * Get output stream.
   * @return Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {
    return dataHandler.getOutputStream();
  } // getOutputStream()

  /**
   * Get content type.
   * @return Content type
   */
  public String getContentType() 
  {
    return dataHandler.getContentType();
  } // getContentType()

  /**
   * Get name.
   * @return Name
   */
  public String getName() 
  {
    return dataHandler.getName();
  } // getName()


} // DataHandlerDataSource
