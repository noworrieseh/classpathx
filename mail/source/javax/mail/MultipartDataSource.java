/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import javax.activation.DataSource;

/**
 * Multipart Data Source
 */
public interface MultipartDataSource extends DataSource {

	//-------------------------------------------------------------
	// Interface: MultipartDataSource -----------------------------
	//-------------------------------------------------------------

	/**
	 * Get body part.
	 * @param param index Index of body part
	 * @returns Body part
	 * @throws MessagingException Messaging exception occurred
	 */
	public BodyPart getBodyPart(int index)
		throws MessagingException;

	/**
	 * Get number of body parts.
	 * @returns Number of body parts
	 */
	public int getCount();


} // MultipartDataSource
