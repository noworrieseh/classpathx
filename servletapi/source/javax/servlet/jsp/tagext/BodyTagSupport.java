/*
 * BodyTagSupport.java -- XXX
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

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;

/**
 * Implementation of the BodyTag interface.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public class BodyTagSupport extends TagSupport implements BodyTag
{
  
  /**
   * Current bodyContent.
   */
  protected BodyContent bodyContent;

  /**
   * The parent
   */
  private Tag parent;

  /**
   * Default constructor.
   */
  public BodyTagSupport()
  {
  }

  /**
   * Default processing of the start tag
   * 
   * @return EVAL_BODY_BUFFERED
   * @throws javax.servlet.jsp.JspException
   * @see Tag#doStartTag()
   */
  public int doStartTag()
    throws JspException
  {
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Default processing of the end tag
   * 
   * @return EVAL_PAGE
   * @throws javax.servlet.jsp.JspException
   * @see Tag#doEndTag()
   */
  public int doEndTag()
    throws JspException
  {
    return EVAL_PAGE;
  }

  /**
   * Prepare for evaluation of the body.
   * 
   * @param bodyContent the body content
   * @see #doInitBody()
   * @see #doAfterBody()
   * @see BodyTag#setBodyContent(javax.servlet.jsp.tagext.BodyContent)
   */
  public void setBodyContent(BodyContent bodyContent)
  {
    this.bodyContent = bodyContent;
  }

  /**
   * Prepare for the evaluation of the body.
   *
   * @throws JspException
   * @see #setBodyContent(javax.servlet.jsp.tagext.BodyContent)
   * @see #doAfterBody()
   * @see BodyTag#doInitBody()
   */
  public void doInitBody()
    throws JspException
  {
  }

  /**
   *
   * @return SKIP_BODY
   * @throws JspException if there is an error while processing this tag.
   * @see #doInitBody()
   * @see IterationTag#doAfterBody()
   */
  public int doAfterBody()
    throws JspException
  {
    return SKIP_BODY;
  }

  /**
   *
   * @see Tag#release()
   */
  public void release()
  {
  }

  /**
   * @return the body content.
   */
  public BodyContent getBodyContent()
  {
    return this.bodyContent;
  }

  /**
   * @return the enclosing JspWriter, from the bodyContent
   */
  public JspWriter getPreviousOut()
  {
    return bodyContent.getEnclosingWriter();
  }

  /**
   * @param parent the parent tag
   */
  public void setParent(Tag parent)
  {
    this.parent = parent;
  }

  /**
   * @return the parent tag
   */
  public Tag getParent()
  {
    return this.parent;
  }

}
