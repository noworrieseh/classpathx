/*
 * NewsAddress.java
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

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.mail.Address;

/**
 * This class models an RFC1036 newsgroup address.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.3
 */
public class NewsAddress
  extends Address
{

  /*
   * The Address type.
   */
  private static final String TYPE = "news";
  
  /**
   * The name of the newsgroup.
   */
  protected String newsgroup;

  /**
   * The host name of the news server.
   */
  protected String host;

  /**
   * Default constructor.
   */
  public NewsAddress()
  {
  }

  /**
   * Construct a NewsAddress with the given newsgroup.
   * @param newsgroup the newsgroup
   */
  public NewsAddress(String newsgroup)
  {
    this(newsgroup, null);
  }

  /**
   * Construct a NewsAddress with the given newsgroup and host.
   * @param newsgroup the newsgroup
   * @param host the host
   */
  public NewsAddress(String newsgroup, String host)
  {
    this.newsgroup = newsgroup;
    this.host = host;
  }

  /**
   * Return the type of this address.
   * The type of a NewsAddress is "news".
   */
  public String getType()
  {
    return TYPE;
  }

  /**
   * Set the newsgroup.
   * @param newsgroup the newsgroup
   */
  public void setNewsgroup(String newsgroup)
  {
    this.newsgroup = newsgroup;
  }

  /**
   * Get the newsgroup.
   */
  public String getNewsgroup()
  {
    return newsgroup;
  }

  /**
   * Set the host.
   * @param host the host
   */
  public void setHost(String host)
  {
    this.host = host;
  }

  /**
   * Get the host.
   */
  public String getHost()
  {
    return host;
  }

  /**
   * Convert this address into a RFC 1036 address.
   */
  public String toString()
  {
    return newsgroup;
  }

  /**
   * The equality operator.
   */
  public boolean equals(Object a)
  {
    if (a instanceof NewsAddress)
      {
        NewsAddress na = (NewsAddress) a;
        return (newsgroup.equals(na.newsgroup) &&
               ((host == null && na.host == null) || 
                (host != null && na.host != null &&
                  host.equalsIgnoreCase(na.host))));
      }
    return false;
  }

  /**
   * Compute a hash code for the address.
   */
  public int hashCode()
  {
    int hashCode = 0;
    if (newsgroup != null)
      {
        hashCode += newsgroup.hashCode();
      }
    if (host != null)
      {
        hashCode += host.hashCode();
      }
    return hashCode;
  }

  /**
   * Convert the given array of NewsAddress objects into a comma separated
   * sequence of address strings.
   * The resulting string contains only US-ASCII characters, and hence 
   * is mail-safe.
   * @param addresses array of NewsAddress objects
   * @return comma separated address strings
   * @exception ClassCastException if any address object in the given array 
   * is not a NewsAddress object. Note that this is a RuntimeException.
   */
  public static String toString(Address[] addresses)
  {
    if (addresses == null || addresses.length == 0)
      {
        return null;
      }
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < addresses.length; i++)
      {
        if (i > 0)
          {
            buffer.append(',');
          }
        // possible ClassCastException...
        NewsAddress na = (NewsAddress) addresses[i];
        buffer.append(na.toString());
      }
    return buffer.toString();
  }

  /**
   * Parse the given comma separated sequence of newsgroups into NewsAddress
   * objects.
   * @param newsgroups comma separated newsgroup string
   * @return array of NewsAddress objects
   * @exception AddressException if the parse failed
   */
  public static NewsAddress[] parse(String newsgroups)
    throws AddressException
  {
    StringTokenizer st = new StringTokenizer(newsgroups, ",");
    ArrayList acc = new ArrayList();
    while (st.hasMoreTokens())
      {
        acc.add(new NewsAddress(st.nextToken()));
      }
    NewsAddress[] addresses = new NewsAddress[acc.size()];
    acc.toArray(addresses);
    return addresses;
  }
  
}
