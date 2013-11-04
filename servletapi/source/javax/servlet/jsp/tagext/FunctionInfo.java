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
 * Information for a function in a Tag Library.
 * @version 2.1
 * @since 2.0
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class FunctionInfo
{

    private String name;
    private String functionClass;
    private String functionSignature;

    /**
     * Construct the function info.
     * @param name the name of the function
     * @param functionClass the class of the function
     * @param functionSignature the signature of the function
     */
    public FunctionInfo(String name,
            String functionClass,
            String functionSignature)
    {
        this.name = name;
        this.functionClass = functionClass;
        this.functionSignature = functionSignature;
    }

    /**
     * Get the Name value.
     * @return the Name value.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the FunctionClass value.
     * @return the FunctionClass value.
     */
    public String getFunctionClass()
    {
        return functionClass;
    }

    /**
     * Get the FunctionSignature value.
     * @return the FunctionSignature value.
     */
    public String getFunctionSignature()
    {
        return functionSignature;
    }

}
