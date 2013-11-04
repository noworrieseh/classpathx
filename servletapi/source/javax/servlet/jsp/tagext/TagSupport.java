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

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;

/**
 * A base class for defining new tag handlers implementing Tag.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
 */
public class TagSupport
    implements IterationTag, Serializable
{

    protected String id;
    protected PageContext pageContext;
    private Tag parent;
    private Hashtable<String, Object> values;

    /**
     * Default constructor.
     */
    public TagSupport()
    {
    }

    /**
     * Find ancestor.
     * @param from
     * @param klass
     * @return the ancestor tag
     * @see SimpleTagSupport#findAncestorWithClass(JspTag,Class)
     */
    public static final Tag findAncestorWithClass(Tag from,
            Class klass)
    {
        if (from != null)
          {
            do
              {
                from = from.getParent();
                if (from == null)
                  {
                    return null;
                  }
                if (klass.isAssignableFrom(from.getClass()))
                  {
                    return from;
                  }
              }
            while (from != null);
        }
        return null;
    }

    /**
     * Default processing.
     * 
     * @return SKIP_BODY
     */
    public int doStartTag()
        throws JspException
    {
        return SKIP_BODY;
    }

    /**
     * Default processing
     * 
     * @return EVAL_PAGE
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * Default processing.
     * 
     * @return SKIP_BODY
     */
    public int doAfterBody()
        throws JspException
    {
        return SKIP_BODY;
    }

    /**
     * @see Tag#release
     */
    public void release()
    {
        parent = null;
        id = null;
        values = null;
    }

    /**
     * Set the parent tag.
     * 
     * @param t the parent tag
     */
    public void setParent(Tag t)
    {
        parent = t;
    }

    /**
     * Get the parent tag.
     * @return the parent tag
     */
    public Tag getParent()
    {
        return parent;
    }

    /**
     * Set the id
     * 
     * @param id the id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Get the id
     * 
     * @return the id
     */ 
    public String getId()
    {
        return id;
    }

    /**
     * Set the page context.
     * 
     * @param pageContext
     */
    public void setPageContext(PageContext pageContext)
    {
        this.pageContext = pageContext;
    }

    /**
     * Get the value
     * @param k the key
     * @return the value
     */
    public Object getValue(String k)
    {
        if (values != null)
          {
            return values.get(k);
          }
        return null;
    }

    /**
     * Set a value bind to a key
     * 
     * @param key the key
     * @param object
     */
    public void setValue(String key, Object object)
    {
        if (values == null)
          {
            values = new Hashtable<String,Object>();
          }
        values.put(key, object);
    }

    /**
     * Remove the value of this key.
     * 
     * @param k the key
     */
    public void removeValue(String k)
    {
        if (values != null)
          {
            values.remove(k);
          }
    }

    /**
     * Enumerate all the values. Why do they use Enumeration and not Iteration?
     * 
     * @return all the values.
     */
    public Enumeration getValues()
    {
        return values == null ? null : values.elements();
    }

}
