/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package oje.mail.util;

// Imports
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class MailDateFormat extends SimpleDateFormat {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	static			boolean 	debug	= false;
	private static	TimeZone	tz		= null;
	private static	Calendar	cal		= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public MailDateFormat() {
		super("EEE,dd MMM yyyy HH:mm:ss z");
	} // MailDateFormat()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public StringBuffer format(Date date, StringBuffer buffer, FieldPosition position) {
		return null; // TODO
	} // format()

	public Date parse(String text, ParsePosition position) {
		return null; // TODO
	} // parse()

	private static synchronized Date ourUTC(int value1, int value2, int value3, int value4, int value5, int value6, int value7) {
		return null; // TODO
	} // ourUTC()

	private static Date parseDate(char[] chars, ParsePosition position) {
		return null; // TODO
	} // parseDate()

	public void setCalendar(Calendar cal) {
		this.cal = cal;
	} // setCalendar()

	public void setNumberFormat(NumberFormat format) {
	} // setNumberFormat()


} // MailDateFormat
