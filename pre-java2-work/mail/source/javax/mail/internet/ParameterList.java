/*
 * ParameterList.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.internet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds MIME parameters (attribute-value pairs).
 */
public class ParameterList
{

  /*
   * The underlying storage.
   */
  private HashMap list = new HashMap();

  /**
   * No-arg Constructor.
   */
  public ParameterList()
  {
  }

  /**
   * Constructor that takes a parameter-list string.
   * The String is parsed and the parameters are collected and stored 
   * internally. A ParseException is thrown if the parse fails.
   * Note that an empty parameter-list string is valid and will be parsed 
   * into an empty ParameterList.
   * @param s the parameter-list string.
   * @exception ParseException if the parse fails.
   */
  public ParameterList(String s)
    throws ParseException
  {
    HeaderTokenizer ht = new HeaderTokenizer(s, HeaderTokenizer.MIME);
    for (int type = 0; type!=HeaderTokenizer.Token.EOF; )
    {
      HeaderTokenizer.Token token = ht.next();
      type = token.getType();

      if (type!=HeaderTokenizer.Token.EOF)
      {
        if (type!=0x3b) // ';'
          throw new ParseException();
        
        token = ht.next();
        type = token.getType();
        if (type!=HeaderTokenizer.Token.ATOM)
          throw new ParseException();
        String key = token.getValue().toLowerCase();
        
        token = ht.next();
        type = token.getType();
        if (type!=0x3d) // '='
          throw new ParseException();

        token = ht.next();
        type = token.getType();
        if (type!=HeaderTokenizer.Token.ATOM && 
            type!=HeaderTokenizer.Token.QUOTEDSTRING)
          throw new ParseException();

        list.put(key, token.getValue());
      }
    }
  }

  /**
   * Return the number of parameters in this list.
   */
  public int size()
  {
    return list.size();
  }

  /**
   * Returns the value of the specified parameter.
   * Note that parameter names are case-insensitive.
   * @param name parameter name.
   * @return Value of the parameter. 
   * Returns null if the parameter is not present.
   */
  public String get(String name)
  {
    return (String)list.get(name.toLowerCase().trim());
  }

  /**
   * Set a parameter.
   * If this parameter already exists, it is replaced by this new value.
   * @param name name of the parameter.
   * @param value value of the parameter.
   */
  public void set(String name, String value)
  {
    list.put(name.toLowerCase().trim(), value);
  }

  /**
   * Removes the specified parameter from this ParameterList.
   * This method does nothing if the parameter is not present.
   * @param name name of the parameter.
   */
  public void remove(String name)
  {
    list.remove(name.toLowerCase().trim());
  }

  /**
   * Return an enumeration of the names of all parameters in this list.
   */
  public Enumeration getNames()
  {
    return new ParameterEnumeration(list.keySet().iterator());
  }

  /**
   * Convert this ParameterList into a MIME String.
   * If this is an empty list, an empty string is returned.
   */
  public String toString()
  {
    // Simply calls toString(int) with a used value of 0.
    return toString(0);
  }

  /**
   * Convert this ParameterList into a MIME String.
   * If this is an empty list, an empty string is returned.
   * The 'used' parameter specifies the number of character positions 
   * already taken up in the field into which the resulting parameter 
   * list is to be inserted. It's used to determine where to fold the
   * resulting parameter list.
   * @param used number of character positions already used, in the field into
   * which the parameter list is to be inserted.
   */
  public String toString(int used)
  {
    StringBuffer buffer = new StringBuffer();
    for (Iterator i = list.keySet().iterator(); i.hasNext(); )
    {
      String key = (String)i.next();
      String value = MimeUtility.quote((String)list.get(key), 
          HeaderTokenizer.MIME);
      
      // delimiter
      buffer.append("; ");
      used += 2;
      
      // wrap to next line if necessary
      int len = key.length()+value.length()+1;
      if ((used+len)>76)
      {
        buffer.append("\r\n\t");
        used = 8;
      }
      
      // append key=value
      buffer.append(key);
      buffer.append('=');
      buffer.append(value);
    }
    return buffer.toString();
  }
  
  /*
   * Needed to provide an enumeration interface for the key iterator.
   */
  static class ParameterEnumeration
    implements Enumeration
  {

    Iterator source;

    ParameterEnumeration(Iterator source)
    {
      this.source = source;
    }

    public boolean hasMoreElements()
    {
      return source.hasNext();
    }

    public Object nextElement()
    {
      return source.next();
    }
    
  }

}
