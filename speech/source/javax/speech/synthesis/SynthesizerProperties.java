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

import java.beans.PropertyVetoException;

public interface SynthesizerProperties extends javax.speech.EngineProperties 
{
  public float getPitchRange();
  public float getPitch();
  public float getSpeakingRate();
  public Voice getVoice();
  public float getVolume();
  public void setPitchRange(float hertz) throws PropertyVetoException;
  public void setPitch(float hertz) throws PropertyVetoException;
  public void setSpeakingRate(float wpm) throws PropertyVetoException;
  public void setVoice(Voice voice) throws PropertyVetoException;
  public void setVolume(float volume) throws PropertyVetoException;
}
