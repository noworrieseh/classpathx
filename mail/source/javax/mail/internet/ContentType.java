/*
 * ContentType.java
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

/**
 * This class represents a MIME Content-Type value.
 * It provides methods to parse a Content-Type string into individual 
 * components and to generate a MIME style Content-Type string.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class ContentType
{

  /*
   * The primary type.
   */
  private String primaryType;

  /*
   * The subtype.
   */
  private String subType;

  /*
   * The list of additional parameters.
   */
  private ParameterList list;

  /**
   * No-arg Constructor.
   */
  public ContentType()
  {
  }

  /**
   * Constructor.
   * @param primaryType the primary type
   * @param subType the subtype
   * @param list the list of additional parameters
   */
  public ContentType(String primaryType, String subType, ParameterList list)
  {
    this.primaryType = primaryType;
    this.subType = subType;
    this.list = list;
  }

  /**
   * Constructor that takes a Content-Type string.
   * The String is parsed into its constituents: primaryType, subType and 
   * parameters. A ParseException is thrown if the parse fails.
   * @param s the Content-Type string.
   * @exception ParseException if the parse fails.
   */
  public ContentType(String s)
    throws ParseException
  {
    HeaderTokenizer ht = new HeaderTokenizer(s, HeaderTokenizer.MIME);
    HeaderTokenizer.Token token = ht.next();
    if (token.getType() != HeaderTokenizer.Token.ATOM)
      {
        throw new ParseException();
      }
    primaryType = token.getValue();
    token = ht.next();
    if (token.getType() != 0x2f) // '/'
      {
        throw new ParseException();
      }
    token = ht.next();
    if (token.getType() != HeaderTokenizer.Token.ATOM)
      {
        throw new ParseException();
      }
    subType = token.getValue();
    s = ht.getRemainder();
    if (s != null)
      {
        list = new ParameterList(s);
      }
  }

  /**
   * Return the primary type.
   */
  public String getPrimaryType()
  {
    return primaryType;
  }

  /**
   * Return the subtype.
   */
  public String getSubType()
  {
    return subType;
  }

  /**
   * Return the MIME type string, without the parameters.
   * The returned value is basically the concatenation of the primaryType,
   * the '/' character and the secondaryType.
   */
  public String getBaseType()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(primaryType);
    buffer.append('/');
    buffer.append(subType);
    return buffer.toString();
  }

  /**
   * Return the specified parameter value.
   * Returns null if this parameter is absent.
   */
  public String getParameter(String name)
  {
    return (list == null) ? null : list.get(name);
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
   * Set the primary type.
   * Overrides existing primary type.
   * @param primaryType the primary type
   */
  public void setPrimaryType(String primaryType)
  {
    this.primaryType = primaryType;
  }

  /**
   * Set the subtype.
   * Overrides existing subtype
   * @param type the subtype
   */
  public void setSubType(String subType)
  {
    this.subType = subType;
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
      {
        list = new ParameterList();
      }
    list.set(name, value);
  }

  /**
   * Set a new parameter list.
   * @param list the Parameter list
   */
  public void setParameterList(ParameterList list)
  {
    this.list = list;
  }

  /**
   * Retrieve a RFC2045 style string representation of this Content-Type.
   * Returns null if the conversion failed.
   * @return RFC2045 style string
   */
  public String toString()
  {
    if (primaryType == null || subType == null)
      {
        return null;
      }
    
    StringBuffer buffer = new StringBuffer();
    buffer.append(primaryType);
    buffer.append('/');
    buffer.append(subType);
    if (list != null)
      {
        // Add the parameters, using the toString(int) method
        // which allows the resulting string to fold properly onto the next
        // header line.
        int used = buffer.length() + 14; // "Content-Type: ".length()
        buffer.append(list.toString(used));
      }
    return buffer.toString();
  }

  /**
   * Match with the specified ContentType object.
   * This method compares only the primaryType and subType.
   * The parameters of both operands are ignored.
   * <p>
   * For example, this method will return true when comparing the 
   * ContentTypes for "text/plain" and "text/plain; charset=foobar".
   * If the subType of either operand is the special character '*', then 
   * the subtype is ignored during the match.
   * For example, this method will return true when comparing the
   * ContentTypes for "text/plain" and "text/*"
   * @param cType the content type to compare this against
   */
  public boolean match(ContentType cType)
  {
    if (!primaryType.equalsIgnoreCase(cType.getPrimaryType()))
      {
        return false;
      }
    String cTypeSubType = cType.getSubType();
    if (subType.charAt(0) == '*' || cTypeSubType.charAt(0) == '*')
      {
        return true;
      }
    return subType.equalsIgnoreCase(cTypeSubType);
  }

  /**
   * Match with the specified content-type string.
   * This method compares only the primaryType and subType.
   * The parameters of both operands are ignored.
   * <p>
   * For example, this method will return true when comparing the 
   * ContentType for "text/plain" with "text/plain; charset=foobar".
   * If the subType of either operand is the special character '*', then 
   * the subtype is ignored during the match.
   * For example, this method will return true when comparing the 
   * ContentType for "text/plain" with "text/*"
   * @param s the string representation of the content type to match
   */
  public boolean match(String s)
  {
    try
      {
        return match(new ContentType(s));
      }
    catch (ParseException e)
      {
        return false;
      }
  }
  
}
