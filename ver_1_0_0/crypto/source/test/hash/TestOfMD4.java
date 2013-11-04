package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfMD4.java,v 1.1 2002-06-12 10:29:13 raif Exp $
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

import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.MD4;
import gnu.crypto.util.Util;

/**
 * <p>Conformance tests for the MD4 implementation.</p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfMD4 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The local reference to an MD4. */
   private IMessageDigest algorithm;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfMD4(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfMD4.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
//         IMessageDigest algorithm = new MD4();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void testA() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("a".getBytes(), 0, 1);
         byte[] md = algorithm.digest();
         String exp = "BDE52CB31DE33E46245E05FBDBD6FB24";
         assertTrue("testA()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testA(): "+String.valueOf(x));
      }
   }

   public void testABC() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("abc".getBytes(), 0, 3);
         byte[] md = algorithm.digest();
         String exp = "A448017AAF21D8525FC10AE87AA6729D";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testABC(): "+String.valueOf(x));
      }
   }

   public void testMessageDigest() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("message digest".getBytes(), 0, 14);
         byte[] md = algorithm.digest();
         String exp = "D9130A8164549FE818874806E1C7014B";
         assertTrue("testMessageDigest()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testMessageDigest(): "+String.valueOf(x));
      }
   }

   public void testAlphabet() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("abcdefghijklmnopqrstuvwxyz".getBytes(), 0, 26);
         byte[] md = algorithm.digest();
         String exp = "D79E1C308AA5BBCDEEA8ED63DF412DA9";
         assertTrue("testAlphabet()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAlphabet(): "+String.valueOf(x));
      }
   }

   public void testAsciiSubset() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".getBytes(), 0, 62);
         byte[] md = algorithm.digest();
         String exp = "043F8582F241DB351CE627E153E7F0E4";
         assertTrue("testAsciiSubset()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAsciiSubset(): "+String.valueOf(x));
      }
   }

   public void testEightyNumerics() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes(), 0, 80);
         byte[] md = algorithm.digest();
         String exp = "E33B4DDC9C38F2199C3E7B164FCC0536";
         assertTrue("testEightyNumerics()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testEightyNumerics(): "+String.valueOf(x));
      }
   }

   public void testCloning() {
      try {
//         IMessageDigest algorithm = new MD4();
         algorithm.update("a".getBytes(), 0, 1);
         IMessageDigest clone = (IMessageDigest) algorithm.clone();
         byte[] md = algorithm.digest();
         String exp = "BDE52CB31DE33E46245E05FBDBD6FB24";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));

         clone.update("bc".getBytes(), 0, 2);
         md = clone.digest();
         exp = "A448017AAF21D8525FC10AE87AA6729D";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testCloning(): "+String.valueOf(x));
      }
   }

   // over-ridden methods from junit.framework.TestCase -----------------------

   protected void setUp() throws Exception {
      algorithm = new MD4();
   }
}
