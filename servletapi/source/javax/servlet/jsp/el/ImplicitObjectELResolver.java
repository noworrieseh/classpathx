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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

/**
 * Variable resolution for EL implicit objects.
 * The following objects are resolved:
 * <ul>
 * <li>pageContext</li>
 * <li>pageScope</li>
 * <li>requestScope</li>
 * <li>sessionScope</li>
 * <li>applicationScope</li>
 * <li>param</li>
 * <li>paramValues</li>
 * <li>header</li>
 * <li>headerValues</li>
 * <li>cookie</li>
 * <li>initParam</li>
 * </ul>
 * @version 2.1
 * @since 2.1
 * @author Chris Burdess
 */
public class ImplicitObjectELResolver
    extends ELResolver
{

    private static final String[] SCOPES = new String[]
    {
        "applicationScope",
        "cookie",
        "header",
        "headerValues",
        "initParam",
        "pageContext",
        "pageScope",
        "param",
        "paramValues",
        "requestScope",
        "sessionScope"
    };

    public Object getValue(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException,
               javax.el.ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null && property != null)
          {
            int i = Arrays.binarySearch(SCOPES, property.toString());
            if (i >= 0)
              {
                context.setPropertyResolved(true);
                PageContext page =
                    (PageContext) context.getContext(JspContext.class);
                switch (i)
                  {
                    case 0: // "applicationScope"
                        return getApplicationScope(page);
                    case 1: // "cookie"
                        return getCookie(page);
                    case 2: // "header"
                        return getHeader(page);
                    case 3: // "headerValues"
                        return getHeaderValues(page);
                    case 4: // "initParam"
                        return getInitParam(page);
                    case 5: // "pageContext"
                        return page;
                    case 6: // "pageScope"
                        return getPageScope(page);
                    case 7: // "param"
                        return getParam(page);
                    case 8: // "paramValues"
                        return getParamValues(page);
                    case 9: // "requestScope"
                        return getRequestScope(page);
                    case 10: // "sessionScope"
                        return getSessionScope(page);
                 }
              }
          }
        return null;
    }

    private Map getApplicationScope(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.applicationScope";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getServletContext().getAttributeNames();
                }
                protected Object _get(String name)
                {
                    return page.getServletContext().getAttribute(name);
                }
                protected void _set(String name, Object value)
                {
                    page.getServletContext().setAttribute(name, value);
                }
                protected void _del(String name)
                {
                    page.getServletContext().removeAttribute(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getCookie(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.cookie";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    Cookie[] cookies =
                        ((HttpServletRequest) page.getRequest()).getCookies();
                    Vector<String> acc = new Vector<String>();
                    if (cookies != null)
                      {
                        for (int i = 0; i < cookies.length; i++)
                          {
                            acc.add(cookies[i].getName());
                          }
                      }
                    return acc.elements();
                }
                protected Object _get(String name)
                {
                    Cookie[] cookies =
                        ((HttpServletRequest) page.getRequest()).getCookies();
                    if (cookies != null)
                      {
                        for (int i = 0; i < cookies.length; i++)
                          {
                            if (cookies[i].getName().equals(name))
                              {
                                return cookies[i];
                              }
                          }
                      }
                    return null;
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getHeader(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.header";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return ((HttpServletRequest) page.getRequest())
                        .getHeaderNames();
                }
                protected Object _get(String name)
                {
                    return ((HttpServletRequest) page.getRequest())
                        .getHeader(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getHeaderValues(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.headerValues";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return ((HttpServletRequest) page.getRequest())
                        .getHeaderNames();
                }
                protected Object _get(String name)
                {
                    Enumeration<String> e =
                        ((HttpServletRequest) page.getRequest())
                        .getHeaders(name);
                    if (e == null)
                      {
                        return null;
                      }
                    List<String> acc = new ArrayList<String>();
                    while (e.hasMoreElements())
                      {
                        acc.add(e.nextElement());
                      }
                    String[] ret = new String[acc.size()];
                    acc.toArray(ret);
                    return ret;
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getInitParam(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.initParam";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getServletContext()
                        .getInitParameterNames();
                }
                protected Object _get(String name)
                {
                    return page.getServletContext()
                        .getInitParameter(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getPageScope(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.pageScope";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getAttributeNamesInScope(
                            PageContext.PAGE_SCOPE);
                }
                protected Object _get(String name)
                {
                    return page.getAttribute(name);
                }
                protected void _set(String name, Object value)
                {
                    page.setAttribute(name, value);
                }
                protected void _del(String name)
                {
                    page.removeAttribute(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getParam(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.param";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getRequest().getParameterNames();
                }
                protected Object _get(String name)
                {
                    return page.getRequest().getParameter(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getParamValues(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.paramValues";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getRequest().getParameterNames();
                }
                protected Object _get(String name)
                {
                    return page.getRequest().getParameterValues(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getRequestScope(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.requestScope";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    return page.getRequest().getAttributeNames();
                }
                protected Object _get(String name)
                {
                    return page.getRequest().getAttribute(name);
                }
                protected void _set(String name, Object value)
                {
                    page.getRequest().setAttribute(name, value);
                }
                protected void _del(String name)
                {
                    page.getRequest().removeAttribute(name);
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    private Map getSessionScope(final PageContext page)
    {
        String key =
            "javax.servlet.jsp.el.ImplicitObjectELResolver.sessionScope";
        ScopeMap map = (ScopeMap) page.getAttribute(key);
        if (map == null)
          {
            map = new ScopeMap()
            {
                protected Enumeration<String> _dir()
                {
                    HttpSession session = page.getSession();
                    return session == null ? null :
                        session.getAttributeNames();
                }
                protected Object _get(String name)
                {
                    HttpSession session = page.getSession();
                    return session == null ? null :
                        session.getAttribute(name);
                }
                protected void _set(String name, Object value)
                {
                    HttpSession session = page.getSession();
                    if (session != null)
                      {
                        session.setAttribute(name, value);
                      }
                }
                protected void _del(String name)
                {
                    HttpSession session = page.getSession();
                    if (session != null)
                      {
                        session.removeAttribute(name);
                      }
                }
            };
            page.setAttribute(key, map);
          }
        return map;
    }

    public Class getType(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException,
               javax.el.ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null && property != null)
          {
            int i = Arrays.binarySearch(SCOPES, property.toString());
            if (i >= 0)
              {
                context.setPropertyResolved(true);
              }
          }
        return null;
    }

    public void setValue(ELContext context,
            Object base,
            Object property,
            Object value)
        throws NullPointerException, PropertyNotFoundException,
        PropertyNotWritableException, javax.el.ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null && property != null)
          {
            int i = Arrays.binarySearch(SCOPES, property.toString());
            if (i >= 0)
              {
                context.setPropertyResolved(true);
                throw new PropertyNotWritableException();
              }
          }
    }

    public boolean isReadOnly(ELContext context,
            Object base,
            Object property)
        throws NullPointerException, PropertyNotFoundException,
               javax.el.ELException
    {
        if (context == null)
          {
            throw new NullPointerException();
          }
        if (base == null && property != null)
          {
            int i = Arrays.binarySearch(SCOPES, property.toString());
            if (i >= 0)
              {
                context.setPropertyResolved(true);
                return true;
              }
          }
        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
            Object base)
    {
        if (base == null)
          {
            List<FeatureDescriptor> acc =
                new ArrayList<FeatureDescriptor>(SCOPES.length);
            for (int i = 0; i < SCOPES.length; i++)
              {
                String scope = SCOPES[i];
                FeatureDescriptor descriptor = new FeatureDescriptor();
                descriptor.setName(scope);
                descriptor.setDisplayName(scope);
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
     * Returns String.class if the base argument is null.
     */
    public Class<String> getCommonPropertyType(ELContext context,
            Object base)
    {
        return (base == null) ? String.class : null;
    }

    private static abstract class ScopeMap
        extends AbstractMap
    {

        protected abstract Enumeration<String> _dir();

        protected abstract Object _get(String name);

        protected void _set(String name, Object value)
        {
            throw new UnsupportedOperationException();
        }

        protected void _del(String name)
        {
            throw new UnsupportedOperationException();
        }

        public Set<Map.Entry> entrySet()
        {
            Set<Map.Entry> ret = new HashSet<Map.Entry>();
            for (Enumeration<String> e = _dir(); e.hasMoreElements(); )
              {
                ret.add(this.new Entry(e.nextElement()));
              }
            return ret;
        }

        public int size()
        {
            int count = 0;
            for (Enumeration<String> e = _dir(); e.hasMoreElements(); )
              {
                e.nextElement();
                count++;
              }
            return count;
        }
        
        public boolean containsKey(Object key)
        {
            for (Enumeration<String> e = _dir(); e.hasMoreElements(); )
              {
                if (e.nextElement().equals(key))
                  {
                    return true;
                  }
              }
            return false;
        }
        
        public Object get(Object key)
        {
            return _get((String) key);
        }

        public Object put(Object key, Object value)
        {
            Object ret = _get((String) key);
            _set((String) key, value);
            return ret;
        }

        public Object remove(Object key, Object value)
        {
            Object ret = _get((String) key);
            _del((String) key);
            return ret;
        }

        private final class Entry
            implements Map.Entry
        {

            private final String key;

            Entry(String key)
            {
                this.key = key;
            }

            public Object getKey()
            {
                return key;
            }

            public Object getValue()
            {
                return _get(key);
            }

            public Object setValue(Object value)
            {
                Object ret = _get(key);
                if (value == null)
                  {
                    _del(key);
                  }
                else
                  {
                    _set(key, value);
                  }
                return ret;
            }

            public int hashCode()
            {
                return key.hashCode();
            }

            public boolean equals(Object other)
            {
                return (other instanceof Entry) &&
                    ((Entry) other).key.equals(key);
            }

        }
    }

}
