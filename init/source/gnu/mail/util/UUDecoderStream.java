/********************************************************************
 * Copyright (c) Open Java Extensions, LGPL License                 *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

/**
 * UU Decoding stream.
 *
 * @author Andrew Selkirk
 * @version 1.0
 * @see java.io.FilterOutputStream
 **/
public class UUDecoderStream extends FilterInputStream {

	// Note: In order to read each line, a DataInputStream is used.
	// This needs to be changed since it is deprecated.  Interesting
	// that Sun uses this in their implement (grep DataInputStream).
	// All we need is an input stream that can identity a '\n' newline.
	// Will possibly create a class to do this if nothing can be
	// found to replace this (reader's are out).  Maybe we can
	// use the LineInputStream in this package?


	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Buffer of bytes to be decoded.
	 */
	private		byte[]	decode_buffer;

	/**
	 * Current number of bytes in buffer to be decoded.
	 */
	private		int		index;

	/**
	 * Decoded buffer.
	 */
	private		byte[]	buffer;

	/**
	 * Current number of bytes in buffer.
	 */
	private		int		bufsize;

	/**
	 * Flag to indicate if prefix read.
	 */
	private		boolean	gotPrefix;

	/**
	 * Name of file to be uudecoded.
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
	 * Create a new UU-Decoding stream.
	 * @param stream Input stream
	 */
	public UUDecoderStream(InputStream stream) {
		super(new DataInputStream(stream));
		this.name = null;
		this.mode = -1;
		gotPrefix = false;
		buffer = new byte[45];
		bufsize = 0;
		decode_buffer = new byte[60];
		index = 0;
	} // UUDecoderStream()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get name from UU-Decoding.
	 * @returns Name of file
	 * @exception IOException IO Exception occurred
	 */
	public String getName() throws IOException {

		// Check for Read Prefix
		if (gotPrefix == false) {
			readPrefix();
		} // if

		// Return Name
		return name;

	} // getName()

	/**
	 * Decode bytes in decode_buffer to buffer for
	 * reading.
	 * @exception IOException IO Exception occurred
	 */
	private void decode() throws IOException {

		// Variables
		int		decode_index;
		int		c1;
		int		c2;
		int		c3;
		int		c4;
		int		a;
		int		b;
		int		c;

		// Process bytes in groups of four
		decode_index = 0;
		index = 0;
		bufsize = 0;
		while (decode_index < decode_buffer.length) {

			// Read in 4 bytes
			c1 = (decode_buffer[decode_index] - ' ') & 0x3f;
			c2 = (decode_buffer[decode_index+1] - ' ') & 0x3f;
			c3 = (decode_buffer[decode_index+2] - ' ') & 0x3f;
			c4 = (decode_buffer[decode_index+3] - ' ') & 0x3f;

			// Decode Bytes
			a = ((c1 << 2) & 0xfc) | ((c2 >>> 4) & 3);
			b = ((c2 << 4) & 0xf0) | ((c3 >>> 2) & 0xf);
			c = ((c3 << 6) & 0xc0) | (c4 & 0x3f);

			// Store Decoded Bytes in read buffer
			buffer[bufsize]   = (byte) (a & 0xff);
			buffer[bufsize+1] = (byte) (b & 0xff);
			buffer[bufsize+2] = (byte) (c & 0xff);

			// Increment Index
			decode_index += 4;
			bufsize += 3;

		} // while()

	} // decode()

