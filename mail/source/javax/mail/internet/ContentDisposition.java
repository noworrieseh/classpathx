/*
 * ContentDisposition.java
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

/**
 * This class represents a MIME ContentDisposition value.
 * It provides methods to parse a ContentDisposition string into 
 * individual components and to generate a MIME style ContentDisposition 
 * string.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class ContentDisposition
{

  /*
   * The disposition value.
   */
  private String disposition;

  /*
   * The available parameters.
   */
  private ParameterList list;

  /**
   * No-arg constructor.
   */
  public ContentDisposition()
  {
  }

  /**
   * Constructor.
   * @param disposition disposition
   * @param list ParameterList
   */
  public ContentDisposition(String disposition, ParameterList list)
  {
    this.disposition = disposition;
    this.list = list;
  }

  /**
   * Constructor that takes a ContentDisposition string.
   * The String is parsed into its constituents: disposition and parameters.
   * A ParseException is thrown if the parse fails.
   * @param s the ContentDisposition string.
   * @exception ParseException if the parse fails.
   */
  public ContentDisposition(String s)
    throws ParseException
  {
    HeaderTokenizer ht = new HeaderTokenizer(s, HeaderTokenizer.MIME);
    HeaderTokenizer.Token token = ht.next();
    if (token.getType()!=HeaderTokenizer.Token.ATOM)
      throw new ParseException();
    
    disposition = token.getValue();
    
    s = ht.getRemainder();
    if (s!=null)
      list = new ParameterList(s);
  }

  /**
   * Return the disposition value.
   * @return the disposition
   */
  public String getDisposition()
  {
    return disposition;
  }

  /**
   * Return the specified parameter value.
   * Returns null if this parameter is absent.
   * @param name the name of the parameter
   * @return the parameter value
   */
  public String getParameter(String name)
  {
    return (list!=null) ? list.get(name) : null;
  }

  /**
   * Return a ParameterList object that holds all the available parameters.
   * Returns null if no parameters are available.
   */
  public ParameterList getParameterList()
  {
    return list;
  }

  /**
   * Set the primary type. Overrides existing primary type.
   * @param primaryType the primary type
   */
  public void setDisposition(String disposition)
  {
    this.disposition = disposition;
  }

  /**
   * Set the specified parameter.
   * If this parameter already exists, it is replaced by this new value.
   * @param name the parameter name
   * @param value the parameter value
   */
  public void setParameter(String name, String value)
  {
    if (list == null)
      list = new ParameterList();
    list.set(name, value);
  }

  /**
   * Set a new ParameterList.
   * @param list the ParameterList
   */
  public void setParameterList(ParameterList list)
  {
    this.list = list;
  }

  /**
   * Retrieve a RFC2045 style string representation of this 
   * ContentDisposition.
   * Returns null if the conversion failed.
   * @return RFC2045 style string
   */
  public String toString()
  {
    if (disposition==null)
      return null;
    if (list==null)
      return disposition;
    else
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(disposition);
      
      // Add the parameters, using the toString(int) method
      // which allows the resulting string to fold properly onto the next
      // header line.
      int used = buffer.length()+21; // "Content-Disposition: ".length()
      buffer.append(list.toString(used));
      return buffer.toString();
    }
  }
  
}
