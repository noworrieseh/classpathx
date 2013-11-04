/*
 * Copyright (C) 2013 Free Software Foundation, Inc.
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

import javax.el.ELContextListener;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;

/**
 * Web application context for JSP containers.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public interface JspApplicationContext
{

    /**
     * Add a resolver for EL variable and property resolution.
     */
    void addELResolver(ELResolver resolver);

    /**
     * Returns the factory use to create EL expressions.
     */
    ExpressionFactory getExpressionFactory();

    /**
     * Registers a listener for EL context lifecycle events.
     */
    void addELContextListener(ELContextListener listener);

}
