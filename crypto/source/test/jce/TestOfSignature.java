package test.jce;

// ----------------------------------------------------------------------------
// $Id: TestOfSignature.java,v 1.1 2002-01-18 02:26:48 raif Exp $
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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;

/**
 * Conformance tests for the JCE signature scheme implementations.<p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfSignature extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private String GNU = "GNU";

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfSignature(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfSignature.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /** Should fail with an unknown scheme. */
   public void testUnknownScheme() {
      try {
         Signature.getInstance("ABC", GNU);
         fail("testUnknownScheme()");
      } catch (Exception x) {
         assertTrue("testUnknownScheme()", true);
      }
   }

   public void testDSSRawSignature() throws Exception {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA", GNU);
      kpg.initialize(512);
      KeyPair kp = kpg.generateKeyPair();

      Signature alice = Signature.getInstance("DSA", GNU);
      Signature bob = (Signature) alice.clone();

      byte[] message = "1 if by land, 2 if by sea...".getBytes();

      alice.initSign(kp.getPrivate());
      alice.update(message);
      byte[] signature = alice.sign();

      bob.initVerify(kp.getPublic());
      bob.update(message);

      assertTrue("Verify own signature", bob.verify(signature));
   }

   public void testRSAPSSRawSignature() throws Exception {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", GNU);
      kpg.initialize(1024);
      KeyPair kp = kpg.generateKeyPair();

      Signature alice = Signature.getInstance("RSA-PSS", GNU);
      Signature bob = (Signature) alice.clone();

      byte[] message = "Que du magnifique...".getBytes();

      alice.initSign(kp.getPrivate());
      alice.update(message);
      byte[] signature = alice.sign();

      bob.initVerify(kp.getPublic());
      bob.update(message);

      assertTrue("Verify own signature", bob.verify(signature));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      Security.addProvider(new GnuCrypto()); // dynamically adds our provider
   }
}
