/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.io.*;
import java.util.*;

/**
 * Multipart.
 */
public abstract class Multipart {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Collection of parts.
	 */
	protected	Vector	parts		= new Vector();

	/**
	 * Content type of multipart.
	 */
	protected	String	contentType	= null;

	/**
	 * Parent part
	 */
	protected	Part	parent		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new multipart.
	 */
	protected Multipart() {
	} // Multipart()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get parent part.
	 * @returns Parent part
	 */
	public Part getParent() {
		return parent;
	} // getParent()

	/**
	 * Write object to output stream.
	 * @param stream Output stream to write to
	 */
	public abstract void writeTo(OutputStream stream)
		throws IOException, MessagingException;

	/**
	 * Get content type.
	 * @returns Content type
	 */
	public String getContentType() {
		return contentType;
	} // getContentType()

	/**
	 * Add body part.
	 * @param part Part to add
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized void addBodyPart(BodyPart part)
			throws MessagingException {
		parts.addElement(part);
	} // addBodyPart()

	/**
	 * Add body part.
	 * @param part Part to add
	 * @param index Position index to add
	 * @throws MessagingException Messaging exception occurred
	 */
	public synchronized void addBodyPart(BodyPart part, int index)
			throws MessagingException {
		parts.insertElementAt(part, index);
	} // addBodyPart()

	/**
	 * Get body part.
	 * @param index Index of part
	 * @returns Body Part
	 * @throws MessagingException Messaging exception occurred
	 */
	public BodyPart getBodyPart(int index) throws MessagingException {
		return (BodyPart) parts.elementAt(index);
	} // getBodyPart()

	/**
	 * Get count.
	 * @returns Body part count
	 * @throws MessagingException Messaging exception occurred
	 */
	public int getCount() throws MessagingException {
		return parts.size();
	} // getCount()

	/**
	 * Remove body part.
	 * @param part Body part to remove
	 * @returns true if successful, false otherwise
	 * @throws MessagingException Messaging exception occurred
	 */
	public boolean removeBodyPart(BodyPart part)
			throws MessagingException {
		return parts.removeElement(part);
	} // removeBodyPart()

	/**
	 * Remove body part at index.
	 * @param index Index of body part ot remove
	 * @throws MessagingException Messaging exception occurred
	 */
	public void removeBodyPart(int index) throws MessagingException {
		parts.removeElementAt(index);
	} // removeBodyPart()

	/**
	 * Set data source.
	 * @param dataSource Data source
	 * @throws MessagingException Messaging exception occurred
	 */
	protected void setMultipartDataSource(MultipartDataSource dataSource)
			throws MessagingException {

		// Variables
		int	index;

		// Add Body Parts
		for (index = 0; index < dataSource.getCount(); index++) {
			addBodyPart(dataSource.getBodyPart(index));
		} // for: index

		// Set Content Type
		contentType = dataSource.getContentType();

	} // setMultipartDataSource()

	/**
	 * Set parent body part.
	 * @param parent Parent body part
	 */
	public void setParent(Part parent) {
		this.parent = parent;
	} // setParent()


} // Multipart
