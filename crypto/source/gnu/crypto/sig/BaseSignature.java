package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: BaseSignature.java,v 1.1 2002-01-21 10:08:17 raif Exp $
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
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

/**
 * A base abstract class to facilitate implementations of concrete Signatures.<p>
 *
 * @version $Revision: 1.1 $
 */
public abstract class BaseSignature implements ISignature {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name of this signature scheme. */
   protected String schemeName;

   /** The underlying message digest instance for this signature scheme. */
   protected IMessageDigest md;

   /** The public key to use when verifying signatures. */
   protected PublicKey publicKey;

   /** The private key to use when generating signatures (signing). */
   protected PrivateKey privateKey;

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

   public void setupVerify(PublicKey key) throws IllegalArgumentException {
      init();
      setupForVerification(key);
   }

   public void setupSign(PrivateKey key) throws IllegalArgumentException {
      init();
      setupForSigning(key);
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
      publicKey = null;
      privateKey = null;
   }
}
