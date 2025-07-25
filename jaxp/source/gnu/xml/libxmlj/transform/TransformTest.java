/* 
 * TransformTest.java
 * Copyright (C) 2003, 2004 Free Software Foundation, Inc.
 * 
 * This file is part of GNU Classpathx/jaxp.
 * 
 * GNU Classpath is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *  
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GNU Classpath; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA.
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
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */

package gnu.xml.libxmlj.transform;

import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 *  Simple test class with command line interface.
 */
public class TransformTest
{

   /**
    *  Launches the test.
    *
    *  @param args[0] Path or URL of the XSLT sheet
    *
    *  @param args[1] Path or URL of the source XML document
    *
    *  @param args[2] Path of the the file the resulting XML document
    *  will be written to.
    *
    *  @fixme It would be nice to use 
    */
  public static void main(String[] args)
  {
    try
      {
        // Force use of Libxsltj
        /*System.setProperty ("javax.xml.transform.TransformerFactory",
          "gnu.xml.libxmlj.transform.GnomeTransformerFactory");*/
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        Source source = new StreamSource(System.in);
        Result target = new StreamResult(System.out);
        // Read arguments
        if (args.length >= 1)
          {
            Source xsltSource = new StreamSource(args[0]);
            if (args.length > 1)
              {
                source = new StreamSource(args[1]);
                if (args.length > 2)
                  {
                    target = new StreamResult(args[2]);
                  }
              }
            
            // Prepare stylesheet
            transformer = factory.newTransformer(xsltSource);
          }
        else
          {
            // Identity transform
            transformer = factory.newTransformer();
          }
        
        // Set test parameters
        transformer.setParameter("bar", "lala");
        
        // Perform transformation
        transformer.transform(source, target);
      }
    catch (Exception e)
      {
        e.printStackTrace(System.err);
        System.err.flush();
      }
  }
  
}

