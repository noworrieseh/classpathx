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
 * Stale Infobus Exception.  Thrown when action performed on an
 * Infobus instance that is no longer active.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public	class	StaleInfoBusException
		extends	RuntimeException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create exception with description
	 * @param message Description string
	 */
	public StaleInfoBusException(String message) {
		super(message);
	} // StaleInfoBusException()


} // StaleInfoBusException
