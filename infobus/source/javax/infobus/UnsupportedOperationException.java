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
 * The UnsupportedOperationException is available to be thrown when
 * a participating InfoBus consumer/producer, or data item does not
 * support a particular method call.
 * @author Andrew Selkirk
 * @version $Revision: 1.3 $
 */
public class UnsupportedOperationException extends RuntimeException {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create an UnsupportedOperationException with a detailed
	 * description of the problem.
	 * @param message Detailed description of the exception
	 */
	public UnsupportedOperationException(String message) {
		super(message);
	} // UnsupportedOperationException()


} // UnsupportedOperationException
