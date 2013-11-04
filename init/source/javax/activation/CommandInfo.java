/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;
import java.beans.Beans;

/**
 * Command Info
 */
public class CommandInfo
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Command verb.
   */
  private String verb = null;

  /**
   * Command class name.
   */
  private String className = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create CommandInfo object.
   * @param verb Command verb
   * @param className Command class name
   */
  public CommandInfo(String verb, String className) 
  {
    this.verb = verb;
    this.className = className;
  } // CommandInfo()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get command class.
   * @returns Command class
   */
  public String getCommandClass() 
  {
    return className;
  } // getCommandClass()

  /**
   * Get command name.
   * @returns Command name
   */
  public String getCommandName() 
  {
    return verb;
  } // getCommandName()

  /**
   * Instantiate command object.
   * @param handler Data handler
   * @param loader Class loader to use
   * @returns Command object
   * @throws IOException IO exception occurred
   * @throws ClassNotFoundException Class not found
   */
  public Object getCommandObject(DataHandler handler, ClassLoader loader)
  throws IOException, ClassNotFoundException 
  {

    // Variables
    Object object;
    CommandObject command;
    Externalizable external;
    ObjectInputStream input;

    // Instantiate Object
    object = Beans.instantiate(loader, className);

    // Check for Command Object
    if (object instanceof CommandObject) 
    {

      // Get Command Object
      command = (CommandObject) object;

      // Set Context
      command.setCommandContext(verb, handler);

    } else if ((object instanceof Externalizable) &&
	       handler != null) 
    {

      // Get Externalizable
      external = (Externalizable) object;

      // Get InputStream From DataHandler
      input = new ObjectInputStream(handler.getInputStream());

      // Send Stream to Object
      external.readExternal(input);

    } // if

    // Return Object
    return object;

  } // getCommandObject()


} // CommandInfo
