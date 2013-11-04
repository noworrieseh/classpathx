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
import java.io.Reader;
import java.io.IOException;

/** a reader that does NO conversion of bytes to chars.
 * I imagine that this is what you get if you do:
 * <pre>
 *   InputStreamReader ir=new InputStreamReader(is,"ASCII");
 * </pre>
 * But the problem with using <code>InputStreamReader</code>
 * is that it seems to buffer at 8192 bytes. If you don't want
 * any buffering to occur that can be a bit of a pain.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class ASCII7BitReader
extends Reader
{

  InputStream in;

  public ASCII7BitReader(InputStream instream)
  {
    in=instream;
  }

  public int read()
  throws IOException
  {
    return in.read();
  }

  public int read(char[] buf,int off,int len)
  throws IOException
  {
    byte[] b=new byte[len];
    int red=in.read(b,0,len);
    int count=off+red;
    for(int i=off,j=0; i<count; i++,j++)
    buf[i]=(char)b[j];
    return red;
  }

  public int read(char[] buf)
  throws IOException
  {
    return this.read(buf,0,buf.length);
  }

  public void close()
  throws IOException
  {
    in.close();
  }
}
