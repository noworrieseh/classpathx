package test.sig.dss;

// ----------------------------------------------------------------------------
// $Id: TestOfDSSKeyGeneration.java,v 1.2 2002-01-11 22:10:35 raif Exp $
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

import gnu.crypto.sig.dss.DSSKeyPairGenerator;
import gnu.crypto.util.Prime;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.HashMap;

/**
 * Conformance tests for the DSS key-pair generation implementation.
 *
 * @version $Revision: 1.2 $
 */
public class TestOfDSSKeyGeneration extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfDSSKeyGeneration(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfDSSKeyGeneration.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testKeyPairGeneration() {
      DSSKeyPairGenerator kpg = new DSSKeyPairGenerator();
      HashMap map = new HashMap();
      map.put(DSSKeyPairGenerator.MODULUS_LENGTH, new Integer(530));

      try {
         kpg.setup(map);
         fail("L should be <= 1024 and of the form 512 + 64n");
      } catch (IllegalArgumentException x) {
         assertTrue("L should be <= 1024 and of the form 512 + 64n", true);
      }

      map.put(DSSKeyPairGenerator.MODULUS_LENGTH, new Integer(512));
      map.put(DSSKeyPairGenerator.USE_DEFAULTS, new Boolean(false));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      BigInteger p1 = ((DSAPublicKey) kp.getPublic()).getParams().getP();
      BigInteger p2 = ((DSAPrivateKey) kp.getPrivate()).getParams().getP();
      assertTrue("p1.equals(p2)", p1.equals(p2));

      BigInteger q1 = ((DSAPublicKey) kp.getPublic()).getParams().getQ();
      BigInteger q2 = ((DSAPrivateKey) kp.getPrivate()).getParams().getQ();
      assertTrue("q1.equals(q2)", q1.equals(q2));

      BigInteger g1 = ((DSAPublicKey) kp.getPublic()).getParams().getG();
      BigInteger g2 = ((DSAPrivateKey) kp.getPrivate()).getParams().getG();
      assertTrue("g1.equals(g2)", g1.equals(g2));

      assertTrue("q is probable prime", Prime.isProbablePrime(q1, true));
      assertTrue("p is probable prime", Prime.isProbablePrime(p1, true));
   }
}
