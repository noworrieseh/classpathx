package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfRipeMD160.java,v 1.3 2001-12-08 21:28:49 raif Exp $
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
import gnu.crypto.hash.RipeMD160;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the RipeMD160 implementation.
 *
 * @version $Revision: 1.3 $
 */
public class TestOfRipeMD160 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfRipeMD160(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfRipeMD160.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void testA() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         algorithm.update("a".getBytes(), 0, 1);
         byte[] md = algorithm.digest();
         String exp = "0BDC9D2D256B3EE9DAAE347BE6F4DC835A467FFE";
         assertTrue("testA()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testA(): "+String.valueOf(x));
      }
   }

   public void testABC() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         algorithm.update("abc".getBytes(), 0, 3);
         byte[] md = algorithm.digest();
         String exp = "8EB208F7E05D987A9B044A8E98C6B087F15A0BFC";
         assertTrue("testABC()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testABC(): "+String.valueOf(x));
      }
   }

   public void testMessageDigest() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         algorithm.update("message digest".getBytes(), 0, 14);
         byte[] md = algorithm.digest();
         String exp = "5D0689EF49D2FAE572B881B123A85FFA21595F36";
         assertTrue("testMessageDigest()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testMessageDigest(): "+String.valueOf(x));
      }
   }

   public void testAlphabet() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         algorithm.update("abcdefghijklmnopqrstuvwxyz".getBytes(), 0, 26);
         byte[] md = algorithm.digest();
         String exp = "F71C27109C692C1B56BBDCEB5B9D2865B3708DBC";
         assertTrue("testAlphabet()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testAlphabet(): "+String.valueOf(x));
      }
   }

   public void testCloning() {
      try {
         IMessageDigest algorithm = new RipeMD160();
         algorithm.update("a".getBytes(), 0, 1);
         IMessageDigest clone = (IMessageDigest) algorithm.clone();
         byte[] md = algorithm.digest();
         String exp = "0BDC9D2D256B3EE9DAAE347BE6F4DC835A467FFE";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));

         clone.update("bc".getBytes(), 0, 2);
         md = clone.digest();
         exp = "8EB208F7E05D987A9B044A8E98C6B087F15A0BFC";
         assertTrue("testCloning()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("testCloning(): "+String.valueOf(x));
      }
   }
}
