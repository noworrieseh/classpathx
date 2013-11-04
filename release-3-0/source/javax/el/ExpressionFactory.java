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
 * Parser that can parse strings into expressions for evaluation.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public abstract class ExpressionFactory
{

    /**
     * Parses an expression into a {@link javax.el.ValueExpression}.
     */
    public abstract ValueExpression createValueExpression(ELContext context,
            String expression,
            Class<?> expectedType)
        throws NullPointerException, ELException;

    /**
     * Creates a {@link javax.el.ValueExpression} that wraps an object.
     */
    public abstract ValueExpression createValueExpression(Object instance,
            Class<?> expectedType);

    /**
     * Parses an expression into a {@link javax.el.MethodExpression}.
     */
    public abstract MethodExpression createMethodExpression(ELContext context,
            String expression,
            Class<?> expectedReturnType,
            Class<?>[] expectedParamTypes)
        throws NullPointerException, ELException;

    /**
     * Coerces an object to a specific type according to the type conversion
     * rules.
     */
    public abstract Object coerceToType(Object obj, Class<?> targetType)
        throws ELException;

}
