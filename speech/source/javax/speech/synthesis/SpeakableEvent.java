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

public class SpeakableEvent extends javax.speech.SpeechEvent 
{
  public static final  int  ELEMENT_CLOSE=1;
  public static final  int  ELEMENT_EMPTY=2;
  public static final  int  ELEMENT_OPEN=3;
  public static final  int  MARKER_REACHED=4;
  public static final  int   SPEAKABLE_CANCELLED=5; 
  public static final  int  SPEAKABLE_ENDED=6;
  public static final  int  SPEAKABLE_PAUSED=7;
  public static final  int  SPEAKABLE_RESUMED=8;
  public static final  int  SPEAKABLE_STARTED=9;
  public static final  int  TOP_OF_QUEUE=10;
  public static final  int  WORD_STARTED=11;

  protected int  wordEnd;
  protected int  wordStart;
  protected int  markerType;
  protected String  text;

  public SpeakableEvent(Object source, int id) 
  {
    super(source, id);
    text=null;
    wordEnd=wordStart=-1;
  }

  public SpeakableEvent(Object source, int id, String text, int markerType) 
  {
    super(source, id);
    this.text=text;
    this.markerType=markerType;
  }

  public SpeakableEvent(Object source, int id, String text, int wordStart, int wordEnd) 
  {
    super(source, id);
    this.text=text;
    this.wordStart=wordStart;
    this.wordEnd=wordEnd;
  }

  public int getMarkerType() 
  {
    return markerType;
  }

  public String getText() 
  {
    return text;
  }

  public int getWordStart() 
  {
    return wordStart;
  }

  public String paramString() 
  {
    return "SpeakableEvent "+id;
  }

  public int getWordEnd() 
  {
    return wordEnd;
  }
}
