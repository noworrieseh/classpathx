package test.jce;

// ----------------------------------------------------------------------------
// $Id: TestOfKeyPairGenerator.java,v 1.1 2002-01-17 11:54:47 raif Exp $
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

import gnu.crypto.jce.GnuCrypto;
import gnu.crypto.util.Prime;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Conformance tests for the JCE keypair generation implementations.<p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfKeyPairGenerator extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private String GNU = "GNU";

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfKeyPairGenerator(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfKeyPairGenerator.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /** Should fail with an unknown algorithm. */
   public void testUnknownGenerator() {
      try {
         KeyPairGenerator.getInstance("ABC", GNU);
         fail("testUnknownGenerator()");
      } catch (Exception x) {
         assertTrue("testUnknownGenerator()", true);
      }
   }

   public void testDSAKeyPairGenerator() throws Exception {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA", GNU);

      // modulus length should be between 512, and 1024 in increments of 64
      try {
         kpg.initialize(530);
         fail("L should be <= 1024 and of the form 512 + 64n");
      } catch (IllegalArgumentException x) {
         assertTrue("L should be <= 1024 and of the form 512 + 64n", true);
      }

      kpg.initialize(512+64);
      KeyPair kp = kpg.generateKeyPair();

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

   public void testRSAKeyPairGenerator() throws Exception {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", GNU);

      // modulus length should be at least 1024
      try {
         kpg.initialize(1023);
         fail("L should be >= 1024");
      } catch (IllegalArgumentException x) {
         assertTrue("L should be >= 1024", true);
      }

      kpg.initialize(1024);
      KeyPair kp = kpg.generateKeyPair();

      BigInteger n1 = ((RSAPublicKey) kp.getPublic()).getModulus();

      BigInteger n2 = ((RSAPrivateKey)    kp.getPrivate()).getModulus();
      BigInteger p =  ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeP();
      BigInteger q =  ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeQ();

      assertTrue("n1 == pq", n1.equals(p.multiply(q)));
      assertTrue("n2 == pq", n2.equals(p.multiply(q)));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      Security.addProvider(new GnuCrypto()); // dynamically adds our provider
   }
}
