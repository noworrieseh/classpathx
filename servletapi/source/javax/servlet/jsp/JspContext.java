/*
 * JspContext.java -- XXX
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

package javax.servlet.jsp;

import java.util.Enumeration;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Base class for the PageContext.
 * 
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @version JSP 2.0
 * @since JSP 2.0
 */
public abstract class JspContext
{

  public static final int PAGE_SCOPE = 1;

  public static final int REQUEST_SCOPE = 2;

  public static final int SESSION_SCOPE = 3;

  public static final int APPLICATION_SCOPE = 4;

  public JspContext()
  {
  }

  /**
   * 
   * @param name
   * @param attribute
   * @throws NullPointerException
   */
  public abstract void setAttribute(String name, Object attribute);

  /**
   * 
   * @param name
   * @param attribute
   * @param scope
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  public abstract void setAttribute(String name, Object attribute, int scope);

  /**
   * 
   * @param name 
   * @return the object associated with this name.
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  public abstract Object getAttribute(String name);

  /**
   * 
   * @param name
   * @param scope
   * @return the object associated with this name.
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  public abstract Object getAttribute(String name, int scope);

  /**
   * 
   * @param name 
   * @return 
   */
  public abstract Object findAttribute(String name);

  /**
   *
   * @param name
   */
  public abstract void removeAttribute(String name);

  /**
   *
   * @param name 
   * @param scope 
   */
  public abstract void removeAttribute(String name, int scope);

  /**
   * 
   * @param name 
   * @return the scope where the attribute is defined.
   */
  public abstract int getAttributesScope(String name);

  /**
   * 
   * @param scope
   * @return 
   */
  public abstract Enumeration getAttributeNamesInScope(int scope);

  /**
   * 
   * @return 
   */
  public abstract JspWriter getOut();

  /**
   * 
   * @return 
   */
  public abstract ExpressionEvaluator getExpressionEvaluator();

  /**
   * 
   * @return 
   */
  public abstract VariableResolver getVariableResolver();

}
