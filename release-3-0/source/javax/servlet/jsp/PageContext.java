/*
 * Copyright (C) 1999, 2013 Free Software Foundation, Inc.
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
package javax.servlet.jsp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * The definition of a page.
 * @version 2.1
 * @author Mark Wielaard
 * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
 * @author Chris Burdess
 */
public abstract class PageContext 
    extends JspContext
{

    /**
     * Denotes page scope.
     */
    public static final int PAGE_SCOPE = 1;

    /**
     * Denotes request scope.
     */
    public static final int REQUEST_SCOPE = 2;

    /**
     * Denotes session scope.
     */
    public static final int SESSION_SCOPE = 3;

    /**
     * Denotes application scope.
     */
    public static final int APPLICATION_SCOPE = 4;

    /**
     * Key for the servlet in the name table.
     */
    public static final String PAGE = "javax.servlet.jsp.jspPage";

    /**
     * Key for the page context itself in the name table.
     */
    public static final String PAGECONTEXT = "javax.servlet.jsp.jspPageContext";

    /**
     * Key for the servlet request in the name table.
     */
    public static final String REQUEST = "javax.servlet.jsp.jspRequest";

    /**
     * Key for the servlet response in the name table.
     */
    public static final String RESPONSE = "javax.servlet.jsp.jspResponse";

    /**
     * Key for the ServletConfig in the name table.
     */
    public static final String CONFIG = "javax.servlet.jsp.jspConfig";

    /**
     * Key for the HttpSession in the name table.
     */
    public static final String SESSION = "javax.servlet.jsp.jspSession";

    /**
     * Key for the current JspWriter in the name table.
     */
    public static final String OUT = "javax.servlet.jsp.jspOut";

    /**
     * Key for the servlet context in the name table.
     */
    public static final String APPLICATION = "javax.servlet.jsp.jspApplication";

    /**
     * Key for an uncaught exception in the name table.
     */
    public static final String EXCEPTION = "javax.servlet.jsp.jspException";

    /**
     * Initialize the JSP page context.
     */
    public abstract void initialize(Servlet servlet,
            ServletRequest request,
            ServletResponse response,
            String errorPageURL,
            boolean needsSession,
            int bufferSize,
            boolean autoFlush)
        throws IOException, IllegalStateException, IllegalArgumentException;

    /**
     * Release this page context.
     */
    public abstract void release();

    /**
     * Returns the current session object.
     */
    public abstract HttpSession getSession();

    /**
     * Returns the current page (a servlet).
     */
    public abstract Object getPage();

    /**
     * Returns the current servlet request object
     */
    public abstract ServletRequest getRequest();

    /**
     * Returns the current servlet response object
     */
    public abstract ServletResponse getResponse();

    /**
     * Returns the current unhandled exception.
     */
    public abstract Exception getException();

    /**
     * Returns the ServletConfig associated with the page.
     */
    public abstract ServletConfig getServletConfig();

    /**
     * Returns the servlet context.
     */
    public abstract ServletContext getServletContext();

    /**
     * Forwards the current request/response pair to another servlet for
     * all further processing.
     * This page will not subsequently modify the response.
     * @param relativeUrlPath the URL path to the target resource relative
     * to this page
     */
    public abstract void forward(String relativeUrlPath)
        throws IOException, ServletException;

    /**
     * Process the request/response pair using another resource.
     * This page may subsequently modify the response.
     * @param relativeUrlPath the URL path to the target resource relative
     * to this page
     */
    public abstract void include(String urlPath)
        throws IOException, ServletException;

    /**
     * Process the request/response pair using another resource.
     * This page may subsequently modify the response.
     * If <code>flush</code> is true, the current <code>out</code> for this
     * page is flushed prior to include processing.
     * @param relativeUrlPath the URL path to the target resource relative
     * to this page
     * @since 2.0
     */
    public abstract void include(String urlPath, boolean flush)
        throws IOException, ServletException;

    /**
     * Handle an exception created by the page.
     */
    public abstract void handlePageException(Exception e)
        throws ServletException, IOException;

    /**
     * Handle an exception created by the page.
     */
    public abstract void handlePageException(Throwable t)
        throws ServletException, IOException;

    /**
     * Calls {@link javax.servlet.jsp.JspContext#pushBody} with the current
     * value of <code>out</code>, and returns a new BodyContent.
     */
    public BodyContent pushBody()
    {
        JspWriter out = (JspWriter) getAttribute(OUT);
        pushBody(out);
        return (BodyContent) out; // FIXME work out what to return
    }

    /**
     * Returns error data from the request attributes.
     * @since 2.0
     */
    public ErrorData getErrorData()
    {
        return null;
    }

}
