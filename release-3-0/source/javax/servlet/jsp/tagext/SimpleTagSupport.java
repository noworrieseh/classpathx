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

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import java.io.IOException;

/**
 * Define tag handlers implementing Simpletag.
 * @version 2.1
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @author Chris Burdess
 */
public class SimpleTagSupport
    implements SimpleTag
{

    /**
     * Body of the tag.
     */
    protected JspFragment jspBody;

    /**
     * Context for the upcoming tag invocation.
     */
    protected JspContext jspContext;

    /**
     * 
     */
    private JspTag parent;

    /**
     * 
     */
    public SimpleTagSupport()
    {
    }

    /**
     * Default processing of the tag (does nothing).
     * 
     * @throws JspException error while processing the page.
     * @throws SkipPageException if the page invoked this tag is no more evaluated.
     * @throws IOException error while writing to the output stream.
     * @throws JspException error while processing this tag.
     * @see SimpleTag#doTag()
     */
    public void doTag()
        throws JspException, IOException
    {
    }

    /**
     * Get the Parent value.
     * @return the Parent of this tag.
     */
    public JspTag getParent()
    {
        return parent;
    }

    /**
     * Set the Parent of this tag.
     * @param newParent The new Parent value (the tag that encloses this one).
     */
    public void setParent(JspTag newParent) 
    {
        this.parent = newParent;
    }

    /**
     * Get the JspBody value.
     * @return the JspBody value.
     */
    public JspFragment getJspBody() 
    {
        return jspBody;
    }

    /**
     * Set the JspBody value.
     * @param newJspBody The new JspBody value.
     */
    public void setJspBody(JspFragment newJspBody) 
    {
        this.jspBody = newJspBody;
    }

    /**
     * Get the JspContext value.
     * @return the JspContext value.
     */
    public JspContext getJspContext() 
    {
        return jspContext;
    }

    /**
     * Set the JspContext value.
     * @param newJspContext The new JspContext value.
     */
    public void setJspContext(JspContext newJspContext)
    {
        this.jspContext = newJspContext;
    }

    /**
     * Find the instance of a given class type that is closest to a given
     * instance.
     * This method uses the getParent method from Tag and/ or SimpleTag
     * interfaces.
     * 
     * @param from the instance from where to start looking
     * @param klass the subclass of JspTag or interface to be matched.
     * @return the nearest ancestor that implements the interface or is an
     * instance of the class specified.
     */
    public static final JspTag findAncestorWithClass(JspTag from,
            Class<?> klass)
    {
        JspTag parent = null;
        do
          {
            if (from instanceof SimpleTag)
              {
                parent = ((SimpleTag) from).getParent();
              }
            else if (from instanceof Tag)
              {
                parent = ((Tag) from).getParent();
              }
            if (parent instanceof TagAdapter)
              {
                parent = ((TagAdapter) parent).getAdaptee();
              }
            if (parent == null)
              {
                return null;
              }
            if (klass.isAssignableFrom(parent.getClass()))
              {
                return parent;
              }
            from = parent;
          }
        while (parent != null);
        return parent;
    }

}
