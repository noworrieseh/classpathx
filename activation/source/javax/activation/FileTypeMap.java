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
 * File Type Map.
 */
public abstract class FileTypeMap 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create default file type map.
   */
  private static FileTypeMap defaultMap = new MimetypesFileTypeMap();


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new file type map.
   */
  public FileTypeMap() 
  {
  } // FileTypeMap()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get content type.
   * @param filename Filename
   */
  public abstract String getContentType(String filename);

  /**
   * Get content type.
   * @param file File source
   */
  public abstract String getContentType(File file);

  /**
   * Get default file type map.
   * @returns Default file type map
   */
  public static FileTypeMap getDefaultFileTypeMap() 
  {
    return defaultMap;
  } // getDefaultFileTypeMap()

  /**
   * Set default file type map.
   * @param map New default file type map
   */
  public static void setDefaultFileTypeMap(FileTypeMap map) 
  {
    defaultMap = map;
  } // setDefaultFileTypeMap()


} // FileTypeMap
