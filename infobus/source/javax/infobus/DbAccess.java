/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.sql.*;
import java.util.Properties;

/**
 * DB Access interface.
 */
public abstract interface DbAccess {

	//-------------------------------------------------------------
	// Interface: DbAccess ----------------------------------------
	//-------------------------------------------------------------

	public void connect() throws SQLException;
	public void connect(String url, String username, String password)
		throws SQLException;
	public void connect(String url, Properties info)
		throws SQLException;
	public void disconnect() throws SQLException;
	public DriverPropertyInfo getPropertyInfo(String url, Properties info);
	public Object executeRetrieval(String retrieval, String dataItemName, String options)
		throws SQLException;
	public int executeCommand(String command, String dataItemName)
		throws SQLException;
	public void beginTransaction()
		throws UnsupportedOperationException;
	public void commitTransaction()
		throws SQLException, RowsetValidationException, UnsupportedOperationException;
	public void rollbackTransaction()
		throws SQLException, RowsetValidationException, UnsupportedOperationException;
	public void validate()
		throws SQLException, RowsetValidationException;
	public void flush()
		throws SQLException, RowsetValidationException;


} // DbAccess
