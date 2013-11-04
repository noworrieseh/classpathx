/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

/**
 * MIME Type Parse Exception.
 */
public class MimeTypeParseException
extends Exception
{

  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create MIME Type parse exception with description.
   * @param value Description
   */
  public MimeTypeParseException(String value)
  {
    super(value);
  } // MimeTypeParseException()

  /**
   * Create MIME Type parse exception.
   */
  public MimeTypeParseException()
  {
    super();
  } // MimeTypeParseException()


} // MimeTypeParseException
