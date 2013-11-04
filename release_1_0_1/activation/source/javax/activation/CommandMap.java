/*
 * CommandMap.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU Java Activation Framework (JAF), a library.
 * 
 * GNU JAF is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
package javax.activation;

/**
 * Registry of command objects available to the system.
 * 
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 1.0.2
 */
public abstract class CommandMap
{

    /* Class scope */
    
    private static CommandMap defaultCommandMap;

    /**
     * Returns the default command map.
     * This returns a MailcapCommandMap if no value has been set using
     * <code>setDefaultCommandMap</code>.
     */
    public static CommandMap getDefaultCommandMap()
    {
        if (defaultCommandMap == null)
            defaultCommandMap = new MailcapCommandMap();
        return defaultCommandMap;
    }

    /**
     * Sets the default command map.
     * @param commandMap the new default command map
     */
    public static void setDefaultCommandMap(CommandMap commandMap)
    {
        SecurityManager security = System.getSecurityManager();
        if (security != null)
        {
            try
            {
                security.checkSetFactory();
            }
            catch (SecurityException e)
            {
                if (commandMap != null && CommandMap.class.getClassLoader() !=
                        commandMap.getClass().getClassLoader())
                    throw e;
            }
        }
        defaultCommandMap = commandMap;
    }

    /* Instance scope */

    /**
     * Returns the list of preferred commands for a MIME type.
     * @param mimeType the MIME type
     */
    public abstract CommandInfo[] getPreferredCommands(String mimeType);

    /**
     * Returns the complete list of commands for a MIME type.
     * @param mimeType the MIME type
     */
    public abstract CommandInfo[] getAllCommands(String mimeType);

    /**
     * Returns the command corresponding to the specified MIME type and
     * command name.
     * @param mimeType the MIME type
     * @param cmdName the command name
     */
    public abstract CommandInfo getCommand(String mimeType, String cmdName);

    /**
     * Returns a DataContentHandler corresponding to the MIME type.
     * @param mimeType the MIME type
     */
    public abstract DataContentHandler createDataContentHandler(String mimeType);

}
