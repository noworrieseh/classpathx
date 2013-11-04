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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Property resolution on arrays.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class ArrayELResolver
    extends ELResolver
{

    private final boolean readOnly;

    /**
     * New read/write resolver.
     */
    public ArrayELResolver()
    {
        this(false);
    }

    /**
     * New resolver with the specified behavior.
     * @param readOnly true only if the resolver is to be read-only
     */
    public ArrayELResolver(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Returns the value at the given index.
     * @param property the array index
     */
    public Object getValue(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base != null && base.getClass().isArray())
          {
            context.setPropertyResolved(true);
            int i = toArrayIndex(property);
            if (i < 0 || i >= Array.getLength(base))
              {
                return null;
              }
            return Array.get(base, i);
        }
        return null;
    }

    /**
     * Returns the type of values in this array.
     * @param property the array index
     */
    public Class getType(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base != null && base.getClass().isArray())
          {
            context.setPropertyResolved(true);
            int i = toArrayIndex(property);
            if (i < 0 || i >= Array.getLength(base))
              {
                throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(i));
              }
            return base.getClass().getComponentType();
          }
        return null;
    }

    /**
     * Sets the value at the given index.
     * @param property the array index
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
        if (base != null && base.getClass().isArray())
          {
            context.setPropertyResolved(true);
            if (readOnly)
            {
                Object[] args = new Object[] { base.getClass().getName() };
                String message = formatMessage(context, 
                        "resolverNotWriteable", args);
                throw new PropertyNotWritableException(message);
            }
            int i = toArrayIndex(property);
            if (i < 0 || i >= Array.getLength(base))
              {
                throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(i));
              }
            if (value != null && !base.getClass().getComponentType().isAssignableFrom(value.getClass()))
              {
                Object[] args = new Object[]
                {
                    value.getClass().getName(),
                    base.getClass().getComponentType()
                };
                String message = formatMessage(context, 
                        "objectNotAssignable", args);
                throw new ClassCastException(message);
              }
            Array.set(base, i, value);
        }
    }

    /**
     * Indicates whether the value at the specified index is read-only.
     * @param property the array index
     */
    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base != null && base.getClass().isArray())
          {
            context.setPropertyResolved(true);
            int i = toArrayIndex(property);
            if (i < 0 || i >= Array.getLength(base))
              {
                throw new PropertyNotFoundException(new ArrayIndexOutOfBoundsException(i));
              }
          }
        return readOnly;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (base != null && base.getClass().isArray())
          {
            int len = Array.getLength(base);
            List<FeatureDescriptor> acc =
                new ArrayList<FeatureDescriptor>(len);
            for (int i = 0; i < len; i++)
              {
                FeatureDescriptor descriptor = new FeatureDescriptor();
                descriptor.setName(Integer.toString(i));
                descriptor.setDisplayName(new StringBuilder("[").append(i).append("]").toString());
                descriptor.setPreferred(true);
                descriptor.setValue(TYPE, Integer.class);
                descriptor.setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.FALSE);
                acc.add(descriptor);
              }
            return acc.iterator();
          }
        return null;
    }

    /**
     * If the base is an array, returns Integer.class. Otherwise returns
     * null.
     */
    public Class getCommonPropertyType(ELContext context, Object base)
    {
        return (base != null && base.getClass().isArray()) ? Integer.class :
            null;
    }

    private static final int toArrayIndex(Object property)
    {
        if (property instanceof Number)
          {
            return ((Number) property).intValue();
          }
        if (property instanceof Boolean)
          {
            return ((Boolean) property).booleanValue() ? 1 : 0;
          }
        if (property instanceof String)
          {
            return Integer.parseInt((String) property);
          }
        throw new IllegalArgumentException(property.toString());
    }

}
