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
import java.sql.*;
import java.util.Properties;

/**
 * DB Access interface.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public interface DbAccess {

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
