package gnu.crypto.mac;

// ----------------------------------------------------------------------------
// $Id: UMac32.java,v 1.3 2002-07-14 01:39:58 raif Exp $
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
import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.UMacGenerator;
import gnu.crypto.util.Util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The implementation of the <i>UMAC</i> (Universal Message Authentication
 * Code).</p>
 *
 * <p>The <i>UMAC</i> algorithms described are <i>parameterized</i>. This means
 * that various low-level choices, like the endian convention and the underlying
 * cryptographic primitive, have not been fixed. One must choose values for
 * these parameters before the authentication tag generated by <i>UMAC</i> (for
 * a given message, key, and nonce) becomes fully-defined. In this document
 * we provide two collections of parameter settings, and have named the sets
 * <i>UMAC16</i> and <i>UMAC32</i>. The parameter sets have been chosen based on
 * experimentation and provide good performance on a wide variety of processors.
 * <i>UMAC16</i> is designed to excel on processors which provide small-scale
 * SIMD parallelism of the type found in Intel's MMX and Motorola's AltiVec
 * instruction sets, while <i>UMAC32</i> is designed to do well on processors
 * with good 32- and 64- bit support. <i>UMAC32</i> may take advantage of SIMD
 * parallelism in future processors.</p>
 *
 * <p><i>UMAC</i> has been designed to allow implementations which accommodate
 * <i>on-line</i> authentication. This means that pieces of the message may
 * be presented to <i>UMAC</i> at different times (but in correct order) and an
 * on-line implementation will be able to process the message correctly without
 * the need to buffer more than a few dozen bytes of the message. For
 * simplicity, the algorithms in this specification are presented as if the
 * entire message being authenticated were available at once.</p>
 *
 * <p>To authenticate a message, <code>Msg</code>, one first applies the
 * universal hash function, resulting in a string which is typically much
 * shorter than the original message.  The pseudorandom function is applied to a
 * nonce, and the result is used in the manner of a Vernam cipher: the
 * authentication tag is the xor of the output from the hash function and the
 * output from the pseudorandom function. Thus, an authentication tag is
 * generated as</p>
 *
 * <pre>
 *    AuthTag = f(Nonce) xor h(Msg)
 * </pre>
 *
 * <p>Here <code>f</code> is the pseudorandom function shared between the sender
 * and the receiver, and h is a universal hash function shared by the sender and
 * the receiver. In <i>UMAC</i>, a shared key is used to key the pseudorandom
 * function <code>f</code>, and then <code>f</code> is used for both tag
 * generation and internally to generate all of the bits needed by the universal
 * hash function.</p>
 *
 * <p>The universal hash function that we use is called <code>UHASH</code>. It
 * combines several software-optimized algorithms into a multi-layered
 * structure. The algorithm is moderately complex. Some of this complexity comes
 * from extensive speed optimizations.</p>
 *
 * <p>For the pseudorandom function we use the block cipher of the <i>Advanced
 * Encryption Standard</i> (AES).</p>
 *
 * <p>The UMAC32 parameters, considered in this implementation are:</p>
 * <pre>
 *                                   UMAC32
 *                                   ------
 *        WORD-LEN                        4
 *        UMAC-OUTPUT-LEN                 8
 *        L1-KEY-LEN                   1024
 *        UMAC-KEY-LEN                   16
 *        ENDIAN-FAVORITE               BIG *
 *        L1-OPERATIONS-SIGN       UNSIGNED
 * </pre>
 *
 * <p>Please note that this UMAC32 differs from the one described in the paper
 * by the <i>ENDIAN-FAVORITE</i> value.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">
 *    UMAC</a>: Message Authentication Code using Universal Hashing.<br>
 *    T. Krovetz, J. Black, S. Halevi, A. Hevia, H. Krawczyk, and P. Rogaway.</li>
 * </ol>
 *
 * @version $Revision: 1.3 $
 */
public class UMac32 extends BaseMac {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * Property name of the user-supplied <i>Nonce</i>. The value associated to
    * this property name is taken to be a byte array.
    */
   public static final String NONCE_MATERIAL = "gnu.crypto.umac.nonce.material";

   /** Known test vector. */
   private static final String TV1 = "5FD764A6D3A9FD9D";
//   private static final String TV1 = "3E5A0E09198B0F94";

   private static final BigInteger MAX_NONCE_ITERATIONS =
         BigInteger.ONE.shiftLeft(16*8);

   // UMAC32 parameters (package private so can be shared with UHash32)
   static final int OUTPUT_LEN =    8;
   static final int L1_KEY_LEN = 1024;
   static final int KEY_LEN =      16;

