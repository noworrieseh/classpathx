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
import java.awt.datatransfer.DataFlavor;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Object Data Content Handler.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public class ObjectDataContentHandler
implements DataContentHandler 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Transfer data flavors list.
   */
  private DataFlavor[] transferFlavors;

  /**
   * Object.
   */
  private Object obj;

  /**
   * MIME type of object.
   */
  private String mimeType;

  /**
   * Data content handler.
   */
  private DataContentHandler handler;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new object data content handler.
   * @param handler Data content handler
   * @param object Object
   * @param mimetype MIME Type
   */
  public ObjectDataContentHandler(DataContentHandler handler,
    Object object, String mimetype)
  {
    this.handler = handler;
    obj = object;
    mimeType = mimetype;
  } // ObjectDataContentHandler()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get data content handler.
   * @return Data content handler
   */
  public DataContentHandler getDCH() 
  {
    return handler;
  } // getDCH()

  /**
   * Get transfer data flavors.
   * @return List of transfer data flavors
   */
  public DataFlavor[] getTransferDataFlavors() 
  {
    return transferFlavors;
  } // getTransferDataFlavors()

  /**
   * Get transfer data flavor.
   * @param flavor Data flavor
   * @param source Data source
   * @return TODO
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
   * @return Object content
   */
  public Object getContent(DataSource source) 
  {
    return null; // TODO
  } // getContent()

  /**
   * Write to.
   * @param object Object to write
   * @param mimeType MIME type of object
   * @param stream Output stream to write to
   * @throws IOException IO exception occurred
   */
  public void writeTo(Object object, String mimeType, OutputStream stream)
  throws IOException 
  {
  } // writeTo()


} // ObjectDataContentHandler
