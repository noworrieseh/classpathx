/*
 * NNTPStore.java
 * Copyright (C) 1999 dog <dog@dog.net.uk>
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
 * 
 * You may retrieve the latest version of this library from
 * http://www.dog.net.uk/knife/
 */

package gnu.mail.providers.nntp;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import gnu.mail.util.*;
import gnu.mail.treeutil.StatusEvent;
import gnu.mail.treeutil.StatusListener;
import gnu.mail.treeutil.StatusSource;

/**
 * The storage class implementing the NNTP Usenet news protocol.
 *
 * @author dog <dog@dog.net.uk>
 * @author Torgeir Veimo <tv@sevenmountains.no>
 * @version 1.4.2
 */
public class NNTPStore 
extends Store 
implements StatusSource 
{

  /**
   * The default NNTP port.
   */
  public static final int DEFAULT_PORT = 119;
	
  static int fetchsize = 1024;
  static boolean memoryOptimisation = false;

  protected Socket socket;
  protected CRLFInputStream in;
  protected CRLFOutputStream out;
  protected String hostname;

  protected String response;
  protected int responseCode;

  static final int HELP = 100;
  static final int READY = 200;
  static final int READ_ONLY = 201;
  static final int STREAMING_OK = 203;
  static final int CLOSING = 205;
  static final int GROUP_SELECTED = 211; // or list of article numbers follows
  static final int LISTING = 215; // list of newsgroups follows
  static final int ARTICLE_RETRIEVED_BOTH = 220;
  static final int ARTICLE_RETRIEVED_HEAD = 221; // or header follows
  static final int ARTICLE_RETRIEVED_BODY = 222;
  static final int ARTICLE_RETRIEVED = 223;
  static final int LISTING_OVERVIEW = 224;
  static final int LISTING_ARTICLES = 230; // list of new articles by message-id follows
  static final int LISTING_NEW = 231; // list of new newsgroups follows
  static final int ARTICLE_POSTED = 240; // article posted ok
  static final int AUTHINFO_OK = 281;
  static final int SEND_ARTICLE = 340; // send article to be posted. End with <CR-LF>.<CR-LF>
  static final int SEND_AUTHINFOPASS = 381; // send password (response to user name)
  static final int SERVICE_DISCONTINUED = 400;
  static final int NO_SUCH_GROUP = 411;
  static final int NO_GROUP_SELECTED = 412; // no newsgroup has been selected
  static final int NO_ARTICLE_SELECTED = 420; // no current article has been selected
  static final int NO_SUCH_ARTICLE_IN_GROUP = 423; // no such article number in this group
  static final int NO_SUCH_ARTICLE = 430; // no such article found
  static final int POSTING_NOT_ALLOWED = 440; // posting not allowed
  static final int POSTING_FAILED = 441; // posting failed
  static final int COMMAND_NOT_RECOGNIZED = 500;
  static final int COMMAND_SYNTAX_ERROR = 501;
  static final int PERMISSION_DENIED = 502;
  static final int SERVER_ERROR = 503;

  protected boolean postingAllowed = false, useNewNews = false;
  protected Root root;
  protected Newsgroup current;
  protected Date lastNewGroup;
  protected Newsrc newsrc;

  protected Hashtable newsgroups = new Hashtable(); // hashtable of newsgroups by name
  protected Hashtable articles = new Hashtable(); // hashtable of articles by message-id

  Vector statusListeners = new Vector();
	
  /**
   * Constructor.
   */
  public NNTPStore(Session session, URLName urlname) 
  {
    super(session, urlname);
    String ccs = session.getProperty("mail.nntp.fetchsize");
    if (ccs!=null) 
    {
      try
      {
	fetchsize = Math.max(Integer.parseInt(ccs), 1024); 
      }
      catch (NumberFormatException e)
      {}
    }
    String mem = session.getProperty("mail.nntp.memoryOptimisation");
    if (mem!=null)
    memoryOptimisation = Boolean.valueOf(mem).booleanValue();
    String unn = session.getProperty("mail.nntp.useNewNews");
    if (unn!=null)
    useNewNews = Boolean.valueOf(unn).booleanValue();
  }
	
  /**
   * Connects to the NNTP server and authenticates with the specified parameters.
   */
  protected boolean protocolConnect(String host, int port, String username, String password) 
  throws MessagingException 
  {
    if (port<0) 
    port = DEFAULT_PORT;
    if (host==null)
    return false;
    try 
    {
      boolean debug = session.getDebug();
			
      hostname = host;
      if (debug)
      System.err.println("DEBUG: nntp: opening connection to "+hostname);
      socket = new Socket(host, port);
      in = new CRLFInputStream(new BufferedInputStream(socket.getInputStream()), fetchsize);
      out = new CRLFOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
      switch (getResponse()) 
      {
	case READY:
	  postingAllowed = true;
	case READ_ONLY:
				//StringTokenizer st = new StringTokenizer(response);
				//if (st.hasMoreTokens()) hostname = st.nextToken();
	  break;
	default:
	  throw new MessagingException("unexpected server response: "+response);
      }
			
      // NNTP basic authentication
      // introduced by Volker Schmidt <vs75@gmx.de>
      // NOTE: need to handle multiple varities of authentication (kerberos, etc)
      if (username!=null && password!=null) 
      {
	send("AUTHINFO USER "+username);
	if (getResponse()!=SEND_AUTHINFOPASS)
	throw new AuthenticationFailedException(response);
	send("AUTHINFO PASS "+password);
	if (getResponse()!=AUTHINFO_OK)
	throw new AuthenticationFailedException(response);
      }
      // end authentication
	
      send("MODE READER"); // newsreader extension
      switch (getResponse()) 
      {
	case READY:
	  postingAllowed = true;
	case READ_ONLY:
	  break;
      }
			
      if (debug)
      System.err.println("DEBUG: nntp: connected to "+hostname+", posting is "+(postingAllowed ? "" : "not ")+"allowed");

      addStore(this);
			
      // build newsgroups hashtable
      if (newsrc==null)
      newsrc = new FileNewsrc(this, url);
      Newsgroup[] n = newsrc.list();
      for (int i=0; i<n.length; i++)
      newsgroups.put(n[i].name, n[i]);
			
      return true;
    } 
    catch(UnknownHostException e) 
    {
      throw new MessagingException("unknown host", e);
    }
    catch(IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  /**
   * Closes the connection.
   */
  public synchronized void close() 
  throws MessagingException 
  {
    if (socket!=null) 
    {
      boolean debug = session.getDebug();
			
      try 
      {
	if (newsrc != null) 
	{
	  if (debug)
	  System.err.println("DEBUG: nntp: closing newsrc");
	  newsrc.close();
	}
      } 
      catch (MessagingException me) 
      {
	System.err.println("ERROR: nntp: error closing newsrc");
      }
			
      // close the newsgroups
      if (current!=null) 
      {
	close(current);
	current = null;
      }
			
      try 
      {
	if (debug)
	System.err.println("DEBUG: nntp: closing connection to "+hostname);
	send("QUIT");
	switch (getResponse()) 
	{
	  case CLOSING:
	    break;
	  case SERVER_ERROR:
	    if (response.toLowerCase().indexOf("timeout")>-1)
	    break;
	  default:
	    throw new MessagingException("unexpected server response: "+response);
	}
	removeStore(this);
	socket.close();
	socket = null;
				
	if (debug)
	System.err.println("DEBUG: nntp: closed connection to "+hostname);
      } 
      catch (IOException e) 
      {
				// socket.close() seems to throw an exception!
				//throw new MessagingException("Close failed", e);
      }
    }
    super.close();
  }
	
  /**
   * Sets the newsrc source to locate subscription information for newsgroups.
   * This should be called <b>before</b> connect()
   */
  public void setNewsrc(Newsrc newsrc) 
  {
    this.newsrc = newsrc;
  }

  /**
   * Returns the newsrc source.
   */
  public Newsrc getNewsrc() 
  {
    return newsrc;
  }
	
  public boolean isSubscribed(String newsgroup) 
  {
    if (newsrc != null)
    return newsrc.isSubscribed(newsgroup);
    return false;	
  }
	
  public void setSubscribed(String newsgroup, boolean subs) 
  {
    if (newsrc != null)
    newsrc.setSubscribed(newsgroup, subs);
  }
	
  public boolean isSeen(String newsgroup, int article) 
  {
    if (newsrc != null)
    return newsrc.isSeen(newsgroup, article);
    return false;	
  }
	
  public void setSeen(String newsgroup, int article, boolean seen) 
  {
    if (newsrc != null)
    newsrc.setSeen(newsgroup, article, seen);
  }

  /**
   * Returns the hostname of the server.
   */
  public String getHostName() 
  {
    return hostname; 
  }
	
  Session getSession() 
  {
    return session;
  }
	
  int getResponse() 
  throws IOException 
  {
    response = in.readLine();
    while ("".equals(response.trim()))
    response = in.readLine();
    boolean debug = session.getDebug();
		
    if (debug)
    System.err.println("DEBUG: nntp: <"+response);
		
    if (response==null)
    response = SERVER_ERROR+" timeout";
    try 
    {
      int index = response.indexOf(' ');
      int index2 = response.indexOf('\t');
      if (index2>-1 && index2<index)
      index = index2;
      if (index>-1) 
      {
	responseCode = parseInt(response.substring(0, index).trim());
	response = response.substring(index+1);
	return responseCode;
      }
      else 
      {
	responseCode = parseInt(response);
	return responseCode;
      }
    } 
    catch (StringIndexOutOfBoundsException e) 
    {
      throw new ProtocolException("NNTP protocol exception: "+response);
    }
    catch (NumberFormatException e) 
    {
      e.printStackTrace();
      throw new ProtocolException("NNTP protocol exception: "+response);
    }
  }
	
  void send(String command) 
  throws IOException 
  {
    boolean debug = session.getDebug();
		
    if (debug)
    System.err.println("DEBUG: nntp: >"+command);
		
    out.write(command.getBytes());
    out.writeln();
    out.flush();
  }

  // Opens a newsgroup.
  synchronized void open(Newsgroup group) 
  throws MessagingException 
  {
    if (current!=null) 
    {
      if (current.equals(group))
      return;
      else
      close(current);
    }
    String name = group.getName();
    try 
    {
      send("GROUP "+name);
      int r = getResponse();
      switch (r) 
      {
	case GROUP_SELECTED:
	  try 
	  {
	    updateGroup(group, response);
	    group.open = true;
	    current = group;
	  }
	  catch (NumberFormatException e) 
	  {
	    throw new MessagingException("NNTP protocol exception: "+response, e);
	  }
	  break;
	case NO_SUCH_GROUP:
	  throw new MessagingException(response);
	case SERVER_ERROR:
	  if (response.toLowerCase().indexOf("timeout")>-1) 
	  {
	    close();
	    connect();
	    open(group);
	    break;
	  }
	default:
	  throw new MessagingException("unexpected server response ("+r+"): "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  // Updates a newsgroup with the most recent article counts.
  void updateGroup(Newsgroup newsgroup, String response) 
  throws IOException 
  {
    try 
    {
      StringTokenizer st = new StringTokenizer(response, " ");
      newsgroup.count = parseInt(st.nextToken());
      newsgroup.first = parseInt(st.nextToken());
      newsgroup.last = parseInt(st.nextToken());
			
      boolean debug = session.getDebug();
			
      if (debug)
      System.err.println("DEBUG: nntp: "+newsgroup.name+": "+newsgroup.count+" articles");
		
    }
    catch (NumberFormatException e) 
    {
      throw new ProtocolException("NNTP protocol exception");
    }
    catch (NoSuchElementException e) 
    {
      throw new ProtocolException("NNTP protocol exception");
    }
  }

  // Closes a newsgroup.
  synchronized void close(Newsgroup group) 
  throws MessagingException 
  {
    if (current!=null && !current.equals(group)) 
    close(current);
    group.open = false;
    if (memoryOptimisation) 
    {
      group.articles = null;
      System.gc();
    }
  }
	
  // Returns the (approximate) number of articles in a newsgroup.
  synchronized int getMessageCount(Newsgroup group) 
  throws MessagingException 
  {
    String name = group.getName();
    try 
    {
      send("GROUP "+name);
      switch (getResponse()) 
      {
	case GROUP_SELECTED:
	  try 
	  {
	    updateGroup(group, response);
	    current = group;
	    return group.count;
	  }
	  catch(NumberFormatException e) 
	  {
	    throw new MessagingException("NNTP protocol exception: "+response, e);
	  }
	case NO_SUCH_GROUP:
	  throw new MessagingException("No such group");
	case SERVER_ERROR:
	  if (response.toLowerCase().indexOf("timeout")>-1) 
	  {
	    close();
	    connect();
	    return getMessageCount(group);
	  }
	default:
	  throw new MessagingException("unexpected server response: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  // Returns the headers for an article.
  synchronized InternetHeaders getHeaders(Article article) 
  throws MessagingException 
  {
    String mid = article.messageId;
    if (mid==null) 
    {
      Newsgroup group = (Newsgroup)article.getFolder();
      if (!current.equals(group)) open(group);
      mid = Integer.toString(article.getMessageNumber());
    }
    try 
    {
      send("HEAD "+mid);
      switch (getResponse()) 
      {
	case ARTICLE_RETRIEVED_HEAD:
	  InternetHeaders headers = new InternetHeaders();
	  try 
	  {
	    String s;
	    while ((s = in.readLine()) != null) 
	    {
	      if (s.length() == 0 || ".".equals(s.trim()))
	      break;
	      headers.addHeaderLine(s);
	    }
	  } 
	  catch (IOException ioe) 
	  {
	    throw new MessagingException("Error in input stream", ioe);
	  }
	  return headers;
	case NO_GROUP_SELECTED:
	  throw new MessagingException("No group selected");
	case NO_ARTICLE_SELECTED:
	  throw new MessagingException("No article selected");
	case NO_SUCH_ARTICLE_IN_GROUP:
				//throw new MessagingException("No such article in group");
				//InternetHeaders h = new InternetHeaders();
				//h.addHeader("Subject", "No such article in group");
	  return null;
	case NO_SUCH_ARTICLE:
	  throw new MessagingException("No such article");
	case SERVER_ERROR:
	  if (response.toLowerCase().indexOf("timeout")>-1) 
	  {
	    close();
	    connect();
	    return getHeaders(article);
	  }
	default:
	  throw new MessagingException("unexpected server response: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }
	
  // Returns the content for an article.
  synchronized byte[] getContent(Article article) 
  throws MessagingException 
  {
    String mid = article.messageId;
    if (mid==null) 
    {
      Newsgroup group = (Newsgroup)article.getFolder();
      if (!current.equals(group)) 
      open(group);
      mid = Integer.toString(article.getMessageNumber());
    }
    try 
    {
      send("BODY "+mid);
      switch (getResponse()) 
      {
	case ARTICLE_RETRIEVED_BODY:
	  int max = fetchsize, len;
	  byte b[] = new byte[max];
	  MessageInputStream min = new MessageInputStream(in);
	  ByteArrayOutputStream bout = new ByteArrayOutputStream();
	  while ((len = min.read(b, 0, max))!=-1)
	  bout.write(b, 0, len);
	  return bout.toByteArray();
	case NO_GROUP_SELECTED:
	  throw new MessagingException("No group selected");
	case NO_ARTICLE_SELECTED:
	  throw new MessagingException("No article selected");
	case NO_SUCH_ARTICLE_IN_GROUP:
	  throw new MessagingException("No such article in group");
	case NO_SUCH_ARTICLE:
	  throw new MessagingException("No such article");
	case SERVER_ERROR:
	  if (response.toLowerCase().indexOf("timeout")>-1) 
	  {
	    close();
	    connect();
	    return getContent(article);
	  }
	default:
	  throw new MessagingException("unexpected server response: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  /**
   * Post an article.
   * @param article the article
   * @param addresses the addresses to post to.
   * @exception MessagingException if a messaging exception occurred or there were no newsgroup recipients
   */
  public synchronized void postArticle(Message article, Address[] addresses) 
  throws MessagingException 
  {
    Vector v = new Vector();
    for (int i=0; i<addresses.length; i++) 
    { // get unique newsgroup addresses
      if (addresses[i] instanceof NewsAddress && !v.contains(addresses[i]))
      v.addElement(addresses[i]);
    }
    NewsAddress[] a = new NewsAddress[v.size()]; v.copyInto(a);
    if (a.length==0) 
    throw new MessagingException("No newsgroups specified as recipients");
    for (int i=0; i<a.length; i++)
    post(article, a[i]);
  }
	
  // Posts an article to the specified newsgroup.
  void post(Message article, NewsAddress address) 
  throws MessagingException 
  {
    String group = address.getNewsgroup();
    try 
    {
      send("POST");
      switch (getResponse()) 
      {
	case SEND_ARTICLE:
	  if (session.getDebug())
	  article.writeTo(new MessageOutputStream(System.out));
	  MessageOutputStream mout = new MessageOutputStream(out);
	  article.writeTo(mout);
	  out.write("\n.\n".getBytes());
	  out.flush();
	  switch (getResponse()) 
	  {
	    case ARTICLE_POSTED:
	      break;
	    case POSTING_FAILED:
	      throw new MessagingException("Posting failed: "+response);
	    default:
	      throw new MessagingException(response);
	  }
	  break;
	case POSTING_NOT_ALLOWED:
	  throw new MessagingException("Posting not allowed");
	case POSTING_FAILED:
	  throw new MessagingException("Posting failed");
	case SERVER_ERROR:
	  if (response.toLowerCase().indexOf("timeout")>-1) 
	  {
	    connect();
	    post(article, address);
	    break;
	  }
	default:
	  throw new MessagingException("unexpected server response: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("I/O error", e);
    }
  }

  // Returns the newsgroups available in this store via the specified listing command.
  Newsgroup[] getNewsgroups(String command, boolean retry) 
  throws MessagingException 
  {
    String line = null;
    Vector vector = new Vector();
    try 
    {
      send(command);
      switch (getResponse()) 
      {
	case LISTING:
	  newsgroups.clear();
	  for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine()) 
	  {
	    StringTokenizer st = new StringTokenizer(line, " ");
	    String name = st.nextToken();
	    int last = (int)parseLong(st.nextToken());
	    int first = (int)parseLong(st.nextToken());
	    boolean posting = ("y".equals(st.nextToken().toLowerCase()));
	    Newsgroup group = (Newsgroup)getFolder(name);
	    group.first = first;
	    group.last = last;
	    group.postingAllowed = posting;
	    vector.addElement(group);
	    newsgroups.put(name, group);
	  }
	  break;
	case SERVER_ERROR:
	  if (!retry) 
	  {
	    if (response.toLowerCase().indexOf("timeout")>-1) 
	    {
	      close();
	      connect();
	      return getNewsgroups(command, true);
	    }
	    else
	    return getNewsgroups("LIST", false); // backward compatibility with rfc977
	  }
	default:
	  throw new MessagingException(command+" failed: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException(command+" failed: "+line, e);
    }
    catch (NumberFormatException e) 
    {
      throw new MessagingException(command+" failed: "+line, e);
    }
    catch (NoSuchElementException e) 
    {
      throw new MessagingException(command+" failed: "+line, e);
    }
    Newsgroup[] groups = new Newsgroup[vector.size()]; vector.copyInto(groups);
    return groups;
  }

  // Returns the articles for a newsgroup.
  Article[] getArticles(Newsgroup newsgroup) 
  throws MessagingException 
  {
    try 
    {
      return getOverview(newsgroup);
    }
    catch (MessagingException e)
    { // try rfc977
      try 
      {
	return getNewArticles(newsgroup, new Date(0L));
      }
      catch (MessagingException e2) 
      {
	if (responseCode==COMMAND_NOT_RECOGNIZED)
	throw e;
	else
	throw e2;
      }
    }
  }

  /**
   * Returns the newsgroups added to this store since the specified date.
   * @exception MessagingException if a messaging error occurred
   */
  Newsgroup[] getNewFolders(Date date) 
  throws MessagingException 
  {
    String datetime = getDateTimeString(date);
    Vector vector = new Vector();
    try 
    {
      send("NEWGROUPS "+datetime);
      switch (getResponse()) 
      {
	case LISTING_NEW:
	  String line;
	  for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine()) 
	  {
	    StringTokenizer st = new StringTokenizer(line, " ");
	    String name = st.nextToken();
	    int last = parseInt(st.nextToken());
	    int first = parseInt(st.nextToken());
	    boolean posting = ("y".equals(st.nextToken().toLowerCase()));
	    Newsgroup group = (Newsgroup)getFolder(name);
	    group.first = first;
	    group.last = last;
	    group.postingAllowed = posting;
	    vector.addElement(group);
	  }
	  break;
	default:
	  throw new MessagingException("Listing failed: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException("Listing failed", e);
    }
    catch (NumberFormatException e) 
    {
      throw new MessagingException("Listing failed", e);
    }
    Newsgroup[] groups = new Newsgroup[vector.size()]; vector.copyInto(groups);
    return groups;
  }

  // Returns the articles added to the specified newsgroup since the specified date.
  Article[] getNewArticles(Newsgroup newsgroup, Date date) 
  throws MessagingException 
  {
    if (!useNewNews)
    return new Article[0];
		
    String command = "NEWNEWS "+newsgroup.getName()+" "+getDateTimeString(date);
    Vector vector = new Vector();
    try 
    {
      send(command);
      switch (getResponse()) 
      {
	case LISTING_ARTICLES:
	  String line;
	  for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine()) 
	  {
	    Message article = getArticle(newsgroup, line);
	    vector.addElement(article);
	  }
	  break;
	default:
				// NEWNEWS is most often disabled by the administrator
	  useNewNews = false;
				//throw new MessagingException(command+" failed: "+response);
      }
    } 
    catch (IOException e) 
    {
      throw new MessagingException(command+" failed", e);
    }
    catch (NumberFormatException e) 
    {
      throw new MessagingException(command+" failed", e);
    }
    Article[] articles = new Article[vector.size()]; vector.copyInto(articles);
    return articles;
  }

  // Returns a GMT date-time formatted string for the specified date,
  // suitable as an argument to NEWGROUPS and NEWNEWS commands.
  String getDateTimeString(Date date) 
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    calendar.setTime(date);
    StringBuffer buffer = new StringBuffer();
    int field;
    String ZERO = "0"; // leading zero
    field = calendar.get(Calendar.YEAR)%100; buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    field = calendar.get(Calendar.MONTH)+1; buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    field = calendar.get(Calendar.DAY_OF_MONTH); buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    buffer.append(" ");
    field = calendar.get(Calendar.HOUR_OF_DAY); buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    field = calendar.get(Calendar.MINUTE); buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    field = calendar.get(Calendar.SECOND); buffer.append((field<10) ? ZERO+field : Integer.toString(field));
    buffer.append(" GMT");
    return buffer.toString();
  }

  /**
   * Returns the root folder.
   */
  public Folder getDefaultFolder() 
  throws MessagingException 
  {
    synchronized (this) {
      if (root==null) 
      root = new Root(this);
    }
    return root;
  }

  /**
   * Returns the newsgroup with the specified name.
   */
  public Folder getFolder(String name) 
  throws MessagingException 
  {
    return getNewsgroup(name);
  }

  /**
   * Returns the newsgroup specified as part of a URLName.
   */
  public Folder getFolder(URLName urlname) 
  throws MessagingException 
  {
    String group = urlname.getFile();
    int hashIndex = group.indexOf('#');
    if (hashIndex>-1) 
    group = group.substring(0, hashIndex);
    return getNewsgroup(group);
  }

  Newsgroup getNewsgroup(String name) 
  {
    Newsgroup newsgroup = (Newsgroup)newsgroups.get(name);
    if (newsgroup==null) 
    {
      newsgroup = new Newsgroup(this, name);
      newsgroups.put(name, newsgroup);
    }
    return newsgroup;
  }

  // Returns the article with the specified message-id for the newsgroup.
  Article getArticle(Newsgroup newsgroup, String mid) 
  throws MessagingException 
  {
    Article article = (Article)articles.get(mid);
    if (article==null) 
    {
      article = new Article(newsgroup, mid);
      articles.put(mid, article);
    }
    return article;
  }

  // -- NNTP extensions --

  int[] getArticleNumbers(Newsgroup newsgroup) 
  throws MessagingException 
  {
    String command = "LISTGROUP "+newsgroup.getName();
    Vector vector = new Vector();
    synchronized (this) 
    {
      try 
      {
	send(command);
	switch (getResponse()) 
	{
	  case GROUP_SELECTED:
	    String line;
	    for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine())
	    vector.addElement(line);
	    break;
	  case NO_GROUP_SELECTED:
	  case PERMISSION_DENIED:
	  default:
	    throw new MessagingException(command+" failed: "+response);
	}
      } 
      catch (IOException e) 
      {
	throw new MessagingException(command+" failed", e);
      }
      catch (NumberFormatException e) 
      {
	throw new MessagingException(command+" failed", e);
      }
    }
    int[] numbers = new int[vector.size()];
    for (int i=0; i<numbers.length; i++)
    numbers[i] = parseInt((String)vector.elementAt(i));
    return numbers;
  }

  private String[] overviewFormat, strippedOverviewFormat;

  String[] getOverviewFormat(boolean strip) 
  throws MessagingException 
  {
    if (overviewFormat==null) 
    {
      String command = "LIST OVERVIEW.FMT";
      synchronized (this) 
      {
	Vector vector = new Vector(), vector2 = new Vector();
	try 
	{
	  send(command);
	  switch (getResponse()) 
	  {
	    case LISTING:
	      String line;
	      for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine()) 
	      {
		vector.addElement(line);
		vector2.addElement(line.substring(0, line.indexOf(':')));
	      }
	      break;
	    case SERVER_ERROR:
	    default:
	      throw new MessagingException(command+" failed: "+response);
	  }
	} 
	catch (IOException e) 
	{
	  throw new MessagingException(command+" failed", e);
	}
	catch (NumberFormatException e) 
	{
	  throw new MessagingException(command+" failed", e);
	}
	overviewFormat = new String[vector.size()]; vector.copyInto(overviewFormat);
	strippedOverviewFormat = new String[vector2.size()]; vector2.copyInto(strippedOverviewFormat);
	if (session.getDebug()) 
	{
	  StringBuffer buffer = new StringBuffer();
	  buffer.append("DEBUG: nntp: overview format: ");
	  for (int i=0; i<strippedOverviewFormat.length;i++) 
	  {
	    if (i>0)
	    buffer.append(", ");
	    buffer.append(strippedOverviewFormat[i]);
	  }
	  System.out.println(buffer.toString());
	}
      }
    }
    if (strip)
    return strippedOverviewFormat;
    else
    return overviewFormat;
  }

  // Prefetches header information for the specified messages, if possible
  Article[] getOverview(Newsgroup newsgroup) 
  throws MessagingException 
  {
    String name = newsgroup.getName();
    String[] format = getOverviewFormat(false);
    String command = "GROUP "+name;
    if (current==null || !name.equals(current.getName())) 
    { // select the group
      synchronized (this) 
      {
	try 
	{
	  send(command);
	  switch (getResponse()) 
	  {
	    case GROUP_SELECTED:
	      try 
	      {
		updateGroup(newsgroup, response);
		current = newsgroup;
	      }
	      catch(NumberFormatException e) 
	      {
		throw new MessagingException("NNTP protocol exception: "+response, e);
	      }
	    case NO_SUCH_GROUP:
	      throw new MessagingException("No such group");
	    case SERVER_ERROR:
	      if (response.toLowerCase().indexOf("timeout")>-1) 
	      {
		close();
		connect();
		return getOverview(newsgroup);
	      }
	    default:
	      throw new MessagingException(command+" failed: "+response);
	  }
	} 
	catch (IOException e) 
	{
	  throw new MessagingException(command+" failed", e);
	}
      }
    }
    command = "XOVER "+newsgroup.first+"-"+newsgroup.last;
    Vector av = new Vector(Math.max(newsgroup.last-newsgroup.first, 10));
    String line = null;
    synchronized (this) 
    {
      try 
      {
	send(command);
	switch (getResponse()) 
	{
	  case LISTING_OVERVIEW:
	    int count = 0, length = (newsgroup.last-newsgroup.first);
	    processStatusEvent(new StatusEvent(this, StatusEvent.OPERATION_START, "Retrieving messages", 0, length, 0));
	    for (line=in.readLine(); line!=null && !".".equals(line); line = in.readLine()) 
	    {
	      String msgnumStr = line.trim();
	      int tabIndex = line.indexOf('\t');
	      if (tabIndex>-1)
	      msgnumStr = line.substring(0, tabIndex).trim();
	      try 
	      {
		int msgnum = parseInt(msgnumStr);
		Article article = new Article(newsgroup, msgnum);
		article.addXoverHeaders(getOverviewHeaders(format, line, tabIndex));
		av.addElement(article);
		if ((++count%50) == 0)
		processStatusEvent(new StatusEvent(this, StatusEvent.OPERATION_UPDATE, "Retrieved "+count+" of "+length+" messages", 0, length, count));
	      }
	      catch (NumberFormatException e) 
	      {
		perr("error: no article number", line);
	      }
	    }
	    processStatusEvent(new StatusEvent(this, StatusEvent.OPERATION_END, "Done", 0, length, length));
	    if (session.getDebug()) 
	    {
	      System.out.println("DEBUG: nntp: overview returned "+count+" articles");
	    }
	    break;
	  case NO_ARTICLE_SELECTED:
	  case PERMISSION_DENIED:
	    break;
	  case NO_GROUP_SELECTED:
	  case SERVER_ERROR:
	  default:
	    throw new MessagingException(command+" failed: "+response);
	}
      } 
      catch (IOException e) 
      {
	throw new MessagingException(command+" failed", e);
      }
      catch (NumberFormatException e) 
      {
	throw new MessagingException(command+" failed", e);
      }
    }
    Article[] articles = new Article[av.size()]; av.copyInto(articles);
    return articles;
  }

  void perr(String key, String line) 
  {
    int tabIndex = line.indexOf('\t');
    if (tabIndex>-1) 
    {
      String msgnumStr = line.substring(0, tabIndex).trim();
      System.err.print(key+": '"+msgnumStr+"' ("+msgnumStr.length()+"), bytes: ");
      byte[] b = msgnumStr.getBytes();
      for (int i=0; i<b.length; i++) 
      {
	System.err.print(b[i]);
	System.err.print(" ");
      }
      System.err.println();
    }
  }
		

  // Returns an InternetHeaders object representing the headers stored in an xover response line.
  InternetHeaders getOverviewHeaders(String[] format, String line, int startIndex) 
  {
    InternetHeaders headers = new InternetHeaders();
    try 
    {
      for (int i=0; i<format.length; i++) 
      {
	int colonIndex = format[i].indexOf(':');
	String key = format[i].substring(0, colonIndex);
	boolean full = "full".equals(format[i].substring(colonIndex+1, format[i].length()));
	int tabIndex = line.indexOf('\t', startIndex+1);
	if (tabIndex<0) 
	tabIndex = line.length();
	String value = line.substring(startIndex+1, tabIndex);
	if (full)
	value = value.substring(value.indexOf(':')+1).trim();
	headers.addHeader(key, value);
	startIndex = tabIndex;
      }
    } 
    catch (StringIndexOutOfBoundsException e) 
    {
      System.err.println("nntp: bad overview line '"+line+"'");
    }
    return headers;
  }

  boolean validateOverviewHeader(String key) 
  throws MessagingException 
  {
    String[] format = getOverviewFormat(true);
    for (int i=0; i<format.length; i++) 
    {
      if (key.equalsIgnoreCase(format[i]))
      return true;
    }
    return false;
  }

  private int parseInt(String s) 
  throws NumberFormatException 
  {
    try 
    {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e) 
    {
      // it may be that there are null bytes in the string.
      // remove these and try again.
      byte[] b = s.getBytes();
      int len = removeBytes(b, 0, b.length, (byte)0);
      s = new String(b, 0, len);
      return Integer.parseInt(s);
    }
  }

  private long parseLong(String s) 
  throws NumberFormatException 
  {
    try
    {
      return Long.parseLong(s);
    }
    catch (NumberFormatException e) 
    {
      // it may be that there are null bytes in the string.
      // remove these and try again.
      byte[] b = s.getBytes();
      int len = removeBytes(b, 0, b.length, (byte)0);
      s = new String(b, 0, len);
      return Long.parseLong(s);
    }
  }

  private static int removeBytes(byte[] b, int off, int len, byte target) 
  {
    for (int index = indexOfByte(b, off, len, target); index>-1; index = indexOfByte(b, off, len, target)) 
    {
      for (int i=index; i<b.length-1; i++)
      b[i] = b[i+1];
      len--;
    }
    return len;
  }

  private static int indexOfByte(byte[] b, int off, int len, byte target) 
  {
    for (int i=off; i<off+len; i++)
    if (b[i]==target) return i;
    return -1;
  }

  public void addStatusListener(StatusListener l) 
  {
    synchronized (statusListeners) 
    {
      statusListeners.addElement(l);
    }
  }
			
  public void removeStatusListener(StatusListener l) 
  {
    synchronized (statusListeners) 
    {
      statusListeners.removeElement(l);
    }
  }
			
  protected void processStatusEvent(StatusEvent event) 
  {
    StatusListener[] listeners;
    synchronized (statusListeners) 
    {
      listeners = new StatusListener[statusListeners.size()];
      statusListeners.copyInto(listeners);
    }
    switch (event.getType()) 
    {
      case StatusEvent.OPERATION_START:
	for (int i=0; i<listeners.length; i++)
	listeners[i].statusOperationStarted(event);
	break;
      case StatusEvent.OPERATION_UPDATE:
	for (int i=0; i<listeners.length; i++)
	listeners[i].statusProgressUpdate(event);
	break;
      case StatusEvent.OPERATION_END:
	for (int i=0; i<listeners.length; i++)
	listeners[i].statusOperationEnded(event);
	break;
    }
  }

  /*
   * Manages multiplexing of store connections.
   */
  static Hashtable stores;
	
  static void addStore(NNTPStore store) 
  {
    if (stores==null)
    stores = new Hashtable();
    stores.put(store.socket, store);
  }
	
  static void removeStore(NNTPStore store) 
  {
    stores.remove(store.socket);
  }
	
  static NNTPStore getStore(InetAddress address, int port) 
  {
    if (stores==null)
    return null;
    for (Enumeration enum = stores.keys(); enum.hasMoreElements(); ) 
    {
      Socket ss = (Socket)enum.nextElement();
      InetAddress sa = ss.getInetAddress();
      int sp = ss.getPort();
      if (sp==port && sa.equals(address))
      return (NNTPStore)stores.get(ss);
    }
    return null;
  }
	
  /**
   * The root holds the newsgroups in an NNTPStore.
   */
  class Root 
  extends Folder 
  {

    /**
     * Constructor.
     */
    protected Root(Store store) 
    {
      super(store); 
    }
	   
    /**
     * Returns the name of this folder.
     */
    public String getName() 
    {
      return "/"; 
    }
	   
    /**
     * Returns the full name of this folder.
     */
    public String getFullName()
    {
      return getName(); 
    }
	   
    /**
     * Returns the type of this folder.
     */
    public int getType()
    throws MessagingException
    { 
      return HOLDS_FOLDERS; 
    }
	   
    /**
     * Indicates whether this folder exists.
     */
    public boolean exists() 
    throws MessagingException
    {
      return true; 
    }
	   
    /**
     * Indicates whether this folder contains any new articles.
     */
    public boolean hasNewMessages()
    throws MessagingException
    { 
      return false; 
    }
	   
    /**
     * Opens this folder.
     */
    public void open(int mode) 
    throws MessagingException 
    {
      if (mode!=this.READ_ONLY) 
      throw new MessagingException("Folder is read-only");
    }
	   
    /**
     * Closes this folder.
     */
    public void close(boolean expunge)
    throws MessagingException
    {}
	   
    /**
     * Expunges this folder.
     */
    public Message[] expunge()
    throws MessagingException
    {
      return new Message[0]; 
    }
	   
    /**
     * Indicates whether this folder is open.
     */
    public boolean isOpen()
    {
      return true; 
    }
	   
    /**
     * Returns the permanent flags for this folder.
     */
    public Flags getPermanentFlags()
    {
      return new Flags(); 
    }
	   
    /**
     * Returns the number of articles in this folder.
     */
    public int getMessageCount()
    throws MessagingException 
    {
      return 0; 
    }
	   
    /**
     * Returns the articles in this folder.
     */
    public Message[] getMessages() 
    throws MessagingException 
    {
      throw new MessagingException("Folder can't contain messages");
    }
	   
    /**
     * Returns the specified message in this folder.
     * Since NNTP articles are not stored in sequential order,
     * the effect is just to reference articles returned by getMessages().
     */
    public Message getMessage(int msgnum) 
    throws MessagingException 
    {
      throw new MessagingException("Folder can't contain messages");
    }
	   
    /**
     * Root folder is read-only.
     */
    public void appendMessages(Message aarticle[]) 
    throws MessagingException 
    {
      throw new MessagingException("Folder is read-only");
    }
	   
    /**
     * Does nothing.
     */
    public void fetch(Message articles[], FetchProfile fetchprofile) 
    throws MessagingException 
    {
    }
	   
    /**
     * This folder does not have a parent.
     */
    public Folder getParent() 
    throws MessagingException 
    {
      return null; 
    }
	   
    /**
     * Returns the newsgroups on the server.
     */
    public Folder[] list() 
    throws MessagingException 
    {
      synchronized (store) 
      {
	return getNewsgroups("LIST ACTIVE", false);
      }
    }
	   
    /**
     * Returns the newsgroups on the server.
     */
    public Folder[] list(String pattern) 
    throws MessagingException 
    {
      synchronized(store) 
      {
	return getNewsgroups(pattern, false);
      }
    }
	   
    /**
     * Returns the subscribed newsgroups on the server.
     */
    public Folder[] listSubscribed() 
    throws MessagingException 
    {
      Vector groups = new Vector();
      for (Enumeration enum = newsgroups.elements(); enum.hasMoreElements(); ) 
      {
	Newsgroup group = (Newsgroup)enum.nextElement();
	if (group.isSubscribed())
	groups.addElement(group);
      }
      Folder[] list = new Folder[groups.size()]; groups.copyInto(list);
      return list;
    }
	   
    /**
     * Returns the subscribed newsgroups on the server.
     */
    public Folder[] listSubscribed(String pattern) 
    throws MessagingException 
    {
      return listSubscribed();
    }
	   
    /**
     * Returns the newsgroup with the specified name.
     */
    public Folder getFolder(String name) 
    throws MessagingException 
    {
      return getNewsgroup(name);
    }
	   
    /**
     * Returns the separator character.
     */
    public char getSeparator() 
    throws MessagingException 
    {
      return '.';
    }
	   
    /**
     * Root folders cannot be created, deleted, or renamed.
     */
    public boolean create(int i) 
    throws MessagingException 
    {
      throw new MessagingException("Folder cannot be created");
    }
	   
    /**
     * Root folders cannot be created, deleted, or renamed.
     */
    public boolean delete(boolean flag) 
    throws MessagingException 
    {
      throw new MessagingException("Folder cannot be deleted");
    }
	   
    /**
     * Root folders cannot be created, deleted, or renamed.
     */
    public boolean renameTo(Folder folder) 
    throws MessagingException 
    {
      throw new MessagingException("Folder cannot be renamed");
    }
	   
  }
	
}
