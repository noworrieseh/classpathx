/*
 * SourceLocator.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obliged to do so.  If you do not wish to do so, delete this
 * exception statement from your version. 
 */
package javax.xml.transform;

/**
 * The location in an XML resource at which an event occurred.
 * Tis is equivalent to the SAX Locator.
 * 
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public interface SourceLocator
{

  /**
   * Returns the XML public ID for the document.
   */
  public String getPublicId();

  /**
   * Returns the XML system ID for the document.
   */
  public String getSystemId();

  /**
   * Returns the line number at which the event occurred.
   * @return the line number, or -1 if not available
   */
  public int getLineNumber();

  /**
   * Returns the column number at which the event occurred.
   * @return the column number, or -1 if not available
   */
  public int getColumnNumber();

}
