package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: BaseCipher.java,v 1.7 2002-07-06 23:37:04 raif Exp $
//
// Copyright (C) 2001-2002, Free Software Foundation, Inc.
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

import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A basic abstract class to facilitate implementing symmetric key block
 * ciphers.</p>
 *
 * @version $Revision: 1.7 $
 */
public abstract class BaseCipher implements IBlockCipher, IBlockCipherSpi {

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
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name prefix of this instance.
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

   // Instance methods
   // -------------------------------------------------------------------------

   // IBlockCipher interface implementation -----------------------------------

   public abstract Object clone();

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
         if (bs == null) { // no block size was specified.
            if (currentBlockSize == 0) { // happy birthday
               currentBlockSize = defaultBlockSize;
            } // else it's a clone. use as is
         } else {
            currentBlockSize = bs.intValue();
            // ensure that value is valid
            Iterator it;
            boolean ok = false;
            for (it = blockSizes(); it.hasNext(); ) {
               ok = (currentBlockSize == ((Integer) it.next()).intValue());
               if (ok) {
                  break;
               }
            }
            if (!ok) {
               throw new IllegalArgumentException(IBlockCipher.CIPHER_BLOCK_SIZE);
            }
         }

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
//         currentBlockSize = 0;
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

      // do symmetry tests for all block-size/key-size combos
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

   // own methods -------------------------------------------------------------

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

   protected boolean testKat(byte[] kb, byte[] ct) {
      try {
         int bs = ct.length;
         byte[] pt = new byte[bs]; // all-zero plaintext
         byte[] t = new byte[bs];

         Object k = makeKey(kb, bs);

         // test encryption
         encrypt(pt, 0, t,  0, k, bs);
         if (!Util.areEqual(t, ct)) {
            return false;
         }
         // test decryption
         decrypt(t, 0, t, 0, k, bs);
         return Util.areEqual(t, pt);

      } catch (Exception x) {
         x.printStackTrace(System.err);
         return false;
      }
   }
}
