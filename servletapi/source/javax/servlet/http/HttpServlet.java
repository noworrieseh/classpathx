/*
  GNU-Classpath Extensions: Servlet API
  Copyright (C) 1998, 1999, 2001   Free Software Foundation, Inc.

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package javax.servlet.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;

import java.util.Locale;

import javax.servlet.GenericServlet;
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
 * @version Servlet API 2.2
 * @since Servlet API 1.0
 * @author Paul Siegmann (pauls@euronet.nl)
 * @author Charles Lowell (cowboyd@pobox.com)
 */
public abstract class HttpServlet
extends GenericServlet
implements Serializable 
{

  private String optionString = null;

  /**
   * Does nothing
   *
   * @since Servlet API 1.0
   */
  public HttpServlet() 
  {
  }


  /**
   * This method is called on a "DELETE" request.
   * The default implementation is that 
   * HttpServletResponse.SC_BAD_REQUEST (error nr: 400)
   * and the message "Method \"DELETE\" is not supported by this servlet"
   * is returned to the client.
   *
   * @since Servlet API 2.0
   *
   * @exception ServletException if an Servlet Exception occurs
   * @exception IOException if an IOException occurs
   */
  protected void doDelete(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		       "Method \"DELETE\" is not supported by this servlet");
  }


  /**
   * This method is called on a "GET" request.
   * The default implementation is that 
   * HttpServletResponse.SC_BAD_REQUEST (error nr: 400)
   * and the message "Method \"GET\" is not supported by this servlet"
   * is returned to the client.
   *
   * @since Servlet API 1.0
   *
   * @exception ServletException if an Servlet Exception occurs
   * @exception IOException if an IOException occurs
   */
  protected void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		       "Method \"GET\" is not supported by this servlet");
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
   * @since Servlet API 2.0
   *
   * @exception ServletException if an Servlet Exception occurs
   * @exception IOException if an IOException occurs
   */
  protected void doHead(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    doGet(request, new DoHeadHttpServletResponse(response));
  }


  /**
   * This method is called on a "OPTIONS" request.
   * It tells the client which methods are supported by the servlet.<BR>
   * This comes down to an implementation where HEAD, TRACE and OPTIONS
   * are supported by default because this class contains default
   * implementations of them. GET, POST, DELETE and PUT are added
   * to this list if the subclass has its own implementation of
   * the doGet, doPost, doDelete or doPut method respectively.
   * Note:<BR>
   * This implementation is probably not the most efficient one
   * (it uses introspection on the servlet class) but the whole
   * OPTIONS thing is intended for debugging purposes, and this
   * implementation does the job.
   *
   * @since Servlet API 2.0
   *
   * @throws ServletException if an Servlet Exception occurs
   * @throws IOException if an IOException occurs
   */
  protected void doOptions(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    if(optionString == null) 
      {
	try 
	  {
	    Method[] methodList = this.getClass().getMethods();
	    String basisClassName = "javax.servlet.http.HttpServlet";
	    StringBuffer result = new StringBuffer("");
	    for(int i = 0; i < methodList.length; i++) 
	      {
		Method servletMethod=methodList[i];
		//first check which class declared the method we're looking at
		if(!basisClassName.equals(servletMethod.getDeclaringClass().getName()))
		  {
		    //if this class did not declare the method that means
		    //the servlet is supporting is directly
		    if("doGet".equals(methodList[i].getName()))
		      result.append("GET, ");
		    else if("doDelete".equals(methodList[i].getName()))
		      result.append("DELETE, ");
		    else if("doPut".equals(methodList[i].getName()))
		      result.append("PUT, ");
		    else if("doPost".equals(methodList[i].getName()))
		      result.append("POST, ");
		  }
	      }
	    //this class provides usefull implementations of these methods
	    result.append("HEAD, TRACE, OPTIONS");
	    optionString = result.toString();
	  }
	catch (SecurityException e) 
	  {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
			       "Method \"OPTIONS\" is not supported by this servlet");
	  }
      }
    response.setHeader("Allow",optionString);
  }


  /**
   * This method is called on a "POST" request.
   * The default implementation is that 
   * HttpServletResponse.SC_BAD_REQUEST (error nr: 400)
   * and the message "Method \"POST\" is not supported by this servlet"
   * is returned to the client.
   *
   * @since Servlet API 1.0
   *
   * @throws ServletException if an Servlet Exception occurs
   * @throws IOException if an IOException occurs
   */
  protected void doPost(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		       "Method \"POST\" is not supported by this servlet");
  }


  /**
   * This method is called on a "PUT" request.
   * The default implementation is that 
   * HttpServletResponse.SC_BAD_REQUEST (error nr: 400)
   * and the message "Method \"PUT\" is not supported by this servlet"
   * is returned to the client.
   *
   * @since Servlet API 2.0
   *
   * @exception ServletException if an Servlet Exception occurs
   * @exception IOException if an IOException occurs
   */
  protected void doPut(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		       "Method \"PUT\" is not supported by this servlet");
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
   * @since Servlet API 2.0
   *
   * @exception ServletException if an Servlet Exception occurs
   * @exception IOException if an IOException occurs
   */
  protected void doTrace(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    StringBuffer result = new StringBuffer("\r\n");
    result.append("TRACE " + request.getRequestURI() + " " + request.getProtocol() + "\r\n");
    String headerName;
    for(Enumeration e = request.getHeaderNames(); e.hasMoreElements();) 
      {
	headerName = (String)e.nextElement();
	result.append(headerName + ": " + request.getHeader(headerName) + "\r\n");
      }
    response.setContentType("message/http");
    response.setContentLength(result.length());
    PrintWriter out = response.getWriter();
    out.print(result.toString());
    out.close();
  }


  /**
   * Returns the time the requested uri was last modified in seconds since
   * 1 january 1970. Default implementation returns -1.
   *
   * @since Servlet API 1.0
   */
  protected long getLastModified(HttpServletRequest request) 
  {
    return -1;
  }


  /**
   * This method looks whether the request is a POST, GET, etc method,
   * and then calls the appropriate doPost, doGet, whatever method.<BR>
   * If the request method is something it can't handle it sends
   * a HttpServletResponse.SC_BAD_REQUEST error through the response.
   *
   * @since Servlet API 1.0
   *
   * @exception ServletException an error has occured
   * @exception IOException an error has occured
   */
  protected void service(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException 
  {
    if(request.getMethod().equals("GET") ) 
      {
	if(testConditional(request,response)) 
	  doGet(request,response);
      }
    else if (request.getMethod().equals("HEAD") ) 
      {
	if(testConditional(request,response)) 
	  doHead(request,response);
      }
    else if (request.getMethod().equals("POST") ) 
      {
	doPost(request,response);
      }
    else if (request.getMethod().equals("DELETE") ) 
      {
	doDelete(request,response);
      }
    else if (request.getMethod().equals("OPTIONS") ) 
      {
	doOptions(request,response);
      }
    else if (request.getMethod().equals("PUT") ) 
      {
	doPut(request,response);
      }
    else if (request.getMethod().equals("TRACE") ) 
      {
	doTrace(request,response);
      }
    else 
      {
	response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Method \""
			   +request.getMethod()+"\" is not supported by this servlet");
      }
  }


  /**
   * Frontend for calling service(HttpServletRequest,HttpServletResponse).
   * <P>
   * This method tries to typecast the ServletRequest and the
   * ServletResponse to HttpServletRequest and HttpServletResponse
   * and then call service(HttpServletRequest,HttpServletResponse).<BR>
   *
   * @since Servlet API 1.0
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
      service((HttpServletRequest)request, (HttpServletResponse)response);
    else 
      throw new ServletException("This servlet only expects http requests");
  }


  /**
   * Check for possible "If-Modified-Since" header parameters.
   * Sets the "Last-Modified" header field if != -1.<BR>
   * Sets the "Date" header to the current date/time<BR>
   * Sets the "Server" header to getServerInfo<BR>
   * throws an IllegalArgumentException on a malformed date header.<BR>
   * just like sun's implemenation. :-( <BR>
   *
   * @return true: get the requested resource
   * false: don't get it.
   */
  private boolean testConditional(HttpServletRequest request,HttpServletResponse response)
    throws IOException 
  {
    response.setDateHeader("Date",System.currentTimeMillis());
    response.setHeader("Server",getServletConfig().getServletContext().getServerInfo());
    long lastModifiedTime = getLastModified(request);
    if(lastModifiedTime >= 0) 
      {
	response.setDateHeader("Last-Modified",lastModifiedTime);
	long requestModifiedTime = request.getDateHeader("If-Modified-Since");
	if((requestModifiedTime >= 0)
	   &&(requestModifiedTime <= lastModifiedTime )) 
	  {
	    response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
	    return false;
	  }
      }
    return true;
  }


  /**
   * This class is built for use in the doHead method of HttpServlet.<BR>
   * The only reason it even has a name is that it needs a constructor.<BR>
   * It enables the doHead method to have the doGet method do the work of
   * a HEAD method without him noticing it.<BR>
   * The only important method in this class is the getOutputStream
   * method.
   * This method returns a ServletOutputStream that has an empty
   * write(int) and an empty write(byte[],int,int) implementation.
   * <P>
   * The real response is passed to this class in the constructor.
   * All other methods call this response to do the work.
   * This means that all those methods have an implementation of this
   * form:
   * <PRE>
   * void foo () 
   {
   * realResponse.foo();
   * }
   * </PRE>
   */
  private class DoHeadHttpServletResponse
    implements HttpServletResponse 
  {

    HttpServletResponse response;


    DoHeadHttpServletResponse(HttpServletResponse response) 
    {
      this.response = response;
    }


    public void addCookie(Cookie cookie) 
    {
      response.addCookie(cookie);
    }


    public boolean containsHeader(String name) 
    {
      return response.containsHeader(name);
    }



    public String encodeRedirectURL(String url) 
    {
      return response.encodeRedirectURL(url);
    }


    public String encodeURL(String url) 
    {
      return response.encodeURL(url);
    }


    public void sendError(int errorCode, String errorMessage)
      throws IOException 
    {
      response.sendError(errorCode,errorMessage);
    }


    public void sendError(int errorCode)
      throws IOException 
    {
      response.sendError(errorCode);
    }


    public void sendRedirect(String location)
      throws IOException 
    {
      response.sendRedirect(location);
    }


    public void setDateHeader(String name, long value) 
    {
      response.setDateHeader(name,value);
    }


    public void setHeader(String name, String value) 
    {
      response.setHeader(name,value);
    }


    public void addHeader(String name, String value) 
    {
      response.addHeader(name,value);
    }


    public void addDateHeader(String name, long value) 
    {
      response.addDateHeader(name,value);
    }


    public void addIntHeader(String name, int value) 
    {
      response.addIntHeader(name,value);
    }


    public void setBufferSize(int size) 
    {
      response.setBufferSize(size);
    }


    public int getBufferSize() 
    {
      return response.getBufferSize();
    }


    public void reset() 
    {
      response.reset();
    }


    public boolean isCommitted() 
    {
      return response.isCommitted();
    }

    public void resetBuffer ()
    {
      response.resetBuffer ();
    }

    public void flushBuffer()
      throws IOException 
    {
      response.flushBuffer();
    }


    public void setLocale(Locale locale) 
    {
      response.setLocale(locale);
    }


    public Locale getLocale() 
    {
      return response.getLocale();
    }



    public void setIntHeader(String name, int value) 
    {
      response.setIntHeader(name,value);
    }


    public void setStatus(int status) 
    {
      response.setStatus(status);
    }


    public void setStatus(int status, String message) 
    {
      // We could also call sendError(sc,sm),
      // but that could throw an IOException.
      response.setStatus(status);
    }


    public String encodeUrl(String url) 
    {
      return response.encodeURL(url);
    }


    public String encodeRedirectUrl(String url) 
    {
      return response.encodeRedirectURL(url);
    }


    /* ********************************************************************
     *                                                                    *
     *                  ServletResponse Methods                           *
     *                                                                    *
     **********************************************************************/

    public void setContentLength(int length) 
    {
      response.setContentLength(length);
    }

    public void setContentType(String type) 
    {
      response.setContentType(type);
    }

    public String getContentType()
    {
      return response.getContentType();
    }

    public void setCharacterEncoding(String encoding)
    {
      response.setCharacterEncoding(encoding);
    }

    /**
     * Returns a dummy ServletOutputStream.
     * This method is the only one whose implementation differs from that
     * of an ordinary HttpServletResponse.
     * The ServletOutputStream it returns has an empty write(int)
     * and an empty write(byte[],int,int) implementation.
     */
    public ServletOutputStream getOutputStream()
      throws IOException 
    {
      return new ServletOutputStream() 
	{
	  public void write(int aByte)
	    throws IOException 
	  {
	  }

	  public void write(byte[] buffer,
			    int offset,
			    int length)
	    throws IOException 
	  {
	  }
	};
    }


    public PrintWriter getWriter()
      throws IOException 
    {
      return response.getWriter();
    }


    public String getCharacterEncoding() 
    {
      return response.getCharacterEncoding();
    }
  }
}
