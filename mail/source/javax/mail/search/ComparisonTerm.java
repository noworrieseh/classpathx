/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * Comparison Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class ComparisonTerm extends SearchTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	public static final	int	LE		= 1;
	public static final	int	LT		= 2;
	public static final	int	EQ		= 3;
	public static final	int	NE		= 4;
	public static final	int	GT		= 5;
	public static final	int	GE		= 6;

	protected		int	comparison	= EQ;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Comparison Term.
	 */
	public ComparisonTerm() {
	} // ComparisonTerm()


} // ComparisonTerm
