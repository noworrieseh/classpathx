/********************************************************************
 * Copyright (c) Open Java Extensions, LGPL License                 *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.*;

/**
 * "B" Encoder stream as specified by rfc 2047.  A "B" encoder
 * stream is identical to the BASE 64 encoding defined in RFC 2045.
 *
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class BEncoderStream extends BASE64EncoderStream {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create B Encoder stream.
	 * @param stream Output stream
	 */
	public BEncoderStream(OutputStream stream) {
		super(stream);
	} // BEncoderStream()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Determine the encoded length of a series of bytes.
	 * @param bytes Byte array to be encoded
	 * @returns Length on encoding
	 */
	public static int encodedLength(byte[] bytes) {

		// Note: This implementation processes the bytes into an
		// encoding and then returns the length of the bytes.
		// There should be an algorithmic way of determining the
		// length of the encoded stream without having the
		// create an entire encoding text.

		return encode(bytes).length;

	} // encodedLength()


} // BEncoderStream
