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

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;

/**
 * Implementation of the BodyTag interface.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class BodyTagSupport
    extends TagSupport
    implements BodyTag
{

    /**
     * Current bodyContent.
     */
    protected BodyContent bodyContent;

    /**
     * The parent
     */
    private Tag parent;

    /**
     * Default constructor.
     */
    public BodyTagSupport()
    {
    }

    /**
     * Default processing of the start tag
     * 
     * @return EVAL_BODY_BUFFERED
     * @throws javax.servlet.jsp.JspException
     * @see Tag#doStartTag()
     */
    public int doStartTag()
        throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Default processing of the end tag
     * 
     * @return EVAL_PAGE
     * @throws javax.servlet.jsp.JspException
     * @see Tag#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * Prepare for evaluation of the body.
     * 
     * @param bodyContent the body content
     * @see #doInitBody()
     * @see #doAfterBody()
     * @see BodyTag#setBodyContent(javax.servlet.jsp.tagext.BodyContent)
     */
    public void setBodyContent(BodyContent bodyContent)
    {
        this.bodyContent = bodyContent;
    }

    /**
     * Prepare for the evaluation of the body.
     *
     * @throws JspException
     * @see #setBodyContent(javax.servlet.jsp.tagext.BodyContent)
     * @see #doAfterBody()
     * @see BodyTag#doInitBody()
     */
    public void doInitBody()
        throws JspException
    {
    }

    /**
     *
     * @return SKIP_BODY
     * @throws JspException if there is an error while processing this tag.
     * @see #doInitBody()
     * @see IterationTag#doAfterBody()
     */
    public int doAfterBody()
        throws JspException
    {
        return SKIP_BODY;
    }

    /**
     *
     * @see Tag#release()
     */
    public void release()
    {
    }

    /**
     * @return the body content.
     */
    public BodyContent getBodyContent()
    {
        return this.bodyContent;
    }

    /**
     * @return the enclosing JspWriter, from the bodyContent
     */
    public JspWriter getPreviousOut()
    {
        return bodyContent.getEnclosingWriter();
    }

    /**
     * @param parent the parent tag
     */
    public void setParent(Tag parent)
    {
        this.parent = parent;
    }

    /**
     * @return the parent tag
     */
    public Tag getParent()
    {
        return this.parent;
    }

}
