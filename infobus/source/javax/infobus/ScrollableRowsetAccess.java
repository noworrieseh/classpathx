/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.sql.SQLException;

/**
 * Scrollable Rowset Access.
 */
public abstract interface	ScrollableRowsetAccess
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
