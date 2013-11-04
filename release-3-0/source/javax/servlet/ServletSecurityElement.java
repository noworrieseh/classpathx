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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;

/**
 * A ServletSecurity annotation value.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public class ServletSecurityElement
    extends HttpConstraintElement
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");

    private Collection<HttpMethodConstraintElement> constraints;
    private Collection<String> names;

    public ServletSecurityElement()
    {
        constraints = new HashSet<HttpMethodConstraintElement>();
        names = new HashSet<String>();
    }

    public ServletSecurityElement(HttpConstraintElement constraint)
    {
        super(constraint.getEmptyRoleSemantic(),
                constraint.getTransportGuarantee(),
                constraint.getRolesAllowed());
        constraints = new HashSet<HttpMethodConstraintElement>();
        names = new HashSet<String>();
    }

    public ServletSecurityElement(Collection<HttpMethodConstraintElement> constraints)
    {
        if (constraints == null)
          {
            constraints = new HashSet<HttpMethodConstraintElement>();
          }
        this.constraints = constraints;
        names = getNames(constraints);
    }

    public ServletSecurityElement(HttpConstraintElement constraint,
            Collection<HttpMethodConstraintElement> constraints)
    {
        super(constraint.getEmptyRoleSemantic(),
                constraint.getTransportGuarantee(),
                constraint.getRolesAllowed());
        if (constraints == null)
          {
            constraints = new HashSet<HttpMethodConstraintElement>();
          }
        this.constraints = constraints;
        names = getNames(constraints);
    }

    public ServletSecurityElement(ServletSecurity annotation)
    {
        super(annotation.value().value(),
                annotation.value().transportGuarantee(),
                annotation.value().rolesAllowed());
        constraints = new HashSet<HttpMethodConstraintElement>();
        HttpMethodConstraint[] a = annotation.httpMethodConstraints();
        for (int i = 0; i < a.length; i++)
          {
            HttpMethodConstraint constraint = a[i];
            constraints.add(new HttpMethodConstraintElement(constraint.value(),
                        new HttpConstraintElement(constraint.emptyRoleSemantic(),
                            constraint.transportGuarantee(),
                            constraint.rolesAllowed())));
          }
        names = getNames(constraints);
    }

    /**
     * Returns the HTTP method specific constraint elements.
     */
    public Collection<HttpMethodConstraintElement> getHttpMethodConstraints()
    {
        return constraints;
    }

    /**
     * Returns the HTTP method names defined by the constraints.
     */
    public Collection<String> getMethodNames()
    {
        return names;
    }

    private static Collection<String> getNames(Collection<HttpMethodConstraintElement> constraints)
    {
        Collection<String> names = new HashSet<String>();
        for (Iterator<HttpMethodConstraintElement> i = constraints.iterator();
                i.hasNext(); )
          {
            HttpMethodConstraintElement constraint = i.next();
            String name = constraint.getMethodName();
            if (names.contains(name))
              {
                String message = L10N.getString("err.duplicate_method_name");
                Object[] args = new Object[] { name };
                throw new IllegalArgumentException(MessageFormat.format(message, args));
              }
            names.add(name);
          }
        return names;
    }

}
