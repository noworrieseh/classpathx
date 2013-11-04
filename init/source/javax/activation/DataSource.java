/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Data Source.
 */
public abstract interface DataSource
{

  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @returns Name
   */
  public abstract String getName();

  /**
   * Get input stream.
   * @returns Input stream
   * @throws IOException IO exception occurred
   */
  public abstract InputStream getInputStream() throws IOException;

  /**
   * Get content type.
   * @returns Content type
   */
  public abstract String getContentType();

  /**
   * Get output stream.
   * @returns Output stream
   * @throws IOException IO exception occurred
   */
  public abstract OutputStream getOutputStream() throws IOException;


} // DataSource
