/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.internet;

// Imports
import javax.mail.MessagingException;
import java.util.Enumeration;
import java.util.Vector;
import java.io.InputStream;
import java.io.IOException;
import oje.mail.util.LineInputStream;

/**
 * Models an RFC822-style header.
 *
 * @author Joey Lesh
 * @author Andrew Selkirk
 * @version 1.0
 */
public class InternetHeaders {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Header container.
	 */
	private	Vector	headers	= new Vector();


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Creates a default header object without anything in it.
	 */
	public InternetHeaders() {
	}// InternetHeaders()

	public InternetHeaders(InputStream is) throws MessagingException {

		// Load Input Stream
		load(is);

	}// InternetHeaders(InputStream)


	// +---------+----------------------------------
	// | Methods |
	// +---------+

	/**
	 * Add a header to the list.
	 * @param name Header name
	 * @param value Header value
	 */
	public void addHeader(String name, String value) {
		headers.addElement(new hdr(name, value));
	}// addHeader(String, String)

	/**
	 * Add header line to list of headers.  If header begins with
	 * a SPACE or HTAB as in RFC822, then the line is appended to
	 * the value of the last added header.
	 * @param line Line to add
	 */
	public void addHeaderLine(String line)  {

		// Variables
		hdr	last;

		// Check First character for append
		if (line.charAt(0) == 32 || line.charAt(0) == 9) {
			last = (hdr) headers.lastElement();
			headers.removeElementAt(headers.size());
			headers.addElement(new hdr(last.getName(), last.getValue() + line));
		} else {
			headers.addElement(new hdr(line));
		} // if

	}// addHeaderLine(String)

	/**
	 * Get an enumeration of all the header lines.
	 * @returns Enumeration of all header lines
	 */
	public Enumeration getAllHeaderLines()  {
		return new matchEnum(headers, null, false, true);
	}// getAllHeaderLines()

	/**
	 * Get an enumeration of all the header objects.
	 * @returns Enumeration of all header lines
	 */
	public Enumeration getAllHeaders()  {
		return headers.elements();
	}// getAllHeaders()

	/**
	 * Return array of all values for the specified header name.
	 * @param name Header name
	 * @returns Array of values
	 */
	public String[] getHeader(String name) {

		// Variables
		Vector		matching;
		hdr			current;
		int			index;
		String[]	list;

		// Process All Headers
		matching = new Vector();
		for (index = 0; index < headers.size(); index++) {

			// Get Header
			current = (hdr) headers.get(index);

			// Check if Name matches
			if (current.getName().equals(name) == true) {
				matching.addElement(current);
			} // if

		} // for: index

		// Create Array Listing
		list = new String[matching.size()];
		for (index = 0; index < matching.size(); index++) {
			list[index] = ((hdr) matching.get(index)).getValue();
		} // for: index

		// Return List
		return list;

	}// getHeader(String)

	/**
	 * Get list of header lines as one string which is delimited
	 * by the specified characters.  If the delimiter is null,
	 * return only the first entry.
	 * @param name Header name
	 * @param delimiter Delimiter
	 * @returns Header set
	 */
	public String getHeader(String name, String delimiter)  {

		// Variables
		String[]		list;
		StringBuffer	buffer;
		int				index;

		// Get All Header Values
		list = getHeader(name);

		// Check Delimiter
		if (delimiter == null) {
			if (list.length > 0) {
				return name + ": " + list[0];
			} else {
				return "";
			}
		} // if

		// Construct Entire Result
		buffer = new StringBuffer();
		for (index = 0; index < list.length; index++) {
			buffer.append(name + ": ");
			buffer.append(list[index]);
			buffer.append(delimiter);
		} // for: index

		// Return new List
		return buffer.toString();

	}// getHeader(String, String)

	/**
	 * Get an enumeration of all the headers lines that match
	 * the specified header names.
	 * @param names Array of header names
	 * @returns Enumeration of resulting header lines
	 */
	public Enumeration getMatchingHeaderLines(String[] names)  {
		return new matchEnum(headers, names, true, true);
	}// getMatchingHeaderLines(String[])

	/**
	 * Get an enumeration of all the headers that match
	 * the specified header names.
	 * @param names Array of header names
	 * @returns Enumeration of resulting header lines
	 */
	public Enumeration getMatchingHeaders(String[] names)  {
		return new matchEnum(headers, names, true, false);
	}// getMatchingHeaders(String[])

	/**
	 * Get an enumeration of all the headers lines that do not
	 * match the specified header names.
	 * @param names Array of header names
	 * @returns Enumeration of resulting header lines
	 */
	public Enumeration getNonMatchingHeaderLines(String[] names)  {
		return new matchEnum(headers, names, false, true);
	}// getNonMatchingHeaderLines(String[])

	/**
	 * Get an enumeration of all the headers that do not
	 * match the specified header names.
	 * @param names Array of header names
	 * @returns Enumeration of resulting header lines
	 */
	public Enumeration getNonMatchingHeaders(String[] names)  {
		return new matchEnum(headers, names, false, false);
	}// getNonMatchingHeaders(String[])

	/**
	 * Load header lines from an input stream.  Headers are added
	 * to the currently existing header listing.
	 * @param is Input stream
	 */
	public void load(InputStream is) throws MessagingException {

		// Variables
		LineInputStream	input;
		String			line;

		try {

			// Process Each Line
			input = new LineInputStream(is);
			line = input.readLine();
			while (line.equals("") == false) {
				addHeaderLine(line);
				line = input.readLine();
			} // while

		} catch (IOException e) {
			throw new MessagingException("Error in input stream");
		} // try

	}// load

	/**
	 * Remove all headers that match the specified header name.
	 * @param name Header name
	 */
	public void removeHeader(String name) {

		// Variables
		Vector		list;
		hdr			current;
		int			index;

		// Process All Headers
		list = new Vector();
		for (index = 0; index < headers.size(); index++) {

			// Get Header
			current = (hdr) headers.get(index);

			// Check if Name matches
			if (current.getName().equals(name) == false) {
				list.addElement(current);
			} // if

		} // for: index

		// Set List as new Header List
		headers = list;

	}// removeHeader(String)

	/**
	 * Modify the first header that matches with the
	 * specified value.  All other instances of the header
	 * name are removed.  If the header is not found, it
	 * is added.
	 * @param name Header name
	 * @param value Header value
	 */
	public void setHeader(String name, String value)  {

		// Variables
		hdr			current;
		int			index;
		boolean		foundFirst;
		Vector		list;

		// Process All Headers
		foundFirst = false;
		list = new Vector();
		for (index = 0; index < headers.size(); index++) {

			// Get Header
			current = (hdr) headers.get(index);

			// Check if Name matches
			if (current.getName().equals(name) == true) {
				if (foundFirst == false) {
					list.addElement(new hdr(name, value));
					foundFirst = true;
				} // if
			} else {
				list.addElement(current);
			} // if

		} // for: index

		// Check if Name was found
		if (foundFirst == false) {
			list.addElement(new hdr(name, value));
		} // if

		// Set new list
		headers = list;

	}// setHeader(String, String)


} // InternetHeaders
