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

// SAX parser factory.
// $Id: ParserFactory.java,v 1.2 2001-06-08 20:42:17 db Exp $

package org.xml.sax.helpers;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.SecurityException;
import java.lang.ClassCastException;

import org.xml.sax.Parser;


/**
 * Java-specific class for dynamically loading SAX parsers.
 *
 * <p><strong>Note:</strong> This class is designed to work with the now-deprecated
 * SAX1 {@link org.xml.sax.Parser Parser} class.  SAX2 applications should use
 * {@link org.xml.sax.helpers.XMLReaderFactory XMLReaderFactory} instead.</p>
 *
 * <p>ParserFactory is not part of the platform-independent definition
 * of SAX; it is an additional convenience class designed
 * specifically for Java XML application writers.  SAX applications
 * can use the static methods in this class to allocate a SAX parser
 * dynamically at run-time based either on the value of the
 * `org.xml.sax.parser' system property or on a string containing the class
 * name.</p>
 *
 * <p>Note that the application still requires an XML parser that
 * implements SAX1.</p>
 *
 * @deprecated This class works with the deprecated
 *             {@link org.xml.sax.Parser Parser}
 *             interface.
 * @since SAX 1.0
 * @author David Megginson, 
 *         <a href="mailto:sax@megginson.com">sax@megginson.com</a>
 * @version 2.0
 * @see org.xml.sax.Parser
 * @see java.lang.Class
 */
public class ParserFactory {
    
    
    /**
     * Private null constructor.
     */
    private ParserFactory ()
    {
    }
    
    
    /**
     * Create a new SAX parser using the `org.xml.sax.parser' system property.
     *
     * <p>The named class must exist and must implement the
     * {@link org.xml.sax.Parser Parser} interface.</p>
     *
     * @exception java.lang.NullPointerException There is no value
     *            for the `org.xml.sax.parser' system property.
     * @exception java.lang.ClassNotFoundException The SAX parser
     *            class was not found (check your CLASSPATH).
     * @exception IllegalAccessException The SAX parser class was
     *            found, but you do not have permission to load
     *            it.
     * @exception InstantiationException The SAX parser class was
     *            found but could not be instantiated.
     * @exception java.lang.ClassCastException The SAX parser class
     *            was found and instantiated, but does not implement
     *            org.xml.sax.Parser.
     * @see #makeParser(java.lang.String)
     * @see org.xml.sax.Parser
     */
    public static Parser makeParser ()
	throws ClassNotFoundException,
	IllegalAccessException, 
	InstantiationException,
	NullPointerException,
	ClassCastException
    {
	String className = System.getProperty("org.xml.sax.parser");
	if (className == null) {
	    throw new NullPointerException("No value for sax.parser property");
	} else {
	    return makeParser(className);
	}
    }
    
    
    /**
     * Create a new SAX parser object using the class name provided.
     *
     * <p>The named class must exist and must implement the
     * {@link org.xml.sax.Parser Parser} interface.</p>
     *
     * @param className A string containing the name of the
     *                  SAX parser class.
     * @exception java.lang.ClassNotFoundException The SAX parser
     *            class was not found (check your CLASSPATH).
     * @exception IllegalAccessException The SAX parser class was
     *            found, but you do not have permission to load
     *            it.
     * @exception InstantiationException The SAX parser class was
     *            found but could not be instantiated.
     * @exception java.lang.ClassCastException The SAX parser class
     *            was found and instantiated, but does not implement
     *            org.xml.sax.Parser.
     * @see #makeParser()
     * @see org.xml.sax.Parser
     */
    public static Parser makeParser (String className)
	throws ClassNotFoundException,
	IllegalAccessException, 
	InstantiationException,
	ClassCastException
    {
	return (Parser)(Class.forName(className).newInstance());
    }
    
}

// end of ParserFactory.java
