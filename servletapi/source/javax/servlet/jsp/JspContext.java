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
package javax.servlet.jsp;

import java.io.Writer;
import java.util.Enumeration;
import javax.el.ELContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Base class for the PageContext.
 * 
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
 * @version 2.1
 * @since 2.0
 */
public abstract class JspContext
{

    /**
     * Set the specified attribute in page scope.
     * @param name the attribute name
     * @param attribute value of the attribute
     * @throws NullPointerException if name is null
     */
    public abstract void setAttribute(String name, Object value);

    /**
     * Set the specified attribute with the specified scope.
     * @param name the attribute name
     * @param attribute value of the attribute
     * @param scope scope of the attribute
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if scope is not a valid scope
     * @see javax.servlet.jsp.PageContext#APPLICATION_SCOPE
     * @see javax.servlet.jsp.PageContext#PAGE_SCOPE
     * @see javax.servlet.jsp.PageContext#REQUEST_SCOPE
     * @see javax.servlet.jsp.PageContext#SESSION_SCOPE
     */
    public abstract void setAttribute(String name, Object attribute, int scope);

    /**
     * Returns the specified attribute value in page scope.
     * @param name the attribute name
     * @return the object associated with this name
     * @throws NullPointerException if name is null
     */
    public abstract Object getAttribute(String name);

    /**
     * Returns the specified attribute value in the specified scope.
     * @param name the attribute name
     * @param scope scope of the attribute
     * @return the object associated with this name.
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if scope is not a valid scope
     * @see javax.servlet.jsp.PageContext#APPLICATION_SCOPE
     * @see javax.servlet.jsp.PageContext#PAGE_SCOPE
     * @see javax.servlet.jsp.PageContext#REQUEST_SCOPE
     * @see javax.servlet.jsp.PageContext#SESSION_SCOPE
     */
    public abstract Object getAttribute(String name, int scope);

    /**
     * Returns the specified attribute value from any scope, or null if not
     * found.
     * Scopes are searched in the following order: page, request, session,
     * application.
     * @param name the attribute name
     * @throws NullPointerException if name is null
     */
    public abstract Object findAttribute(String name);

    /**
     * Removes the specified attribute from all scopes.
     * @param name the attribute name
     * @throws NullPointerException if name is null
     */
    public abstract void removeAttribute(String name);

    /**
     * Removes the specified attribute from the specified scope.
     * @param name the attribute name
     * @param scope the scope
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if scope is not a valid scope
     * @see javax.servlet.jsp.PageContext#APPLICATION_SCOPE
     * @see javax.servlet.jsp.PageContext#PAGE_SCOPE
     * @see javax.servlet.jsp.PageContext#REQUEST_SCOPE
     * @see javax.servlet.jsp.PageContext#SESSION_SCOPE
     */
    public abstract void removeAttribute(String name, int scope);

    /**
     * Returns the scope of the given attribute.
     * @param name the attribute name
     * @return the scope where the attribute is defined, or 0 if not found
     * @throws NullPointerException if name is null
     */
    public abstract int getAttributesScope(String name);

    /**
     * Returns all the attribute names in the given scope.
     * @param scope the scope
     * @throws IllegalArgumentException if scope is not a valid scope
     */
    public abstract Enumeration getAttributeNamesInScope(int scope);

    /**
     * Returns the <code>out</code> page object.
     */
    public abstract JspWriter getOut();

    /**
     * Returns the EL expression evaluator.
     * @deprecated use
     * {@link javax.servlet.jsp.JspApplicationContext#getExpressionFactory()}
     * @since 2.0
     */
    public abstract ExpressionEvaluator getExpressionEvaluator();

    /**
     * Returns the variable resolver in this context.
     * @deprecated use
     * {@link javax.el.ELContext#getELResolver()}
     * @since 2.0
     */
    public abstract VariableResolver getVariableResolver();

    /**
     * Returns the EL context associated with this context.
     * @since 2.1
     */
    public abstract ELContext getELContext();

    /**
     * Returns a new JspWriter that wraps the specified writer.
     * This updates the value of the <code>out</code> page object.
     * @since 2.0
     */
    public abstract JspWriter pushBody(Writer writer);

    /**
     * Returns the <i>previous</i> JspWriter saved by the last matching call
     * to {@link #pushBody}.
     * This updates the value of the <code>out</code> page object.
     */
    public abstract JspWriter popBody();

}
