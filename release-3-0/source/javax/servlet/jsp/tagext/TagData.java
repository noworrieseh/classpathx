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

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Translation-time only attribute/value information for a tag instance.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
 */
public class TagData
    implements Cloneable
{

    private Hashtable<String,Object> attrs;

    /**
     * 
     */
    public static final Object REQUEST_TIME_VALUE = new Object();

    /**
     * Constructor for TagData.
     * 
     * @param attrs the static attributes and values. May be null.
     */
    public TagData(Object[][] attrs)
    {
        if (attrs == null)
          {
            this.attrs = new Hashtable<String,Object>();
          }
        else
          {
            this.attrs = new Hashtable<String,Object>(attrs.length);
            for (int i = 0; i<attrs.length; i++)
              {
                Object attName = attrs[i][0];
                Object value = attrs[i][1];
                this.attrs.put(attName.toString(), value);
              }
          }
    }

    /**
     * Constructor for TagData.
     * 
     * @param attrs a hashtable to get the values from.
     */
    public TagData(Hashtable<String,Object> attrs)
    {
        this.attrs = new Hashtable<String,Object>(attrs.size());
        this.attrs.putAll(attrs);
    }

    /**
     * The value of the ID if available
     * @return the value of the id or null
     */
    public String getId()
    {
        return getAttributeString(TagAttributeInfo.ID);
    }

    /**
     * The value of the attribute.
     * @param attName the name of the attribute.
     * @return the attribute's value object.
     */
    public Object getAttribute(String attName)
    {
        return attrs.get(attName);
    }

    /**
     * Set the value of an attribute.
     * @param attName the name of the attribute
     * @param value the value.
     */
    public void setAttribute(String attName, Object value)
    {
        attrs.put(attName, value);
    }

    /**
     * Get the value for a given attribute.
     * @param attName the name of the attribute.
     * @return the value
     * @throws ClassCastException if attribute value is not a String
     */
    public String getAttributeString(String attName)
    {
        return (String) attrs.get(attName);
    }

    /**
     * Enumerates the attributes.
     * @return an Enumeration of the attributes in the TagData.
     */
    public Enumeration getAttributes()
    {
        return attrs.keys();
    }

}
