package test.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: TestOfRSAKeyGeneration.java,v 1.1 2002-01-11 22:03:28 raif Exp $
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

import gnu.crypto.sig.rsa.GnuRSAPrivateKey;
import gnu.crypto.sig.rsa.RSA;
import gnu.crypto.sig.rsa.RSAKeyPairGenerator;
import gnu.crypto.util.Prime;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Random;

/**
 * Conformance tests for the RSA key-pair generation implementation.
 *
 * @version $Revision: 1.1 $
 */
public class TestOfRSAKeyGeneration extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final BigInteger ZERO = BigInteger.ZERO;
   private static final BigInteger ONE = BigInteger.ONE;

   private RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
   private HashMap map = new HashMap();

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfRSAKeyGeneration(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfRSAKeyGeneration.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testKeyPairGeneration() {
      map.put(RSAKeyPairGenerator.MODULUS_LENGTH, new Integer(530));
      try {
         kpg.setup(map);
         fail("L should be >= 1024");
      } catch (IllegalArgumentException x) {
         assertTrue("L should be >= 1024", true);
      }

      map.put(RSAKeyPairGenerator.MODULUS_LENGTH, new Integer(1024));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      BigInteger n1 = ((RSAPublicKey) kp.getPublic()).getModulus();

      BigInteger n2 = ((RSAPrivateKey)    kp.getPrivate()).getModulus();
      BigInteger p =  ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeP();
      BigInteger q =  ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeQ();

      assertTrue("n1 == pq", n1.equals(p.multiply(q)));
      assertTrue("n2 == pq", n1.equals(p.multiply(q)));
   }

   public void testRSAParams() {
      map.put(RSAKeyPairGenerator.MODULUS_LENGTH, new Integer(1024));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      BigInteger n1 = ((RSAPublicKey) kp.getPublic()).getModulus();
      BigInteger e =  ((RSAPublicKey) kp.getPublic()).getPublicExponent();

      BigInteger n2 = ((RSAPrivateKey) kp.getPrivate()).getModulus();
      BigInteger d =  ((RSAPrivateKey) kp.getPrivate()).getPrivateExponent();

      BigInteger p =    ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeP();
      BigInteger q =    ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeQ();
      BigInteger dP =   ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeExponentP();
      BigInteger dQ =   ((RSAPrivateCrtKey) kp.getPrivate()).getPrimeExponentQ();
      BigInteger qInv = ((RSAPrivateCrtKey) kp.getPrivate()).getCrtCoefficient();

      assertTrue("n1 is a 1024-bit MPI", n1.bitLength() == 1024);
      assertTrue("n2 is a 1024-bit MPI", n2.bitLength() == 1024);
      assertTrue("n1 == n2", n1.equals(n2));

      // In a valid RSA private key with this representation, the two factors p
      // and q are the prime factors of the modulus n,
      assertTrue("p is prime", Prime.isProbablePrime(p, true));
      assertTrue("q is prime", Prime.isProbablePrime(q, true));
      assertTrue("n == pq", n1.equals(p.multiply(q)));

      // dP and dQ are positive integers less than p and q respectively
      BigInteger p_minus_1 = p.subtract(ONE);
      BigInteger q_minus_1 = q.subtract(ONE);
      assertTrue("0 < dP < p-1",
            ZERO.compareTo(dP) < 0 && dP.compareTo(p_minus_1) < 0);
      assertTrue("0 < dQ < q-1",
            ZERO.compareTo(dQ) < 0 && dQ.compareTo(q_minus_1) < 0);
      // satisfying
      //    e · dP = 1 (mod p?1);
      //    e · dQ = 1 (mod q?1),
      assertTrue("e·dP == 1 (mod p?1)", e.multiply(dP).mod(p_minus_1).equals(ONE));
      assertTrue("e·dQ == 1 (mod q?1)", e.multiply(dQ).mod(q_minus_1).equals(ONE));

      // and the CRT coefficient qInv is a positive integer less than p
      // satisfying
      //    q · qInv = 1 (mod p).
      assertTrue("q·qInv == 1 (mod p)", q.multiply(qInv).mod(p).equals(ONE));

      BigInteger phi = p_minus_1.multiply(q_minus_1);
      assertTrue("gcd(e, phi) == 1", e.gcd(phi).equals(ONE));
      assertTrue("e.d == 1 (mod phi)", e.multiply(d).mod(phi).equals(ONE));
   }

   public void testRSAPrimitives() {
      map.put(RSAKeyPairGenerator.MODULUS_LENGTH, new Integer(1024));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      PublicKey pubK =   kp.getPublic();
      PrivateKey privK = kp.getPrivate();

      BigInteger n = ((RSAPublicKey) pubK).getModulus();
      BigInteger m = ZERO;
      Random prng = new Random(System.currentTimeMillis());
      while (m.equals(ZERO) || m.compareTo(n) >= 0) {
         m = new BigInteger(1024, prng);
      }
      BigInteger s = RSA.sign(privK, m);
      BigInteger cm = RSA.verify(pubK, s);

      assertTrue("cm == m", cm.equals(m));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      kpg = new RSAKeyPairGenerator();
      map = new HashMap();
   }
}
