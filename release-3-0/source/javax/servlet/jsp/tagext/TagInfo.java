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
 * 
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagInfo
{
    /** 
     * Static content for getBodyContent() when it's a JSP.
     */
    public static final String BODY_CONTENT_JSP = "JSP";

    /**
     * Static content for getBodyContent() when it's Tag dependent.
     */
    public static final String BODY_CONTENT_TAG_DEPENDENT = "TAGDEPENDENT";

    /**
     * Static content for getBodyContent() when it's empty.
     */
    public static final String BODY_CONTENT_EMPTY = "EMPTY";

    /**
     * Static content for getBodyContent() when it's scriptless.
     * @since 2.0
     */
    public static final String BODY_CONTENT_SCRIPTLESS = "SCRIPTLESS";

    private String tagName;
    private String tagClassName;
    private String bodycontent;
    private String infoString;
    private TagLibraryInfo taglib;
    private TagExtraInfo tagExtraInfo;
    private TagAttributeInfo[] attributeInfo;
    private String displayName;
    private String smallIcon;
    private String largeIcon;
    private TagVariableInfo[] tvi;
    private boolean dynamicAttributes;

    /**
     * Constructor for TagInfo from data in the JSP 2.0 format for
     * TLD.
     * 
     * @param tagName
     * @param tagClassName
     * @param bodycontent
     * @param infoString
     * @param taglib
     * @param tagExtraInfo
     * @param attributeInfo
     */
    public TagInfo(String tagName,
            String tagClassName,
            String bodycontent,
            String infoString,
            TagLibraryInfo taglib,
            TagExtraInfo tagExtraInfo,
            TagAttributeInfo[] attributeInfo)
    {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodycontent = bodycontent;
        this.infoString = infoString;
        this.taglib = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
    }

    /** 
     * Constructor for TagInfo from data in the JSP 1.2 format for
     * TLD.
     * 
     * @param tagName
     * @param tagClassName
     * @param bodycontent
     * @param infoString
     * @param taglib
     * @param tagExtraInfo
     * @param attributeInfo
     * @param displayName
     * @param smallIcon
     * @param largeIcon
     * @param tvi
     */
    public TagInfo(String tagName,
            String tagClassName,
            String bodycontent,
            String infoString,
            TagLibraryInfo taglib,
            TagExtraInfo tagExtraInfo,
            TagAttributeInfo[] attributeInfo,
            String displayName,
            String smallIcon,
            String largeIcon,
            TagVariableInfo[] tvi)
    {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodycontent = bodycontent;
        this.infoString = infoString;
        this.taglib = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
        this.displayName = displayName;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.tvi = tvi;
    }

    /**
     * Constructor for TagInfo from data in the JSP 2.0 format for
     * TLD.
     *
     * @param tagName
     * @param tagClassName
     * @param bodycontent
     * @param infoString
     * @param taglib
     * @param tagExtraInfo
     * @param attributeInfo
     * @param displayName
     * @param smallIcon
     * @param largeIcon
     * @param tvi
     * @param dynamicAttributes
     * @since 2.0
     */
    public TagInfo(String tagName,
            String tagClassName,
            String bodycontent,
            String infoString,
            TagLibraryInfo taglib,
            TagExtraInfo tagExtraInfo,
            TagAttributeInfo[] attributeInfo,
            String displayName,
            String smallIcon,
            String largeIcon,
            TagVariableInfo[] tvi,
            boolean dynamicAttributes)
    {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodycontent = bodycontent;
        this.infoString = infoString;
        this.taglib = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
        this.displayName = displayName;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.tvi = tvi;
        this.dynamicAttributes = dynamicAttributes;
    }

    /**
     * The name of the Tag.
     * @return  the name of the tag.
     */
    public String getTagName()
    {
        return this.tagName;
    }

    /**
     * Attribute information (in the TLD) on this tag.
     * 
     * @return the array of TagAttributeInfo for this tag, or a
     * zero-length array if the tag has no attributes.  
     */
    public TagAttributeInfo[] getAttributes()
    {
        return this.attributeInfo;
    }

    /**
     * Information on the scripting objects created by this tag at
     * runtime.
     * @param data TagData describing this action.
     * @return if a TagExtraInfo object is associated with this TagInfo,
     * the result of getTagExtraInfo().getVariableInfo( data ), otherwise
     * null if the tag has no "id" attribute or new VariableInfo[] { new
     * VariableInfo( data.getId(), "java.lang.Object", true,
     * VariableInfo.NESTED ) } if an "id" attribute is present.
     */
    public VariableInfo[] getVariableInfo(TagData data)
    {
        return this.tagExtraInfo.getVariableInfo(data);
    }

    /**
     * Translation-time validation of the attributes.
     * @param data The translation-time TagData instance.
     * @return Whether the data is valid.
     */
    public boolean isValid(TagData data)
    {
        return this.tagExtraInfo.isValid(data);
    }

    /**
     * Translation-time validation of the attributes.
     * @param data The translation-time TagData instance.
     * @return A null object, or zero length array if no errors, an array
     * of ValidationMessages otherwise.
     * @since 2.0
     */
    public ValidationMessage[] validate(TagData data)
    {
        return this.tagExtraInfo.validate(data);
    }

    /**
     * Set the instance for extra tag information.
     * @param tei TagExtraInfo instance
     */
    public void setTagExtraInfo(TagExtraInfo tei)
    {
        this.tagExtraInfo = tei;
    }

    /**
     * The instance for extra tag information.
     * @return the TagExtraInfo instance, if any.
     */
    public TagExtraInfo getTagExtraInfo()
    {
        return this.tagExtraInfo;
    }

    /**
     * Name of the class that provides the handler for this tag.
     * @return name of the tag handler class.
     */
    public String getTagClassName()
    {
        return this.tagClassName;
    }

    /**
     * bodycontent information for this tag.
     * @return the body content string.
     */
    public String getBodyContent()
    {
        return this.bodycontent;
    }

    /**
     * The information string for the tag.
     * @return the info string, or null if not defined.
     */
    public String getInfoString()
    {
        return this.infoString;
    }

    /**
     * Set the TagLibraryInfo property.
     * 
     * @param tl the TagLibraryInfo.
     */
    public void setTagLibrary(TagLibraryInfo tl)
    {
        this.taglib = tl;
    }

    /**
     * The instance of TabLibraryInfo we belong to.
     * @return the tag library instance we belong to.
     */
    public TagLibraryInfo getTagLibrary()
    {
        return this.taglib;
    }

    /**
     * Get the displayName.
     * @return a short name to be displayed by tools, or null if not defined
     */
    public String getDisplayName()
    {
        return this.displayName;
    }

    /**
     * Get the path to the small icon.
     * @return path to a small icon to be displayed by tools, or null if not defined
     */
    public String getSmallIcon()
    {
        return this.smallIcon;
    }

    /**
     * Get the path to the large icon.
     * @return path to a large icon to be displayed by tools, or null if not defined
     */
    public String getLargeIcon()
    {
        return this.largeIcon;
    }

    /**
     * Get TagVariableInfo objects associated with this TagInfo.
     * @return Array of TagVariableInfo objects corresponding to variables
     * declared by this tag, or a zero length array if no variables have
     * been declared
     */
    public TagVariableInfo[] getTagVariableInfos()
    {
        return this.tvi;
    }

    /**
     * Get dynamicAttributes associated with this TagInfo.
     * @return true if tag handler supports dynamic attributes
     * @since 2.0
     */
    public boolean hasDynamicAttributes()
    {
        return this.dynamicAttributes;
    }

}
