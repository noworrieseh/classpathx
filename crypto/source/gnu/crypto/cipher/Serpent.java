package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: Serpent.java,v 1.3 2002-09-04 09:56:39 raif Exp $
//
// Copyright (C) 2002, Free Software Foundation, Inc.
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

import gnu.crypto.Registry;
import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.Collections;
import java.util.Iterator;

/**
 * <p>Serpent is a 32-round substitution-permutation network block cipher,
 * operating on 128-bit blocks and accepting keys of 128, 192, and 256 bits in
 * length. At each round the plaintext is XORed with a 128 bit portion of the
 * session key -- a 4224 bit key computed from the input key -- then one of
 * eight S-boxes are applied, and finally a simple linear transformation is
 * done. Decryption does the exact same thing in reverse order, and using the
 * eight inverses of the S-boxes.</p>
 *
 * <p>Serpent was designed by Ross Anderson, Eli Biham, and Lars Knudsen as a
 * proposed cipher for the Advanced Encryption Standard.</p>
 *
 * <p>Serpent can be sped up greatly by replacing S-box substitution with a
 * sequence of binary operations, and the optimal implementation depends
 * upon finding the fastest sequence of binary operations that reproduce this
 * substitution. This implementation uses the S-boxes discovered by
 * <a href="http://www.ii.uib.no/~osvik/">Dag Arne Osvik</a>, which are
 * optimized for the Pentium family of processors.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.cl.cam.ac.uk/~rja14/serpent.html">Serpent: A
 *    Candidate Block Cipher for the Advanced Encryption Standard.</a></li>
 * </ol>
 *
 * @version $Revision: 1.3 $
 */
public class Serpent extends BaseCipher {

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "serpent";

   private static final int DEFAULT_KEY_SIZE = 16;
   private static final int DEFAULT_BLOCK_SIZE = 16;
   private static final int ROUNDS = 32;

   /** The fractional part of the golden ratio, (sqrt(5)+1)/2. */
   private static final int PHI = 0x9e3779b9;

   /**
    * KAT vector (from ecb_vk):
    * I=9
    * KEY=008000000000000000000000000000000000000000000000
    * CT=5587B5BCB9EE5A28BA2BACC418005240
    */
   private static final byte[] KAT_KEY =
         Util.toBytesFromString("008000000000000000000000000000000000000000000000");
   private static final byte[] KAT_CT =
         Util.toBytesFromString("5587B5BCB9EE5A28BA2BACC418005240");

   /** caches the result of the correctness test, once executed. */
   private static Boolean valid;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial zero-argument constructor. */
   public Serpent() {
      super(Registry.SERPENT_CIPHER, DEFAULT_BLOCK_SIZE, DEFAULT_KEY_SIZE);
   }

   // Class methods
   // -------------------------------------------------------------------------

   /** Serpent's linear transformation. */
   private static final void transform(int[] x) {
      x[0] = x[0] << 13 | x[0] >>> 19;
      x[2] = x[2] << 3 | x[2] >>> 29;
      x[1] = x[1] ^ x[0] ^ x[2];
      x[3] = x[3] ^ x[2] ^ (x[0] << 3);
      x[1] = x[1] << 1 | x[1] >>> 31;
      x[3] = x[3] << 7 | x[3] >>> 25;
      x[0] = x[0] ^ x[1] ^ x[3];
      x[2] = x[2] ^ x[3] ^ (x[1] << 7);
      x[0] = x[0] << 5 | x[0] >>> 27;
      x[2] = x[2] << 22 | x[2] >>> 10;
   }

