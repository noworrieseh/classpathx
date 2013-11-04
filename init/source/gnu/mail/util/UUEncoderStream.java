/********************************************************************
 * Copyright (c) Open Java Extensions, LGPL License                 *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.OutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

/**
 * UU Encoding stream.
 *
 * @author Andrew Selkirk
 * @version 1.0
 * @see java.io.FilterOutputStream
 **/
public class UUEncoderStream extends FilterOutputStream {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Encoding buffer.  Up to 45 bytes are read in before encoding
	 * is started (unless a flush is initiated).
	 */
	private		byte[]	buffer;

	/**
	 * Current number of bytes in buffer.
	 */
	private		int		bufsize;

	/**
	 * Flag to indicate if prefix written.
	 */
	private		boolean	wrotePrefix;

	/**
	 * Name of file to be uuencoded.
	 */
	protected	String	name;

	/**
	 * Permission mode of file.
	 */
	protected 	int		mode;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new UU-Encoding stream with the default name
	 * "encoder.buf" with the permission mode 644.
	 * @param stream Output stream
	 */
	public UUEncoderStream(OutputStream stream) {
		this(stream, "encoder.buf", 644);
	} // UUEncoderStream()

	/**
	 * Create a new UU-Encoding stream with the default name
	 * permission mode 644.
	 * @param stream Output stream
	 * @param name File name
	 */
	public UUEncoderStream(OutputStream stream, String name) {
		this(stream, name, 644);
	} // UUEncoderStream()

	/**
	 * Create a new UU-Encoding stream.
	 * @param stream Output stream
	 * @param name File name
	 * @param mode File permission mode
	 */
	public UUEncoderStream(OutputStream stream, String name, int mode) {
		super(stream);
		this.name = name;
		this.mode = mode;
		wrotePrefix = false;
		buffer = new byte[45];
		bufsize = 0;
	} // UUEncoderStream()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Flush encoding buffer.
	 * @exception IOException IO Exception occurred
	 */
	public void flush() throws IOException {
		encode();
	} // flush()

	/**
	 * Write bytes to encoding stream.
	 * @param bytes Byte array to read values from
	 * @param offset Offset to start reading bytes from
	 * @param length Number of bytes to read
	 * @exception IOException IO Exception occurred
	 */
	public void write(byte[] bytes, int offset, int length)
			throws IOException {

		// Note: This implementation is not as efficient as
		// it could be.  Instead of delegating the work to
		// write(int), the bytes should be written in bulk
		// to the buffer and initiating encoding if necessary.
		// This idea does introduce some duplication of code.

		// Variables
		int		index;

		// Write Bytes
		for (index = offset; index < length; index++) {
			write(bytes[index]);
		} // for: index

	} // write()

	/**
	 * Write bytes to stream.
	 * @param bytes Byte array to write to stream
	 * @exception IOException IO Exception occurred
	 */
	public void write(byte[] bytes) throws IOException {
		write(bytes, 0, bytes.length);
	} // write()

	/**
	 * Write a byte to the stream.
	 * @param b Byte to write to the stream
	 * @exception IOException IO Exception occurred
	 */
	public void write(int b) throws IOException {

		// Check for Written Prefix
		if (wrotePrefix == false) {
			writePrefix();
		} // if

		// Add Byte to Buffer
		buffer[bufsize] = (byte) b;
		bufsize += 1;

		// Check for Flush
		if (bufsize == buffer.length) {
			encode();
		} // if

	} // write()

	/**
	 * Close stream.
	 * @exception IOException IO Exception occurred
	 */
	public void close() throws IOException {

		// Flush Encoding buffer
		flush();

		// Write Encoding Suffix
		writeSuffix();

		// Close
		out.close();

	} // close()

	/**
	 * Flush encoding buffer.
	 * @exception IOException IO Exception occurred
	 */
	private void encode() throws IOException {

		// Variables
		int		index;
		int		c1;
		int		c2;
		int		c3;
		int		c4;
		int		a;
		int		b;
		int		c;

		// Write Line prefix
		super.write((bufsize & 0x3f) + ' ');

		// Process bytes in groups of three
		index = 0;
		while (index < bufsize) {

			// Get Bytes
			a = buffer[index];
			if (index + 1 < bufsize) {
				b = buffer[index+1];
			} else {
				b = 1;
			} // if
			if (index + 2 < bufsize) {
				c = buffer[index+2];
			} else {
				c = 1;
			} // if

			// Calculate UU Encoding
			c1 = (a >>> 2) & 0xff;
			c2 = ((a << 4) & 0x30) | ((b >>> 4) & 0xf);
			c3 = ((b << 2) & 0x3c) | ((c >>> 6) & 0x3);
			c4 = c & 0x3f;

			// Write Encoded Characters
			out.write(c1 + ' ');
			out.write(c2 + ' ');
			out.write(c3 + ' ');
			out.write(c4 + ' ');

			// Increment position
			index += 3;

		} // while

		// Write Newline
		out.write('\n');

		// Reset Buffer size
		bufsize = 0;

	} // encode()

	/**
	 * Set the name and mode for this uu encoded stream.  Note
	 * that this is only valid before data has been uuencoded as
	 * this information is written at the beginning of the stream.
	 * @param name File name
	 * @param mode Permission mode
	 */
	public void setNameMode(String name, int mode) {
		this.name = name;
		this.mode = mode;
	} // setNameMode()

	/**
	 * Write uuencoding prefix.  Constructed as "begin <mode> <name>".
	 * @exception IO Exception has occurred
	 */
	private void writePrefix() throws IOException {

		// Variables
		StringBuffer	prefix;
		String			modeString;

		// Construct Prefix
		prefix = new StringBuffer();
		prefix.append("begin ");
		if (mode < 100) {
			prefix.append("0");
		} // if
		if (mode < 10) {
			prefix.append("0");
		} // if
		prefix.append(mode);
		prefix.append(" " + name + "\n");

		// Write Prefix to stream
		out.write(prefix.toString().getBytes());

		// Flag that prefix has been written
		wrotePrefix = true;

	} // writePrefix()

	/**
	 * Write uuencoding suffix.  Constructed as
	 * " <newline>end<newline>".
	 * @exception IO Exception has occurred
	 */
	private void writeSuffix() throws IOException {

		// Variables
		String	suffix;

		// Construct Suffix
		suffix = " \nend\n";

		// Write Suffix
		out.write(suffix.getBytes());

	} // writeSuffix()


} // UUEncoderStream
