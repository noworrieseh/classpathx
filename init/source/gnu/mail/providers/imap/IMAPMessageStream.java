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


/** read a message of count bytes and then continue to process IMAP responses.
 * Things could go horribly wrong with this but it's worth it to provide speed
 * for webmail. Access to this class is hidden inside the IMAPMessage class
 * so it's not easy to get your hands on this.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class IMAPMessageStream
extends ByteCountInputStream
{

  IMAPConnection con;

  boolean responseDone=false;

  /** create the stream.
   */
  public IMAPMessageStream(InputStream in,int max,IMAPConnection connection)
  {
    super(in,max);
    con=connection;
    if(con.__DEBUG) System.err.println("   IMAPMsgStr READING "+max+" bytes");
  }

  protected int eventEOF()
  throws IOException
  {
    if(!responseDone)
    {
      try
      {
	//this is usefull debug
	if(con.__DEBUG) System.err.println(">>IMAPMessageStream found the end!");
	//read an empty line which ends the content
	//it actually has the close ')' on it.
	con.readLine();
	while(!con.processResponse());
	responseDone=true;
      }
      catch(Exception e)
      {
	e.printStackTrace(System.err);
      }
    }
    return -1;
  }
}
