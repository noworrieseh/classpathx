/* MimeMultipart.java  */

/* Liscense here */

package javax.mail.internet;

import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.activation.DataSource;
import java.io.OutputStream;
import java.util.Enumeration;


/**
 * This is a class. :)
 *
 *
 * @author Joey Lesh
 */
public class MimeMultipart extends Multipart {

	/* Hmm */
	protected DataSource ds;
	
	/*      */
	protected boolean parsed;

	// +--------------+------------------------------------------
	// | Constructors |
	// +--------------+

	MimeMultipart() {
		super();
		parts = null;
	}// MimeMulitpart()
	
	MimeMultipart(DataSource ds) {
		if (ds intanceof MultipartDataSource)
			super((MultipartDataSource) ds);
		
	}// MimeMultipart(DataSource)

	MimeMultipart(String subtype) {
		super();
		this.contentType = "multipart/"+subtype;
	}// MimeMultipart(String)

	// +---------+---------------------------------------
	// | Methods |
	// +---------+

	/**
	 * Sets the subtype. The default is "mixed."
	 *
	 * @param subtype The new value of the subtype.
	 */
	public void setSubType(String subtype) throws MessagingException {
		this.contentType = "multipart/" +subtype;
	}// setSubType(String)

	/**
	 * Gets the number of elements in this instance of 
	 * <code>MimeMultipart</code>
	 *
	 * @return The number of parts in <code>this</code>.
	 */
	public int getCount() {
		// parts is a java.util.Vector of parts. It is inherited from
		// Multipart
		return this.parts.size();
	}// getCount()

	/**
	 * The the BodyPart at <code>index</code>  Numbered from 0.
	 *
	 * @param index The index of the <code>BodyPart</code>
	 * @return The <code>BodyPart</code>
	 * @exception Throws a <code>MessagingException</code> if the 
	 *  part does not exist.
	 */
	public BodyPart getBodyPart(int index) throws MessagingException {
		BodyPart part = null;
		try {
			part = (BodyPart) this.parts.elementAt(index);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new MessagingException("Index is out of the range for this object. Indices must adhere to these rules: 0 <= index < this.getCount()", e);
		}
		return part;
	}// getBodyPart(int)
	
	/**
	 * Gets the <code>BodyPart</code> that has a Content-ID header with the
	 * value of <code>CID</code>.  Returns null if no <code>BodyPart</code>
	 * with that CID could be found. See RFC 2046 for more information about
	 * the CID. 
	 *
	 * @param CID The value of the Content-ID header that is in the 
	 *             <code>BodyPart</code> that we're trying to get.
	 * @return A <code>BodyPart</code> or null. 
	 * @exception Throws one if the API programmer messed up the boundary 
	 *            conditions.
	 */
	public BodyPart getBodyPart(String CID) throws MessagingException {
		// First of all if we're empty, be done now.
		if (this.parts.isEmpty())
			return null;
		
		// Alright, there are elements in this.parts, let's get set up.
		int i;
		MimeBodyPart part;
		
		// Look through the MimeBodyParts for one with a
		// matching Content-ID. Content-ID is supposed to be unique so, we
		// return whatever we find.
		for (i = 0; i < this.getCount(); i++) {
			part = (MimeBodyPart) this.getBodyPart(i);
			if (part.getContentID().equals(CID))
				return part;
		}
		return null;
	}// getBodyPart(String)

	/**
	 * Updates all the headers for the parts contained in this. Done as part
	 * of <code>saveChanges</code>
	 */
	public void updateHeaders() {
		int i;
		for (i = 0; i < this.getCount(); i++) {
			((MimeBodyPart) parts.elementAt(i)).updateHeaders();
		}
	}// updateHeaders() 

	public void writeTo(OutputStream out) {
		//TO DO
		// Need to learn about how to write out MIME boundaries.
	}// writeTo(OutputStream

		
}// MimeMultipart




