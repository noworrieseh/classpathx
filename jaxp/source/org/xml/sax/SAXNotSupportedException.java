/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 David Brownell

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
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

// SAXNotSupportedException.java - unsupported feature or value.
// Written by David Megginson, sax@megginson.com

// $Id: SAXNotSupportedException.java,v 1.2 2001-06-08 20:42:17 db Exp $


package org.xml.sax;

/**
 * Exception class for an unsupported operation.
 *
 * <p>An XMLReader will throw this exception when it recognizes a
 * feature or property identifier, but cannot perform the requested
 * operation (setting a state or value).  Other SAX2 applications and
 * extensions may use this class for similar purposes.</p>
 *
 * @since SAX 2.0
 * @author David Megginson, 
 *         <a href="mailto:sax@megginson.com">sax@megginson.com</a>
 * @version 2.0r2pre
 * @see org.xml.sax.SAXNotRecognizedException 
 */
public class SAXNotSupportedException extends SAXException
{

    /**
     * Construct a new exception with no message.
     */
    public SAXNotSupportedException ()
    {
	super();
    }


    /**
     * Construct a new exception with the given message.
     *
     * @param message The text message of the exception.
     */
    public SAXNotSupportedException (String message)
    {
	super(message);
    }

}

// end of SAXNotSupportedException.java