   /**
    * The inverse linear transformation. The XOR with the key is done
    * here to save space above.
    *
    * @param x The current block being decrypted.
    * @param key The session key.
    * @param off The index in the key to start from.
    */
   private static final void transformInv(int[] x, int[] key, int off) {
      x[0] ^= key[off++];
      x[1] ^= key[off++];
      x[2] ^= key[off++];
      x[3] ^= key[off++];

      x[2] = x[2] >>> 22 | x[2] << 10;
      x[0] = x[0] >>> 5 | x[0] << 27;
      x[2] = x[2] ^ x[3] ^ (x[1] << 7);
      x[0] = x[0] ^ x[1] ^ x[3];
      x[3] = x[3] >>> 7 | x[3] << 25;
      x[1] = x[1] >>> 1 | x[1] << 31;
      x[3] = x[3] ^ x[2] ^ (x[0] << 3);
      x[1] = x[1] ^ x[0] ^ x[2];
      x[2] = x[2] >>> 3 | x[2] << 29;
      x[0] = x[0] >>> 13 | x[0] << 19;
   }

   // Bit-flip madness methods
   //
   // The following S-Box functions were developed by Dag Arne Osvik, and are
   // described in his paper, "Speeding up Serpent". They are optimized to
   // perform on the Pentium chips, but work well here too.
   //
   // The methods below are Copyright (C) 2000 Dag Arne Osvik.

   // These methods may be de-assembler-ized (more than one operation in each
   // statement) for readability (?) and speed (??).

   /** S-Box 0. */
   private static final void
   sbox0(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r1 ^ r2;
      r3 ^= r0;
      r1 = r1 & r3 ^ r0;
      r0 = (r0 | r3) ^ r4;
      r4 ^= r3;
      r3 ^= r2;
      r2 = (r2 | r1) ^ r4;
      r4 = ~r4 | r1;
      r1 ^= r3 ^ r4;
      r3 |= r0;
      w[off  ] = r1 ^ r3;
      w[off+1] = r4 ^ r3;
      w[off+2] = r2;
      w[off+3] = r0;
   }

   /** The inverse of S-Box 0. */
   private static final void
   sboxI0(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r1;
      r2 = ~r2;
      r1 = (r1 | r0) ^ r2;
      r4 = ~r4;
      r2 |= r4;
      r1 ^= r3;
      r0 ^= r4;
      r2 ^= r0;
      r0 &= r3;
      r4 ^= r0;
      r0 = (r0 | r1) ^ r2;
      r3 = r3 ^ r4 ^ r0 ^ r1;
      r2 = (r2 ^ r1) & r3;
      w[off  ] = r0;
      w[off+1] = r4 ^ r2;
      w[off+2] = r1;
      w[off+3] = r3;
   }

   /** S-Box 1. */
   private static final void
   sbox1(int r0, int r1, int r2, int r3, int[] w, int off) {
      r0 = ~r0;
      int r4 = r0;
      r2 = ~r2;
      r0 &= r1;
      r2 ^= r0;
      r0 |= r3;
      r3 ^= r2;
      r1 ^= r0;
      r0 ^= r4;
      r4 |= r1;
      r1 ^= r3;
      r2 = (r2 | r0) & r4;
      r0 ^= r1;
      w[off  ] = r2;
      w[off+1] = r0 & r2 ^ r4;
      w[off+2] = r3;
      w[off+3] = r1 & r2 ^ r0;
   }

   /** The inverse of S-Box 1. */
   private static final void
   sboxI1(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r1;
      r1 ^= r3;
      r3 = r3 & r1 ^ r0;
      r4 ^= r2;
      r2 ^= r3;
      r0 = (r0 | r1) ^ r4 | r2;
      r1 ^= r3;
      r0 ^= r1;
      r1 = (r1 | r3) ^ r0;
      r4 = ~r4 ^ r1;
      w[off  ] = r4;
      w[off+1] = r0;
      w[off+2] = r3 ^ ((r1 | r0) ^ r0 | r4);
      w[off+3] = r2;
   }

   /** S-Box 2. */
   private static final void
   sbox2(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r0;
      r0 = r0 & r2 ^ r3;
      r2 = r2 ^ r1 ^ r0;
      r3 = (r3 | r4) ^ r1;
      r4 ^= r2;
      r1 = r3;
      r3 = (r3 | r4) ^ r0;
      r0 &= r1;
      r4 ^= r0;
      w[off  ] = r2;
      w[off+1] = r3;
      w[off+2] = r1 ^ r3 ^ r4;
      w[off+3] = ~r4;
   }

