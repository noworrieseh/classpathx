package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: ICMGenerator.java,v 1.6 2002-06-08 05:24:34 raif Exp $
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

import gnu.crypto.Registry;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.cipher.CipherFactory;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Counter Mode is a way to define a pseudorandom keystream generator using
 * a block cipher. The keystream can be used for additive encryption, key
 * derivation, or any other application requiring pseudorandom data.</p>
 *
 * <p>In ICM, the keystream is logically broken into segments. Each segment is
 * identified with a segment index, and the segments have equal lengths. This
 * segmentation makes ICM especially appropriate for securing packet-based
 * protocols.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.ietf.org/internet-drafts/draft-mcgrew-saag-icm-00.txt">
 *    Integer Counter Mode</a>, David A. McGrew.</li>
 * </ol>
 *
 * @version $Revision: 1.6 $
 */
public class ICMGenerator extends BasePRNG {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Property name of underlying block cipher for this ICM generator. */
   public static final String CIPHER = "gnu.crypto.prng.icm.cipher.name";

   /** Property name of ICM's block index length. */
   public static final String BLOCK_INDEX_LENGTH =
         "gnu.crypto.prng.icm.block.index.length";

   /** Property name of ICM's segment index length. */
   public static final String SEGMENT_INDEX_LENGTH =
         "gnu.crypto.prng.icm.segment.index.length";

   /** Property name of ICM's segment index length. */
   public static final String OFFSET = "gnu.crypto.prng.icm.offset";

   /** Property name of ICM's segment index length. */
   public static final String SEGMENT_INDEX = "gnu.crypto.prng.icm.segment.index";

   /** The integer value 256 as a BigInteger. */
   private static final BigInteger TWO_FIFTY_SIX = new BigInteger("256");

   /** The underlying cipher implementation. */
   private IBlockCipher cipher;

   /** The underlying cipher block size to use, in octets. */
   private int cipherBlockSize;

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

