package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: BaseCipher.java,v 1.1.1.1 2001-11-20 13:40:25 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU Library General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
// details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; see the file COPYING. If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
// ----------------------------------------------------------------------------

import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.Map;

/**
 * A basic abstract class to facilitate implementing symmetric block ciphers.
 *
 * @version $Revision: 1.1.1.1 $
 */
public abstract class BaseCipher implements IBlockCipher {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of the cipher. */
   protected String name;

   /** The default block size, in bytes. */
   protected int defaultBlockSize;

   /** The default key size, in bytes. */
   protected int defaultKeySize;

   /** The current block size, in bytes. */
   protected int currentBlockSize;

   /** The session key for this instance. */
   protected transient Object currentKey;

   /** The instance lock. */
   protected Object lock = new Object();

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial constructor for use by concrete subclasses.
    *
    * @param defaultBlockSize the default block size in bytes.
    * @param defaultKeySize the default key size in bytes.
    */
   protected BaseCipher(String name, int defaultBlockSize, int defaultKeySize) {
      super();

      this.name = name;
      this.defaultBlockSize = defaultBlockSize;
      this.defaultKeySize = defaultKeySize;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // IBlockCipher interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      StringBuffer sb = new StringBuffer(name).append('-');
      if (currentKey == null) {
         sb.append(String.valueOf(8*defaultBlockSize));
      } else {
         sb.append(String.valueOf(8*currentBlockSize));
      }
      return sb.toString();
   }

   public int defaultBlockSize() {
      return defaultBlockSize;
   }

   public int defaultKeySize() {
      return defaultKeySize;
   }

   public void init(Map attributes) throws InvalidKeyException {
      synchronized(lock) {
         if (currentKey != null) {
            throw new IllegalStateException();
         }

         Integer bs = (Integer) attributes.get(CIPHER_BLOCK_SIZE);
         currentBlockSize = (bs == null ? defaultBlockSize : bs.intValue());

         byte[] k = (byte[]) attributes.get(KEY_MATERIAL);
         currentKey = makeKey(k, currentBlockSize);
      }
   }

   public int currentBlockSize() {
      if (currentKey == null) {
         throw new IllegalStateException();
      }
      return currentBlockSize;
   }

   public void reset() {
      synchronized(lock) {
         currentBlockSize = 0;
         currentKey = null;
      }
   }

   public void encryptBlock(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException {
      synchronized(lock) {
         if (currentKey == null) {
            throw new IllegalStateException();
         }

         encrypt(in, inOffset, out, outOffset, currentKey, currentBlockSize);
      }
   }

   public void decryptBlock(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException {
      synchronized(lock) {
         if (currentKey == null) {
            throw new IllegalStateException();
         }

         decrypt(in, inOffset, out, outOffset, currentKey, currentBlockSize);
      }
   }

   public boolean selfTest() {
      int ks;
      Iterator bit;
      for (Iterator kit = keySizes(); kit.hasNext(); ) {
         ks = ((Integer) kit.next()).intValue();
         for (bit = blockSizes(); bit.hasNext(); ) {
            if (!testSymmetry(ks, ((Integer) bit.next()).intValue())) {
               return false;
            }
         }
      }

      return true;
   }

   // methods to be implemented by concrete subclasses
   // -------------------------------------------------------------------------

   public abstract Iterator blockSizes();

   public abstract Iterator keySizes();

   /**
    * Expands a user-supplied key material into a session key for a designated
    * <i>block size</i>.
    *
    * @param k the user-supplied key material.
    * @param bs the desired block size in bytes.
    * @return an Object encapsulating the session key.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception InvalidKeyException if the key data is invalid.
    */
   public abstract Object makeKey(byte[]k, int bs) throws InvalidKeyException;

   /**
    * Encrypts exactly one block of plaintext.
    *
    * @param in the plaintext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the ciphertext.
    * @param outOffset index of <i>out</i> from which to store the result.
    * @param k the session key to use.
    * @param bs the block size to use.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception ArrayIndexOutOfBoundsException if there is not enough room in
    * either the plaintext or ciphertext buffers.
    */
   public abstract void
   encrypt(byte[] in, int inOffset, byte[] out, int outOffset, Object k, int bs);

   /**
    * Decrypts exactly one block of plaintext.
    *
    * @param in the ciphertext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the plaintext.
    * @param outOffset index of <i>out</i> from which to store the result.
    * @param k the session key to use.
    * @param bs the block size to use.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception ArrayIndexOutOfBoundsException if there is not enough room in
    * either the plaintext or ciphertext buffers.
    */
   public abstract void
   decrypt(byte[] in, int inOffset, byte[] out, int outOffset, Object k, int bs);

   // own methods
   // -------------------------------------------------------------------------

   private boolean testSymmetry(int ks, int bs) {
      try {
         byte[] kb = new byte[ks];
         byte[] pt = new byte[bs];
         byte[] ct = new byte[bs];
         byte[] cpt = new byte[bs];
         int i;
         for (i = 0; i < ks; i++) {
            kb[i] = (byte) i;
         }
         for (i = 0; i < bs; i++) {
            pt[i] = (byte) i;
         }

         Object k = makeKey(kb, bs);
         encrypt(pt, 0, ct,  0, k, bs);
         decrypt(ct, 0, cpt, 0, k, bs);

         return Util.areEqual(pt, cpt);

      } catch (Exception x) {
         x.printStackTrace(System.err);
         return false;
      }
   }
}
