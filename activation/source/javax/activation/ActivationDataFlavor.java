/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk

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
import java.awt.datatransfer.DataFlavor;

/**
 * Activation Data Flavor.
 * @author Andrew Selkirk
 * @version $Revision: 1.4 $
 */
public class ActivationDataFlavor
extends DataFlavor
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Data flavor MIME Type.
   */
  private String mimeType = null;

  /**
   * Data flavor MIME type object.
   */
  private MimeType mimeObject = null;

  /**
   * Human presentable name of data flavor.
   */
  private String humanPresentableName = null;

  /**
   * Representation class of data flavor.
   */
  private Class representationClass = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create Activation Data Flavor.
   * @param representationClass Class representing flavor
   * @param humanPresentableName Readable name
   */
  public ActivationDataFlavor(Class representationClass, 
			      String humanPresentableName) 
  {
    this(representationClass, null, humanPresentableName);
  } // ActivationDataFlavor()

  /**
   * Create Activation Data Flavor.
   * @param mimeType MIME Type
   * @param humanPresentableName Readable name
   */
  public ActivationDataFlavor(String mimeType, 
			      String humanPresentableName) 
  {
    this(null, mimeType, humanPresentableName);
  } // ActivationDataFlavor()

  /**
   * Create Activation Data Flavor.
   * @param representationClass Class representing flavor
   * @param mimeType MIME Type
   * @param humanPresentableName Readable name
   */
  public ActivationDataFlavor(Class representationClass, 
			      String mimeType, 
			      String humanPresentableName) 
  {
    /* In JDK1.1 we must use the DataFlavor(Class, String) constructor */
    super(representationClass, humanPresentableName);
    this.mimeType = mimeType;
  } // ActivationDataFlavor()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Test data flavor for equivalence.
   * @param dataFlavor Data flavor to test
   * @return true if equals, otherwise false
   */
  public boolean equals(DataFlavor dataFlavor) 
  {

    // Variables
    ActivationDataFlavor flavor;

    if (dataFlavor instanceof ActivationDataFlavor) 
    {
      flavor = (ActivationDataFlavor) dataFlavor;
    }

    return false;

  } // equals()

  /**
   * Get human presentable name
   * @return Name
   */
  public String getHumanPresentableName() 
  {
    return humanPresentableName;
  } // getHumanPresentableName()

  /**
   * Set the human presentable name.
   * @param humanPresentableName Name
   */
  public void setHumanPresentableName(String humanPresentableName)
  {
    this.humanPresentableName = humanPresentableName;
  } // setHumanPresentationName()

  /**
   * Get MIME Type.
   * @return MIME Type
   */
  public String getMimeType() 
  {
    return mimeType;
  } // getMimeType()

  /**
   * Get representation class.
   * @return Representation class
   */
  public Class getRepresentationClass() 
  {
    return representationClass;
  } // getRepresentationClass()

  /**
   * Determine if MIME Type is equals to data flavor.
   * @param mimeType MIME Type to test
   * @return true if equal, false otherwise
   */
  public boolean isMimeTypeEqual(String mimeType) 
  {
    if (this.mimeType.equals(mimeType) == true) 
    {
      return true;
    } else 
    {
      return false;
    }
  } // isMimeTypeEqual()

  /**
   * Normalize MIME Type.
   * @param mimeType MIME Type to normalize
   * @return Normalized MIME Type
   */
  protected String normalizeMimeType(String mimeType) 
  {
    return null; // TODO
  } // normalizeMimeType()

  /**
   * Normalize MIME type parameter.
   * @param parameterName Parameter name to normalize
   * @param parameterValue Parameter value to normalize
   * @return Normalized parameter
   */
  protected String normalizeMimeTypeParameter(String parameterName, 
					      String parameterValue) 
  {
    return null; // TODO
  } // normalizeMimeTypeParameter()


} // ActivationDataFlavor
