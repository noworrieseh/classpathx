package test.sig.dss;

// ----------------------------------------------------------------------------
// $Id: TestOfDSSSignature.java,v 1.3 2002-01-28 01:43:23 raif Exp $
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

import gnu.crypto.sig.BaseSignature;
import gnu.crypto.sig.dss.DSSKeyPairGenerator;
import gnu.crypto.sig.dss.DSSPrivateKey;
import gnu.crypto.sig.dss.DSSPublicKey;
import gnu.crypto.sig.dss.DSSSignature;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.HashMap;

/**
 * Conformance tests for the DSS signature generation/verification
 * implementation.
 *
 * @version $Revision: 1.3 $
 */
public class TestOfDSSSignature extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfDSSSignature(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfDSSSignature.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

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
      DSSSignature bob = bob = (DSSSignature) alice.clone();

      byte[] message = "1 if by land, 2 if by sea...".getBytes();

      map.put(BaseSignature.SIGNER_KEY, privateK);
      alice.setupSign(map);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      map.put(BaseSignature.VERIFIER_KEY, publicK);
      bob.setupVerify(map);
      bob.update(message, 0, message.length);

      assertTrue("Verify own signature", bob.verify(signature));
   }
}
