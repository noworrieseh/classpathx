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

import java.util.*;

public class EngineModeDesc 
{
  Locale locale;
  String engineName;
  String modeName;
  Boolean running;

  public EngineModeDesc() 
  {
    this(null);
  }

  public EngineModeDesc(Locale locale) 
  {
    this(null, null, locale, null);
  }

  public EngineModeDesc(String engineName, String modeName, Locale locale, Boolean running) 
  {
    this.engineName=engineName;
    this.modeName=modeName;
    this.locale=locale;
    this.running=running;
  }

  public boolean equals(Object o) 
  {
    if (o==null)
    return false;
    try 
    {
      EngineModeDesc temp = (EngineModeDesc)o;
      return (((engineName==null && temp.engineName==null) ||
	       (engineName!=null && engineName.equals(temp.engineName))) &&
	      ((modeName==null && temp.modeName==null) ||
	       (modeName!=null && modeName.equals(temp.modeName))) &&
	      ((locale==null && temp.locale==null) ||
	       (locale!=null && locale.equals(temp.locale))));
    } catch (ClassCastException e) 
    {
      return false;
    }
  }

  public String getEngineName() 
  {
    return engineName;
  }

  public Locale getLocale() 
  {
    return locale;
  }

  public String getModeName() 
  {
    return modeName;
  }

  public Boolean getRunning() 
  {
    return running;
  }

  public boolean match(EngineModeDesc require) 
  {
    if (require.getEngineName()!=null && require.getEngineName().length()>0)
    if (!require.getEngineName().equals(engineName))
    return false;
    if (require.getModeName()!=null && require.getModeName().length()>0)
    if (!require.getModeName().equals(modeName))
    return false;
    if (require.getLocale()!=null)
    if (!require.getLocale().equals(locale))
    return false;
    return true;
  }

  public void setEngineName(String engineName) 
  {
    this.engineName=engineName;
  }

  public void setLocale(Locale locale) 
  {
    this.locale=locale;
  }

  public void setModeName(String modeName) 
  {
    this.modeName=modeName;
  }

  public void setRunning(Boolean running) 
  {
    this.running=running;
  }
}

