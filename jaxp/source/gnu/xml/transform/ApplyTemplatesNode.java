/*
 * ApplyTemplatesNode.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import gnu.xml.xpath.Expr;

/**
 * A template node representing the XSL <code>apply-templates</code>
 * instruction.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class ApplyTemplatesNode
  extends TemplateNode
{

  final Expr select;
  final String mode;
  final List sortKeys;
  final List withParams;

  ApplyTemplatesNode(TemplateNode children, TemplateNode next,
                     Expr select, String mode,
                     List sortKeys, List withParams)
  {
    super(children, next);
    this.select = select;
    this.mode = mode;
    this.sortKeys = sortKeys;
    this.withParams = withParams;
  }

  void apply(Stylesheet stylesheet, Node context, String mode,
             Node parent, Node nextSibling)
    throws TransformerException
  {
    Object ret = select.evaluate(context);
    if (ret != null && ret instanceof Collection)
      {
        if (withParams != null)
          {
            // push the parameter context
            stylesheet.bindings.push(false);
            // set the parameters
            for (Iterator i = withParams.iterator(); i.hasNext(); )
              {
                WithParam p = (WithParam) i.next();
                stylesheet.bindings.set(p.name, p.value, false);
              }
          }
        Collection ns = (Collection) ret;
        if (sortKeys != null)
          {
            List list = new ArrayList(ns);
            Collections.sort(list, new XSLComparator(sortKeys));
            ns = list;
          }
        for (Iterator i = ns.iterator(); i.hasNext(); )
          {
            Node subject = (Node) i.next();
            stylesheet.applyTemplates(subject, subject,
                                      (this.mode != null) ? this.mode : mode,
                                      parent, nextSibling);
          }
        if (withParams != null)
          {
            // pop the variable context
            stylesheet.bindings.pop(false);
          }
      }
    // apply-templates doesn't have processable children
    if (next != null)
      {
        next.apply(stylesheet, context, mode, parent, nextSibling);
      }
  }
  
  public String toString()
  {
    StringBuffer buf = new StringBuffer(getClass().getName());
    buf.append('[');
    boolean o = false;
    if (select != null)
      {
        buf.append("select=");
        buf.append(select);
        o = true;
      }
    if (mode != null)
      {
        if (o)
          {
            buf.append(',');
          }
        buf.append("mode=");
        buf.append(mode);
      }
    buf.append(']');
    return buf.toString();
  }
  
}
