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

public class SynthesizerModeDesc extends javax.speech.EngineModeDesc 
{
  Voice[] voices;
  public SynthesizerModeDesc() 
  {
    super();
    voices=null;
  }

  public SynthesizerModeDesc(java.util.Locale l) 
  {
    super(l);
    voices=null;
  }

  public SynthesizerModeDesc(String engineName, String modeName, java.util.Locale l, Boolean running, Voice[] voices) 
  {
    super(engineName, modeName, l, running);
    this.voices=voices;
  }

  public void addVoice(Voice v) 
  {
    if (voices==null)
    voices = new Voice[] {v};
    else 
    {
      Voice[] newVoices = new Voice[voices.length+1];
      for(int i=0;i<voices.length;i++)
      newVoices[i] = voices[i];
      newVoices[voices.length]=v;
      voices=newVoices;
    }
  }

  public boolean equals(Object anObject) 
  {
    if (super.equals(anObject)) 
    {
      Voice[] temp = ((SynthesizerModeDesc)anObject).voices;
      if (temp==null || voices==null)
      return false;
      if (temp.length!=voices.length)
      return false;
      for(int i=0;i<voices.length;i++)
      if (!voices[i].equals(temp[i]))
      return false;
      return true;
    }
    return false;
  }

  public Voice[] getVoices() 
  {
    return voices;
  }

  public boolean match(javax.speech.EngineModeDesc require) 
  {
    if (super.match(require)) 
    {
      if (voices==null && (!(require instanceof SynthesizerModeDesc) || ((SynthesizerModeDesc)require).voices==null))
      return true;
      if (!(require instanceof SynthesizerModeDesc))
      return false;
      Voice[] temp = ((SynthesizerModeDesc)require).voices;
      if (temp==null)
      return false;
      for(int i=0;i<temp.length;i++) 
      {
	boolean match = false;
	for(int j=0;j<voices.length;j++)
	if (voices[j].match(temp[i])) 
	{
	  match=true;
	  break;
	}
	if (!match)
	return false;
      }
      return true;
    }
    return false;
  }

  public void setVoices(Voice[] voices) 
  {
    this.voices=voices;
  }
}



