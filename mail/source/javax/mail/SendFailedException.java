/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

/**
 * Send Failed Exception.
 */
public class SendFailedException extends MessagingException {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Invalid addresses.
	 */
	protected	transient	Address[]	invalid		= null;

	/**
	 * Valid sent addresses.
	 */
	protected	transient	Address[]	validSent	= null;

	/**
	 * Valid unsent addresses.
	 */
	protected	transient	Address[]	validUnsent	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create new send failed exception.
	 */
	public SendFailedException() {
	} // SendFailedException()

	/**
	 * Create new send failed with description.
	 * @param message Description
	 */
	public SendFailedException(String message) {
		super(message);
	} // SendFailedException()

	/**
	 * Create new send failed with description.
	 * @param message Description
	 * @param exception Next exception
	 */
	public SendFailedException(String message, Exception exception) {
		super(message);
//		next = exception;
	} // SendFailedException()

	/**
	 * Create new send failed with description.
	 * @param message Description
	 * @param exception Next exception
	 */
	public SendFailedException(String message, Exception exception,
				Address[] invalid, Address[] validSent,
				Address[] validUnsent) {
		this(message, exception);
		this.invalid = invalid;
		this.validSent = validSent;
		this.validUnsent = validUnsent;
	} // SendFailedException()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get Invalid addresses.
	 * @returns Invalid addresses
	 */
	public Address[] getInvalidAddresses() {
		return invalid;
	} // getInvalidAddresses()

	/**
	 * Get valid sent addresses.
	 * @returns Valid sent addresses
	 */
	public Address[] getValidSentAddresses() {
		return validSent;
	} // getValidSentAddresses()

	/**
	 * Get valid unsent addresses.
	 * @returns Valid unsent addresses
	 */
	public Address[] getValidUnsentAddresses() {
		return validUnsent;
	} // getValidUnsentAddresses()


} // SendFailedException

