/*
 * MultipartDataSource.java
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

package javax.mail;

import javax.activation.DataSource;

/**
 * MultipartDataSource is a DataSource that contains body parts.
 * This allows "mail aware" DataContentHandlers to be implemented 
 * more efficiently by being aware of such DataSources and using the 
 * appropriate methods to access BodyParts.
 * <p>
 * Note that the data of a MultipartDataSource is also available as an input
 * stream.
 * <p>
 * This interface will typically be implemented by providers that preparse
 * multipart bodies, for example an IMAP provider.
 * @see DataSource
 */
public interface MultipartDataSource
  extends DataSource
{

  /**
   * Return the number of enclosed BodyPart objects.
   */
  public int getCount();

  /**
   * Get the specified Part.
   * Parts are numbered starting at 0.
   * @param index the index of the desired Part
   * @exception IndexOutOfBoundsException if the given index is out of range.
   */
  public BodyPart getBodyPart(int index)
    throws MessagingException;
  
}
