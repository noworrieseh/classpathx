/*
 * GnomeCharacterData.java
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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

/**
 * A DOM character data node implemented in libxml2.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
abstract class GnomeCharacterData
extends GnomeNode
implements CharacterData
{

  GnomeCharacterData (long id)
    {
      super (id);
    }

  public String getData ()
    throws DOMException
    {
      return getNodeValue ();
    }

  public void setData (String data)
    throws DOMException
    {
      setNodeValue (data);
    }

  public int getLength ()
    {
      return getData ().length ();
    }

  public String substringData (int offset, int count)
    throws DOMException
    {
      return getData ().substring (offset, offset + count);
    }

  public void appendData (String arg)
    throws DOMException
    {
      setData (getData () + arg);
    }

  public void insertData (int offset, String arg)
    throws DOMException
    {
      String data = getData ();
      setData (data.substring (0, offset) + arg + data.substring (offset));
    }

  public void deleteData (int offset, int count)
    throws DOMException
    {
      String data = getData ();
      setData (data.substring (0, offset) + data.substring (offset + count));
    }

  public void replaceData (int offset, int count, String arg)
    {
      String data = getData ();
      setData (data.substring (0, offset) + arg +
               data.substring (offset + count));
    }

}