   /** The inverse of S-Box 2. */
   private static final void
   sboxI2(int r0, int r1, int r2, int r3, int[] w, int off) {
      r2 ^= r3;
      r3 ^= r0;
      int r4 = r3;
      r3 = r3 & r2 ^ r1;
      r1 = (r1 | r2) ^ r4;
      r4 &= r3;
      r2 ^= r3;
      r4 = r4 & r0 ^ r2;
      r3 = ~r3;
      r2 = (r2 & r1 | r0) ^ r3;
      r0 = (r0 ^ r3) & r1;
      w[off  ] = r1;
      w[off+1] = r4;
      w[off+2] = r2;
      w[off+3] = r3 ^ r4 ^ r0;
   }

   /** S-Box 3. */
   private static final void
   sbox3(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r0;
      r0 |= r3;
      r3 ^= r1;
      r1 &= r4;
      r4 = r4 ^ r2 | r1;
      r2 ^= r3;
      r3 = r3 & r0 ^ r4;
      r0 ^= r1;
      r4 = r4 & r0 ^ r2;
      r1 = (r1 ^ r3 | r0) ^ r2;
      r0 ^= r3;
      w[off  ] = (r1 | r3) ^ r0;
      w[off+1] = r1;
      w[off+2] = r3;
      w[off+3] = r4;
   }

   /** Inverse of S-Box 3. */
   private static final void
   sboxI3(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r2;
      r2 ^= r1;
      r0 ^= r2;
      r4 = r4 & r2 ^ r0;
      r0 &= r1;
      r1 ^= r3;
      r3 |= r4;
      r2 ^= r3;
      r0 ^= r3;
      r1 ^= r4;
      r3 = r3 & r2 ^ r1;
      r1 = (r1 ^ r0 | r2) ^ r4;
      w[off  ] = r2;
      w[off+1] = r1;
      w[off+2] = r3;
      w[off+3] = r0 ^ r3 ^ r1;
   }

   /** S-Box 4. */
   private static final void
   sbox4(int r0, int r1, int r2, int r3, int[] w, int off) {
      r1 ^= r3;
      int r4 = r1;
      r3 = ~r3;
      r2 ^= r3;
      r3 ^= r0;
      r1 = r1 & r3 ^ r2;
      r4 ^= r3;
      r0 ^= r4;
      r2 = r2 & r4 ^ r0;
      r0 &= r1;
      r3 ^= r0;
      r4 = (r4 | r1) ^ r0;
      w[off  ] = r1;
      w[off+1] = r4 ^ (r2 & r3);
      w[off+2] = ~((r0 | r3) ^ r2);
      w[off+3] = r3;
   }

   /** Inverse of S-Box 4. */
   private static final void
   sboxI4(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r2;
      r2 = r2 & r3 ^ r1;
      r1 = (r1 | r3) & r0;
      r4 = r4 ^ r2 ^ r1;
      r1 &= r2;
      r0 = ~r0;
      r3 ^= r4;
      r1 ^= r3;
      r3 = r3 & r0 ^ r2;
      r0 ^= r1;
      r3 ^= r0;
      w[off  ] = r0;
      w[off+1] = r3 ^ r0;
      w[off+2] = (r2 & r0 ^ r4 | r3) ^ r1;
      w[off+3] = r4;
   }

   /** S-Box 5. */
   private static final void
   sbox5(int r0, int r1, int r2, int r3, int[] w, int off) {
      r0 ^= r1;
      r1 ^= r3;
      int r4 = r1;
      r3 = ~r3;
      r1 &= r0;
      r2 ^= r3;
      r1 ^= r2;
      r2 |= r4;
      r4 ^= r3;
      r3 = r3 & r1 ^ r0;
      r4 = r4 ^ r1 ^ r2;
      w[off  ] = r1;
      w[off+1] = r3;
      w[off+2] = r0 & r3 ^ r4;
      w[off+3] = ~(r2 ^ r0) ^ (r4 | r3);
   }

