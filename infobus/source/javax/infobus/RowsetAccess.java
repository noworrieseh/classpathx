/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.sql.*;

/**
 * Rowset Access
 */
public abstract interface RowsetAccess {

	//-------------------------------------------------------------
	// Interface: RowsetAccess ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get column count.
	 * @returns Column count
	 */
	public int getColumnCount();

	/**
	 * Get column name
	 * @param columnIndex Column index
	 * @returns Name of column
	 * @throws IndexOutOfBoundsException Invalid index
	 */
	public String getColumnName(int columnIndex)
		throws IndexOutOfBoundsException;

	/**
	 * Get data type of column
	 * @param columnIndex Column index
	 * @returns SQL data type number
	 * @throws IndexOutOfBoundsException Invalid index
	 */
	public int getColumnDatatypeNumber(int columnIndex)
		throws IndexOutOfBoundsException;

	/**
	 * Get data type name of column
	 * @param columnIndex Column index
	 * @returns SQL data type name
	 * @throws IndexOutOfBoundsException Invalid index
	 */
	public String getColumnDatatypeName(int columnIndex)
		throws IndexOutOfBoundsException;

	/**
	 * Move to next row.
	 * @returns true if successful, false otherwise
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public boolean next()
		throws SQLException, RowsetValidationException;

	/**
	 * TODO
	 * @returns TODO
	 */
	public int getHighWaterMark();

	/**
	 * Check if there are more rows.
	 * @returns true if more rows, false otherwise
	public boolean hasMoreRows();

	/**
	 * Get column item value.
	 * @param columnIndex Column index
	 * @returns Object value
	 * @throws IndexOutOfBoundsException Invalid index
	 * @throws SQLException SQL Exception
	 */
	public Object getColumnItem(int columnIndex)
		throws IndexOutOfBoundsException, SQLException;

	/**
	 * Get column item value.
	 * @param columnName Column name
	 * @returns Object value
	 * @throws IndexOutOfBoundsException Invalid index
	 * @throws SQLException SQL Exception
	 */
	public Object getColumnItem(String columnName)
		throws	ColumnNotFoundException, 
				DuplicateColumnException, 
				SQLException;

	/**
	 * Insert new row.
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void newRow()
		throws SQLException, RowsetValidationException;

	/**
	 * Set column item value.
	 * @param columnIndex Column index
	 * @param object Object value
	 * @throws IndexOutOfBoundsException Invalid index
	 * @throws SQLException SQL Exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void setColumnValue(int columnIndex, Object object)
		throws	IndexOutOfBoundsException, 
				SQLException, 
				RowsetValidationException;

	/**
	 * Set column item value.
	 * @param columnName Column name
	 * @param object Object value
	 * @throws ColumnNotFoundException Column not found
	 * @throws DuplicateColumnException Duplicate column exception
	 * @throws SQLException SQL Exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void setColumnValue(String columnName, Object object)
		throws	ColumnNotFoundException, 
				DuplicateColumnException, 
				SQLException, 
				RowsetValidationException;

	/**
	 * Delete row.
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void deleteRow()
		throws SQLException, RowsetValidationException;

	/**
	 * Flush row.
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void flush()
		throws SQLException, RowsetValidationException;

	/**
	 * Lock row.
	 * @throws SQLException SQL exception
	 * @throws RowsetValidationException Rowset validation exception
	 */
	public void lockRow()
		throws SQLException, RowsetValidationException;

	/**
	 * Check if insert possible.
	 * @returns true if possible, false otherwise
	 */
	public boolean canInsert();

	/**
	 * Check if update possible.
	 * @returns true if possible, false otherwise
	 */
	public boolean canUpdate();

	/**
	 * Check if update of column possible.
	 * @param columnName Column name
	 * @returns true if possible, false otherwise
	 * @throws ColumnNotFoundException Column not found
	 * @throws DuplicateColumnException Duplicate column exception
	 */
	public boolean canUpdate(String columnName)
		throws ColumnNotFoundException, DuplicateColumnException;

	/**
	 * Check if update of column possible.
	 * @param columnIndex Column index
	 * @returns true if possible, false otherwise
	 * @throws IndexOutOfBoundsException Invalid index
	 */
	public boolean canUpdate(int columnNumber)
		throws IndexOutOfBoundsException;

	/**
	 * Check if delete possible.
	 * @returns true if possible, false otherwise
	 */
	public boolean canDelete();

	/**
	 * Get DB Access.
	 * @returns DB Access
	 */
	public DbAccess getDb();


} // RowsetAccess
