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
import java.io.IOException;
import java.io.OutputStream;
import java.awt.datatransfer.DataFlavor;

/**
 * Data Content Handler.
 */
public abstract interface DataContentHandler
{

  //-------------------------------------------------------------
  // Interface: DataContentHandler ------------------------------
  //-------------------------------------------------------------

  /**
   * Get content object from data source.
   * @param source Data source
   * @returns Object
   * @throws IOException IO exception occurred
   */
  public abstract Object getContent(DataSource source)
  throws IOException;

  /**
   * Write object to stream.
   * @param object Object to write
   * @param mimeType MIME Type of object
   * @param stream Output stream
   * @throws IOException IO exception occurred
   */
  public abstract void writeTo(Object object, String mimeType,OutputStream stream)
  throws IOException;

  /**
   * Get transfer data.
   * @param flavor Data flavor
   * @param source Data source
   * @throws IOException IO exception occurred
   */
  public abstract Object getTransferData(DataFlavor flavor,DataSource source)
  throws IOException;

  /**
   * Get list of transfer data flavors.
   * @returns Array listing of data transfer flavors
   */
  public abstract DataFlavor[] getTransferDataFlavors();


} // DataContentHandler
