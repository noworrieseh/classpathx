package test.sig.dss;

// ----------------------------------------------------------------------------
// $Id: TestOfDSS.java,v 1.1 2001-12-30 16:01:58 raif Exp $
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

import gnu.crypto.sig.IKeyPairCodec;
import gnu.crypto.sig.dss.DSSKeyPairGenerator;
import gnu.crypto.sig.dss.DSSKeyPairRawCodec;
import gnu.crypto.sig.dss.DSSPrivateKey;
import gnu.crypto.sig.dss.DSSPublicKey;
import gnu.crypto.sig.dss.DSSSignature;
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
 * Conformance tests for the DSS implementation.
 *
 * @version $Revision: 1.1 $
 */
public class TestOfDSS extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfDSS(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfDSS.class);
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

      boolean isGnuPrime = 
      		!Prime.hasSmallPrimeDivisor(q1)
            && Prime.passEulerCriterion(q1)
            && Prime.passMillerRabin(q1);
      assertTrue("q is probable prime", isGnuPrime);

      if (isGnuPrime != q1.isProbablePrime(100)) {
         notifyMaintainer(q1, isGnuPrime);
      }

      isGnuPrime = 
      		!Prime.hasSmallPrimeDivisor(p1)
            && Prime.passEulerCriterion(p1)
            && Prime.passMillerRabin(p1);
      assertTrue("p is probable prime", isGnuPrime);

      if (isGnuPrime != p1.isProbablePrime(100)) {
         notifyMaintainer(p1, isGnuPrime);
      }
   }

   public void testKeyPairCodec() {
      DSSKeyPairGenerator kpg = new DSSKeyPairGenerator();
      HashMap map = new HashMap();      
      map.put(DSSKeyPairGenerator.MODULUS_LENGTH, new Integer(512));
      map.put(DSSKeyPairGenerator.USE_DEFAULTS, new Boolean(false));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      DSAPublicKey pubK = (DSAPublicKey) kp.getPublic();
      DSAPrivateKey secK = (DSAPrivateKey) kp.getPrivate();

      byte[] pk1, pk2;
      // try an invalid format ID
      try {
         pk1 = ((DSSPublicKey) pubK).getEncoded(0);
         fail("Succeeded with unknown format ID");
      } catch (IllegalArgumentException x) {
         assertTrue("Recognised unknown format ID", true);
      }

      pk1 = ((DSSPublicKey) pubK).getEncoded(IKeyPairCodec.RAW_FORMAT);
      pk2 = ((DSSPrivateKey) secK).getEncoded(IKeyPairCodec.RAW_FORMAT);
      
      IKeyPairCodec codec = new DSSKeyPairRawCodec();
      PublicKey newPubK = codec.decodePublicKey(pk1);
      PrivateKey newSecK = codec.decodePrivateKey(pk2);

      assertTrue("DSS public key Raw encoder/decoder test", pubK.equals(newPubK));
      assertTrue("DSS private key Raw encoder/decoder test", secK.equals(newSecK));
   }

   public void testSignature() {
      DSSKeyPairGenerator kpg = new DSSKeyPairGenerator();
      HashMap map = new HashMap();      
      map.put(DSSKeyPairGenerator.MODULUS_LENGTH, new Integer(512));
      map.put(DSSKeyPairGenerator.USE_DEFAULTS, new Boolean(false));
      kpg.setup(map);
      KeyPair kp = kpg.generate();

      DSAPublicKey publicK = (DSAPublicKey) kp.getPublic();
      DSAPrivateKey privateK = (DSAPrivateKey) kp.getPrivate();

      DSSSignature alice = new DSSSignature();
      DSSSignature bob = (DSSSignature) alice.clone();

      byte[] message = "1 if by land, 2 if by sea...".getBytes();

      alice.setupSign(privateK);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      bob.setupVerify(publicK);
      bob.update(message, 0, message.length);

      assertTrue("Verify own signature", bob.verify(signature));
   }

   // helper methods
   // -------------------------------------------------------------------------

   private static void notifyMaintainer(BigInteger n, boolean isGnuPrime) {
      System.err.println("This library and the JDK disagree on whether 0x"
      	+n.toString(16)+" is a prime or not.");
      System.err.println("While this library claims it is"
      	+(isGnuPrime ? "" : " not")+", the JDK claims the opposite.");
      System.err.println("Please contact the maintainer of this library, and "
      	+"provide this message for further investigation. TIA");
   }
}
