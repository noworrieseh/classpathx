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
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Externalizable;
import java.io.IOException;

/**
 * Represents a MIME type as per RFC2046.
 *
 * @author Andrew Selkirk
 * @version $Revision: 1.4 $
 */
public class MimeType
implements Externalizable
{

  //-------------------------------------------------------------
  // Constants --------------------------------------------------
  //-------------------------------------------------------------

  //private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";


  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Primary type.
   */
  String primaryType = null;

  /**
   * Sub type
   */
  String subType = null;

  /**
   * MIME Type parameters.
   */
  MimeTypeParameterList parameters = new MimeTypeParameterList();


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create a MIME Type object from a raw MIME Type entry.
   * @param rawdata MIME Type entry
   */
  public MimeType(String rawdata) 
  {
    // TODO: should we be silently failing here?
    try 
    {
      parse(rawdata);
    }
    catch (Exception e) 
    {
    }
  } // MimeType

  /**
   * Create MIME Type object from primary/sub
   * @param primary MIME Type primary
   * @param sub MIME Type sub type
   */
  public MimeType(String primary, String sub) 
  {
    primaryType = primary;
    subType = sub;
  } // MimeType()

  /**
   * Create empty MIME Type
   */
  public MimeType() 
  {
  } // MimeType()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Get a MIME type string encoding.  The format is
   * <i>primary type</i>/<i>subtype</i>; <i>parameters</i>
   * @return canonical representation of the MIME type
   */
  public String toString()
  {

    // Variables
    StringBuffer buffer;

    // Construct Type
    buffer = new StringBuffer();
    buffer.append(getBaseType());
    if (parameters.size() > 0)
    {
      buffer.append("; ");
      buffer.append(parameters.toString());
    } // if

    // Return MIME type encoding
    return buffer.toString();

  } // toString()

  /**
   * Parse a MIME Type entry.
   * @param mimetype MIME Type entry
   * @throws MimeTypeParseException MIME type parsing exception
   */
  private void parse(String mimetype)
  throws MimeTypeParseException 
  {

    // Variables
    int endIndex;
    int typeIndex;
    String type;
    String parameterList;
    String primary;
    String sub;

    // Check for type and parameter separator
    endIndex = mimetype.indexOf(";");
    if (endIndex == -1)
    {
      endIndex = mimetype.length();
    } // if

    // Get Type Index
    type = mimetype.substring(0, endIndex);
    typeIndex = type.indexOf("/");

    // Get the Primary and Sub parts of the MIME type
    primary = type.substring(0, typeIndex);
    sub = type.substring(typeIndex + 1, endIndex);

    // Set the Primary and Sub Types of the object
    setPrimaryType(primary);
    setSubType(sub);

    // Set Parameter List
    parameterList = mimetype.substring(endIndex);
    parameters = new MimeTypeParameterList(parameterList);

  } // parse()

  /**
   * Get the Base of the MIME type.  Consists of the format:
   * <i>primary type</i>/<i>subtype</i>
   * @return Base type
   */
  public String getBaseType() 
  {
    return getPrimaryType() + "/" + getSubType();
  } // getBaseType()

  /**
   * Get the value of a MIME type parameter.
   * @param name Parameter name
   * @return Parameter value, or null
   */
  public String getParameter(String name) 
  {
    return parameters.get(name);
  } // getParameter()

  /**
   * Get the parameter collection
   * @return Parameter object
   */
  public MimeTypeParameterList getParameters() 
  {
    return parameters;
  } // getParameters()

  /**
   * Remove the specified parameter from the MIME type
   * @param name Parameter name
   */
  public void removeParameter(String name)
  {
    parameters.remove(name);
  } // removeParameter()

  /**
   * Set a MIME type parameter.
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, String value)
  {
    parameters.set(name, value);
  } // setParameter()

  /**
   * Get the primary type of the MIME type
   * @return Primary type
   */
  public String getPrimaryType()
  {
    return primaryType;
  } // getPrimaryType()

  /**
   * Get the sub type of the MIME type
   * @return Sub type
   */
  public String getSubType()
  {
    return subType;
  } // getSubType()

  /**
   * Set the primary type of the MIME type.
   * @param primary Primary type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setPrimaryType(String primary)
  throws MimeTypeParseException
  {
    // TODO: Why does this method declare an exception?
    primaryType = primary;
  } // setPrimaryType()

  /**
   * Set the sub type of the MIME type.
   * @param sub Sub type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setSubType(String sub)
  throws MimeTypeParseException
  {
    // TODO: Why does this method declare an exception?
    subType = sub;
  } // setSubType()

  /**
   * Determine if the raw MIME Type entry matches this MIME Type.
   * @param rawdata MIME type entry to check
   * @return true if it matches, false otherwise
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public boolean match(String rawdata)
  throws MimeTypeParseException
  {

    // Variables
    MimeType mimeType;

    // Construct a Mime Type from the raw data
    mimeType = new MimeType(rawdata);

    // Match the Mime Type to check equality
    return match(mimeType);

  } // match()

  /**
   * Determine if the MIME Type matches this MIME Type.
   * @param type MIME type to check
   * @return true if matches, false otherwise
   */
  public boolean match(MimeType type)
  {
    // TODO: should the parameters be checked as well?

    // Check the Primary and Sub types
    return (getPrimaryType().equals(type.getPrimaryType())
             && getSubType().equals(type.getSubType()));

  } // match()


  //-------------------------------------------------------------
  // Interface: Externalizable ----------------------------------
  //-------------------------------------------------------------

  /**
   * Read external.  Part of serialization.
   * @param input Object input
   * @throws IOException IOException occurred
   * @throws ClassNotFoundException ClassNotFoundException occurred
   */
  public void readExternal(ObjectInput input)
  throws IOException, ClassNotFoundException
  {
    try
    {
      parse((String) input.readObject());
    }
    catch (MimeTypeParseException e)
    {
    }
  } // readExternal()

  /**
   * Write external.  Part of serialization.
   * @param output Object output
   * @throws IOException IO exception occurred
   */
  public void writeExternal(ObjectOutput output)
  throws IOException 
  {
    output.writeObject(toString());
  } // writeExternal()


} // MimeType
