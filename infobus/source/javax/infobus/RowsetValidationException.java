/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Rowset Validation Exception.
 */
public	class	RowsetValidationException
	extends	InvalidDataException {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Rowset access reference.
	 */
	private RowsetAccess		rowset	= null;

	/**
	 * Property map
	 */
	private	InfoBusPropertyMap	map		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create Rowset Validation Exception
	 * @param message Exception message
	 * @param rowset Rowset
	 * @param map Infobus property map
	 */
	public RowsetValidationException(String				message, 
								 RowsetAccess		rowset, 
								 InfoBusPropertyMap map) {
		super(message);
		this.rowset = rowset;
		this.map = map;
	} // RowsetValidationException()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get rowset.
	 * @returns Rowset
	 */
	public RowsetAccess getRowset() {
		return rowset;
	} // getRowset()

	/**
	 * Get property
	 * @param propertyName Name of property to retrieve
	 * @returns Property value
	 */
	public Object getProperty(String propertyName) {
		return map.get(propertyName);
	} // getProperty()


} // RowsetValidationException
