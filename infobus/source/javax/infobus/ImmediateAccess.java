/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

// Imports
import java.util.Locale;

/**
 * Immediate Access.
 */
public abstract interface ImmediateAccess {

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