	/**
	 * Read byte from decoded buffer.  If buffer empty,
	 * the next line of encoded bytes is read and decoded.
	 * @returns Next byte
 	 * @exception IOException IO Exception occurred
	 */
	public int read() throws IOException {

		// Variables
		int		next;
		int		lengthByte;
		int		length;
		int		decodeLength;
		int		result;
		String	line;

		// Check for Read Prefix
		if (gotPrefix == false) {
			readPrefix();
		} // if

		// Check for Reading in Decoding buffer
		if (index == bufsize) {

			// Read Line
			line = ((DataInputStream) in).readLine();

			// Get Length
			lengthByte = line.getBytes()[0];
			length = (lengthByte - ' ') & 0x3f;

			// Check for Decoded length
			if (length > 45) {
				throw new IOException("UUDecode error: " +
						"line length to large (" + length + ")");
			} // if

			// Check for End
			if (length == 0) {
				readSuffix();
				return -1;
			} // if

			// Get Encoded Line Size
			decodeLength = line.length() - 1;

			// Check for encoded line length multiple of 4
			if ((decodeLength % 4) != 0) {
				throw new IOException("UUDecode error: " +
						"line length not multiple of 4 (" +
						decodeLength + ")");
			} // if

			// Get Encoded Line
			decode_buffer = line.substring(1).getBytes();

			// Decode Buffer
			decode();

		} // if

		// Get Next Byte
		next = buffer[index];

		// Increment Index
		index += 1;

		// Return Byte
		return next;

	} // read()

	/**
	 * Read byte from decoded buffer.  If buffer empty,
	 * the next line of encoded bytes is read and decoded.
	 * @param bytes Byte array to write bytes into
	 * @param offset Offset of array to write
	 * @param length Number of bytes to write
	 * @returns Number of bytes read
 	 * @exception IOException IO Exception occurred
	 */
	public int read(byte[] bytes, int offset, int length)
			throws IOException {

		// Note: This implementation is not as efficient as
		// it could be.  Instead of delegating the work to
		// read(), the bytes should be read in bulk
		// to the buffer and initiating decoding if necessary.
		// This idea does introduce some duplication of code.

		// Variables
		int		index;

		// Write Bytes
		for (index = offset; index < length; index++) {
			bytes[index] = (byte) read();
		} // for: index

		// Return Number of bytes
		return length - offset;

	} // read()

	/**
	 * Return the number of bytes that are available that will
	 * not block.
	 * @returns Number of bytes
	 * @exception IOException IO Exception occurred
	 */
	public int available() throws IOException {

		// Check for Read Prefix
		if (gotPrefix == false) {
			readPrefix();
			read();
			index = 0;
		} // if

		// Calculate # bytes left in current buffer
		return bufsize - index;

	} // available()

	/**
	 * Mark is not supported in UU Decoding.
	 * @returns false
	 */
	public boolean markSupported() {
		return false;
	} // markSupported()

	/**
	 * Get mode from UU-Decoding.
	 * @returns File permission mode
	 * @exception IOException IO Exception occurred
	 */
	public int getMode() throws IOException {

		// Check for Read Prefix
		if (gotPrefix == false) {
			readPrefix();
		} // if

		// Return mode
		return mode;

	} // getMode()

	/**
	 * Read prefix.
	 * @exception IOException IO Exception occurred
	 */
	private void readPrefix() throws IOException {

		// Note: One enhancement for this is to scan lines
		// until we find a 'begin'.  This way, it is fine
		// if there are a number of newlines before the
		// stream begins.

		// Variables
		String			line;

		// Get First line
		line = ((DataInputStream) in).readLine();

		// Check for 'begin'
		if (line.startsWith("begin") == false) {
			throw new IOException("UUDecoder error: No Begin");
		} // if

		// Check for Mode
		try {
			mode = Integer.parseInt(line.substring(6, 9));
		} catch (Exception e) {
			throw new IOException("UUDecoder error: Unable to determine mode");
		} // try

		// Check for name
		try {
			name = line.substring(10);
		} catch (Exception e) {
			throw new IOException("UUDecoder error: Unable to determine name");
		} // try

		// Mark Prefix Read
		gotPrefix = true;

	} // readPrefix()

	/**
	 * Read suffix.
	 * @exception IOException IO Exception occurred
	 */
	private void readSuffix() throws IOException {

		// Variables
		String			line;

		// Get First line
		line = ((DataInputStream) in).readLine();

		// Check for 'end'
		if (line.startsWith("end") == false) {
			throw new IOException("UUDecoder error: No End" + line);
		} // if

	} // readSuffix()


} // UUDecoderStream
