/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.net.Socket;
import java.util.Properties;
import java.io.IOException;

/**
 * Socket Fetcher
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class SocketFetcher {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public SocketFetcher() {
	} // SocketFetcher()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public static Socket getSocket(String host, int port,
			Properties props, String unknown) throws IOException {
		return null; // TODO
	} // getSocket()


} // SocketFetcher
