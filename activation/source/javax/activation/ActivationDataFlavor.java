/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.awt.datatransfer.DataFlavor;

/**
 * Activation Data Flavor.
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
    this.representationClass = representationClass;
    this.mimeType = mimeType;
    this.humanPresentableName = humanPresentableName;
  } // ActivationDataFlavor()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Test data flavor for equivalence.
   * @param dataFlavor Data flavor to test
   * @returns true if equals, otherwise false
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
   * @returns Name
   */
  public String getHumanPresentableName() 
  {
    return humanPresentableName;
  } // getHumanPresentableName()

  /**
   * Get MIME Type.
   * @returns MIME Type
   */
  public String getMimeType() 
  {
    return mimeType;
  } // getMimeType()

  /**
   * Get representation class.
   * @returns Representation class
   */
  public Class getRepresentationClass() 
  {
    return representationClass;
  } // getRepresentationClass()

  /**
   * Determine if MIME Type is equals to data flavor.
   * @param mimeType MIME Type to test
   * @returns true if equal, false otherwise
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
   * @returns Normalized MIME Type
   */
  protected String normalizeMimeType(String mimeType) 
  {
    return null; // TODO
  } // normalizeMimeType()

  /**
   * Normalize MIME type parameter.
   * @param parameterName Parameter name to normalize
   * @param parameterValue Parameter value to normalize
   * @returns Normalized parameter
   */
  protected String normalizeMimeTypeParameter(String parameterName, 
					      String parameterValue) 
  {
    return null; // TODO
  } // normalizeMimeTypeParameter()

  /**
   * Set the human presentable name.
   * @param humanPresentableName Name
   */
  public void setHumanPresentableName(String humanPresentableName) 
  {
    this.humanPresentableName = humanPresentableName;
  } // setHumanPresentationName()


} // ActivationDataFlavor

