/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.IOException;

/**
 * Command Object.
 */
public abstract interface CommandObject
{

  /**
   * Set command context of object.
   * @param verb Command name
   * @param handler Data handler
   */
  public abstract void setCommandContext(String verb, DataHandler handler)
  throws IOException;


} // CommandObject
