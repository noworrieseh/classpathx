/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.text.ParseException;

/**
 * Parser
 * @author	Andrew Selkirk
 * @version	1.0
 */
class Parser {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	int		index	= -1;
	char[]	orig	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public Parser(char[] chars) {
		orig = chars;
		index = 0;
	} // Parser()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	int getIndex() {
		return index;
	} // getIndex()

	public int parseAlphaTimeZone() throws ParseException {
		return -1; // TODO
	} // parseAlphaTimeZone()

	public int parseMonth() throws ParseException {
		return -1; // TODO
	} // parseMonth()

	public int parseNumber() throws ParseException {
		return -1; // TODO
	} // parseNumber()

	public int parseNumericTimeZone() throws ParseException {
		return -1; // TODO
	} // parseNumericTimeZone()

	public int parseTimeZone() throws ParseException {
		return -1; // TODO
	} // parseTimeZone()

	public int peekChar() throws ParseException {
		return -1; // TODO
	} // peekChar()

	public void skipChar(char character) throws ParseException {
		// TODO
	} // skipChar()

	public boolean skipIfChar(char character) throws ParseException {
		return false; // TODO
	} // skipIfChar()

	public void skipUntilNumber() throws ParseException {
		// TODO
	} // skipUntilNumber()

	public void skipWhiteSpace() {
		// TODO
	} // skipWhiteSpace()


} // Parser
