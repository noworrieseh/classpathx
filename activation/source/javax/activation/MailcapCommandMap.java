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
      //line states
      final int LINE_CONTINUE=-1;
      final int DO_LINE=0;
      //some states that we use in this mini-FSM
      final int STARTCAP=0;
      final int READNAME=2;
      final int READVALUE=3;
      //the state register
      int state=STARTCAP;
      int lineState=DO_LINE;
      //the mime type we're looking for
      String mt=null;
      //the command register: 2 elements store each command
      Vector commands=new Vector();
      //the name buffer
      StringBuffer name=null;
      StringBuffer value=null;
      //setup the tokenizer to parse a standard mailcap file
      StreamTokenizer toker=new StreamTokenizer(in);
      toker.commentChar('#');
      toker.eolIsSignificant(true);
      toker.ordinaryChar('/');
      while(true)
      {
	switch(toker.nextToken())
	{
	  case StreamTokenizer.TT_EOF:
	    return registry;
	  case StreamTokenizer.TT_EOL:
	    if(lineState==LINE_CONTINUE)
	    lineState=DO_LINE;
	    else
	    {
	      //move the vect to an array
	      CommandInfo[] com=new CommandInfo[commands.size()];
	      commands.toArray(com);
	      //add the array to the registry
	      registry.put(mt,com);
	      //change the state to start again
	      state=STARTCAP;
	    }
	    continue;
	  case StreamTokenizer.TT_WORD:
	    switch(state)
	    {
	      case STARTCAP:
		//create a new hash for storing the command tokens
		commands.clear();
		name=new StringBuffer();
		value=new StringBuffer();
		//set the mimetype for this entry
		mt=toker.sval;
		//update the current state
		state=READNAME;
		break;
	      case READNAME:
		if(toker.sval.equals("="))
		state=READVALUE;
		else if(toker.sval.equals(";"))
		addCommand(commands,name,null);
		else if(toker.sval.equals("\\"))
		lineState=LINE_CONTINUE;
		else
		name.append(toker.sval);
		break;
	      case READVALUE:
		if(toker.sval.equals(";"))
		{
		  addCommand(commands,name,value);
		  state=READNAME;
		}
		else if(toker.sval.equals("\\"))
		lineState=LINE_CONTINUE;
		else
		value.append(toker.sval);
		break;
	    }
	    continue;
	  default:
	    //should never get called
	    break;
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

  /** add the specified command name and value to the list.
   * The command is only added if the name begins with:
   * <pre>
   *   x-java-
   * </pre>
   *
   * @param commandList the list of commands to add this one to
   * @param name the name of the command
   * @param value the text of the command
   */
  private void addCommand(Vector commandList,StringBuffer name,StringBuffer value)
  {
    String comName=name.toString();
    if(comName.startsWith("x-java-"))
    {
      CommandInfo ci=new CommandInfo(comName,value.toString());
      commandList.addElement(ci);
    }
  }
}
