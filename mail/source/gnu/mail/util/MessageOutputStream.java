/*
 * MessageOutputStream.java
 * Copyright (C) 2002 The Free Software Foundation
 * 
 * This file is part of GNU JavaMail, a library.
 * 
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package gnu.mail.util;

import java.io.*;

/**
 * An output stream that escapes any dots on a line by themself with
 * another dot, for the purposes of sending messages to SMTP and NNTP
 * servers.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class MessageOutputStream
	extends FilterOutputStream
{
	
	/**
	 * The stream termination octet.
	 */
	public static final int END = 46;
	
	/**
	 * The line termination octet.
	 */
	public static final int LF = 10;
	
	int[] last = { LF, LF }; // the last character written to the stream
	
	/**
	 * Constructs a message output stream connected to the specified output stream.
	 * @param out the target output stream
	 */
	public MessageOutputStream(OutputStream out)
	{
		super(out);
	}
	
	/**
	 * Writes a character to the underlying stream.
	 * @exception IOException if an I/O error occurred
	 */
	public void write(int ch)
		throws IOException
	{
		if (last[0]==LF && last[1]==END && ch==LF)
			out.write(END);
		out.write(ch);
		last[0] = last[1];
		last[1] = ch;
	}
	
	/**
	 * Writes a portion of a byte array to the underlying stream.
	 * @exception IOException if an I/O error occurred
	 */
	public void write(byte b[], int off, int len)
		throws IOException
	{
		for (int i = 0; i < len; i++)
		{
			int ch = (int)b[off+i];
			if (last[0]==LF && last[1]==END && ch==LF)
			{
				byte[] b2 = new byte[b.length+1];
				System.arraycopy(b, off, b2, off, i);
				b2[off+i] = END;
				System.arraycopy(b, off+i, b2, off+i+1, len-i);
				b = b2;
				i++; len++;
			}
			last[0] = last[1];
			last[1] = ch;
		}
		out.write(b, off, len);
	}
	
}

