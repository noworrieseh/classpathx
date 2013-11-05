/*
 * MessagingException.java
 * Copyright (C) 2002 The Free Software Foundation
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package javax.mail;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A general messaging exception.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 * @version 1.4
 */
public class MessagingException
  extends Exception
{

  /*
   * The next exception in a chain of exceptions.
   */
  private Exception nextException;

  /**
   * Constructs a messaging exception with no detail message.
   */
  public MessagingException()
  {
    this(null, null);
  }

  /**
   * Constructs a messaging exception with the specified detail message.
   * @param message the detail message
   */
  public MessagingException(String message)
  {
    this(message, null);
  }

  /**
   * Constructs a messaging exception with the specified exception and detail
   * message.
   * @param message the detail message
   * @param exception the embedded exception
   */
  public MessagingException(String message, Exception exception)
  {
    super(message);
    nextException = exception;
  }

  /**
   * Returns the next exception chained to this one.
   * If the next exception is a messaging exception, the chain may extend
   * further.
   */
  public Exception getNextException()
  {
    return nextException;
  }

  /**
   * Adds an exception to the end of the chain.
   * If the end is not a messaging exception, this exception cannot be added
   * to the end.
   * @param exception the new end of the exception chain
   * @return true if this exception was added, false otherwise.
   */
  public synchronized boolean setNextException(Exception exception)
  {
    Object o;
    for (o = this;
         (o instanceof MessagingException) &&
         ((MessagingException) o).nextException != null;
         o = ((MessagingException) o).nextException);
    if (o instanceof MessagingException)
      {
       ((MessagingException) o).nextException = exception;
        return true;
      }
    return false;
  }

  /**
   * Returns the message, including the message from any nested exception.
   */
  public String getMessage()
  {
    String message = super.getMessage();
    if (nextException != null)
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append(message);
        buffer.append(";\n  nested exception is: \n\t");
        buffer.append(nextException.toString());
        message = buffer.toString();
      }
    return message;
  }

  public void printStackTrace(PrintStream out)
  {
    super.printStackTrace(out);
    if (nextException != null)
      {
        out.println("nested exception is:");
        nextException.printStackTrace(out);
      }
  }

  public void printStackTrace(PrintWriter out)
  {
    super.printStackTrace(out);
    if (nextException != null)
      {
        out.println("nested exception is:");
        nextException.printStackTrace(out);
      }
  }

}
