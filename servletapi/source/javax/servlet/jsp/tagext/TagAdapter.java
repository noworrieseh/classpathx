/*
 * TagAdapter.java -- XXX
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

/**
 * Wraps a SimpleTag and exposes it with a Tag interface.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @since JSP2.0
 */
public class TagAdapter implements Tag
{

  /**
   * 
   */
  private SimpleTag adaptee;
  
  /**
   * Creates a new TagAdapter that wraps the given SimpleTag.
   * 
   * @param adaptee the simple tag adapted as a Tag.
   */
  public TagAdapter(SimpleTag adaptee)
  {
    this.adaptee = adaptee;
  }

  /**
   * Must not be called.
   * 
   * @param pageContext
   * @throws UnsupportedOperationException Must not be called.
   */
  public void setPageContext(PageContext pageContext)
  {
    throw new UnsupportedOperationException("Must not be called.");
  }

  /**
   * Must not be called. The parent is always getAdaptee().getparent().
   * 
   * @param tag 
   * @throws UnsupportedOperationException Must not be called.
   */
  public void setParent(Tag tag)
  {
    throw new UnsupportedOperationException("Must not be called;");
  }

  /**
   * Returns the parent of this Tag. Always getAdaptee().getParent().
   * 
   * @return the parent tag 
   * @see TagSupport#findAncestorWithClass(javax.servlet.jsp.tagext.Tag,java.lang.Class)
   */
  public Tag getParent()
  {
    Tag result = TagSupport.findAncestorWithClass( ((Tag)this.getAdaptee()).getParent(), Tag.class );
    return result;
  }

  /**
   * Gets the tag. This should be an instance of SimpleTag in JSP2.0.
   * 
   * @return the tag that is being adapted.
   */
  public JspTag getAdaptee()
  {
    return this.adaptee;
  }

  /**
   * Must not be called.
   * 
   * @return always throws UnsupportedOperationException.
   * @throws JspException
   * @throws UnsupportedOperationException Must not be called.
   * @see BodyTag
   */
  public int doStartTag()
    throws JspException
  {
    throw new UnsupportedOperationException("Must not be called.");
  }
  
  /**
   * Must not be called.
   * 
   * @return always throws UnsupportedOperationException.
   * @throws JspException
   * @throws UnsupportedOperationException Must not be called.
   */
  public int doEndTag()
    throws JspException
  {
    throw new UnsupportedOperationException("Must not be called.");
  }
  
  /**
   * Must not be called.
   * 
   * @throws UnsupportedOperationException Must not be called.
   */
  public void release()
  {
    throw new UnsupportedOperationException("Must not be called.");
  }
  
}
