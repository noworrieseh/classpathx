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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.net.URL;

/**
 * Data Handler.
 * @author Andrew Selkirk
 * @version $Revision: 1.6 $
 */
public class DataHandler implements Transferable 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * TODO
   */
  private DataSource dataSource;

  /**
   * TODO
   */
  private DataSource objDataSource;

  /**
   * TODO
   */
  private Object object;

  /**
   * TODO
   */
  private String objectMimeType;

  /**
   * TODO
   */
  private CommandMap currentCommandMap;

  /**
   * TODO
   */
  private static final DataFlavor[] emptyFlavors = new DataFlavor[0];

  /**
   * TODO
   */
  private DataFlavor[] transferFlavors = emptyFlavors;

  /**
   * TODO
   */
  private DataContentHandler dataContentHandler;

  /**
   * TODO
   */
  private DataContentHandler factoryDCH;

  /**
   * TODO
   */
  private static DataContentHandlerFactory factory;

  /**
   * TODO
   */
  private DataContentHandlerFactory oldFactory = factory;

  /**
   * TODO
   */
  private String shortType;


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
  }

  /**
   * Create data handler based on object and MIME type.
   * @param object Object
   * @param mimeType MIME type
   */
  public DataHandler(Object object, String mimeType) 
  {
    this.object = object;
    this.objectMimeType = mimeType;
  }

  /**
   * Create data handler based on URL.
   * @param url URL reference
   */
  public DataHandler(URL url) 
  {
    this(new URLDataSource(url));
  }


  //-------------------------------------------------------------
  // Public Accessor Methods ------------------------------------
  //-------------------------------------------------------------

  /**
   * Get name.
   * @return Data handler name
   */
  public String getName() 
  {
    if (dataSource!=null) 
      return dataSource.getName();
    else
      return null;
  }

  /**
   * Get content.
   * @return Content object
   * @throws IOException IO exception occurred
   */
  public Object getContent()
    throws IOException 
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
   * @return Input stream
   * @throws IOException IO exception occurred
   */
  public InputStream getInputStream()
    throws IOException 
  {
    if (dataSource!=null)
      return dataSource.getInputStream();
    else
    {
      // Get Data Content Handler
      DataContentHandler handler = getDataContentHandler();
      // Check Handler
      if (handler==null)
        throw new UnsupportedDataTypeException("No data content handler for "+
            "MIME content type: "+getBaseType());
      if (handler instanceof ObjectDataContentHandler)
      {
        if (((ObjectDataContentHandler)handler).getDCH()==null)
          throw new UnsupportedDataTypeException("No object data content "+
              "handler for MIME content type: "+getBaseType());
      }
      PipedOutputStream out = new PipedOutputStream();
      PipedInputStream in = new PipedInputStream(out);
      InputStreamWriter writer = this.new InputStreamWriter(handler, in, out);
      Thread writerThread = new Thread(writer);
      writerThread.setName("DataHandler.getInputStream");
      writerThread.start();
      return in;
    }
  }

  class InputStreamWriter
    implements Runnable
  {

    DataContentHandler handler;
    PipedInputStream in;
    PipedOutputStream out;

    InputStreamWriter(DataContentHandler handler, 
        PipedInputStream in,
        PipedOutputStream out)
    {
      this.handler = handler;
      this.in = in;
      this.out = out;
    }

    public void run()
    {
      try
      {
        handler.writeTo(object, objectMimeType, out);
      }
      catch (IOException e)
      {
        try
        {
          out.close();
        }
        catch (IOException e2)
        {
        }
      }
    }
  }

  /**
   * Write to output stream.
   * @param stream Output stream 
   * @throws IOException IO exception occurred
   */
  public void writeTo(OutputStream stream)
    throws IOException 
  {
    // Check For Data Source
    if (dataSource!=null) 
    {
      // Get Input Stream
      InputStream in = dataSource.getInputStream();
      int max = 16384; // TODO make configurable
      byte[] bytes = new byte[max];
      int len;
      while ((len = in.read(bytes)) > 0)
        stream.write(bytes, 0, len);
      in.close();
    }
    else
    {
      DataContentHandler handler = getDataContentHandler();
      handler.writeTo(object, objectMimeType, stream);
    }
  }

  /**
   * Get content type of data handler.
   * @return Content type
   */
  public String getContentType() 
  {
    if (dataSource!=null)
      return dataSource.getContentType();
    else
      return objectMimeType;
  }

  /**
   * Get output stream.
   * @return Output stream
   * @throws IOException IO exception occurred
   */
  public OutputStream getOutputStream()
    throws IOException 
  {
    if (dataSource!=null) 
      return dataSource.getOutputStream();
    else
      return null;
  }

  /**
   * Get all commands.
   * @return Command list
   */
  public CommandInfo[] getAllCommands() 
  {
    CommandMap map = getCommandMap();
    return map.getAllCommands(getBaseType());
  }

  /**
   * Get base type.
   * @return Base type
   */
  private synchronized String getBaseType() 
  {
    if (shortType==null)
    {
      String contentType = getContentType();
      try
      {
        shortType = new MimeType(contentType).getBaseType();
      }
      catch (MimeTypeParseException e)
      {
        shortType = contentType;
      }
    }
    return shortType;
  } // getBaseType()

  /**
   * Get beans of command.
   * @param commandInfo TODO
   * @return Instantiated command bean, or null
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
   * @return Command information
   */
  public CommandInfo getCommand(String command) 
  {
    CommandMap map = getCommandMap();
    return map.getCommand(getBaseType(), command);
  }

  /**
   * Get command map.
   * @return Command map
   */
  private synchronized CommandMap getCommandMap() 
  {
    if (currentCommandMap==null)
      return CommandMap.getDefaultCommandMap();
    return currentCommandMap;
  }

  /**
   * Get data content handler.
   * @return Data content handler
   */
  private synchronized DataContentHandler getDataContentHandler() 
  {
    if (factory!=oldFactory)
    {
      dataContentHandler = null;
      factoryDCH = null;
      oldFactory = factory;
      transferFlavors = emptyFlavors;
    }
    if (dataContentHandler!=null)
      return dataContentHandler;
    String contentType = getBaseType();
    if (factory!=null && factoryDCH==null)
      factoryDCH = factory.createDataContentHandler(contentType);
    if (factoryDCH!=null)
      dataContentHandler = factoryDCH;
    else
    {
      CommandMap map = getCommandMap();
      dataContentHandler = map.createDataContentHandler(contentType);
    }
    if (dataSource!=null)
      dataContentHandler = 
          new DataSourceDataContentHandler(dataContentHandler, dataSource);
    else
      dataContentHandler = 
          new ObjectDataContentHandler(dataContentHandler, object, objectMimeType);
    return dataContentHandler; 
  }

  /**
   * Get data source.
   * @return Data source, or null
   */
  public DataSource getDataSource() 
  {
    if (dataSource==null)
    {
      if (objDataSource==null)
        objDataSource = new DataHandlerDataSource(this);
      else
        return objDataSource;
    }
    return dataSource;
  }

  /**
   * Get list of preferred commands.
   * @return List of preferred commands
   */
  public CommandInfo[] getPreferredCommands() 
  {
    CommandMap map = getCommandMap();
    return map.getPreferredCommands(getBaseType());
  }

  /**
   * Get transfer data based on data flavor.
   * @param dataFlavor Data flavor
   * @return Transfer data
   * @throws UnsupportedFlavorException Unsupported data flavor
   * @throws IOException IO exception occurred
   */
  public Object getTransferData(DataFlavor dataFlavor)
    throws UnsupportedFlavorException, IOException 
  {
    DataContentHandler handler = getDataContentHandler();
    return handler.getTransferData(dataFlavor, dataSource);
  }

  /**
   * Get transfer data flavors.
   * @return List of data flavors
   */
  public synchronized DataFlavor[] getTransferDataFlavors()
  {
    if (factory!=oldFactory)
      transferFlavors = emptyFlavors;
    if (transferFlavors==emptyFlavors)
      transferFlavors = getDataContentHandler().getTransferDataFlavors();
    return transferFlavors;
  }

  /**
   * Determine if data flavor is supported.
   * @param dataFlavor Data flavor
   * @return true if supported, false otherwise
   */
  public boolean isDataFlavorSupported(DataFlavor dataFlavor)
  {
    DataFlavor[] flavors = getTransferDataFlavors();
    for (int i=0; i<flavors.length; i++)
      if (flavors[i].equals(dataFlavor))
        return true;
    return false;
  }

  /**
   * Set command map.  If null, command map is reset to default.
   * @param map Command map
   */
  public synchronized void setCommandMap(CommandMap map)
  {
    if (map==null || map!=currentCommandMap) 
    {
      // Assume nowt
      currentCommandMap = map;
      dataContentHandler = null;
      transferFlavors = emptyFlavors;
    }
  }

  /**
   * Set the data content factory for data handler.  Note that this
   * is a one time call.  Further calls result in an Error.
   * @param newFactory New factory
   */
  public static synchronized void setDataContentHandlerFactory(
      DataContentHandlerFactory newFactory)
  {
    // Check For existing Factory
    if (factory != null) 
    {
      throw new Error();
    }

    // Set Factory
    SecurityManager sm = System.getSecurityManager();
    if (sm!=null)
    {
      try
      {
        sm.checkSetFactory();
      }
      catch (SecurityException e)
      {
        Class dataHandlerClazz = javax.activation.DataHandler.class;
        Class factoryClazz = newFactory.getClass();
        if (factoryClazz!=dataHandlerClazz)
          throw e;
      }
      factory = newFactory;
    }
  }

}
