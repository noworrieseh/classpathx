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

/**
 * Scrollable Rowset Access.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public	interface	ScrollableRowsetAccess
	extends		RowsetAccess {

	//-------------------------------------------------------------
	// Interface: ScrollableRowsetAccess --------------------------
	//-------------------------------------------------------------

	/**
	 * Get new cursor instance
	 * @returns New scrollable rowset access cursor
	 */
	public ScrollableRowsetAccess newCursor();

	/**
	 * Set buffer size
	 * @param size Buffer size
	 */
	public void setBufferSize(int size);

	/**
	 * Get buffer size
	 * @returns Buffer size
	 */
	public int getBufferSize();

	/**
	 * Move to previous row.
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean previous() throws	SQLException, 
									RowsetValidationException;

	/**
	 * Move to first row.
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean first() throws	SQLException, 
								RowsetValidationException;

	/**
	 * Move to last row.
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean last() throws	SQLException, 
								RowsetValidationException;

	/**
	 * ????
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean relative(int numRows) throws	SQLException, 
											RowsetValidationException;

	/**
	 * Get current row index
	 * @returns Row index
	 */
	public int getRow();

	/**
	 * Get row count.
	 * @returns Row count
	 */
	public int getRowCount();

	/**
	 * ????
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean absolute(int rowIndex) 
				throws	SQLException, 
						RowsetValidationException;


} // ScrollableRowsetAccess
