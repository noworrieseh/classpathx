/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Session.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class Session 
{

  //-------------------------------------------------------------
  // Variables --------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Session properties.
   */
  private Properties props = null;

  /**
   * Session authenticator call back that is used when password
   * authentication is required by the service.
   */
  private Authenticator authenticator = null;

  /**
   * Authentication table that shares PasswordAuthentication
   * objects between users of a session.
   */
  private Hashtable authTable = new Hashtable();

  /**
   * Debug mode flag.
   */
  private boolean debug = false;

  /**
   * List of providers.
   */
  private Vector providers = new Vector();

  /**
   * Mapping of providers by protocol.
   */
  private Hashtable providersByProtocol = new Hashtable();

  /**
   * Mapping of providers by class name.
   */
  private Hashtable providersByClassName = new Hashtable();

  /**
   * Mapping of address types to protocol.
   */
  private Properties addressMap = new Properties();

  /**
   * Unknown.
   */
  private static Method getResources = null;

  /**
   * Unknown.
   */
  private static Method getSystemResources = null;

  /**
   * Default Session.
   */
  private static Session defaultSession = null;


  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Session constructor.
   * @param properties Session properties
   * @param auth Authenticator for session
   */
  private Session(Properties properties, Authenticator auth) 
  {

    // Variables
    String result;

    props = properties;
    authenticator = auth;

    // Check mail.debug property
    result = getProperty("mail.debug");
    if (result != null && result.equals("true") == true) 
    {
      setDebug(true);
    } // if

    // Load Providers
    loadProviders(getClass());

    // Load Address Maps
    loadAddressMap(getClass());

  } // Session()


  //-------------------------------------------------------------
  // Methods ----------------------------------------------------
  //-------------------------------------------------------------

  /**
   * Get session property.
   * @param name Property name
   * @returns Value of property if property exists, null otherwise.
   */
  public String getProperty(String name) 
  {
    return props.getProperty(name);
  } // getProperty()

  /**
   * Get the debug state of this session.
   * @returns true if debug is on, false otherwise
   */
  public boolean getDebug() 
  {
    return debug;
  } // getDebug()

  /**
   * Create session instance.
   * @param properties Session properties
   * @param auth Authenticator for session
   */
  public static Session getInstance(Properties properties, Authenticator auth) 
  {
    return new Session(properties, auth);
  } // getInstance()

  /**
   * Returns an array of installed providers.
   * @returns Array of providers
   */
  public Provider[] getProviders() 
  {

    // Variables
    Provider[] list;
    int index;

    // Create Array
    list = new Provider[providers.size()];
    for (index = 0; index < providers.size(); index++) 
    {
      list[index] = (Provider) providers.elementAt(index);
    } // for

    // Return List
    return list;

  } // getProviders()

  /**
   * Get provider for the specified protocol.
   * @param protocol Protocol name
   * @returns Provider for protocol
   * @throws NoSuchProviderException No provider exists for the
   * specified protocol
   */
  public Provider getProvider(String protocol) throws NoSuchProviderException 
  {

    // Variables
    String query;
    Provider provider;

    // Check for Provider Class
    query = getProperty("mail." + protocol + ".class");
    if (query != null) 
    {
      return (Provider) providersByClassName.get(query);
    } // if

    // Check by Protocol
    provider = (Provider) providersByProtocol.get(protocol);
    if (provider != null) 
    {
      return provider;
    } // if

    // Unknown Provider Error
    throw new NoSuchProviderException();

  } // getProvider()

  /**
   * Add a default provider.  The protocol name is extracted
   * from the provider.
   * @param Provider to register
   * @throws NoSuchProviderException Unknown why this would be
   * thrown // TODO
   */
  public void setProvider(Provider provider)
  throws NoSuchProviderException 
  {

    // Variables
    String protocol;
    String className;

    // Get Protocol of Provider
    protocol = provider.getProtocol();

    // Get Class Name
    className = provider.getClassName();

    // Register as Default
    providersByProtocol.put(protocol, provider);
    providersByClassName.put(className, provider);

  } // setProvider()

  /**
   * Retrieve the default session instance.  A default session
   * instance will be created if it doesn't exist yet.
   * @param properties Properties for default instance
   * @param auth Authenticator for default instance
   * @returns Default session instance
   */
  public static Session getDefaultInstance(Properties properties,
					   Authenticator auth) 
  {

    // Check Permission
    // TODO

    // Create Default Session
    if (defaultSession == null) 
    {
      defaultSession = getInstance(properties, auth);
    } // if

    // Return Default Session
    return defaultSession;

  } // getDefaultInstance()

  /**
   * Get Folder from URLName.
   * @param urlName URLName of folder
   * @returns Folder
   * @throws MessagingException Messaging problem has occurred
   */
  public Folder getFolder(URLName urlName) throws MessagingException 
  {

    // Variables
    Store store;

    try 
    {

      // Get Store
      store = getStore(urlName);

      // Get Folder from Store
      return store.getFolder(urlName);

    } catch (Exception e) 
    {
      throw new MessagingException();
    } // try

  } // getFolder()

  /**
   * Get password authentication for specified URLName.
   * @param urlName URLName to lookup
   * @returns Password authentication
   */
  public PasswordAuthentication getPasswordAuthentication(URLName urlName) 
  {
    return (PasswordAuthentication) authTable.get(urlName);
  } // getPasswordAuthentication()

  /**
   * Get service instance.
   * @param provider Provider
   * @param urlname URLName to initialize store
   * @returns Service instance
   * @throws NoSuchProviderException Provider doesn't exist
   */
  private Object getService(Provider provider, URLName urlName)
  throws NoSuchProviderException 
  {

    // Variables
    String className;
    Class classObject;
    Constructor constructor;
    Object instance;

    // Get Class Name of Provider
    className = provider.getClassName();

    try 
    {

      // Get Class
      classObject = Class.forName(className);

      // Get Constructor
      constructor = classObject.getConstructor(new Class[] 
	{
	  Class.forName("javax.mail.Session"),
	  Class.forName("javax.mail.URLName")});

      // Instantiate Class
      instance = constructor.newInstance(new Object[] {this, urlName});

      // Return Object
      return instance;

    } catch (Exception e) 
    {
      throw new NoSuchProviderException();
    } // try

  } // getService()

  /**
   * Get default protocol store instance.
   * @returns Store instance
   * @throws NoSuchProviderException Store provider doesn't exist
   */
  public Store getStore() throws NoSuchProviderException 
  {

    // Variables
    String defaultStore;

    // Get Default Store Protocol
    defaultStore = getProperty("mail.store.protocol");

    // Get Store for Protocol
    return getStore(defaultStore);

  } // getStore()

  /**
   * Get store instance.
   * @param protocol Store protocol
   * @returns Store instance
   * @throws NoSuchProviderException Store provider doesn't exist
   */
  public Store getStore(String protocol) throws NoSuchProviderException 
  {

    // Variables
    Provider provider;

    // Get Provider for Protocol
    provider = (Provider) providersByProtocol.get(protocol);

    // Check Result
    if (provider == null) 
    {
      throw new NoSuchProviderException();
    } // if

    // Check Type
    if (provider.getType() != Provider.Type.STORE) 
    {
      throw new NoSuchProviderException();
    }

    // Generate Store Object
    return getStore(provider);

  } // getStore()

  /**
   * Get store instance.
   * @param urlname URLName to initialize store
   * @returns Store instance
   * @throws NoSuchProviderException Store provider doesn't exist
   */
  public Store getStore(URLName urlName) throws NoSuchProviderException 
  {

    // Variables
    String protocol;

    // Get Protocol from URLName
    protocol = urlName.getProtocol();
    if (protocol == null) 
    {
      throw new NoSuchProviderException();
    } // if

    // Get Store for Protocol
    return getStore(protocol);

  } // getStore()

  /**
   * Get store instance.
   * @param provider Provider
   * @returns Store instance
   * @throws NoSuchProviderException Store provider doesn't exist
   */
  public Store getStore(Provider provider) throws NoSuchProviderException 
  {
    return getStore(provider, null); // TODO
  } // getStore()

  /**
   * Get store instance.
   * @param provider Provider
   * @param urlname URLName to initialize store
   * @returns Store instance
   * @throws NoSuchProviderException Store provider doesn't exist
   */
  private Store getStore(Provider provider, URLName urlName)
  throws NoSuchProviderException 
  {

    // Variables
    Object service;

    // Get Service
    service = getService(provider, urlName);

    // Check for Store
    if (service instanceof Store) 
    {
      return (Store) service;
    } // if

    // Problem, return error
    throw new NoSuchProviderException();

  } // getStore()

  /**
   * Instantiate a transport based on the default
   * transport protocol.  Looks up parameter "mail.transport.protocol"
   * @return Transport instance
   * @throws NoSuchProviderException No provider available
   */
  public Transport getTransport() throws NoSuchProviderException 
  {

    // Variables
    String defaultTransport;

    // Get Default Transport Protocol
    defaultTransport = getProperty("mail.transport.protocol");

    // Get Store for Protocol
    if (defaultTransport == null) 
    {
      throw new NoSuchProviderException("property mail.transport.protocol" +
					" does not exist");
    } // if
    return getTransport(defaultTransport);

  } // getTransport()

  /**
   * Instantiate a transport for the specified protocol.
   * @param protocol Protocol to transport over
   * @returns Transport object
   * @throws NoSuchProviderException There doesn't exist a
   * transport that implements the specified protocol
   */
  public Transport getTransport(String protocol)
  throws NoSuchProviderException 
  {

    // Variables
    Provider provider;

    // Get Provider for Protocol
    provider = (Provider) providersByProtocol.get(protocol);

    // Check Result
    if (provider == null) 
    {
      throw new NoSuchProviderException();
    } // if

    // Check Type
    if (provider.getType() != Provider.Type.TRANSPORT) 
    {
      throw new NoSuchProviderException();
    }

    // Generate Transport Object
    return getTransport(provider);

  } // getTransport()

  /**
   * Get a transport based on a URL Name.
   * @param urlName URL name to look up provider with
   * @returns Transport object
   * @throws NoSuchProviderException Provider doesn't exist
   */
  public Transport getTransport(URLName urlName)
  throws NoSuchProviderException 
  {

    // Variables
    String protocol;

    // Get Protocol from URLName
    protocol = urlName.getProtocol();
    if (protocol == null) 
    {
      throw new NoSuchProviderException();
    } // if

    // Get Transport for Protocol
    return getTransport(protocol);

  } // getTransport()

  /**
   * Returns an instance of the provider.
   * @param provider Provider to instantiate transport from
   * @returns Transport object
   * @throws NoSuchProviderException Provider doesn't
   * exist or supply a transport instance.
   */
  public Transport getTransport(Provider provider)
  throws NoSuchProviderException 
  {
    return getTransport(provider, null); // TODO
  } // getTransport()

  /**
   * Get transport based on protocol used to deliver
   * to the provided address.
   * @param address Address to deliver to
   * @returns Transport object
   * @throws NoSuchProviderException There doesn't exist
   * a registered provider to deliver a message to
   * the address.
   */
  public Transport getTransport(Address address)
  throws NoSuchProviderException 
  {

    // Variables
    String addressType;
    String protocol;

    // Get Address Type
    addressType = address.getType();

    // Check for protocol mapping for Type
    protocol = addressMap.getProperty(addressType);

    // Check for Error
    if (protocol == null) 
    {
      throw new NoSuchProviderException();
    } // if

    // Get Transport
    return getTransport(protocol);

  } // getTransport()

  /**
   * Get transport for specified provider and URL.
   * @param provider Provider to use
   * @param urlName URLName to use
   * @returns Transport object
   */
  private Transport getTransport(Provider provider, URLName urlName)
  throws NoSuchProviderException 
  {

    // Variables
    Object service;

    // Get Service
    service = getService(provider, urlName);

    // Check for Store
    if (service instanceof Transport) 
    {
      return (Transport) service;
    } // if

    // Problem, return error
    throw new NoSuchProviderException();

  } // getTransport()

  /**
   * Load Providers.
   * @param object Class to obtain loader from
   */
  private void loadProviders(Class object) 
  {

    // Variables
    ClassLoader loader;
    Provider provider;
    InputStream stream;
    String home;
    String resource;

    // Get Class Loader
    loader = object.getClassLoader();

    try 
    {

      // Load <java.home>/lib/javamail.provider
      home = System.getProperty("java.home");
      resource = home + "/lib/javamail.provider";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	loadProvidersFromStream(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

    try 
    {

      // Load META-INF/javamail.provider
      resource = "META-INF/javamail.provider";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	loadProvidersFromStream(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

    try 
    {

      // Load META-INF/javamail.default.provider
      resource = "META-INF/javamail.default.providers";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	loadProvidersFromStream(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

  } // loadProviders()

  /**
   * Load Address Map.
   * @param object Class to obtain loader from
   */
  private void loadAddressMap(Class object) 
  {

    // Variables
    ClassLoader loader;
    Provider provider;
    InputStream stream;
    String home;
    String resource;

    // Get Class Loader
    loader = object.getClassLoader();

    try 
    {

      // Load <java.home>/lib/javamail.address.map
      home = System.getProperty("java.home");
      resource = home + "/lib/javamail.address.map";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	addressMap.load(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

    try 
    {

      // Load META-INF/javamail.address.map
      resource = "META-INF/javamail.address.map";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	addressMap.load(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

    try 
    {

      // Load META-INF/javamail.default.address.map
      resource = "META-INF/javamail.default.address.map";
      stream = loader.getResourceAsStream(resource);
      if (stream != null) 
      {
	addressMap.load(stream);
      } // if

    } catch (IOException e) 
    {
      System.out.println(e);
    } // try

  } // loadAddressMap()

  /**
   * Load providers from specified stream.
   * @param stream Stream to load providers from
   * @throws IOException IO exception occurred during reading
   */
  private void loadProvidersFromStream(InputStream stream)
  throws IOException 
  {

    // Variables
    BufferedReader input;
    String line;
    StringTokenizer tokens;
    String section;
    String field;
    String value;
    String value2;
    int index;
    String protocol;
    String className;
    String vendor;
    String version;
    Provider.Type providerType;
    Provider provider;

    // Get Reader
    // TODO: This should 1.1 compatible...no readers
    input = new BufferedReader(new InputStreamReader(stream));

    // Process Each Line
    line = input.readLine();
    while (line != null) 
    {

      // Check for Comment
      if (line.trim().startsWith("#") == false) 
      {

	// Initialize Elements
	protocol = null;
	className = null;
	providerType = null;
	vendor = null;
	version = null;

	// Tokenize Line
	tokens = new StringTokenizer(line, ";");
	while (tokens.hasMoreTokens() == true) 
	{
	  section = tokens.nextToken();
	  index = section.indexOf("=");
	  if (index != -1) 
	  {
	    field = section.substring(0, index).trim().toUpperCase();
	    value = section.substring(index + 1).trim();

	    // Check for Protocol
	    if (field.equals("PROTOCOL") == true) 
	    {
	      protocol = value;

	      // Check for Class
	    } else if (field.equals("CLASS") == true) 
	    {
	      className = value;

	      // Check for Type
	    } else if (field.equals("TYPE") == true) 
	    {

	      value2 = value.toUpperCase();
	      if (value2.equals("TRANSPORT") == true) 
	      {
		providerType = Provider.Type.TRANSPORT;
	      } else if (value2.equals("STORE") == true) 
	      {
		providerType = Provider.Type.STORE;
	      }

	      // Check for Vendor
	    } else if (field.equals("VENDOR") == true) 
	    {
	      vendor = value;

	      // Check for Version
	    } else if (field.equals("VERSION") == true) 
	    {
	      version = value;

	    } // if
	    // System.out.println("Field: " + field + " Value: " + value);
	  }
	} // while

	// Check for necessary information for Provider
	if ((protocol != null) &&
	    (providerType != null) &&
	    (className != null)) 
	{

	  // Create Provider
	  provider = new Provider(providerType, protocol,
				  className, vendor, version);

	  try 
	  {

	    // Add Provider
	    providers.addElement(provider);
	    setProvider(provider);

	  } catch (Exception e) 
	  {
	  } // try

	} // if

      } // if

      // Read Next Line
      line = input.readLine();

    } // while

  } // loadProvidersFromStream()

  private static void pr(String value) 
  {
    // TODO
  } // pr()

  /**
   * Request that callback be done to request a password
   * authentication for this session.
   * @param address Internet address being inquiried about
   * @param port Port on host
   * @param protocol Protocol being authenticated
   * @param prompt Prompt to send to user
   * @param defaultUsername Default username to use in authentiation
   * @returns Password authentication, or null if unsuccessful
   */
  public PasswordAuthentication requestPasswordAuthentication(InetAddress address,
							      int port, String protocol, String prompt, String defaultUserName) 
  {

    // Check for Authenticator
    if (authenticator != null) 
    {
      return authenticator.requestPasswordAuthentication(address, port,
							 protocol, prompt, defaultUserName);
    } // if

    // Unknown
    return null;

  } // requestPasswordAuthentication()

  /**
   * Set the debug state of the session.
   * @param value Debug state to set
   */
  public void setDebug(boolean value) 
  {
    debug = value;
  } // setDebug()

  /**
   * Set the default password authentication for the specified
   * URLName.
   * @param urlName URLName to register PA with
   * @param pass Password Authentication object
   */
  public void setPasswordAuthentication(URLName urlName,
					PasswordAuthentication pass) 
  {
    authTable.put(urlName, pass);
  } // setPasswordAuthentication()


} // Session
