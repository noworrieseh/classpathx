/*
 * GnomeNode.java
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

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A DOM node implemented in libxml2.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class GnomeNode
implements Node
{

  final int id;

  GnomeNode(int id)
  {
    this.id = id;
  }

  public native String getNodeName();

  public native String getNodeValue()
    throws DOMException;

  public native void setNodeValue(String nodeValue)
    throws DOMException;

  public native short getNodeType();

  public native Node getParentNode();

  public native NodeList getChildNodes();

  public native Node getFirstChild();

  public native Node getLastChild();

  public native Node getPreviousSibling();

  public native Node getNextSibling();

  public native NamedNodeMap getAttributes();

  public native Document getOwnerDocument();

  public native Node insertBefore(Node newChild, Node refChild)
    throws DOMException;

  public native Node replaceChild(Node newChild, Node oldChild)
    throws DOMException;

  public native Node removeChild(Node oldChild)
    throws DOMException;
  
  public native Node appendChild(Node newChild)
    throws DOMException;

  public native boolean hasChildNodes();

  public native Node cloneNode(boolean deep);

  public native void normalize();

  public native boolean isSupported(String feature, String version);

  public native String getNamespaceURI();

  public native String getPrefix();

  public native void setPrefix(String prefix)
    throws DOMException;

  public native String getLocalName();

  public native boolean hasAttributes();

  public int hashCode()
  {
    return id;
  }
  
  public boolean equals(Object other)
  {
    if (other==this)
      return true;
    return (other instanceof GnomeNode &&
        ((GnomeNode)other).id==id);
  }

  public String toString()
  {
    return getClass().getName() + '#' + Integer.toHexString(id);
  }

}
