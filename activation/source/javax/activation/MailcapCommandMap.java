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


import java.io.*;
import java.util.*;


/** a comand map that represents the mailcap file.
 * The <i>mailcap</i> file format is specified in RFC1524.
 * Within the JAF it is used as an easy way to specify beans that
 * perform certain operations on data of a particular MIME content type.
 *
 * <p>For example, the following mailcap file specifies 2 beans that
 * perform different operations on the <code>text/html</code> MIME type:
 * <pre>
 *   text/html; ; x-java-edit=gnu.inet.html.HTMLBrowser; \
 *      x-java-print=gnu.inet.xml.DocPrinter;
 * </pre>
 * </p>
 *
 * <p>This class provides a way to access the registry of information that
 * is declared in such a mailcap file.</p>
 *
 * <p>mailcap commands that do not begin with <code>x-java-</code> are ignored.
 * The special command: <code>x-java-content-handler</code> causes this class
 * to intepret the command as a <code>DataContentHandler</code>. There is a
 * special method for obtaining the content handler for a particular MIME type.
 * Other methods provide various ways of accessing the command registry for
 * a particular MIME type.
 * </p>
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 * @author Nic Ferrier: nferrier@tapsellferrier.co.uk
 */
public class MailcapCommandMap
extends CommandMap 
{

  /** the database of mailcap registrys.
   */
  private Hashtable[] DB=null;

  private static final int PROG = 0;
  private static final int HOME = 1;
  private static final int SYS = 2;
  private static final int JAR = 3;
  private static final int DEF = 4;
  private static boolean debug = false;


  /** create default MIME Types registry.
   */
  public MailcapCommandMap() 
  {
    DB = new Hashtable[5];
    Properties properties=System.getProperties();
    String sep=properties.getProperty("file.separator");
    //init programmatic entries
    DB[PROG] = new Hashtable();
    //the user's mime types
    try
    {
      File userHome=new File(properties.getProperty("user.home")+sep+".mailcap");
      DB[HOME] = loadMailcapRegistry(new FileReader(userHome));
    }
    catch(Exception e)
    {
      DB[HOME]=new Hashtable();
    }
    //the system mime types
    try
    {
      File javaHome=new File(properties.getProperty("java.home")+sep+"lib"+sep+"mailcap");
      DB[SYS] = loadMailcapRegistry(new FileReader(javaHome));
    }
    catch(Exception e)
    {
      DB[SYS]=new Hashtable();
    }
    //a possible jar-file local copy of the mime types
    try
    {
      String resource="META-INF"+sep+"mailcap";
      InputStream str=getClass().getClassLoader().getResourceAsStream(resource);
      DB[JAR]=loadMailcapRegistry(new InputStreamReader(str));
    }
    catch(Exception e)
    {
      DB[JAR]=new Hashtable();
    }
    //the default providers... obtained from a possible META-INF/ location
    try
    {
      String resource="META-INF"+sep+"mailcap.default";
      InputStream str=getClass().getClassLoader().getResourceAsStream(resource);
      DB[DEF]=loadMailcapRegistry(new InputStreamReader(str));
    }
    catch(Exception e)
    {
      DB[DEF]=new Hashtable();
    }
  }

  /** create mailcap command map with entries from file.
   *
   * @param fileName Name of file to read
   * @throws IOException IO exception occurred
   */
  public MailcapCommandMap(String fileName)
  throws IOException 
  {
    this();
    try
    {
      DB[PROG] = loadMailcapRegistry(new FileReader(fileName));
    }
    catch(Exception e)
    {
      //
    }
  }

  /** create mailcap command map with entries from stream.
   *
   * @param stream Input stream of mailcap formatted entries
   */
  public MailcapCommandMap(InputStream stream) 
  {
    this();
    try 
    {
      DB[PROG]=loadMailcapRegistry(new InputStreamReader(stream));
    }
    catch (Exception e) 
    {
      //
    }
  }

  /** programmically add a mailcap entry.
   * Mailcap entries added in this way achieve the highest preference
   * for the search commands.
   *
   * @param mailCapEntry Mailcap formatted entry
   */
  public void addMailcap(String mailCapEntry) 
  {
    StringReader rd=new StringReader(mailCapEntry);
    Hashtable entries=loadMailcapRegistry(rd);
    DB[PROG].putAll(entries);
  }

  /** create a content handler for the specified MIME type.
   * The database of registrys is searched and the most preferential
   * content handler (commands with the name: <code>content-handler</code>)
   * is returned.
   *
   * @param mimeType the MIME type to find a content handler for
   * @return the content handler
   */
  public DataContentHandler createDataContentHandler(String mimeType) 
  {
    CommandInfo ch=getCommand(mimeType,"x-java-content-handler");
    if(ch==null)
    return null;
    //we do have the content handler so return it
    try 
    {
      Class classObject = Class.forName(ch.getCommandClass());
      return (DataContentHandler)classObject.newInstance();
    }
    catch(Exception e) 
    {
      //perhaps the ch was not a DataContentHandler...
      //doesn't matter what the problem is: just return null
      return null;
    }
  }

  /** get the list of all commands based on MIME type.
   * The commands in all the registries of the mailcap database
   * are searched.
   *
   * @param mimeType MIME type to search for
   * @return command information associated with the mime type
   */
  public CommandInfo[] getAllCommands(String mimeType) 
  {
    CommandInfo[] allCommands=null;
    synchronized(DB)
    {
      //first establish the number of commands across all registrys
      int size=0;
      for(int i=0; i<DB.length; i++)
      {
	CommandInfo[] entry=(CommandInfo[])DB[i].get(mimeType);
	if(entry!=null)
	size+=entry.length;
      }
      //create an array big enough
      allCommands=new CommandInfo[size];
      //copy the contents of each array into the destination array
      int pos=0;
      for(int i=0; i<DB.length; i++)
      {
	CommandInfo[] entry=(CommandInfo[])DB[i].get(mimeType);
	if(entry!=null)
	{
	  System.arraycopy(entry,0,allCommands,pos,entry.length);
	  pos+=entry.length;
	}
      }
    }
    return allCommands;
  }

  /** get command info for the specified MIME type and command name.
   * The search for the command is done by order of preference.
   *
   * @param mimeType MIME type to search for
   * @param cmdName Command name to check
   * @return Command information, or null
   */
  public CommandInfo getCommand(String mimeType, String cmdName)
  {
    CommandInfo[] infoList=getAllCommands(mimeType);
    for(int i=0; i<infoList.length; i++) 
    {
      if(infoList[i].getCommandName().equals(cmdName))
      return infoList[i];
    }
    return null;
  }

  /** get list of preferred commands based on MIME type.
   * The registry is searched for a mailcap entry assigned to the
   * specified MIME type. The first entry that matches the MIME
   * type is returned.
   *
   * @param mimeType MIME type to search for
   * @return listing of preferred command information
   */
  public CommandInfo[] getPreferredCommands(String mimeType) 
  {
    CommandInfo[] entry=null;
    synchronized(DB)
    {
      for(int i=0; i<DB.length; i++)
      {
	Hashtable registry=DB[i];
	entry=(CommandInfo[])registry.get(mimeType);
	if(entry!=null)
	break;
      }
    }
    return entry;
  }

  /** loads a mailcap file from the specified stream.
   *
   * @param in the stream to load from
   * @return map of <code>mailcaps</code> keyed by content type
   */
  private Hashtable loadMailcapRegistry(Reader in) 
  {
    try 
    {
      Hashtable registry=new Hashtable();
      //some states that we use in this mini-FSM
      final int READMIMETYPE=0;
      final int READUNIXCOMMAND=1;
      final int READCOMMANDNAME=2;
      final int READCOMMANDVALUE=3;
      final int SWALLOW=4;
      //handles line continuations
      boolean continueLine=false;
      //the state register
      int state=READMIMETYPE;
      //various buffers for storing temp values
      String mimetype=null;
      String name=null;
      String value=null;
      StringBuffer mtBuf=new StringBuffer();
      StringBuffer nameBuf=new StringBuffer();
      StringBuffer valueBuf=new StringBuffer();
      //setup the tokenizer to parse a standard mailcap file
      StreamTokenizer toker=new StreamTokenizer(in);
      toker.commentChar('#');
      toker.eolIsSignificant(true);
      toker.wordChars('/','/');
      while(true)
      {
	int token=toker.nextToken();
	//first handle line continuations and END condition
	switch(token)
	{
	  case StreamTokenizer.TT_EOF:
	    return registry;
	  case StreamTokenizer.TT_EOL:
	    if(continueLine)
	    {
	      do
	      token=toker.nextToken();
	      while(token==StreamTokenizer.TT_EOL);
	      continueLine=false;
	    }
	    break;
	  case '\\':
	    continueLine=true;
	    continue;
	}
	//now the main state machine
	switch(state)
	{
	  case READMIMETYPE:
	    switch(token)
	    {
	      case StreamTokenizer.TT_EOL:
		//the mailcap entry has finished without specifying beans
		mtBuf.setLength(0);
		break;
	      case ';':
		//the mime type has been specified
		mimetype=mtBuf.toString();
		mtBuf.setLength(0);
		state=READUNIXCOMMAND;
		break;
	      case StreamTokenizer.TT_WORD:
		mtBuf.append(toker.sval);
		break;
	      default:
		mtBuf.append((char)token);
	    }
	    continue;
	  case READUNIXCOMMAND:
	    switch(token)
	    {
	      case ';':
		//the command has been read - start reading the beans
		state=READCOMMANDNAME;
		break;
	      case StreamTokenizer.TT_EOL:
		//the mailcap entry has finished without specifying beans
		state=READMIMETYPE;
		break;
	      default:
		break;
	    }
	    continue;
	  case READCOMMANDNAME:
	    switch(token)
	    {
	      case StreamTokenizer.TT_EOL:
		//the entry has finished without specifying a bean
		state=READMIMETYPE;
		break;
	      case ';':
		//the field has finished without specifying a bean...
		//... carry on looking for one
		nameBuf.setLength(0);
		break;
	      case '=':
		//the field name has finished correctly
		name=nameBuf.toString();
		nameBuf.setLength(0);
		//if we read a bean specifying field name then move on
		if(name.startsWith("x-java-"))
		state=READCOMMANDVALUE;
		else
		state=READUNIXCOMMAND;
		break;
	      case StreamTokenizer.TT_WORD:
		nameBuf.append(toker.sval);
		break;
	      default:
		nameBuf.append((char)token);
	    }
	    continue;
	  case READCOMMANDVALUE:
	    switch(token)
	    {
	      case StreamTokenizer.TT_EOL:
		value=valueBuf.toString();
		valueBuf.setLength(0);
		addCommand(registry,mimetype,name,value);
		state=READMIMETYPE;
		break;
	      case ';':
		value=valueBuf.toString();
		valueBuf.setLength(0);
		addCommand(registry,mimetype,name,value);
		state=SWALLOW;
		break;
	      case StreamTokenizer.TT_WORD:
		valueBuf.append(toker.sval);
		break;
	      default:
		valueBuf.append((char)token);
	    }
	    continue;
	  case SWALLOW:
	    switch(token)
	    {
	      case StreamTokenizer.TT_EOL:
		state=READMIMETYPE;
		break;
	      default:
		break;
	    }
	    continue;
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

  /** add the specified command name and value to the registry.
   *
   * @param reg the registry of MIME types against <code>CommandInfo[]</code>
   * @param mimetype the name of the mime type
   * @param name the name of the command
   * @param value the text of the command
   */
  private void addCommand(Hashtable reg,String mimetype,String name,String value)
  {
    System.out.println("mimetype="+mimetype+" command="+name+"="+value);
    if(name.startsWith("x-java-"))
    {
      CommandInfo ci=new CommandInfo(name,value);
      //get the existing array
      CommandInfo[] list=(CommandInfo[])reg.get(mimetype);
      if(list==null)
      {
	list=new CommandInfo[1];
	list[0]=ci;
	reg.put(mimetype,list);
	return;
      }
      //the list exists - we have to add our action at the end
      CommandInfo[] bigger=new CommandInfo[list.length+1];
      System.arraycopy(list,0,bigger,0,list.length);
      bigger[list.length]=ci;
      reg.put(mimetype,bigger);
    }
  }


  public static void main(String[] argv)
  {
    try
    {
      MailcapCommandMap map=new MailcapCommandMap();
      CommandInfo[] pref=map.getPreferredCommands("text/*");
      for(int i=0; i<pref.length; i++)
      {
	System.err.println("command="+pref[i]);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
    }
  }
}
