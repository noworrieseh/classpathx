/*
 * GnomeXPathExpression.java
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
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
package gnu.xml.libxmlj.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;

/**
 * A compiled XPath expression implemented in libxml2.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class GnomeXPathExpression
implements XPathExpression
{

  /**
   * xmlXPathCompExprPtr
   */
  final long expr;

  GnomeXPathExpression (GnomeDocument doc, String expression,
                        XPathNSResolver resolver)
    {
      expr = init (expression);
      // TODO resolver
    }

  protected void finalize ()
    {
      free (expr);
    }

  private native long init (String expression);

  private native void free (long expr);

  public Object evaluate (Node contextNode,
                          short type,
                          Object result)
    throws XPathException, DOMException
    {
      return evaluate (expr, contextNode, type, result);
    }
  
  private native Object evaluate (long expr,
                                  Node contextNode,
                                  short type,
                                  Object result)
    throws XPathException, DOMException;
    
}
