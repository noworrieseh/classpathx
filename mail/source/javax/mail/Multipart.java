/*
 * Mulipart.java
 * Copyright (C) 2002 The Free Software Foundation
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

package javax.mail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataSource;

/**
 * Multipart is a container that holds multiple body parts. 
 * Multipart provides methods to retrieve and set its subparts.
 * <p>
 * Multipart also acts as the base class for the content object returned by 
 * most Multipart DataContentHandlers. For example, invoking 
 * <code>getContent()</code> on a DataHandler whose source is a 
 * "multipart/signed" data source may return an appropriate subclass 
 * of Multipart.
 * <p>
 * Some messaging systems provide different subtypes of Multiparts.
 * For example, MIME specifies a set of subtypes that include 
 * "alternative", "mixed", "related", "parallel", "signed", etc.
 * <p>
 * Multipart is an abstract class. Subclasses provide actual implementations.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public abstract class Multipart
{

  /**
   * List of BodyPart objects.
   */
  protected List parts;

  /**
   * This field specifies the content-type of this multipart object.
   * It defaults to "multipart/mixed".
   */
  protected String contentType;

  /**
   * The Part containing this Multipart, if known.
   */
  protected Part parent;

  /**
   * Default constructor. An empty Multipart object is created.
   */
  protected Multipart()
  {
    parts = new ArrayList();
    contentType = "multipart/mixed";
  }

  /**
   * Setup this Multipart object from the given MultipartDataSource.
   * <p>
   * The method adds the MultipartDataSource's BodyPart objects into this
   * Multipart. This Multipart's <code>contentType</code> is set to that of 
   * the MultipartDataSource.
   * <p>
   * This method is typically used in those cases where one has a multipart 
   * data source that has already been pre-parsed into the individual body 
   * parts (for example, an IMAP datasource), but needs to create an 
   * appropriate Multipart subclass that represents a specific multipart 
   * subtype.
   * @param mp Multipart datasource
   */
  protected void setMultipartDataSource(MultipartDataSource mp)
    throws MessagingException
  {
    contentType = mp.getContentType();
    int count = mp.getCount();
    for (int i = 0; i<count; i++)
      addBodyPart(mp.getBodyPart(i));
  }

  /**
   * Return the content-type of this Multipart.
   * <p>
   * This implementation just returns the value of the 
   * <code>contentType</code> field.
   */
  public String getContentType()
  {
    return contentType;
  }

  /**
   * Return the number of enclosed BodyPart objects.
   */
  public int getCount()
    throws MessagingException
  {
    return (parts==null) ? 0 : parts.size();
  }

  /**
   * Get the specified Part.
   * Parts are numbered starting at 0.
   * @param index the index of the desired Part
   * @exception IndexOutOfBoundsException if the given index is out of range.
   */
  public BodyPart getBodyPart(int index)
    throws MessagingException
  {
    if (parts==null)
      throw new IndexOutOfBoundsException();
    return (BodyPart)parts.get(index);
  }

  /**
   * Remove the specified part from the multipart message.
   * Shifts all the parts after the removed part down one.
   * @param part The part to remove
   * @return true if part removed, false otherwise
   * @exception MessagingException if no such Part exists
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   */
  public boolean removeBodyPart(BodyPart part)
    throws MessagingException
  {
    if (parts==null)
      throw new MessagingException("No such BodyPart");
    synchronized (parts)
    {
      boolean success = parts.remove(part);
      if (success)
        part.setParent(null);
      return success;
    }
  }

  /**
   * Remove the part at specified location (starting from 0).
   * Shifts all the parts after the removed part down one.
   * @param index Index of the part to remove
   * @exception IndexOutOfBoundsException if the given index is out of range.
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   */
  public void removeBodyPart(int index)
    throws MessagingException
  {
    if (parts==null)
      throw new IndexOutOfBoundsException("No such BodyPart");
    synchronized (parts)
    {
      BodyPart part = (BodyPart)parts.get(index);
      parts.remove(index);
      part.setParent(null);
    }
  }

  /**
   * Adds a Part to the multipart. 
   * The BodyPart is appended to the list of existing Parts.
   * @param part The Part to be appended
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   */
  public synchronized void addBodyPart(BodyPart part)
    throws MessagingException
  {
    if (parts==null)
      parts = new ArrayList();
    synchronized (parts)
    {
      parts.add(part);
      part.setParent(this);
    }
  }

  /**
   * Adds a BodyPart at position index.
   * If index is not the last one in the list, the subsequent parts 
   * are shifted up. If index is larger than the number of parts present,
   * the BodyPart is appended to the end.
   * @param part The BodyPart to be inserted
   * @param index Location where to insert the part
   * @exception IllegalWriteException if the underlying implementation 
   * does not support modification of existing values
   */
  public synchronized void addBodyPart(BodyPart part, int index)
    throws MessagingException
  {
    if (parts==null)
      parts = new ArrayList();
    synchronized (parts)
    {
      parts.add(index, part);
      part.setParent(this);
    }
  }

  /**
   * Output an appropriately encoded bytestream to the given OutputStream.
   * The implementation subclass decides the appropriate encoding algorithm 
   * to be used. The bytestream is typically used for sending.
   */
  public abstract void writeTo(OutputStream os)
    throws IOException, MessagingException;

  /**
   * Return the Part that contains this Multipart object, or null if not known.
   */
  public Part getParent()
  {
    return parent;
  }

  /**
   * Set the parent of this Multipart to be the specified Part.
   * Normally called by the Message or BodyPart 
   * <code>setContent(Multipart)</code> method. parent may be null if 
   * the Multipart is being removed from its containing Part.
   */
  public void setParent(Part part)
  {
    parent = part;
  }
  
}
