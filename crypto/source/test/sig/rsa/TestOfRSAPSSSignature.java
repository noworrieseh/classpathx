package test.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: TestOfRSAPSSSignature.java,v 1.1 2002-01-11 22:03:28 raif Exp $
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

import gnu.crypto.hash.HashFactory;
import gnu.crypto.sig.rsa.GnuRSAPrivateKey;
import gnu.crypto.sig.rsa.GnuRSAPublicKey;
import gnu.crypto.sig.rsa.RSAKeyPairGenerator;
import gnu.crypto.sig.rsa.RSAPSSSignature;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * Conformance tests for the RSA-PSS signature generation/verification
 * implementation.
 *
 * @version $Revision: 1.1 $
 */
public class TestOfRSAPSSSignature extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
   private RSAPublicKey publicK;
   private RSAPrivateKey privateK;
   private RSAPSSSignature alice, bob;
   private byte[] message;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfRSAPSSSignature(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfRSAPSSSignature.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSigWithDefaults() {
      alice = new RSAPSSSignature(); // SHA + 0-octet salt
      bob = (RSAPSSSignature) alice.clone();

      message = "1 if by land, 2 if by sea...".getBytes();

      alice.setupSign(privateK);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      bob.setupVerify(publicK);
      bob.update(message, 0, message.length);

      assertTrue("testSigWithDefaults()", bob.verify(signature));
   }

   public void testSigWithShaSalt16() {
      alice = new RSAPSSSignature(HashFactory.SHA1_HASH, 16);
      bob = (RSAPSSSignature) alice.clone();

      message = "Que du magnifique...".getBytes();

      alice.setupSign(privateK);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      bob.setupVerify(publicK);
      bob.update(message, 0, message.length);

      assertTrue("testSigWithShaSalt16()", bob.verify(signature));
   }

   public void testSigWithRipeMD160Salt8() {
      alice = new RSAPSSSignature(HashFactory.RIPEMD160_HASH, 8);
      bob = (RSAPSSSignature) alice.clone();

      message = "abcdefghijklmnopqrstuvwxyz0123456789".getBytes();

      alice.setupSign(privateK);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      bob.setupVerify(publicK);
      bob.update(message, 0, message.length);

      assertTrue("testSigWithRipeMD160Salt8()", bob.verify(signature));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      kpg.setup(new HashMap()); // default is to use 1024-bit keys
      KeyPair kp = kpg.generate();
      publicK = (RSAPublicKey) kp.getPublic();
      privateK = (RSAPrivateKey) kp.getPrivate();
   }
}
