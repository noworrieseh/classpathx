/*
 * ValidationMessage.java -- XXX
 * 
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck (arnaud.vandyck@ulg.ac.be)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation  
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp.tagext;

/**
 * A validation message.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class ValidationMessage
{
  
  private String id;
  private String message;

  /**
   * Construct a Validation Message.
   * 
   * @param id
   * @param message
   */
  public ValidationMessage(String id,
                           String message)
  {
    this.id = id;
    this.message = message;
  }

  /**
   * Get the Id value.
   * @return the Id value.
   */
  public String getId() {
    return id;
  }

  /**
   * Get the Message value.
   * @return the Message value.
   */
  public String getMessage() {
    return message;
  }

}
