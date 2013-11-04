/* 
 * $Id: JavaContext.java,v 1.1.1.1 2003-02-27 01:22:24 julian Exp $
 * Copyright (C) 2003 Julian Scheid
 * 
 * This file is part of GNU LibxmlJ, a JAXP-compliant Java wrapper for
 * the XML and XSLT C libraries for Gnome (libxml2/libxslt).
 * 
 * GNU LibxmlJ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *  
 * GNU LibxmlJ is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GNU LibxmlJ; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA. 
 */

package gnu.xml.libxmlj.transform;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import java.io.InputStream;

import java.util.Map;
import java.util.HashMap;

public class JavaContext
{
  private ErrorListener errorListener;
  private URIResolver uriResolver;
  private Map cache = new HashMap ();

  public JavaContext (URIResolver uriResolver, ErrorListener errorListener)
  {
    this.errorListener = errorListener;
    this.uriResolver = uriResolver;
  }

  //--- Implementation of
  //--- gnu.xml.transform.LibxsltTransformErrorAdapter follows.
  public void saxWarning (String message, SourceLocator sourceLocator)
    throws TransformerException
  {
    errorListener.
      warning (new TransformerException (message.trim (), sourceLocator));
  } 

  public void saxError (String message, SourceLocator sourceLocator) 
    throws TransformerException
  {
    errorListener.
      error (new TransformerException (message.trim (), sourceLocator));
  } 

  public void saxFatalError (String message, SourceLocator sourceLocator) 
    throws TransformerException
  {
    errorListener.
      fatalError (new TransformerException (message.trim (), sourceLocator));
  } 

  public void xsltGenericError (String message) 
    throws TransformerException
  {
    TransformerException exception =
      new TransformerException (message.trim ());
    errorListener.error (exception);
  } 


  SourceWrapper resolveURI (String href, String base) 
    throws TransformerException
  {
    return new SourceWrapper (uriResolver.resolve (href, base));
  }

  public String toString ()
  {
    return "JavaContext{errorListener=" + errorListener + ",uriResolver=" +
      uriResolver + "}";
  }

  private native long parseDocument (InputStream in, String systemId,
				     String publicId);

  long resolveURIAndOpen (String href,
			  String base) 
    throws TransformerException
  {
    if (null != cache.get (href))
      {
	return ((Long) cache.get (href)).longValue ();
      }

    else
      {
	SourceWrapper sourceWrapper = resolveURI (href, base);
	long rc = parseDocument (sourceWrapper.getInputStream (),
				 sourceWrapper.getFilename (), null);
	cache.put (href, new Long (rc));
	return rc;
      }
  }
}
