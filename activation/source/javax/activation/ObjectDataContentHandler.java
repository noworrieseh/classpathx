/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.awt.datatransfer.*;
import java.io.*;

/**
 * Object Data Content Handler.
 */
public class ObjectDataContentHandler
implements DataContentHandler 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Transfer data flavors list.
   */
  private DataFlavor[] transferFlavors;

  /**
   * Object.
   */
  private Object obj;

  /**
   * MIME type of object.
   */
  private String mimeType;

  /**
   * Data content handler.
   */
  private DataContentHandler dhc;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create new object data content handler.
   * @param handler Data content handler
   * @param object Object
   * @param mimetype MIME Type
   */
  public ObjectDataContentHandler(DataContentHandler handler,Object object, String mimetype) 
  {
    dhc = handler;
    obj = object;
    mimeType = mimetype;
  } // ObjectDataContentHandler()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get data content handler.
   * @returns Data content handler
   */
  public DataContentHandler getDCH() 
  {
    return dhc;
  } // getDCH()

  /**
   * Get transfer data flavors.
   * @returns List of transfer data flavors
   */
  public DataFlavor[] getTransferDataFlavors() 
  {
    return transferFlavors;
  } // getTransferDataFlavors()

  /**
   * Get transfer data flavor.
   * @param flavor Data flavor
   * @param source Data source
   * @throws IOException IO exception occurred
   */
  public Object getTransferData(DataFlavor flavor, DataSource source) 
  throws IOException 
  {
    return null; // TODO
  } // getTransferData()

  /**
   * Get content.
   * @param source Data source
   * @returns Object content
   */
  public Object getContent(DataSource source) 
  {
    return null; // TODO
  } // getContent()

  /**
   * Write to.
   * @param object Object to write
   * @param mimeType MIME type of object
   * @param stream Output stream to write to
   * @throws IOException IO exception occurred
   */
  public void writeTo(Object object, String mimeType, OutputStream stream)
  throws IOException 
  {
  } // writeTo()


} // ObjectDataContentHandler
