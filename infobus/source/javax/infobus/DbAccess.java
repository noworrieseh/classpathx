/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package javax.infobus;

// Imports
import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.util.Properties;

/**
 * DB Access interface.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public interface DbAccess {

	//-------------------------------------------------------------
	// Interface: DbAccess ----------------------------------------
	//-------------------------------------------------------------

	/**
	 * Connect
	 * @throws SQLException TODO
	 */
	public void connect() throws SQLException;

	/**
	 * Connect
	 * @param url TODO
	 * @param username TODO
	 * @param password TODO
	 * @throws SQLException TODO
	 */
	public void connect(String url, String username, String password)
		throws SQLException;

	/**
	 * Connect
	 * @param url TODO
	 * @param info TODO
	 * @throws SQLException TODO
	 */
	public void connect(String url, Properties info)
		throws SQLException;

	/**
	 * Disconnect
	 * @throws SQLException TODO
	 */
	public void disconnect() throws SQLException;

	/**
	 * Get property info
	 * @param url TODO
	 * @param info TODO
	 * @return TODO
	 */
	public DriverPropertyInfo getPropertyInfo(String url, Properties info);

	/**
	 * Execute Retrieval
	 * @param retrieval TODO
	 * @param dataItemName TODO
	 * @param options TODO
	 * @return TODO
	 * @throws SQLException TODO
	 */
	public Object executeRetrieval(String retrieval,
		String dataItemName, String options) throws SQLException;

	/**
	 * Execute command
	 * @param command TODO
	 * @param dataItemName TODO
	 * @return TODO
	 * @throws SQLException TODO
	 */
	public int executeCommand(String command, String dataItemName)
		throws SQLException;

	/**
	 * Begin transaction
	 * @throws UnsupportedOperationException TODO
	 */
	public void beginTransaction()
		throws UnsupportedOperationException;

	/**
	 * Commit transaction
	 * @throws SQLException TODO
	 * @throws RowsetValidationException TODO
	 * @throws UnsupportedOperationException TODO
	 */
	public void commitTransaction()
		throws	SQLException,
				RowsetValidationException,
				UnsupportedOperationException;

	/**
	 * Rollback transaction
	 * @throws SQLException TODO
	 * @throws RowsetValidationException TODO
	 * @throws UnsupportedOperationException TODO
	 */
	public void rollbackTransaction()
		throws	SQLException,
				RowsetValidationException,
				UnsupportedOperationException;

	/**
	 * Validate
	 * @throws SQLException TODO
	 * @throws RowsetValidationException TODO
	 */
	public void validate()
		throws SQLException, RowsetValidationException;

	/**
	 * Flush
	 * @throws SQLException TODO
	 * @throws RowsetValidationException TODO
	 */
	public void flush()
		throws SQLException, RowsetValidationException;


} // DbAccess
