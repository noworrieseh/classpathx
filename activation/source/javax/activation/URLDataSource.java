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
import java.net.URL;
import java.net.URLConnection;

/**
 * A data source based on a URL.
 *
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class URLDataSource
implements DataSource
{

  //-------------------------------------------------------------
  // Constants --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Encoding of the default content type for url data sources
   */
  private static final String DEFAULT_CONTENT = "application/octet-stream";


  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * URL to the datasource.
   */
  private URL url = null;

  /**
   * Cached URL connection
   */
  private URLConnection connection = null;


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
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name of the URL.
   * @return Name of the data source
   */
  public String getName()
  {
    return url.getFile();
  } // getName()

  /**
   * Get the URL.
   * @return URL of the data source
   */
  public URL getURL() 
  {
    return url;
  } // getURL()

  /**
   * Get input stream of the URL
   * @return Input stream of data source
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    if (connection == null)
    {
      connection = url.openConnection();
    }
    return connection.getInputStream();
  } // getInputStream()

  /**
   * Get the content type of the URL data source.
   * @return Content type
   */
  public String getContentType() 
  {

    // Variables
    String type;

    try 
    {

      // Check if a connection has been established
      if (connection == null)
      {
        connection = url.openConnection();
      }

      // Determine the Content type
      type = connection.getContentType();
      if (type != null)
      {
        return type;
      }

    } catch (Exception e)
    {
    } // try

    // Return the default content type
    return DEFAULT_CONTENT;

  } // getContentType()

  /**
   * Get output stream.
   * @return Output stream of data source
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException
  {

    // Check if a connection has been established
    if (connection == null)
    {
      connection = url.openConnection();
    }

    // Return an output stream of the connection
    return connection.getOutputStream();

  } // getOutputStream()


} // URLDataSource
