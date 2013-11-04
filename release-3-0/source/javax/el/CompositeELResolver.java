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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.jsp.el.ScopedAttributeELResolver;

/**
 * An ordered composite list of resolvers.
 * @version 1.2
 * @since 1.2
 * @author Chris Burdess
 */
public class CompositeELResolver extends ELResolver
{

    private static final class CompositeIterator
        implements Iterator
    {

        private final Iterator<ELResolver> resolverIterator;
        private final ELContext context;
        private final Object base;
        private Iterator<FeatureDescriptor> descriptorIterator;
        private FeatureDescriptor next;

        CompositeIterator(Iterator<ELResolver> resolverIterator,
                ELContext context,
                Object base)
        {
            this.resolverIterator = resolverIterator;
            this.context = context;
            this.base = base;
        }
        
        public boolean hasNext()
        {
            if (next != null)
              {
                return true;
              }
            if (descriptorIterator != null && !descriptorIterator.hasNext())
              {
                descriptorIterator = null;
              }
            while (descriptorIterator == null)
              {
                if (!resolverIterator.hasNext())
                  {
                    return false;
                  }
                ELResolver resolver = resolverIterator.next();
                descriptorIterator =
                    resolver.getFeatureDescriptors(context, base);
                if (!descriptorIterator.hasNext())
                  {
                    descriptorIterator = null;
                  }
              }
            return true;
        }

        public Object next()
        {
            if (!hasNext())
              {
                throw new NoSuchElementException();
              }
            FeatureDescriptor ret = next;
            next = null;
            return ret;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

    }

    private List<ELResolver> resolvers = new ArrayList<ELResolver>();

    /**
     * Adds the specified resolver to the list.
     */
    public void add(ELResolver elResolver)
    {
        if (elResolver == null)
        {
            throw new NullPointerException();
        }
        synchronized (resolvers)
          {
            resolvers.add(elResolver);
          }
    }

    public Object getValue(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        context.setPropertyResolved(false);
        synchronized (resolvers)
          {
            int len = resolvers.size();
            for (int i = 0; i < len; i++)
              {
                ELResolver resolver = resolvers.get(i);
                Object value = resolver.getValue(context, base, property);
                if (context.isPropertyResolved())
                  {
                    return value;
                  }
              }
          }
        return null;
    }

    public void setValue(ELContext context, Object base,
            Object property, Object value)
        throws NullPointerException, PropertyNotFoundException,
               PropertyNotWritableException, ELException
    {
        context.setPropertyResolved(false);
        synchronized (resolvers)
          {
            int len = resolvers.size();
            for (int i = 0; i < len; i++)
              {
                ELResolver resolver = resolvers.get(i);
                resolver.setValue(context, base, property, value);
                if (context.isPropertyResolved())
                  {
                    return;
                  }
              }
          }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        context.setPropertyResolved(false);
        synchronized (resolvers)
          {
            int len = resolvers.size();
            for (int i = 0; i < len; i++)
              {
                ELResolver resolver = resolvers.get(i);
                boolean readOnly = resolver.isReadOnly(context, base, property);
                if (context.isPropertyResolved())
                  {
                    return readOnly;
                  }
              }
          }
        return false;
    }

    public Class getType(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        context.setPropertyResolved(false);
        synchronized (resolvers)
          {
            int len = resolvers.size();
            for (int i = 0; i < len; i++)
              {
                ELResolver resolver = resolvers.get(i);
                Class type = resolver.getType(context, base, property);
                if (context.isPropertyResolved())
                  {
                    if (ScopedAttributeELResolver.class
                            .isAssignableFrom(resolver.getClass()))
                      {
                        Object value =
                            resolver.getValue(context, base, property);
                        if (value != null)
                          {
                            return value.getClass();
                          }
                      }
                    return type;
                  }
              }
          }
        return null;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base)
    {
        return new CompositeIterator(resolvers.iterator(), context, base);
    }

    public Class getCommonPropertyType(ELContext context, Object base)
    {
        Class commonType = null;
        synchronized (resolvers)
          {
            int len = resolvers.size();
            for (int i = 0; i < len; i++)
              {
                ELResolver resolver = resolvers.get(i);
                Class type = resolver.getCommonPropertyType(context, base);
                if (type != null &&
                        (commonType == null ||
                         commonType.isAssignableFrom(type)))
                  {
                    commonType = type;
                  }
              }
          }
        return commonType;
    }

}
