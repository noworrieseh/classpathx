// SAXNotRecognizedException.java - unrecognized feature or value.
// http://sax.sourceforge.net
// Written by David Megginson
// NO WARRANTY!  This class is in the Public Domain.

// $Id: SAXNotRecognizedException.java,v 1.4 2001-10-18 00:36:10 db Exp $


package org.xml.sax;


/**
 * Exception class for an unrecognized identifier.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * <p>An XMLReader will throw this exception when it finds an
 * unrecognized feature or property identifier; SAX applications and
 * extensions may use this class for other, similar purposes.</p>
 *
 * @since SAX 2.0
 * @author David Megginson
 * @version 2.0r2pre2
 * @see org.xml.sax.SAXNotSupportedException
 */
public class SAXNotRecognizedException extends SAXException
{

    /**
     * Default constructor.
     */
    public SAXNotRecognizedException ()
    {
	super();
    }


    /**
     * Construct a new exception with the given message.
     *
     * @param message The text message of the exception.
     */
    public SAXNotRecognizedException (String message)
    {
	super(message);
    }

}

// end of SAXNotRecognizedException.java
