/*
 * ResultHandler.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU inetlib, a library.
 * 
 * GNU inetlib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU inetlib is distributed in the hope that it will be useful,
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

package gnu.inet.ldap;

import java.util.List;
import java.util.Map;

/**
 * Callback handler for receiving notification of search results.
 * The application must pass an implementation of this interface into the
 * <code>LDAPConnection.search</code> method. Search responses received
 * during the execution of the method result in calls to the methods defined
 * in this interface.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface ResultHandler
{

  /**
   * Receive an LDAP SearchResultEntry response.
   * The attributes map provides a mapping of attribute names to values. In
   * the case where <code>typesOnly</code> was <code>true</code>, the value
   * for each attribute will be null. Otherwise it will be a Set of
   * attribute values, which may be of the following types:
   * <ul>
   * <li>java.lang.String</li>
   * <li>java.lang.Integer</li>
   * <li>java.lang.Double</li>
   * <li>java.lang.Boolean</li>
   * <li>byte[]</li>
   * <ul>
   * @param name the object name DN
   * @param attributes a map of attribute names to values
   */
  void searchResultEntry (String name, Map attributes);

  /**
   * Receive an LDAP SearchResultReference response.
   * The argument to this function is a sequence of LDAP URLs, one for each
   * entry not explored by the server during the search.
   * @param urls the list of LDAP URLs
   */
  void searchResultReference (List urls);
  
}
