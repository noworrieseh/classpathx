package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: BaseSignature.java,v 1.2 2002-01-28 01:43:23 raif Exp $
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

import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.sig.ISignature;
import gnu.crypto.util.PRNG;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Map;

/**
 * A base abstract class to facilitate implementations of concrete Signatures.<p>
 *
 * @version $Revision: 1.2 $
 */
public abstract class BaseSignature implements ISignature {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Property name of the verifier's public key. */
   public static final String VERIFIER_KEY = "gnu.crypto.sig.public.key";

   /** Property name of the signer's private key. */
   public static final String SIGNER_KEY = "gnu.crypto.sig.private.key";

   /**
    * Property name of an optional {@link java.security.SecureRandom} instance
    * to use. The default is to use a classloader singleton from
    * {@link gnu.crypto.util.PRNG}.
    */
   public static final String SOURCE_OF_RANDOMNESS = "gnu.crypto.sig.prng";

   /** The canonical name of this signature scheme. */
   protected String schemeName;

   /** The underlying message digest instance for this signature scheme. */
   protected IMessageDigest md;

   /** The public key to use when verifying signatures. */
   protected PublicKey publicKey;

   /** The private key to use when generating signatures (signing). */
   protected PrivateKey privateKey;

   /** The optional {@link java.security.SecureRandom} instance to use. */
   private SecureRandom rnd;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial constructor.<p>
    *
    * @param schemeName the name of this signature scheme.
    * @param md the underlying instance of the message digest algorithm.
    */
   protected BaseSignature(String schemeName, IMessageDigest md) {
      super();

      this.schemeName = schemeName;
      this.md = md;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // gnu.crypto.sig.ISignature interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      return schemeName;
   }

   public void setupVerify(Map attributes) throws IllegalArgumentException {
      init();

      // do we have a SecureRandom, or should we use our own?
      rnd = (SecureRandom) attributes.get(SOURCE_OF_RANDOMNESS);
      // do we have a public key?
      PublicKey key = (PublicKey) attributes.get(VERIFIER_KEY);
      if (key != null) {
         setupForVerification(key);
      }
   }

   public void setupSign(Map attributes) throws IllegalArgumentException {
      init();

      // do we have a SecureRandom, or should we use our own?
      rnd = (SecureRandom) attributes.get(SOURCE_OF_RANDOMNESS);
      // do we have a private key?
      PrivateKey key = (PrivateKey) attributes.get(SIGNER_KEY);
      if (key != null) {
         setupForSigning(key);
      }
   }

   public void update(byte b) {
      if (md == null) {
         throw new IllegalStateException();
      }
      md.update(b);
   }

   public void update(byte[] b, int off, int len) {
      if (md == null) {
         throw new IllegalStateException();
      }
      md.update(b, off, len);
   }

   public Object sign() {
      if (md == null || privateKey == null) {
         throw new IllegalStateException();
      }

      return generateSignature();
   }

   public boolean verify(Object sig) {
      if (md == null || publicKey == null) {
         throw new IllegalStateException();
      }

      return verifySignature(sig);
   }

   // abstract methods to be implemented by concrete subclasses
   // -------------------------------------------------------------------------

   public abstract Object clone();

   protected abstract void setupForVerification(PublicKey key)
   throws IllegalArgumentException;

   protected abstract void setupForSigning(PrivateKey key)
   throws IllegalArgumentException;

   protected abstract Object generateSignature()
   throws IllegalStateException;

   protected abstract boolean verifySignature(Object signature)
   throws IllegalStateException;

   // Other instance methods
   // -------------------------------------------------------------------------

   /** Initialises the internal fields of this instance. */
   protected void init() {
      md.reset();
      rnd = null;
      publicKey = null;
      privateKey = null;
   }

   /**
    * Fills the designated byte array with random data.
    *
    * @param buffer the byte array to fill with random data.
    */
   protected void nextRandomBytes(byte[] buffer) {
      if (rnd != null) {
         rnd.nextBytes(buffer);
      } else {
         PRNG.nextBytes(buffer);
      }
   }
}
