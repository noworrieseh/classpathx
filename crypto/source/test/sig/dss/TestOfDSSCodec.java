package test.sig.dss;

// ----------------------------------------------------------------------------
// $Id: TestOfDSSCodec.java,v 1.2 2002-01-11 22:10:35 raif Exp $
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
import gnu.crypto.sig.ISignatureCodec;
import gnu.crypto.sig.dss.DSSKeyPairGenerator;
import gnu.crypto.sig.dss.DSSKeyPairRawCodec;
import gnu.crypto.sig.dss.DSSPrivateKey;
import gnu.crypto.sig.dss.DSSPublicKey;
import gnu.crypto.sig.dss.DSSSignatureRawCodec;
import gnu.crypto.sig.dss.DSSSignature;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.HashMap;

/**
 * Conformance tests for the DSS key/signature format encoding/decoding
 * implementation.
 *
 * @version $Revision: 1.2 $
 */
public class TestOfDSSCodec extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private DSSKeyPairGenerator kpg = new DSSKeyPairGenerator();
   private KeyPair kp;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfDSSCodec(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfDSSCodec.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testKeyPairRawCodec() {
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

   public void testSignatureRawCodec() {
      DSAPublicKey publicK = (DSAPublicKey) kp.getPublic();
      DSAPrivateKey privateK = (DSAPrivateKey) kp.getPrivate();

      DSSSignature alice = new DSSSignature();
      DSSSignature bob = (DSSSignature) alice.clone();

      byte[] message = "1 if by land, 2 if by sea...".getBytes();

      alice.setupSign(privateK);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      ISignatureCodec codec = new DSSSignatureRawCodec();

      byte[] encodedSignature = codec.encodeSignature(signature);
      Object decodedSignature = codec.decodeSignature(encodedSignature);

      bob.setupVerify(publicK);
      bob.update(message, 0, message.length);

      assertTrue("Signature Raw encoder/decoder test", bob.verify(decodedSignature));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      HashMap map = new HashMap();
      map.put(DSSKeyPairGenerator.MODULUS_LENGTH, new Integer(512));
      map.put(DSSKeyPairGenerator.USE_DEFAULTS, new Boolean(false));

      kpg.setup(map);
      kp = kpg.generate();
   }
}