   /** Inverse of S-Box 5. */
   private static final void
   sboxI5(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r3;
      r1 = ~r1;
      r2 ^= r1;
      r3 = (r3 | r0) ^ r2;
      r4 ^= r3;
      r2 = (r2 | r1) & r0 ^ r4;
      r4 = (r4 | r0) ^ r1 ^ r2;
      r1 = r1 & r2 ^ r3;
      r3 &= r4;
      r4 ^= r1;
      w[off  ] = r1;
      w[off+1] = ~r4;
      w[off+2] = r3 ^ r4 ^ r0;
      w[off+3] = r2;
   }

   /** S-Box 6. */
   private static final void
   sbox6(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r3;
      r2 = ~r2;
      r3 = r3 & r0 ^ r2;
      r0 ^= r4;
      r2 = (r2 | r4) ^ r0;
      r1 ^= r3;
      r0 |= r1;
      r2 ^= r1;
      r4 ^= r0;
      r0 = (r0 | r3) ^ r2;
      r4 = r4 ^ r3 ^ r0;
      w[off  ] = r0;
      w[off+1] = r1;
      w[off+2] = r4;
      w[off+3] = r2 & r4 ^ ~r3;
   }

   /** Inverse of S-Box 6. */
   private static final void
   sboxI6(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r2;
      r0 ^= r2;
      r2 &= r0;
      r4 ^= r3;
      r3 ^= r1;
      r2 = ~r2 ^ r3;
      r4 |= r0;
      r0 ^= r2;
      r3 ^= r4;
      r4 ^= r1;
      r1 = r1 & r3 ^ r0;
      r0 = r0 ^ r3 | r2;
      w[off  ] = r1;
      w[off+1] = r2;
      w[off+2] = r4 ^ r0;
      w[off+3] = r3 ^ r1;
   }

   /** S-Box 7. */
   private static final void
   sbox7(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r1;
      r1 = (r1 | r2) ^ r3;
      r4 ^= r2;
      r2 ^= r1;
      r3 = (r3 | r4) & r0;
      r4 ^= r2;
      r3 ^= r1;
      r1 = (r1 | r4) ^ r0;
      r0 = (r0 | r4) ^ r2;
      r1 ^= r4;
      r2 ^= r1;
      w[off  ] = r4 ^ (~r2 | r0);
      w[off+1] = r3;
      w[off+2] = r1 & r0 ^ r4;
      w[off+3] = r0;
   }

