/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk, Nic Ferrier

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
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import gnu.activation.MIMETypeParser;

/**
 * MimetypesFileTypeMap is the default data typing engine of files for
 * the activation framework.  The engine maps filename extensions to
 * data types using the mime.type file format.  The registry is
 * constructed from a number of <code>mime.type</code> files from
 * various locations, as well as, programmically from data streams
 * and API calls.
 *
 * <h4>MIME Type registry file locations</h4>
 * <p>The following files make up the default MIME Type registry:
 * <ul>
 * <li><i>user's home</i>/.mime.types
 * <li><i>java.home</i>/lib/mime.types
 * </ul>
 * and if the application is stored in a jar file:
 * <ul>
 * <li>/META-INF/mime.types
 * <li>/META-INF/mimetypes.default
 * </ul>
 * </p>
 *
 * <p><h4>Command line testing</h4>
 * This class has a <code>main</code> method so it's possible to test some
 * of the funtionality. Supply a content type on the command line to find
 * all the matching extensions in the (automatically constructed) database.
 * </p>
 *
 * @author Andrew Selkirk
 * @author Nic Ferrier
 * @version $Revision: 1.8 $
 */
public class MimetypesFileTypeMap extends FileTypeMap
{

  //-------------------------------------------------------------
  // Constants --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * The default MIME type to report if the engine is unable to
   * identify the content type of the file based on the file extension.
   */
  private static final String defaultMIMEType = "application/octet-stream";

  /**
   * Registry index to the programmically added MIME Type entries
   */
  private static final int PROG = 0;

  /**
   * Registry index to the MIME Type entries from the user's home
   * directory.  <i>user's home</i>/.mime.types
   */
  private static final int USERHOME = 1;

  /**
   * Registry index to the java system MIME Types entries.
   */
  private static final int SYSTEM = 2;

  /**
   * Registry index to the MIME Type entries stored in an
   * applications jar'd archive.
   */
  private static final int ARCHIVE = 3;

  /**
   * Registry index to the default MIME Type entries stored
   * in the activation framework's jar archive.
   */
  private static final int DEFAULT = 4;



  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * MIME Types registry.  The registry is indexed according to the
   * source of the MIME Types regsitry information.  Refer to the
   * constants: PROG, USERHOME, SYSTEM, ARCHIVE, DEFAULT.  Each
   * registry is a mapping of file extensions to MIME type.
   */
  private Hashtable[] registry = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create a default MIME Types registry.
   */
  public MimetypesFileTypeMap()
  {
    initializeRegistry();
  } // MimetypesFileTypeMap()

  /**
   * Create a MIME Types registry with further entries loaded
   * from the specified input stream.
   * @param stream MIME Types formatted data stream
   */
  public MimetypesFileTypeMap(InputStream stream)
  {

    // Initialize the Registry
    initializeRegistry();

    // Load entries from stream
    if (stream != null) {
      registry[PROG] = MIMETypeParser.parseStream(new InputStreamReader(stream));
    } else {
      System.err.println("Activation: Unable to load from stream");
    } // if

  } // MimetypesFileTypeMap()

  /**
   * Create a MIME Types registry with further entries loaded
   * from the specified file
   * @param mimeTypeFileName MIME Types formatted file
   * @throws IOException IO problem occurred loading the file
   */
  public MimetypesFileTypeMap(String filename) throws IOException
  {

    // Variables
    File  file;

    // Initialize the Registry
    initializeRegistry();

    // Load entries from file
    file = new File(filename);
    if (file.exists() == true) {
      registry[PROG] = MIMETypeParser.parseStream(new FileReader(filename));
    } else {
      System.err.println("Activation: Unable to load from file " + filename);
    } // if

  } // MimetypesFileTypeMap()

