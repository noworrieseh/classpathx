/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

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
