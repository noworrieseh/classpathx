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

public class Word 
{
  public static long  ABBREVIATION=1;
  public static long  ADJECTIVE=2; 
  public static long  ADVERB=4;
  public static long  AUXILIARY=8;
  public static long  CARDINAL=16;
  public static long  CONJUNCTION=32;
  public static long  CONTRACTION=64;
  public static long  DETERMINER=128;
  public static long  DONT_CARE=256;
  public static long  NOUN=512;
  public static long  OTHER=1024;
  public static long  PREPOSITION=2048;
  public static long  PRONOUN=4096;
  public static long  PROPER_ADJECTIVE=8192;
  public static long  PROPER_NOUN=16384;
  public static long  UNKNOWN=32768;
  public static long  VERB=65536;

  long cats;
  String[] pron;
  String spoken;
  String written;

  public Word() 
  {
  }

  public long getCategories() 
  {
    return cats;
  }

  public String[] getPronunciations() 
  {
    return pron;
  }

  public String getSpokenWord() 
  {
    return spoken;
  }

  public String getWrittenWord() 
  {
    return written;
  }

  public void setCategories(long cats) 
  {
    this.cats=cats;
  }

  public void setPronunciations(String[] prons) 
  {
    this.pron=prons;
  }

  public void setSpokenWord(String spoken) 
  {
    this.spoken=spoken;
  }

  public void setWrittenWord(String written) 
  {
    this.written=written;
  }
}
