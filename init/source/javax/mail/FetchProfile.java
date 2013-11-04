/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.*;

/**
 * Fetch Profile
 */
public class FetchProfile {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	Vector	specials	= null;
	private	Vector	headers		= null;

	public static class Item {
		public static final Item ENVELOPE	= new Item("ENVELOPE");
		public static final Item CONTENT_INFO	= new Item("CONTENT_INFO");
		public static final Item FLAGS		= new Item("FLAGS");

		private	String		name	= null;

		protected Item(String value) {
			name = value;
		} // Item()


	} // Item


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Fetch Profile.
	 */
	public FetchProfile() {
		specials = new Vector();
		headers = new Vector();
	} // FetchProfile()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Add special item to the fetch profile.
	 * @param item Special item to be fetched
	 */
	public void add(Item item) {
		specials.addElement(item);
	} // add()

	/**
	 * Add header name to the fetch profile.
	 * @param headerName Header name to be prefetched
	 */
	public void add(String headerName) {
		headers.addElement(headerName);
	} // add()

	/**
	 * Check if fetch profile contains the special item.
	 * @param item Special item to check for
	 * @returns true if exists, false otherwise
	 */
	public boolean contains(Item item) {
		return specials.contains(item);
	} // contains()

	/**
	 * Check if fetch profile contains the header name.
	 * @param headerName Name of header to check
	 * @returns true if exists, false otherwise
	 */
	public boolean contains(String headerName) {
		return headers.contains(headerName);
	} // contains()

	/**
	 * Get list of header names in fetch profile.
	 * @returns List of header names
	 */
	public String[] getHeaderNames() {

		// Variables
		String[]	list;
		int		index;

		// Create Item List
		list = new String[headers.size()];

		// Populate list
		for (index = 0; index < headers.size(); index++) {
			list[index] = (String) headers.elementAt(index);
		} // for: index

		// Return List
		return list;

	} // getHeaderNames()

	/**
	 * Get list of special items in fetch profile.
	 * @returns List of special items
	 */
	public Item[] getItems() {

		// Variables
		Item[]	list;
		int	index;

		// Create Item List
		list = new Item[specials.size()];

		// Populate list
		for (index = 0; index < specials.size(); index++) {
			list[index] = (Item) specials.elementAt(index);
		} // for: index

		// Return List
		return list;

	} // getItems()


} // FetchProfile
