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

public interface Synthesizer extends javax.speech.Engine 
{
  public static final long QUEUE_EMPTY=128;
  public static final long QUEUE_NOT_EMPTY=64;

  public void addSpeakableListener(SpeakableListener listener);
  public void cancelAll();
  public void cancel();
  public void cancel(Object source);
  public java.util.Enumeration enumerateQueue();
  public SynthesizerProperties getSynthesizerProperties();
  public String phoneme(String text);
  public void removeSpeakableListener(SpeakableListener listener);
  public void speakPlainText(String text, SpeakableListener listener);
  public void speak(Speakable JSMLText, SpeakableListener listener) throws JSMLException;
  public void speak(java.net.URL JSMLurl, SpeakableListener listener) throws JSMLException, java.io.IOException;
  public void speak(String JSMLText, SpeakableListener listener) throws JSMLException;
}
