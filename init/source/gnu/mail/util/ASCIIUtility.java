/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class ASCIIUtility {

	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	private ASCIIUtility() {
	} // ASCIIUtility


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Convert an array of bytes to a String.
	 * @param bytes Byte array to convert
	 * @param offset Offset in byte array to start
	 * @param length Number of bytes to convert to string
	 * @returns New string based on specified bytes
	 */
	public static String toString(byte[] bytes, int offset, int length) {

		// Variables
		char[]		chars;
		int			index;

		// Create Character Array
		chars = new char[length - offset];

		// Convert Bytes to Characters
		for (index = 0; index < (length - offset); index++) {
			chars[index] = (char) bytes[offset + index];
		} // for: index

		return new String(chars);

	} // toString()

	public static String toString(ByteArrayInputStream stream) {
		return null; // TODO
	} // toString()

	public static byte[] getBytes(String string) {

		// Variables
		char[]	chars;
		byte[]	bytes;
		int		index;

		// Convert String to Characters
		chars = string.toCharArray();

		// Convert Characters to Bytes
		bytes = new byte[chars.length];
		for (index = 0; index < chars.length; index++) {
			bytes[index] = (byte) chars[index];
		} // for: index

		// Return Byte Array
		return bytes;

	} // getBytes()

	public static byte[] getBytes(InputStream stream)
			throws IOException {
		return null; // TODO
	} // getBytes()

	public static int parseInt(byte[] bytes, int value1, int value2, int value3)
			throws NumberFormatException {
		return -1; // TODO
	} // parseInt()

	public static int parseInt(byte[] bytes, int value1, int value2)
			throws NumberFormatException {
		return -1; // TODO
	} // parseInt()

	public static long parseLong(byte[] bytes, int value1, int value2, int value3)
			throws NumberFormatException {
		return (long) -1; // TODO
	} // parseLong()

	public static long parseLong(byte[] bytes, int value1, int value2)
			throws NumberFormatException {
		return (long) -1; // TODO
	} // parseLong()


} // ASCIIUtility
