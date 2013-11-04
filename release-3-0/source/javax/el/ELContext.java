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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Context for EL expression evaluation.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public abstract class ELContext
{

    private Locale locale;
    private Map<Class,Object> map;
    private boolean resolved;

    /**
     * Called by ELResolver to indicate successful resolution of a property.
     */
    public void setPropertyResolved(boolean resolved)
    {
        this.resolved = resolved;
    }

    /**
     * Indicates whether a resolver has resolved a property.
     */
    public boolean isPropertyResolved()
    {
        return resolved;
    }

    /**
     * Associate a context object with this context.
     */
    public void putContext(Class key, Object contextObject)
        throws NullPointerException
    {
        if (key == null || contextObject == null)
          {
            throw new NullPointerException();
          }
        if (map == null)
          {
            map = new HashMap<Class,Object>();
          }
        map.put(key, contextObject);
    }

    /**
     * Returns the context object associated with the specified key.
     */
    public Object getContext(Class key)
    {
        return (map == null) ? null : map.get(key);
    }

    /**
     * Returns the resolver for this context.
     */
    public abstract ELResolver getELResolver();

    /**
     * Returns the function mapper for this context.
     */
    public abstract FunctionMapper getFunctionMapper();

    /**
     * Returns the locale in use for this context.
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * Sets the locale to use for this context.
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    /**
     * Returns the variable mapper for this context.
     */
    public abstract VariableMapper getVariableMapper();

}
