/*
  GNU-Classpath Extensions: Speech API
  Copyright (C) 2001 Brendan Burns

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

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
package javax.speech.synthesis;

public class SynthesizerQueueItem 
{
  protected SpeakableListener listener;
  protected boolean plainText;
  protected Object source;
  protected String text;

  public SynthesizerQueueItem(Object source, String text, boolean plainText, SpeakableListener listener) 
  {
    this.source=source;
    this.text=text;
    this.plainText=plainText;
    this.listener=listener;
  }

  public Object getSource() 
  {
    return source;
  }

  public SpeakableListener getSpeakableListener() 
  {
    return listener;
  }

  public String getText() 
  {
    return text;
  }

  public boolean isPlainText() 
  {
    return plainText;
  }
}
