package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfSha160.java,v 1.1 2001-12-08 21:31:32 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
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

import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.Sha160;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the SHA-1 implementation.
 *
 * @version $Revision: 1.1 $
 */
public class TestOfSha160 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfSha160(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfSha160.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IMessageDigest algorithm = new Sha160();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void testAlphabet() {
      try {
         IMessageDigest algorithm = new Sha160();
         algorithm.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes(), 0, 56);
         byte[] md = algorithm.digest();
         String exp = "84983E441C3BD26EBAAE4AA1F95129E5E54670F1";
         assertTrue("testAlphabet()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAlphabet(): "+String.valueOf(x));
      }
   }
}
