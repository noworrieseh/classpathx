package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfAnubis.java,v 1.1.1.1 2001-11-20 13:40:44 raif Exp $
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

import gnu.crypto.cipher.Anubis;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;

/**
 * Conformance tests for the Anubis implementation.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class TestOfAnubis extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfAnubis(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfAnubis.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
         IBlockCipher algorithm = new Anubis();
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }
}