  /**
   * Initialize the MIME Type registry database entries.
   */
  private void initializeRegistry() {

    // Variables
    File        file;
    InputStream stream;
    String      sep;
    String      location;
    ClassLoader loader;

    // NOTES: (1) Check into the use of separators here, especially with
    // the resources in jars.  I remember something fishy about using
    // '\' characters with jar files (or more precise, non '/' chars).
    // I also recall that resources load fine with '/' under both
    // win and linux?  why?
    // (2) Are we grabbing the correct class loader here?
    // (3) Checking for Exception...yuck...

    // Initialize
    registry = new Hashtable[5];
    sep = System.getProperty("file.separator");

    // Initialize the programmic registry
    registry[PROG] = new Hashtable();

    // Load User Home Entries
    location = System.getProperty("user.home") +
        sep + ".mime.types";
    try {
      file = new File(location);
      registry[USERHOME] = MIMETypeParser.parseStream(new FileReader(file));
    } catch (Exception e) {
      registry[USERHOME] = new Hashtable();
    } // try

    // Load Java's System Entries
    location = System.getProperty("java.home") +
        sep + "lib" + sep + "mime.types";
    try {
      file = new File(location);
      registry[SYSTEM] = MIMETypeParser.parseStream(new FileReader(file));
    } catch (Exception e) {
      registry[SYSTEM] = new Hashtable();
    } // try

    // Load Application Jar Entries
    loader = getClass().getClassLoader();
    location = "META-INF" + sep + "mime.types";
    try {
      stream = loader.getResourceAsStream(location);
      registry[ARCHIVE] = MIMETypeParser.parseStream(new InputStreamReader(stream));
    } catch (Exception e){
      // Safe to ignore
      registry[ARCHIVE] = new Hashtable();
    } // try

    // Load Default Activation Entries
    location = "META-INF" + sep + "mimetypes.default";
    try {
      stream = loader.getResourceAsStream(location);
      registry[DEFAULT] = MIMETypeParser.parseStream(new InputStreamReader(stream));
    } catch (Exception e) {
      System.err.println("Activation: Unable to locate " + location);
      registry[DEFAULT] = new Hashtable();
    } // try

  } // initializeRegistry()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Get the content type of the specified file.
   * @param file File to check
   * @return MIME Type of the file
   * @see #getContentType(String) which is called with the file's name
   */
  public String getContentType(File file)
  {
    return getContentType(file.getName());
  } // getContentType()

  /**
   * Get the content type of the specified file.  Each of the
   * registries are successively checked to locate a matching
   * file extension to MIME Type content type.  The default
   * MIME type is returned if the extension is not located.
   * @param filename name to check
   * @return MIME Type of the file
   */
  public String getContentType(String filename)
  {

    // Variables
    int       index;
    String    extension;
    MimeType  mimeType;

    // Check if the filename has an extension
    index = filename.lastIndexOf(".");
    if (index == -1)
    {

      // Return the default MIME Type
      return defaultMIMEType;

    } // if

    // Extract the file extension from filename
    extension = filename.substring(index + 1);

    // Search MIME Type registry
    for (index = 0; index < registry.length; index++)
    {

      // Check for the MIME Type
      mimeType = (MimeType) (registry[index].get(extension));
      if (mimeType != null) {
        return mimeType.getBaseType();
      } // if

    } // for

    // Unable to locate extension is registry.  Return the
    // default MIME Type
    return defaultMIMEType;

  } // getContentType()

  /**
   * Add MIME Type entries to the programmic registry.  The string
   * must be a properly formatted MIME type file entry.
   * @param mimeTypes MIME Types formatted entry
   */
  public void addMimeTypes(String mimeTypes)
  {

    // Variables
    Reader  reader;

    try {
      reader = new StringReader(mimeTypes);
      registry[PROG].putAll(MIMETypeParser.parseStream(reader));
    } catch(Exception e) {
      // Don't bother with errors
    } // try

  } // addMimeTypes()

  /**
   * Takes a content type and finds all the extensions associated
   * with it. All the extensions are printed out, one per line.
   * @param argv Command-line arguments
   */
  public static void main(String[] argv)
  {
    MimetypesFileTypeMap fm = new MimetypesFileTypeMap();
    if (argv.length < 1)
    {
      //if no arguments then just die
      System.exit(0);
    }
    String contentType = argv[0];
    for (int i = 0; i < fm.registry.length; i++)
    {
      Enumeration exts = fm.registry[i].keys();
      Enumeration mimeTypes = fm.registry[i].elements();
      while (exts.hasMoreElements())
      {
        String extension = (String) exts.nextElement();
        MimeType mt = (MimeType) mimeTypes.nextElement();
        String baseType = mt.getBaseType();
        if (baseType.equals(contentType))
        {
          System.out.println(contentType + " " + extension);
        }
      }
    }
  } // main()


} // MimetypesFileTypeMap
