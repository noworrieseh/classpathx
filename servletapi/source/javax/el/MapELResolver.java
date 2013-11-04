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

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Property resolution on maps.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class MapELResolver
    extends ELResolver
{

    private static final Class unmodifiableMapClass =
        Collections.unmodifiableMap(new HashMap()).getClass();

    private final boolean readOnly;

    /**
     * New read/write resolver.
     */
    public MapELResolver()
    {
        this(false);
    }

    /**
     * New resolver with the specified behavior.
     * @param readOnly true only if the resolver is to be read-only
     */
    public MapELResolver(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Returns the value at the given index.
     * @param property the map index
     */
    public Object getValue(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base instanceof Map)
          {
            context.setPropertyResolved(true);
            Map map = (Map) base;
            return map.get(property);
        }
        return null;
    }

    /**
     * Returns the type of values in this map.
     * @param property the map index
     */
    public Class getType(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base instanceof Map)
          {
            context.setPropertyResolved(true);
            return Object.class;
          }
        return null;
    }

    /**
     * Sets the value at the given index.
     * @param property the map index
     */
    public void setValue(ELContext context, Object base, Object property,
            Object value)
        throws NullPointerException, PropertyNotFoundException,
               PropertyNotWritableException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base instanceof Map)
          {
            context.setPropertyResolved(true);
            if (readOnly)
              {
                Object[] args = new Object[] { base.getClass().getName() };
                String message = formatMessage(context, 
                        "resolverNotWriteable", args);
                throw new PropertyNotWritableException(message);
              }
            Map map = (Map) base;
            try
              {
                map.put(property, value);
              }
            catch (IndexOutOfBoundsException e)
              {
                throw new PropertyNotFoundException(e);
              }
            catch (UnsupportedOperationException e)
              {
                throw new PropertyNotWritableException(e);
              }
        }
    }

    /**
     * Indicates whether the value at the specified index is read-only.
     * @param property the map index
     */
    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base instanceof Map)
          {
            context.setPropertyResolved(true);
            Map map = (Map) base;
            if (unmodifiableMapClass.equals(map.getClass()))
              {
                return true;
              }
          }
        return readOnly;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (base instanceof Map)
          {
            Map map = (Map) base;
            List<FeatureDescriptor> acc =
                new ArrayList<FeatureDescriptor>(map.size());
            for (Iterator i = map.keySet().iterator(); i.hasNext(); )
              {
                Object key = i.next();
                FeatureDescriptor descriptor = new FeatureDescriptor();
                descriptor.setName(key.toString());
                descriptor.setDisplayName(key.toString());
                descriptor.setPreferred(true);
                descriptor.setValue(TYPE, key.getClass());
                descriptor.setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.FALSE);
                acc.add(descriptor);
              }
            return acc.iterator();
          }
        return null;
    }

    /**
     * If the base is a map, returns Object.class. Otherwise returns
     * null.
     */
    public Class getCommonPropertyType(ELContext context, Object base)
    {
        return (base instanceof Map) ? Object.class : null;
    }

}
