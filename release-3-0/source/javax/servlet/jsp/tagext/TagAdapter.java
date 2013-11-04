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

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

/**
 * Wraps a SimpleTag and exposes it with a Tag interface.
 * @version 2.1
 * @since 2.0
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagAdapter
    implements Tag
{

    /**
     * 
     */
    private SimpleTag adaptee;

    /**
     * Creates a new TagAdapter that wraps the given SimpleTag.
     * 
     * @param adaptee the simple tag adapted as a Tag.
     */
    public TagAdapter(SimpleTag adaptee)
    {
        this.adaptee = adaptee;
    }

    /**
     * Must not be called.
     * 
     * @param pageContext
     * @throws UnsupportedOperationException Must not be called.
     */
    public void setPageContext(PageContext pageContext)
    {
        throw new UnsupportedOperationException("Must not be called.");
    }

    /**
     * Must not be called. The parent is always getAdaptee().getparent().
     * 
     * @param tag 
     * @throws UnsupportedOperationException Must not be called.
     */
    public void setParent(Tag tag)
    {
        throw new UnsupportedOperationException("Must not be called;");
    }

    /**
     * Returns the parent of this Tag. Always getAdaptee().getParent().
     * 
     * @return the parent tag 
     * @see TagSupport#findAncestorWithClass(javax.servlet.jsp.tagext.Tag,java.lang.Class)
     */
    public Tag getParent()
    {
        Tag result = TagSupport.findAncestorWithClass( ((Tag)this.getAdaptee()).getParent(), Tag.class );
        return result;
    }

    /**
     * Gets the tag. This should be an instance of SimpleTag in JSP2.0.
     * 
     * @return the tag that is being adapted.
     */
    public JspTag getAdaptee()
    {
        return this.adaptee;
    }

    /**
     * Must not be called.
     * 
     * @return always throws UnsupportedOperationException.
     * @throws JspException
     * @throws UnsupportedOperationException Must not be called.
     * @see BodyTag
     */
    public int doStartTag()
        throws JspException
    {
        throw new UnsupportedOperationException("Must not be called.");
    }

    /**
     * Must not be called.
     * 
     * @return always throws UnsupportedOperationException.
     * @throws JspException
     * @throws UnsupportedOperationException Must not be called.
     */
    public int doEndTag()
        throws JspException
    {
        throw new UnsupportedOperationException("Must not be called.");
    }

    /**
     * Must not be called.
     * 
     * @throws UnsupportedOperationException Must not be called.
     */
    public void release()
    {
        throw new UnsupportedOperationException("Must not be called.");
    }

}
