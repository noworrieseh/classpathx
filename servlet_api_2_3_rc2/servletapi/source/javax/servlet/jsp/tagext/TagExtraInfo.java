/*
 * TagExtraInfo.java -- XXX
 * 
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck (arnaud.vandyck@ulg.ac.be)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation  
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp.tagext;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Optional class provided by the tag library author to describe additional translation-time information not described in the TLD.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagExtraInfo
{

  private TagInfo tagInfo;
  
  /**
   * Constructor.
   */
  public TagExtraInfo()
  {
  }
  
  /**
   * Information on scripting variables defined by the tag associated with this TagExtraInfo instance.
   * @param data TagData instance
   * @return variableinfo defined in the tag data
   */
  public VariableInfo[] getVariableInfo(TagData data)
  {
    VariableInfo[] vi = null;
    Enumeration e = data.getAttributes();
    Vector v = new Vector();
    while(e.hasMoreElements())
      {
        String attName = (String)e.nextElement();
        Object value = data.getAttribute( attName );
        if ( value instanceof String ) {
          v.add( new VariableInfo( attName, (String) value, false, VariableInfo.NESTED ) );          
        } // end of if ()
      }
    if ( v!=null && !v.isEmpty() ) {
      vi = new VariableInfo[ v.size() ];
      vi = (VariableInfo[])v.toArray( vi );
    } // end of if ()
    return vi;
  }

  /**
   * 
   * @return false
   */
  public boolean isValid(TagData data)
  {
    return false;
  }
  
  /**
   * 
   * @return null
   */
  public ValidationMessage[] validate(TagData data)
  {
    return null;
  }

  /**
   * Set the value of TagInfo.
   * @param tagInfo the TagInfo value
   */
  public final void setTagInfo(TagInfo tagInfo)
  {
    this.tagInfo = tagInfo;
  }

  /**
   * Get the TagInfo value.
   * @return the tagInfo value
   */
  public final TagInfo getTagInfo()
  {
    return this.tagInfo;
  }

}
