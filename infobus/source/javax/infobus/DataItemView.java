/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.infobus;

/**
 * Data Item View interface.
 */
public abstract interface DataItemView {

	//-------------------------------------------------------------
	// Interface: DataItemView ------------------------------------
	//-------------------------------------------------------------

	public int getViewStart();
	public void setViewStart(int absoluteRow);
	public void scrollView(int relativeAmount);
	public ArrayAccess getView(int viewSize);


} // DataItemView
