/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.*;

/**
 * Flags.
 */
public class Flags implements Cloneable {

	//-------------------------------------------------------------
	// Classes ----------------------------------------------------
	//-------------------------------------------------------------

	public static final class Flag {

		public static final	Flag	ANSWERED= new Flag(ANSWERED_BIT);
		public static final	Flag	DELETED	= new Flag(DELETED_BIT);
		public static final	Flag	DRAFT	= new Flag(DRAFT_BIT);
		public static final	Flag	FLAGGED	= new Flag(FLAGGED_BIT);
		public static final	Flag	RECENT	= new Flag(RECENT_BIT);
		public static final	Flag	SEEN	= new Flag(SEEN_BIT);
		public static final	Flag	USER	= new Flag(USER_BIT);

		/**
		 * Flag bit value
		 */
		private	int	bit	= -1;

		/**
		 * Create new flag
		 * @param bitValue Bit value of Flag
		 */
		private Flag(int bitValue) {
			bit = bitValue;
		} // Flag()


	} // Flag


	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	int			system_flags	= 0;
//	private	Vector		system_flags	= new Vector();
//	private	Hashtable	user_flags		= new Hashtable();
	private	Vector		user_flags		= new Vector();

	private	static final int	ANSWERED_BIT	= 0;
	private	static final int	DELETED_BIT		= 1;
	private	static final int	DRAFT_BIT		= 2;
	private	static final int	FLAGGED_BIT		= 4;
	private	static final int	RECENT_BIT		= 8;
	private	static final int	SEEN_BIT		= 16;
	private	static final int	USER_BIT		= 32;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public Flags() {
	} // Flags()

	public Flags(Flags flags) {
		add(flags);
	} // Flags()

	public Flags(Flag flag) {
		add(flag);
	} // Flags()

