/*
 * SimpleTag.java -- XXX
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
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Base class for Tag and SimpleTag.
 * 
 * @since JSP2.0
 * @see SimpleTagSupport
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public interface SimpleTag extends JspTag
{

  /**
   * 
   * 
   * @throws JspException 
   * @throws SkipPageException 
   * @throws IOException 
   */
  public void doTag()
    throws JspException, IOException;

  /**
   * 
   * 
   * @param parent set the parent
   */
  public void setParent(JspTag parent);

  /**
   * 
   * 
   * @return the parent of this tag
   */
  public JspTag getParent();

  /**
   * 
   * 
   * @param pageContext
   * @see Tag#setPageContext(javax.servlet.jsp.PageContext)
   */
  public void setJspContext(JspContext pageContext);

  /**
   * 
   * 
   * @param jspBody
   */
  public void setJspBody(JspFragment jspBody);

}