   /** Trivial 0-arguments constructor. */
   public ICMGenerator() {
      super(Registry.ICM_PRNG);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // Implementation of abstract methods in BasePRNG --------------------------

   // Conceptually, ICM is a keystream generator that takes a secret key
   // and a segment index as an input and then outputs a keystream
   // segment.  The segmentation lends itself to packet encryption, as
   // each keystream segment can be used to encrypt a distinct packet.
   //
   // An ICM key consists of the block cipher key and an Offset.  The
   // Offset is an integer with BLOCK_LENGTH octets...
   //
   public void setup(Map attributes) {
      // find out which cipher algorithm to use
      String underlyingCipher = (String) attributes.get(CIPHER);
      if (underlyingCipher == null)
         underlyingCipher = "rijndael";

      // ensure that we have a reliable implementation of this cipher algorithm
      cipher = CipherFactory.getInstance(underlyingCipher);

      // assume we'll use its default block size
      cipherBlockSize = cipher.defaultBlockSize();

      // find out what block size we should use for it. if null stick with
      // default;
      Integer i = (Integer) attributes.get(IBlockCipher.CIPHER_BLOCK_SIZE);
      if (i != null) {
         cipherBlockSize = i.intValue();
      }

      Iterator it;
      // ensure that value is valid for the chosen underlying cipher
      boolean ok = false;
      for (it = cipher.blockSizes(); it.hasNext(); ) {
         ok = (cipherBlockSize == ((Integer) it.next()).intValue());
         if (ok) {
            break;
         }
      }
      if (!ok)
         throw new IllegalArgumentException(IBlockCipher.CIPHER_BLOCK_SIZE);

      // get the key material
      byte[] key = (byte[]) attributes.get(IBlockCipher.KEY_MATERIAL);
      if (key == null) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }

      int keyLength = key.length;

      // ensure that keyLength is valid for the chosen underlying cipher
      ok = false;
      for (it = cipher.keySizes(); it.hasNext(); ) {
         ok = (keyLength == ((Integer) it.next()).intValue());
         if (ok) {
            break;
         }
      }
      if (!ok) {
         throw new IllegalArgumentException("keyLength");
      }

      // ensure that remaining params make sense
      int blockIndexLength = -1; // number of octets in the block index
      i = (Integer) attributes.get(BLOCK_INDEX_LENGTH);
      if (i != null) {
         blockIndexLength = i.intValue();
         if (blockIndexLength < 1) {
            throw new IllegalArgumentException(BLOCK_INDEX_LENGTH);
         }
      }

      int segmentIndexLength = -1; // number of octets in the segment index
      i = (Integer) attributes.get(SEGMENT_INDEX_LENGTH);
      if (i != null) {
         segmentIndexLength = i.intValue();
         if (segmentIndexLength < 1) {
            throw new IllegalArgumentException(SEGMENT_INDEX_LENGTH);
         }
      }

      // if both are undefined spit the dummy
      if ((blockIndexLength == -1) && (segmentIndexLength == -1)) {
         throw new IllegalArgumentException(BLOCK_INDEX_LENGTH+", "+SEGMENT_INDEX_LENGTH);
      } else { // if one is undefined, set it to BLOCK_LENGTH / 2 minus the other
         int limit = cipherBlockSize / 2;
         if (blockIndexLength == -1) {
            blockIndexLength = limit - segmentIndexLength;
         } else if (segmentIndexLength == -1) {
            segmentIndexLength = limit - blockIndexLength;
         } else if ((segmentIndexLength + blockIndexLength) > limit) {
            throw new IllegalArgumentException(BLOCK_INDEX_LENGTH+", "+SEGMENT_INDEX_LENGTH);
         }
      }

      maxBlocksPerSegment = TWO_FIFTY_SIX.pow(blockIndexLength);
      // Maximum number segments possible
      BigInteger maxSegmentCount = TWO_FIFTY_SIX.pow(segmentIndexLength);
      counterRange = TWO_FIFTY_SIX.pow(cipherBlockSize);

      Object obj = attributes.get(OFFSET);
      // allow either a byte[] or a BigInteger
      BigInteger r;
      if (obj instanceof BigInteger) {
         r = (BigInteger) obj;
      } else { // assume byte[]. should be same length as cipher block size
         byte[] offset = (byte[]) obj;
         if (offset.length != cipherBlockSize) {
            throw new IllegalArgumentException(OFFSET);
         }

         r = new BigInteger(1, offset);
      }

      // get the segment index as a BigInteger
      BigInteger s = (BigInteger) attributes.get(SEGMENT_INDEX);
      if (s.compareTo(maxSegmentCount) > 0) {
         throw new IllegalArgumentException(SEGMENT_INDEX);
      }

      // The initial counter of the keystream segment with segment index s is
      // defined as follows, where r denotes the Offset:
      //
      // C[0] = (s * (256^BLOCK_INDEX_LENGTH) + r) modulo (256^BLOCK_LENGTH)
      //
      C0 = s.multiply(maxBlocksPerSegment).add(r).modPow(BigInteger.ONE, counterRange);
      blockNdx = BigInteger.ZERO;

      // finally initialise the underlying cipher
      HashMap map = new HashMap();
      map.put(IBlockCipher.CIPHER_BLOCK_SIZE, new Integer(cipherBlockSize));
      map.put(IBlockCipher.KEY_MATERIAL, key);
      try {
         cipher.init(map);
      } catch (InvalidKeyException x) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }
   }

   public void fillBlock() throws LimitReachedException {
      if (!(blockNdx.compareTo(maxBlocksPerSegment) < 0))
         throw new LimitReachedException();

      // encrypt the counter for the current blockNdx
      // C[i] = (C[0] + i) modulo (256^BLOCK_LENGTH).

      BigInteger Ci = C0.add(blockNdx).modPow(BigInteger.ONE, counterRange);
      buffer = Ci.toByteArray();
      int limit = buffer.length;
      if (limit < cipherBlockSize) {
         byte[] data = new byte[cipherBlockSize];
         System.arraycopy(buffer, 0, data, cipherBlockSize-limit, limit);
         buffer = data;
      } else if (limit > cipherBlockSize) {
         byte[] data = new byte[cipherBlockSize];
         System.arraycopy(buffer, limit-cipherBlockSize, data, 0, cipherBlockSize);
         buffer = data;
      }

      cipher.encryptBlock(buffer, 0, buffer, 0);
      blockNdx = blockNdx.add(BigInteger.ONE); // increment blockNdx
   }
}
