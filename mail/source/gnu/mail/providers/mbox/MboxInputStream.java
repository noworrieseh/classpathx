/*
 * MboxInputStream.java
 * Copyright (C) 2002 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * You also have permission to link it with the Sun Microsystems, Inc. 
 * JavaMail(tm) extension and run that combination.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Contributor(s): nil
 */

package gnu.mail.providers.mbox;

import java.io.IOException;
import java.io.InputStream;
import gnu.mail.util.LineInputStream;

/**
 * A line input stream that unescapes escaped From lines.
 *
 * @author dog@dog.net.uk
 * @version 2.0
 */
public class MboxInputStream
  extends LineInputStream
{

  public MboxInputStream(InputStream in)
  {
    super(in);
  }

  public String readLine()
    throws IOException
  {
    String line = super.readLine();
    if (line!=null)
    {
      int len = line.length();
      if (len>6)
      {
        for (int i=0; i<(len-5); i++)
        {
          char c = line.charAt(i);
          if (i>0 &&
              (c=='F' &&
               line.charAt(i+1)=='r' &&
               line.charAt(i+2)=='o' &&
               line.charAt(i+3)=='m' &&
               line.charAt(i+4)==' '))
            return line.substring(1);
          if (c!='>')
            break;
        }
      }
    }
    return line;
  }
  
}
