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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Property resolution on resource bundles.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class ResourceBundleELResolver
    extends ELResolver
{

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
        if (base instanceof ResourceBundle)
          {
            context.setPropertyResolved(true);
            if (property != null)
              {
                ResourceBundle bundle = (ResourceBundle) base;
                return bundle.getObject(property.toString());
              }
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
        if (base instanceof ResourceBundle)
          {
            context.setPropertyResolved(true);
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
        if (base instanceof ResourceBundle)
          {
            context.setPropertyResolved(true);
            Object[] args = new Object[] { base.getClass().getName() };
            String message = formatMessage(context, 
                    "resolverNotWriteable", args);
            throw new PropertyNotWritableException(message);
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
        if (base instanceof ResourceBundle)
          {
            context.setPropertyResolved(true);
          }
        return true;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (base instanceof ResourceBundle)
          {
            ResourceBundle bundle = (ResourceBundle) base;
            List<FeatureDescriptor> acc = new ArrayList<FeatureDescriptor>();
            for (Enumeration<String> e = bundle.getKeys();
                    e.hasMoreElements(); )
              {
                String key = e.nextElement();
                FeatureDescriptor descriptor = new FeatureDescriptor();
                descriptor.setName(key);
                descriptor.setDisplayName(key);
                descriptor.setPreferred(true);
                descriptor.setValue(TYPE, String.class);
                descriptor.setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                acc.add(descriptor);
              }
            return acc.iterator();
          }
        return null;
    }

    /**
     * If the base is a ResourceBundle, returns String.class. Otherwise
     * returns null.
     */
    public Class getCommonPropertyType(ELContext context, Object base)
    {
        return (base instanceof ResourceBundle) ? String.class : null;
    }

}
