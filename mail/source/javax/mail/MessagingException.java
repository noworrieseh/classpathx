/*
 * MessagingException.java
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

/**
 * The base class for all exceptions thrown by the Messaging classes.
 */
public class MessagingException 
  extends Exception
{

  /*
   * The next exception in the chain.
   */
  private Exception nextException;

  /**
   * Constructs a MessagingException with no detail message.
   */
  public MessagingException()
  {
  }

  /**
   * Constructs a MessagingException with the specified detail message.
   * @param message the detail message
   */
  public MessagingException(String message)
  {
    super(message);
  }

  /**
   * Constructs a MessagingException with the specified Exception and detail
   * message. The specified exception is chained to this exception.
   * @param message the detail message
   * @param exception the embedded exception
   */
  public MessagingException(String message, Exception exception)
  {
    super(message);
    nextException = exception;
  }

  /**
   * Get the next exception chained to this one.
   * If the next exception is a MessagingException, the chain may extend 
   * further.
   */
  public Exception getNextException()
  {
    return nextException;
  }

  /**
   * Add an exception to the end of the chain.
   * If the end is not a MessagingException, this exception cannot be added 
   * to the end.
   * @param exception the new end of the Exception chain
   * @return true if the this Exception was added, false otherwise.
   */
  public synchronized boolean setNextException(Exception exception)
  {
    Object o;
    for (o = this;
        (o instanceof MessagingException) && 
        ((MessagingException)o).nextException!=null;
        o = ((MessagingException)o).nextException);
    if (o instanceof MessagingException)
    {
      ((MessagingException)o).nextException = exception;
      return true;
    }
    return false;
  }

  /**
   * Produce the message, include the message from the nested exception 
   * if there is one.
   */
  public String getMessage()
  {
    String message = super.getMessage();
    if (nextException!=null)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(message);
      buffer.append(";\n  nested exception is: \n\t");
      buffer.append(nextException.toString());
      message = buffer.toString();
    }
    return message;
  }
  
}
