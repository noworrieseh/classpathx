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

/**
 * Rowset Validation Exception.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
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
