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
import java.io.Externalizable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.beans.Beans;

/**
 * Describes a command.
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 * @author Nic Ferrier: nferrier@tapsellferrier.co.uk
 * @version $Revision: 1.4 $
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
   * Create the command information
   * @param verb Command verb
   * @param className Command class name
   */
  public CommandInfo(String verb, String className) 
  {
    this.verb = verb;
    this.className = className;
  } // CommandInfo()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Get string representation
   * @return the command name
   * @see #getCommandName which this method uses
   */
  public String toString()
  {
    return getCommandName();
  } // toString()

  /**
   * Get the class of the command.
   * @return Command class
   */
  public String getCommandClass() 
  {
    return className;
  } // getCommandClass()

  /**
   * Get the command's name.
   * @return Command name
   */
  public String getCommandName() 
  {
    return verb;
  } // getCommandName()

  /**
   * Instantiate the command object.
   * @param handler Data handler
   * @param loader Class loader to use
   * @return Command object
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

    // Create the object
    object = Beans.instantiate(loader, className);

    // Check for a Command Object
    if (object instanceof CommandObject)
    {
      command = (CommandObject) object;

      // Set the context of the command object
      command.setCommandContext(verb, handler);

    } // if

    // Check for Externalizable object
    else if ((object instanceof Externalizable) && handler != null)
    {
      external = (Externalizable) object;

      // Get the stream to read the object from the handler
      input = new ObjectInputStream(handler.getInputStream());

      // Cause the object to read itself from the stream
      external.readExternal(input);

    } // if

    // Return the Command object
    return object;

  } // getCommandObject()


} // CommandInfo
