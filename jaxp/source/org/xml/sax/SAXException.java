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

// SAX exception class.
// $Id: SAXException.java,v 1.2 2001-06-08 20:42:17 db Exp $

package org.xml.sax;

/**
 * Encapsulate a general SAX error or warning.
 *
 * <p>This class can contain basic error or warning information from
 * either the XML parser or the application: a parser writer or
 * application writer can subclass it to provide additional
 * functionality.  SAX handlers may throw this exception or
 * any exception subclassed from it.</p>
 *
 * <p>If the application needs to pass through other types of
 * exceptions, it must wrap those exceptions in a SAXException
 * or an exception derived from a SAXException.</p>
 *
 * <p>If the parser or application needs to include information about a
 * specific location in an XML document, it should use the
 * {@link org.xml.sax.SAXParseException SAXParseException} subclass.</p>
 *
 * @since SAX 1.0
 * @author David Megginson, 
 *         <a href="mailto:sax@megginson.com">sax@megginson.com</a>
 * @version 2.0r2pre
 * @see org.xml.sax.SAXParseException
 */
public class SAXException extends Exception {


    /**
     * Create a new SAXException.
     */
    public SAXException ()
    {
	super();
	this.exception = null;
    }
    
    
    /**
     * Create a new SAXException.
     *
     * @param message The error or warning message.
     * @see org.xml.sax.Parser#setLocale
     */
    public SAXException (String message) {
	super(message);
	this.exception = null;
    }
    
    
    /**
     * Create a new SAXException wrapping an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, and its message will become the default message for
     * the SAXException.</p>
     *
     * @param e The exception to be wrapped in a SAXException.
     */
    public SAXException (Exception e)
    {
	super();
	this.exception = e;
    }
    
    
    /**
     * Create a new SAXException from an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, but the new exception will have its own message.</p>
     *
     * @param message The detail message.
     * @param e The exception to be wrapped in a SAXException.
     * @see org.xml.sax.Parser#setLocale
     */
    public SAXException (String message, Exception e)
    {
	super(message);
	this.exception = e;
    }
    
    
    /**
     * Return a detail message for this exception.
     *
     * <p>If there is an embedded exception, and if the SAXException
     * has no detail message of its own, this method will return
     * the detail message from the embedded exception.</p>
     *
     * @return The error or warning message.
     * @see org.xml.sax.Parser#setLocale
     */
    public String getMessage ()
    {
	String message = super.getMessage();
	
	if (message == null && exception != null) {
	    return exception.getMessage();
	} else {
	    return message;
	}
    }
    
    
    /**
     * Return the embedded exception, if any.
     *
     * @return The embedded exception, or null if there is none.
     */
    public Exception getException ()
    {
	return exception;
    }


    /**
     * Override toString to pick up any embedded exception.
     *
     * @return A string representation of this exception.
     */
    public String toString ()
    {
	if (exception != null) {
	    return exception.toString();
	} else {
	    return super.toString();
	}
    }
    
    
    
    //////////////////////////////////////////////////////////////////////
    // Internal state.
    //////////////////////////////////////////////////////////////////////


    /**
     * @serial The embedded exception if tunnelling, or null.
     */    
    private Exception exception;
    
}

// end of SAXException.java
