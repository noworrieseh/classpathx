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
 * Method metadata.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class MethodInfo
{

    private final String name;
    private final Class<?> returnType;
    private final Class<?>[] paramTypes;

    public MethodInfo(String name, Class<?> returnType, Class<?>[] paramTypes)
    {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public String getName()
    {
        return name;
    }

    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }

    public Class<?> getReturnType()
    {
        return returnType;
    }

}
