/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;

/**
 * MIME Type.
 */
public class MimeType
implements Externalizable
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Primary type.
   */
  private String primaryType = null;

  /**
   * Sub type
   */
  private String subType = null;

  /**
   * MIME Type parameters.
   */
  private MimeTypeParameterList parameters = new MimeTypeParameterList();

  private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create MIME Type object from MIME Type entry
   * @param rawdata MIME Type entry
   */
  public MimeType(String rawdata) 
  {
    try 
    {
      parse(rawdata);
    } catch (Exception e) 
    {
    }
  } // MimeType()

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
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get string representation of MIME Type.
   * @returns String representation
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
    }

    // Return String
    return buffer.toString();

  } // toString()

  /**
   * Parse MIME Type entry.
   * @param mimetype MIME Type entry
   * @throws MimeTypeParseException MIME type parsing exception
   */
  private void parse(String mimetype) throws MimeTypeParseException 
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
    }

    // Get Type Index
    type = mimetype.substring(0, endIndex);
    typeIndex = type.indexOf("/");

    // Get Primary and Sub
    primary = type.substring(0, typeIndex);
    sub = type.substring(typeIndex + 1, endIndex);

    // Set Primary and Sub Types
    setPrimaryType(primary);
    setSubType(sub);

    // Set Parameter List
    parameterList = mimetype.substring(endIndex);
    parameters = new MimeTypeParameterList(parameterList);

  } // parse()

  /**
   * Get base type of MIME Type.
   * @returns Base type
   */
  public String getBaseType() 
  {
    return getPrimaryType() + "/" + getSubType();
  } // getBaseType()

  /**
   * Get value of MIME type parameter.
   * @param name Parameter name
   * @returns Parameter value, or null
   */
  public String getParameter(String name) 
  {
    return parameters.get(name);
  } // getParameter()

  /**
   * Get parameters object.
   * @returns Parameter object
   */
  public MimeTypeParameterList getParameters() 
  {
    return parameters;
  } // getParameters()

  /**
   * Get primary of MIME Type.
   * @returns Primary type
   */
  public String getPrimaryType() 
  {
    return primaryType;
  } // getPrimaryType()

  /**
   * Get sub of MIME Type.
   * @returns Sub type
   */
  public String getSubType() 
  {
    return subType;
  } // getSubType()

  /**
   * Determine if token is character.
   * @param token Character token
   * @returns true if valid, false otherwise
   */
  private static boolean isTokenChar(char token) 
  {
    return false; // TODO
  } // isTokenChar()

  /**
   * Determine if token is valid
   * @param token Token
   * @returns true if valid, false otherwise
   */
  private boolean isValidToken(String token) 
  {
    return false; // TODO
  } // isValidToken(0

  /**
   * Determine if raw MIME Type entry matches this MIME Type.
   * @param rawdata MIME type entry
   * @returns true if matches, false otherwise
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public boolean match(String rawdata) throws MimeTypeParseException 
  {

    // Variables
    MimeType mimeType;

    // Construct Mime Type
    mimeType = new MimeType(rawdata);

    // Match Mime Type
    return match(mimeType);

  } // match()

  /**
   * Determine if MIME Type matches this MIME Type.
   * @param type MIME type
   * @returns true if matches, false otherwise
   */
  public boolean match(MimeType type) 
  {
    if (getPrimaryType().equals(type.getPrimaryType()) == true &&
	getSubType().equals(type.getSubType()) == true) 
    {
      return true;
    }
    return false;
  } // match()

  /**
   * Read external.  Part of serialization.
   * @param input Object input
   */
  public void readExternal(ObjectInput input)
  throws IOException, ClassNotFoundException 
  {
    try 
    {
      parse((String) input.readObject());
    } catch (MimeTypeParseException e) 
    {
    }
  } // readExternal()

  /**
   * Remove parameter.
   * @param name Parameter name
   */
  public void removeParameter(String name) 
  {
    parameters.remove(name);
  } // removeParameter()

  /**
   * Set MIME type parameter.
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, String value) 
  {
    parameters.set(name, value);
  } // setParameter()

  /**
   * Set primary type of MIME type.
   * @param primary Primary type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setPrimaryType(String primary)
  throws MimeTypeParseException 
  {

    // Check Type
    //TODO

    // Set Primary Type
    primaryType = primary;

  } // setPrimaryType()

  /**
   * Set sub type of MIME type.
   * @param sub Sub type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setSubType(String sub)
  throws MimeTypeParseException 
  {

    // Check SubType
    // TODO

    // Set Sub Type
    subType = sub;

  } // getSubType()

  /**
   * Write external.  Part of serialization.
   * @param output Object output
   * @throws IOException IO exception occurred
   */
  public void writeExternal(ObjectOutput output) throws IOException 
  {
    output.writeObject(toString());
  } // writeExternal()


} // MimeType
