/*
 * IMAPConstants.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gnu.mail.providers.imap4;

/**
 * IMAP4rev1 string constants.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public interface IMAPConstants
{

  // Client commands
  public static final String LOGIN = "LOGIN".intern();
  public static final String LOGOUT = "LOGOUT".intern();
  public static final String SELECT = "SELECT".intern();
  public static final String EXAMINE = "EXAMINE".intern();
  public static final String CREATE = "CREATE".intern();
  public static final String DELETE = "DELETE".intern();
  public static final String RENAME = "RENAME".intern();
  public static final String SUBSCRIBE = "SUBSCRIBE".intern();
  public static final String UNSUBSCRIBE = "UNSUBSCRIBE".intern();
  public static final String LIST = "LIST".intern();
  public static final String LSUB = "LSUB".intern();
  public static final String STATUS = "STATUS".intern();
  public static final String APPEND = "APPEND".intern();
  public static final String CHECK = "CHECK".intern();
  public static final String CLOSE = "CLOSE".intern();
  public static final String EXPUNGE = "EXPUNGE".intern();
  public static final String SEARCH = "SEARCH".intern();
  public static final String FETCH = "FETCH".intern();
  public static final String STORE = "STORE".intern();
  public static final String COPY = "COPY".intern();
  
  // Server responses
  public static final String OK = "OK".intern();
  public static final String NO = "NO".intern();
  public static final String BAD = "BAD".intern();
  public static final String PREAUTH = "PREAUTH".intern();
  public static final String BYE = "BYE".intern();

  // Response codes
  public static final String ALERT = "ALERT".intern();
  public static final String NEWNAME = "NEWNAME".intern();
  public static final String PARSE = "PARSE".intern();
  public static final String PERMANENTFLAGS = "PERMANENTFLAGS".intern();
  public static final String READ_ONLY = "READ-ONLY".intern();
  public static final String READ_WRITE = "READ-WRITE".intern();
  public static final String TRYCREATE = "TRYCREATE".intern();
  public static final String UIDVALIDITY = "UIDVALIDITY".intern();
  public static final String UNSEEN = "UNSEEN".intern();

  // Select responses
  public static final String FLAGS = "FLAGS".intern();
  public static final String EXISTS = "EXISTS".intern();
  public static final String RECENT = "RECENT".intern();
  public static final String FETCH_FLAGS = "FETCH FLAGS".intern();

  // Status items
  public static final String MESSAGES = "MESSAGES".intern();
  public static final String UIDNEXT = "UIDNEXT".intern();

  // List responses
  public static final String LIST_NOINFERIORS = "\\Noinferiors".intern();
  public static final String LIST_NOSELECT = "\\Noselect".intern();
  public static final String LIST_MARKED = "\\Marked".intern();
  public static final String LIST_UNMARKED = "\\Unmarked".intern();

  // Flags
  public static final String FLAG_SEEN = "\\Seen".intern();
  public static final String FLAG_ANSWERED = "\\Answered".intern();
  public static final String FLAG_FLAGGED = "\\Flagged".intern();
  public static final String FLAG_DELETED = "\\Deleted".intern();
  public static final String FLAG_DRAFT = "\\Draft".intern();
  public static final String FLAG_RECENT = "\\Recent".intern();

  // Fetch data items
  public static final String BODY = "BODY".intern();
  public static final String BODY_PEEK = "BODY.PEEK".intern();
  public static final String BODYHEADER = "BODYHEADER".intern();
  public static final String BODYSTRUCTURE = "BODYSTRUCTURE".intern();
  public static final String ENVELOPE = "ENVELOPE".intern();
  public static final String INTERNALDATE = "INTERNALDATE".intern();
  public static final String RFC822 = "RFC822".intern();
  public static final String RFC822_HEADER = "RFC822.HEADER".intern();
  public static final String RFC822_SIZE = "RFC822.SIZE".intern();
  public static final String RFC822_TEXT = "RFC822.TEXT".intern();
  
  // NIL
  public static final String NIL = "NIL".intern();

}
