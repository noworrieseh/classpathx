/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 Andrew Selkirk

  For more information on the classpathx please mail:
  nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.activation;

// Imports
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * MIME Type Parameter List.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class MimeTypeParameterList 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Parameter mapping.
   */
  private Hashtable parameters = new Hashtable();


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create MIME type parameters list from string.
   * @param parameterList String to parse
   */
  public MimeTypeParameterList(String parameterList)
  throws MimeTypeParseException 
  {
    try 
    {
      parse(parameterList);
    } catch (Exception e) 
    {
    }
  } // MimeTypeParameterList()

  /**
   * Create empty parameter list.
   */
  public MimeTypeParameterList() 
  {
  } // MimeTypeParameterList()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Return string representation of parameter list.
   * @return String value
   */
  public String toString() 
  {

    // Variables
    Enumeration enum;
    StringBuffer buffer;
    String name;
    String value;
 
    // Get Names
    enum = getNames();
 
    // Process Each Name
    buffer = new StringBuffer();
    while (enum.hasMoreElements() == true) 
    {
 
      // Get Parameter
      name = (String) enum.nextElement();
      value = get(name);
 
      // Append to Result
      buffer.append(name);
      buffer.append("=");
      buffer.append(quote(value));
 
      // Check for Seperator
      if (enum.hasMoreElements() == true) 
      {
        buffer.append("; ");
      }
 
    } // while
 
    // Return Result
    return buffer.toString();
 
  } // toString()

  /**
   * Get value of name.
   * @param name Parameter name
   * @return Parameter value, or null
   */
  public String get(String name) 
  {
    return (String) parameters.get(name);
  } // get()

  /**
   * Set value of name.
   * @param name Parameter name
   * @param value Parameter value
   */
  public void set(String name, String value) 
  {
    parameters.put(name, value);
  } // set()

  /**
   * Get size of parameter list.
   * @return Number of parameters
   */
  public int size() 
  {
    return parameters.size();
  } // size()

  /**
   * Remove parameter.
   * @param name Name of parameter
   */
  public void remove(String name) 
  {
    parameters.remove(name);
  } // remove()

  /**
   * Check if parameter list is empty.
   * @return true if empty, false otherwise
   */
  public boolean isEmpty() 
  {
    if (parameters.size() == 0) 
    {
      return true;
    }
    return false;
  } // isEmpty()

  /**
   * Parse parameter list.
   * @param parameterList Parameter list to parse
   * @throws MimeTypeParseException Parsing exception occurred
   */
  protected void parse(String parameterList)
  throws MimeTypeParseException 
  {
 
    // Variables
    StringTokenizer tokens;
    String parameter;
    String name;
    String value;
    int index;
 
    // Create Tokenizer
    tokens = new StringTokenizer(parameterList, ";");
 
    // Load Each Parameter
    while (tokens.hasMoreTokens() == true) 
    {
      parameter = tokens.nextToken();
      index = parameter.indexOf("=");
      if (index != parameter.lastIndexOf("=")) 
      {
        throw new MimeTypeParseException("multiple =");
      }
      name = parameter.substring(0, index).trim();
      value = (parameter.substring(index + 1).trim());
 
      // Check name characters
      for (index = 0; index < name.length(); index++) 
      {
        if (isTokenChar(name.charAt(index)) == false)
        {
          throw new MimeTypeParseException("invalid character");
        }
      } // for: index
 
      // Check value characters
      for (index = 0; index < value.length(); index++) 
      {
        if (isTokenChar(value.charAt(index)) == false)
        {
          throw new MimeTypeParseException("invalid character");
        }
      } // for: index
 
      // Add to Parameter List
      set(name, value);
 
    } // while())
 
  } // parse()

  /**
   * Get enumeration of parameter names.
   * @return Name enumeration
   */
  public Enumeration getNames() 
  {
    return parameters.keys();
  } // getNames()

  /**
   * Check if token is character.
   * @param token Token to check
   * @return true if character, false otherwise
   */
  private static boolean isTokenChar(char token) 
  {
    if (token > 32 && token < 127) 
    {
      if (token == '(' || token == ')' || token == '<' ||
          token == '<' || token == '@' || token == ',' ||
          token == ';' || token == ':' || token == '\\' ||
          token == '"' || token == '/' || token == '[' ||
          token == ']' || token == '?' || token == '=')
      {
        return false;
      }
      return true;
    }
    return false;
  } // isTokenChar()

  /**
   * Quote a string.
   * @param value String to quote
   * @return Quoted string
   */
  private static String quote(String value) 
  {

    // Add quotes around the value
    return "\"" + value + "\"";

  } // quote()

  /**
   * Remove quotes from string.
   * @param value Quoted string
   * @return Unquoted string
   */
  private static String unquote(String value) 
  {

    // Check for starting quote
    if (value.startsWith("\"") == true)
    {
      value = value.substring(1);
    }

    // Check for ending quote
    if (value.endsWith("\"") == true)
    {
      value = value.substring(0, value.length() - 1);
    }

    // Return Unquoted string
    return value;

  } // unquote()


} // MimeTypeParameterList
