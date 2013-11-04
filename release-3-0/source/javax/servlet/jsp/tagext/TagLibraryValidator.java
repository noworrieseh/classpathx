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

import java.util.Map;
import java.util.HashMap;

/**
 * Validator operates on the XML view associated with the JSP page.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public abstract class TagLibraryValidator
{

    private Map<String,Object> initParameters;

    public TagLibraryValidator()
    {
    }

    /**
     * Get the init parameters.
     * @return the InitParameters value.
     */
    public Map<String,Object> getInitParameters()
    {
        return initParameters;
    }

    /**
     * Set the init parameters.
     * @param map the new parameter values
     */
    public void setInitParameters(Map<String,Object> map)
    {
        initParameters = map;
    }

    /**
     * If validation is OK, the method returns null, otherwise,
     * it returns an array of messages.
     * @return ValidationMessages or null if no error occurs.
     */
    public ValidationMessage[] validate(String prefix,
            String uri,
            PageData page)
    {
        return null;
    }

    /**
     * Release any data
     */
    public void release()
    {
        initParameters = null;
    }

}
