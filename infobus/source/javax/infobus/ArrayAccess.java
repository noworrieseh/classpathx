/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Array Access interface.
 */
public abstract interface ArrayAccess {

	//-------------------------------------------------------------
	// Interface: ArrayAccess -------------------------------------
	//-------------------------------------------------------------

	public Object getItemByCoordinates(int[] coordinates);
	public void setItemByCoordinates(int[] coordinates, Object newValue)
		throws InvalidDataException;
	public ArrayAccess subdivide(int[] startCoordinates, int[] endCoordinates);
	public int[] getDimensions();


} // ArrayAccess
