// DeclHandler.java - Optional handler for DTD declaration events.
// http://sax.sourceforge.net
// Public Domain: no warranty.
// $Id: DefaultHandler2.java,v 1.1 2001-11-07 02:07:11 db Exp $

package org.xml.sax.ext;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


// $Id: DefaultHandler2.java,v 1.1 2001-11-07 02:07:11 db Exp $

/**
 * This class extends the SAX2 base handler class to support the
 * SAX2 {@link LexicalHandler} and {@link DeclHandler} extensions.
 * The added handler methods just return; subclassers may override
 * on a method-by-method basis.
 *
 * <p> <em>Note:</em> this class might yet learn that the
 * <em>ContentHandler.setDocumentLocator()</em> call might be passed a
 * {@link Locator2} object, and that the
 * <em>ContentHandler.startElement()</em> call might be passed a
 * {@link Attributes2} object.
 *
 * @since SAX 2.0 (extensions 1.1 alpha)
 * @author David Brownell
 * @version TBS
 */
public class DefaultHandler2 extends DefaultHandler
    implements LexicalHandler, DeclHandler
{
    /** Constructs a handler which ignores all parsing events. */
    public DefaultHandler2 () { }


    // SAX2 LexicalHandler

    public void startCDATA ()
    throws SAXException
	{}

    public void endCDATA ()
    throws SAXException
	{}

    public void startDTD (String name, String publicId, String systemId)
    throws SAXException
	{}

    public void endDTD ()
    throws SAXException
	{}

    public void startEntity (String name)
    throws SAXException
	{}

    public void endEntity (String name)
    throws SAXException
	{}

    public void comment (char ch [], int start, int length)
    throws SAXException
	{ }


    // SAX2 DeclHandler

    public void attributeDecl (String eName, String aName,
	    String type, String mode, String value)
    throws SAXException
	{}

    public void elementDecl (String name, String model)
    throws SAXException
	{}

    public void externalEntityDecl (String name,
    	String publicId, String systemId)
    throws SAXException
	{}

    public void internalEntityDecl (String name, String value)
    throws SAXException
	{}
}
