/*
 * DOMSourceWrapper.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obliged to do so.  If you do not wish to do so, delete this
 * exception statement from your version. 
 */

package gnu.xml.transform;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * DOM source wrapper that parses the underlying source into a DOM tree if
 * necessary.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class DOMSourceWrapper
  extends DOMSource
{

  final Source source;
  final URIResolver uriResolver;
  final ErrorListener errorListener;

  DOMSourceWrapper(Source source, URIResolver resolver, ErrorListener listener)
  {
    this.source = source;
    uriResolver = resolver;
    errorListener = listener;
  }

  public Node getNode()
  {
    Node ret = null;
    if (source instanceof DOMSource)
      {
        ret = ((DOMSource) source).getNode();
      }
    if (ret == null)
      {
        try
          {
            // We must currently use the GNU DOM
            // as this supports native ordering of nodes
            DocumentBuilderFactory factory =
              new gnu.xml.dom.JAXPFactory();
            factory.setNamespaceAware(true); // must have namespace support
            //factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (uriResolver != null)
              {
                builder.setEntityResolver(new URIResolverEntityResolver(uriResolver));
              }
            if (errorListener != null)
              {
                builder.setErrorHandler(new ErrorListenerErrorHandler(errorListener));
              }
            String systemId = absolutize(source.getSystemId());
            //System.out.println("Loading "+systemId);
            if (source instanceof SAXSource)
              {
                InputSource in = ((SAXSource) source).getInputSource();
                if (in == null)
                  {
                    in = new InputSource(getInputStream(systemId));
                    in.setSystemId(systemId);
                  }
                ret = builder.parse(in);
              }
            else
              {
                InputStream in = (source instanceof StreamSource) ?
                  ((StreamSource) source).getInputStream() :
                  null;
                if (in == null)
                  {
                    in = getInputStream(systemId);
                  }
                Document doc = builder.parse(in, systemId);
                in.close();
                ret = doc;
              }
          }
        catch (Exception e)
          {
            if (errorListener != null)
              {
                try
                  {
                    errorListener.fatalError(new TransformerException(e));
                  }
                catch (TransformerException e2)
                  {
                    e2.printStackTrace();
                  }
              }
            else
              {
                e.printStackTrace(System.err);
              }
          }
      }
    if (ret == null)
      {
        System.err.println("Can't load "+source.getSystemId());
      }
    return ret;
  }

  String absolutize(String systemId)
    throws IOException
  {
    try
      {
        return new URL(systemId).toString();
      }
    catch (MalformedURLException e)
      {
        return new File(systemId).toURL().toString();
      }
  }

  InputStream getInputStream(String systemId)
    throws IOException
  {
    URL url = new URL(systemId);
    return url.openStream();
  }

  public String getSystemId()
  {
    return source.getSystemId();
  }

  public void setSystemId(String systemId)
  {
    source.setSystemId(systemId);
  }
  
}
