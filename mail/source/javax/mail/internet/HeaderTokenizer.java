/********************************************************************
 * Copyright (c) Open Java Extensions, LGPL License                 *
 ********************************************************************/

package javax.mail.internet;

// Imports
import javax.mail.internet.ParseException;

/**
 * Tokenizes a <code>String</code> into the basic tokens of
 * RFC822 or MIME formatted headers.
 *
 * @author Joey Lesh
 * @author Andrew Selkirk
 * @version 1.0
 */
public class HeaderTokenizer {

	// +--------+----------------------------------------------
	// | Fields |
	// +--------+

	/**
	 * RFC822 special characters. They delimit atoms.
	 */
	public static final String RFC822 = "()<>@,;:\\\".[]";
	
	/**
	 * The MIME special characters. 
	 */
	public static final String MIME = "()<>@,;:\\\"[]/?";

	/**
	 * Token that represents the end of input.
	 */
	private	static final Token EOFToken =
					new Token(Token.EOF, ""); // CHECK Value

	/**
	 * This the header we're parsing.
	 */
	private String string;
	
	/**
	 * These are the delimiters to use during the parsing.
	 */
	private String delimiters;

	/**
	 * True if you are to not tokenize and return the comments.
	 */
	private boolean skipComments;

	/**
	 * Current position in parsing.
	 */
	private int		currentPos;

	/**
	 * End of input position.
	 */
	private	int		maxPos;

	/**
	 * Position of next token in input.
	 */
	private	int		nextPos;

	/**
	 * Position of next peek token in input.
	 */
	private	int		peekPos;


	// +--------------+-------------------------------------------
	// | Constructors |
	// +--------------+

	/**
	 * Constructs a <code>HeaderTokenizer</code> that will use the parameter
	 * as an RFC822 header when it will tokenize it. It also does not return
	 * comments as tokens.
	 *
	 * @param header The RFC822 header to tokenize.
	 */
	public HeaderTokenizer(String header) {
		this(header, RFC822, true);
	}// HeaderTokenizer(String)

	/**
	 * Constructs a <code>HeaderTokenizer</code> that will use the
	 * <code>delimiters</code> when tokenizing <code>header</code>.
	 * It also skips comments.
	 *
	 * @param header The <code>String</code> to be turned into tokens.
	 * @param delimiters A <code>String<code> that contains the characters
	 *                   that delimit atoms.
	 */
	public HeaderTokenizer(String header, String delimiters) {
		this(header, delimiters, true);
	}// HeaderTokenizer(String, String)


	/**
	 * Constructs a <code>HeaderTokenizer</code> that will use the
	 * <code>delimiters</code> when tokenizing <code>header</code>.
	 * Allows you to specify whether or not to skip comments.
	 *
	 * @param header The <code>String</code> to be turned into tokens.
	 * @param delimiters A <code>String<code> that contains the characters
	 *                   that delimit atoms.
	 * @param skipComments When <code>true</code> no comments are returned
	 *            as tokens. When <code>false</code> it will return them.
	 */
	public HeaderTokenizer(String header, String delimiters, boolean skipComments) {
		this.skipComments = skipComments;
		this.string = header;
		this.delimiters = delimiters;
		this.currentPos = 0;
		this.nextPos = 0;
		this.peekPos = 0;
		this.maxPos = string.length();
	}// HeaderTokenizer(String, String, boolean)


	// +----------------+--------------------------------------------
	// | Public Methods |
	// +----------------+

	/**
	 * Gets the next <code>HeaderTokenizer.Token</code>.
	 *
	 * @return The next <code>HeaderTokenizer.Token</code> in the parsing
	 * stream.
	 */
	public HeaderTokenizer.Token next() throws ParseException {

		// Variables
		Token	nextToken;

		// Set the Cursor to the Next Position
		currentPos = nextPos;

		do {
			// Get Next Token
			nextToken = getNext();

		} while (skipComments == true &&
					nextToken.getType() == Token.COMMENT);

		// Set the Next Position to the Current
		nextPos = currentPos;

		// Reset Peek Position to the Current position as well
		peekPos = currentPos;

		// Return Token
		return nextToken;

	}// next()

	/**
	 * Gets the next <code>HeaderTokenizer.Token</code> without moving the
	 * parsing stream forward. A subsequent call to <code>next()</code> will
	 * return the same token as that from the previous call to
	 * <code>peek()</code>.
	 *
	 * @return The next <code>HeaderTokenizer.Token</code> in the parsing
	 * stream.
	 */
	public HeaderTokenizer.Token peek() throws ParseException {

		// Variables
		Token	peekToken;

		// Set the Cursor to the Peek Position
		currentPos = nextPos;

		do {

			// Get Peek Token
			peekToken = getNext();

		} while (skipComments == true &&
					peekToken.getType() == Token.COMMENT);

		// Set the Peek Position to the Current
		peekPos = currentPos;

		// Return Token
		return peekToken;

	}// peek()

