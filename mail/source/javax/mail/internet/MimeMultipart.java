/*
 * MimeMultipart.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
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

package javax.mail.internet;

import java.io.*;
import javax.activation.DataSource;
import javax.mail.*;
import gnu.mail.util.CRLFInputStream;
import gnu.mail.util.CRLFOutputStream;

/**
 * The MimeMultipart class is an implementation of the abstract Multipart 
 * class that uses MIME conventions for the multipart data.
 * <p>
 * A MimeMultipart is obtained from a MimePart whose primary type is
 * "multipart" (by invoking the part's <code>getContent()</code> method)
 * or it can be created by a client as part of creating a new MimeMessage.
 * <p>
 * The default multipart subtype is "mixed".
 * The other multipart subtypes, such as "alternative", "related", and so on,
 * can be implemented as subclasses of MimeMultipart with additional methods 
 * to implement the additional semantics of that type of multipart content.
 * The intent is that service providers, mail JavaBean writers and mail 
 * clients will write many such subclasses and their Command Beans,
 * and will install them into the JavaBeans Activation Framework,
 * so that any JavaMail implementation and its clients can transparently 
 * find and use these classes.
 * Thus, a MIME multipart handler is treated just like any other type handler,
 * thereby decoupling the process of providing multipart handlers from 
 * the JavaMail API. Lacking these additional MimeMultipart subclasses,
 * all subtypes of MIME multipart data appear as MimeMultipart objects.
 * <p>
 * An application can directly construct a MIME multipart object of any 
 * subtype by using the MimeMultipart(String subtype) constructor.
 * For example, to create a "multipart/alternative" object,
 * use <code>new MimeMultipart("alternative")</code>.
 */
