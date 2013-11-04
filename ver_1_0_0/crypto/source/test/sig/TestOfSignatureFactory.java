package test.sig;

// ----------------------------------------------------------------------------
// $Id: TestOfSignatureFactory.java,v 1.1 2002-01-11 22:00:15 raif Exp $
//
// Copyright (C) 2001, 2002 Free Software Foundation, Inc.
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

import gnu.crypto.sig.ISignature;
import gnu.crypto.sig.SignatureFactory;

import java.util.Iterator;

/**
 * Conformance tests for the SignatureFactory implementation.<p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfSignatureFactory extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfSignatureFactory(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfSignatureFactory.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testGetInstance() {
      String scheme;
      ISignature algorithm;
      for (Iterator it = SignatureFactory.getNames().iterator(); it.hasNext(); ) {
         scheme = (String) it.next();
         try {
            algorithm = null;
            algorithm = SignatureFactory.getInstance(scheme);
            assertNotNull("getInstance("+String.valueOf(scheme)+")", algorithm);
         } catch (RuntimeException x) {
            fail("getInstance("+String.valueOf(scheme)+"): "+String.valueOf(x));
         }
      }
   }
}
