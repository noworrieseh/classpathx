/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;
import java.net.*;

/**
 * URL Data Source.
 */
public class URLDataSource
implements DataSource
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * URL.
   */
  private URL url = null;

  /**
   * URL connection.
   */
  private URLConnection url_conn = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new URL data source from URL. 
   * @param url URL
   */
  public URLDataSource(URL url) 
  {
    this.url = url;
  } // URLDataSource()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @returns Name of data source
   */
  public String getName() 
  {
    return url.getFile();
  } // getName()

  /**
   * Get URL.
   * @returns URL of data source
   */
  public URL getURL() 
  {
    return url;
  } // getURL()

  /**
   * Get input stream.
   * @returns Input stream of data source
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    if (url_conn == null) 
    {
      url_conn = url.openConnection();
    }
    return url_conn.getInputStream();
  } // getInputStream()

  /**
   * Get content type of URL data source.
   * @returns Content type
   */
  public String getContentType() 
  {

    // Variables
    String type;

    try 
    {
      if (url_conn == null) 
      {
	url_conn = url.openConnection();
      }
      type = url_conn.getContentType();
      if (type != null) 
      {
	return type;
      }
    } catch (Exception e) 
    {
    }
    return "application/octet-stream";
  } // getContentType()

  /**
   * Get output stream.
   * @returns Output stream of data source
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {
    if (url_conn == null) 
    {
      url_conn = url.openConnection();
    }
    return url_conn.getOutputStream();
  } // getOutputStream()


} // URLDataSource
