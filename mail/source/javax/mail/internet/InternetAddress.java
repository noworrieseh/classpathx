/*
 * InternetAddress.java
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.mail.Address;
import javax.mail.Session;

/**
 * This class models an RFC822 address.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class InternetAddress
  extends Address
  implements Cloneable
{

  /*
   * The type of InternetAddreses.
   */
  private static final String RFC822 = "rfc822";

  /**
   * The string form of the address.
   */
  protected String address;

  /**
   * The personal name.
   */
  protected String personal;

  /**
   * The RFC 2047 encoded version of the personal name.
   */
  protected String encodedPersonal;
  
  /**
   * Default constructor.
   */
  public InternetAddress()
  {
  }

  /**
   * Parse the given string and create an InternetAddress.
   * @param address the address in RFC822 format
   * @exception AddressException if the parse failed
   */
  public InternetAddress(String address)
    throws AddressException
  {
    InternetAddress[] addresses = parse(address, true);
    if (addresses.length!=1)
      throw new AddressException("Illegal address", address);
    this.address = addresses[0].address;
    this.personal = addresses[0].personal;
    this.encodedPersonal = addresses[0].encodedPersonal;
  }

  private InternetAddress(String s, boolean flag)
    throws AddressException
  {
    this(s);
    if (flag)
      checkAddress(address, true, true);
  }

  /**
   * Construct an InternetAddress given the address and personal name.
   * The address is assumed to be a syntactically valid RFC822 address.
   * @param address the address in RFC822 format
   * @param personal the personal name
   */
  public InternetAddress(String address, String personal)
    throws UnsupportedEncodingException
  {
    this(address, personal, null);
  }

  /**
   * Construct an InternetAddress given the address and personal name.
   * The address is assumed to be a syntactically valid RFC822 address.
   * @param address the address in RFC822 format
   * @param personal the personal name
   * @param charset the charset for the name
   */
  public InternetAddress(String address, String personal, String charset)
    throws UnsupportedEncodingException
  {
    this.address = address;
    setPersonal(personal, charset);
  }

  /**
   * Return a copy of this InternetAddress object.
   */
  public Object clone()
  {
    InternetAddress address = new InternetAddress();
    address.address = this.address;
    address.personal = personal;
    address.encodedPersonal = encodedPersonal;
    return address;
  }

  /**
   * Return the type of this address. The type of an InternetAddress is
   * "rfc822".
   */
  public String getType()
  {
    return RFC822;
  }

  /**
   * Set the email address.
   */
  public void setAddress(String address)
  {
    this.address = address;
  }

  /**
   * Set the personal name.
   * If the name contains non US-ASCII characters, then the name will be 
   * encoded using the specified charset as per RFC 2047. If the name 
   * contains only US-ASCII characters, no encoding is done and the
   * name is used as is.
   * @param name personal name
   * @param charset charset to be used to encode the name as per RFC 2047.
   * @param UnsupportedEncodingException if the charset encoding fails.
   */
  public void setPersonal(String name, String charset)
    throws UnsupportedEncodingException
  {
    personal = name;
    if (name!=null)
    {
      if (charset==null)
        encodedPersonal = MimeUtility.encodeWord(name);
      else
        encodedPersonal = MimeUtility.encodeWord(name, charset, null);
    }
    else
      encodedPersonal = null;
  }

  /**
   * Set the personal name.
   * If the name contains non US-ASCII characters, then the name will be 
   * encoded using the platform's default charset. If the name 
   * contains only US-ASCII characters, no encoding is done and the
   * name is used as is.
   * @param name - personal name
   * @exception UnsupportedEncodingException - if the charset encoding fails.
   */
  public void setPersonal(String name)
    throws UnsupportedEncodingException
  {
    setPersonal(name, null);
  }

  /**
   * Get the email address.
   */
  public String getAddress()
  {
    return address;
  }

  /**
   * Get the personal name.
   * If the name is encoded as per RFC 2047, it is decoded and converted 
   * into Unicode. If the decoding or convertion fails, the raw data is 
   * returned as is.
   */
  public String getPersonal()
  {
    if (personal!=null)
      return personal;
    if (encodedPersonal!=null)
    {
      try
      {
        personal = MimeUtility.decodeText(encodedPersonal);
        return personal;
      }
      catch (Exception e)
      {
        return encodedPersonal;
      }
    }
    return null;
  }

  /**
   * Convert this address into a RFC 822 / RFC 2047 encoded address.
   * The resulting string contains only US-ASCII characters,
   * and hence is mail-safe.
   */
  public String toString()
  {
    if (encodedPersonal==null && personal!=null)
    {
      try
      {
        encodedPersonal = MimeUtility.encodeWord(personal);
      }
      catch (UnsupportedEncodingException e)
      {
      }
    }
    
    StringBuffer buffer = new StringBuffer();
    if (encodedPersonal!=null)
    {
      buffer.append(quote(encodedPersonal));
      buffer.append(' ');
      buffer.append('<');
      buffer.append(address);
      buffer.append('>');
    }
    else if (isGroupAddress(address) || isSimpleAddress(address))
    {
      buffer.append(address);
    }
    else
    {
      buffer.append('<');
      buffer.append(address);
      buffer.append('>');
    }
    return buffer.toString();
  }

  /**
   * Returns a properly formatted address (RFC 822 syntax) of Unicode
   * characters.
   */
  public String toUnicodeString()
  {
    StringBuffer buffer = new StringBuffer();
    if (getPersonal()!=null)
    {
      buffer.append(quote(personal));
      buffer.append(' ');
      buffer.append('<');
      buffer.append(address);
      buffer.append('>');
    }
    else if (isGroupAddress(address) || isSimpleAddress(address))
    {
      buffer.append(address);
    }
    else
    {
      buffer.append('<');
      buffer.append(address);
      buffer.append('>');
    }
    return buffer.toString();
  }

  /*
   * Indicates if this address is simple.
   */
  private static boolean isSimpleAddress(String address)
  {
    if (address.indexOf('"')>-1)
      return false;
    if (address.indexOf('(')>-1)
      return false;
    if (address.indexOf(')')>-1)
      return false;
    if (address.indexOf(',')>-1)
      return false;
    if (address.indexOf(':')>-1)
      return false;
    if (address.indexOf(';')>-1)
      return false;
    if (address.indexOf('<')>-1)
      return false;
    if (address.indexOf('>')>-1)
      return false;
    if (address.indexOf('[')>-1)
      return false;
    if (address.indexOf('\\')>-1)
      return false;
    if (address.indexOf(']')>-1)
      return false;
    return true;
  }

  /*
   * Indicates if this address is a group address (see RFC822).
   */
  private static boolean isGroupAddress(String address)
  {
    int len = address.length();
    return (len>0 && address.indexOf(':')>0 && address.charAt(len-1)==';');
  }

  /**
   * The equality operator.
   */
  public boolean equals(Object other)
  {
    if (other instanceof InternetAddress)
    {
      String otherAddress = ((InternetAddress)other).getAddress();
      return (this==other || 
        (address!=null && address.equalsIgnoreCase(otherAddress)));
    }
    return false;
  }

  /**
   * Compute a hash code for the address.
   */
  public int hashCode()
  {
    return (address==null) ? 0 : address.hashCode();
  }

  /**
   * Convert the given array of InternetAddress objects into a comma separated
   * sequence of address strings.
   * The resulting string contains only US-ASCII characters,
   * and hence is mail-safe.
   * @param addresses array of InternetAddress objects
   * @return comma separated address strings
   * @exception ClassCastException if any address object in the given array 
   * is not an InternetAddress objects. Note that this is a RuntimeException.
   */
  public static String toString(Address[] addresses)
  {
    return toString(addresses, 0);
  }

  /**
   * Convert the given array of InternetAddress objects into a comma separated
   * sequence of address strings.
   * The resulting string contains only US-ASCII characters,
   * and hence is mail-safe.
   * <p>
   * The 'used' parameter specifies the number of character positions already
   * taken up in the field into which the resulting address sequence string is
   * to be inserted. Its used to determine the line-break positions in the
   * resulting address sequence string.
   * @param addresses array of InternetAddress objects
   * @param used number of character positions already used, in the field into
   * which the address string is to be inserted.
   * @return comma separated address strings
   * @exception ClassCastException if any address object in the given array 
   * is not an InternetAddress object. Note that this is a RuntimeException.
   */
  public static String toString(Address[] addresses, int used)
  {
    if (addresses==null || addresses.length==0)
      return null;
    String crlf = "\r\n";
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i<addresses.length; i++)
    {
      if (i!=0)
      {
        buffer.append(", ");
        used += 2;
      }
      String addressText = addresses[i].toString();
      int len = addressText.length();
      int fl = addressText.indexOf(crlf); // pos of first crlf
      if (fl<0)
        fl = addressText.length();
      int ll = addressText.lastIndexOf(crlf); // pos of last crlf
      
      if ((used+fl)>76)
      {
        buffer.append("\r\n\t");
        used = 8;
      }
      buffer.append(addressText);
      used = (ll>-1) ? (used+len) : (len-ll-2);
    }
    return buffer.toString();
  }

  /**
   * Return an InternetAddress object representing the current user.
   * The entire email address may be specified in the "mail.from" property.
   * If not set, the "mail.user" and "mail.host" properties are tried.
   * If those are not set, the "user.name" property and 
   * InetAddress.getLocalHost method are tried.
   * Security exceptions that may occur while accessing this information are
   * ignored.
   * If it is not possible to determine an email address, null is returned.
   * @param session Session object used for property lookup
   * @return current user's email address
   */
  public static InternetAddress getLocalAddress(Session session)
  {
    String username = null;
    String hostname = null;
    String address = null;
    try
    {
      if (session==null)
      {
        username = System.getProperty("user.name");
        hostname = InetAddress.getLocalHost().getHostName();
      }
      else
      {
        address = session.getProperty("mail.from");
        if (address==null)
        {
          username = session.getProperty("mail.user");
          if (username==null)
            username = session.getProperty("user.name");
          if (username==null)
            username = System.getProperty("user.name");
          hostname = session.getProperty("mail.host");
          if (hostname==null)
          {
            InetAddress localhost = InetAddress.getLocalHost();
            if (localhost!=null)
              hostname = localhost.getHostName();
          }
        }
      }
      if (address==null && username!=null && hostname!=null)
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append(username);
        buffer.append('@');
        buffer.append(hostname);
        address = buffer.toString();
      }
      if (address!=null)
        return new InternetAddress(address);
    }
    catch (AddressException e)
    {
    }
    catch (SecurityException e)
    {
    }
    catch (UnknownHostException e)
    {
    }
    return null;
  }

  /**
   * Parse the given comma separated sequence of addresses into 
   * InternetAddress objects.
   * Addresses must follow RFC822 syntax.
   * @param addresslist comma separated address strings
   * @return array of InternetAddress objects
   * @exception AddressException if the parse failed
   */
  public static InternetAddress[] parse(String addresslist)
    throws AddressException
  {
    return parse(addresslist, true);
  }

  /**
   * Parse the given sequence of addresses into InternetAddress objects.
   * If <code>strict</code> is false, simple email addresses separated by 
   * spaces are also allowed. If strict is true, many (but not all) of the 
   * RFC822 syntax rules are enforced. In particular, even if strict is true,
   * addresses composed of simple names (with no "@domain" part) are allowed.
   * Such "illegal" addresses are not uncommon in real messages.
   * <p>
   * Non-strict parsing is typically used when parsing a list of mail 
   * addresses entered by a human.
   * Strict parsing is typically used when parsing address headers in mail 
   * messages.
   * @param addresslist comma separated address strings
   * @param strict enforce RFC822 syntax
   * @return array of InternetAddress objects
   * @exception AddressException if the parse failed
   */
  public static InternetAddress[] parse(String addresslist, boolean strict)
    throws AddressException
  {
    /*
     * address := mailbox / group ; one addressee, named list
     * group := phrase ":" [#mailbox] ";"
     * mailbox := addr-spec / phrase route-addr ; simple address,
     *                                          ; name & addr-spec
     * route-addr := "<" [route] addr-spec ">"
     * route := 1#("@" domain) ":" ; path-relative
     * addr-spec := local-part "@" domain ; global address
     * local-part := word *("." word) ; uninterpreted, case-preserved
     * domain := sub-domain *("." sub-domain)
     * sub-domain := domain-ref / domain-literal
     * domain-ref := atom ; symbolic reference
     */

    // NB I have been working on this parse for about 8 hours now.
    // It is very likely I am starting to lose the plot.
    // If anyone wants to work on it, I strongly recommend you write some
    // kind of tokenizer and attack it from that direction.

    boolean inGroup = false;
    boolean gotDelimiter = false;
    boolean inAddress = false;
    int len = addresslist.length();
    int pEnd = -1;
    int pStart = -1;
    int start = -1;
    int end = -1;
    ArrayList acc = new ArrayList();

    int pos;
    for (pos = 0; pos<len; pos++)
    {
      char c = addresslist.charAt(pos);
      //System.out.println("c="+c);
      switch (c)
      {
        case '\t':
        case '\n':
        case '\r':
        case ' ':
          break;
          
        case '<': // bra-ket delimited address
          inAddress = true;
          if (gotDelimiter)
            throw new AddressException("Too many route-addr", addresslist, pos);
          if (!inGroup)
          {
            start = pStart;
            if (start>=0)
              end = pos;
            pStart = pos + 1;
          }
          pos++;
          boolean inQuote = false;
          boolean gotKet = false;
          while (pos<len && !gotKet)
          {
            char c2 = addresslist.charAt(pos);
            //System.out.println("c2="+c2);
            switch (c2)
            {
              case '"':
                inQuote = !inQuote;
                break;
              case '>':
                if (!inQuote)
                {
                  gotKet = true;
                  pos--;
                }
                break;
              case '\\':
                pos++;
                break;
            }
            pos++;
          }
          if (!gotKet && pos>=len)
          {
            if (inQuote)
              throw new AddressException("Unmatched '\"'", addresslist, pos);
            else
              throw new AddressException("Unmatched '<'", addresslist, pos);
          }
          gotDelimiter = true;
          pEnd = pos;
          break;
        case '>':
          throw new AddressException("Unmatched '>'", addresslist, pos);
          
        case '(': // paren delimited personal
          inAddress = true;
          if (pStart>=0 && pEnd==-1)
            pEnd = pos;
          if (start==-1)
            start = pos+1;
          pos++;
          int parenCount = 1;
          while (pos<len && parenCount>0)
          {
            c = addresslist.charAt(pos);
            switch (c)
            {
              case '(':
                parenCount++;
                break;
              case ')':
                parenCount--;
                break;
              case '\\':
                pos++;
                break;
            }
            pos++;
          }
          if (parenCount>0)
            throw new AddressException("Unmatched '('", addresslist, pos);
          pos--;
          if (end==-1)
            end = pos;
          break;
        case ')':
          throw new AddressException("Unmatched ')'", addresslist, pos);
          
        case '"': // quote delimited personal
          inAddress = true;
          if (pStart==-1)
            pStart = pos;
          pos++;
          boolean gotQuote = false;
          while (pos<len && !gotQuote)
          {
            c = addresslist.charAt(pos);
            switch (c)
            {
              case '"':
                gotQuote = true;
                pos--;
                break;
              case '\\':
                pos++;
                break;
            }
            pos++;
          }
          if (pos>=len)
            throw new AddressException("Unmatched '\"'", addresslist, pos);
          break;
          
        case '[':
          inAddress = true;
          pos++;
          boolean gotBracket = false;
          while (pos<len && !gotBracket)
          {
            c = addresslist.charAt(pos);
            switch (c)
            {
              case ']':
                gotBracket = true;
                pos--;
                break;
              case '\\':
                pos++;
                break;
            }
            pos++;
          }
          if (pos>=len)
            throw new AddressException("Unmatched '['", addresslist, pos);
          break;
          
        case ',': // address delimiter
          if (pStart==-1)
          {
            gotDelimiter = false;
            inAddress = false;
            pStart = pEnd = -1;
            break;
          }
          if (inGroup)
            break;
          if (pEnd==-1)
            pEnd = pos;
          {
            String addressText = addresslist.substring(pStart, pEnd).trim();
            if (inAddress || strict)
            {
              checkAddress(addressText, gotDelimiter, strict);
              InternetAddress address = new InternetAddress();
              address.setAddress(addressText);
              if (start>=0)
              {
                String personal = addresslist.substring(start, end).trim();
                address.encodedPersonal = unquote(personal);
                start = end = -1;
              }
              acc.add(address);
            }
            else
            {
              StringTokenizer st = new StringTokenizer(addressText);
              while (st.hasMoreTokens())
              {
                addressText = st.nextToken();
                checkAddress(addressText, false, strict);
                InternetAddress address = new InternetAddress();
                address.setAddress(addressText);
                acc.add(address);
              }
            }
          }
          gotDelimiter = false;
          inAddress = false;
          pStart = pEnd = -1;
          break;
          
        case ':': // group indicator
          inAddress = true;
          if (inGroup)
            throw new AddressException("Cannot have nested group",
                addresslist, pos);
          inGroup = true;
          break;
        case ';': // group delimiter
          if (!inGroup)
            throw new AddressException("Unexpected ';'", addresslist, pos);
          inGroup = false;
          pEnd = pos+1;
          {
            String addressText = addresslist.substring(pStart, pEnd).trim();
            InternetAddress address = new InternetAddress();
            address.setAddress(addressText);
            acc.add(address);
          }
          gotDelimiter = false;
          pStart = pEnd = -1;
          break;
          
        default:
          if (pStart==-1)
            pStart = pos;
          break;
      }
    }
    
    if (pStart>-1)
    {
      if (pEnd==-1)
        pEnd = pos;
      String addressText = addresslist.substring(pStart, pEnd).trim();
      if (inAddress || strict)
      {
        checkAddress(addressText, gotDelimiter, strict);
        InternetAddress address = new InternetAddress();
        address.setAddress(addressText);
        if (start>=0)
        {
          String personal = addresslist.substring(start, end).trim();
          address.encodedPersonal = unquote(personal);
        }
        acc.add(address);
      }
      else
      {
        StringTokenizer st = new StringTokenizer(addressText);
        while (st.hasMoreTokens())
        {
          addressText = st.nextToken();
          checkAddress(addressText, false, strict);
          InternetAddress address = new InternetAddress();
          address.setAddress(addressText);
          acc.add(address);
        }
      }
    }
    
    InternetAddress[] addresses = new InternetAddress[acc.size()];
    acc.toArray(addresses);
    return addresses;
  }

  private static void checkAddress(String address, boolean gotDelimiter,
      boolean strict)
    throws AddressException
  {
    // TODO What happens about addresses with quoted strings?
    int pos = 0;
    if (!strict || gotDelimiter)
    {
      int i = address.indexOf(',', pos);
      if (i<0)
        i = address.indexOf(':', pos);
      while (i>-1)
      {
        if (address.charAt(pos)!='@')
          throw new AddressException("Illegal route-addr", address);
        if (address.charAt(i)!=':')
        {
          i = address.indexOf(',', pos);
          if (i<0)
            i = address.indexOf(':', pos);
        }
        else
        {
          pos = i + 1;
          i = -1;
        }
      }
    }
    
    // Get atomic parts
    String localName = address;
    String domain = null;
    int atIndex = address.indexOf('@', pos);
    if (atIndex>-1)
    {
      if (atIndex==pos)
        throw new AddressException("Missing local name", address);
      if (atIndex==address.length()-1)
        throw new AddressException("Missing domain", address);
      localName = address.substring(pos, atIndex);
      domain = address.substring(atIndex+1);
    }

    // Check atomic parts
    String illegalWS = "\t\n\r ";
    for (int i = 0; i<illegalWS.length(); i++)
    {
      if (address.indexOf(illegalWS.charAt(i))>-1)
        throw new AddressException("Illegal whitespace", address);
    }
    String illegalName = "\"(),:;<>@[\\]";
    for (int i = 0; i<illegalName.length(); i++)
    {
      if (localName.indexOf(illegalName.charAt(i))>-1)
        throw new AddressException("Illegal local name", address);
    }
    if (domain!=null)
    {
      for (int i = 0; i<illegalName.length(); i++)
      {
        if (localName.indexOf(illegalName.charAt(i))>-1)
          throw new AddressException("Illegal domain", address);
      }
    }
  }

  /*
   * The list of characters that need quote-escaping.
   */
  private static final String needsQuoting = "()<>@,;:\\\".[]";

  /*
   * Quote-escapes the specified text.
   */
  private static String quote(String text)
  {
    int len = text.length();
    boolean needsQuotes = false;
    for (int i = 0; i<len; i++)
    {
      char c = text.charAt(i);
      if (c=='"' || c=='\\')
      {
        StringBuffer buffer = new StringBuffer(len+3);
        buffer.append('"');
        for (int j = 0; j<len; j++)
        {
          c = text.charAt(j);
          if (c=='"' || c=='\\')
            buffer.append('\\');
          buffer.append(c);
        }
        buffer.append('"');
        return buffer.toString();
      }
      if ((c<' ' && c!='\r' && c!='\n' && c!='\t') ||
          (c>='\177') ||
          needsQuoting.indexOf(c)>-1)
        needsQuotes = true;
    }

    if (needsQuotes)
    {
      StringBuffer buffer = new StringBuffer(len+2);
      buffer.append('"');
      buffer.append(text);
      buffer.append('"');
      text = buffer.toString();
    }
    return text;
  }

  /*
   * Un-quote-escapes the specified text.
   */
  private static String unquote(String text)
  {
    int len = text.length();
    if (len>2 && text.charAt(0)=='"' && text.charAt(len-1)=='"')
    {
      text = text.substring(1, len-1);
      if (text.indexOf('\\')>-1)
      {
        len -= 2;
        StringBuffer buffer = new StringBuffer(len);
        for (int i = 0; i<len; i++)
        {
          char c = text.charAt(i);
          if (c=='\\' && i<(len-1))
            c = text.charAt(++i);
          buffer.append(c);
        }
        text = buffer.toString();
      }
    }
    return text;
  }

}
