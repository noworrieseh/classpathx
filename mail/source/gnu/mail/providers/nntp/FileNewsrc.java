/*
 * FileNewsrc.java
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
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.URLName;

/**
 * A .newsrc configuration on a filesystem.
 *
 * @author Torgeir Veimo <tv@sevenmountains.no>
 * @author Gautam Mani <g_mani@vsnl.com>
 * @version 1.3.2
 */
public class FileNewsrc 
implements Newsrc 
{
	
  protected File file;
  protected NNTPStore store;
  protected Hashtable	lines;
  private boolean debug;
  private boolean dirty;
	
  /**
   * Constructs a FileNewsrc for the specified url.
   */
  public FileNewsrc(NNTPStore store, URLName url) 
  {
    this.store = store;
    debug = store.getSession().getDebug();

    lines = new Hashtable();
		
    if (url != null) 
    {
      String home = System.getProperty("user.home");
      String filename = ".newsrc-"+url.getHost();
			
      file = new File(home, filename);
      /*
	if (!file.exists())
	file = null;
      */
    }	
  }
	
		
  protected BufferedReader getReader() 
  {
    checkFile();
    try 
    {
      if (file != null) 
      {
	if (debug)
	System.err.println("DEBUG: nntp: reading subscription file " + file.getAbsolutePath());
	BufferedReader reader = new BufferedReader(new FileReader(file));
		
	return reader;
      }
    } 
    catch (IOException ioe) 
    {
      System.err.println("ERROR: nntp: error reading newsrc file");
    }
    return null;
  }
			
  protected BufferedWriter getWriter() 
  {
    checkFile();
    try 
    {
      if (file != null) 
      {
	if (debug)
	System.err.println("DEBUG: nntp: writing subscription file " + file.getAbsolutePath());
	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
	return writer;
      }
    } 
    catch (IOException ioe) 
    {
      System.err.println("ERROR: nntp: error writing newsrc file");
    }
    return null;
  }
	
  /**
   * Returns a list currently subscribed newsgroups.
   */
  public Newsgroup[] list() 
  throws MessagingException 
  {
    Vector v = new Vector();

    try 
    {
      BufferedReader reader = getReader();
      if (debug)
      System.err.println("DEBUG: nntp: enumerating newsgroups");
      String line;
      if (reader != null) 
      {
	while ((line = reader.readLine())!=null) 
	{
	  //System.err.println("line:" + line);
	  StringTokenizer st = new StringTokenizer(line, ":!", true);
	  try 
	  {
	    String name = st.nextToken();
	    Newsgroup group = new Newsgroup(store, name);

	    NewsrcLine rcLine = new NewsrcLine(line);
	    lines.put(name, rcLine);
						
	    v.addElement(group);
	  }
	  catch (NoSuchElementException e)
	  {}
	}
      }
      if (debug)
      System.err.println("DEBUG: nntp: "+v.size()+" newsgroups found");
    } 
    catch (FileNotFoundException e) 
    {
      System.err.println("ERROR: nntp: newsrc file not found");
    }
    catch (IOException e) 
    {
      System.err.println("ERROR: nntp: unable to read newsrc file");
    }
    catch (SecurityException e)
    { // not allowed to read file
      System.err.println("ERROR: nntp: no read permission on newsrc file");
    }
    Newsgroup[] n = new Newsgroup[v.size()]; v.copyInto(n);
    return n;
  }
	
  /**
   * Stores updated SEEN flags for articles.
   */
  public void close() 
  throws MessagingException 
  {
    // not yet...
    updateSubscription();
  }
	
  public boolean isSubscribed(String newsgroup) 
  {
    if (lines.containsKey(newsgroup)) 
    {
      return ((NewsrcLine)lines.get(newsgroup)).isSubscribed();
    }
    return false;
  }
	
  public void setSubscribed(String newsgroup, boolean subs) 
  {
    if (lines.containsKey(newsgroup)) 
    {
      ((NewsrcLine)lines.get(newsgroup)).setSubscribed(subs);
      dirty = true;
    } else 
    {
      NewsrcLine line = new NewsrcLine(newsgroup + "!");
      line.setSubscribed(subs);
      lines.put(newsgroup, line);
      dirty = true;
    }
  }
	
  public boolean isSeen(String newsgroup, int article) 
  {
    if (lines.containsKey(newsgroup)) 
    {
      return ((NewsrcLine)lines.get(newsgroup)).isSeen(article);
    }
    return false;
  }
		
  public void setSeen(String newsgroup, int article, boolean seen) 
  {
    if (lines.containsKey(newsgroup)) 
    {
      ((NewsrcLine)lines.get(newsgroup)).setSeen(article, seen);
      dirty = true;
    }		
  }
	
  public void checkFile() 
  {
    if (file == null) 
    {
			
      // Must create a new .newsrc file.
      // If this fails, we don't store any subscription data.

      String home = System.getProperty("user.home");
      String filename = ".newsrc-"+store.getURLName().getHost();

      file = new File(home, filename);
    }
  }
		
  public void updateSubscription() 
  {
    if (!dirty)
    return;
		
    try 
    {
      String name;
				
      BufferedWriter bwriter = getWriter();
			
      if (bwriter != null) 
      {
	PrintWriter writer = new PrintWriter(bwriter);
	for(Enumeration enum = lines.keys(); enum.hasMoreElements() ;) 
	{
	  name = (String) enum.nextElement();
	  NewsrcLine rcLine = (NewsrcLine) lines.get(name);
	  writer.println(name + rcLine.toString());
	}
				
	writer.flush();
	writer.close();
      }
			
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
      //		} catch (SecurityException e) { // not allowed to read file
    }
  }
	
  /**
   * Creates a .newsrc for the specified nntp url.
   */
  public static void main(String[] args) 
  {
    try 
    {
      javax.mail.Session session = javax.mail.Session.getInstance(System.getProperties(), null);
      session.setDebug(true);
      javax.mail.URLName url = new javax.mail.URLName(args[0]);
      NNTPStore store = new NNTPStore(session, url);
      store.connect();
      store.getDefaultFolder().list();
      store.close();
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
	
	
  public class NewsrcLine 
  {
		
    protected RangeVector seen = new RangeVector();
    protected boolean subscribed = false;

    public NewsrcLine(String line) 
    {
      try 
      {
	StringTokenizer st = new StringTokenizer(line, "!:,", true);
	String token = st.nextToken(); // discard name
				
	token = st.nextToken();				
	if (token.indexOf("!") == -1)
	subscribed = true;

				// ok, parse the string for "seen" articles..
	while(st.hasMoreTokens()) 
	{
	  token = st.nextToken();
	  if (token.indexOf(",") != -1)
	  continue;
					
	  if (token.indexOf("-") != -1) 
	  {
	    // parse a range 
	    String fromString = token.substring(0, token.indexOf("-")).trim();;
	    String toString = token.substring(token.indexOf("-")+1).trim();
						
	    int f = Integer.parseInt(fromString);
	    int t = Integer.parseInt(toString);
	    if (f != -1 && t != -1)
	    seen.addRange(f,t);
	  }
	  else 
	  {
	    // parse a single number 
	    int num = Integer.parseInt(token.trim());
	    if (num != -1)
	    seen.addRange(num);
	  }
	}
				
      } 
      catch (NumberFormatException e) 
      {
	System.err.println("ERROR: nntp: format error parsing seen data");
      }
      catch (NoSuchElementException e) 
      {
	System.err.println("ERROR: nntp: internal error parsing seen data");
      }
    }
		
			
		
    public boolean isSubscribed() 
    {
      return subscribed;
    }
		
    public void setSubscribed(boolean subs) 
    {
      subscribed = subs;	
    }
		
    public boolean isSeen(int num) 
    {
      return seen.containsRange(num);
    }

		
    public void setSeen(int num, boolean read) 
    {
      if (read)
      seen.addRange(num);
      else
      seen.removeRange(num);
      if(debug)
      System.err.println("line: " + seen.toString());			
    }

    public String toString() 
    {
      StringBuffer buf = new StringBuffer();
      if (!subscribed)
      buf.append("!");
      else			
      buf.append(":");
			
      buf.append(seen.toString());
      return buf.toString();
    }
		
    // A RangeVector holds a series of ranges that are ordered and non-overlapping.
  } 	
	
  class RangeVector 
  {
    Vector v = new Vector();
	   
    public boolean containsRange(int num) 
    {
      for (int i = 0; i < v.size(); i++) 
      {
	Range r = (Range) v.elementAt(i);
	if (r.contains(num)) return true;
      }
      return false;
    }
	   
    public void addRange(int start, int end) 
    {
      if (start > end)
      {
	int tmp = end; end = start; start = tmp;	 
      }
		   
      // nonoptimal...
      for (int i = start; i <= end; i++)
      addRange(i);
    }
	   
    public void addRange(int num) 
    {
      Range found = null;
      if (v.isEmpty()) 
      {
	v.addElement(new Range(num));
	return;
      }
      for (int i = 0; i < v.size(); i++) 
      {
	Range r = (Range) v.elementAt(i);
			   
	if (num == (r.from - 1)) 
	{
	  if (found != null) 
	  {
	    found.to = r.to;
	    v.removeElement(r);
	    return;
	  } 
	  r.from -= 1;										
	  found = r;
	}
	else if (num == (r.to + 1)) 
	{
	  if (found != null) 
	  {
	    // duplicate found 
	    v.removeElement(r);
	    return;
	  }
	  r.to += 1;
	  found = r;
	} 
	else if (num == r.from || num == r.to)
	return;
      }
      if (found == null) 
      {
	for (int i = 0; i < v.size(); i++) 
	{
	  Range r = (Range) v.elementAt(i);
	  if (num < r.from) 
	  {
	    v.insertElementAt(new Range(num), i);
	    return;
	  }
	}
	v.addElement(new Range(num));
      }
    }
	   
	   
    public void removeRange(int num) 
    {
		   
      // Possibly split a range... Messy!
      for (int i = 0; i < v.size(); i++) 
      {
	Range r = (Range) v.elementAt(i);
			   
	if (r.from == r.to && r.from == num) 
	{
	  v.removeElementAt(i);
	}
	else 
	{
	  if (num == r.from)
	  r.from += 1;										
	  else if (num == r.to) 
	  r.to -= 1;
	  else if (r.contains(num)) 
	  {
	    Range n = new Range(num+1, r.to);
	    r.to = num -1;
					   
	    for (int j = 0; j < v.size(); j++) 
	    {
	      r = (Range) v.elementAt(j);
	      if ((num + 1) < r.from) 
	      {
		v.insertElementAt(n, j);
		return;
	      }
	    }
	    v.addElement(n);
	  }					
	}
      }
    }
	   
    public String toString() 
    {
      StringBuffer buf = new StringBuffer();
		   
      boolean first = true;
      Enumeration enum = v.elements();
      while(enum.hasMoreElements()) 
      {
	Range r = (Range) enum.nextElement();
	if (first) 
	{
	  buf.append(r.toString());
	  first = false;
	}
	else 
	buf.append(",").append(r.toString());
      }
      return buf.toString();
    }
  }
	
  // A range is either a single integer or a range between two integers.
  class Range
  {
    int from;
    int to;
	   
    public Range(int i)
    {
      from = i; to = i;	
    }
	   
    public Range(int f, int t) 
    {
      if (f > t)
      {
	from = t; to = f; 
      }
      else
      {
	from = f; to = t; 
      }
    }
	   
    public boolean contains(int num) 
    {
      return (num < from || num > to) ? false : true; 
    }
	   
    public String toString() 
    {
      StringBuffer buf = new StringBuffer();
      buf.append(from);
      if (from != to)
      buf.append("-").append(to);
      return buf.toString();
    }
  }
	
}
