/*
 * TagFileInfo.java -- XXX
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
 * Tag information for a tag file in a tag lib.
 *
 * @since 2.0
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagFileInfo
{
  
  private String name;
  private String path;
  private TagInfo tagInfo;

  /**
   * Constructor.
   * @param name
   * @param path
   * @param tagInfo
   */
  public TagFileInfo(String name, String path, TagInfo tagInfo)
  {
    this.name = name;
    this.path = path;
    this.tagInfo = tagInfo;
  }

  /**
   * Get the unique Name of this tag.
   * @return the Name value.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Where to find the .tag file implementing this action.
   * @return the path
   */
  public String getPath() {
    return path;
  }
  
  /**
   * Get the TagInfo.
   * @return the TagInfo.
   */
  public TagInfo getTagInfo() {
    return tagInfo;
  }
  
}
