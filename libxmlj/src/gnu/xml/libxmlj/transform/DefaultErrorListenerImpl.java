/* 
 * $Id: DefaultErrorListenerImpl.java,v 1.1.1.1 2003-02-27 01:22:24 julian Exp $
 * Copyright (C) 2003 Julian Scheid
 * 
 * This file is part of GNU LibxmlJ, a JAXP-compliant Java wrapper for
 * the XML and XSLT C libraries for Gnome (libxml2/libxslt).
 * 
 * GNU LibxmlJ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *  
 * GNU LibxmlJ is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GNU LibxmlJ; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA. 
 */

package gnu.xml.libxmlj.transform;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 *  A simple implementation of {@link ErrorListener} which prints
 *  warning messages to {@link System.err} and throws a {@link
 *  TransformerException} for normal and fatal errors.
 */
class DefaultErrorListenerImpl implements ErrorListener
{
  public void error (TransformerException exception)
    throws TransformerException
  {
    System.err.println ("Error: " + exception.getMessageAndLocation ());
  }

  public void fatalError (TransformerException exception)
    throws TransformerException
  {
    throw exception;
  }

  public void warning (TransformerException exception)
    throws TransformerException
  {
    throw exception;
  }
}
