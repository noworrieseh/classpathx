/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 1998, 1999, 2001   Free Software Foundation, Inc.

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.servlet.http;

import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.ServletInputStream;

/**
 * A set of utility methods for http server writers.
 *
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 */
public class HttpUtils
{
  // must be >= 2 or parsePostData will generate lots of
  // StringIndexOutOfBoundsExceptions for you.
  private static final int PARSE_POST_DATA_CHUNK_SIZE = 32;

  /**
   * Creates a HttpUtils object, cool!
   *
   * @since Servlet API 1.0
   */
  public HttpUtils() 
  {
  }


  /**
   * Turns a http QUERY_STRING that conforms to
   * rfc1945("Hypertext Transfer Protocol -- HTTP/1.0") or
   * rfc2068 ("Hypertext Transfer Protocol -- HTTP/1.1") into a Hashtable
   * with key = key and as values arrays of String.
   * Implementation note: when a key or value is missing it will be
   * represented as a zero length string.
   * <P>
   * Results:<BR>
   * ?a=b : key = "a", value = "b"<BR>
   * ?a   : key = "a", value = ""<BR>
   * ?=b  : key = "",  value = "b"<BR>
   * <P>
   * @since Servlet API 1.0
   *
   * @param queryString The queryString to process
   * @return a Hashtable with String keys, and array of String values.
   * @exception IllegalArgumentException If the queryString contains
   * an error it can't handle.
   */
  public static Hashtable parseQueryString(String queryString)
  throws IllegalArgumentException 
  {
    // Use of a StringTokenizer would be easier to build and
    // maintain, but leads to the creation of many unnecessary
    // substring calls.
    // This is a bit more work, but should be more efficient.

    if(queryString == null) 
    {
      throw new IllegalArgumentException();
    }
    Hashtable result = new Hashtable();

    int parameterBegin = 0;
    int parameterSeparator = queryString.indexOf('=',parameterBegin);
    int parameterEnd;

    while(parameterBegin < queryString.length()) 
    {

      parameterEnd = queryString.indexOf('&',parameterBegin);
      if(parameterEnd == -1) 
      {
	parameterEnd = queryString.length();
      }
      if((parameterSeparator > parameterEnd)
	 ||(parameterSeparator < 0)) 
      {
	addParameter(
		     result,
		     urlDecode(queryString,parameterBegin,parameterEnd),
		     "");
	// XXX what should be the policy in this case?
      } else 
      {
	addParameter(
		     result,
		     urlDecode(queryString,
			       parameterBegin,
			       parameterSeparator),
		     urlDecode(queryString,
			       parameterSeparator+1,
			       parameterEnd));
	parameterSeparator = queryString.indexOf('=',parameterEnd + 1);
      }
      parameterBegin = parameterEnd + 1;
    }
    return result;
  }


  private static void addParameter(Hashtable parameterTable,String name,String value) 
  {
    try 
    {
      String[] oldArray = ((String[])parameterTable.get(name));
      String[] newArray = new String[oldArray.length + 1];
      System.arraycopy(oldArray,0,newArray,0,oldArray.length);
      newArray[oldArray.length] = value;
      parameterTable.put(name, newArray);
    }
    catch(NullPointerException e) 
    {
      parameterTable.put(name, new String[] {value});
    }
  }


  /**
   * UrlDecodes a piece of the given string.
   * This implementation allows characters which are officialy not
   * allowed in url encoded strings. Things like spaces and characters
   * like @#$ are added as-is to the result.
   *
   * @param firstChar index of the char to start to decoding
   * @param beyondLastChar for the index <EM>after</EM> the char where
   * to stop decoding. This way the caller can call
   * urlDecode(s,0,s.length())
   * @return the string in its url decoded form
   */
  private static String urlDecode(String aString, int firstChar, int beyondLastChar)
  throws IllegalArgumentException 
  {
    // The problem with this initial StringBuffer size is that
    // every "%xx" piece of string will result in only 1 character.
    // For Strings containing many of these this will lead to
    // a 3 times too large intial size.<BR>
    // This could become a problem when uploading large binary
    // files using a POST request.<BR>
    // Setting the initial buffersize to 1/3 the length of the
    // piece of text to decode would lead to a initial buffer size
    // that's too short in 99% of the cases.<BR>
    // *sigh* decisions, decisions.
    StringBuffer result = new StringBuffer(beyondLastChar - firstChar);
    char currentChar;
    for(int i = firstChar; i < beyondLastChar; i++) 
    {
      currentChar = aString.charAt(i);
      if (currentChar == '+') 
      {
	result.append(' ');
      }
      else if (currentChar == '%') 
      {
	try 
	{
	  result.append(new String(new byte
	    [] {(byte)Integer.parseInt(aString.substring(i+1, i+3),16)}));
	}
	catch(StringIndexOutOfBoundsException e) 
	{
	  throw new IllegalArgumentException();
	}
	catch(NumberFormatException f) 
	{
	  throw new IllegalArgumentException();
	}
	i += 2;
      }
      else 
      {
	result.append(currentChar);
      }
    }
    return result.toString();
  }


  /**
   * Reads the data provided by the client using the POST method, 
   * passes these on to HttpUtils.parseQueryString for further treatment,
   * and returns the resulting Hashtable.
   * <p>
   * bonus:<br>
   * When contentLength &lt; 0 it keeps on reading data until EOF<br>
   * throws an IllegalArgumentException when contentLength != amount of data in the inputstream
   *
   * @since Servlet API 1.0
   *
   * @return a Hashtable with String keys, and array of String values.
   * @exception IllegalArgumentException If an IO error occurs or
   * the POST data contains an error it can't handle.
   */
  public static Hashtable parsePostData(int contentLength,ServletInputStream in)
  throws IllegalArgumentException 
  {
    try 
    {
      // the size of contentLength is known
      // try to read contentLength bytes
      if(contentLength >= 0) 
      {
	byte[] buffer = new byte[contentLength];
	int bytesRead = in.read(buffer, 0, buffer.length);
	int totalBytesRead = bytesRead;
	while(bytesRead > 0 && totalBytesRead < contentLength) 
	{
	  bytesRead = in.read(buffer, totalBytesRead,contentLength - totalBytesRead);
	  totalBytesRead += bytesRead;
	}
	// check wether there was to little data
	if(totalBytesRead < contentLength) 
	{
	  throw new IllegalArgumentException("Amount of POST data doesn't match contentLength");
	}
	return parseQueryString(new String(buffer));
      }
      else 
      {
	throw new IllegalArgumentException("Missing or illegal content length in Post request");
      }
    }
    catch (IOException e) 
    {
      throw new IllegalArgumentException("Error reading POST data:" + e.getMessage());
    }
  }


  /**
   * Determines which URL the client used when issuing his request.
   * Does <em>not</em> return the querystring (the ?name=value part)
   *
   * @since Servlet API 1.0
   *
   * @return a URL.
   */
  public static StringBuffer getRequestURL(HttpServletRequest request) 
  {
    StringBuffer result = new StringBuffer("");
    result.append(request.getScheme());
    result.append("://");
    result.append(request.getServerName());
    int serverPort = request.getServerPort();
    if(serverPort != 80) 
    {
      result.append(":");
      result.append(serverPort);
    }
    result.append(request.getContextPath());
    result.append(request.getRequestURI());
    return result;
  }
}
