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

import javax.servlet.jsp.JspException;

/**
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public interface BodyTag
    extends IterationTag
{

    /**
     * Request a new BodyContent for evaluating this tag.
     */
    public static final int EVAL_BODY_BUFFERED = 2;

    /**
     * 
     * @deprecated Use BodyTag.EVAL_BODY_BUFFERED or IterationTag.EVAL_BODY_AGAIN
     */
    public static final int EVAL_BODY_TAG = 2;

    /**
     * Set the bodyContent property.
     * 
     * @param bodyContent the body content
     * @see #doInitBody()
     * @see IterationTag#doAfterBody()
     */
    public void setBodyContent(BodyContent b);

    /**
     * Prepare for the evaluation of the body.
     *
     * @throws JspException
     * @see IterationTag#doAfterBody()
     */
    public void doInitBody()
        throws JspException;

}
