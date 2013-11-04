package test.hash;

// ----------------------------------------------------------------------------
// $Id: TestOfWhirlpool.java,v 1.1.1.1 2001-11-20 13:40:46 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU Library General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
// details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; see the file COPYING. If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
// ----------------------------------------------------------------------------

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.Whirlpool;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the Whirlpool implementation.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class TestOfWhirlpool extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfWhirlpool(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfWhirlpool.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IMessageDigest algorithm = new Whirlpool();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   public void test8ZeroBits() {
      try {
         IMessageDigest algorithm = new Whirlpool();
         algorithm.update((byte) 0x00);
         byte[] md = algorithm.digest();
         String exp = "EBAA1DF2E97113BE187EB0303C660F6E643E2C090EF2CDA9A2EA6DCF5002147D1D0E1E9D996E879CEF9D26896630A5DB3308D5A0DC235B199C38923BE2259E03";
         assertTrue("test8ZeroBits()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("test8ZeroBits(): "+String.valueOf(x));
      }
   }

   public void test16ZeroBits() {
      try {
         IMessageDigest algorithm = new Whirlpool();
         algorithm.update((byte) 0x00);
         algorithm.update((byte) 0x00);
         byte[] md = algorithm.digest();
         String exp = "5777FC1F8467A1C004CD9130439403CCDAA9FDC86092D9CFFE339E6008612374D04C8FC0C724707FEAE6F7CEB1E030CABF652A673DA1849B02654AF76EEE24A7";
         assertTrue("test16ZeroBits()", exp.equals(Util.toString(md)));
      } catch (Exception x) {
         fail("test16ZeroBits(): "+String.valueOf(x));
      }
   }
}
