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
 * 
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public interface Tag
    extends JspTag
{

    /**
     * 
     */
    public static final int EVAL_BODY_INCLUDE = 1;

    /**
     * 
     */
    public static final int EVAL_PAGE = 6;

    /**
     * 
     */
    public static final int SKIP_BODY = 0;


    /**
     * 
     */
    public static final int SKIP_PAGE = 5;

    /**
     * 
     * 
     * @param pageContext
     */
    public void setPageContext(PageContext pageContext);

    /**
     * 
     * 
     * @param tag 
     */
    public void setParent(Tag tag);

    /**
     * 
     * 
     * @return the parent tag 
     * @see TagSupport#findAncestorWithClass(javax.servlet.jsp.tagext.Tag,java.lang.Class)
     */
    public Tag getParent();

    /**
     * 
     * 
     * @return EVAL_BODY_INCLUDE or SKIP_BODY
     * @throws JspException
     * @see BodyTag
     */
    public int doStartTag()
        throws JspException;

    /**
     * 
     * 
     * @return continue the evaluation or not
     * throws JspException
     */
    public int doEndTag()
        throws JspException;

    /**
     * 
     */ 
    public void release();

}
