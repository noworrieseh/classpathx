/*
 * ELException.java.
 * 
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck <arnaud.vandyck@ulg.ac.be>
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

package javax.servlet.jsp.el;


/**
 * This page must stop evaluation.
 *
 * @since JSP 2.0
 * @author {@link mailto:arnaud.vandyck@ulg.ac.be Arnaud Vandyck}
 */
public class ELException extends Exception
{

  protected Throwable rootCause;

  /** 
   */
  public ELException ()
  {
    super("jsp skip page problem");
  }

  /** 
   */
  public ELException (String message)
  {
    super(message);
  }
  
  public ELException (String message,
                      Throwable rootCause)
  {
    super(message);
    this.rootCause = rootCause;
  }

  public ELException (Throwable rootCause)
  {
    this();
    this.rootCause = rootCause;
  }

  public Throwable getRootCause()
  {
    return this.rootCause;
  }

  /**
   * We should see what the original implementation returns...
   */
  public String toString()
  {
    return super.getMessage() + " " 
      + ( (rootCause != null) ? rootCause.getMessage() : "" );
  }

}
