/*
 * JspWriter.java -- XXX
 *
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 * Written by Mark Wielaard (mark@klomp.org)
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

package javax.servlet.jsp;

import java.io.Writer;
import java.io.IOException;

/**
 * The output stream used by JSP engines.
 *
 * @author Nic Ferrier <nferrier@tapsellferrier.co.uk>
 * @author Mark Wielaard <mark@klomp.org>
 */
public abstract class JspWriter extends Writer
{

  protected boolean autoFlush;

  protected int bufferSize;

  public static final int DEFAULT_BUFFER = -1;

  public static final int NO_BUFFER = -2;

  public static final int UNBOUNDED_BUFFER = -3;


  /** Make a JSP io stream.
   */
  protected JspWriter (int bufferSize, boolean autoFlush)
  {
    super();
    this.autoFlush = autoFlush;
    this.bufferSize = bufferSize;
  }

  /**
   * XXX
   *
   * @exception IOException if an error occurs
   * @exception IllegalStateException XXX
   */
  public abstract  void clear() throws IOException, IllegalStateException;

  /**
   * Clears the output buffer.
   *
   * @exception IOException if an error occurs
   * @exception IllegalStateException XXX
   */
  public abstract  void clearBuffer ()
    throws IOException, IllegalStateException;

  /**
   * XXX
   *
   * @exception IOException if an error occurs
   */
  public abstract  void close() throws IOException;

  /**
   * XXX
   *
   * @exception IOException if an error occurs
   */
  public abstract  void flush() throws IOException;

  /**
   * XXX
   *
   * @return XXX
   */
  public int getBufferSize()
  {
    return bufferSize;
  }

  /**
   * XXX
   *
   * @return XXX
   */
  public boolean isAutoFlush()
  {
    return autoFlush;
  }

  /**
   * XXX
   *
   * @exception IOException if an error occurs
   */
  public abstract  void newLine() throws IOException;

  /**
   * XXX
   *
   * @param b XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(boolean b) throws IOException;

  /**
   * XXX
   *
   * @param c XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(char c) throws IOException;

  /**
   * XXX
   *
   * @param cb XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(char[] cb) throws IOException;

  /**
   * XXX
   *
   * @param d XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(double d) throws IOException;

  /**
   * XXX
   *
   * @param f XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(float f) throws IOException;

  /**
   * XXX
   *
   * @param i XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(int i) throws IOException;

  /**
   * XXX
   *
   * @param l XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(long l) throws IOException;

  /**
   * XXX
   *
   * @param o XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(Object o) throws IOException;

  /**
   * XXX
   *
   * @param s XXX
   * @throws IOException if an error occurs
   */
  public abstract  void print(String s) throws IOException;

  /**
   * XXX
   *
   * @exception IOException if an error occurs
   */
  public abstract  void println() throws IOException;

  /**
   * XXX
   *
   * @param b XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(boolean b) throws IOException;

  /**
   * XXX
   *
   * @param c XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(char c) throws IOException;

  /**
   * XXX
   *
   * @param cb XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(char[] cb) throws IOException;

  /**
   * XXX
   *
   * @param d XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(double d) throws IOException;

  /**
   * XXX
   *
   * @param f XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(float f) throws IOException;

  /**
   * XXX
   *
   * @param i XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(int i) throws IOException;

  /**
   * XXX
   *
   * @param l XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(long l) throws IOException;

  /**
   * XXX
   *
   * @param o XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(Object o) throws IOException;

  /**
   * XXX
   *
   * @param s XXX
   * @throws IOException if an error occurs
   */
  public abstract  void println(String s) throws IOException;

  /**
   * XXX
   * 
   * @return XXX
   */
  public abstract int getRemaining();

}

