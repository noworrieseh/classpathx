/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Reshapeable Array Access.
 */
public abstract interface	ReshapeableArrayAccess
		extends		ArrayAccess {

	//-------------------------------------------------------------
	// Interface: ReshapeableArrayAccess --------------------------
	//-------------------------------------------------------------

	/**
	 * Set dimensions.
	 * @param newDimensions New dimensions
	 * @throws IllegalArgumentException Illegal argument
	 */
	public void setDimensions(int[] newDimensions)
		throws IllegalArgumentException;

	/**
	 * TODO
	 * @param dimension TODO
	 * @param position TODO
	 * @param count TODO
	 * @throws IllegalArgumentException Illegal argument
	 */
	public void insert(int dimension, int position, int count)
		throws IllegalArgumentException;

	/**
	 * TODO
	 * @param dimension TODO
	 * @param position TODO
	 * @param count TODO
	 * @throws IllegalArgumentException Illegal argument
	 */
	public void delete(int dimension, int position, int count)
		throws IllegalArgumentException;


} // ReshapeableArrayAccess
