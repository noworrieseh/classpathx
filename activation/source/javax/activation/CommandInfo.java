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
import java.beans.Beans;


/** describes a command.
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 * @author Nic Ferrier: nferrier@tapsellferrier.co.uk
 */
public class CommandInfo
{

  /** the command verb.
   */
  private String verb = null;

  /** the command class name.
   */
  private String className = null;

  /** create the command information
   *
   * @param verb Command verb
   * @param className Command class name
   */
  public CommandInfo(String verb, String className) 
  {
    this.verb = verb;
    this.className = className;
  }

  /** @return the command name
   * @see #getCommandName which this method uses
   */
  public String toString()
  {
    return getCommandName();
  }

  /** get the class of the command.
   *
   * @return Command class
   */
  public String getCommandClass() 
  {
    return className;
  }

  /** get the command's name.
   *
   * @return Command name
   */
  public String getCommandName() 
  {
    return verb;
  }

  /** instantiate the command object.
   *
   * @param handler Data handler
   * @param loader Class loader to use
   * @return Command object
   * @throws IOException IO exception occurred
   * @throws ClassNotFoundException Class not found
   */
  public Object getCommandObject(DataHandler handler, ClassLoader loader)
  throws IOException, ClassNotFoundException 
  {
    //create the object
    Object object = Beans.instantiate(loader, this.className);
    //check for Command Object
    if (object instanceof CommandObject) 
    {
      CommandObject command = (CommandObject)object;
      //set the context of the command object
      command.setCommandContext(verb, handler);
    }
    else if ((object instanceof Externalizable) && handler != null) 
    {
      Externalizable external = (Externalizable)object;
      //get the stream to read the object from the handler
      ObjectInputStream input = new ObjectInputStream(handler.getInputStream());
      //cause the object to read itself from the stream
      external.readExternal(input);
    }
    return object;
  }

}
