/*
 * TagLibraryInfo.java -- XXX
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

import javax.servlet.jsp.JspEngineInfo;

/**
 * Translation-time information associated with a taglib directive, and its underlying TLD file.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagLibraryInfo
{
  /** Functions defined in this tag library. */  
  protected FunctionInfo[] functions;

  /** Information for this TLD. */
  protected String info;

  /** Version of the JSP specification. */
  protected String jspversion;
 
  /** Prefix assigned to this taglib from the taglib directive. */
  protected String prefix;
 
  /** Short name (prefix). */
  protected String shortname;

  /** Tag files defined in this tag library. */
  protected TagFileInfo[] tagFiles;
 
  /** Tags defined in this tag library. */
  protected TagInfo[] tags;
 
  /** Version of the tag library. */
  protected String tlibversion;
 
  /** Uri attribute from the taglib directive for this library. */
  protected String uri;
 
  /** The "reliable" URN. */
  protected String urn;
 
  private String prefixString;

  /**
   * Constructor. This will invoke the constructors for TagInfo, and TagAttributeInfo after parsing the TLD file.
   * @param prefix
   * @param uri
   */
  protected TagLibraryInfo(String prefix, 
                           String uri)

  {
    this.prefixString = prefix;
    this.uri = uri;
  }

  /**
   * Get the URI value.
   * @return the URI value.
   */
  public String getURI() {
    return uri;
  }

  /**
   * Get the PrefixString value.
   * @return the PrefixString value.
   */
  public String getPrefixString() {
    return prefixString;
  }
  
  /**
   * Get the short name.
   * @return the short name
   */
  public String getShortName()
  {
    return this.shortname;
  }

  /**
   * Get the URN.
   * @return the URN.
   */
  public String getReliableURN()
  {
    return this.urn;
  }

  /**
   * Get the info string
   * @return the info string
   */
  public String getInfoString()
  {
    return this.info;
  }

  /**
   * Requiered version of the jsp container
   * @return minimal version.
   * @see JspEngineInfo
   */
  public String getRequiredVersion()
  {
    return this.jspversion;
  }

  /**
   * Tags defined in this tag lib.
   * @return tag info
   */
  public TagInfo[] getTags()
  {
    return this.tags;
  }

  /**
   * Tag files defined in this tag lib.
   * @return tag files
   * @since 2.0
   */
  public TagFileInfo[] getTagFiles()
  {
    return this.tagFiles;
  }

  /**
   * TagInfo for a given tag name, looking through all the tags in this tag library.
   * @param shortname of the tag
   * @return the tag with the name or null if not found.
   */
  public TagInfo getTag(String shortname)
  {
    TagInfo t = null;
    if (shortname!=null && this.tags!=null)
    {
      boolean found=false;
      int cpt=-1;
      while(!found && cpt<this.tags.length)
      {
        cpt++;
        found = shortname.equals( this.tags[cpt].getTagName() );
      }
      if (found)
      {
        t = this.tags[cpt];
      }
    }
    return t;
  }

  /**
   * TagFileInfo for a given tag name, looking through all the tag files in this tag library.
   * @param shortname of the tag
   * @return the TagFileInfo for the tag file
   * @since 2.0
   */
  public TagFileInfo getTagFile(String shortname)
  {
    TagFileInfo t = null;
    if (shortname!=null && this.tagFiles!=null)
    {
      boolean found=false;
      int cpt=-1;
      while(!found && cpt<this.tagFiles.length)
      {
        cpt++;
        found = shortname.equals( this.tagFiles[cpt].getTagInfo().getTagName() );
      }
      if (found)
      {
        t = this.tagFiles[cpt];
      }
    }
    return t;
  }

  /**
   * Functions that are defined in this tag library.
   * @return functions defined in the tag lib.
   * @since 2.0
   */
  public FunctionInfo[] getFunctions()
  {
    return this.functions;
  }

  /**
   * FunctionInfo for a given function name, looking through all the functions in this tag library.
   * @param name of the function
   * @return the FunctionInfo with the given name.
   * @since 2.0
   */
  public FunctionInfo getFunction(String name)
  {
    FunctionInfo f = null;
    if (name!=null && this.functions!=null)
    {
      boolean found=false;
      int cpt=-1;
      while(!found && cpt<this.functions.length)
      {
        cpt++;
        found = name.equals( this.functions[cpt].getName() );
      }
      if (found)
      {
        f = this.functions[cpt];
      }
    }
    return f;
  }

}
