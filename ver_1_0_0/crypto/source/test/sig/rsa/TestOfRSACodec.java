package test.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: TestOfRSACodec.java,v 1.2 2002-01-28 01:43:23 raif Exp $
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
import gnu.crypto.sig.IKeyPairCodec;
import gnu.crypto.sig.ISignatureCodec;
import gnu.crypto.sig.rsa.GnuRSAPrivateKey;
import gnu.crypto.sig.rsa.GnuRSAPublicKey;
import gnu.crypto.sig.rsa.RSAKeyPairGenerator;
import gnu.crypto.sig.rsa.RSAKeyPairRawCodec;
import gnu.crypto.sig.rsa.RSAPSSSignature;
import gnu.crypto.sig.rsa.RSAPSSSignatureRawCodec;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * Conformance tests for the RSA key/signature format encoding/decoding
 * implementation.
 *
 * @version $Revision: 1.2 $
 */
public class TestOfRSACodec extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
   private RSAPublicKey pubK;
   private RSAPrivateKey secK;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfRSACodec(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfRSACodec.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testKeyPairRawCodec() {
      byte[] pk1, pk2;
      try { // an invalid format ID
         pk1 = ((GnuRSAPublicKey) pubK).getEncoded(0);
         fail("Succeeded with unknown format ID");
      } catch (IllegalArgumentException x) {
         assertTrue("Recognised unknown format ID", true);
      }

      pk1 = ((GnuRSAPublicKey) pubK).getEncoded(IKeyPairCodec.RAW_FORMAT);
      pk2 = ((GnuRSAPrivateKey) secK).getEncoded(IKeyPairCodec.RAW_FORMAT);

      IKeyPairCodec codec = new RSAKeyPairRawCodec();
      PublicKey newPubK = codec.decodePublicKey(pk1);
      PrivateKey newSecK = codec.decodePrivateKey(pk2);

      assertTrue("RSA public key Raw encoder/decoder test",
            pubK.equals(newPubK));
      assertTrue("RSA private key Raw encoder/decoder test",
            secK.equals(newSecK));
   }

   public void testSignatureRawCodec() {
      RSAPSSSignature alice = new RSAPSSSignature();
      RSAPSSSignature bob = (RSAPSSSignature) alice.clone();

      byte[] message = "1 if by land, 2 if by sea...".getBytes();

      HashMap map = new HashMap();
      map.put(BaseSignature.SIGNER_KEY, secK);
      alice.setupSign(map);
      alice.update(message, 0, message.length);
      Object signature = alice.sign();

      ISignatureCodec codec = new RSAPSSSignatureRawCodec();

      byte[] encodedSignature = codec.encodeSignature(signature);
      Object decodedSignature = codec.decodeSignature(encodedSignature);

      map.put(BaseSignature.VERIFIER_KEY, pubK);
      bob.setupVerify(map);
      bob.update(message, 0, message.length);

      assertTrue("RSA-PSS signature Raw encoder/decoder test",
            bob.verify(decodedSignature));
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      HashMap map = new HashMap();
      map.put(RSAKeyPairGenerator.MODULUS_LENGTH, new Integer(1024));

      kpg.setup(map);
      KeyPair kp = kpg.generate();
      pubK = (RSAPublicKey) kp.getPublic();
      secK = (RSAPrivateKey) kp.getPrivate();
   }
}
