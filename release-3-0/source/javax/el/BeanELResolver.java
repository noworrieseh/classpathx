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

import java.beans.*;
import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves variables and properties on JavaBeans.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class BeanELResolver
    extends ELResolver
{

    protected static final class BeanProperties
    {

        private final Map<String,BeanProperty> propertyMap;
        private final Class baseClass;

        public BeanProperties(Class<?> baseClass)
            throws ELException
        {
            this.baseClass = baseClass;
            propertyMap = new HashMap<String,BeanProperty>();
            try
              {
                BeanInfo info = Introspector.getBeanInfo(baseClass);
                PropertyDescriptor[] descriptors =
                    info.getPropertyDescriptors();
                for (int i = 0; i < descriptors.length; i++)
                  {
                    String name = descriptors[i].getName();
                    BeanProperty property =
                        new BeanProperty(baseClass, descriptors[i]);
                    propertyMap.put(name, property);
                  }
              }
            catch (IntrospectionException e)
              {
                throw new ELException(e);
              }
        }
        
        public BeanProperty getBeanProperty(String name)
        {
            BeanProperty property = (BeanProperty) propertyMap.get(name);
            if (property == null)
              {
                Object[] args = new Object[] { baseClass.getName(), name };
                String message = ELResolver.formatMessage(null,
                        "propertyNotFound", args);
                throw new PropertyNotFoundException(message);
              }
            return property;
        }

    }

    protected static final class BeanProperty
    {

        private Class baseClass;
        private PropertyDescriptor descriptor;
        private Method readMethod;
        private Method writeMethod;

        public BeanProperty(Class baseClass, PropertyDescriptor descriptor)
        {
            this.baseClass = baseClass;
            this.descriptor = descriptor;
            readMethod = descriptor.getReadMethod();
            writeMethod = descriptor.getWriteMethod();
        }

        public Class getPropertyType()
        {
            return descriptor.getPropertyType();
        }

        public boolean isReadOnly()
        {
            return (writeMethod == null);
        }

        public Method getReadMethod()
        {
            if (readMethod == null)
              {
                Object[] args = new Object[] {
                    baseClass.getName(), descriptor.getName() };
                String message = ELResolver.formatMessage(null,
                        "propertyNotReadable",
                        args);
                throw new PropertyNotFoundException(message);
              }
            return readMethod;
        }

        public Method getWriteMethod()
        {
            if (writeMethod == null)
              {
                Object[] args = new Object[] {
                    baseClass.getName(), descriptor.getName() };
                String message = ELResolver.formatMessage(null,
                        "propertyNotReadable",
                        args);
                throw new PropertyNotFoundException(message);
              }
            return writeMethod;
        }
    
    }

    private boolean readOnly;
    private Map<Class,BeanProperties> typeProperties;

    /**
     * New read/write bean resolver.
     */
    public BeanELResolver()
    {
        this(false);
    }

    /**
     * New bean resolver with the specified read-only property.
     */
    public BeanELResolver(boolean readOnly)
    {
        this.readOnly = readOnly;
        typeProperties = new HashMap<Class,BeanProperties>();
    }

    private BeanProperties getBeanProperties(Class type)
    {
        BeanProperties properties = typeProperties.get(type);
        if (properties == null)
          {
            properties = new BeanProperties(type);
            typeProperties.put(type, properties);
          }
        return properties;
    }

    public Object getValue(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null || property == null)
          {
            return null;
          }
        context.setPropertyResolved(true);
        BeanProperties properties = getBeanProperties(base.getClass());
        BeanProperty p = properties.getBeanProperty(property.toString());
        Method method = p.getReadMethod();
        try
          {
            return method.invoke(base, (Object[]) null);
          }
        catch (Exception e)
          {
            throw new ELException(e);
          }
    }

    public Class getType(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null || property == null)
          {
            return null;
          }
        context.setPropertyResolved(true);
        BeanProperties properties = getBeanProperties(base.getClass());
        BeanProperty p = properties.getBeanProperty(property.toString());
        return p.getPropertyType();
    }

    public void setValue(ELContext context, Object base, Object property, Object value)
        throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null || property == null)
          {
            return;
          }
        context.setPropertyResolved(true);
        if (readOnly)
          {
            Object[] args = new Object[] { base.getClass().getName() };
            String message = ELResolver.formatMessage(null,
                    "resolverNotWriteable",
                    args);
            throw new PropertyNotWritableException(message);
          }
        BeanProperties properties = getBeanProperties(base.getClass());
        BeanProperty p = properties.getBeanProperty(property.toString());
        Method method = p.getWriteMethod();
        Object[] args = new Object[] { value };
        try
          {
            method.invoke(base, args);
          }
        catch (Exception e)
          {
            throw new ELException(e);
          }
    }

    public boolean isReadOnly(ELContext context, Object base, Object property)
        throws NullPointerException, PropertyNotFoundException, ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null || property == null)
          {
            return false;
          }
        context.setPropertyResolved(true);
        if (readOnly)
        {
            return true;
        }
        BeanProperties properties = getBeanProperties(base.getClass());
        BeanProperty p = properties.getBeanProperty(property.toString());
        return p.isReadOnly();
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null)
          {
            return null;
          }
        try
          {
            List<FeatureDescriptor> acc = new ArrayList<FeatureDescriptor>();
            BeanInfo info = Introspector.getBeanInfo(base.getClass());
            PropertyDescriptor[] d = info.getPropertyDescriptors();
            for (int i = 0; i < d.length; i++)
            {
                d[i].setValue(RESOLVABLE_AT_DESIGN_TIME, Boolean.TRUE);
                d[i].setValue(TYPE, d[i].getPropertyType());
                acc.add(d[i]);
            }
            return acc.iterator();
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    public Class getCommonPropertyType(ELContext context, Object base)
    {
        return (base != null) ? Object.class : null;
    }

}
