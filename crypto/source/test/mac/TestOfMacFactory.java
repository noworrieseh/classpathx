package test.mac;

// ----------------------------------------------------------------------------
// $Id: TestOfMacFactory.java,v 1.1 2002-06-08 05:32:17 raif Exp $
//
// Copyright (C) 2002, Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
// more details.
//
// You should have received a copy of the GNU General Public License along with
// this program; see the file COPYING.  If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
//
// As a special exception, if you link this library with other files to produce
// an executable, this library does not by itself cause the resulting
// executable to be covered by the GNU General Public License.  This exception
// does not however invalidate any other reasons why the executable file might
// be covered by the GNU General Public License.
// ----------------------------------------------------------------------------

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import gnu.crypto.mac.IMac;
import gnu.crypto.mac.MacFactory;

import java.util.Iterator;

/**
 * <p>Conformance test for the {@link MacFactory} implementation.</p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfMacFactory extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfMacFactory(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfMacFactory.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testGetInstance() {
      String mac;
      IMac algorithm;
      for (Iterator it = MacFactory.getNames().iterator(); it.hasNext(); ) {
         mac = (String) it.next();
         try {
            algorithm = null;
            algorithm = MacFactory.getInstance(mac);
            assertNotNull("getInstance("+String.valueOf(mac)+")", algorithm);
         } catch (InternalError x) {
            fail("getInstance("+String.valueOf(mac)+"): "+String.valueOf(x));
         }
      }
   }

   protected void setUp() throws Exception {
   }
}
