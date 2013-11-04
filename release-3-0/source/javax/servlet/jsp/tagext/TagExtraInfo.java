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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Optional class provided by the tag library author to describe additional
 * translation-time information not described in the TLD.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
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
        List acc = new ArrayList();
        Enumeration e = data.getAttributes();
        while (e.hasMoreElements())
          {
            String attName = (String) e.nextElement();
            Object value = data.getAttribute(attName);
            if (value instanceof String)
              {
                acc.add(new VariableInfo(attName,
                            (String) value,
                            false,
                            VariableInfo.NESTED));          
              }
          }
        if (acc.isEmpty())
          {
            return null;
          }
        VariableInfo[] ret = new VariableInfo[acc.size()];
        acc.toArray(ret);
        return ret;
    }

    /**
     * Indicates whether the data is valid.
     */
    public boolean isValid(TagData data)
    {
        return true;
    }

    /**
     * Validate the data. Prefer this method over {@link #isValid} as it
     * gives more detail.
     * @since 2.0
     * @return null or a zero length array if there are no errors, an array
     * of messages otherwise
     */
    public ValidationMessage[] validate(TagData data)
    {
        ValidationMessage[] ret = null;
        if (!isValid(data))
          {
            ret = new ValidationMessage[]
              {
                new ValidationMessage(data.getId(), "invalid")
              };
          }
        return ret;
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
