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
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File Data Source.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
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
  private File file = null;

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
    this.file = file;
  } // FileDataSource()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @return Name
   */
  public String getName() 
  {
    return file.getName(); // TODO
  } // getName()

  /**
   * Get file.
   * @return File
   */
  public File getFile() 
  {
    return file;
  } // getFile()

  /**
   * Get input stream.
   * @return Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {
    return new FileInputStream(file); // TODO
  } // getInputStream()

  /**
   * Get content type.
   * @return Content type
   */
  public String getContentType() 
  {
    if (typeMap == null) 
    {
      return FileTypeMap.getDefaultFileTypeMap().getContentType(file);
    } else 
    {
      return typeMap.getContentType(file);
    }
  } // getContentType()

  /**
   * Get output stream.
   * @return Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {
    if (file.canWrite() == false)
    {
      throw new IOException("Cannot write");
    }
    return new FileOutputStream(file);
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
