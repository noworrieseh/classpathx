/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.activation;

// Imports
import java.io.*;
import java.awt.datatransfer.*;
import java.net.URL;

/**
 * Data Handler.
 */
public class DataHandler implements Transferable 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  private DataSource dataSource = null;
  private DataSource objDataSource = null;
  private Object object = null;
  private String objectMimeType = null;
  private CommandMap currentCommandMap = null;
  private static final DataFlavor[] emptyFlavors = null;
  private DataFlavor[] transferFlavors = null;
  private DataContentHandler dataContentHandler = null;
  private DataContentHandler factoryDCH = null;
  private static DataContentHandlerFactory factory = null;
  private DataContentHandlerFactory oldFactory = null;
  private String shortType = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create data handler based on data source.
   * @param source Data source
   */
  public DataHandler(DataSource source) 
  {
    dataSource = source;
    dataContentHandler = new DataSourceDataContentHandler(null, source);
    currentCommandMap = CommandMap.getDefaultCommandMap();
  } // DataHandler()

  /**
   * Create data handler based on object and MIME type.
   * @param object Object
   * @param mimeType MIME type
   */
  public DataHandler(Object object, String mimeType) 
  {
    this.object = object;
    this.objectMimeType = mimeType;
    dataContentHandler = new ObjectDataContentHandler(null, object, mimeType);
    currentCommandMap = CommandMap.getDefaultCommandMap();
  } // DataHandler()

  /**
   * Create data handler based on URL.
   * @param url URL reference
   */
  public DataHandler(URL url) 
  {
    this(new URLDataSource(url));
  } // DataHandler()


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @returns Data handler name
   */
  public String getName() 
  {
    if (dataSource != null) 
    {
      return dataSource.getName();
    }
    return null;
  } // getName()

  /**
   * Get content.
   * @returns Content object
   * @throws IOException IO exception occurred
   */
  public Object getContent() throws IOException 
  {

    // Variables
    DataContentHandler handler;

    if (dataSource == null) 
    {
      return object;
    } else 
    {
      // handler = getCommandMap().createDataContentHandler(getContentType());
      handler = getDataContentHandler();
      if (handler != null) 
      {
	return handler.getContent(dataSource);
      } else 
      {
	return dataSource.getInputStream();
      }
    }
  } // getContent()

  /**
   * Get input stream.
   * @returns Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream() throws IOException 
  {

    // Variables
    DataContentHandler handler;
    PipedInputStream input;
    PipedOutputStream output;
    Thread writeThread;

    if (dataSource != null) 
    {
      return dataSource.getInputStream();
    } else 
    {

      // Get Data Content Handler
      handler = getCommandMap().createDataContentHandler(objectMimeType);

      // Check Handler
      if (handler != null) 
      {
	input = new PipedInputStream();
	output = new PipedOutputStream(input);
	input.connect(output);
	try 
	{
	  handler.writeTo(object, objectMimeType, output);
	} catch (Exception e) 
	{
	}
	return input;
      } else 
      {
	throw new UnsupportedDataTypeException();
      } // if: handler

    } // if: dataSource

  } // getInputStream()

  /**
   * Write to output stream.
   * @param stream Output stream 
   * @throws IOException IO exception occurred
   */
  public void writeTo(OutputStream stream) throws IOException 
  {

    // Variables
    InputStream input;
    int data;

    // Check For Data Source
    if (dataSource != null) 
    {

      // Get Input Stream
      input = getInputStream();

      while ((data = input.read()) != -1) 
      {
	stream.write(data);
      }

    } // if: dataSource

    // TODO: Object case

  } // writeTo()

  /**
   * Get content type of data handler.
   * @returns Content type
   */
  public String getContentType() 
  {
    if (dataSource != null) 
    {
      return dataSource.getContentType();
    }
    return objectMimeType;
  } // getContentType()

  /**
   * Get output stream.
   * @returns Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream() throws IOException 
  {

    if (dataSource != null) 
    {
      return dataSource.getOutputStream();
    }
    return null;
  } // getOutputStream()

  /**
   * Get all commands.
   * @returns Command list
   */
  public CommandInfo[] getAllCommands() 
  {
    return getCommandMap().getAllCommands(getContentType());
  } // getAllCommands()

  /**
   * Get base type.
   * @returns Base type
   */
  private synchronized String getBaseType() 
  {
    return null; // TODO
  } // getBaseType()

  /**
   * Get beans of command.
   * @returns Instantiated command bean, or null
   */
  public Object getBean(CommandInfo commandInfo) 
  {
    try 
    {
      return commandInfo.getCommandObject(this, 
					  getClass().getClassLoader());
    } catch (Exception e) 
    {
      return null;
    }
  } // getBean()

  /**
   * Get command information based on command verb.
   * @param command Command verb
   * @returns Command information
   */
  public CommandInfo getCommand(String command) 
  {
    return getCommandMap().getCommand(getContentType(), command);
  } // getCommand()

  /**
   * Get command map.
   * @returns Command map
   */
  private synchronized CommandMap getCommandMap() 
  {
    return currentCommandMap;
  } // getCommandMap()

  /**
   * Get data content handler.
   * @returns Data content handler
   */
  private synchronized DataContentHandler getDataContentHandler() 
  {
    if (factory != null) 
    {
      dataContentHandler = factory.createDataContentHandler(getContentType());
    }
    return dataContentHandler; 
  } // getDataContentHandler()

  /**
   * Get data source.
   * @returns Data source, or null
   */
  public DataSource getDataSource() 
  {
    return dataSource;
  } // getDataSource()

  /**
   * Get list of preferred commands.
   * @returns List of preferred commands
   */
  public CommandInfo[] getPreferredCommands() 
  {
    return getCommandMap().getPreferredCommands(getContentType());
  } // getPreferredCommands()

  /**
   * Get transfer data based on data flavor.
   * @param dataFlavor Data flavor
   * @returns Transfer data
   * @throws UnsupportedFlavorException Unsupported data flavor
   * @throws IOException IO exception occurred
   */
  public Object getTransferData(DataFlavor dataFlavor)
  throws UnsupportedFlavorException, IOException 
  {
    // Needs testing...
    // Variables
    DataContentHandler handler;
    try 
    {

      if (dataSource != null) 
      {
	handler = getCommandMap().createDataContentHandler(getContentType());
	if (handler != null) 
	{
	  return handler.getTransferData(dataFlavor, dataSource);
	} else if (dataFlavor.getMimeType().equals(getContentType()) == true &&
		   dataFlavor.getRepresentationClass().equals(Class.forName("java.io.InputStream")) == true) 
	{
	  return dataSource.getInputStream();
	} else 
	{
	  throw new UnsupportedDataTypeException();
	}
      } else 
      {
	if (dataFlavor.getMimeType().equals(getContentType()) == true &&
	    dataFlavor.getRepresentationClass().equals(object.getClass()) == true) 
	{
	  return object;
	} else 
	{
	  throw new UnsupportedDataTypeException();
	}
      }
    } catch (Exception e) 
    {
      return null;
    }
  } // getTransferData()

  /**
   * Get transfer data flavors.
   * @returns List of data flavors
   */
  public synchronized DataFlavor[] getTransferDataFlavors() 
  {

    // Variables
    DataContentHandler handler;

    // Get Data Content Handler
    handler = getDataContentHandler();

    // Get Transfer Data Flavors
    if (handler != null) 
    {
      return handler.getTransferDataFlavors();
    }
    return null;
  } // getTransferDataFlavors()

  /**
   * Determine if data flavor is supported.
   * @param dataFlavor Data flavor
   * @returns true if supported, false otherwise
   */
  public boolean isDataFlavorSupported(DataFlavor dataFlavor) 
  {

    // Variables
    int index;
    DataFlavor[] list;
    DataFlavor element;

    // Get List of Data Flavors
    list = getTransferDataFlavors();

    // Loop through List
    for (index = 0; index < list.length; index++) 
    {
      element = list[index];
      if (element.equals(dataFlavor) == true) 
      {
	return true;
      }
    }

    // Unable to locate Flavor, return false
    return false;

  } // isDataFlavorSupported()

  /**
   * Set command map.  If null, command map is reset to default.
   * @param map Command map
   */
  public synchronized void setCommandMap(CommandMap map) 
  {
    if (map == null) 
    {
      currentCommandMap = CommandMap.getDefaultCommandMap();
    } else 
    {
      currentCommandMap = map;
    }
  } // setCommandMap()

  /**
   * Set the data content factory for data handler.  Note that this
   * is a one time call.  Further calls result in an Error.
   * @param newFactory New factory
   */
  public static synchronized void setDataContentHandlerFactory(DataContentHandlerFactory newFactory) 
  {

    // Check For existing Factory
    if (factory != null) 
    {
      throw new Error();
    }

    // Set Factory
    factory = newFactory;

  } // setDataContentHandlerFactory()


} // DataHandler

/**
 * Experiment data writer.  I'm going to do away with this.  Data Handler
 * actually uses this in writeTo as an anonymous inner class.  To stay
 * consistent with Sun's implementation, so will this be.
 */
class DataHandlerWriter implements Runnable 
{

  private final PipedOutputStream pos;
  private final DataHandler dh;
  private final DataContentHandler fdch;

  DataHandlerWriter(DataContentHandler contentHandler, PipedOutputStream stream, DataHandler handler) 
  {
    fdch = contentHandler;
    pos = stream;
    dh = handler;
  } // DataHandler$1()

  public void run() 
  {
    try 
    {
      // handler.writeTo(object, objectMimeType, pos);
    } catch (Exception e) 
    {
    }

  } // run()


} // DataHandler$1
