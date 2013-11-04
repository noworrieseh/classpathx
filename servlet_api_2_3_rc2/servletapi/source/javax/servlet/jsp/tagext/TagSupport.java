/*
 * TagSupport.java -- XXX
 * 
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck (arnaud.vandyck@ulg.ac.be)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation  
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp.tagext;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
//import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.Tag;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * A base class for defining new tag handlers implementing Tag.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class TagSupport implements IterationTag, Serializable /*, JspTag, Tag*/
{
  
  protected String id;
  protected PageContext pageContext;
  protected Tag parent;
  private Hashtable map;

  /**
   * Default constructor.
   */
  public TagSupport()
  {
    this.map = new Hashtable();
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
    if (from!=null)
    {
      from = from.getParent();
      if (from!=null)
      {
        boolean isAncestor = from.getClass().isAssignableFrom( klass );
        if (!isAncestor)
        {
          from=findAncestorWithClass(from,klass);
        }
      }
    }
    return from;
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

  }

  /**
   * Set the parent tag.
   * 
   * @param t the parent tag
   */
  public void setParent(Tag t)
  {
    this.parent = t;
  }

  /**
   * Get the parent tag.
   * @return the parent tag
   */
  public Tag getParent()
  {
    return this.parent;
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
    return this.id;
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
   * Set a value bind to a key
   * 
   * @param key the key
   * @param object
   */
  public void setValue(String key,
                       Object object)
  {
    if (key!=null && object!=null)
    {
      this.map.put( key, object );
    }
  }

  /**
   * Get the value
   * @param k the key
   * @return the value
   */
  public Object getValue(String k)
  {
    Object result = null;
    if (k!=null)
    {
      result = this.map.get(k);
    }
    return result;
  }
  
  /**
   * Remove the value of this key.
   * 
   * @param k the key
   */
  public void removeValue(String k)
  {
    if (k!=null)
    {
      this.map.remove(k);
    }
  }
  
  /**
   * Enumerate all the values. Why do they use Enumeration and not Iteration?
   * 
   * @return all the values.
   */
  public Enumeration getValues()
  {
    return this.map.elements();
  }

}
