package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfMD5.java,v 1.1 2001-12-08 21:31:32 raif Exp $
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
import gnu.crypto.hash.MD5;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the MD5 implementation.
 *
 * @version $Revision: 1.1 $
 */
public class TestOfMD5 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfMD5(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfMD5.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IMessageDigest algorithm = new MD5();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void testA() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("a".getBytes(), 0, 1);
         byte[] md = algorithm.digest();
         String exp = "0CC175B9C0F1B6A831C399E269772661";
         assertTrue("testA()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testA(): "+String.valueOf(x));
      }
   }

   public void testABC() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("abc".getBytes(), 0, 3);
         byte[] md = algorithm.digest();
         String exp = "900150983CD24FB0D6963F7D28E17F72";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testABC(): "+String.valueOf(x));
      }
   }

   public void testMessageDigest() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("message digest".getBytes(), 0, 14);
         byte[] md = algorithm.digest();
         String exp = "F96B697D7CB7938D525A2F31AAF161D0";
         assertTrue("testMessageDigest()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testMessageDigest(): "+String.valueOf(x));
      }
   }

   public void testAlphabet() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("abcdefghijklmnopqrstuvwxyz".getBytes(), 0, 26);
         byte[] md = algorithm.digest();
         String exp = "C3FCD3D76192E4007DFB496CCA67E13B";
         assertTrue("testAlphabet()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAlphabet(): "+String.valueOf(x));
      }
   }

   public void testAsciiSubset() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".getBytes(), 0, 62);
         byte[] md = algorithm.digest();
         String exp = "D174AB98D277D9F5A5611C2C9F419D9F";
         assertTrue("testAsciiSubset()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAsciiSubset(): "+String.valueOf(x));
      }
   }

   public void testEightyNumerics() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes(), 0, 80);
         byte[] md = algorithm.digest();
         String exp = "57EDF4A22BE3C955AC49DA2E2107B67A";
         assertTrue("testEightyNumerics()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testEightyNumerics(): "+String.valueOf(x));
      }
   }

   public void testCloning() {
      try {
         IMessageDigest algorithm = new MD5();
         algorithm.update("a".getBytes(), 0, 1);
         IMessageDigest clone = (IMessageDigest) algorithm.clone();
         byte[] md = algorithm.digest();
         String exp = "0CC175B9C0F1B6A831C399E269772661";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));

         clone.update("bc".getBytes(), 0, 2);
         md = clone.digest();
         exp = "900150983CD24FB0D6963F7D28E17F72";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testCloning(): "+String.valueOf(x));
      }
   }
}
