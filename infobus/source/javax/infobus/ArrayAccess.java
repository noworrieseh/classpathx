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
 * Array Access interface.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public interface ArrayAccess {

	//-------------------------------------------------------------
	// Interface: ArrayAccess -------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get item by coordinates
	 * @param coordinates TODO
	 * @return TODO
	 */
	public Object getItemByCoordinates(int[] coordinates);

	/**
	 * Set item by coordinates
	 * @param coordinates TODO
	 * @param newValue TODO
	 * @throws InvalidDataException TODO
	 */
	public void setItemByCoordinates(int[] coordinates, Object newValue)
		throws InvalidDataException;

	/**
	 * Subdivide
	 * @param startCoordinates TODO
	 * @param endCoordinates TODO
	 * @return TODO
	 */
	public ArrayAccess subdivide(int[] startCoordinates, int[] endCoordinates);

	/**
	 * Get the dimensions of the array
	 * @return TODO
	 */
	public int[] getDimensions();


} // ArrayAccess
