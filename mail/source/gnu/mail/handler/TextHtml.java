/*
 * TextHtml.java
 * Copyright (C) 2002 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gnu.mail.handler;

/**
 * A JAF data content handler for the text/html MIME content type.
 */
public final class TextHtml
  extends Text
{

  /**
   * Constructor for text/html.
   */
  public TextHtml()
  {
    super("text/html", "HTML");
  }
  
}
