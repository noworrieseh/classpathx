package gnu.crypto.mac;

// ----------------------------------------------------------------------------
// $Id: HMac.java,v 1.3 2002-07-06 23:48:40 raif Exp $
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
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.MD5;
import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>The implementation of the <i>HMAC</i> (Keyed-Hash Message Authentication
 * Code).</p>
 *
 * <p><i>HMAC</i> can be used in combination with any iterated cryptographic
 * hash function. <i>HMAC</i> also uses a <i>secret key</i> for calculation and
 * verification of the message authentication values. The main goals behind this
 * construction are</p>
 *
 * <ul>
 *    <li>To use, without modifications, available hash functions. In
 *    particular, hash functions that perform well in software, and for which
 *    code is freely and widely available.</li>
 *
 *    <li>To preserve the original performance of the hash function without
 *    incurring a significant degradation.</li>
 *
 *    <li>To use and handle keys in a simple way.</li>
 *
 *    <li>To have a well understood cryptographic analysis of the strength of
 *    the authentication mechanism based on reasonable assumptions on the
 *    underlying hash function.</li>
 *
 *    <li>To allow for easy replaceability of the underlying hash function in
 *    case that faster or more secure hash functions are found or required.</li>
 * </ul>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.ietf.org/rfc/rfc-2104.txt">RFC 2104</a>HMAC:
 *    Keyed-Hashing for Message Authentication.<br>
 *    H. Krawczyk, M. Bellare, and R. Canetti.</li>
 * </ol>
 *
 * @version $Revision: 1.3 $
 */
public class HMac extends BaseMac {

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final byte IPAD_BYTE = 0x36;
   private static final byte OPAD_BYTE = 0x5C;

   /** caches the result of the correctness test, once executed. */
   private static Boolean valid;

   protected int macSize;
   protected int blockSize;
   protected IMessageDigest ipadHash;
   protected IMessageDigest opadHash;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param underlyingHash the underlying hash algorithm instance.
    */
   protected HMac(IMessageDigest underlyingHash) {
      super(Registry.HMAC_NAME_PREFIX + underlyingHash.name(), underlyingHash);

      this.blockSize = underlyingHash.blockSize();
      this.macSize = underlyingHash.hashSize();
      ipadHash = opadHash = null;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      HMac result = new HMac((IMessageDigest) this.underlyingHash.clone());
      result.truncatedSize = this.truncatedSize;
      if (this.ipadHash != null) {
         result.ipadHash = (IMessageDigest) this.ipadHash.clone();
      }
      if (this.opadHash != null) {
         result.opadHash = (IMessageDigest) this.opadHash.clone();
      }

      return result;
   }

   // implementation of abstract methods in BaseMac ---------------------------

   public void init(Map attributes)
   throws InvalidKeyException, IllegalStateException {
      Integer ts = (Integer) attributes.get(TRUNCATED_SIZE);
      truncatedSize = (ts == null ? macSize : ts.intValue());
      if (truncatedSize < (macSize / 2)) {
         throw new IllegalArgumentException("Truncated size too small");
      } else if (truncatedSize < 10) {
         throw new IllegalArgumentException("Truncated size less than 80 bits");
      }

      // we dont use/save the key outside this method
      byte[] K = (byte[]) attributes.get(MAC_KEY_MATERIAL);
      if (K == null) { // take it as an indication to re-use previous key if set
         if (ipadHash == null) {
            throw new InvalidKeyException("Null key");
         }
         // we already went through the motions; ie. up to step #4.  re-use
         underlyingHash = (IMessageDigest) ipadHash.clone();
         return;
      }

      if (K.length < macSize) {
         throw new InvalidKeyException("Key too short");
      } else if (K.length != blockSize) {
         // (1) append zeros to the end of K to create a B byte string
         //     (e.g., if K is of length 20 bytes and B=64, then K will be
         //     appended with 44 zero bytes 0x00)
         int limit = (K.length > blockSize) ? blockSize : K.length;
         byte[] newK = new byte[blockSize];
         System.arraycopy(K, 0, newK, 0, limit);
         K = newK;
      }

      underlyingHash.reset();
      opadHash = (IMessageDigest) underlyingHash.clone();

      // (2) XOR (bitwise exclusive-OR) the B byte string computed in step
      //     (1) with ipad
      // (3) append the stream of data 'text' to the B byte string resulting
      //     from step (2)
      // (4) apply H to the stream generated in step (3)
      for (int i = 0; i < blockSize; i++) {
         underlyingHash.update((byte)(K[i] ^ IPAD_BYTE));
         opadHash.update((byte)(K[i] ^ OPAD_BYTE));
      }

      ipadHash = (IMessageDigest) underlyingHash.clone();
      K = null;
   }

   public byte[] digest() {
      if (ipadHash == null) {
         throw new IllegalStateException("HMAC not initialised");
      }

      byte[] out = underlyingHash.digest();
      // (5) XOR (bitwise exclusive-OR) the B byte string computed in
      //     step (1) with opad
      underlyingHash = (IMessageDigest) opadHash.clone();
      // (6) append the H result from step (4) to the B byte string
      //     resulting from step (5)
      underlyingHash.update(out, 0, macSize);
      // (7) apply H to the stream generated in step (6) and output
      //     the result
      out = underlyingHash.digest(); // which also resets the underlying hash

      // truncate and return
      if (truncatedSize == macSize)
         return out;

      byte[] result = new byte[truncatedSize];
      System.arraycopy(out, 0, result, 0, truncatedSize);

      return result;
   }

   public boolean selfTest() {
      if (valid == null) {
         try {
            IMac mac = new HMac(new MD5()); // use rfc-2104 test vectors
            String tv1 = "9294727A3638BB1C13F48EF8158BFC9D";
            String tv3 = "56BE34521D144C88DBB8C733F0E8B3F6";
            byte[] k1 = new byte[] {
               0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B,
               0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B, 0x0B
            };
            byte[] k3 = new byte[] {
               (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
               (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
               (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
               (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA
            };
            byte[] data = new byte[50];
            for (int i = 0; i < 50; ) {
               data[i++] = (byte) 0xDD;
            }

            HashMap map = new HashMap();

            // test vector #1
            map.put(MAC_KEY_MATERIAL, k1);
            mac.init(map);
            mac.update("Hi There".getBytes("ASCII"), 0, 8);
            if (!tv1.equals(Util.toString(mac.digest()))) {
               valid = Boolean.FALSE;
            }

            // test #2 is not used since it causes a "Key too short" exception

            // test vector #3
            map.put(MAC_KEY_MATERIAL, k3);
            mac.init(map);
            mac.update(data, 0, 50);
            if (!tv3.equals(Util.toString(mac.digest()))) {
               valid = Boolean.FALSE;
            }
            valid = Boolean.TRUE;
         } catch (Exception x) {
            x.printStackTrace(System.err);
            valid = Boolean.FALSE;
         }
      }
      return valid.booleanValue();
   }
}
