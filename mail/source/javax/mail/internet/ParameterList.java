/*
 * ParameterList.java
 * Copyright(C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
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
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds MIME parameters(attribute-value pairs).
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
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
    HashMap charsets = new HashMap();
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
    return (String) list.get(name.toLowerCase().trim());
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
