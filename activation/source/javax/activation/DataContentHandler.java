/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

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