   /** caches the result of the correctness test, once executed. */
   private static Boolean valid;

   private byte[] nonce;
   private UHash32 uhash32;
   private BigInteger nonceReuseCount;

   /** The authentication key for this instance. */
   private transient byte[] K;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public UMac32() {
      super("umac32");
   }

   /**
    * <p>Private constructor for cloning purposes.</p>
    *
    * @param that the instance to clone.
    */
   private UMac32(UMac32 that) {
      this();

      if (that.K != null) {
         this.K = (byte[]) that.K.clone();
      }
      if (that.nonce != null) {
         this.nonce = (byte[]) that.nonce.clone();
      }
      if (that.uhash32 != null) {
         this.uhash32 = (UHash32) that.uhash32.clone();
      }
      this.nonceReuseCount = that.nonceReuseCount;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      return new UMac32(this);
   }

   // gnu.crypto.mac.IMac interface implementation ----------------------------

   public int macSize() {
      return OUTPUT_LEN;
   }

   /**
    * <p>Initialising a <i>UMAC</i> instance consists of defining values for
    * the following parameters:</p>
    *
    * <ol>
    *    <li>Key Material: as the value of the attribute entry keyed by
    *    {@link #MAC_KEY_MATERIAL}. The value is taken to be a byte array
    *    containing the user-specified key material. The length of this array,
    *    if/when defined SHOULD be exactly equal to {@link #KEY_LEN}.</li>
    *
    *    <li>Nonce Material: as the value of the attribute entry keyed by
    *    {@link #NONCE_MATERIAL}. The value is taken to be a byte array
    *    containing the user-specified nonce material. The length of this array,
    *    if/when defined SHOULD be (a) greater than zero, and (b) less or equal
    *    to 16 (the size of the AES block).</li>
    * </ol>
    *
    * <p>For convenience, this implementation accepts that not both parameters
    * be always specified.</p>
    *
    * <ul>
    *    <li>If the <i>Key Material</i> is specified, but the <i>Nonce Material</i>
    *    is not, then this implementation, re-uses the previously set <i>Nonce
    *    Material</i> after (a) converting the bytes to an unsigned integer,
    *    (b) incrementing the number by one, and (c) converting it back to 16
    *    bytes.</li>
    *
    *    <li>If the <i>Nonce Material</i> is specified, but the <i>Key Material</i>
    *    is not, then this implementation re-uses the previously set <i>Key
    *    Material</i>.</li>
    * </ul>
    *
    * <p>This method throws an exception if no <i>Key Material</i> is specified
    * in the input map, and there is no previously set/defined <i>Key Material</i>
    * (from an earlier invocation of this method). If a <i>Key Material</i> can
    * be used, but no <i>Nonce Material</i> is defined or previously set/defined,
    * then a default value of all-zeroes shall be used.</p>
    *
    * @param attributes one or both of required parameters.
    * @throws InvalidKeyException the key material specified is not of the
    * correct length.
    */
   public void init(Map attributes)
   throws InvalidKeyException, IllegalStateException {
      byte[] key = (byte[]) attributes.get(MAC_KEY_MATERIAL);
      byte[] n = (byte[]) attributes.get(NONCE_MATERIAL);

      boolean newKey = (key != null);
      boolean newNonce = (n != null);

      if (newKey) {
         if (key.length != KEY_LEN) {
            throw new InvalidKeyException("Key length: "+String.valueOf(key.length));
         }
         K = key;
      } else {
         if (K == null) {
            throw new InvalidKeyException("Null Key");
         }
      }

      if (newNonce) {
         if (n.length < 1 || n.length > 16) {
            throw new IllegalArgumentException("Invalid Nonce length: "
                  +String.valueOf(n.length));
         }

         if (n.length < 16) { // pad with zeroes
            byte[] newN = new byte[16];
            System.arraycopy(n, 0, newN, 0, n.length);
            nonce = newN;
         } else {
            nonce = n;
         }

         nonceReuseCount = BigInteger.ZERO;
      } else if (nonce == null) { // use all-0 nonce if 1st time
         nonce = new byte[16];
         nonceReuseCount = BigInteger.ZERO;
      } else if (!newKey) { // increment nonce if still below max count
         nonceReuseCount = nonceReuseCount.add(BigInteger.ONE);
         if (nonceReuseCount.compareTo(MAX_NONCE_ITERATIONS) >= 0) {
            // limit reached. we SHOULD have a key
            throw new InvalidKeyException("Null Key and unusable old Nonce");
         }
         BigInteger N = new BigInteger(1, nonce);
         N = N.add(BigInteger.ONE).mod(MAX_NONCE_ITERATIONS);
         n = N.toByteArray();
         if (n.length == 16) {
            nonce = n;
         } else if (n.length < 16) {
            nonce = new byte[16];
            System.arraycopy(n, 0, nonce, 16 - n.length, n.length);
         } else {
            nonce = new byte[16];
            System.arraycopy(n, n.length - 16, nonce, 0, 16);
         }
      } else { // do nothing, re-use old nonce value
         nonceReuseCount = BigInteger.ZERO;
      }

      if (uhash32 == null) {
         uhash32 = new UHash32();
      }

      Map map = new HashMap();
      map.put(MAC_KEY_MATERIAL, K);
      uhash32.init(map);
   }

