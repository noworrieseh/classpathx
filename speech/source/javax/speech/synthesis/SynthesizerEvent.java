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

public class SynthesizerEvent extends javax.speech.EngineEvent 
{
  public static final int QUEUE_EMPTIED=7;
  public static final int QUEUE_UPDATED=8;
  protected boolean topOfQueueChanged;

  public SynthesizerEvent(Synthesizer synth, int id, boolean topOfQueueChanged, long oldEngineState, long newEngineState) 
  {
    super(synth, id, oldEngineState, newEngineState);
    this.topOfQueueChanged = topOfQueueChanged;
  }
}
