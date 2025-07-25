/*
 * CommDriver.java
 * Copyright (C) 2004 The Free Software Foundation
 *
 * This file is part of GNU CommAPI, a library.
 *
 * GNU CommAPI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GNU CommAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */
package javax.comm;

/**
 * This interface is used internally by loadable device drivers, and should
 * not be used by application-level programs.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 2.0.3
 */
public interface CommDriver
{

  /**
   * This method ensures that the hardware is present, loads any native
   * libraries, and register the port names with the CommPortIdentifier.
   */
  public void initialize();

  /**
   * This method will be called on <code>CommPortIdentifier.open</code>.
   * @param portName a string registered earlier using
   * <code>CommPortIdentifier.addPortName</code>
   * @param portType the port type
   */
  public CommPort getCommPort(String portName, int portType);

}
