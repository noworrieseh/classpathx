/* MimePartDataSource.java */

/* Liscense goes here. */

package javax.mail.internet;

import javax.mail.MessageAware;
import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Joey Lesh
 */
public class MimePartDataSource implements DataSource, MessageAware {
	
  MimeBodyPart part;

  // +-------------+----------------------------------------
  // | Constructor |
  // +-------------+

  /**
   *
   */
  public MimePartDataSource(MimePart part) {
    //  Make a data source out of the MimePart.
    this.part = part;
  }// MimePartDataSource(MimePart)

  // +---------+------------------------------------------
  // | Methods |
  // +---------+

  /**
   *
   */
  public InputStream getInputStream() throws IOException {
    return MimeUtility.decode(part.getContentStream(), part.getEncoding());
  }// getInputStream()
	
  /**
   *
   */
  public OutputStream getOutputStream() throws IOException {
    // The API spec said that it's implementation throws this.
    throw UnknownServiceException;
    return null;
  }

  /**
   * Gets the content type from the <code>MimePart.</code>
   */
  public String getContentType() {
    return part.getContentType();
  }// getContentType()
	
  /**
   * Returns the name. This implementation has the name as and empty string.
   *
   * @return An empty String.
   */
  public String getName() {
    return "";
  }// getName()
		
  /**
   * Gets the MessageContext for this part.
   *
   * @return The <code>MessageContext</code>
   * @since JavaMail 1.1
   */
  public MessageContext getMessageContext() {
    return new MessageContext(part);
  }// getMessageContext()
}// MimePartDataSource







