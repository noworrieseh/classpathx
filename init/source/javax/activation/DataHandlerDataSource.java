/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;

/**
 * Data handler data source.  Use of this class is currently unknown.
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
   * @returns Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    return dataHandler.getInputStream();
  } // getInputStream()

  /**
   * Get output stream.
   * @returns Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {
    return dataHandler.getOutputStream();
  } // getOutputStream()

  /**
   * Get content type.
   * @returns Content type
   */
  public String getContentType() 
  {
    return dataHandler.getContentType();
  } // getContentType()

  /**
   * Get name.
   * @returns Name
   */
  public String getName() 
  {
    return dataHandler.getName();
  } // getName()


} // DataHandlerDataSource
