/*
 * BodyContent.java -- XXX
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
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

/**
 * Encapsulation of the body of an action.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public abstract class BodyContent extends JspWriter
{

  /**
   * 
   */
  protected JspWriter jspWriter;

  /**
   * Unbounded buffer?, no autoflushing
   * 
   * @param enclosed JspWriter
   */
  protected BodyContent(JspWriter e)
  {
    super(JspWriter.UNBOUNDED_BUFFER, false);
    this.jspWriter = e;
  }

  /**
   * 
   * 
   * @throws IOException always thrown because flush do not have to be
   * called here.
   */
  public void flush()
    throws IOException
  {
    throw new IOException( "It's not valid to flush a BodyContent!" );
  }

  /**
   * Clear the body. 
   */
  public void clearBody()
  {
    try {
      this.jspWriter.clearBuffer();
    } catch ( IOException ioex ) {
    } catch ( IllegalStateException illex ) {
    } // end of catch
    
  }

  /**
   * 
   * 
   * @return the BodyContent as a Reader
   */
  public abstract Reader getReader();

  /**
   * 
   * 
   * @return the Bodycontent as a String
   */
  public abstract String getString();

  /**
   * 
   * 
   * @param out a writer
   * @throws IOException
   */
  public abstract void writeOut(Writer out)
    throws IOException;

  /**
   * 
   * 
   * @return the JspWriter enclosed 
   */
  public JspWriter getEnclosingWriter()
  {
    return jspWriter;
  }

}
