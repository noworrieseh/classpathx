/*
 * TagData.java -- XXX
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

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Translation-time only attribute/value information for a tag instance.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagData implements Cloneable
{
  
  /** Should this field be static?
   * 
   */
  private Hashtable attrs = new Hashtable();

  /**
   * 
   */
  public static final Object REQUEST_TIME_VALUE = new Object();

  /**
   * Constructor for TagData.
   * 
   * @param atts the static attributes and values. May be null.
   */
  public TagData(Object[][] atts)
  {
    if (atts!=null)
    {
      this.attrs = new Hashtable(atts.length);
      for ( int loop=0; loop<atts.length; loop++ )
      {
        Object attName = null;
        Object value = null;
        // avoid ArrayOutOfBoundException
        if ( atts[loop].length==2 )
        {
          attName=atts[loop][0];
          value=atts[loop][1];
        }
        this.setAttribute( attName.toString(), value );
      }
    }
  }
  
  /**
   * Constructor for TagData.
   * 
   * @param attrs a hashtable to get the values from.
   */
  public TagData(Hashtable attrs)
  {
    this.attrs = attrs;
  }

  /**
   * The value of the ID if available
   * @return the value of the id or null
   */
  public String getId()
  {
    String value = null;
    if (this.attrs!=null)
    {
      if (this.attrs.get(TagAttributeInfo.ID)!=null)
      {
        value = this.attrs.get(TagAttributeInfo.ID).toString();
      }
    }
    return value;
  }

  /**
   * The value of the attribute.
   * @param attName the name of the attribute.
   * @return the attribute's value object.
   */
  public Object getAttribute(String attName)
  {
    Object value = null;
    if (attName != null && this.attrs!=null)
    {
      value =  this.attrs.get(attName);
    }
    return value;
  }

  /**
   * Set the value of an attribute.
   * @param attName the name of the attribute
   * @param value the value.
   */
  public void setAttribute(String attName,
                           Object value)
  {
    if (this.attrs==null)
    {
      this.attrs = new Hashtable();
    }
    // avoid NullPointerException
    if (attName!=null && value!=null)
    {
      this.attrs.put( attName, value );
    }
  }

  /**
   * Get the value for a given attribute.
   * @param attName the name of the attribute.
   * @return the value
   * @throws ClassCastException if attribute value is not a String
   */
  public String getAttributeString(String attName)
  {
    String result = null;
    if (this.attrs!=null)
    {
      result = (String)this.attrs.get( attName );
    }
    return result;
  }

  /**
   * Enumerates the attributes.
   * @return an Enumeration of the attributes in the TagData.
   */
  public Enumeration getAttributes()
  {
    Enumeration result = null;
    if (this.attrs!=null)
    {
      result = this.attrs.keys();
    }
    return result;
  }

}
