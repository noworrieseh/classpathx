/*
 * Copyright (C) 2003, 2013 Free Software Foundation, Inc.
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
package javax.servlet.jsp.tagext;

import javax.servlet.jsp.JspWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

/**
 * Encapsulation of the body of an action.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public abstract class BodyContent
    extends JspWriter
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
        try
          {
            jspWriter.clearBuffer();
          }
        catch (IOException e)
          {
          }
        catch (IllegalStateException e)
          {
          }
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
