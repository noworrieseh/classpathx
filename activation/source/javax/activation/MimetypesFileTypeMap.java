/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk, Nic Ferrier

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


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;


/** maps MIME types to "filename" extensions.
 * The entire MIME type registry for an application can either be made
 * from the contents of a stream (programmatic registry) or it can be
 * constructed from a number of <code>mime.type</code> files in
 * different locations.
 *
 * <h4>MIME registry file locations</h4>
 * <p>The following files make up the default MIME registry:
 * <ul>
 * <li>~/.mime.types
 * <li><i>java.home</i>/lib/mime.types
 * </ul>
 * and if the application is stored in a jar file:
 * <ul>
 * <li>/META-INF/mime.types
 * <li>/META-INF/mimetypes.default
 * </ul>
 * </p>
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 * @author Nic Ferrier: nferrier@tapsellferrier.co.uk
 */
public class MimetypesFileTypeMap
extends FileTypeMap 
{

  /** the standard mime type registry.
   */
  private static Hashtable defDB = null;

  /** the default MIME type.
   */
  private static String defaultType = "application/octet-stream";

  /** the full registry.
   * Each index of the array is a hash of mime types read
   * from some location. If parsing the contents of the location
   * causes an error then the appropriate index will contain
   * an empty hashtable.
   */
  private Hashtable[] DB = null;

  /** index into the registry.
   */
  private static final int PROG = 0;
  private static final int HOME = 1;
  private static final int SYS = 2;
  private static final int JAR = 3;
  private static final int DEF = 4;


  /** create default MIME Types registry.
   */
  public MimetypesFileTypeMap() 
  {
    DB = new Hashtable[5];
    Properties properties=System.getProperties();
    String sep=properties.getProperty("file.separator");
    //init programmatic entries
    DB[PROG] = new Hashtable();
    //the user's mime types
    try
    {
      File userHome=new File(properties.getProperty("user.home")+sep+".mime.types");
      DB[HOME] = loadMimeRegistry(new FileReader(userHome));
    }
    catch(Exception e)
    {
      DB[HOME]=new Hashtable();
    }
    //the system mime types
    try
    {
      File javaHome=new File(properties.getProperty("java.home")+sep+"lib"+sep+"mime.types");
      DB[SYS] = loadMimeRegistry(new FileReader(javaHome));
    }
    catch(Exception e)
    {
      DB[SYS]=new Hashtable();
    }
    //a possible jar-file local copy of the mime types
    try
    {
      String resource="META-INF" +sep+ "mime.types";
      InputStream str=getClass().getClassLoader().getResourceAsStream(resource);
      DB[JAR]=loadMimeRegistry(new InputStreamReader(str));
    }
    catch(Exception e)
    {
      DB[JAR]=new Hashtable();
    }
    //the default providers... obtained from a possible META-INF/ location
    try
    {
      String resource="META-INF" + sep + "mimetypes.default";
      InputStream str=getClass().getClassLoader().getResourceAsStream(resource);
      DB[DEF]=loadMimeRegistry(new InputStreamReader(str));
    }
    catch(Exception e)
    {
      DB[DEF]=new Hashtable();
    }
  }

  /** create MIME Types registry from the stream.
   *
   * @param stream MIME Types file map formatted stream
   */
  public MimetypesFileTypeMap(InputStream stream) 
  {
    this();
    try 
    {
      DB[PROG] = loadMimeRegistry(new InputStreamReader(stream));
    }
    catch (Exception e) 
    {
    }
  }

  /** create MIME Types registry with entries from file
   *
   * @param mimeTypeFileName MIME Types file map formatted file name
   */
  public MimetypesFileTypeMap(String mimeTypeFileName) 
  {
    this();
    try 
    {
      DB[PROG] = loadMimeRegistry(new FileReader(mimeTypeFileName));
    }
    catch (Exception e) 
    {
    }
  }


  /** get content type of file.
   *
   * @param file File to check
   * @return Content type, or null
   * @see #getContentType(String) which is called with the file's name
   */
  public String getContentType(File file) 
  {
    return getContentType(file.getName());
  }

  /** get content type of file.
   * Looks up the extension of the file (returns default content type if
   * there is no extension) in successive registrys.
   *
   * @param filename name to check
   * @return the first matching Content type, or the default if none matched
   */
  public String getContentType(String filename) 
  {
    int index;
    //get the extension
    index = filename.lastIndexOf(".");
    //if the filename has no extension then just return the def type
    if (index == -1) 
    return defaultType;
    //we must know the file extension
    String ext = filename.substring(index + 1);
    //search each mime.type file
    for(index=0; index<DB.length; index++) 
    {
      //if there is no list of types at this index skip it
      if (DB[index] == null) 
      continue;
      //get the mime type from the list of types
      MimeType mimeType = (MimeType)(DB[index].get(ext));
      if (mimeType != null) 
      return mimeType.getBaseType();
    }
    return defaultType;
  }

  /** programmically add MIME Types entries.
   * In fact the MIME types specified here are put into the existing
   * programmatic registry. These supplement that registry.
   *
   * @param mime_types MIME Types formatted entry
   */
  public void addMimeTypes(String mime_types) 
  {
    try
    {
      StringReader sr=new StringReader(mime_types);
      DB[PROG].putAll(loadMimeRegistry(sr));
    }
    catch(Exception e)
    {
      //don't bother with errors
    }
  }

  /** load a MIME registry from the specified stream.
   *
   * @param in the stream to load from
   * @return map of <code>MimeType</code> keyed by extension
   */
  private Hashtable loadMimeRegistry(Reader in) 
  {
    try 
    {
      Hashtable registry=new Hashtable();
      //some states that we use in this mini-FSM
      final int STARTLINE=0;
      final int READTYPE=1;
      final int READEXT=2;
      //the state register
      int state=READTYPE;
      //the mimetype register
      MimeType mt=null;
      StringBuffer mimeTypeBuffer=new StringBuffer();
      StringBuffer extBuffer=new StringBuffer();
      //setup the tokenizer to parse a standard mime.types file
      StreamTokenizer toker=new StreamTokenizer(in);
      toker.commentChar('#');
      toker.eolIsSignificant(true);
      toker.wordChars('/','/');
      toker.wordChars('-','-');
      while(true)
      {
	switch(toker.nextToken())
	{
	  case StreamTokenizer.TT_EOF:
	    return registry;
	  case StreamTokenizer.TT_EOL:
	    switch(state)
	    {
	      case READEXT:
		//set the state
		state=READTYPE;
		continue;
	      default:
		continue;
	    }
	  case StreamTokenizer.TT_WORD:
	    switch(state)
	    {
	      case READTYPE:
		//type has been read - create the object
		mt=new MimeType(toker.sval);
		state=READEXT;
		continue;
	      case READEXT:
		registry.put(toker.sval,mt);
		continue;
	      default:
		continue;
	    }
	}
      }
    }
    catch (Exception e) 
    {
      //this is only here for debugging
      e.printStackTrace();
    }
    return null;
  }

  /** only for testing purposes.
   */
  public static void main(String[] argv)
  {
    MimetypesFileTypeMap fm=new MimetypesFileTypeMap();
    String contentType=fm.getContentType("file.html");
    System.out.println("the content type was: "+contentType);
  }
}