	/**
	 * Gets the rest of the unparsed data and is returned as a
	 * <code>String</code>.
	 *
	 * @return The unparsed data.
	 */
	public String getRemainder() {

		// Variables
		String	result;
		// Check for end-of-input
		if (currentPos == maxPos) {
			return null;

		// Return remaining string
		// Note: currently sets the current position to the end
		// Is this command supposed to consume the remaining tokens?
		// If not, the position adjustments should be removed
		} else {
			result = string.substring(currentPos);
			nextPos = maxPos;
			peekPos = maxPos;
			return result;
		} // if

	}// getRemainder()

	private static String filterToken(String token, int value1,
			int value2) {
		return null; // TODO
	} // filterToken()

	private HeaderTokenizer.Token getNext() throws ParseException {

// NOTE: Implementation does not incorporate the delimiters
// as of yet.  Uncertain if this is a getNext() or skipWhitespace
// issue.

		// Variables
		int		position;
		Token	nextToken;
		char	nextChar;

		// Check For End
		if (currentPos == maxPos) {
			return EOFToken;
		} // if

		// Skip any whitespace
		currentPos = skipWhiteSpace();
		if (currentPos == maxPos) {
			return EOFToken;
		} // if

		// Check for Comment
		if (string.charAt(currentPos) == '(') {

			// Check for End of comment
			position = string.indexOf(")", currentPos + 1);
			if (position == -1) {
				throw new ParseException("Unbalanced comments");
			} // if

			// Create Token
			nextToken = new Token(Token.COMMENT,
						string.substring(currentPos + 1, position));

			// Set new Position
			currentPos = position;

		// Check for Quoted String
		} else if (string.charAt(currentPos) == '\"') {

			// Check for End of quoted string
			position = string.indexOf("\"", currentPos + 1);
			if (position == -1) {
				throw new ParseException("Unbalanced quoted string");
			} // if

			// Create Token
			nextToken = new Token(Token.QUOTEDSTRING,
						string.substring(currentPos + 1, position));

			// Set new Position
			currentPos = position;

		// Check for Atom
		} else {
			// Process Characters
			position = currentPos;
			nextChar = string.charAt(position);
			while (position < maxPos && nextChar > 32 && nextChar <= 127) {
				position += 1;
				nextChar = string.charAt(position);
			} // while

			// Create Token
			nextToken = new Token(Token.ATOM,
						string.substring(currentPos + 1, position));

			// Set new Position
			currentPos = position;

		} // if

		// Return Next Token
		return nextToken;

	} // getNext()

	private int skipWhiteSpace() {

		// Variables
		int		position;
		char	nextChar;

		// Initialize to Current Position
		position = currentPos;

		// Process Characters
		nextChar = string.charAt(position);
		while (position < maxPos && (nextChar == 32 || nextChar == 9 ||
				nextChar == 13 || nextChar == 10)) {
			position += 1;
			nextChar = string.charAt(position);
		} // while

		// Return New Position
		return position;

	} // skipWhiteSpace()


	// +----------------+--------------------------------------------
	// | Internal Class |
	// +----------------+

	/**
	 * Represents a Token. Has a type and value. Type is atom, comment,
	 * eof, or quoted string.
	 *
	 * @author Joey Lesh
	 */
	public static class Token {

		/**
		 * Type of Token.
		 */
		private int type;

		/**
		 * Value of token.
		 */
		private String value;

		/*
		 * Token type that represents an ATOM.
		 */
		public static final int ATOM		= 0;

		/*
		 * Token type that represents a comment.
		 */
		public static final int COMMENT		= 1;

		/*
		 * Token type indicating that the end of the
		 * data has been reached.
		 */
		public static final int EOF			= 2;

		/*
		 * Token type representing a QUOTEDSTRING.
		 */
		public static final int QUOTEDSTRING= 3;

		/**
		 * Creates a new <code>Token</code>. The <code>value</code> parameter
		 * should take its value from the static fields provided. For example,
		 * <pre>
		 * new HeaderTokenizer.Token(HeaderTokenizer.Token.ATOM, "John")
		 *</pre>
		 *
		 * @param type The Token type, value should be ATOM, COMMENT, EOF,
		 *         or QUOTEDSTRING.
		 * @param value The value of the Token.
		 */
		public Token(int type, String value) {
			this.type = type;
			this.value = value;
		} // Token()

		/**
		 * Get Type of token.
		 * @returns Token type
		 */
		public int getType() {
			return this.type;
		}// getType()

		/**
		 * Get Value of token.
		 * @returns Token value
		 */
		public String getValue() {
			return this.value;
		}// getvalue()


	}// Token


}// HeaderTokenizer
