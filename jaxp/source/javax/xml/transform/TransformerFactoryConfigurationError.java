/*
  GNU-Classpath Extensions:	jaxp
  Copyright (C) 2001 Andrew Selkirk, David Brownell

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

package javax.xml.transform;

/**
 * Reports a {@link TransformerFactory} configuration error.
 * @author	Andrew Selkirk, David Brownell
 * @version	1.0
 */
public class TransformerFactoryConfigurationError extends Error
{
    private Exception	exception	= null;


    //-------------------------------------------------------------
    // Initialization ---------------------------------------------
    //-------------------------------------------------------------

    public TransformerFactoryConfigurationError ()
	{ }

    public TransformerFactoryConfigurationError (Exception e)
	{ exception = e; }

    public TransformerFactoryConfigurationError (Exception e, String msg)
    {
	super (msg);
	exception = e;
    }

    public TransformerFactoryConfigurationError (String msg)
	{ super (msg); }


    //-------------------------------------------------------------
    // Methods ----------------------------------------------------
    //-------------------------------------------------------------

    public String getMessage ()
    {
	if (super.getMessage () == null && exception != null)
	    return exception.getMessage ();
	return super.getMessage ();
    }

    public Exception getException ()
	{ return exception; }
}
