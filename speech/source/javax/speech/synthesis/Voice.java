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

public class Voice 
{
  public static final int AGE_CHILD=1;
  public static final int AGE_DONT_CARE=-1;
  public static final int AGE_MIDDLE_ADULT=2;
  public static final int AGE_NEUTRAL=4;
  public static final int AGE_OLDER_ADULT=8;
  public static final int AGE_TEENAGER=16;
  public static final int AGE_YOUNGER_ADULT=32;
  public static final int GENDER_DONT_CARE=-1;
  public static final int GENDER_FEMALE=2;
  public static final int GENDER_MALE=1;
  public static final int GENDER_NEUTRAL=0;

  int age;
  int gender;
  String name;
  String style;

  public Voice() 
  {
    age=AGE_DONT_CARE;
    gender=GENDER_DONT_CARE;
    name=null;
    style=null;
  }

  public Voice(String name, int gender, int age, String style) 
  {
    this.name=name;
    this.gender=gender;
    this.age=age;
    this.style=style;
  }

  public Object clone() 
  {
    return new Voice(name, gender, age, style);
  }

  public boolean equals(Object anObject) 
  {
    if (anObject==null)
    return false;
    Voice temp = (Voice)anObject;
    if (name==null)
    if (temp.name!=null)
    return false;
    if (!name.equals(temp.name))
    return false;
    if (gender != temp.gender)
    return false;
    if (age != temp.age)
    return false;
    if (style==null)
    if (temp.style!=null)
    return false;
    if (!style.equals(temp.style))
    return false;
    return true;
  }

  public int getAge() 
  {
    return age;
  }

  public int getGender() 
  {
    return gender;
  }

  public String getName() 
  {
    return name;
  }

  public String getStyle() 
  {
    return style;
  }

  public boolean match(Voice require) 
  {
    if (require.name!=null)
    if (!require.name.equals(name))
    return false;
    if (require.age>0)
    if (require.age!=age)
    return false;
    if (require.gender>0)
    if (require.gender!=gender)
    return false;
    if (require.style!=null)
    if (!require.style.equals(style))
    return false;
    return true;
  }

  public void setAge(int age) 
  {
    this.age=age;
  }

  public void setGender(int gender) 
  {
    this.gender=gender;
  }

  public void setName(String name) 
  {
    this.name=name;
  }

  public void setStyle(String style) 
  {
    this.style=style;
  }
}
