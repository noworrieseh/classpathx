/*
 * JspEngineInfo.java.
 * 
 * Copyright (c) 2002 by Free Software Foundation, Inc.
 * Written by Nic Ferrier <nferrier@tapsellferrier.co.uk>
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


/**
 * Provide information about a JSP engine.
 *
 * @author Nic Ferrier <nferrier@tapsellferrier.co.uk>
 */
public abstract class JspEngineInfo
{

  /** The default constructor does nothing.
   */
  public JspEngineInfo ()
  {
  }


  /** Get the specification version.
   */
  public abstract String getSpecificationVersion ();
}
