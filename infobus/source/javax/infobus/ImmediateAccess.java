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
import java.util.Locale;

/**
 * Immediate Access.
 * @author Andrew Selkirk
 * @version $Revision: 1.2 $
 */
public interface ImmediateAccess {

	//-------------------------------------------------------------
	// Interface: ImmediateAccess ---------------------------------
	//-------------------------------------------------------------

	/**
	 * Get value as a string.
	 * @returns Value as string
	 */
	public String getValueAsString();

	/**
	 * Get value as an object.
	 * @returns Value as an object
	 */
	public Object getValueAsObject();

	/**
	 * Get presentation string.
	 * @param locale Locale
	 * @return Locale-based presentation string
	 */
	public String getPresentationString(Locale locale);

	/**
	 * Set value.
	 * @param newValue New value
	 */
	public void setValue(Object newValue)
		throws InvalidDataException;


} // ImmediateAccess
