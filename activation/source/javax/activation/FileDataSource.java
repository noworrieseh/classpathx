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

/**
 * File Data Source.
 */
public class FileDataSource
implements DataSource
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * File source.
   */
  private File _file = null;

  /**
   * Content type map.
   */
  private FileTypeMap typeMap = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create file data source.
   * @param name Filename
   */
  public FileDataSource(String name) 
  {
    this(new File(name));
  } // FileDataSource()

  /**
   * Create file data source
   * @param file File source
   */
  public FileDataSource(File file) 
  {
    _file = file;
  } // FileDataSource()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @returns Name
   */
  public String getName() 
  {
    return _file.getName(); // TODO
  } // getName()

  /**
   * Get file.
   * @returns File
   */
  public File getFile() 
  {
    return _file;
  } // getFile()

  /**
   * Get input stream.
   * @returns Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    return new FileInputStream(_file); // TODO
  } // getInputStream()

  /**
   * Get content type.
   * @returns Content type
   */
  public String getContentType() 
  {
    if (typeMap == null) 
    {
      return FileTypeMap.getDefaultFileTypeMap().getContentType(_file);
    } else 
    {
      return typeMap.getContentType(_file);
    }
  } // getContentType()

  /**
   * Get output stream.
   * @returns Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {
    if (_file.canWrite() == false) 
    {
      throw new IOException("Cannot write");
    }
    return new FileOutputStream(_file);
  } // getOutputStream()

  /**
   * Set file type map.
   * @param map File type map
   */
  public void setFileTypeMap(FileTypeMap map) 
  {
    typeMap = map;
  } // setFileTypeMap()


} // FileDataSource
