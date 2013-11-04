/*
 * Tag.java -- XXX
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
 * 
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public interface Tag extends JspTag
{
  
  /**
   * 
   */
  public static final int EVAL_BODY_INCLUDE = 1;

  /**
   * 
   */
  public static final int EVAL_PAGE = 6;

  /**
   * 
   */
  public static final int SKIP_BODY = 0;


  /**
   * 
   */
  public static final int SKIP_PAGE = 5;

  /**
   * 
   * 
   * @param pageContext
   */
  public void setPageContext(PageContext pageContext);

  /**
   * 
   * 
   * @param tag 
   */
  public void setParent(Tag tag);

  /**
   * 
   * 
   * @return the parent tag 
   * @see TagSupport#findAncestorWithClass(javax.servlet.jsp.tagext.Tag,java.lang.Class)
   */
  public Tag getParent();

  /**
   * 
   * 
   * @return EVAL_BODY_INCLUDE or SKIP_BODY
   * @throws JspException
   * @see BodyTag
   */
  public int doStartTag()
    throws JspException;

  /**
   * 
   * 
   * @return continue the evaluation or not
   * throws JspException
   */
  public int doEndTag()
    throws JspException;

  /**
   * 
   */ 
  public void release();

}
