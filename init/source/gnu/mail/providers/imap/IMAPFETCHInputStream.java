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
import javax.mail.MessagingException;


/** read a message performing one FETCH for each read.
 * This is an efficient way to ensure that messages can remain
 * light weight objects. A call to:
 * <pre>
 *   read(byte[],int,int)
 * </pre>
 * will cause a partial FETCH to the IMAP server of the size
 * requested, eg:
 * <pre>
 *   A007  FETCH 1 (body[]<0.5000>)
 * </pre>
 *
 * <p><h4>FETCH size limits</h4>
 * The variable <code>fetchLimit</code> is a lower limit on the size
 * of the partial FETCH. The limit is defined in this class for now
 * but will become a <code>Store</code> property.</p>
 *
 * <p>When a small number of bytes than <code>fetchLimit</code> is
 * requested then the stream actually FETCHes <code>fetchLimit</code>
 * and causes the bytes to be buffered.</p>
 *
 * @see #cache for why this class might not work as you expect
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPFETCHInputStream
extends InputStream
{

  /** the lower limit for partial fetches.
   * If the constructor argument <code>max</code> is less than the default
   * value for this then <code>max</code> is used.
   */
  int fetchLimit=5000;

  /** the current extent of the cache buffer (if it's in use).
   * The extent is the high water mark. The last location in the buffer
   * where there is valid data. The buffer can be larger than the extent
   * so that capacity does not have to be constantly reallocated for
   * expanding buffers.
   */
  int extent=0;

  /** the last read point in the cache buffer.
   * Everything before this point can be considered read.
   * When:
   * <pre>
   *    index==extent
   * </pre>
   * the contents of the buffer have been read.
   */
  int index=0;

  /** the cache buffer.
   * The cache is used to read in any data that IMAP has sent us
   * that the user hasn't got space for in the current call to
   * <code>read</code>.
   *
   * <p><h4>Buffer size</h4>
   * The idea of this class is that we read fixed amounts of data
   * from the IMAP server. Therefore this cache is normally a fixed
   * size (the same size as we use to read data from IMAP).
   * However, the server can send more data than it should.
   * For example, the Novell Groupwise 4.1 IMAP FEP doesn't do
   * partial fetches - it sends the entire part back in response
   * to a partial fetch. This buffer is re-created when such events.
   * The size of the buffer then will be the size of whatever data
   * is waiting to be read. That could be huge, if so change your
   * IMAP server to one that DOES support partial fetches.
   */
  byte[] cache=new byte[fetchLimit];

  /** the message we're associated with.
   */
  IMAPMessage msg;

  /** the next offset into the IMAP server's message data.
   * In a situation where a large message is being retrieved chunk
   * by chunk this indicates the start position of the next chunk.
   * When this <code>==-1</code> the entire message has been read.
   */
  int offset=0;

  /** the part specifier which we have to read.
   */
  int part;

  /** create the stream.
   *
   * @param message the IMAP message object we're associated with
   */
  public IMAPFETCHInputStream(IMAPMessage message,int msgPart)
  {
    msg=message;
    part=msgPart;
  }

  /** cause bytes to be read with a partial fetch.
   */
  public int read()
  throws IOException
  {
    byte[] buf=new byte[1];
    int red=read(buf,0,1);
    //EOF?
    if(red==-1)
    return -1;
    //sucessfull read
    return buf[0];
  }

  /** causes bytes to be read with a partial fetch.
   */
  public int read(byte[] buf,int start,int len)
  throws IOException
  {
    //is there any cache to read?
    int cacheAvailable=extent-index;
    if(cacheAvailable<1)
    return readIO(buf,start,len);
    else
    {
      //can we service the read entirely from the cache?
      if(len<cacheAvailable)
      {
	System.arraycopy(cache,index,buf,start,len);
	index+=len;
	// System.err.println("\n    IMAPFETCHstr from cache red="+len+" index="+index+" extent="+extent);
	return len;
      }
      else
      {
	//the cache is smaller than len so put it all in the user's buffer
	System.arraycopy(cache,index,buf,start,cacheAvailable);
	//reset pointers coz the cache is now empty
	index=0;
	extent=0;
	//now read the rest
	int red=readIO(buf,start+cacheAvailable,len-cacheAvailable);
	return cacheAvailable+red;
      }
    }
  }


  /** perform a read on the underlying stream.
   * Caching of the extant data is peformed. IMAP server failings are
   * also taken account of - IMAP servers that don't support partial
   * fetches are coped with.
   */
  int readIO(byte[] buf,int start,int len)
  throws IOException
  {
    //the offset is the flag to indicate we've read the entire message
    if(offset==-1)
    return -1;
    //try and get at least 'fetchLimit' bytes
    InputStream partialFetch=null;
    try
    {
      if(len<fetchLimit)
      partialFetch=msg.readRawStream(part,offset,fetchLimit);
      else
      partialFetch=msg.readRawStream(part,offset,len);
    }
    catch(MessagingException e)
    {
      throw new IOException("couldn't read IMAP");
    }
    //the server might not support partial fetch so check how many bytes were returned
    int bytesToRead=partialFetch.available();
    if(bytesToRead>fetchLimit)
    cache=new byte[bytesToRead];
    //read the number of bytes that the user requested direct into his buffer
    int red=partialFetch.read(buf,start,len);
    //read any remaining bytes into the cache
    if(red>-1 && red<bytesToRead)
    {
      //we can update the offset into the message data with what we've read
      offset+=red;
      int l=bytesToRead-red;
      int i=index;
      while(l>0)
      {
	int r=partialFetch.read(cache,i,l);
	i+=r;
	extent+=r;
	l-=r;
	offset+=r;
      }
    }
    //the message has ended if we get more, or less bytes than we asked for
    if(bytesToRead!=fetchLimit)
    offset=-1;
    return red;
  }
}
