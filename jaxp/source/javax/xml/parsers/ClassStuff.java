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

package javax.xml.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


// $Id: ClassStuff.java,v 1.1 2001-10-15 04:06:42 db Exp $

/**
 * Package-private utility methods for sharing
 * magic related to class loading.
 *
 * @author David Brownell
 * @version $Version$
 */
final class ClassStuff
{
    private ClassStuff () { }

    /**
     * Get the default factory using the four-stage defaulting
     * mechanism defined by JAXP.
     */
    static Object
    createFactory (String label, String defaultClass)
    throws FactoryConfigurationError
    {
	String		name = null;
	ClassLoader	loader = null;

	// NOTE:  Two restrictions here.
	// (a) Needs JDK 1.2 to compile this, but runs on JDK 1.1.x
	// (b) Seemingly won't work on the MSFT JVM, which uses
	//	early method binding (ok by JDK 1.1 spec, not 1.2)

	// Can we use JDK 1.2 APIs?
	try {
	    loader = Thread.currentThread ().getContextClassLoader ();
	} catch (Exception e) { /* ignore */ }

	// If not, use JDK 1.1 calls.
	try {
	    if (loader == null)
		loader = ClassLoader.class.getClassLoader ();
	} catch (Exception e) { /* ignore */ }


	// 1. Check System Property
	// ... normally fails in applet environments
	try { name = System.getProperty (label);
	} catch (SecurityException e) { /* IGNORE */ }

	// 2. Check in $JAVA_HOME/lib/jaxp.properties
	try {
	    if (name == null) {
		String	javaHome;
		File	file;

		javaHome = System.getProperty ("java.home");
		file = new File (new File (javaHome, "lib"), "jaxp.properties");
		if (file.exists() == true) {
		    FileInputStream	in = new FileInputStream (file);
		    Properties		props = new Properties();

		    props.load (in);
		    name  = props.getProperty (label);
		    in.close ();
		}
	    }
	} catch (Exception e) { /* IGNORE */ }

	// 3. Check Services API
	if (name == null) {
	    try {
		String		service = "META-INF/services/" + label;
		InputStream	in;
		BufferedReader	reader;

		if (loader == null)
		    in = ClassLoader.getSystemResourceAsStream (service);
		else
		    in = loader.getResourceAsStream (service);
		if (in != null) {
		    reader = new BufferedReader (
			new InputStreamReader (in, "UTF8"));
		    name = reader.readLine();
		    in.close ();
		}
	    } catch (Exception e2) { /* IGNORE */ }
	}

	// 4. Distro-specific fallback
	if (name == null)
	    name = defaultClass;
	
	// Instantiate!
	try {
	    Class	klass;

	    if (loader == null)
		klass = Class.forName (name);
	    else
		klass = loader.loadClass (name);
	    return klass.newInstance ();

	} catch (ClassNotFoundException e) {
	    throw new FactoryConfigurationError (e,
		"Factory class " + name
		    + " not found");
	} catch (IllegalAccessException e) {
	    throw new FactoryConfigurationError (e,
		"Factory class " + name
		    + " found but cannot be loaded");
	} catch (InstantiationException e) {
	    throw new FactoryConfigurationError (e,
		"Factory class " + name
		    + " loaded but cannot be instantiated"
		    + " ((no default constructor?)");
	}
    }
}
