/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.*;

/**
 * Flag Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public final class FlagTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	protected	Flags	flags;
	protected	boolean	set;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Flags Term.
	 * @param flags Flags to check for
	 * @param set Whether to check for set/unset
	 */
	public FlagTerm(Flags flags, boolean set) {
		this.flags = flags;
		this.set = set;
	} // FlagTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	public Flags getFlags() {
		return flags;
	} // getFlags()

	public boolean getTestSet() {
		return set;
	} // getTestSet()

	/**
	 * Do Message match.
	 * @param Address Message to check
	 * @returns true if match, false otherwise
	 */
	public boolean match(Message message) {

		// Variables
		Flags.Flag[]	systemFlags;
		String[]		userFlags;
		Flags			messageFlags;
		int				index;

		try {

			// Get Message Flags
			messageFlags = message.getFlags();
			systemFlags = flags.getSystemFlags();
			userFlags = flags.getUserFlags();

		} catch (MessagingException e) {
			return false;
		} // try

		// Process System Flags
		for (index = 0; index < systemFlags.length; index++) {
			if (messageFlags.contains(systemFlags[index]) != set) {
				return false;
			} // if
		} // for

		// Process User Flags
		for (index = 0; index < userFlags.length; index++) {
			if (messageFlags.contains(userFlags[index]) != set) {
				return false;
			} // if
		} // for

		// Flags Match
		return true;

	} // match()


} // FlagTerm
