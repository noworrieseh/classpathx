/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 Andrew Selkirk

  For more information on the classpathx please mail: nferrier@tapsellferrier.co.uk

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


import java.io.*;


/** represents a MIME type as per RFC2046.
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 */
public class MimeType
implements Externalizable
{

  /** primary type.
   */
  String primaryType = null;

  /** sub type
   */
  String subType = null;

  /** MIME Type parameters.
   */
  MimeTypeParameterList parameters = new MimeTypeParameterList();

  private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";


  /** create MIME Type object from MIME Type entry.
   *
   * @param rawdata MIME Type entry
   */
  public MimeType(String rawdata) 
  {
    try 
    {
      parse(rawdata);
    }
    catch (Exception e) 
    {
    }
  }

  /**
   * Create MIME Type object from primary/sub
   * @param primary MIME Type primary
   * @param sub MIME Type sub type
   */
  public MimeType(String primary, String sub) 
  {
    primaryType = primary;
    subType = sub;
  }

  /**
   * Create empty MIME Type
   */
  public MimeType() 
  {
  }

  /** @return canonical representation of the MIME type
   *   this is usually: <i>primary type</i>/<i>subtype</i>; <i>parameters</i>
   */
  public String toString() 
  {
    StringBuffer buffer;
    // Construct Type
    buffer = new StringBuffer();
    buffer.append(getBaseType());
    if (parameters.size() > 0) 
    {
      buffer.append("; ");
      buffer.append(parameters.toString());
    }
    return buffer.toString();
  }

  /** parse MIME Type entry.
   *
   * @param mimetype MIME Type entry
   * @throws MimeTypeParseException MIME type parsing exception
   */
  private void parse(String mimetype)
  throws MimeTypeParseException 
  {
    int endIndex;
    int typeIndex;
    String type;
    String parameterList;
    String primary;
    String sub;
    //check for type and parameter separator
    endIndex = mimetype.indexOf(";");
    if (endIndex == -1) 
    endIndex = mimetype.length();
    //get Type Index
    type = mimetype.substring(0, endIndex);
    typeIndex = type.indexOf("/");
    //get Primary and Sub
    primary = type.substring(0, typeIndex);
    sub = type.substring(typeIndex + 1, endIndex);
    //set Primary and Sub Types
    setPrimaryType(primary);
    setSubType(sub);
    // Set Parameter List
    parameterList = mimetype.substring(endIndex);
    parameters = new MimeTypeParameterList(parameterList);
  }

  /** @return Base type
   */
  public String getBaseType() 
  {
    return getPrimaryType() + "/" + getSubType();
  }

  /** get value of MIME type parameter.
   *
   * @param name Parameter name
   * @return Parameter value, or null
   */
  public String getParameter(String name) 
  {
    return parameters.get(name);
  }

  /** @return Parameter object
   */
  public MimeTypeParameterList getParameters() 
  {
    return parameters;
  }

  /** remove the specified parameter.
   *
   * @param name Parameter name
   */
  public void removeParameter(String name) 
  {
    parameters.remove(name);
  }

  /** set the MIME type parameter.
   *
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, String value) 
  {
    parameters.set(name, value);
  }

  /** @return Primary type
   */
  public String getPrimaryType() 
  {
    return primaryType;
  }

  /** @return Sub type
   */
  public String getSubType() 
  {
    return subType;
  }

  /** set the primary type of MIME type.
   *
   * @param primary Primary type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setPrimaryType(String primary)
  throws MimeTypeParseException 
  {
    primaryType = primary;
  }

  /** set the sub type of MIME type.
   *
   * @param sub Sub type
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public void setSubType(String sub)
  throws MimeTypeParseException 
  {
    subType = sub;
  }

  /** determine if raw MIME Type entry matches this MIME Type.
   *
   * @param rawdata MIME type entry
   * @return true if matches, false otherwise
   * @throws MimeTypeParseException Parsing exception occurred
   */
  public boolean match(String rawdata)
  throws MimeTypeParseException 
  {
    MimeType mimeType;
    // Construct Mime Type
    mimeType = new MimeType(rawdata);
    // Match Mime Type
    return match(mimeType);
  }

  /** determine if MIME Type matches this MIME Type.
   *
   * @param type MIME type
   * @return true if matches, false otherwise
   */
  public boolean match(MimeType type) 
  {
    return (getPrimaryType().equals(type.getPrimaryType())
	    && getSubType().equals(type.getSubType()));
  }


  //externalizable implementation

  /** Read external.  Part of serialization.
   *
   * @param input Object input
   */
  public void readExternal(ObjectInput input)
  throws IOException, ClassNotFoundException 
  {
    try 
    {
      parse((String)input.readObject());
    }
    catch (MimeTypeParseException e) 
    {
    }
  }

  /** write external.  Part of serialization.
   *
   * @param output Object output
   * @throws IOException IO exception occurred
   */
  public void writeExternal(ObjectOutput output)
  throws IOException 
  {
    output.writeObject(toString());
  }

}
