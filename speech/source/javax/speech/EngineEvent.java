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
package javax.speech;

public class EngineEvent extends SpeechEvent 
{
  public static final int  ENGINE_ALLOCATED=1;
  public static final int  ENGINE_ALLOCATING_RESOURCES=2;
  public static final int  ENGINE_DEALLOCATED=3;
  public static final int  ENGINE_DEALLOCATING_RESOURCES=4;
  public static final int  ENGINE_PAUSED=5;
  public static final int  ENGINE_RESUMED=6;
  protected long  newEngineState;
  protected long  oldEngineState;

  public EngineEvent(Engine source, int id, long oldState, long newState) 
  {
    super(source, id);
    this.newEngineState = newState;
    this.oldEngineState = oldState;
  }

  public long getOldEngineState() 
  {
    return oldEngineState;
  }

  public long getNewEngineState() 
  {
    return newEngineState;
  }

  public String paramString() 
  {
    return ("Engine: "+source);
  }
}