public class MimeMultipart
  extends Multipart
{

  /**
   * The DataSource supplying our InputStream.
   */
  protected DataSource ds;

  /**
   * Have we parsed the data from our InputStream yet?
   * Defaults to true; set to false when our constructor is given 
   * a DataSource with an InputStream that we need to parse.
   */
  protected boolean parsed;

  /**
   * Default constructor. 
   * An empty MimeMultipart object is created. 
   * Its content type is set to "multipart/mixed".
   * A unique boundary string is generated and this string is set up
   * as the "boundary" parameter for the <code>contentType</code> field.
   * <p>
   * MimeBodyParts may be added later.
   */
  public MimeMultipart()
  {
    this("mixed");
  }

  /**
   * Construct a MimeMultipart object of the given subtype.
   * A unique boundary string is generated and this string is set up
   * as the "boundary" parameter for the <code>contentType</code> field.
   * <p>
   * MimeBodyParts may be added later.
   */
  public MimeMultipart(String subtype)
  {
    String boundary = MimeUtility.getUniqueBoundaryValue();
    ContentType ct = new ContentType("multipart", subtype, null);
    ct.setParameter("boundary", boundary);
    contentType = ct.toString();
    parsed = true;
  }

  /**
   * Constructs a MimeMultipart object and its bodyparts from the given
   * DataSource.
   * <p>
   * This constructor handles as a special case the situation where the given
   * DataSource is a MultipartDataSource object. In this case, this method 
   * just invokes the superclass (i.e., Multipart) constructor that takes a
   * MultipartDataSource object.
   * <p>
   * Otherwise, the DataSource is assumed to provide a MIME multipart byte
   * stream. The parsed flag is set to false. When the data for the body parts
   * are needed, the parser extracts the "boundary" parameter from the content
   * type of this DataSource, skips the 'preamble' and reads bytes till the
   * terminating boundary and creates MimeBodyParts for each part of the 
   * stream.
   * @param ds DataSource, can be a MultipartDataSource
   */
  public MimeMultipart(DataSource ds)
    throws MessagingException
  {
    if (ds instanceof MessageAware)
    {
      MessageContext mc = ((MessageAware)ds).getMessageContext();
      setParent(mc.getPart());
    }
    if (ds instanceof MultipartDataSource)
    {
      setMultipartDataSource((MultipartDataSource)ds);
      parsed = true;
    }
    else
    {
      this.ds = ds;
      contentType = ds.getContentType();
      parsed = false;
    }
  }

  /**
   * Set the subtype. 
   * This method should be invoked only on a new MimeMultipart object 
   * created by the client. The default subtype of such a multipart object 
   * is "mixed".
   * @param subtype Subtype
   */
  public void setSubType(String subtype)
    throws MessagingException
  {
    ContentType ct = new ContentType(contentType);
    ct.setSubType(subtype);
    contentType = ct.toString();
  }

  /**
   * * Return the number of enclosed BodyPart objects.
   */
  public int getCount()
    throws MessagingException
  {
    synchronized (this)
    {
      parse();
      return super.getCount();
    }
  }

  /**
   * Get the specified BodyPart.
   * BodyParts are numbered starting at 0.
   * @param index the index of the desired BodyPart
   * @exception MessagingException if no such BodyPart exists
   */
  public BodyPart getBodyPart(int index)
    throws MessagingException
  {
    synchronized (this)
    {
      parse();
      return super.getBodyPart(index);
    }
  }

  /**
   * Get the MimeBodyPart referred to by the given ContentID (CID).
   * Returns null if the part is not found.
   * @param CID the ContentID of the desired part
   */
  public BodyPart getBodyPart(String CID)
    throws MessagingException
  {
    synchronized (this)
    {
      parse();
      int count = getCount();
      for (int i = 0; i<count; i++)
      {
        MimeBodyPart bp = (MimeBodyPart)getBodyPart(i);
        String contentID = bp.getContentID();
        if (contentID!=null && contentID.equals(CID))
          return bp;
      }
      return null;
    }
  }

  /**
   * Update headers.
   * The default implementation here just calls the <code>updateHeaders</code>
   * method on each of its children BodyParts.
   * <p>
   * Note that the boundary parameter is already set up when a new and empty
   * MimeMultipart object is created.
   * <p>
   * This method is called when the <code>saveChanges</code> method is
   * invoked on the Message object containing this Multipart.
   * This is typically done as part of the Message send process,
   * however note that a client is free to call it any number of times.
   * So if the header updating process is expensive for a specific 
   * MimeMultipart subclass, then it might itself want to track whether
   * its internal state actually did change,
   * and do the header updating only if necessary.
   */
  protected void updateHeaders()
    throws MessagingException
  {
    synchronized (parts)
    {
      for (int i = 0; i<parts.size(); i++)
        ((MimeBodyPart)parts.get(i)).updateHeaders();
    }
  }

  /**
   * Iterates through all the parts and outputs each Mime part 
   * separated by a boundary.
   * @exception IOException if an IO related exception occurs
   */
  public void writeTo(OutputStream os)
    throws IOException, MessagingException
  {
    parse();
    ContentType ct = new ContentType(contentType);
    StringBuffer buffer = new StringBuffer();
    buffer.append("--");
    buffer.append(ct.getParameter("boundary"));
    String boundary = buffer.toString();
    
    CRLFOutputStream crlfos = null;
    if (os instanceof CRLFOutputStream)
      crlfos = (CRLFOutputStream)os;
    else
      crlfos = new CRLFOutputStream(os);
    
    synchronized (parts)
    {
      for (int i = 0; i<parts.size(); i++)
      {
        crlfos.write(boundary);
        crlfos.writeln();
        crlfos.flush();
        ((MimeBodyPart)parts.get(i)).writeTo(os);
        crlfos.writeln();
      }
    }

    buffer.append("--");
    boundary = buffer.toString();
    crlfos.write(boundary);
    crlfos.writeln();
    crlfos.flush();
  }

  /**
   * Parse the InputStream from our DataSource, constructing the appropriate
   * MimeBodyParts.
   * The parsed flag is set to true, and if true on entry nothing is done.
   * This method is called by all other methods that need data for the body 
   * parts, to make sure the data has been parsed.
   */
  protected void parse()
    throws MessagingException
  {
    if (parsed)
      return;
    synchronized (this)
    {
      InputStream is = null;
      SharedInputStream sis = null;
      try
      {
        is = ds.getInputStream();
        if (is instanceof SharedInputStream)
          sis = (SharedInputStream)is;
        // buffer it
        if (!(is instanceof ByteArrayInputStream) && 
            !(is instanceof BufferedInputStream))
          is = new BufferedInputStream(is);
        
        ContentType ct = new ContentType(contentType);
        StringBuffer buffer = new StringBuffer();
        buffer.append("--");
        buffer.append(ct.getParameter("boundary"));
        String boundary = buffer.toString();
        
        byte[] bbytes = boundary.getBytes();
        int blen = bbytes.length;
        
        CRLFInputStream crlfis = new CRLFInputStream(is);
        String line;
        while ((line = crlfis.readLine())!=null)
        {
          if (line.trim().equals(boundary))
            break;
        }
        if (line==null)
          throw new MessagingException("No start boundary");
        
        long start = 0L, end = 0L;
        for (boolean done = false; !done;)
        {
          InternetHeaders headers = null;
          if (sis!=null)
          {
            start = sis.getPosition();
            while ((line = crlfis.readLine())!=null && line.length()>0);
            if (line==null)
              throw new IOException("EOF before content body");
          }
          else
          {
            headers = createInternetHeaders(is);
          }
          ByteArrayOutputStream bos = null;
          if (sis==null)
            bos = new ByteArrayOutputStream();

          // NB this routine uses the InputStream.mark() method
          // if it is not supported by the underlying stream we will run into
          // problems
          boolean eol = true;
          int last = -1;
          int afterLast = -1;
          while (true)
          {
            int c;
            if (eol)
            {
              is.mark(blen+1024);
              int pos = 0;
              while (pos<blen)
              {
                if (is.read()!=bbytes[pos])
                  break;
                pos++;
              }
              
              if (pos==blen)
              {
                c = is.read();
                if (c=='-' && is.read()=='-')
                {
                  done = true;
                  break;
                }
                while (c==' ' || c=='\t')
                  c = is.read();
                if (c=='\r')
                {
                  is.mark(1);
                  if (is.read()!='\n')
                    is.reset();
                  break;
                }
                if (c=='\n')
                  break;
              }
              if (bos!=null && last!=-1)
              {
                bos.write(last);
                if (afterLast!=-1)
                  bos.write(afterLast);
                last = afterLast = -1;
              }
              is.reset();
            }
            c = is.read();
            if (c<0)
            {
              done = true;
              break;
            }
            else if (c=='\r' || c=='\n')
            {
              eol = true;
              if (sis!=null)
                end = sis.getPosition()-1L;
              last = c;
              if (c=='\r')
              {
                is.mark(1);
                if ((c = is.read())=='\n')
                  afterLast = c;
                else
                  is.reset();
              }
            }
            else
            {
              eol = false;
              if (bos!=null)
                bos.write(c);
            }
          }
          
          // Create a body part from the stream
          MimeBodyPart bp;
          if (sis!=null)
            bp = createMimeBodyPart(sis.newStream(start, end));
          else
            bp = createMimeBodyPart(headers, bos.toByteArray());
          addBodyPart(bp);
        }
        
      }
      catch (IOException e)
      {
        throw new MessagingException("I/O error", e);
      }
      parsed = true;
    }
  }
  
  /**
   * Create and return an InternetHeaders object that loads the headers 
   * from the given InputStream.
   * Subclasses can override this method to return a subclass
   * of InternetHeaders, if necessary.
   * This implementation simply constructs and returns an InternetHeaders 
   * object.
   * @param is the InputStream to read the headers from
   */
  protected InternetHeaders createInternetHeaders(InputStream is)
    throws MessagingException
  {
    return new InternetHeaders(is);
  }
  
  /**
   * Create and return a MimeBodyPart object to represent a body part parsed
   * from the InputStream.
   * Subclasses can override this method to return a subclass of 
   * MimeBodyPart, if necessary. This implementation simply constructs and 
   * returns a MimeBodyPart object.
   * @param headers the headers for the body part
   * @param content the content of the body part
   */
  protected MimeBodyPart createMimeBodyPart(InternetHeaders headers,
      byte[] content)
    throws MessagingException
  {
    return new MimeBodyPart(headers, content);
  }
  
  /**
   * Create and return a MimeBodyPart object to represent a body part parsed
   * from the InputStream.
   * Subclasses can override this method to return a subclass of 
   * MimeBodyPart, if necessary. This implementation simply constructs and 
   * returns a MimeBodyPart object.
   * @param is InputStream containing the body part
   */
  protected MimeBodyPart createMimeBodyPart(InputStream is)
    throws MessagingException
  {
    return new MimeBodyPart(is);
  }
  
}
