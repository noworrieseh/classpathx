/*
 * SMTPOutputStream.java
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

    private byte last = '\n';

    SMTPOutputStream(OutputStream out)
    {
        super(out);
    }

    public void write(int c)
        throws IOException
    {
        if (last == '\n' && c == '.')
        {
            super.write(c); // double dot at start of line
        }
        super.write(c);
        last = (byte) c;
    }

    public void write(byte[] b, int off, int len)
        throws IOException
    {
        if (len > 0)
        {
            byte b0 = b[off];
            if (last == '\n' && b0 == '.')
            {
                super.write((int) b0); // double dot at start of line
            }
            super.write(b, off, len);
            last = b[off + (len - 1)];
        }
    }

}
