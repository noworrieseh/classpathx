/*
 * DocumentFunction.java
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

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import org.w3c.dom.Node;
import gnu.xml.xpath.Expr;

/**
 * The XSLT <code>document()</code>function.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class DocumentFunction
  implements XPathFunction
{

  final Stylesheet stylesheet;

  DocumentFunction(Stylesheet stylesheet)
  {
    this.stylesheet = stylesheet;
  }

  public Object evaluate(List args)
    throws XPathFunctionException
  {
    String base = stylesheet.systemId;
    switch (args.size())
      {
      case 2:
        Object arg2 = args.get(1);
        base = Expr._string(null, arg2);
        // Fall through
      case 1:
        Object arg = args.get(0);
        if (arg instanceof Collection)
          {
            Collection ns = (Collection) arg;
            Collection acc = new TreeSet();
            for (Iterator i = ns.iterator(); i.hasNext(); )
              {
                Node node = (Node) i.next();
                String uri = Expr.stringValue(node);
                acc.add(_document(uri, base));
              }
            return acc;
          }
        else
          {
            String uri = Expr._string(null, arg);
            return Collections.singleton(_document(uri, base));
          }
      default:
        throw new XPathFunctionException("invalid arity");
      }
  }

  Node _document(String uri, String base)
    throws XPathFunctionException
  {
    InputStream in = null;
    if (base != null)
      {
        try
          {
            try
              {
                URL url = new URL(new URL(base), uri);
                uri = url.toString();
                in = url.openStream();
              }
            catch (MalformedURLException e)
              {
                in = new FileInputStream(uri);
              }
          }
        catch (IOException e2)
          {
            throw new XPathFunctionException("can't open " + uri);
          }
      }
    URIResolver uriResolver = (stylesheet.transformer == null) ? null :
      stylesheet.transformer.uriResolver;
    ErrorListener errorListener = (stylesheet.transformer == null) ? null:
      stylesheet.transformer.errorListener;
    StreamSource source = new StreamSource(in);
    source.setSystemId(uri);
    DOMSourceWrapper wrapper = new DOMSourceWrapper(source,
                                                    uriResolver,
                                                    errorListener);
    return wrapper.getNode();
  }
  
}
