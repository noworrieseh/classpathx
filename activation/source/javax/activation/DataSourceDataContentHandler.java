/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk

  For more information on the classpathx please mail: nferrier@tapsellferrier.co.uk

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
import java.awt.datatransfer.*;
import java.io.*;

class DataSourceDataContentHandler
implements DataContentHandler 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Data source.
   */
  private DataSource ds;

  /**
   * List of adata flavors.
   */
  private DataFlavor[] transferFlavors;

  /**
   * Data content handler.
   */
  private DataContentHandler dhc;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create data source data content handler.
   * @param handler Data content handler
   * @param source Data source
   */
  public DataSourceDataContentHandler(DataContentHandler handler,DataSource source) 
  {
    dhc = handler;
    ds = source;
  } // DataSourceDataContentHandler()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get transfer data flavors.
   * @returns List of data flavors
   */
  public DataFlavor[] getTransferDataFlavors() 
  {
    return transferFlavors;
  } // getTransferDataFlavors()

  /**
   * Get transfer data based on data flavor and data source
   * @param flavor Data flavor
   * @param source Data source
   * @returns Transfer data
   * @throws IOException IO exception occurred
   */
  public Object getTransferData(DataFlavor flavor, DataSource source) 
  throws IOException 
  {
    return null; // TODO
  } // getTransferData()

  /**
   * Get content.
   * @param source Data source
   * @returns Content object
   * @throws IOException IO exception occurred
   */
  public Object getContent(DataSource source) throws IOException 
  {
    return null; // TODO
  } // getContent()

  /**
   * Write to.
   * @param object Object to write
   * @param mimeType MIME type
   * @param stream Output stream
   */
  public void writeTo(Object object, String mimeType, OutputStream stream)
  throws IOException 
  {
  } // writeTo()


} // DataSourceDataContentHandler
