/*
  GNU Javamail IMAP provider
  Copyright (C) N.J.Ferrier, Tapsell-Ferrier Limited 2000,2001 for the OJE project

  For more information on this please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.mail.providers.imap;


import gnu.kawa.lispexpr.ScmRead;
import gnu.mapping.InPort;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.text.LineBufferedReader;
import gnu.text.SyntaxException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;


/** represents a connection to an IMAP server.
 * The object is connected until <code>disconnect()</code> is called.
 * After disconnection the object cannot be reconnected... use another
 * object to reconnect.
 *
 * <p><h4>IMAP session</h4>
 * This class encapsulates the concept of an IMAP session... including the
 * sending of commands (prefixed with the command line marker - in this case
 * a serial number) and the recieving of responses.
 * Objects of this class can access many folders but only one folder at a
 * time. The current folder is a property of objects of this class.</p>
 *
 * <p><h4>Fetching messages</h4>
 * We only provide fetches by sequence numbers right now. However, since it's
 * perfectly possible for this class to maintain sequence numbers without
 * UIDs at all that might be okay for now.<br>
 * We have to get UID stuff in the end though because Sun's IMAP stuff supports
 * it.</p>
 *
 * <p><h4>About response handling</h4>
 * There isn't a tradition finite state machine doing the parsing, partly
 * because IMAP is so messy that the FSM would be rather large.</p>
 *
 * <p>Instead we use a simple parser that parses responses a line at a time.
 * Each response line is analysed to establish whether it is a status response
 * (to indicate success or failure) or an untagged response (which is used
 * to transfer some information from server to client).</p>
 *
 * <p>IMAP commands are issued only though methods in this class. The methods
 * have names that are analagous to the commands and take similar arguments.
 * For that reason we refer to these methods as <i>command methods</i>.</p>
 *
 * <p>The command methods issue the IMAP command and then call the response
 * parser continually until they recieve a status message or an exception
 * is thrown.</p>
 *
 * <p>The response parser knows how to establish what a particular response
 * is, it then hands off the rest of the response parsing to a specific method
 * for the response type. These extra parsing methods are called <i>response
 * handler methods</i>.</p>
 *
 * <p>So the response parser is a connector which links the command method
 * to the response handler methods.</p>
 *
 * <p>The response parser throws a <code>IMAPOp</code> exception when it finds
 * a non-OK status response (such as <code>BAD</code>). When <code>OK</code> is
 * found the parser returns <code>true</code>. This allows the command methods
 * to establish safe completion.</p>
 *
 * <p>Untagged responses are often dealt with by response handler methods by
 * setting a variable inside this class or firing an event.</p>
 *
 * <p><h4>Complex response handler methods</h4>
 * Some response handler methods are more complex than the above suggests.
 * Where IMAP responses are more complex than a single string the associated
 * response handler method will usually throw an <code>IMAPOp</code> with
 * a stream that wraps the socket stream. The exception acts like a longjmp
 * back into the command method. The command method can then read or return
 * the wrapped stream. The stream wrapper can handle buffering and any other
 * response parsing that might be needed.<p>
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPConnection
{

  /** this is our stand in debug switch...
   * it turns on the Tee on the socket os and the output of readline
   */
  boolean __DEBUG=false;

  /** used to turn on specific output of some responses.
   */
  final static boolean debugShowResponses=false;

  /** the listener that recieves notifications of stuff going on here.
   */
  ConnectionListener m_listener;

  /** the list of objects listening to unsolicited responses.
   */
  UnsolListener[] m_unsolListeners=new UnsolListener[0];

  /** the socket connected to the IMAP server.
   */
  Socket m_imap;

  /** the input stream coming from the IMAP server.
   * If we implement debugging then this will have to be a Y stream with one
   * fork printing data to the debug console and the other fork going to the
   * application.
   */
  InputStream m_instream;

  /** used as the code facing in stream.
   * This KAWA class is used so that we can easily place a Scheme reader
   * (<code>gnu.kawa.lispexpr.ScmRead</code>) on top of it. That allows
   * us to easily read s-expressions.
   *
   * <p>This stream does some buffering but that doesn't matter too much
   * because the structure of IMAP means that buffering can't be that
   * problematic.</p>
   */
  LineBufferedReader m_in;

  /** the output stream going to the IMAP server.
   * If we implement debugging then this will have to be a Y stream with one
   * fork going to the debug console and the other to the socket.
   */
  OutputStream m_outstream;

  /** the writer for sending commands to out stream.
   */
  PrintWriter m_out;

  /** the next avaialble command tag.
   */
  int m_commandtag=0;

  /** the string used as a heirarchy delimitter.
   * @see #readListResponse which causes this to get set
   */
  String m_delim=null;


  //mailbox data that is set through the course of the session

  /** the write state of the mailbox.
   * If this is <code>false</code> then the mailbox is read-only.
   */
  boolean m_isWriteable=false;

  /** the number of messages there are in this mailbox.
   */
  int m_exists=0;

  /** the number of recent message there are in this mailbox.
   */
  int m_recent=0;

  /** the first unseen message number.
   */
  int m_unseen=-1;

  /** the uidvalidity.
   */
  long m_uidvalidity=0;

  /** the currently selected folder of this connection.
   * Initially this is "/" which means authenticated state.
   */
  String m_folderName="/";


  //data that must be cycled every request

  /** any alert message that we receive in a status response.
   */
  String m_alertMessage=null;

  /** the last status response message recieved.
   */
  String m_statusResponseMessage=null;


  //constructor

  /** connect and authenticate to the specified IMAP server.
   * Notifies the listener supplied of everything that goes on except errors which
   * get notified through the exception interface.
   *
   * This method also works out the folder namespace delimitter character.
   *
   * @throws IMAPException if the server is not an IMAP server or the con is refused
   * @throws IOException as a temp measure (processResponses throws IOExcep)
   */
  public IMAPConnection(ConnectionListener cl,String host,int port,
			String username,String password)
  throws IMAPException,IOException
  {
    //first validate arguments
    if(username==null || password==null)
    throw new IMAPException("can't authenticate because username or password is null");
    try
    {
      m_imap=new Socket(host,port);
      //setup the streams for output - the command writer has autoflush
      if(__DEBUG)
      m_outstream=new TeeOutputStream(m_imap.getOutputStream(),System.err);
      else
      m_outstream=m_imap.getOutputStream();
      m_out=new PrintWriter(m_outstream,true);
      //setup the input environment
      m_instream=m_imap.getInputStream();
      m_in=new InPort(new ASCII7BitReader(m_instream));
      //we need a small buffer to stop it sucking up message
      //data when we don't want it to
      m_in.setBuffer(new char[1]);
      m_listener=cl;
      String connectResponse=readLine();
      if(m_listener!=null)
      m_listener.connected(this,connectResponse);
    }
    catch(UnknownHostException badhost)
    {
      try
      {
	m_imap.close();
      }
      catch(Throwable t){}
      throw new IMAPException("no possible to connect to the specified IMAP server");
    }
    //now handle login
    try
    {
      m_out.println("gnumail"+(++m_commandtag)+" login "+username+" "+password);
      while(!processResponse());
      if(m_listener!=null)
      m_listener.authenticated(this,m_statusResponseMessage);
    }
    catch(IMAPOp badLogin)
    {
      try
      {
	m_listener.disconnected(this,m_statusResponseMessage);
	m_imap.close();
      }
      catch(Throwable t){}
      throw new IMAPException("can't authenticate - server dropped connection");
    }
  }


  //handle unsolicited responses listeners

  /** add a listener for unsolicitied responses.
   */
  public void addUnsolicitedResponseListener(UnsolListener l)
  {
    UnsolListener[] realloc=new UnsolListener[m_unsolListeners.length+1];
    System.arraycopy(m_unsolListeners,0,realloc,0,realloc.length-1);
    realloc[realloc.length-1]=l;
    m_unsolListeners=realloc;
  }

  /** remove a listener.
   */
  public void removeUnsolicitedResponseListener(UnsolListener l)
  {
    UnsolListener[] realloc=new UnsolListener[m_unsolListeners.length-1];
    for(int i=0,j=0; i<m_unsolListeners.length; i++)
    {
      if(m_unsolListeners[i]!=l)
      realloc[j++]=m_unsolListeners[i];
    }
    m_unsolListeners=realloc;
  }


  //data provision

  /** get the namespace delimiter.
   * The namespace delimitter is the character that is used in LIST
   * responses to signify the sections of a folder path. Most often
   * it is "/".
   *
   * @return the delimitter for folder namespaces <code>null</code>
   *   if the namespace is flat
   */
  public String getNSDelimitter()
  {
    if(m_delim==null || m_delim.length()==0)
    return null;
    return m_delim;
  }

  /** get the current folder's name.
   * @return the folder name, null indicates authenticated but un-selected state
   */
  public String getFolderName()
  {
    return m_folderName;
  }

  /** get the current message count.
   */
  public int getMessageCount()
  {
    return m_exists;
  }

  /** get the number of new messages.
   */
  public int getNewMessageCount()
  {
    return m_recent;
  }

  /** get the index of the first unseen message.
   * @return the index of -1 if there isn't one
   */
  public int getFirstUnread()
  {
    return m_unseen;
  }

  /** is the current mailbox readonly?
   */
  public boolean isReadOnly()
  {
    return !m_isWriteable;
  }


  //these are command methods
  //- users call them to get something done on the IMAP server

  /** actively logout this connection.
   * This method logs out, reads the responses from the command and
   * then closes the socket. It never propagates any errors it recieves
   * and *always* closes the connection.
   */
  public synchronized void disconnect()
  {
    String byeMessage="farewell sweet romance";
    try
    {
      m_out.println("gnumail"+(++m_commandtag)+" LOGOUT");
      //this call will result in a BYE which throws the exception
      while(!processResponse());
    }
    catch(IMAPOp loggedOut)
    {
      byeMessage=m_statusResponseMessage;
      //this should then read the "OK logged out" response
      try
      {
	processResponse();
      }
      catch(Throwable t)
      {
      }
    }
    finally
    {
      try
      {
	m_listener.disconnected(this,byeMessage);
	m_imap.close();
      }
      catch(Throwable t)
      {
	//no need to do anything - it's probably closed anyway
      }
    }
  }

  /** no-operation.
   * This is actually used quite a bit.
   * It's used to ensure that connections we return within the implementation
   * are not going to be "server disconnected" just before we want to use them.
   */
  public synchronized void noop()
  throws IMAPException
  {
    try
    {
      m_out.println("gnumail"+(++m_commandtag)+" NOOP");
      while(!processResponse());
    }
    catch(IMAPOp failure)
    {
      if(failure.getToken().equals("BYE"))
      m_listener.disconnected(this,m_statusResponseMessage);
      if(failure.getToken().equals("BAD") || failure.getToken().equals("NO"))
      throw new IMAPException("NOOP failed - wierd.");
    }
  }

  /** select a particular folder.
   * We cache the current folder name thus preventing us from having
   * to select multiple times.
   *
   * @throws IMAPException if the folder doesn't exist.
   */
  public synchronized void select(String folder)
  throws IMAPException
  {
    try
    {
      //first check that there aren't spaces in the supplied folder name
      if(folder.indexOf(' ')>-1)
      folder="\""+folder+"\"";
      //some usefull debugging
      // System.err.println("IMAPConnection::select folder="+folder+" current folder="+m_folderName);
      //if we've already done it do nothing
      if(m_folderName.equalsIgnoreCase(folder))
      return;
      //otherwise send the folder selection statement
      m_out.println("gnumail"+(++m_commandtag)+" SELECT "+folder);
      while(!processResponse());
      m_folderName=folder;
    }
    catch(IMAPOp failure)
    {
      if(failure.getToken().equals("BYE"))
      m_listener.disconnected(this,m_statusResponseMessage);
      if(failure.getToken().equals("BAD") || failure.getToken().equals("NO"))
      throw new IMAPException("no such folder");
    }
  }

  /** close a folder; select authenticated state.
   *
   * @throws IMAPException if there's an error.
   */
  public synchronized void close()
  throws IMAPException
  {
    try
    {
      //we don't need to select auth state if we're in it already
      if(m_folderName.equals("/"))
      return;
      m_out.println("gnumail"+(++m_commandtag)+" CLOSE");
      while(!processResponse());
      m_folderName="/";
    }
    catch(IMAPOp failure)
    {
      if(failure.getToken().equals("BYE"))
      m_listener.disconnected(this,m_statusResponseMessage);
      if(failure.getToken().equals("BAD") || failure.getToken().equals("NO"))
      throw new IMAPException("can't close.");
    }
  }

  /** perform a list of folders in the mailbox.
   * The folder information is returned as a list of lists.
   * Each list contains a <code>String</code> which describes the
   * folder-name and a sequence of zero or more folder attributes
   * as defined in IMAP4rev1 section 7.2.2.
   *
   * @param reference if <code>null</code> this is sent as ""
   * @param mailboxName if <code>null</code> this is sent as "*"
   * @return a list of the folders that were matched as an s-expression
   */
  public synchronized Pair listFolders(String reference,String mailboxName)
  throws IMAPException,IOException
  {
    try
    {
      if(reference==null)
      reference="\"\"";
      if(mailboxName==null)
      mailboxName="\"*\"";
      m_out.println("gnumail"+(++m_commandtag)+" LIST "+reference+" "+mailboxName);
      //the start point of the list we'll use
      Pair listOfLists=new Pair();
      Pair lastList=listOfLists;
      Pair list=listOfLists;
      boolean done=false;
      while(!done)
      {
	try
	{
	  done=processResponse();
	}
	catch(IMAPOp failureOrSuccess)
	{
	  String token=failureOrSuccess.getToken();
	  if(token.equals("BYE"))
	  {
	    m_listener.disconnected(this,m_statusResponseMessage);
	    throw new IMAPException("connection closed");
	  }
	  //is this error handling correct?
	  if(token.equals("BAD") || token.equals("NO"))
	  throw new IMAPException("no such message");
	  //must have a list response
	  list.car=failureOrSuccess.getList();
	  //make the cons cell a new list
	  list.cdr=new Pair();
	  //setup the pointers
	  lastList=list;
	  list=(Pair)list.cdr;
	}
      }
      //this ensures the list is properly terminated
      lastList.cdr=LList.Empty;
      //some debug printing stuff for testing responses
      // printList(listOfLists,System.err);
      return listOfLists;
    }
    catch(Throwable t)
    {
      t.printStackTrace(System.err);
      return null;
    }
  }

  /** fetch a body structure.
   * The s-expression body structure is returned as a string.
   *
   * @param msgNumber is the sequence number
   * @return the bodysturcture s-expression
   */
  public synchronized Pair fetchBodyStructure(int msgNumber)
  throws IMAPException
  {
    try
    {
      m_out.println("gnumail"+(++m_commandtag)+" FETCH "+msgNumber+" (BODYSTRUCTURE)");
      boolean done=false;
      Pair structure=null;
      while(!done)
      {
	try
	{
	  done=processResponse();
	}
	catch(IMAPOp failureOrSuccess)
	{
	  String token=failureOrSuccess.getToken();
	  if(token.equals("BYE"))
	  {
	    m_listener.disconnected(this,m_statusResponseMessage);
	    throw new IMAPException("connection closed");
	  }
	  //is this error handling correct?
	  if(token.equals("BAD") || token.equals("NO"))
	  throw new IMAPException("no such message");
	  //must be success!
	  structure=failureOrSuccess.getList();
	}
      }
      return structure;
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      return null;
    }
  }

  /** fetch a message content.
   * Fetch a message specified by a sequence number. The message data
   * is returned "inline"; that means that the status response has NOT
   * been read when the message is returned.
   *
   * <p><h4>Part specification</h4>
   * If the part is specified as -1 then the part specifier is not sent
   * which results in the fetch data item:
   * <pre>
   *   (body[])
   * </pre>
   * This returns the whole message, with the header first and all the parts
   * together (usually with some kind of encoding).</p>
   *
   * <p>If the part is specified as 0 then the header is requested.
   * Some IMAP servers allow you to do this to retrieve the header:
   * <pre>
   *   (body[0])
   * </pre>
   * This is non standard so we convert requests for part 0 to this:
   * <pre>
   *   (body[header])
   * </pre>
   * Which is the standard way of getting the header.</p>
   *
   * <p><h4>Partial fetch specification</h4>
   * You an specify a start byte position and a length of bytes for partial
   * content retrieval. If the <code>offset</code> is specified as less than
   * 0 or the <code>length</code> is specified as less than 1 a partial fetch
   * is not performed, a normal fetch is used instead.</p>
   *
   * @param msgNumber is the sequence number
   * @param part which part to fetch (see above)
   * @param offset the partial fetch offset
   * @param length the partial fetch length
   * @return the stream of data.
   * @see #fetchEnvelope which is an s-expression equivalent of
   *   <code>fetchBody(x,0)</code>
   * @see IMAPMessageStream which handles "inlining" responses
   */
  public synchronized InputStream fetchBody(int msgNumber,int part,int offset,int length)
  throws IMAPException
  {
    try
    {
      m_out.print("gnumail");
      m_out.print(++m_commandtag);
      m_out.print(" FETCH ");
      m_out.print(msgNumber);
      m_out.print(" (BODY[");
      //part specifier handling as detailed in the javadoc
      if(part>-1 && part==0)
      m_out.print("header");
      else if(part>-1)
      m_out.print(part);
      //partial fetch handling as described above
      if(offset<0 || length<1)
      m_out.println("])");
      else
      {
	m_out.print("]<");
	m_out.print(offset);
	m_out.print(".");
	m_out.print(length);
	m_out.println(">)");
      }
      while(!processResponse());
    }
    catch(IMAPOp failureOrSuccess)
    {
      String token=failureOrSuccess.getToken();
      if(token.equals("BYE"))
      m_listener.disconnected(this,m_statusResponseMessage);
      //is this error handling correct?
      if(token.equals("BAD") || token.equals("NO"))
      throw new IMAPException("no such message");
      //must be success!
      return failureOrSuccess.getInputStream();
    }
    return null;
  }

  /** fetch the envelope data.
   * IMAP thoughtfully supplies header data as an s-expression.
   * Since we use s-expressions for other facets I couldn't resist
   * adding some handling for headers that way.
   * See RFC2060 page 62 for details about ENVELOPE.
   *
   * @param msgNumber is the number of the message to get
   * @return the s-expression defining the header
   */
  public synchronized Pair fetchEnvelope(int msgNumber)
  throws IMAPException
  {
    try
    {
      while(!processResponse());
      return null;
    }
    catch(IMAPOp failureOrSuccess)
    {
      throw new IMAPException("pah!");
    }
  }

  /** causes an expunge.
   *
   * @throws IOException temporarily - because processResponse does.
   */
  public synchronized void expunge()
  throws IMAPException,IOException
  {
    try
    {
      //send the expunge command
      m_out.println("gnumail"+(++m_commandtag)+" EXPUNGE");
      while(!processResponse());
    }
    catch(IMAPOp failure)
    {
      //need to handle stuff like disconnection warnings
      if(failure.getToken().equals("BYE"))
      m_listener.disconnected(this,m_statusResponseMessage);
    }
  }


  //response reading/processing

  /** this is used to parse "normal" responses.
   * A normal response is one where a return is not required during the processing
   * of the response. An example of a non-normal response is one where you want to
   * inline the return of the message data.
   *
   * @return true when a tagged response is found
   * @throws IMAPOp if the command failed
   * @throws IOException if it can't read off streams properly
   */
  boolean processResponse()
  throws IMAPOp
  {
    try
    {
      skipWhitespace();
      //this presumes that whitespace will be skipped
      char ch=(char)m_in.read();
      if(ch!='*')
      {
	//read a tagged response - can only be a status response
	StringBuffer buf=new StringBuffer();
	while(!Character.isWhitespace(ch))
	{
	  buf.append(ch);
	  ch=(char)m_in.read();
	}
	String commandtag=buf.toString();
	if(!commandtag.startsWith("gnumail"))
	throw new IOException("unexpected input: "+commandtag+"<<<");
	//get the serial number so we can match it with the current one if we want
	String serialNo=commandtag.substring(7);
	int serialNumber=Integer.parseInt(serialNo);
	/***
	    FIXME
	    We don't yet match the serial number and the number used to issue the command
	***/
	skipWhitespace();
	//this *should* clear the buffer but we should check that
	buf.delete(0,buf.length());
	//get the status token
	ch=(char)m_in.read();
	while(!Character.isWhitespace(ch))
	{
	  buf.append(ch);
	  ch=(char)m_in.read();
	}
	processStatusResponse(buf.toString());
	return true;
      }
      //read an untagged response
      skipWhitespace();
      //if the next token is a digit then the response is one of
      // fetch
      // expunge
      // exists
      // recent
      //and if not a digit it could be any other untagged response
      ch=(char)m_in.read();
      if(Character.isDigit(ch))
      {
	//we start off by converting the number
	int val=Character.digit(ch,19);
	ch=(char)m_in.read();
	while(Character.isDigit(ch))
	{
	  val*=10;
	  val+=Character.digit(ch,10);
	  ch=(char)m_in.read();
	}
	//now find the response token
	skipWhitespace();
	StringBuffer buf=new StringBuffer();
	ch=(char)m_in.read();
	while(!Character.isWhitespace(ch))
	{
	  buf.append(ch);
	  ch=(char)m_in.read();
	}
	String resptag=buf.toString();
	//when each of these methods is called the stream is positioned after any whitespace
	if(resptag.equalsIgnoreCase("FETCH"))
	readFetchResponse(val);
	else if(resptag.equalsIgnoreCase("EXPUNGE"))
	readExpungeResponse(val);
	else if(resptag.equalsIgnoreCase("EXISTS"))
	readExistsResponse(val);
	else if(resptag.equalsIgnoreCase("RECENT"))
	readRecentResponse(val);
      }
      else
      {
	//got a token to read
	StringBuffer buf=new StringBuffer();
	while(!Character.isWhitespace(ch))
	{
	  buf.append(ch);
	  ch=(char)m_in.read();
	}
	String resptag=buf.toString();
	if(!processStatusResponse(resptag))
	{
	  //process non-status responses, eg: list response
	  if(resptag.equalsIgnoreCase("LIST"))
	  readListResponse();
	  if(resptag.equalsIgnoreCase("FLAGS"))
	  readFlagsResponse();
	}
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    //now return false because the response was untagged
    return false;
  }

  /** process status responses.
   * Most status responses can be tagged or untagged; having a method to handle them
   * means we don't have to repeat this code inside <code>processResponse()</code>.
   *
   * @param token a string representing the token read from the stream
   * @return true if the response was handled
   * @throws IMAPOp if something went wrong
   */
  boolean processStatusResponse(String token)
  throws IMAPOp
  {
    if(token.equals("BYE") || token.equals("BAD") || token.equals("NO"))
    {
      readStatusMessage();
      throw new IMAPOp(token);
    }
    else if(token.equals("PREAUTH"))
    {
      readStatusMessage();
      return true;
    }
    else if(token.equals("OK"))
    {
      readStatusMessage();
      return true;
    }
    //we shouldn't ever get here
    return false;
  }

  /** reads a message from a status response.
   * This includes the processing of any response code.
   * The currently defined response codes are:
   * <ul>
   * <li>ALERT  the text following the code is a message that the user must see
   * <li>NEWNAME  the mailbox name has changed
   * <li>PARSE  some headers weren't parseable
   * <li>PERMANENTFLAGS  the list of flags that can be stored
   * <li>READ-ONLY  the mailbox is read only
   * <li>READ-WRITE  the mailbox is not read only
   * <li>TRYCREATE  an operation is failing because the target doesn't exist
   * <li>UIDVALIDITY  the uid validity number has changed
   * <li>UNSEEN  the sequence number of the first unread message
   * </ul>
   *
   * <p><h4>Responding to response codes</h4>
   * This method performs some actions automatically for some of the above
   * response codes. For example, for UIDVALIDITY the class instance variable
   * <code>m_uidvalidity</code> is set.<br>
   * Not *all* the response codes are read properly yet.<br>
   * In the future this system *could* be replaced by a listener system.</p>
   */
  void readStatusMessage()
  {
    try
    {
      skipWhitespace();
      String line=readLine();
      int i=0;
      char ch;
      ch=line.charAt(i);
      if(ch!='[')
      m_statusResponseMessage=line;
      else
      {
	ch=line.charAt(++i);
	switch(ch)
	{
	  case 'A':
	    //alert so set the alert message
	    m_alertMessage=line.substring(5).trim();
	    break;
	  case 'N':
	    //newname... just discard the rest of the string
	    break;
	  case 'P':
	    if(line.charAt(++i)=='A')
	    //parse error - just discard the rest of the string
	    ;
	    else
	    //permanent-flags - we should collect this but we don't right now...
	    ;
	    break;
	  case 'R':
	    //trash the next 5 bytes
	    i+=5;
	    if(line.charAt(i)=='O')
	    //mailbox is read-only
	    m_isWriteable=false;
	    else
	    //mailbox is read-write
	    m_isWriteable=true;
	    break;
	  case 'T':
	    //trycreate - just dump the line
	    ;
	    break;
	  case 'U':
	    if(line.charAt(++i)=='I')
	    {
	      //uidvalidity - read the new number
	      i+=10;
	      String num=line.substring(i,line.indexOf(']')).trim();
	      try
	      {
		m_uidvalidity=Integer.parseInt(num);
		//we need to fire the listener event here
	      }
	      catch(NumberFormatException e)
	      {
		System.err.println("whoops! UDIVALIDITY");
	      }
	    }
	    else
	    {
	      //unseen - read the number
	      i+=5;
	      String num=line.substring(i,line.indexOf(']')).trim();
	      try
	      {
		m_unseen=Integer.parseInt(num);
		//we need to do something with this value
	      }
	      catch(NumberFormatException e)
	      {
		System.err.println("whoops! UNSEEN");
	      }
	    }
	    break;
	}
      }
    }
    catch(IOException e)
    {
      //hmmmm....
    }
  }


  //response handler methods

  /** read the response to an EXPUNGE.
   */
  void readExpungeResponse(int messageNum)
  {
    //no need to do anything yet
    //probably *should* check the messageNum against what was requested or something...
    try
    {
      if(debugShowResponses)
      System.err.println("expunge discarding: "+readLine());
    }
    catch(Throwable t) {}
  }

  /** occurs as the result of a SELECT or EXAMINE or because of external change.
   * Set the exists counter in this connection and fire an event.
   */
  void readExistsResponse(int existing)
  {
    //since all whitespace has been read we just set the exists counter
    m_exists=existing;
    try
    {
      //fire events
      for(int i=0; i<m_unsolListeners.length; i++)
      m_unsolListeners[i].existsChanged(m_folderName,m_exists);
      //here's some debug
      if(debugShowResponses)
      System.err.println("exists had "+existing+" discarding rest of line: "+readLine());
    }
    catch(Throwable e) {}
  }

  /** occurs as the result of SELECT or EXAMINE.
   * The data here is the number of messages that are recent in this folder.
   *
   * <p>These response are not very reliable, none the less we fire an event
   * to provide the information.</p>
   */
  void readRecentResponse(int recent)
  {
    //since all whitespace has been read we just set the recent counter
    m_recent=recent;
    try
    {
      String recentResponse=readLine();
      if(debugShowResponses)
      System.err.println("recent response was: "+recentResponse);
      //fire events
      /***
	  FIXME!!!!
	  We always send 0 here because we've yet to add
	  the response parsing.
      ***/
      for(int i=0; i<m_unsolListeners.length; i++)
      m_unsolListeners[i].recentChanged(m_folderName,0);
    }
    catch(Throwable e)
    {
    }
  }

  /** reads the "FLAGS" response.
   * This is issued in response to a "SELECT" or "EXAMINE" or because of some
   * external event. The method reads the data and fires the flags event.
   *
   * <p>It details the flags that are applicable to the folder that is selected.
   * Any attempt to set flags other than those specified by the response will
   * be invalid.</p>
   */
  void readFlagsResponse()
  {
    try
    {
      String flagsResponse=readLine();
      if(debugShowResponses)
      System.err.println("the flags response: "+flagsResponse);
      //fire events
      for(int i=0; i<m_unsolListeners.length; i++)
      m_unsolListeners[i].flagsSet(m_folderName,flagsResponse);
    }
    catch(IOException e)
    {
    }
  }

  /** read a response to the LIST command.
   * This is quite an unusual response processing command
   * because a list response consists of many untagged responses.
   * This method causes ALL the untagged LIST responses to be read
   * and returned to the calling command method through the use of
   * the list slot in the <code>IMAPOp</code>.
   *
   * <p>The format of the list returned is different to what you
   * might expect... it's not the same as the list response from the
   * server. It looks something like:
   * <pre>
   *   ( "foldername" \UnMarked  )
   * </pre>
   * The syntax is a list with the first element being a string
   * describing the name of the folder and the subsequent elements
   * being the folder's attributes.</p>
   *
   * @see #m_delim which gets set by this method
   */
  void readListResponse()
  throws IMAPOp
  {
    //a LIST response is:
    // attribute list
    // heirarchy delimitter
    // name
    //eg: * LIST (\Noselect) "/" "/fred/"
    //
    try
    {
      IMAPOp longjmp=new IMAPOp("list");
      //cast the stream back to the InPort so we can read the attribute list
      // InPort inp=new InPort(m_in);
      // inp.setBuffer(new char[0]);
      ScmRead rdr=new ScmRead((InPort)m_in);
      Pair attribList=(Pair)rdr.readObject();
      //handle the heirarchy delim
      String nsSeparator=rdr.readObject().toString();
      if(m_delim==null)
      m_delim=nsSeparator;
      //now get the folder name
      String name=rdr.readObject().toString();
      //now add the folder name as the first element in the list
      // Pair list=new Pair('\"'+name+'\"',attribList);
      Pair list=new Pair(name,attribList);
      //now set the list and return
      longjmp.setList(list);
      throw longjmp;
    }
    catch(IOException e)
    {
      //don't do anything with it...
      e.printStackTrace(System.err);
    }
    catch(SyntaxException e)
    {
      //whoops! Kawa found a problem
      e.printStackTrace(System.err);
    }
  }

  /** read the fetch response from the stream.
   * The fetch response is related to the message number specified.
   *
   * <p>This shows the responses we handle and how we do it.
   * <dl>
   * <dt>BODY[section]
   * <dd>this is the way message content is transfered to us.
   * We throw <code>IMAPOp</code> from here to the calling command
   * method. The exception has a stream set on it which is used to
   * read the content of the message.
   * The stream will also have to ensure that the status response
   * following the message content is read correctly.
   * </dd>
   *
   * <dt>BODYSTRUCTURE
   * <dd>this is how me know what content there is.
   * We throw <code>IMAPOp</code> with the s-expression we've read
   * from the response set in the linked list slot.
   * </dd>
   *
   * <dt>FLAGS
   * <dd>we handle these coz we might want to cache them
   * </dd>
   *
   * <dt>UID
   * <dd>we need this eventually.
   * </dd>
   * </dl></p>
   *
   * @throws IMAPOp with the response
   */
  void readFetchResponse(int messageNum)
  throws IMAPOp
  {
    try
    {
      skipWhitespace();
      //we read the start of the list
      char ch=(char)m_in.read();
      if(ch!='(')
      throw new NullPointerException("shouldn't that be a ()?");
      //read the tag whicn identifies what sort of response it is
      StringBuffer buf=new StringBuffer();
      ch=(char)m_in.read();
      while(Character.isLetter(ch))
      {
	buf.append(ch);
	ch=(char)m_in.read();
      }
      String listType=buf.toString();
      if(listType.equalsIgnoreCase("BODY"))
      {
	//what sort of body is it? the body part specifier tells us, eg:
	//  BODY[HEADER]  the header
	//  BODY[1]       part 1
	//  BODY[]        the whole of the message
	m_in.read();
	//empty the buffer so we can reuse it
	buf.setLength(0);
	//read the word
	ch=(char)m_in.read();
	while(Character.isLetter(ch))
	{
	  buf.append(ch);
	  ch=(char)m_in.read();
	}
	//work out what type of body fetch this is
	String bType=null;
	if(buf.length()==0)
	bType="full";
	else
	bType=buf.toString();
	//after the body part specifier is:
	//- possibly a partial fetch offset specifier, eg: BODY[]<0>
	//- always a quoted string byte count, eg: BODY[] {300}
	skipWhitespace();
	//skip everything until we've read the open '{'
	while(((char)m_in.read())!='{');
	//read the byte count indicating the size of the quoted string
	ScmRead rdr=new ScmRead((InPort)m_in);
	gnu.math.IntNum byteCount=(gnu.math.IntNum)rdr.readObject();
	//read the '}'
	readLine();
	IMAPOp longjmp=new IMAPOp("body");
	//create a counting stream on the current input stream
	IMAPMessageStream counter=new IMAPMessageStream(m_instream,byteCount.intValue(),this);
	longjmp.setStream(counter);
	throw longjmp;
      }
      else if(listType.equalsIgnoreCase("BODYSTRUCTURE"))
      {
	//read the body structure
	skipWhitespace();
	ScmRead rdr=new ScmRead((InPort)m_in);
	//and read the s-expression
	Pair bsList=(Pair)rdr.readObject();
	//read the end of the line, this should just be the eol marker
	readLine();
	//debug of the list
	// printList(bsList,System.err);
	IMAPOp longjmp=new IMAPOp("bodystruct");
	longjmp.setList(bsList);
	throw longjmp;
      }
      else if(listType.equalsIgnoreCase("FLAGS"))
      {
      }
      else if(listType.equalsIgnoreCase("UID"))
      {
      }
      else
      {
	//something went very wrong - we're not supposed to be able to get
	//anything but the 4 above.
      }
    }
    catch(IOException e)
    {
      //don't do anything with it...
    }
    catch(SyntaxException e)
    {
    }
  }


  //io helper methods

  /** read characters from the stream until whitespace ends.
   */
  void skipWhitespace()
  {
    try
    {
      char ch=' ';
      while(Character.isWhitespace(ch))
      ch=(char)m_in.read();
      //this was used when the code was using the PushBackInputStream
      // m_in.unread((byte)ch);
      m_in.unread();
    }
    catch(IOException e)
    {
      //don't do anything with it
    }
  }

  /** read a line of text from the input stream.
   * Ensures 7bit transport.
   *
   * @return the line or null if the stream ran out.
   */
  String readLine()
  throws IOException
  {
    StringBuffer sb=new StringBuffer();
    int ch=m_in.read();
    if(ch==-1)
    return null;
    while(true)
    {
      if(__DEBUG)
      System.err.write(ch);
      if(ch==-1 || ((char)ch)=='\n')
      break;
      sb.append((char)ch);
      ch=m_in.read();
    }
    // System.err.println(">>>readLine="+sb.toString());
    return sb.toString();
  }

  /** a usefull debug tool to print a list using Kawa.
   */
  static void printList(Pair list,OutputStream out)
  {
    gnu.mapping.OutPort o=new gnu.mapping.OutPort(out);
    o.writeSchemeObject(list,false);
    o.flush();
  }

}

/** a longjump exception.
 * It has various "slots" for different sorts of data so that
 * parser methods can store data in it and throw it back to the original
 * caller method. The caller can then extract the data and return it to the
 * user.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
class IMAPOp
extends Exception
{

  String m_token;

  /** the stream slot.
   * This is used for large amounts of data, for example message bodys.
   */
  InputStream slot_is=null;

  /** the list slot.
   * This is used for bodystructures and LIST responses.
   */
  Pair slot_list=null;

  /** create an operation failure message.
   *
   * @param token the token which was reported as the cause of the failure.
   */
  IMAPOp(String token)
  {
    super(token);
    m_token=token;
  }

  String getToken()
  {
    return m_token;
  }

  /** set the stream slot.
   */
  void setStream(InputStream is)
  {
    slot_is=is;
  }

  /** get the stream slot.
   */
  InputStream getInputStream()
  {
    return slot_is;
  }

  /** set the list slot.
   */
  void setList(Pair list)
  {
    slot_list=list;
  }

  /** get the list slot.
   */
  Pair getList()
  {
    return slot_list;
  }
}
