/*
 * FunctionInfo.java -- XXX
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

/**
 * Information for a function in a Tag Library.
 *
 * @author Arnaud Vandyck - arnaud.vandyck@ulg.ac.be
 * @since JSP2.0
 */
public class FunctionInfo
{

  private String name;
  private String functionClass;
  private String functionSignature;

  /**
   * Construct the function info.
   * @param name the name of the function
   * @param functionClass the class of the function
   * @param functionSignature the signature of the function
   */
  public FunctionInfo(String name,
                      String functionClass,
                      String functionSignature)
  {
    this.name = name;
    this.functionClass = functionClass;
    this.functionSignature = functionSignature;
  }

  /**
   * Get the Name value.
   * @return the Name value.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the FunctionClass value.
   * @return the FunctionClass value.
   */
  public String getFunctionClass() {
    return functionClass;
  }

  /**
   * Get the FunctionSignature value.
   * @return the FunctionSignature value.
   */
  public String getFunctionSignature() {
    return functionSignature;
  }
  
}