   public void update(byte b) {
      uhash32.update(b);
   }

   public void update(byte[] b, int offset, int len) {
      uhash32.update(b, offset, len);
   }

   public byte[] digest() {
      byte[] result = uhash32.digest();
      byte[] pad = pdf(); // pdf(K, nonce);
      for (int i = 0; i < OUTPUT_LEN; i++) {
         result[i] = (byte)(result[i] ^ pad[i]);
      }

      return result;
   }

   public void reset() {
      uhash32.reset();
   }

   public boolean selfTest() {
      if (valid == null) {
         byte[] key;
         try {
            key = "abcdefghijklmnop".getBytes("ASCII");
         } catch (UnsupportedEncodingException x) {
            throw new RuntimeException("ASCII not supported");
         }
         byte[] nonce = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
         UMac32 mac = new UMac32();
         Map attributes = new HashMap();
         attributes.put(MAC_KEY_MATERIAL, key);
         attributes.put(NONCE_MATERIAL, nonce);
         try {
            mac.init(attributes);
         } catch (InvalidKeyException x) {
            x.printStackTrace(System.err);
            return false;
         }

         byte[] data = new byte[128];
         data[0] = (byte) 0x80;

         mac.update(data, 0, 128);
         byte[] result = mac.digest();
//         System.out.println("UMAC test vector: "+Util.toString(result));
         valid = new Boolean(TV1.equals(Util.toString(result)));
      }
      return valid.booleanValue();
   }

   // helper methods ----------------------------------------------------------

   /**
    *
    * @return byte array of length 8 (or OUTPUT_LEN) bytes.
    */
   private byte[] pdf() {
      // Make Nonce 16 bytes by prepending zeroes. done (see init())

      // one AES invocation is enough for more than one PDF invocation
      // number of index bits needed = 1

      // Extract index bits and zero low bits of Nonce
      BigInteger Nonce = new BigInteger(1, nonce);
      int nlowbitsnum = Nonce.testBit(0) ? 1 : 0;
      Nonce = Nonce.clearBit(0);

      // Generate subkey, AES and extract indexed substring
      IRandom kdf = new UMacGenerator();
      Map map = new HashMap();
      map.put(IBlockCipher.KEY_MATERIAL, K);
//      map.put(IBlockCipher.CIPHER_BLOCK_SIZE, new Integer(128/8));
      map.put(UMacGenerator.INDEX, new Integer(128));
//      map.put(UMacGenerator.CIPHER, Registry.AES_CIPHER);
      kdf.init(map);
      byte[] Kp = new byte[KEY_LEN];
      try {
         kdf.nextBytes(Kp, 0, KEY_LEN);
      } catch (IllegalStateException x) {
         x.printStackTrace(System.err);
         throw new RuntimeException(String.valueOf(x));
      } catch (LimitReachedException x) {
         x.printStackTrace(System.err);
         throw new RuntimeException(String.valueOf(x));
      }
      IBlockCipher aes = CipherFactory.getInstance(Registry.AES_CIPHER);
      map.put(IBlockCipher.KEY_MATERIAL, Kp);
      try {
         aes.init(map);
      } catch (InvalidKeyException x) {
         x.printStackTrace(System.err);
         throw new RuntimeException(String.valueOf(x));
      } catch (IllegalStateException x) {
         x.printStackTrace(System.err);
         throw new RuntimeException(String.valueOf(x));
      }
      byte[] T = new byte[16];
      aes.encryptBlock(nonce, 0, T, 0);
      byte[] result = new byte[OUTPUT_LEN];
      System.arraycopy(T, nlowbitsnum, result, 0, OUTPUT_LEN);

      return result;
   }
}
