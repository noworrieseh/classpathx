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
package javax.el;

/**
 * Expression representing a method on a specific object.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public abstract class MethodExpression
    extends Expression
{

    /**
     * Returns information about the referenced method.
     * @param context the evaluation context
     */
    public abstract MethodInfo getMethodInfo(ELContext context)
        throws NullPointerException, PropertyNotFoundException,
               MethodNotFoundException, ELException;

    /**
     * If the expression is a string literal, returns its value coerced to
     * the expected return type of the method signature. If not, returns the
     * result of the method invocation using the specified parameters.
     * @param context the evaluation context
     * @param params the parameters to pass the method or null
     * @return the result of the method invocation or null if void
     */
    public abstract Object invoke(ELContext context, Object[] params)
        throws NullPointerException, PropertyNotFoundException,
               MethodNotFoundException, ELException;

}
