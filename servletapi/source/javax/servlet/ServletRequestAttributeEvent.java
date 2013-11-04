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
package javax.servlet;

/** an attribute within the <code>ServletRequest</code> changed.
 * The event source is defined as the source <code>ServletRequest</code>.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @version 3.0
 * @since 2.4
 */
public class ServletRequestAttributeEvent
    extends ServletRequestEvent
{

    /** the name of the attribute.
     */
    private String name;

    /** the value of the attribute.
     */
    private Object value;


    /** create the event.
     */
    public ServletRequestAttributeEvent(ServletContext context,
            ServletRequest request,
            String name,
            Object value)
    {
        super(context, request);
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

}
