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

import java.io.*;
import java.util.*;
import javax.speech.synthesis.*;

public class Central 
{
  static java.util.Vector centrals;

  static 
  {
    centrals = new java.util.Vector();
    File user = new File(System.getProperty("user.home")+
			 System.getProperty("file.separator")+
			 "speech.properties");
    File system = new File(System.getProperty("java.home")+
			   System.getProperty("file.separator")+
			   "speech.properties");
    Properties p = new Properties();
    if (user.exists()) 
    {
      try 
      {
	p.load(new FileInputStream(user));
      } catch (IOException ignore) {}
    }

    if (system.exists()) 
    {
      try 
      {
	p.load(new FileInputStream(system));
      } catch (IOException ignore) {}
    }

    Enumeration e = p.propertyNames();
    while(e.hasMoreElements()) 
    try 
    {
      registerEngineCentral(p.getProperty((String)e.nextElement()));
    } catch (EngineException ex) {ex.printStackTrace();}
  }
    
  public static EngineList availableRecognizers(EngineModeDesc require) 
  {
    EngineList result = new EngineList();
    EngineList temp;
    EngineCentral central;
    for(int i=0;i<centrals.size();i++) 
    {
      central = (EngineCentral)centrals.elementAt(i);
      temp = central.createEngineList(require);
      if (temp!=null)
      for(int j=0;j<temp.size();j++)
      result.addElement(temp.elementAt(j));
    }
    return result;
  }
    
  public static EngineList availableSynthesizers(EngineModeDesc require) 
  {
    EngineList result = new EngineList();
    EngineList temp;
    EngineCentral central;
    System.out.println(centrals.size());
    for(int i=0;i<centrals.size();i++) 
    {
      central = (EngineCentral)centrals.elementAt(i);
      temp = central.createEngineList(require);
      if (temp!=null)
      for(int j=0;j<temp.size();j++)
      result.addElement(temp.elementAt(j));
    }
    return result;
  }
    
  public static Engine createRecognizer(EngineModeDesc require) 
  {
    if (require==null)
    require = new EngineModeDesc(java.util.Locale.getDefault());

    if (require instanceof EngineCreate)
    return ((EngineCreate)require).createEngine();
    else 
    {
      EngineList el = availableRecognizers(require);
      if (el.size()!=0)
      return ((EngineCreate)el.elementAt(0)).createEngine();
      else 
      return null;
    }
  }
    
  public static Synthesizer createSynthesizer(EngineModeDesc require) 
  {
    if (require==null)
    require = new SynthesizerModeDesc(java.util.Locale.getDefault());

    if (require instanceof EngineCreate)
    return (Synthesizer)((EngineCreate)require).createEngine();
    else 
    {
      EngineList el = availableSynthesizers(require);
      if (el.size()!=0)
      return (Synthesizer)((EngineCreate)el.elementAt(0)).createEngine();
      else 
      return null;
    }
  }
    
  public static void registerEngineCentral(String className) 
  throws EngineException
  {
    for(int i=0;i<centrals.size();i++)
    if (centrals.elementAt(i).getClass().getName().equals(className))
    return;
    try 
    {
      centrals.addElement((EngineCentral)(Class.forName(className).newInstance()));
    } 
    catch (ClassCastException e) 
    {
      throw new EngineException(className+" does not implement javax.speech.EngineCentral.");
    }
    catch (ClassNotFoundException e) 
    {
      throw new EngineException(className+" was not found.");
    }
    catch (InstantiationException e) 
    {
      throw new EngineException("Error instantiating "+className);
    }
    catch (IllegalAccessException e) 
    {
      throw new EngineException("Cannot access "+className);
    }
  }
}

