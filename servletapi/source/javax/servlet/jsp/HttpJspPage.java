/*
 * HttpJspPage.java -- XXX
 * 
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 * Written by Mark Wielaard (mark@klomp.org)
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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 * An HTTP contextual enironment for a JSP.
 *
 * @author Mark Wielaard <mark@klomp.org>
 */
public interface HttpJspPage extends JspPage {

  /**
   * Provide a service for a page.
   *
   * @param req the request.
   * @param res the response.
   * @exception ServletException thrown if there's some error.
   * @exception IOException thrown if there's an io problem.
   */
  void _jspService(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException;
}
