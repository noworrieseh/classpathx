/*
 * $Id: JAXPFactory.java,v 1.1 2001-06-22 14:50:18 db Exp $
 * Copyright (C) 2001 David Brownell
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package gnu.xml.dom;

import java.io.IOException;

import org.w3c.dom.Document;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import gnu.xml.pipeline.DomConsumer;


/**
 * DOM bootstrapping API, for use with JAXP.
 *
 * @author David Brownell
 */
public final class JAXPFactory extends DocumentBuilderFactory
{
    private static final String	PROPERTY = "http://xml.org/sax/properties/";
    private static final String	FEATURE = "http://xml.org/sax/features/";

    private SAXParserFactory	pf;

    /**
     * Default constructor.
     */
    public JAXPFactory () { }

    /**
     * Constructs a JAXP document builder which uses the default
     * JAXP SAX2 parser and the DOM implementation in this package.
     */
    public DocumentBuilder newDocumentBuilder ()
    throws ParserConfigurationException
    {
	if (pf == null)
	    pf = SAXParserFactory.newInstance ();

	// JAXP default: false
	pf.setValidating (isValidating ());

	// FIXME:  this namespace setup may cause errors in some
	// conformant SAX2 parsers, which we CAN patch up by
	// splicing a "NSFilter" stage up front ...

	// JAXP default: false
	pf.setNamespaceAware (isNamespaceAware ());

	try {
	    // undo rude "namespace-prefixes=false" default
	    pf.setFeature (FEATURE + "namespace-prefixes", true);

	    return new JAXPBuilder (pf.newSAXParser ().getXMLReader (), this);
	} catch (SAXException e) {
	    throw new ParserConfigurationException (
		"can't create JAXP DocumentBuilder: " + e.getMessage ());
	}
    }

    /** There seems to be no useful specification for attribute names */
    public void setAttribute (String name, Object value)
    throws IllegalArgumentException
    {
	throw new IllegalArgumentException (name);
    }

    /** There seems to be no useful specification for attribute names */
    public Object getAttribute (String name)
    throws IllegalArgumentException
    {
	throw new IllegalArgumentException (name);
    }

    static final class JAXPBuilder extends DocumentBuilder
	implements ErrorHandler
    {
	private DomConsumer	consumer;
	private XMLReader	producer;

	JAXPBuilder (XMLReader parser, JAXPFactory factory)
	throws ParserConfigurationException
	{
	    // set up consumer side
	    try {
		consumer = new DomConsumer (DomDocument.class);
	    } catch (SAXException e) {
		throw new ParserConfigurationException (e.getMessage ());
	    }

	    // JAXP default: true (bleech)
	    consumer.setExpandingReferences (
		    factory.isExpandEntityReferences ());

	    // JAXP default:  save all this noise (bleech).
	    // DomConsumer could give more granular control;
	    // for now, follow the JAXP pro-noise bias.
	    consumer.setSavingExtraNodes (!(
		       factory.isCoalescing ()
			    // coalesce == ignore CDATA boundaries
		    || factory.isIgnoringComments ()
		    || factory.isIgnoringElementContentWhitespace ()
		    ));

	    // JAXP default: false
	    consumer.setUsingNamespaces (factory.isNamespaceAware ());

	    // set up producer side
	    producer = parser;
	    producer.setContentHandler (consumer.getContentHandler ());
	    producer.setDTDHandler (consumer.getDTDHandler ());

	    if (consumer.isSavingExtraNodes ()) {
		try {
		    String	id;

		    id = PROPERTY + "lexical-handler";
		    producer.setProperty (id, consumer.getProperty (id));
		    id = PROPERTY + "declaration-handler";
		    producer.setProperty (id, consumer.getProperty (id));
		} catch (SAXException e) {
		    throw new ParserConfigurationException (e.getMessage ());
		}
	    }
	    
	    // if validating, default to treating validity errors as fatal
	    if (factory.isValidating ())
	    	producer.setErrorHandler (this);
	}


	public Document parse (InputSource source) 
	throws SAXException, IOException
	{
	    producer.parse (source);
	    return consumer.getDocument ();
	}

	public boolean isNamespaceAware ()
	    { return consumer.isUsingNamespaces (); }

	public boolean isValidating ()
	{
	    try {
		return producer.getFeature (FEATURE + "validation");
	    } catch (SAXException e) {
		// "can't happen"
		throw new RuntimeException (e.getMessage ());
	    }
	}

	public void setEntityResolver (EntityResolver resolver)
	    { producer.setEntityResolver (resolver); }

	public void setErrorHandler (ErrorHandler handler)
	{
	    producer.setErrorHandler (handler);
	    consumer.setErrorHandler (handler);
	}

	public Document newDocument ()
	    { return new DomDocument (); }
	
	// implementation of error handler that's used when validating
	public void fatalError (SAXParseException e) throws SAXException
	    { throw e; }
	public void error (SAXParseException e) throws SAXException
	    { throw e; }
	public void warning (SAXParseException e) throws SAXException
	    { /* ignore */ }
    }
}
