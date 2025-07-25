package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: BaseMode.java,v 1.3 2002-06-08 05:08:35 raif Exp $
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

import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A basic abstract class to facilitate implementing block cipher modes of
 * operations.</p>
 *
 * @version $Revision: 1.3 $
 */
public abstract class BaseMode implements IMode {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of this mode. */
   protected String name;

   /** The state indicator of this instance. */
   protected int state;

   /** The underlying block cipher implementation. */
   protected IBlockCipher cipher;

   /** The block size, in bytes, to operate the underlying block cipher in. */
   protected int cipherBlockSize;

   /** The block size, in bytes, in which to operate the mode instance. */
   protected int modeBlockSize;

   /** The initialisation vector value. */
   protected byte[] iv;

   /** The instance lock. */
   protected Object lock = new Object();

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name prefix of this mode.
    * @param underlyingCipher the implementation of the underlying cipher.
    * @param cipherBlockSize the block size, in bytes, in which to operate the
    * underlying cipher.
    */
   protected
   BaseMode(String name, IBlockCipher underlyingCipher, int cipherBlockSize) {
      super();

      this.name = name;
      this.cipher = underlyingCipher;
      this.cipherBlockSize = cipherBlockSize;
      state = -1;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // IMode interface implementation ------------------------------------------

   public void update(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException {
      synchronized(lock) {
         switch (state) {
         case ENCRYPTION:
            encryptBlock(in, inOffset, out, outOffset);
            break;
         case DECRYPTION:
            decryptBlock(in, inOffset, out, outOffset);
            break;
         default:
            throw new IllegalStateException();
         }
      }
   }

   // IBlockCipher interface implementation -----------------------------------

   public String name() {
      return new StringBuffer()
         .append(name).append('(').append(cipher.name()).append(')')
         .toString();
   }

   /**
    * <p>Returns the default value, in bytes, of the mode's block size. This
    * value is part of the construction arguments passed to the Factory methods
    * in {@link ModeFactory}. Unless changed by an invocation of any of the
    * <code>init()</code> methods, a <i>Mode</i> instance would operate with
    * the same block size as its underlying block cipher. As mentioned earlier,
    * the block size of the underlying block cipher itself is specified in one
    * of the method(s) available in the factory class.</p>
    *
    * @return the default value, in bytes, of the mode's block size.
    * @see gnu.crypto.mode.ModeFactory
    */
   public int defaultBlockSize() {
      return cipherBlockSize;
   }

   /**
    * <p>Returns the default value, in bytes, of the underlying block cipher
    * key size.</p>
    *
    * @return the default value, in bytes, of the underlying cipher's key size.
    */
   public int defaultKeySize() {
      return cipher.defaultKeySize();
   }

   /**
    * <p>Returns an {@link Iterator} over the supported block sizes. Each
    * element returned by this object is an {@link Integer}.</p>
    *
    * <p>The default behaviour is to return an iterator with just one value,
    * which is that currently configured for the underlying block cipher.
    * Concrete implementations may override this behaviour to signal their
    * ability to support other values.</p>
    *
    * @return an {@link Iterator} over the supported block sizes.
    */
   public Iterator blockSizes() {
      ArrayList al = new ArrayList();
      al.add(new Integer(cipherBlockSize));

      return Collections.unmodifiableList(al).iterator();
   }

   /**
    * <p>Returns an {@link Iterator} over the supported underlying block cipher
    * key sizes. Each element returned by this object is an instance of
    * {@link Integer}.</p>
    *
    * @return an {@link Iterator} over the supported key sizes.
    */
   public Iterator keySizes() {
      return cipher.keySizes();
   }

   public void init(Map attributes)
   throws InvalidKeyException, IllegalStateException {
      synchronized(lock) {
         if (state != -1) {
            throw new IllegalStateException();
         }

         Integer want = (Integer) attributes.get(STATE);
         if (want != null) {
            switch (want.intValue()) {
            case ENCRYPTION: state = ENCRYPTION; break;
            case DECRYPTION: state = DECRYPTION; break;
            default: throw new IllegalArgumentException();
            }
         }

         Integer bs = (Integer) attributes.get(MODE_BLOCK_SIZE);
         modeBlockSize = (bs == null ? cipherBlockSize : bs.intValue());

         byte[] iv = (byte[]) attributes.get(IV);
         if (iv != null) {
            this.iv = (byte[]) iv.clone();
         } else {
            this.iv = new byte[modeBlockSize];
         }

         cipher.init(attributes);
         setup();
      }
   }

   public int currentBlockSize() {
      if (state == -1) {
         throw new IllegalStateException();
      }
      return modeBlockSize;
   }

   public void reset() {
      synchronized(lock) {
         state = -1;
         iv = null;
         cipher.reset();

         teardown();
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

   // methods to be implemented by concrete subclasses ------------------------

   public abstract Object clone();

   /** The initialisation phase of the concrete mode implementation. */
   public abstract void setup();

   /** The termination phase of the concrete mode implementation. */
   public abstract void teardown();

   public abstract void encryptBlock(byte[] in, int i, byte[] out, int o);

   public abstract void decryptBlock(byte[] in, int i, byte[] out, int o);

   // own methods -------------------------------------------------------------

   private boolean testSymmetry(int ks, int bs) {
      try {
         IMode mode = (IMode) this.clone();
         byte[] iv = new byte[cipherBlockSize]; // all zeroes
         byte[] k = new byte[ks];
         int i;
         for (i = 0; i < ks; i++) {
            k[i] = (byte) i;
         }

         int blockCount = 5;
         int limit = blockCount * bs;
         byte[] pt = new byte[limit];
         for (i = 0; i < limit; i++) {
            pt[i] = (byte) i;
         }
         byte[] ct = new byte[limit];
         byte[] cpt = new byte[limit];

         Map map = new HashMap();
         map.put(KEY_MATERIAL, k);
         map.put(CIPHER_BLOCK_SIZE, new Integer(bs));
         map.put(STATE, new Integer(ENCRYPTION));
         map.put(IV, iv);
         map.put(MODE_BLOCK_SIZE, new Integer(bs));

         mode.reset();
         mode.init(map);
         for (i = 0; i < blockCount; i++) {
            mode.update(pt, i * bs, ct, i * bs);
         }

         mode.reset();
         map.put(STATE, new Integer(DECRYPTION));
         mode.init(map);
         for (i = 0; i < blockCount; i++) {
            mode.update(ct, i * bs, cpt, i * bs);
         }

         return Util.areEqual(pt, cpt);

      } catch (Exception x) {
         x.printStackTrace(System.err);
         return false;
      }
   }
}
