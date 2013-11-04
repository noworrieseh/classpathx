/*
 * Copyright (C) 2003, 2013 Free Software Foundation, Inc.
 *
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspEngineInfo;

/**
 * Translation-time information associated with a taglib directive,
 * and its underlying TLD file.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
 */
public class TagLibraryInfo
{

    /**
     * Functions defined in this tag library.
     */  
    protected FunctionInfo[] functions;

    /**
     * Information for this TLD.
     */
    protected String info;

    /**
     * Version of the JSP specification.
     */
    protected String jspversion;

    /**
     * Prefix assigned to this taglib from the taglib directive.
     */
    protected String prefix;

    /**
     * Short name (prefix).
     */
    protected String shortname;

    /**
     * Tag files defined in this tag library.
     * @since 2.0
     */
    protected TagFileInfo[] tagFiles;

    /**
     * Tags defined in this tag library.
     * @since 2.0
     */
    protected TagInfo[] tags;

    /**
     * Version of the tag library.
     */
    protected String tlibversion;

    /**
     * Uri attribute from the taglib directive for this library.
     */
    protected String uri;

    /**
     * The "reliable" URN.
     */
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
    public String getURI()
    {
        return uri;
    }

    /**
     * Get the PrefixString value.
     * @return the PrefixString value.
     */
    public String getPrefixString()
    {
        return prefixString;
    }

    /**
     * Get the short name.
     * @return the short name
     */
    public String getShortName()
    {
        return shortname;
    }

    /**
     * Get the URN.
     * @return the URN.
     */
    public String getReliableURN()
    {
        return urn;
    }

    /**
     * Get the info string
     * @return the info string
     */
    public String getInfoString()
    {
        return info;
    }

    /**
     * Requiered version of the jsp container
     * @return minimal version.
     * @see JspEngineInfo
     */
    public String getRequiredVersion()
    {
        return jspversion;
    }

    /**
     * Tags defined in tag lib.
     * @return tag info
     */
    public TagInfo[] getTags()
    {
        return tags;
    }

    /**
     * Tag files defined in tag lib.
     * @return tag files
     * @since 2.0
     */
    public TagFileInfo[] getTagFiles()
    {
        return tagFiles;
    }

    /**
     * TagInfo for a given tag name, looking through all the tags in tag
     * library.
     * @param shortname of the tag
     * @return the tag with the name or null if not found.
     */
    public TagInfo getTag(String shortname)
    {
        TagInfo[] tags = getTags();
        if (shortname != null && tags != null)
          {
            for (int i = 0; i < tags.length; i++)
              {
                if (shortname.equals(tags[i].getTagName()))
                  {
                    return tags[i];
                  }
              }
          }
        return null;
    }

    /**
     * TagFileInfo for a given tag name, looking through all the tag files
     * in tag library.
     * @param shortname of the tag
     * @return the TagFileInfo for the tag file
     * @since 2.0
     */
    public TagFileInfo getTagFile(String shortname)
    {
        TagFileInfo[] tagFiles = getTagFiles();
        if (shortname != null && tagFiles != null)
          {
            for (int i = 0; i < tagFiles.length; i++)
              {
                if (shortname.equals(tagFiles[i].getTagInfo().getTagName()))
                  {
                    return tagFiles[i];
                  }
              }
          }
        return null;
    }

    /**
     * Functions that are defined in tag library.
     * @return functions defined in the tag lib.
     * @since 2.0
     */
    public FunctionInfo[] getFunctions()
    {
        return functions;
    }

    /**
     * FunctionInfo for a given function name, looking through all the
     * functions in tag library.
     * @param name of the function
     * @return the FunctionInfo with the given name.
     * @since 2.0
     */
    public FunctionInfo getFunction(String name)
    {
        FunctionInfo[] functions = getFunctions();
        if (name != null && functions != null)
          {
            for (int i = 0; i < functions.length; i++)
              {
                if (name.equals(functions[i].getName()))
                  {
                    return functions[i];
                  }
              }
          }
        return null;
    }

}
