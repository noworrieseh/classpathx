/*
 * Copyright (C) 2003, 2013 Free Software Foundation, Inc.
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package javax.servlet.jsp.tagext;

/**
 * A validation message.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class ValidationMessage
{

    private String id;
    private String message;

    /**
     * Construct a Validation Message.
     * 
     * @param id
     * @param message
     */
    public ValidationMessage(String id, String message)
    {
        this.id = id;
        this.message = message;
    }

    /**
     * Get the Id value.
     * @return the Id value.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Get the Message value.
     * @return the Message value.
     */
    public String getMessage()
    {
        return message;
    }

}
