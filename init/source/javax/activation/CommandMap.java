/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.IOException;

/**
 * Command Map.
 */
public abstract class CommandMap
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Default command map for JAF is Mailcap.
   */
  private static CommandMap defaultCommandMap = new MailcapCommandMap();


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create Command Map.
   */
  public CommandMap() 
  {
  } // CommandMap()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Create data content handler for MIME type.
   * @param mimeType MIME Type
   * @returns Data content handler
   */
  public abstract DataContentHandler createDataContentHandler(String mimeType);

  /**
   * Get all commands for a particular MIME type.
   * @param mimeType MIME Type
   * @returns Array of commands
   */
  public abstract CommandInfo[] getAllCommands(String mimeType);

  /**
   * Get command of a particular MIME type and command name.
   * @param mimeType MIME Type
   * @param cmdName Command name
   * @returns Command info
   */
  public abstract CommandInfo getCommand(String mimeType, String cmdName);

  /**
   * Get reference to default command map.
   * @returns Default command map
   */
  public static CommandMap getDefaultCommandMap() 
  {
    return defaultCommandMap;
  } // getDefaultCommandMap()

  /**
   * Get list of preferred commands.  One entry per command name.
   * @param mimeType MIME Type
   * @returns List of commands
   */
  public abstract CommandInfo[] getPreferredCommands(String mimeType);

  /**
   * Set the default command map.
   * @param commandMap Command map to set as default, or null for default
   */
  public static void setDefaultCommandMap(CommandMap commandMap) 
  {
    if (commandMap == null) 
    {
      defaultCommandMap = new MailcapCommandMap();
    } else 
    {
      defaultCommandMap = commandMap;
    }
  } // setDefaultCommandMap()


} // CommandMap
