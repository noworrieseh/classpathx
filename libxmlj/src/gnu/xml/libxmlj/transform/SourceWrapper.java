/* 
 * $Id: SourceWrapper.java,v 1.2 2003-03-07 01:52:26 julian Exp $
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

import java.io.File;
import java.io.InputStream;
import java.io.PushbackInputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import javax.xml.transform.stream.StreamSource;

public class SourceWrapper
{
  private Source source;

  public SourceWrapper (Source source)
  {
    this.source = source;
  }

  public PushbackInputStream getInputStream () 
    throws TransformerException
  {
    return IOToolkit.getSourceInputStream (source);
  }

  public String getFilename ()
  {
    return new File (source.getSystemId ()).getName ();
  }

  public String getDirectory ()
  {
    return new File (source.getSystemId ()).
      getParentFile ().getAbsolutePath ();
  }

  public String getSystemId ()
  {
    return source.getSystemId ();
  }
}