	public Flags(String flag) {
		add(flag);
	} // Flags()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public int hashCode() {
		return 0; // TODO
	} // hashCode()

	public boolean equals(Object object) {

		// Check if Object type is Flags
		if (object instanceof Flags) {
			return equals((Flags) object);
		} // if

		// Unknown Type
		return false;

	} // equals()

	public Object clone() {

		// Variables
		Flags	newFlags;

		// Create new Flags
		newFlags = new Flags(this);

		// Return new Clone
		return newFlags;

	} // clone()

	public void add(Flag flag) {

		if (flag == Flag.ANSWERED) {
			system_flags = system_flags | ANSWERED_BIT;
		} else if (flag == Flag.DELETED) {
			system_flags = system_flags | DELETED_BIT;
		} else if (flag == Flag.DRAFT) {
			system_flags = system_flags | DRAFT_BIT;
		} else if (flag == Flag.FLAGGED) {
			system_flags = system_flags | FLAGGED_BIT;
		} else if (flag == Flag.RECENT) {
			system_flags = system_flags | RECENT_BIT;
		} else if (flag == Flag.SEEN) {
			system_flags = system_flags | SEEN_BIT;
		} else if (flag == Flag.USER) {
			system_flags = system_flags | USER_BIT;
		} // if

	} // add()

	public void add(String flag) {

		// Check if User Flag Exists
		if (user_flags.contains(flag) == false) {
			user_flags.addElement(flag);
		} // if

	} // add()

	public void add(Flags flags) {

		// Variables
		Flag[]          systemFlags;
		String[]        userFlags;
		int				index;

		// Get Flags
		systemFlags = flags.getSystemFlags();
		userFlags = flags.getUserFlags();

		// Add System Flags
		for (index = 0; index < systemFlags.length; index++) {
			add(systemFlags[index]);
		} // index

		// Add User Flags
		for (index = 0; index < userFlags.length; index++) {
			add(userFlags[index]);
		}

	} // add()

	public boolean contains(Flag flag) {

		if (flag == Flag.ANSWERED) {
			return (system_flags & ANSWERED_BIT) != 0;
		} else if (flag == Flag.DELETED) {
			return (system_flags & DELETED_BIT) != 0;
		} else if (flag == Flag.DRAFT) {
			return (system_flags & DRAFT_BIT) != 0;
		} else if (flag == Flag.FLAGGED) {
			return (system_flags & FLAGGED_BIT) != 0;
		} else if (flag == Flag.RECENT) {
			return (system_flags & RECENT_BIT) != 0;
		} else if (flag == Flag.SEEN) {
			return (system_flags & SEEN_BIT) != 0;
		} else if (flag == Flag.USER) {
			return (system_flags & USER_BIT) != 0;
		} // if

		// Unknown flag
		return false;

	} // contains()

	public boolean contains(String flag) {
		return user_flags.contains(flag);
	} // contains()

	public boolean equals(Flags flags) {

		// Variables
		Flag[]		systemFlags;
		String[]	userFlags;
		int			index;

		// Get Flag Entries
		systemFlags = flags.getSystemFlags();
		userFlags = flags.getUserFlags();

		// Check # System Flags
		if (systemFlags.length != getSystemFlags().length) {
			return false;
		} // if

		// Check # of User Flags
		if (userFlags.length != getUserFlags().length) {
			return false;
		} // if

		// Check System Flags
		for (index = 0; index < systemFlags.length; index++) {
			if (contains(systemFlags[index]) == false) {
				return false;
			} // if
		} // for

		// Check User Flags
		for (index = 0; index < userFlags.length; index++) {
			if (contains(userFlags[index]) == false) {
				return false;
			} // if
		} // for

		// All tests passed, return true
		return true;

	} // equals()

	public void remove(Flag flag) {

		if (flag == Flag.ANSWERED) {
			system_flags = system_flags ^ ANSWERED_BIT;
		} else if (flag == Flag.DELETED) {
			system_flags = system_flags ^ DELETED_BIT;
		} else if (flag == Flag.DRAFT) {
			system_flags = system_flags ^ DRAFT_BIT;
		} else if (flag == Flag.FLAGGED) {
			system_flags = system_flags ^ FLAGGED_BIT;
		} else if (flag == Flag.RECENT) {
			system_flags = system_flags ^ RECENT_BIT;
		} else if (flag == Flag.SEEN) {
			system_flags = system_flags ^ SEEN_BIT;
		} else if (flag == Flag.USER) {
			system_flags = system_flags ^ USER_BIT;
		} // if

	} // remove()

	public void remove(String flag) {
		user_flags.removeElement(flag);
	} // remove()

	public void remove(Flags flags) {

		// Variables
		Flag[]		systemFlags;
		String[]	userFlags;
		int			index;

		// Get Flags to Remove
		systemFlags = flags.getSystemFlags();
		userFlags = flags.getUserFlags();

		// Remove System Flags
		for (index = 0; index < systemFlags.length; index++) {
			remove(systemFlags[index]);
		} // for

		// Remove User Flags
		for (index = 0; index < userFlags.length; index++) {
			remove(userFlags[index]);
		} // for

	} // remove()

	public Flag[] getSystemFlags() {

		// Variables
		Vector	list;
		int		index;
		Flag[]	flagList;

		// Initialize List
		list = new Vector();

		// Check Flags
		if (contains(Flag.ANSWERED) == true) {
			list.addElement(Flag.ANSWERED);
		} else if (contains(Flag.DELETED) == true) {
			list.addElement(Flag.DELETED);
		} else if (contains(Flag.DRAFT) == true) {
			list.addElement(Flag.DRAFT);
		} else if (contains(Flag.FLAGGED) == true) {
			list.addElement(Flag.FLAGGED);
		} else if (contains(Flag.RECENT) == true) {
			list.addElement(Flag.RECENT);
		} else if (contains(Flag.SEEN) == true) {
			list.addElement(Flag.SEEN);
		} else if (contains(Flag.USER) == true) {
			list.addElement(Flag.USER);
		} // if

		// Create Flag List
		flagList = new Flag[list.size()];
		for (index = 0; index < list.size(); index++) {
			flagList[index] = (Flag) list.elementAt(index);
		} // for

		// Return Flag List
		return flagList;

	} // getSystemFlags()

	public String[] getUserFlags() {

		// Variables
		String[]	results;
		int			index;

		// Create Array
		results = new String[user_flags.size()];
		for (index = 0; index < user_flags.size(); index++) {
			results[index] = (String) user_flags.elementAt(index);
		} // for

		// Return Results
		return results;

	} // getUserFlags()


} // Flags
