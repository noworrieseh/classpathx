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
