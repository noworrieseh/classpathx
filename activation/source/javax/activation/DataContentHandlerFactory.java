/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.IOException;

/**
 * Data Content Handler Factory.
 */
public abstract interface DataContentHandlerFactory
{

  //-------------------------------------------------------------
  // Interface: DataContentHandlerFactory -----------------------
  //-------------------------------------------------------------

  /**
   * Create data content handler based on MIME type.
   * @param mimeType MIME Type
   * @returns Data content handler
   */
  public abstract DataContentHandler createDataContentHandler(String mimeType);


} // DataContentHandlerFactory
