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
 * An HttpConstraint annotation value.
 * @version 3.0
 * @since 3.0
 * @author Chris Burdess
 */
public class HttpConstraintElement
{

    private static final ResourceBundle L10N =
        ResourceBundle.getBundle("javax.servlet.L10N");
    private ServletSecurity.EmptyRoleSemantic emptyRoleSemantic;
    private ServletSecurity.TransportGuarantee transportGuarantee;
    private String[] rolesAllowed;

    /**
     * Constructs an element with a default semantic of <code>PERMIT</code>.
     */
    public HttpConstraintElement()
    {
        this(ServletSecurity.EmptyRoleSemantic.PERMIT);
    }

    /**
     * Constructs an element with the specified default authorization semantic.
     */
    public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic)
    {
        this(semantic, ServletSecurity.TransportGuarantee.NONE, new String[0]);
    }

    /**
     * Constructs an element with the specified transport guarantee and
     * semantic.
     */
    public HttpConstraintElement(ServletSecurity.TransportGuarantee guarantee, String roleNames[])
    {
        this(ServletSecurity.EmptyRoleSemantic.PERMIT, guarantee, roleNames);
    }

    /**
     * Constructs an element with the specified transport guarantee,
     * semantic and role names.
     */
    public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic, ServletSecurity.TransportGuarantee guarantee, String... roleNames)
    {
        if (semantic == ServletSecurity.EmptyRoleSemantic.DENY && roleNames.length > 0)
          {
            throw new IllegalArgumentException(L10N.getString("err.deny_semantic_with_roles_allowed"));
          }
        emptyRoleSemantic = semantic;
        transportGuarantee = guarantee;
        rolesAllowed = roleNames;
    }

    /**
     * Returns the default authorization semantic.
     */
    public ServletSecurity.EmptyRoleSemantic getEmptyRoleSemantic()
    {
        return emptyRoleSemantic;
    }

    /**
     * Returns the transport guarantee.
     */
    public ServletSecurity.TransportGuarantee getTransportGuarantee()
    {
        return transportGuarantee;
    }

    /**
     * Returns the names of the authorized roles.
     * If the returned value is empty, its meaning shall be determined by
     * {@link #getEmptyRoleSemantic}.
     */
    public String[] getRolesAllowed()
    {
        return rolesAllowed;
    }

}
