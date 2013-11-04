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
 * Information for the attributes of a Tag.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
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
    private String description;
    private boolean deferredValue;
    private boolean deferredMethod;
    private String expectedTypeName;
    private String methodSignature;

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
     * @since 2.0
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
     * 2.1 constructor.
     * @param name name of the attribute
     * @param required if the attribute is required in the tag
     * @param type name of the type of the attribute
     * @param reqTime true if the attribute has a request-time
     * @param fragment true if the attribute is a JspFragment
     * @param description
     * @param deferredValue true if the attribute is a deferred value
     * @param deferredMethod true if the attribute is a deferred method
     * @param expectedTypeName expected type of the deferred value
     * @param methodSignature expected method signature of deferred method
     * @since 2.1
     */
    public TagAttributeInfo(String name,
            boolean required,
            String type,
            boolean reqTime,
            boolean fragment,
            String description,
            boolean deferredValue,
            boolean deferredMethod,
            String expectedTypeName,
            String methodSignature)
    {
        this.name = name;
        this.required = required;
        this.typeName = type;
        this.requestTime = reqTime;
        this.fragment = fragment;
        this.description = description;
        this.deferredValue = deferredValue;
        this.deferredMethod = deferredMethod;
        this.expectedTypeName = expectedTypeName;
        this.methodSignature = methodSignature;
    }

    /**
     * Get the Name of the attribute.
     * @return the name of the attribute.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the Type (as a String) of the attribute.
     * @return the Type of the attribute (as a String).
     */
    public String getTypeName()
    {
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
        for (int i = 0; i < a.length; i++)
          {
            if (TagAttributeInfo.ID.equals(a[i].getName()))
              {
                return a[i];
              }
          }
        return null;
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
        StringBuilder buf = new StringBuilder(super.toString())
            .append("[name=")
            .append(name)
            .append(",typeName=")
            .append(typeName);
        if (required)
        {
            buf.append(",required");
        }
        if (requestTime)
        {
            buf.append(",requestTime");
        }
        if (fragment)
        {
            buf.append(",fragment");
        }
        if (description != null)
        {
            buf.append("description=");
            buf.append(description);
        }
        if (deferredValue)
        {
            buf.append(",deferredValue");
        }
        if (deferredMethod)
        {
            buf.append(",deferredMethod");
        }
        buf.append("]");
        return buf.toString();
    }

}
