/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 Andrew Selkirk

  For more information on the classpathx please mail: nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
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
package javax.activation;

/**
 * MIME Type Parse Exception.
 */
public class MimeTypeParseException
extends Exception
{

  //-------------------------------------------------------------
  // Initialization ---------------------------------------------
  //-------------------------------------------------------------

  /**
   * Create MIME Type parse exception with description.
   * @param value Description
   */
  public MimeTypeParseException(String value)
  {
    super(value);
  } // MimeTypeParseException()

  /**
   * Create MIME Type parse exception.
   */
  public MimeTypeParseException()
  {
    super();
  } // MimeTypeParseException()


} // MimeTypeParseException
