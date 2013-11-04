/*
  GNU Javamail IMAP provider
  Copyright (C) N.J.Ferrier, Tapsell-Ferrier Limited 2000,2001 for the OJE project

  For more information on this please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.mail.providers.imap;


import java.io.InputStream;
import java.io.FilterReader;
import java.io.FilterInputStream;
import java.io.Reader;
import java.io.IOException;


/** a stream which returns EOF after reading a count of bytes.
 * After count bytes are read the stream doesn't perform anymore
 * reads on the underlying stream.
 *
 * <p>The class also allows a limited amount of post processing
 * when the bytes have been read by use of the <code>eventEOF</code>
 * method.
 *.</p>
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class ByteCountInputStream
extends FilterInputStream
{

  /** a temporary debug switch.
   * It turns on some debugging statements in the reads.
   */
  boolean __DEBUG=false;

  /** the connected stream.
   */
  protected InputStream connected;

  /** the max count.
   */
  protected int maxCount;

  /** the count of bytes read from the connected stream.
   */
  protected int count=0;

  /** create the stream on top of the specified stream.
   *
   * @param instream the stream to read until max bytes have been read
   * @param max the number of bytes to read before returning EOF
   */
  public ByteCountInputStream(InputStream instream,int max)
  {
    super(instream);
    connected=instream;
    maxCount=max;
  }

  /** called when the read methods decide the count has been read.
   * This method is designed to let extenders peform post processing
   * after the end of stream has been reached in the main class.
   *
   * <p>This method is called in 2 situations:
   * <ol>
   * <li>a read method has been called and the count has already
   * reached the max. In this case the value returned by this method
   * is returned through the read method.
   * <li>a read method has found the end of stream whilst processing
   * a read call. In this case this method is called but the read
   * method doesn't return the value, it just gets discarded.
   * </ul>
   * </p>
   */
  protected int eventEOF()
  throws IOException
  {
    return -1;
  }


  //stream implementation

  /** get the number of bytes still waiting to be read.
   * This is worked out so:
   * <pre>
   *    maxCount-count
   * </pre>
   * In other words it uses the information that this stream
   * was created with rather than using an analysis of what
   * is actually on the stream.
   */
  public int available()
  throws IOException
  {
    return maxCount-count;
  }

  public int read()
  throws IOException
  {
    if(count>=maxCount)
    return eventEOF();
    int i=connected.read();
    if(i>-1)
    ++count;
    if(count>=maxCount)
    eventEOF();
    return i;
  }

  public int read(byte[] b)
  throws IOException
  {
    return read(b,0,b.length);
  }

  public int read(byte[] b,int off,int len)
  throws IOException
  {
    if(__DEBUG) System.err.println("    BCIStr("+off+","+len+")  count="+count+" maxCount="+maxCount);
    if(count>=maxCount)
    return eventEOF();
    //the case where we don't have enough remaining
    //bytes for the requested read len
    if(count+len>=maxCount)
    {
      int spare=maxCount-count;
      int red=connected.read(b,off,spare);
      if(__DEBUG) System.err.println("    BCIStr count="+count+" red="+red);
      count+=red;
      if(count>=maxCount)
      eventEOF();
      return red;
    }
    //the case where we have enough remaining bytes to fill the buffer
    int red=connected.read(b,off,len);
    if(__DEBUG) System.err.println("    BCIStr red="+red);
    count+=red;
    if(count>=maxCount)
    eventEOF();
    return red;
  }

}
