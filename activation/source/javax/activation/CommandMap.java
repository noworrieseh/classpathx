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
//import java.io.IOException;

/**
 * Command Map.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
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
   * @return Data content handler
   */
  public abstract DataContentHandler createDataContentHandler(String mimeType);

  /**
   * Get all commands for a particular MIME type.
   * @param mimeType MIME Type
   * @return Array of commands
   */
  public abstract CommandInfo[] getAllCommands(String mimeType);

  /**
   * Get command of a particular MIME type and command name.
   * @param mimeType MIME Type
   * @param cmdName Command name
   * @return Command info
   */
  public abstract CommandInfo getCommand(String mimeType, String cmdName);

  /**
   * Get reference to default command map.
   * @return Default command map
   */
  public static CommandMap getDefaultCommandMap() 
  {
    return defaultCommandMap;
  } // getDefaultCommandMap()

  /**
   * Get list of preferred commands.  One entry per command name.
   * @param mimeType MIME Type
   * @return List of commands
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
