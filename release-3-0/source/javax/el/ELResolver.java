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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Resolves variables and properties in EL expressions.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public abstract class ELResolver
{

    /**
     * FeatureDescriptor property indicating whether the variable or
     * property can be resolved at compile time.
     */
    public static final String RESOLVABLE_AT_DESIGN_TIME =
        "resolvableAtDesignTime";
    
    /**
     * FeatureDescriptor property indicating the class of the variable or
     * property.
     */
    public static final String TYPE = "type";

    private static final Map<ELContext,ResourceBundle> contextBundles =
        new HashMap<ELContext,ResourceBundle>();

    /*
     * Convenience method for subclasses in this package.
     */
    static String formatMessage(ELContext context, String name, Object[] args)
    {
        ResourceBundle bundle = contextBundles.get(context);
        if (bundle == null)
          {
            Locale locale = (context == null) ? null : context.getLocale();
            if (locale == null)
              {
                locale = Locale.getDefault();
              }
            bundle = ResourceBundle.getBundle("javax.el.L10N", locale);
            contextBundles.put(context, bundle);
          }
        String message = bundle.getString(name);
        if (args != null)
          {
            message = MessageFormat.format(message, args);
          }
        return message;
    }

    /**
     * Resolves the specified property of the base object.
     * @param context the evaluation context
     * @param base the target of the operation
     * @param property the property of the base object
     */
    public abstract Object getValue(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException, ELException;

    /**
     * Returns the class of the specified property of the base object.
     * @param context the evaluation context
     * @param base the target of the operation
     * @param property the property of the base object
     */
    public abstract Class getType(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException, ELException;

    /**
     * Sets the value of the specified property of the base object.
     * @param context the evaluation context
     * @param base the target of the operation
     * @param property the property of the base object
     * @param value the new value of the property
     * @throws PropertyNotWritableException if the property is read-only
     */
    public abstract void setValue(ELContext context,
            Object base,
            Object property,
            Object value)
        throws NullPointerException, PropertyNotFoundException,
               PropertyNotWritableException, ELException;

    /**
     * Indicates whether the specified property of the base object is
     * read-only.
     * @param context the evaluation context
     * @param base the target of the operation
     * @param property the property of the base object
     */
    public abstract boolean isReadOnly(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException, ELException;

    /**
     * Returns metadata about the variables or properties within the
     * specified base object.
     * @param context the evaluation context
     * @param base the target of the operation
     */
    public abstract Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base);

    /**
     * Returns the most general class of properties in the base object.
     * @param context the evaluation context
     * @param base the target of the operation
     */
    public abstract Class getCommonPropertyType(ELContext context,
            Object base);

}
