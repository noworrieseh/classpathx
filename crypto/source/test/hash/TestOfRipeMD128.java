package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfRipeMD128.java,v 1.3 2001-12-08 21:27:34 raif Exp $
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
import gnu.crypto.hash.RipeMD128;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the RipeMD128 implementation.
 *
 * @version $Revision: 1.3 $
 */
public class TestOfRipeMD128 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfRipeMD128(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfRipeMD128.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void testA() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         algorithm.update("a".getBytes(), 0, 1);
         byte[] md = algorithm.digest();
         String exp = "86BE7AFA339D0FC7CFC785E72F578D33";
         assertTrue("testA()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testA(): "+String.valueOf(x));
      }
   }

   public void testABC() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         algorithm.update("abc".getBytes(), 0, 3);
         byte[] md = algorithm.digest();
         String exp = "C14A12199C66E4BA84636B0F69144C77";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testABC(): "+String.valueOf(x));
      }
   }

   public void testMessageDigest() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         algorithm.update("message digest".getBytes(), 0, 14);
         byte[] md = algorithm.digest();
         String exp = "9E327B3D6E523062AFC1132D7DF9D1B8";
         assertTrue("testMessageDigest()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testMessageDigest(): "+String.valueOf(x));
      }
   }

   public void testAlphabet() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         algorithm.update("abcdefghijklmnopqrstuvwxyz".getBytes(), 0, 26);
         byte[] md = algorithm.digest();
         String exp = "FD2AA607F71DC8F510714922B371834E";
         assertTrue("testAlphabet()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAlphabet(): "+String.valueOf(x));
      }
   }

   public void testCloning() {
      try {
         IMessageDigest algorithm = new RipeMD128();
         algorithm.update("a".getBytes(), 0, 1);
         IMessageDigest clone = (IMessageDigest) algorithm.clone();
         byte[] md = algorithm.digest();
         String exp = "86BE7AFA339D0FC7CFC785E72F578D33";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));

         clone.update("bc".getBytes(), 0, 2);
         md = clone.digest();
         exp = "C14A12199C66E4BA84636B0F69144C77";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testCloning(): "+String.valueOf(x));
      }
   }
}
