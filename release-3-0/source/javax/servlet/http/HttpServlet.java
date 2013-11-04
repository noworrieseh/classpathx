/*
 * Copyright (C) 1998, 1999, 2001, 2013 Free Software Foundation, Inc.
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
package javax.servlet.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The mother-of-all-HttpServlets.
 * Every normal http servlet extends this class and overrides either the doGet
 * or doPost methods (or both).
 * The server calls service. Service in its turn calls doGet, doPost, whatever,
 * depending on the client's request.
 *
 * @version 3.0
 * @since 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 * @author Chris Burdess
 */
public abstract class HttpServlet
    extends GenericServlet
    implements Serializable 
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.http.L10N");
    private static final Set HTTP_METHODS;
    static
    {
        Set acc = new TreeSet();
        acc.add("GET");
        acc.add("HEAD");
        acc.add("POST");
        acc.add("PUT");
        acc.add("DELETE");
        acc.add("TRACE");
        acc.add("OPTIONS");
        HTTP_METHODS = Collections.unmodifiableSet(acc);
    }

    /**
     * Does nothing
     */
    public HttpServlet() 
    {
    }

    /**
     * This method is called on a "GET" request.
     * The default implementation is that for HTTP/1.0 requests we return a
     * HttpServletResponse.SC_BAD_REQUEST status code, and for HTTP/1.1
     * requests, HttpServletResponse.SC_METHOD_NOT_ALLOWED.
     *
     * @exception ServletException if an Servlet Exception occurs
     * @exception IOException if an IOException occurs
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        String protocol = request.getProtocol();
        String message = L10N.getString("http.method_get_not_supported");
        int sc = protocol.endsWith("1.1") ?
            HttpServletResponse.SC_METHOD_NOT_ALLOWED :
            HttpServletResponse.SC_BAD_REQUEST;
        response.sendError(sc, message);
    }

    /**
     * Returns the time the requested uri was last modified in seconds since
     * 1 january 1970. Default implementation returns -1.
     */
    protected long getLastModified(HttpServletRequest request) 
    {
        return -1L;
    }

    /**
     * This method is called on a "HEAD" request.
     * This method calls the doGet method with the request parameter,
     * but the response object is replaced by a HttpServletResponse that
     * is identical to the reponse, but it has a "dummy" OutputStream.<BR>
     * The doGet method should perform a check like the following:<BR>
     * <CODE>if(request.getMethod().equals("HEAD")) { ... }</CODE><BR>
     * if it wants to know whether it was called from doHead.
     *
     * @since 2.0
     *
     * @exception ServletException if an Servlet Exception occurs
     * @exception IOException if an IOException occurs
     */
    protected void doHead(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        EmptyServletResponse r = new EmptyServletResponse(response);
        doGet(request, r);
        r.updateContentLength();
    }

    /**
     * This method is called on a "POST" request.
     * The default implementation is that for HTTP/1.0 requests we return a
     * HttpServletResponse.SC_BAD_REQUEST status code, and for HTTP/1.1
     * requests, HttpServletResponse.SC_METHOD_NOT_ALLOWED.
     *
     * @throws ServletException if an Servlet Exception occurs
     * @throws IOException if an IOException occurs
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        String protocol = request.getProtocol();
        String message = L10N.getString("http.method_post_not_supported");
        int sc = protocol.endsWith("1.1") ?
            HttpServletResponse.SC_METHOD_NOT_ALLOWED :
            HttpServletResponse.SC_BAD_REQUEST;
        response.sendError(sc, message);
    }

    /**
     * This method is called on a "PUT" request.
     * The default implementation is that for HTTP/1.0 requests we return a
     * HttpServletResponse.SC_BAD_REQUEST status code, and for HTTP/1.1
     * requests, HttpServletResponse.SC_METHOD_NOT_ALLOWED.
     *
     * @since 2.0
     *
     * @exception ServletException if an Servlet Exception occurs
     * @exception IOException if an IOException occurs
     */
    protected void doPut(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        String protocol = request.getProtocol();
        String message = L10N.getString("http.method_put_not_supported");
        int sc = protocol.endsWith("1.1") ?
            HttpServletResponse.SC_METHOD_NOT_ALLOWED :
            HttpServletResponse.SC_BAD_REQUEST;
        response.sendError(sc, message);
    }

    /**
     * This method is called on a "DELETE" request.
     * The default implementation is that for HTTP/1.0 requests we return a
     * HttpServletResponse.SC_BAD_REQUEST status code, and for HTTP/1.1
     * requests, HttpServletResponse.SC_METHOD_NOT_ALLOWED.
     *
     * @since 2.0
     *
     * @exception ServletException if an Servlet Exception occurs
     * @exception IOException if an IOException occurs
     */
    protected void doDelete(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        String protocol = request.getProtocol();
        String message = L10N.getString("http.method_delete_not_supported");
        int sc = protocol.endsWith("1.1") ?
            HttpServletResponse.SC_METHOD_NOT_ALLOWED :
            HttpServletResponse.SC_BAD_REQUEST;
        response.sendError(sc, message);
    }

    private static void addHttpMethods(Set acc, Class t)
    {
        if (HttpServlet.class.equals(t))
          {
            return;
          }
        addHttpMethods(acc, t.getSuperclass());
        Method[] methods = t.getDeclaredMethods();
        if (methods != null)
          {
            for (int i = 0; i < methods.length; i++)
              {
                String name = methods[i].getName();
                if (name.startsWith("do"))
                  {
                    name = name.substring(2).toUpperCase();
                    if (HTTP_METHODS.contains(name))
                      {
                        acc.add(name);
                      }
                  }
              }
          }
    }

    /**
     * This method is called on a "OPTIONS" request.
     * It tells the client which methods are supported by the servlet.
     * This comes down to an implementation where TRACE and OPTIONS
     * are supported by default. GET, HEAD, POST, DELETE and PUT are added
     * to this list if the subclass has its own implementation of
     * the doGet, doHead, doPost, doDelete or doPut method respectively.
     *
     * @since 2.0
     *
     * @throws ServletException if an Servlet Exception occurs
     * @throws IOException if an IOException occurs
     */
    protected void doOptions(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        Set methods = new HashSet();
        addHttpMethods(methods, getClass());
        methods.add("TRACE");
        methods.add("OPTIONS");
        StringBuilder buf = new StringBuilder();
        for (Iterator i = methods.iterator(); i.hasNext(); )
        {
            String name = (String) i.next();
            if (buf.length() > 0)
            {
                buf.append(", ");
            }
            buf.append(name);
        }
        response.setHeader("Allow", buf.toString());
    }

    /**
     * This method is called on a "TRACE" request.
     * This method is for debugging purposes.<BR>
     * When a client makes a TRACE request the following is returned:<BR>
     * content type = "message/http"<BR>
     * message size: &lt;size of the complete message&gt;<BR>
     * first line of the message : TRACE &lt;requested uri&gt;
     * &lt;protocol&gt;<BR>
     * on the following lines all the request header names and values
     *
     * @since 2.0
     *
     * @exception ServletException if an Servlet Exception occurs
     * @exception IOException if an IOException occurs
     */
    protected void doTrace(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        StringBuilder result = new StringBuilder();
        result.append("TRACE ");
        result.append(request.getRequestURI());
        result.append(" ");
        result.append(request.getProtocol());
        result.append("\r\n");
        String headerName;
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) 
        {
            headerName = (String) e.nextElement();
            result.append(headerName);
            result.append(": ");
            result.append(request.getHeader(headerName));
            result.append("\r\n");
        }
        response.setContentType("message/http");
        response.setContentLength(result.length());
        ServletOutputStream out = response.getOutputStream();
        out.print(result.toString());
    }

    /**
     * This method looks whether the request is a POST, GET, etc method,
     * and then calls the appropriate doPost, doGet, whatever method.<BR>
     * If the request method is something it can't handle it sends
     * a HttpServletResponse.SC_BAD_REQUEST error through the response.
     *
     * @exception ServletException an error has occured
     * @exception IOException an error has occured
     */
    protected void service(HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException 
    {
        String method = request.getMethod();
        if (method.equals("GET") ) 
          {
            if (checkIfModifiedSince(request, response)) 
              {
                doGet(request, response);
              }
            else
              {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
              }
          }
        else if (method.equals("HEAD") ) 
          {
            long lastModified = getLastModified(request);
            if (!response.containsHeader("Last-Modified"))
              {
                response.setDateHeader("Last-Modified", lastModified);
              }
            doHead(request, response);
          }
        else if (method.equals("POST") ) 
          {
            doPost(request, response);
          }
        else if (method.equals("PUT") ) 
          {
            doPut(request, response);
          }
        else if (method.equals("DELETE") ) 
          {
            doDelete(request, response);
          }
        else if (method.equals("OPTIONS") ) 
          {
            doOptions(request, response);
          }
        else if (method.equals("TRACE") ) 
          {
            doTrace(request, response);
          }
        else 
          {
            String message = L10N.getString("http.method_not_implemented");
            Object[] args = new Object[] { method };
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    MessageFormat.format(message, args));
          }
    }

    /**
     * Frontend for calling service(HttpServletRequest,HttpServletResponse).
     * <P>
     * This method tries to typecast the ServletRequest and the
     * ServletResponse to HttpServletRequest and HttpServletResponse
     * and then call service(HttpServletRequest,HttpServletResponse).<BR>
     *
     * @param request The client's request
     * @param response The class to write the response date to.
     * @exception ServletException an error has occured
     * @exception IOException an error has occured
     */
    public void service(ServletRequest request,ServletResponse response)
        throws ServletException, IOException 
    {
        if ((request instanceof HttpServletRequest) &&
                (response instanceof HttpServletResponse)) 
          {
            service((HttpServletRequest) request, (HttpServletResponse) response);
          }
        else 
          {
            throw new ServletException("This servlet only expects http requests");
          }
    }

    /**
     * Check for possible "If-Modified-Since" header parameters.
     * Handles setting the "Last-Modified" header field if necessary.
     *
     * @return true if doGet is to be called, false if 304
     */
    private boolean checkIfModifiedSince(HttpServletRequest request,
            HttpServletResponse response)
        throws IOException 
    {
        long lastModified = getLastModified(request);
        if (lastModified < 0)
          {
            return true;
          }
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince >= lastModified) 
          {
            return false;
          }
        if (!response.containsHeader("Last-Modified"))
          {
            response.setDateHeader("Last-Modified", lastModified);
          }
        return true;
    }

    /**
     * This class is built for use in the doHead method of HttpServlet.
     * It enables the doHead method to have the doGet method do the work of
     * a HEAD method without him noticing it.
     */
    private static class EmptyServletResponse
        extends HttpServletResponseWrapper
    {

        private static final int NONE = 0;
        private static final int OUTPUTSTREAM = 1;
        private static final int WRITER = 2;

        private int state = NONE;
        private EmptyOutputStream out;
        private PrintWriter writer;

        EmptyServletResponse(HttpServletResponse response) 
        {
            super(response);
        }

        void updateContentLength() {
            if (writer != null)
              {
                writer.flush();
              }
            if (out != null)
              {
                setContentLength(out.contentLength);
              }
        }

        /**
         * Returns a ServletOutputStream that suppresses any data written to
         * it.
         */
        public ServletOutputStream getOutputStream()
            throws IOException 
        {
            if (state == WRITER)
              {
                throw new IllegalStateException(L10N.getString("err.ise.getOutputStream"));
              }
            if (out == null)
              {
                out = new EmptyOutputStream();
                state = OUTPUTSTREAM;
              }
            return out;
        }

        /**
         * Returns a PrintWriter that suppresses any data written to it.
         */
        public PrintWriter getWriter()
            throws IOException 
        {
            if (state == OUTPUTSTREAM)
              {
                throw new IllegalStateException(L10N.getString("err.ise.getWriter"));
              }
            if (writer == null)
              {
                writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
                state = WRITER;
              }
            return writer;
        }

    }

    private static class EmptyOutputStream
        extends ServletOutputStream
    {

        int contentLength;

        public void write(int c)
            throws IOException 
        {
            contentLength++;
        }

        public void write(byte[] buffer, int offset, int length)
            throws IOException 
        {
            if (length < 0)
              {
                throw new IOException(L10N.getString("err.io.negativelength"));
              }
            contentLength += length;
        }

    }

}
