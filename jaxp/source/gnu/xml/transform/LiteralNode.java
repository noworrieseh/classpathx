/*
 * LiteralNode.java
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

import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * A template node that copies a DOM node in the template to the result
 * tree.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class LiteralNode
  extends TemplateNode
{

  /**
   * The source node in the XSL template.
   */
  final Node source;

  LiteralNode(TemplateNode children, TemplateNode next, Node source)
  {
    super(children, next);
    this.source = source;
  }

  void apply(Stylesheet stylesheet, Node context, String mode,
             Node parent, Node nextSibling)
    throws TransformerException
  {
    // Insert result node
    Node result = source.cloneNode(false);
    Document doc = (parent instanceof Document) ? (Document) parent :
      parent.getOwnerDocument();
    result = doc.adoptNode(result);
    if (nextSibling != null)
      {
        parent.insertBefore(result, nextSibling);
      }
    else
      {
        parent.appendChild(result);
      }
    // Copy attributes
    NamedNodeMap attrs = source.getAttributes();
    if (attrs != null)
      {
        NamedNodeMap resultAttrs = result.getAttributes();
        int len = attrs.getLength();
        for (int i = 0; i < len; i++)
          {
            Node attr = attrs.item(i).cloneNode(true);
            attr = doc.adoptNode(attr);
            resultAttrs.setNamedItemNS(attr);
          }
      }
    // Process children and next sibling
    if (children != null)
      {
        children.apply(stylesheet, context, mode, result, null);
      }
    if (next != null)
      {
        next.apply(stylesheet, context, mode, parent, nextSibling);
      }
  }
  
}
