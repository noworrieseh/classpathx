/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.IOException;

/**
 * Unsupported Data Type Exception.
 */
public class UnsupportedDataTypeException extends IOException 
{

  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new unsupported data type exception with description.
   * @param value Description
   */
  public UnsupportedDataTypeException(String value) 
  {
    super(value);
  } // UnsupportedDataTypeException()

  /**
   * Create new unsupported data type exception.
   */
  public UnsupportedDataTypeException() 
  {
    super();
  } // UnsupportedDataTypeException()


} // UnsupportedDataTypeException
