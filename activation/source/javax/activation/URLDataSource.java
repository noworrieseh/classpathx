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
import java.io.*;
import java.net.*;

/** a data source based on a URL.
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 */
public class URLDataSource
implements DataSource
{

  /**
   * URL.
   */
  private URL url = null;

  /**
   * URL connection.
   */
  private URLConnection url_conn = null;


  /**
   * Create new URL data source from URL. 
   * @param url URL
   */
  public URLDataSource(URL url) 
  {
    this.url = url;
  } // URLDataSource()


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
