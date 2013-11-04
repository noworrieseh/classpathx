/*
 * Copyright (C) 2013 Free Software Foundation, Inc.
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

import java.util.ResourceBundle;
import javax.servlet.annotation.ServletSecurity;

/**
 * An HttpMethodConstraint annotation value.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public class HttpMethodConstraintElement extends HttpConstraintElement
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");
    private String methodName;

    /**
     * Constructs an element with the specified method name.
     */
    public HttpMethodConstraintElement(String methodName)
    {
        if (methodName == null || methodName.length() == 0)
          {
            throw new IllegalArgumentException("err.invalid_http_method_name");
          }
        this.methodName = methodName;
    }

    /**
     * Constructs an element with the specified method name and value.
     */
    public HttpMethodConstraintElement(String methodName, HttpConstraintElement constraint)
    {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        if (methodName == null || methodName.length() == 0)
          {
            throw new IllegalArgumentException("err.invalid_http_method_name");
          }
        this.methodName = methodName;
    }

    /**
     * Returns the HTTP method name.
     */
    public String getMethodName()
    {
        return methodName;
    }

}
