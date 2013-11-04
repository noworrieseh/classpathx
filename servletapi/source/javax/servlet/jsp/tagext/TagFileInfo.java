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

/**
 * Tag information for a tag file in a tag lib.
 * @version 2.1
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
