package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: ICM.java,v 1.2 2001-12-04 12:56:08 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
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

import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of David McGrew Integer Counter Mode (ICM) as an
 * {@link gnu.crypto.mode.IMode}.<p>
 *
 * ICM is a way to define a pseudorandom keystream generator using a block
 * cipher. The keystream can be used for additive encryption, key derivation,
 * or any other application requiring pseudorandom data. In the case of this
 * class, it is used as additive encryption, XOR-ing the keystream with the
 * input text --for both encryption and decryption.<p>
 *
 * In ICM, the keystream is logically broken into segments. Each segment is
 * identified with a segment index, and the segments have equal lengths. This
 * segmentation makes ICM especially appropriate for securing packet-based
 * protocols. ICM also allows a variety of configurations based, among other
 * things, on two parameters: the <i>block index length</i> and the <i>segment
 * index length</i>. A constraint on those two values exists: The sum of
 * <i>segment index length</i> and <i>block index length</i> <b>must not</b>
 * half the <i>block size</i> of the underlying cipher. This requirement
 * protects the ICM keystream generator from potentially failing to be
 * pseudorandom.<p>
 *
 * For simplicity, this implementation, fixes these two values to the
 * following:
 * <ul>
 *    <li>block index length: is half the underlying cipher block size, and</li>
 *    <li>segment index length: is zero.</li>
 * </ul>
 * For a 128-bit block cipher, the above values imply a maximum keystream length
 * of 295,147,905,179,352,825,856 octets, since in ICM, each segment must not
 * exceed the value <tt>(256 ^ <i>block index length</i>) * <i>block length</i></tt>
 * octets.<p>
 *
 * Finally, for this implementation of the ICM, the IV placeholder will be used
 * to pass the value of the <i>Offset</i> in the keystream segment.
 *
 * References:<br>
 * <a href="http://www.ietf.org/internet-drafts/draft-mcgrew-saag-icm-00.txt">
 * Integer Counter Mode</a>, David A. McGrew
 *
 * @version $Revision: 1.2 $
 */
public class ICM extends BaseMode implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The integer value 256 as a BigInteger. */
   private static final BigInteger TWO_FIFTY_SIX = new BigInteger("256");

   /** Maximum number of blocks per segment. */
   private BigInteger maxBlocksPerSegment;

   /** A work constant. */
   private BigInteger counterRange;

   /** The initial counter for a given keystream segment. */
   private BigInteger C0;

   /** The index of the next block for a given keystream segment. */
   private BigInteger blockNdx;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial package-private constructor for use by the Factory class.
    *
    * @param underlyingCipher the underlying cipher implementation.
    * @param cipherBlockSize the underlying cipher block size to use.
    */
   ICM(IBlockCipher underlyingCipher, int cipherBlockSize) {
      super(ModeFactory.ICM_MODE, underlyingCipher, cipherBlockSize);
   }

   /** Private constructor for cloning purposes. */
   private ICM(ICM that) {
      this(that.cipher, that.cipherBlockSize);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Cloneable interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      return new ICM(this);
   }

   // Implementation of abstract methods in BaseMode
   // -------------------------------------------------------------------------

   public void setup() {
      if (modeBlockSize != cipherBlockSize) {
         throw new IllegalArgumentException();
      }

      counterRange = TWO_FIFTY_SIX.pow(cipherBlockSize);
      maxBlocksPerSegment = TWO_FIFTY_SIX.pow(cipherBlockSize / 2);
      BigInteger r = new BigInteger(1, iv);
      C0 = maxBlocksPerSegment.add(r).modPow(BigInteger.ONE, counterRange);
      blockNdx = BigInteger.ZERO;
   }

   public void teardown() {
      counterRange = null;
      maxBlocksPerSegment = null;
      C0 = null;
      blockNdx = null;
   }

   public void encryptBlock(byte[] in, int i, byte[] out, int o) {
      icm(in, i, out, o);
   }

   public void decryptBlock(byte[] in, int i, byte[] out, int o) {
      icm(in, i, out, o);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   private void icm(byte[] in, int inOffset, byte[] out, int outOffset) {
      if (blockNdx.compareTo(maxBlocksPerSegment) >= 0)
         throw new RuntimeException("Maximum blocks for segment reached");

      // encrypt the counter for the current blockNdx
      // C[i] = (C[0] + i) modulo (256^BLOCK_LENGTH).

      BigInteger Ci = C0.add(blockNdx).modPow(BigInteger.ONE, counterRange);
      byte[] result = Ci.toByteArray();
      int limit = result.length;
//      if (limit < cipherBlockSize) {
//         byte[] data = new byte[cipherBlockSize];
//         System.arraycopy(result, 0, data, cipherBlockSize-limit, limit);
//         result = data;
//      } else if (limit > cipherBlockSize) {
//         byte[] data = new byte[cipherBlockSize];
//         System.arraycopy(result, limit-cipherBlockSize, data, 0, cipherBlockSize);
//         result = data;
//      }
//
//      cipher.encryptBlock(result, 0, result, 0);
//      blockNdx = blockNdx.add(BigInteger.ONE); // increment blockNdx
//      for (int i = 0; i < modeBlockSize; ) { // xor result with input block
//         out[outOffset++] = (byte)(in[inOffset++] ^ result[i++]);
//      }
      int ndx = 0;
      if (limit < cipherBlockSize) {
         byte[] data = new byte[cipherBlockSize];
         System.arraycopy(result, 0, data, cipherBlockSize-limit, limit);
         result = data;
      } else if (limit > cipherBlockSize) {
         ndx = limit-cipherBlockSize;
      }

      cipher.encryptBlock(result, ndx, result, ndx);
      blockNdx = blockNdx.add(BigInteger.ONE); // increment blockNdx
      for (int i = 0; i < modeBlockSize; i++) { // xor result with input block
         out[outOffset++] = (byte)(in[inOffset++] ^ result[ndx++]);
      }
   }
}
