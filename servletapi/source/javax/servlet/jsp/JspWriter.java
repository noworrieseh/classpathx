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

import java.io.IOException;

/**
 * XXX
 */
public interface JspWriter {

	/**
	 * XXX
	 *
	 * @exception IOException if an error occurs
	 * @exception IllegalStateException XXX
	 */
	void clear() throws IOException, IllegalStateException;

	/**
	 * XXX
	 *
	 * @exception IOException if an error occurs
	 */
	void close() throws IOException;

	/**
	 * XXX
	 *
	 * @exception IOException if an error occurs
	 */
	void flush() throws IOException;

	/**
	 * XXX
	 *
	 * @return XXX
	 */
	int getBufferSize();

	/**
	 * XXX
	 *
	 * @return XXX
	 */
	int getRemainingSize();

	/**
	 * XXX
	 *
	 * @return XXX
	 */
	boolean isAutoFlush();

	/**
	 * XXX
	 *
	 * @exception IOException if an error occurs
	 */
	void newLine() throws IOException;

	/**
	 * XXX
	 *
	 * @param b XXX
	 * @throws IOException if an error occurs
	 */
	void print(boolean b) throws IOException;

	/**
	 * XXX
	 *
	 * @param c XXX
	 * @throws IOException if an error occurs
	 */
	void print(char c) throws IOException;

	/**
	 * XXX
	 *
	 * @param cb XXX
	 * @throws IOException if an error occurs
	 */
	void print(char[] cb) throws IOException;

	/**
	 * XXX
	 *
	 * @param d XXX
	 * @throws IOException if an error occurs
	 */
	void print(double d) throws IOException;

	/**
	 * XXX
	 *
	 * @param f XXX
	 * @throws IOException if an error occurs
	 */
	void print(float f) throws IOException;

	/**
	 * XXX
	 *
	 * @param i XXX
	 * @throws IOException if an error occurs
	 */
	void print(int i) throws IOException;

	/**
	 * XXX
	 *
	 * @param l XXX
	 * @throws IOException if an error occurs
	 */
	void print(long l) throws IOException;

	/**
	 * XXX
	 *
	 * @param o XXX
	 * @throws IOException if an error occurs
	 */
	void print(Object o) throws IOException;

	/**
	 * XXX
	 *
	 * @param s XXX
	 * @throws IOException if an error occurs
	 */
	void print(String s) throws IOException;

	/**
	 * XXX
	 *
	 * @exception IOException if an error occurs
	 */
	void println() throws IOException;

	/**
	 * XXX
	 *
	 * @param b XXX
	 * @throws IOException if an error occurs
	 */
	void println(boolean b) throws IOException;

	/**
	 * XXX
	 *
	 * @param c XXX
	 * @throws IOException if an error occurs
	 */
	void println(char c) throws IOException;

	/**
	 * XXX
	 *
	 * @param cb XXX
	 * @throws IOException if an error occurs
	 */
	void println(char[] cb) throws IOException;

	/**
	 * XXX
	 *
	 * @param d XXX
	 * @throws IOException if an error occurs
	 */
	void println(double d) throws IOException;

	/**
	 * XXX
	 *
	 * @param f XXX
	 * @throws IOException if an error occurs
	 */
	void println(float f) throws IOException;

	/**
	 * XXX
	 *
	 * @param i XXX
	 * @throws IOException if an error occurs
	 */
	void println(int i) throws IOException;

	/**
	 * XXX
	 *
	 * @param l XXX
	 * @throws IOException if an error occurs
	 */
	void println(long l) throws IOException;

	/**
	 * XXX
	 *
	 * @param o XXX
	 * @throws IOException if an error occurs
	 */
	void println(Object o) throws IOException;

	/**
	 * XXX
	 *
	 * @param s XXX
	 * @throws IOException if an error occurs
	 */
	void println(String s) throws IOException;

	/**
	 * XXX
	 *
	 * @param cb XXX
	 * @exception IOException if an error occurs
	 */
	void write(char[] cb) throws IOException;

	/**
	 * XXX
	 *
	 * @param cb XXX
	 * @param off XXX
	 * @param len XXX
	 * @exception IOException if an error occurs
	 */
	void write(char[] cb, int off, int len) throws IOException;

	/**
	 * XXX
	 *
	 * @param c XXX
	 * @exception IOException if an error occurs
	 */
	void write(int c) throws IOException;

	/**
	 * XXX
	 *
	 * @param s XXX
	 * @exception IOException if an error occurs
	 */
	void write(String s) throws IOException;

	/**
	 * XXX
	 *
	 * @param s XXX
	 * @param off XXX
	 * @param len XXX
	 * @exception IOException if an error occurs
	 */
	void write(String s, int off, int len) throws IOException;

}


