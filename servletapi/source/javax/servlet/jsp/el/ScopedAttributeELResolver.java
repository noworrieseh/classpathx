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
package javax.servlet.jsp.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

/**
 * Variable resolution for scoped attributes.
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class ScopedAttributeELResolver
    extends ELResolver
{

    /**
     * If the base object is null, returns the value of the specified
     * attribute within the context. Otherwise returns null.
     */
    public Object getValue(ELContext context,
            Object base,
            Object property)
    {
        if (base == null)
          {
            context.setPropertyResolved(true);
            if (property != null)
              {
                PageContext page = (PageContext) context.getContext(JspContext.class);
                return page.findAttribute(property.toString());
              }
          }
        return null;
    }

    /**
     * If the base argument is null, returns Object.class, otherwise returns
     * null.
     */
    public Class getType(ELContext context,
            Object base,
            Object property)
    {
        if (base == null)
          {
            context.setPropertyResolved(true);
            return Object.class;
          }
        return null;
    }

    /**
     * If the base argument is null, sets the specified attribute in the
     * context to the specified value. If the attribute does not yet exist
     * it will be created with page scope.
     */
    public void setValue(ELContext context,
            Object base,
            Object property,
            Object value)
    {
        if (base == null)
          {
            context.setPropertyResolved(true);
            if (property != null)
              {
                PageContext page = (PageContext) context.getContext(JspContext.class);
                String name = property.toString();
                int scope = page.getAttributesScope(name);
                if (scope == 0)
                  {
                    page.setAttribute(name, value);
                  }
                else
                  {
                    page.setAttribute(name, value, scope);
                  }
              }
          }
    }

    /**
     * Scoped attributes are not read only, so returns false.
     */
    public boolean isReadOnly(ELContext context,
            Object base,
            Object property)
    {
        if (base == null)
          {
            context.setPropertyResolved(true);
          }
        return false;
    }

    /**
     * If the base argument is null, returns an iterator of
     * FeatureDescriptors containing information about each scoped attribute
     * in the context. Otherwise return null.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (base == null)
          {
            return null;
          }
        PageContext page = (PageContext) context.getContext(JspContext.class);
        List acc = new ArrayList();
        int[] scopes = new int[]
          {
            PageContext.PAGE_SCOPE,
            PageContext.REQUEST_SCOPE,
            PageContext.SESSION_SCOPE,
            PageContext.APPLICATION_SCOPE
          };
        for (int i = 0; i < scopes.length; i++)
          {
            int scope = scopes[i];
            if (scope == PageContext.SESSION_SCOPE && page.getSession() == null)
              {
                continue;
              }
            for (Enumeration<String> e = page.getAttributeNamesInScope(scope);
                    e.hasMoreElements(); )
              {
                String name = e.nextElement();
                Object value = page.getAttribute(name, scope);
                FeatureDescriptor descriptor = new FeatureDescriptor();
                descriptor.setName(name);
                descriptor.setDisplayName(name);
                descriptor.setPreferred(true);
                descriptor.setValue(ELResolver.TYPE, value.getClass());
                descriptor.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME,
                        false);
              }
          }
        return acc.iterator();
    }

    /**
     * Returns String.class if the base argument is null.
     */
    public Class<String> getCommonPropertyType(ELContext context,
            Object base)
    {
        return base == null ? String.class : null;
    }

}
