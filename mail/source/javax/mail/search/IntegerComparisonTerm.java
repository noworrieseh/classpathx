/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import javax.mail.Message;

/**
 * Integer Comparison Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class IntegerComparisonTerm extends ComparisonTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Comparison number.
	 */
	protected	int	number	= 0;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Comparison Term.
	 */
	public IntegerComparisonTerm(int comparison, int number) {
		this.comparison = comparison;
		this.number = number;
	} // IntegerComparisonTerm()


	//-------------------------------------------------------------
	// Methods ----------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get comparison operator.
	 * @returns Comparison operator
	 */
	public int getComparison() {
		return comparison;
	} // getComparison()

	/**
	 * Get comparison value.
	 * @returns Comparison value
	 */
	public int getNumber() {
		return number;
	} // getNumber()

	/**
	 * Integer comparison match.
	 * @param value Value to check
	 * @returns true if match, false otherwise
	 */
	protected boolean match(int value) {
		switch (comparison) {
			case LE:
				return (value <= number);
			case LT:
				return (value < number);
			case EQ:
				return (value == number);
			case GT:
				return (value > number);
			case GE:
				return (value >= number);
		} // switch()
		return false;
	} // match()


} // IntegerComparisonTerm
