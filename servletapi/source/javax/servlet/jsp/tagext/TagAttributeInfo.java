/*
 * TagAttributeInfo.java -- XXX
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

/**
 * Information for the attributes of a Tag.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagAttributeInfo
{

  /**
   * "id"
   */
  public static final String ID = "id";

  private String name;

  private String typeName;

  private boolean required;

  private boolean requestTime;

  private boolean fragment;

  /**
   * Constructor for TagAttributeInfo.
   * @param name the name of the attribute.
   * @param required is the attribute required in tag instances.
   * @param type type name of the attribute.
   * @param reqTime 
   */
  public TagAttributeInfo(String name,
                          boolean required,
                          String type,
                          boolean reqTime)
  {
    this.name = name;
    this.required = required;
    this.typeName = type;
    this.requestTime = reqTime;
  }
  
  /**
   * Constructor for TagAttributeInfo in JSP2.0.
   * @param name the name of the attribute.
   * @param required is the attribute required in tag instances.
   * @param type type name of the attribute.
   * @param reqTime 
   * @param fragment JspFragment or not?
   * @since JSP2.0
   */
  public TagAttributeInfo(String name,
                          boolean required,
                          String type,
                          boolean reqTime,
                          boolean fragment)
  {
    this.name = name;
    this.required = required;
    this.typeName = type;
    this.requestTime = reqTime;
    this.fragment = fragment;
  }
  
  /**
   * Get the Name of the attribute.
   * @return the name of the attribute.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the Type (as a String) of the attribute.
   * @return the Type of the attribute (as a String).
   */
  public String getTypeName() {
    return typeName;
  }
  
  /**
   * 
   */
  public boolean canBeRequestTime()
  {
    return this.requestTime;
  }

  /**
   * 
   */
  public boolean isRequired()
  {
    return this.required;
  }

  /**
   * Parse an array of TagAttributeInfo and find the object with the name equals to "id".
   * @param an array of TagAttributeInfo
   */
  public static TagAttributeInfo getIdAttribute(TagAttributeInfo[] a)
  {
    TagAttributeInfo returnTag = null;
    int loop=0;
    int max=a.length;
    boolean found=false;
    while( (!found) && (loop<max) )
      {
        found= ( TagAttributeInfo.ID.equals(a[loop].getName()) );
        loop = (found) ? loop : loop+1;
      }
    if(found)
      {
        returnTag = a[loop];
      }
    return returnTag;
  }

  /**
   * 
   * @since JSP2.0
   */
  public boolean isFragment()
  {
    return this.fragment;
  }

  /**
   * String representation of this TagAttributeInfo (for debugging).
   * @return a String representation for debugging purpose.
   */
  public String toString()
  {
    return "Name: " + this.name + " (type: " + this.typeName + ")"
      + " - isRequired?" + this.required
      + " - canBeRequestTime?" + this.requestTime
      + " - isFragment?" + this.fragment;
  }
  
}
