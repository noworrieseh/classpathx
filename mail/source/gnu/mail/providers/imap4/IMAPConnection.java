/*
 * IMAPConnection.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package gnu.mail.providers.imap4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The protocol class implementing IMAP4rev1.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPConnection implements IMAPConstants
{

  // -- TESTING
  public static void main(String[] args) {
    try {
      IMAPConnection c = new IMAPConnection("localhost", 143);
      c.setDebug(true);
      // login
      if (!c.login("test", "imaptest")) {
        System.err.println("bad login");
        c.logout();
        System.exit(1);
      }
      // list
      ListEntry[] le = c.list(null, "*");
      for (int i=0; i<le.length; i++) {
        System.out.println(le[i]);
      }
      // select INBOX
      MailboxStatus fs = c.select("INBOX");
      System.out.println("INBOX has "+fs.messageCount+" messages");
      // fetch message 2
      int[] messages = new int[] { 2 };
      String[] fetchCommands = new String[] { "BODY.PEEK[]" };
      MessageStatus[] ms = c.fetch(messages, fetchCommands);
      for (int i=0; i<ms.length; i++) {
        System.out.println("Message "+ms[i].messageNumber+" properties:");
        System.out.println(ms[i].properties);
      }
      // close
      c.close();
      // logout
      c.logout();
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  } // -- END TESTING

  // Prefix for tags
  protected static final String TAG_PREFIX = "A";

  protected static final String DEFAULT_ENCODING = "US-ASCII";

  /**
   * The socket used for communication with the server.
   */
  protected Socket socket;

  /**
   * The tokenizer used to read IMAP responses from.
   */
  protected IMAPResponseTokenizer in;

  /**
   * The output stream.
   */
  protected OutputStream out;

  /*
   * Used to generate new tags for tagged commands.
   */
  private int tagIndex = 0;

  /*
   * Print debugging output to stderr.
   */
  private boolean debug = false;

  /**
   * Constructor.
   */
  public IMAPConnection(String host, int port)
    throws UnknownHostException, IOException
  {
    socket = new Socket(host, port);
    in = new IMAPResponseTokenizer(socket.getInputStream());
    out = socket.getOutputStream();
  }

  /**
   * Sets whether to log debugging output to stderr.
   */
  public void setDebug(boolean flag)
  {
    debug = flag;
  }

  /**
   * Returns a new tag for a command.
   */
  protected String newTag() {
    return new StringBuffer(TAG_PREFIX).append(++tagIndex).toString();
  }

  /**
   * Sends the specified IMAP tagged command to the server.
   */
  protected void sendCommand(String tag, String command)
    throws IOException
  {
    if (debug)
      System.err.println("> "+tag+" "+command);
    byte[] bytes = new StringBuffer(tag)
      .append(' ')
      .append(command)
      .append('\r')
      .append('\n')
      .toString()
      .getBytes(DEFAULT_ENCODING);
    out.write(bytes);
  }

  /**
   * Sends the specified IMAP command.
   * @param command the command
   * @return true if OK was received, or false if NO was received
   * @exception IOException if BAD was received or an I/O error occurred
   */
  protected boolean invokeSimpleCommand(String command)
    throws IOException
  {
    String tag = newTag();
    sendCommand(tag, command);
    while (true)
    {
      IMAPResponse response = readResponse();
      if (response.isTagged()) {
        String id = response.getID();
        if (id==OK)
          return true;
        else if (id==NO)
          return false;
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }


  /**
   * Reads an IMAP response from the server.
   * The response will consist of <i>either</i>:
   * <ul>
   * <li>A tagged response corresponding to a pending command</li>
   * <li>An untagged error response</li>
   * <li>A continuation response</li>
   */
  protected IMAPResponse readResponse()
    throws IOException
  {
    IMAPResponse response = in.next();
    if (debug)
      System.err.println("< "+response.toString());
    return response;
  }

  /**
   * Login to the connection using the username and password method.
   * @param username the authentication principal
   * @param password the authentication credentials
   * @return true if authentication was successful, false otherwise
   */
  public boolean login(String username, String password)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(LOGIN)
        .append(' ')
        .append(username)
        .append(' ')
        .append(password)
        .toString());
  }

  /**
   * Logout this connection.
   * Underlying network resources will be freed.
   */
  public void logout()
    throws IOException
  {
    String tag = newTag();
    sendCommand(tag, LOGOUT);
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (id==OK)
      {
        socket.close();
        return;
      }
    }
  }

  /**
   * Selects the specified mailbox.
   * The mailbox is identified as read-write if writes are permitted.
   * @param mailbox the mailbox name
   * @return a MailboxStatus containing the state of the selected mailbox
   */
  public MailboxStatus select(String mailbox)
    throws IOException
  {
    return selectImpl(mailbox, SELECT);
  }

  /**
   * Selects the specified mailbox.
   * The mailbox is identified as read-only.
   * @param mailbox the mailbox name
   * @return a MailboxStatus containing the state of the selected mailbox
   */
  public MailboxStatus examine(String mailbox)
    throws IOException
  {
    return selectImpl(mailbox, EXAMINE);
  }

  protected MailboxStatus selectImpl(String mailbox, String command)
    throws IOException
  {
    String tag = newTag();
    sendCommand(tag, new StringBuffer(command)
        .append(' ')
        .append(mailbox)
        .toString());
    MailboxStatus ms = new MailboxStatus();
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        if (id==OK)
        {
          List rc = response.getResponseCode();
          int len = rc.size();
          for (int i=0; i<len; i++)
          {
            Object ocmd = rc.get(i);
            if (ocmd instanceof String)
            {
              String cmd = (String)ocmd;
              if (i+1<len)
              {
                Object oparam = rc.get(i+1);
                if (oparam instanceof String)
                {
                  String param = (String)oparam;
                  try
                  {
                    if (cmd==UNSEEN)
                    {
                      ms.firstUnreadMessage = Integer.parseInt(param);
                      i++;
                    }
                    else if (cmd==UIDVALIDITY)
                    {
                      ms.uidValidity = Integer.parseInt(param);
                      i++;
                    }
                  }
                  catch (NumberFormatException e)
                  {
                    throw new ProtocolException("Illegal "+cmd+" value: "+
                        param);
                  }
                }
                else if (oparam instanceof List)
                {
                  if (cmd==PERMANENTFLAGS)
                  {
                    ms.permanentFlags = (List)oparam;
                    i++;
                  }
                }
              }
            }
          }
        }
        else if (id==EXISTS)
          ms.messageCount = response.getCount();
        else if (id==RECENT)
          ms.newMessageCount = response.getCount();
        else if (id==FLAGS)
          ms.flags = response.getResponseCode();
      }
      else if (tag.equals(response.getTag()))
      {
        if (id==OK)
        {
          List rc = response.getResponseCode();
          if (rc.size()>0 && rc.get(0)==READ_WRITE)
            ms.readWrite = true;
          return ms;
        }
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  /**
   * Creates a mailbox with the specified name.
   * @param mailbox the mailbox name
   * @return true if the mailbox was successfully created, false otherwise
   */
  public boolean create(String mailbox)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(CREATE)
        .append(' ')
        .append(mailbox)
        .toString());
  }

  /**
   * Deletes the mailbox with the specified name.
   * @param mailbox the mailbox name
   * @return true if the mailbox was successfully deleted, false otherwise
   */
  public boolean delete(String mailbox)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(DELETE)
        .append(' ')
        .append(mailbox)
        .toString());
  }

  /**
   * Renames the source mailbox to the specified name.
   * @param source the source mailbox name
   * @param target the target mailbox name
   * @return true if the mailbox was successfully renamed, false otherwise
   */
  public boolean rename(String source, String target)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(RENAME)
        .append(' ')
        .append(source)
        .append(' ')
        .append(target)
        .toString());
  }

  /**
   * Adds the specified mailbox to the set of subscribed mailboxes as
   * returned by the LSUB command.
   * @param mailbox the mailbox name
   * @return true if the mailbox was successfully subscribed, false otherwise
   */
  public boolean subscribe(String mailbox)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(SUBSCRIBE)
        .append(' ')
        .append(mailbox)
        .toString());
  }

  /**
   * Removes the specified mailbox from the set of subscribed mailboxes as
   * returned by the LSUB command.
   * @param mailbox the mailbox name
   * @return true if the mailbox was successfully unsubscribed, false otherwise
   */
  public boolean unsubscribe(String mailbox)
    throws IOException
  {
    return invokeSimpleCommand(new StringBuffer(UNSUBSCRIBE)
        .append(' ')
        .append(mailbox)
        .toString());
  }

  /**
   * Returns a subset of names from the compete set of names available to
   * the client.
   * @param reference the context relative to which mailbox names are
   * defined
   * @param mailbox a mailbox name, possibly including IMAP wildcards
   */
  public ListEntry[] list(String reference, String mailbox)
    throws IOException
  {
    return listImpl(LIST, reference, mailbox);
  }

  /**
   * Returns a subset of subscribed names.
   * @see #list
   */
  public ListEntry[] lsub(String reference, String mailbox)
    throws IOException
  {
    return listImpl(LSUB, reference, mailbox);
  }

  protected ListEntry[] listImpl(String command, String reference,
      String mailbox)
    throws IOException
  {
    if (reference==null || reference.length()==0)
      reference = "\"\"";
    if (mailbox==null || mailbox.length()==0)
      mailbox = "\"\"";
    String tag = newTag();
    sendCommand(tag, new StringBuffer(command)
        .append(' ')
        .append(reference)
        .append(' ')
        .append(mailbox)
        .toString());
    List acc = new ArrayList();
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        if (id==LIST)
        {
          List code = response.getResponseCode();
          String text = response.getText();
          ListEntry entry = new ListEntry();
          entry.attributes = code;
          int si = text.indexOf(' ');
          String delimiter = text.substring(0, si).intern();
          if (delimiter==NIL)
            entry.delimiter='\u0000';
          else
            entry.delimiter = stripQuotes(delimiter).charAt(0);
          entry.mailbox = stripQuotes(text.substring(si+1));
          acc.add(entry);
        }
      }
      else if (response.isTagged())
      {
        if (id==OK)
        {
          ListEntry[] entries = new ListEntry[acc.size()];
          acc.toArray(entries);
          return entries;
        }
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  /**
   * Requests the status of the specified mailbox.
   */
  public MailboxStatus status(String mailbox, String[] statusNames)
    throws IOException
  {
    String tag = newTag();
    StringBuffer buffer = new StringBuffer(STATUS)
      .append(' ')
      .append(mailbox)
      .append(' ')
      .append('(');
    for (int i=0; i<statusNames.length; i++)
    {
      if (i>0)
        buffer.append(' ');
      buffer.append(statusNames[i]);
    }
    buffer.append(')');
    sendCommand(tag, buffer.toString());
    MailboxStatus ms = new MailboxStatus();
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        if (id==STATUS)
        {
          List code = response.getResponseCode();
          int last = code.size()-1;
          for (int i=0; i<last; i+=2)
          {
            try
            {
              String statusName = ((String)code.get(i)).intern();
              int value = Integer.parseInt((String)code.get(i+1));
              if (statusName==MESSAGES)
                ms.messageCount = value;
              else if (statusName==RECENT)
                ms.newMessageCount = value;
              else if (statusName==UIDNEXT)
                ms.uidNext = value;
              else if (statusName==UIDVALIDITY)
                ms.uidValidity = value;
              else if (statusName==UNSEEN)
                ms.firstUnreadMessage = value;
            }
            catch (NumberFormatException e)
            {
              throw new IMAPException(id, "Invalid code: "+code);
            }
          }
        }
      }
      else if (response.isTagged())
      {
        if (id==OK)
          return ms;
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  /**
   * Append a message to the specified mailbox.
   * This method returns an OutputStream to which the message should be
   * written and then closed.
   * @param mailbox the mailbox name
   * @param flags optional list of flags to specify for the message
   * @param content the message body (including headers)
   * @return true if successful, false if error in flags/text
   */
  public boolean append(String mailbox, String[] flags, byte[] content)
    throws IOException
  {
    String tag = newTag();
    StringBuffer buffer = new StringBuffer(APPEND)
      .append(' ')
      .append(mailbox)
      .append(' ');
    if (flags!=null)
    {
      buffer.append('(');
      for (int i=0; i<flags.length; i++)
      {
        if (i>0)
          buffer.append(' ');
        buffer.append(flags[i]);
      }
      buffer.append(')');
      buffer.append(' ');
    }
    buffer.append('{');
    buffer.append(content.length);
    buffer.append('}');
    sendCommand(tag, buffer.toString());
    out.write(content); // write the message body
    while (true)
    {
      IMAPResponse response = readResponse();
      if (response.isTagged()) {
        String id = response.getID();
        if (id==OK)
          return true;
        else if (id==NO)
          return false;
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  /**
   * Request a checkpoint of the currently selected mailbox.
   */
  public void check()
    throws IOException
  {
    invokeSimpleCommand(CHECK);
  }

  /**
   * Permanently remove all messages that have the \Deleted flags set,
   * and close the mailbox.
   * @return true if successful, false if no mailbox was selected
   */
  public boolean close()
    throws IOException
  {
    return invokeSimpleCommand(CLOSE);
  }

  /**
   * Permanently removes all messages that have the \Delete flag set.
   * @return the numbers of the messages expunged
   */
  public int[] expunge()
    throws IOException
  {
    String tag = newTag();
    sendCommand(tag, EXPUNGE);
    List numbers = new ArrayList();
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        if (id==EXPUNGE)
          numbers.add(new Integer(response.getCount()));
      }
      else if (response.isTagged())
      {
        if (id==OK)
        {
          int[] mn = new int[numbers.size()];
          for (int i=0; i<mn.length; i++)
            mn[i] = ((Integer)numbers.get(i)).intValue();
          return mn;
        }
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  public void search()
    throws IOException
  {
    // TODO
  }

  /**
   * Retrieves data associated with messages in the mailbox.
   * @param messages the message numbers
   */
  public MessageStatus[] fetch(int[] messages, String[] fetchCommands)
    throws IOException
  {
    String tag = newTag();
    StringBuffer buffer = new StringBuffer(FETCH);
    buffer.append(' ');
    for (int i=0; i<messages.length; i++)
    {
      if (i>0)
        buffer.append(',');
      buffer.append(messages[i]);
    }
    buffer.append(' ');
    buffer.append('(');
    for (int i=0; i<fetchCommands.length; i++)
    {
      if (i>0)
        buffer.append(' ');
      buffer.append(fetchCommands[i]);
    }
    buffer.append(')');
    sendCommand(tag, buffer.toString());
    List list = new ArrayList(messages.length);
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        if (id==FETCH)
        {
          MessageStatus status = new MessageStatus(response.getCount());
          List code = response.getResponseCode();
          addKeys(code, status);
          status.content = response.content;
          list.add(status);
        }
      }
      else if (response.isTagged())
      {
        if (id==OK)
        {
          MessageStatus[] statuses = new MessageStatus[list.size()];
          list.toArray(statuses);
          return statuses;
        }
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  void addKeys(List code, MessageStatus status)
  {
    int len = code.size();
    for (int i=0; i<len; i++)
    {
      Object key = code.get(i);
      if (key instanceof String)
      {
        Object value = null;
        if ((i+1)<len)
        {
          value = code.get(i+1);
          i++;
        }
        status.put((String)key, value);
      }
      else if (key instanceof List)
      {
        addKeys((List)key, status);
      }
    }
  }

  /**
   * Alters data associated with a message in the mailbox.
   * @param messages the message numbers
   * @param flagCommand FLAGS, +FLAGS, -FLAGS (or .SILENT versions)
   * @param flags message flags to set
   * @return a list of message-number to current flags
   */
  public MessageStatus[] store(int[] messages,
      String flagCommand,
      String[] flags)
    throws IOException
  {
    String tag = newTag();
    StringBuffer buffer = new StringBuffer(STORE);
    buffer.append(' ');
    for (int i=0; i<messages.length; i++)
    {
      if (i>0)
        buffer.append(',');
      buffer.append(messages[i]);
    }
    buffer.append(' ');
    buffer.append(flagCommand);
    buffer.append(' ');
    buffer.append('(');
    for (int i=0; i<flags.length; i++)
    {
      if (i>0)
        buffer.append(' ');
      buffer.append(flags[i]);
    }
    buffer.append(')');
    sendCommand(tag, buffer.toString());
    List list = new ArrayList(messages.length);
    while (true)
    {
      IMAPResponse response = readResponse();
      String id = response.getID();
      if (response.isUntagged())
      {
        // 2 different styles returned by server: FETCH or FETCH FLAGS
        if (id==FETCH)
        {
          MessageStatus mf = new MessageStatus(response.getCount());
          List code = response.getResponseCode();
          int len = code.size();
          for (int i=0; i<len; i++)
          {
            Object key = code.get(i);
            if (key instanceof String && (i+1)<len)
            {
              Object value = code.get(i+1);
              if (value instanceof List)
              {
                mf.put((String)key, value);
                i++;
              }
            }
          }
          list.add(mf);
        }
        else if (id==FETCH_FLAGS)
        {
          MessageStatus mf = new MessageStatus(response.getCount());
          mf.put(FLAGS, response.getResponseCode());
          list.add(mf);
        }
      }
      else if (response.isTagged())
      {
        if (id==OK)
        {
          MessageStatus[] mf = new MessageStatus[list.size()];
          list.toArray(mf);
          return mf;
        }
        else
          throw new IMAPException(id, response.getText());
      }
    }
  }

  /**
   * Copies the specified messages to the end of the destination mailbox.
   * @param messages the message numbers
   * @param mailbox the destination mailbox
   */
  public boolean copy(int[] messages, String mailbox)
    throws IOException
  {
    if (messages==null || messages.length<1)
      return true;
    StringBuffer buffer = new StringBuffer(COPY)
      .append(' ');
    for (int i=0; i<messages.length; i++)
    {
      if (i>0)
        buffer.append(',');
      buffer.append(messages[i]);
    }
    buffer.append(' ').append(mailbox);
    return invokeSimpleCommand(buffer.toString());
  }

  // -- Utility methods --
  
  static String stripQuotes(String text)
  {
    if (text.charAt(0)=='"')
    {
      int len = text.length();
      if (text.charAt(len-1)=='"')
        return text.substring(1, len-1);
    }
    return text;
  }

}
