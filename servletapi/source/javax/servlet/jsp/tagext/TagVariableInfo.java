/*
 * TagVariableInfo.java -- XXX
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
 * Variable information for a tag in a TagLib.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagVariableInfo
{
  
  private String nameGiven;
  private String nameFromAttribute;
  private String className;
  private boolean declare;
  private int scope;

  /**
   * Construct information about a tag.
   * 
   * @param nameGiven
   * @param nameFromAttribute
   * @param className
   * @param declare
   * @param scope
   */
  public TagVariableInfo(String nameGiven,
                         String nameFromAttribute,
                         String className,
                         boolean declare,
                         int scope)
  {
    this.nameGiven = nameGiven;
    this.nameFromAttribute = nameFromAttribute;
    this.className = className;
    this.declare = declare;
    this.scope = scope;
  }

  /**
   * Get the NameGiven value.
   * @return the NameGiven value.
   */
  public String getNameGiven() {
    return nameGiven;
  }
  
  /**
   * Get the NameFromAttribute value.
   * @return the NameFromAttribute value.
   */
  public String getNameFromAttribute() {
    return nameFromAttribute;
  }
  
  /**
   * Get the ClassName value.
   * @return the ClassName value.
   */
  public String getClassName() {
    return className;
  }

  /**
   * Get the Declare value.
   * @return the Declare value.
   */
  public boolean getDeclare() {
    return declare;
  }
  
  /**
   * Get the Scope value.
   * @return NESTED scope will be returned if not defined in the TLD.
   */
  public int getScope() {
    return scope;
  }

}
