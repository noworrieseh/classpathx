package test.sig;

// ----------------------------------------------------------------------------
// $Id: AllTests.java,v 1.2 2002-01-11 22:00:55 raif Exp $
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

/**
 * TestSuite that runs all tests for all subordinate packages.<p>
 *
 * @version $Revision: 1.2 $
 */
public class AllTests extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   public AllTests(String name) {
      super(name);
   }

   // Constructor(s)
   // -------------------------------------------------------------------------

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite()); // run the regression tests
   }

   public static Test suite() {
      TestSuite result =
         new TestSuite("GNU Crypto keypair generator and signature tests");

      result.addTest(test.sig.TestOfKeyPairGeneratorFactory.suite());
      result.addTest(test.sig.TestOfSignatureFactory.suite());
      result.addTest(test.sig.dss.AllTests.suite());
      result.addTest(test.sig.rsa.AllTests.suite());

      return result;
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
