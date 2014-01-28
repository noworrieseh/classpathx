/*
 * SMTPOutputStream.java
 * Copyright (C) 2014 The Free Software Foundation
 * Copyright (C) 2013 Chris Burdess <dog@gnu.org>
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

package gnu.inet.smtp;

import java.io.IOException;
import java.io.OutputStream;
import gnu.inet.util.CRLFOutputStream;

/**
 * This convenience class ensures that the SMTP client escapes a dot at the
 * start of a line by doubling it.
 */
class SMTPOutputStream extends CRLFOutputStream
{

    /**
     * The Period octet.
     */
    public static final int Period = '.';

    SMTPOutputStream(OutputStream out)
    {
        super(out);
    }

    public void write(int c)
        throws IOException
    {
        if (atBOL && c == Period)
        {
            super.write(c); // double dot at start of line
        }
        super.write(c);
    }

    public void write(byte[] b, int off, int len)
        throws IOException
    {
        if (len > 0)
        {
            int b0 = b[off];
            if (atBOL && b0 == Period)
            {
                super.write(b0); // double dot at start of line
            }
            super.write(b, off, len);
        }
    }

}
