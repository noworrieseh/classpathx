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

/**
 * File Type Map abstract class provides an interface to the data
 * typing of data files.  The class is designed to allow the
 * plugging in of any algorithm as the system default engine to
 * determine the data content type of files.  By default, the
 * activation framework uses the MimeTypeFileTypeMap.
 *
 * @author Andrew Selkirk
 * @version $Revision: 1.4 $
 */
public abstract class FileTypeMap 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Reference to the default engine for mapping files to data types
   */
  private static FileTypeMap defaultFileTypeMap = new MimetypesFileTypeMap();


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Default constructor of FileTypeMap
   */
  public FileTypeMap() 
  {
  } // FileTypeMap()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get the MIME content type of the specified file
   * @param filename Filename of file to query
   * @return MIME content type of file
   */
  public abstract String getContentType(String filename);

  /**
   * Get the MIME content type of the specified file.
   * @param file File to query
   * @return MIME content type of file
   */
  public abstract String getContentType(File file);

  /**
   * Get the system default file type mapping algorithm.  Unless
   * otherwise modified, this will be an instance of the
   * MimeTypesFileTypeMap engine.
   * @return System default file type map
   * @see javax.activation.MimeTypesFileTypeMap
   * @see setDefaultFileTypeMap(javax.activation.FileTypeMap)
   */
  public static FileTypeMap getDefaultFileTypeMap()
  {
    return defaultFileTypeMap;
  } // getDefaultFileTypeMap()

  /**
   * Set the system default file type mapping algorithm.
   * @param fileTypeMap File type map to set as the system default
   * @throws SecurityException Unsufficient permission to change
   * the system default
   * @see getDefaultFileTypeMap()
   */
  public static void setDefaultFileTypeMap(FileTypeMap fileTypeMap)
  {
    defaultFileTypeMap = fileTypeMap;
  } // setDefaultFileTypeMap()


} // FileTypeMap
