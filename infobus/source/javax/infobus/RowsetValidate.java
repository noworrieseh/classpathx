/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Rowset Validate Interface.
 */
public abstract interface RowsetValidate {

	//-------------------------------------------------------------
	// Interface: RowsetValidate ----------------------------------
	//-------------------------------------------------------------

	/**
	 * Validate the current row
	 * @throws RowsetValidationException Rowset Validation exception
	 */
	public void validateCurrentRow()
		throws RowsetValidationException;

	/**
	 * Validate the row set
	 * @throws RowsetValidationException Rowset Validation exception
	 */
	public void validateRowset()
		throws RowsetValidationException;


} // RowsetValidate
