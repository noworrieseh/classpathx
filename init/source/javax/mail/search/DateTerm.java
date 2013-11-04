/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail.search;

// Imports
import java.util.Date;
import javax.mail.Message;

/**
 * Date Term.
 * @author	Andrew Selkirk
 * @version	1.0
 */
public abstract class DateTerm extends ComparisonTerm {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Comparison date.
	 */
	protected	Date	date	= null;


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create a new Date Term.
	 */
	public DateTerm(int comparison, Date date) {
		this.comparison = comparison;
		this.date = date;
	} // DateTerm()


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
	 * @returns Comparison date
	 */
	public Date getDate() {
		return date;
	} // getDate()

	/**
	 * Date comparison match.
	 * @param value Date to check
	 * @returns true if match, false otherwise
	 */
	protected boolean match(Date value) {
		switch (comparison) {
			case LE:
				return (value.getTime() <= date.getTime());
			case LT:
				return (value.getTime() < date.getTime());
			case EQ:
				return (value.getTime() == date.getTime());
			case GT:
				return (value.getTime() > date.getTime());
			case GE:
				return (value.getTime() >= date.getTime());
		} // switch()
		return false;
	} // match()


} // DateTerm
