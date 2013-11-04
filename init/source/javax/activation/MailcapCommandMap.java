/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;
import java.util.*;
import oje.activation.registries.*;
#
/**
 * Mailcap Command Map.
 */
public class MailcapCommandMap
extends CommandMap 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  private static MailcapFile defDB = null;
  private MailcapFile[] DB = null;
  private static final int PROG = 0;
  private static final int HOME = 1;
  private static final int SYS = 2;
  private static final int JAR = 3;
  private static final int DEF = 4;
  private static boolean debug = false;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create mailcap command map with entries from file.
   * @param fileName Name of file to read
   * @throws IOException IO exception occurred
   */
  public MailcapCommandMap(String fileName) throws IOException 
  {
    this();
    DB[PROG] = new MailcapFile(fileName);
  } // MailcapCommandMap()

  /**
   * Create mailcap command map with entries from stream.
   * @param stream Input stream of mailcap formatted entries
   */
  public MailcapCommandMap(InputStream stream) 
  {
    this();
    try 
    {
      DB[PROG] = new MailcapFile(stream);
    } catch (Exception e) 
    {
      DB[PROG] = new MailcapFile();
    }
  } // MailcapCommandMap()

  /**
   * Create default mailcap command map.
   */
  public MailcapCommandMap() 
  {

    // Variables
    String separator;
    Properties properties;
 
    // Initialize Mailcap Array
    DB = new MailcapFile[5];
    properties = System.getProperties();
    separator = properties.getProperty("file.separator");
 
    // Initialize Programmed Entries
    DB[PROG] = new MailcapFile();
 
    // Initialize User Mailcap (~/.mailcap)
    DB[HOME] = loadFile(properties.getProperty("user.home") +
			separator + ".mailcap");
 
    // Initialize Java Mailcap (<java-home>/lib/mailcap)
    DB[SYS] = loadFile(properties.getProperty("java.home") +
		       separator + "lib" + separator + "mailcap");

    // Initialize Resource Mailcap (META-INF/mailcap)
    DB[JAR] = loadResource("META-INF" + separator + "mailcap");
 
    // Initialize Default Resource Mailcap (META-INF/mailcap.default)
    DB[DEF] = loadResource("META-INF" + separator + "mailcap.default");

  } // MailcapCommandMap()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Programmically add a mailcap entry.
   * @param mail_cap Mailcap formatted entry
   */
  public synchronized void addMailcap(String mail_cap) 
  {
    DB[PROG].appendToMailcap(mail_cap);
  } // addMailcap()

  /**
   * Append commands from table into vector list.
   * @param table Hashtable
   * @param vector Vector list of commands
   */
  private void appendCmdsToVector(Hashtable table, Vector vector) 
  {

    // Variables
    Enumeration enum;
    String verb;
    String className;
    Vector list;
    int index;

    // Get Enumerations of verbs
    enum = table.keys();

    // Process Each Verb
    while (enum.hasMoreElements() == true) 
    {

      // Get Verb
      verb = (String) enum.nextElement();
      list = (Vector) table.get(verb);

      // Add Each List entry
      for (index = 0; index < list.size(); index++) 
      {
	className = (String) list.elementAt(index);
	vector.addElement(new CommandInfo(verb, className));
      } // for: index

    } // while

  } // appendCmdsToVector()

  /**
   * Append preferred commands to list from table.
   * @param table Hashtable
   * @param vector Preferred commands list
   */
  private void appendPrefCmdsToVector(Hashtable table, Vector vector) 
  {

    // Variables
    Enumeration enum;
    String verb;
    String className;
    Vector list;

    // Get Enumerations of verbs
    enum = table.keys();

    // Process Each Verb
    while (enum.hasMoreElements() == true) 
    {

      // Get Verb
      verb = (String) enum.nextElement();
      list = (Vector) table.get(verb);

      // Check For Existence
      if (checkForVerb(vector, verb) == false) 
      {
	className = (String) list.elementAt(0);
	vector.addElement(new CommandInfo(verb, className));
      }

    } // while

  } // appendPrefCmdsToVector()

  /**
   * Check if command verb exists in command list.
   * @param vector Command list
   * @param verb Command name verb to check
   * @returns true if exists, false otherwise
   */
  private boolean checkForVerb(Vector vector, String verb) 
  {

    // Variables
    int index;
    CommandInfo info;

    // Loop Through All Elements
    for (index = 0; index < vector.size(); index++) 
    {
      info = (CommandInfo) vector.elementAt(index);
      if (info.getCommandName().equals(verb) == true) 
      {
	return true;
      }
    } // for: index

    // Unable to locate
    return false;

  } // checkForVerb()

  /**
   * Check for content handler from command names for particular 
   * MIME type.
   * @param mimeType MIME type
   * @returns Data content handler
   */
  public synchronized DataContentHandler 
  createDataContentHandler(String mimeType) 
  {

    // Variables
    CommandInfo info;
    int index;
    String primary;
    Class classObject;

    // Get Command
    info = getCommand(mimeType, "content-handler");

    // If non-existent, Check wildcard
    index = mimeType.indexOf("/");
    if (index != -1) 
    {

      primary = mimeType.substring(0, index);
      info = getCommand(primary + "/*", "content-handler");

    } // if

    // Check if Command Info Located
    if (info == null) 
    {
      return null;
    }

    try 
    {

      classObject = Class.forName(info.getCommandClass());
      return (DataContentHandler) classObject.newInstance();

    } catch (Exception e) 
    {
      return null;
    }

  } // createDataContentHandler()

  /**
   * Get list of all commands based on MIME type.
   * @param mimeType MIME type to search for
   * @returns Listing of command information
   */
  public synchronized CommandInfo[] getAllCommands(String mimeType) 
  {

    // Variables
    int index;
    Hashtable table;
    Vector list;
    CommandInfo[] infoList;

    // Retrieve from Each Source
    list = new Vector();
    for (index = 0; index < DB.length; index++) 
    {

      // Get Table from Source
      if (DB[index] != null) 
      {
	table = DB[index].getMailcapList(mimeType);
	appendCmdsToVector(table, list);
      }

    } // for: index

    // Create Array
    infoList = new CommandInfo[list.size()];
    for (index = 0; index < list.size(); index++) 
    {
      infoList[index] = (CommandInfo) list.elementAt(index);
    } // for: index

    // Return array
    return infoList;

  } // getAllCommands()

  /**
   * Get command information from MIME type and command name verb.
   * @param mimeType MIME type to search for
   * @param cmdName Command name to check
   * @returns Command information, or null
   */
  public synchronized CommandInfo getCommand(String mimeType, String cmdName) 
  {

    // Variables
    CommandInfo[] infoList;
    String verb;
    int index;
    CommandInfo info;

    // Get Preferred Commands
    infoList = getPreferredCommands(mimeType);

    // Search For Command name
    for (index = 0; index < infoList.length; index++) 
    {
      verb = infoList[index].getCommandName();
      if (verb.equals(cmdName) == true) 
      {
	return infoList[index];
      }
    } // for: index

    // Unable to locate
    return null;

  } // getCommand()

  /**
   * Get list of preferred commands based on MIME type.
   * @param mimeType MIME type to search for
   * @returns Listing of preferred command information
   */
  public synchronized CommandInfo[] getPreferredCommands(String mimeType) 
  {
    //System.out.println("getPreferredCommands: " + mimeType);
    // Variables
    int index;
    Hashtable table;
    Vector list;
    CommandInfo[] infoList;

    // Retrieve from Each Source
    list = new Vector();
    for (index = 0; index < DB.length; index++) 
    {

      // Get Table from Source
      if (DB[index] != null) 
      {
	table = DB[index].getMailcapList(mimeType);
	if (table != null) 
	{
	  appendPrefCmdsToVector(table, list);
	}
      }

    } // for: index

    // Create Array
    infoList = new CommandInfo[list.size()];
    for (index = 0; index < list.size(); index++) 
    {
      infoList[index] = (CommandInfo) list.elementAt(index);
    } // for: index

    // Return array
    return infoList;

  } // getPreferredCommands()

  /**
   * Load mailcap file.
   * @param filename Name of file to load
   * @returns Mailcap file, or null
   */
  private MailcapFile loadFile(String filename) 
  {
    try 
    {
      return new MailcapFile(filename);
    } catch (Exception e) 
    {
      return null;
    }
  } // loadFile()

  /**
   * Load mailcap resource.
   * @param filename Name of resource to load
   * @returns Mailcap file, or null
   */
  private MailcapFile loadResource(String filename) 
  {

    // Variables
    InputStream stream;
    MailcapFile file;
 
    // Get Resource
    stream = getClass().getClassLoader().getResourceAsStream(filename);
 
    try 
    {
      // Get Mailcap File
      file = new MailcapFile(stream);
    } catch (Exception e) 
    {
      file = null;
    }
 
    // Return File
    return file;

  } // loadResource()


} // MailcapCommandMap
