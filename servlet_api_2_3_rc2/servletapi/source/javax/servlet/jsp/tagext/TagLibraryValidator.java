/*
 * TagLibraryValidator.java -- XXX
 * 
 * Copyright (c) 2003 by Free Software Foundation, Inc.
 * Written by Arnaud Vandyck (arnaud.vandyck@ulg.ac.be)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation  
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package javax.servlet.jsp.tagext;

import java.util.Map;
import java.util.HashMap;

/**
 * Validator operates on the XML view associated with the JSP page.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 */
public abstract class TagLibraryValidator
{

  private Map initParameters;

  public TagLibraryValidator()
  {
    this.initParameters = new HashMap();
  }

  /**
   * Get the InitParameters value.
   * @return the InitParameters value.
   */
  public Map getInitParameters() {
    return initParameters;
  }

  /**
   * Set the InitParameters value.
   * @param newInitParameters The new InitParameters value.
   */
  public void setInitParameters(Map newInitParameters) {
    this.initParameters = newInitParameters;
  }

  /**
   * If validation is OK, the method returns null, otherwise, it returns an array of messages.
   * @return ValidationMessages or null if no error occurs.
   */
  public ValidationMessage[] validate(String prefix,
                                      String uri,
                                      PageData page)
  {
    return null;
  }

  /**
   * Release any data
   */
  public void release()
  {
    this.initParameters = new HashMap();
  }

}
