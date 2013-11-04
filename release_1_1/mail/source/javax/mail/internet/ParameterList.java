/*
 * ParameterList.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
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

package javax.mail.internet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * A list of MIME parameters. MIME parameters are name-value pairs
 * associated with a MIME header.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class ParameterList
{

  /*
   * The underlying storage.
   */
  private LinkedHashMap list = new LinkedHashMap();

  /**
   * Constructor for an empty parameter list.
   */
  public ParameterList()
  {
  }

  /**
   * Constructor with a parameter-list string.
   * @param s the parameter-list string
   * @exception ParseException if the parse fails
   */
  public ParameterList(String s)
    throws ParseException
  {
    LinkedHashMap charsets = new LinkedHashMap();
    HeaderTokenizer ht = new HeaderTokenizer(s, HeaderTokenizer.MIME);
    for (int type = 0; type != HeaderTokenizer.Token.EOF; )
      {
        HeaderTokenizer.Token token = ht.next();
        type = token.getType();
        
        if (type != HeaderTokenizer.Token.EOF)
          {
            if (type != 0x3b) // ';'
              {
                throw new ParseException("expected ';': " + s);
              }
            
            token = ht.next();
            type = token.getType();
            if (type != HeaderTokenizer.Token.ATOM)
              {
                throw new ParseException("expected parameter name: " + s);
              }
            String key = token.getValue().toLowerCase();
            
            token = ht.next();
            type = token.getType();
            if (type != 0x3d) // '='
              {
                throw new ParseException("expected '=': " + s);
              }
            
            token = ht.next();
            type = token.getType();
            if (type != HeaderTokenizer.Token.ATOM && 
                type != HeaderTokenizer.Token.QUOTEDSTRING)
              {
                throw new ParseException("expected parameter value: " + s);
              }
            String value = token.getValue();

            // Handle RFC 2231 encoding and continuations
            // This will handle out-of-order extended-other-values
            // but the extended-initial-value must precede them
            int si = key.indexOf('*');
            if (si > 0)
              {
                int len = key.length();
                if (si == len - 1 ||
                   (si == len - 3 &&
                     key.charAt(si + 1) == '0' &&
                     key.charAt(si + 2) == '*'))
                  {
                    // extended-initial-name
                    key = key.substring(0, si);
                    // extended-initial-value
                    int ai = value.indexOf('\'');
                    if (ai == -1)
                      {
                        throw new ParseException("no charset specified: " +
                                                  value);
                      }
                    String charset = value.substring(0, ai);
                    charset = MimeUtility.javaCharset(charset);
                    charsets.put(key, charset);
                    // advance to last apostrophe
                    for (int i = value.indexOf('\'', ai + 1); i != -1; )
                      {
                        ai = i;
                        i = value.indexOf('\'', ai + 1);
                      }
                    value = decode(value.substring(ai + 1), charset);
                    ArrayList values = new ArrayList();
                    set(values, 0, value);
                    list.put(key, values);
                  }
                else
                  {
                    // extended-other-name
                    int end = (key.charAt(len - 1) == '*') ? len - 1 : len;
                    int section = -1;
                    try
                      {
                        section =
                          Integer.parseInt(key.substring(si + 1, end));
                        if (section < 1)
                          {
                            throw new NumberFormatException();
                          }
                      }
                    catch (NumberFormatException e)
                      {
                        throw new ParseException("invalid section: " + key);
                      }
                    key = key.substring(0, si);
                    // extended-other-value
                    String charset = (String) charsets.get(key);
                    ArrayList values = (ArrayList) list.get(key);
                    if (charset == null || values == null)
                      {
                        throw new ParseException("no initial extended " +
                                                  "parameter for '" + key +
                                                  "'");
                      }
                    if (type == HeaderTokenizer.Token.ATOM)
                      {
                        value = decode(value, charset);
                      }
                    set(values, section, value);
                  }
              }
            else
              {
                list.put(key, value);
              }
          }
      }
    // Replace list values by string concatenations of their components
    int len = list.size();
    String[] keys = new String[len];
    list.keySet().toArray(keys);
    for (int i = 0; i < len; i++)
      {
        Object value = list.get(keys[i]);
        if (value instanceof ArrayList)
          {
            ArrayList values = (ArrayList) value;
            StringBuffer buf = new StringBuffer();
            for (Iterator j = values.iterator(); j.hasNext(); )
              {
                String comp = (String) j.next();
                if (comp != null)
                  {
                    buf.append(comp);
                  }
              }
            list.put(keys[i], buf.toString());
          }
      }
  }

  private void set(ArrayList list, int index, Object value)
  {
    int len = list.size();
    while (index > len - 1)
      {
        list.add(null);
        len++;
      }
    list.set(index, value);
  }

  private String decode(String text, String charset)
    throws ParseException
  {
    char[] schars = text.toCharArray();
    int slen = schars.length;
    byte[] dchars = new byte[slen];
    int dlen = 0;
    for (int i = 0; i < slen; i++)
      {
        char c = schars[i];
        if (c == '%')
          {
            if (i + 3 > slen)
              {
                throw new ParseException("malformed: " + text);
              }
            int val = Character.digit(schars[i + 2], 16) +
              Character.digit(schars[i + 1], 16) * 16;
            dchars[dlen++] = ((byte) val);
            i += 2;
          }
        else
          {
            dchars[dlen++] = ((byte) c);
          }
      }
    try
      {
        return new String(dchars, 0, dlen, charset);
      }
    catch (UnsupportedEncodingException e)
      {
        throw new ParseException("Unsupported encoding: " + charset);
      }
  }

  /**
   * Returns the number of parameters in this list.
   */
  public int size()
  {
    return list.size();
  }

  /**
   * Returns the value of the specified parameter.
   * Parameter names are case insensitive.
   * @param name the parameter name
   */
  public String get(String name)
  {
    return (String) list.get(name.toLowerCase().trim());
  }

  /**
   * Sets the specified parameter.
   * @param name the parameter name
   * @param value the parameter value
   */
  public void set(String name, String value)
  {
    list.put(name.toLowerCase().trim(), value);
  }

  /**
   * Removes the specified parameter from this list.
   * @param name the parameter name
   */
  public void remove(String name)
  {
    list.remove(name.toLowerCase().trim());
  }

  /**
   * Returns the names of all parameters in this list.
   * @return an Enumeration of String
   */
  public Enumeration getNames()
  {
    return new ParameterEnumeration(list.keySet().iterator());
  }

  /**
   * Returns the MIME string representation of this parameter list.
   */
  public String toString()
  {
    // Simply calls toString(int) with a used value of 0.
    return toString(0);
  }

  /**
   * Returns the MIME string representation of this parameter list.
   * @param used the number of character positions already used in the
   * field into which the parameter list is to be inserted
   */
  public String toString(int used)
  {
    StringBuffer buffer = new StringBuffer();
    for (Iterator i = list.keySet().iterator(); i.hasNext(); )
      {
        String key = (String) i.next();
        String value = MimeUtility.quote((String) list.get(key), 
                                          HeaderTokenizer.MIME);
        
        // delimiter
        buffer.append("; ");
        used += 2;
        
        // wrap to next line if necessary
        int len = key.length() + value.length() + 1;
        if ((used + len) > 76)
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