   /** Inverse of S-Box 7. */
   private static final void
   sboxI7(int r0, int r1, int r2, int r3, int[] w, int off) {
      int r4 = r2;
      r2 = ~(r2 ^ r0);
      r0 &= r3;
      r4 |= r3;
      r3 ^= r1;
      r1 |= r0;
      r0 ^= r2;
      r2 &= r4;
      r3 &= r4;
      r1 ^= r2;
      r2 ^= r0;
      r0 = (r0 | r2) ^ r3;
      r4 ^= r1;
      w[off  ] = r3 ^ r4 ^ r2;
      w[off+1] = r0;
      w[off+2] = r1;
      w[off+3] = (r4 | r0) ^ r2;
   }

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      return new Serpent();
   }

   // IBlockCipherSpi interface implementation --------------------------------

   public Iterator blockSizes() {
      return Collections.singleton(new Integer(DEFAULT_BLOCK_SIZE)).iterator();
   }

   public Iterator keySizes() {
      return new Iterator() {
         int i = 0;
         // Support 128, 192, and 256 bit keys.
         Integer[] keySizes = {
            new Integer(16), new Integer(24), new Integer(32)
         };

         public boolean hasNext() {
            return i < keySizes.length;
         }

         public Object next() {
            if (hasNext()) {
               return keySizes[i++];
            }
            return null;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Object makeKey(byte[] key, int blockSize) throws InvalidKeyException {
      // Not strictly true, but here to conform with the AES proposal.
      // This restriction can be removed if deemed necessary.
      if (key.length != 16 && key.length != 24 && key.length != 32) {
         throw new InvalidKeyException("Key length is not 16, 24, or 32 bytes");
      }

      // Here w is our "pre-key".
      int[] w = new int[4*(ROUNDS+1)];
      int i, j;
      for (i = 0, j = key.length-4; i < 8 && j >= 0; i++) {
         w[i] = (key[j  ] & 0xff) << 24 | (key[j+1] & 0xff) << 16 |
                (key[j+2] & 0xff) << 8  | (key[j+3] & 0xff);
         j -= 4;
      }
      // Pad key if < 256 bits.
      if (i != 8) {
         w[i] = 1;
      }
      // Transform using w_i-8 ... w_i-1
      for (i = 8; i < 16; i++) {
         int t = w[i-8] ^ w[i-5] ^ w[i-3] ^ w[i-1] ^ PHI ^ (i-8);
         w[i] = t << 11 | t >>> 21;
      }
      // Translate by 8.
      for (i = 0; i < 8; i++) {
         w[i] = w[i+8];
      }
      // Transform the rest of the key.
      for (i = 8; i < w.length; i++) {
         int t = w[i-8] ^ w[i-5] ^ w[i-3] ^ w[i-1] ^ PHI ^ i;
         w[i] = t << 11 | t >>> 21;
      }

      // After these s-boxes the pre-key (w, above) will become the
      // session key (w, below).
      sbox3(w[  0], w[  1], w[  2], w[  3], w,   0);
      sbox2(w[  4], w[  5], w[  6], w[  7], w,   4);
      sbox1(w[  8], w[  9], w[ 10], w[ 11], w,   8);
      sbox0(w[ 12], w[ 13], w[ 14], w[ 15], w,  12);
      sbox7(w[ 16], w[ 17], w[ 18], w[ 19], w,  16);
      sbox6(w[ 20], w[ 21], w[ 22], w[ 23], w,  20);
      sbox5(w[ 24], w[ 25], w[ 26], w[ 27], w,  24);
      sbox4(w[ 28], w[ 29], w[ 30], w[ 31], w,  28);
      sbox3(w[ 32], w[ 33], w[ 34], w[ 35], w,  32);
      sbox2(w[ 36], w[ 37], w[ 38], w[ 39], w,  36);
      sbox1(w[ 40], w[ 41], w[ 42], w[ 43], w,  40);
      sbox0(w[ 44], w[ 45], w[ 46], w[ 47], w,  44);
      sbox7(w[ 48], w[ 49], w[ 50], w[ 51], w,  48);
      sbox6(w[ 52], w[ 53], w[ 54], w[ 55], w,  52);
      sbox5(w[ 56], w[ 57], w[ 58], w[ 59], w,  56);
      sbox4(w[ 60], w[ 61], w[ 62], w[ 63], w,  60);
      sbox3(w[ 64], w[ 65], w[ 66], w[ 67], w,  64);
      sbox2(w[ 68], w[ 69], w[ 70], w[ 71], w,  68);
      sbox1(w[ 72], w[ 73], w[ 74], w[ 75], w,  72);
      sbox0(w[ 76], w[ 77], w[ 78], w[ 79], w,  76);
      sbox7(w[ 80], w[ 81], w[ 82], w[ 83], w,  80);
      sbox6(w[ 84], w[ 85], w[ 86], w[ 87], w,  84);
      sbox5(w[ 88], w[ 89], w[ 90], w[ 91], w,  88);
      sbox4(w[ 92], w[ 93], w[ 94], w[ 95], w,  92);
      sbox3(w[ 96], w[ 97], w[ 98], w[ 99], w,  96);
      sbox2(w[100], w[101], w[102], w[103], w, 100);
      sbox1(w[104], w[105], w[106], w[107], w, 104);
      sbox0(w[108], w[109], w[110], w[111], w, 108);
      sbox7(w[112], w[113], w[114], w[115], w, 112);
      sbox6(w[116], w[117], w[118], w[119], w, 116);
      sbox5(w[120], w[121], w[122], w[123], w, 120);
      sbox4(w[124], w[125], w[126], w[127], w, 124);
      sbox3(w[128], w[129], w[130], w[131], w, 128);

      return w;
   }

   public void encrypt(byte[] in, int i, byte[] out, int o, Object K, int bs) {
      int[] key = (int[]) K;
      int[] x = new int[4];
      x[3] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[2] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[1] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[0] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);

      sbox0(key[  0]^x[0], key[  1]^x[1], key[  2]^x[2], key[  3]^x[3], x, 0);
      transform(x);
      sbox1(key[  4]^x[0], key[  5]^x[1], key[  6]^x[2], key[  7]^x[3], x, 0);
      transform(x);
      sbox2(key[  8]^x[0], key[  9]^x[1], key[ 10]^x[2], key[ 11]^x[3], x, 0);
      transform(x);
      sbox3(key[ 12]^x[0], key[ 13]^x[1], key[ 14]^x[2], key[ 15]^x[3], x, 0);
      transform(x);
      sbox4(key[ 16]^x[0], key[ 17]^x[1], key[ 18]^x[2], key[ 19]^x[3], x, 0);
      transform(x);
      sbox5(key[ 20]^x[0], key[ 21]^x[1], key[ 22]^x[2], key[ 23]^x[3], x, 0);
      transform(x);
      sbox6(key[ 24]^x[0], key[ 25]^x[1], key[ 26]^x[2], key[ 27]^x[3], x, 0);
      transform(x);
      sbox7(key[ 28]^x[0], key[ 29]^x[1], key[ 30]^x[2], key[ 31]^x[3], x, 0);
      transform(x);
      sbox0(key[ 32]^x[0], key[ 33]^x[1], key[ 34]^x[2], key[ 35]^x[3], x, 0);
      transform(x);
      sbox1(key[ 36]^x[0], key[ 37]^x[1], key[ 38]^x[2], key[ 39]^x[3], x, 0);
      transform(x);
      sbox2(key[ 40]^x[0], key[ 41]^x[1], key[ 42]^x[2], key[ 43]^x[3], x, 0);
      transform(x);
      sbox3(key[ 44]^x[0], key[ 45]^x[1], key[ 46]^x[2], key[ 47]^x[3], x, 0);
      transform(x);
      sbox4(key[ 48]^x[0], key[ 49]^x[1], key[ 50]^x[2], key[ 51]^x[3], x, 0);
      transform(x);
      sbox5(key[ 52]^x[0], key[ 53]^x[1], key[ 54]^x[2], key[ 55]^x[3], x, 0);
      transform(x);
      sbox6(key[ 56]^x[0], key[ 57]^x[1], key[ 58]^x[2], key[ 59]^x[3], x, 0);
      transform(x);
      sbox7(key[ 60]^x[0], key[ 61]^x[1], key[ 62]^x[2], key[ 63]^x[3], x, 0);
      transform(x);
      sbox0(key[ 64]^x[0], key[ 65]^x[1], key[ 66]^x[2], key[ 67]^x[3], x, 0);
      transform(x);
      sbox1(key[ 68]^x[0], key[ 69]^x[1], key[ 70]^x[2], key[ 71]^x[3], x, 0);
      transform(x);
      sbox2(key[ 72]^x[0], key[ 73]^x[1], key[ 74]^x[2], key[ 75]^x[3], x, 0);
      transform(x);
      sbox3(key[ 76]^x[0], key[ 77]^x[1], key[ 78]^x[2], key[ 79]^x[3], x, 0);
      transform(x);
      sbox4(key[ 80]^x[0], key[ 81]^x[1], key[ 82]^x[2], key[ 83]^x[3], x, 0);
      transform(x);
      sbox5(key[ 84]^x[0], key[ 85]^x[1], key[ 86]^x[2], key[ 87]^x[3], x, 0);
      transform(x);
      sbox6(key[ 88]^x[0], key[ 89]^x[1], key[ 90]^x[2], key[ 91]^x[3], x, 0);
      transform(x);
      sbox7(key[ 92]^x[0], key[ 93]^x[1], key[ 94]^x[2], key[ 95]^x[3], x, 0);
      transform(x);
      sbox0(key[ 96]^x[0], key[ 97]^x[1], key[ 98]^x[2], key[ 99]^x[3], x, 0);
      transform(x);
      sbox1(key[100]^x[0], key[101]^x[1], key[102]^x[2], key[103]^x[3], x, 0);
      transform(x);
      sbox2(key[104]^x[0], key[105]^x[1], key[106]^x[2], key[107]^x[3], x, 0);
      transform(x);
      sbox3(key[108]^x[0], key[109]^x[1], key[110]^x[2], key[111]^x[3], x, 0);
      transform(x);
      sbox4(key[112]^x[0], key[113]^x[1], key[114]^x[2], key[115]^x[3], x, 0);
      transform(x);
      sbox5(key[116]^x[0], key[117]^x[1], key[118]^x[2], key[119]^x[3], x, 0);
      transform(x);
      sbox6(key[120]^x[0], key[121]^x[1], key[122]^x[2], key[123]^x[3], x, 0);
      transform(x);
      sbox7(key[124]^x[0], key[125]^x[1], key[126]^x[2], key[127]^x[3], x, 0);

      x[0] ^= key[128];
      x[1] ^= key[129];
      x[2] ^= key[130];
      x[3] ^= key[131];

      for (int j = x.length-1; j >= 0; j--) {
         out[o++] = (byte) (x[j] >>> 24);
         out[o++] = (byte) (x[j] >>> 16);
         out[o++] = (byte) (x[j] >>>  8);
         out[o++] = (byte)  x[j];
      }
   }

   public void decrypt(byte[] in, int i, byte[] out, int o, Object K, int bs) {
      int[] key = (int[]) K;
      int[] x = new int[4];
      x[3] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[2] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[1] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);
      x[0] = (in[i++] & 0xff) << 24 | (in[i++] & 0xff) << 16 |
             (in[i++] & 0xff) <<  8 | (in[i++] & 0xff);

      sboxI7(key[128]^x[0], key[129]^x[1], key[130]^x[2], key[131]^x[3], x, 0);
      transformInv(x, key, 124);
      sboxI6(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 120);
      sboxI5(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 116);
      sboxI4(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 112);
      sboxI3(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 108);
      sboxI2(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 104);
      sboxI1(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key, 100);
      sboxI0(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  96);
      sboxI7(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  92);
      sboxI6(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  88);
      sboxI5(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  84);
      sboxI4(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  80);
      sboxI3(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  76);
      sboxI2(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  72);
      sboxI1(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  68);
      sboxI0(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  64);
      sboxI7(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  60);
      sboxI6(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  56);
      sboxI5(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  52);
      sboxI4(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  48);
      sboxI3(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  44);
      sboxI2(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  40);
      sboxI1(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  36);
      sboxI0(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  32);
      sboxI7(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  28);
      sboxI6(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  24);
      sboxI5(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  20);
      sboxI4(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  16);
      sboxI3(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,  12);
      sboxI2(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,   8);
      sboxI1(x[0], x[1], x[2], x[3], x, 0);
      transformInv(x, key,   4);
      sboxI0(x[0], x[1], x[2], x[3], x, 0);

      x[0] ^= key[0];
      x[1] ^= key[1];
      x[2] ^= key[2];
      x[3] ^= key[3];

      for (int j = x.length-1; j >= 0; j--) {
         out[o++] = (byte) (x[j] >>> 24);
         out[o++] = (byte) (x[j] >>> 16);
         out[o++] = (byte) (x[j] >>>  8);
         out[o++] = (byte)  x[j];
      }
   }

   public boolean selfTest() {
      if (valid == null) {
         boolean result = super.selfTest(); // do symmetry tests
         if (result) {
            result = testKat(KAT_KEY, KAT_CT);
         }
         valid = new Boolean(result);
      }
      return valid.booleanValue();
   }
}

