package gnu.crypto.hash;

// ----------------------------------------------------------------------------
// $Id: Sha160.java,v 1.1 2001-12-08 21:34:45 raif Exp $
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

import gnu.crypto.util.Util;
import java.io.PrintWriter;

/**
 * The Secure Hash Algorithm (SHA-1) is required for use with the Digital
 * Signature Algorithm (DSA) as specified in the Digital Signature Standard
 * (DSS) and whenever a secure hash algorithm is required for federal
 * applications. For a message of length less than 2^64 bits, the SHA-1
 * produces a 160-bit condensed representation of the message called a message
 * digest. The message digest is used during generation of a signature for the
 * message. The SHA-1 is also used to compute a message digest for the received
 * version of the message during the process of verifying the signature. Any
 * change to the message in transit will, with very high probability, result in
 * a different message digest, and the signature will fail to verify.
 *
 * The SHA-1 is designed to have the following properties: it is
 * computationally infeasible to find a message which corresponds to a given
 * message digest, or to find two different messages which produce the same
 * message digest.
 *
 * References:<br>
 * <a href="http://www.itl.nist.gov/fipspubs/fip180-1.htm">SECURE HASH
 * STANDARD</a><br>
 * Federal Information, Processing Standards Publication 180-1, 1995 April 17.
 *
 * @version $Revision: 1.1 $
 */
public class Sha160 extends BaseHash {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "sha-160";
   private static final boolean DEBUG = false;
   private static final int debuglevel = 9;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final int BLOCK_SIZE = 64; // default block size in bytes

   /** 160-bit interim result. */
   private int h0, h1, h2, h3, h4;

   private final int[] W = new int[80];

   private static final String DIGEST0 = "A9993E364706816ABA3E25717850C26C9CD0D89D";

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public Sha160() {
      super(HashFactory.SHA160_HASH, 20, BLOCK_SIZE);

      resetContext();
   }

   /**
    * private constructor for cloning purposes.
    *
    * @param md the instance to clone.
    */
   private Sha160(Sha160 md) {
      this();

      this.h0 = md.h0;
      this.h1 = md.h1;
      this.h2 = md.h2;
      this.h3 = md.h3;
      this.h4 = md.h4;
      this.count = md.count;
      this.buffer = (byte[]) md.buffer.clone();
   }

   // Cloneable interface implementation
   // -------------------------------------------------------------------------

   /** @return a cloned copy of this instance. */
   public Object clone() {
      return new Sha160(this);
   }

   // Implementation of concrete methods in BaseHash
   // -------------------------------------------------------------------------

   protected void transform(byte[] in, int offset) {
      int t;
      for (t = 0; t < 16; t++) {
         W[t] =  in[offset++]         << 24 |
                (in[offset++] & 0xFF) << 16 |
                (in[offset++] & 0xFF) <<  8 |
                (in[offset++] & 0xFF);
      }

      int T;
      for (t = 16; t < 80; t++) {
         T = W[t-3] ^ W[t-8] ^ W[t-14] ^ W[t-16];
         W[t] = T << 1 | T >>> 31;
      }

      int A = h0;
      int B = h1;
      int C = h2;
      int D = h3;
      int E = h4;

      // rounds 0-19
      for (t = 0; t < 20; ++ t) {
         T = (A << 5 | A >>> 27) + ((B & C) | (~B & D)) + E + W[t] + 0x5A827999;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      // rounds 20-39
      for (t = 20; t < 40; ++ t) {
         T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + W[t] + 0x6ED9EBA1;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      // rounds 40-59
      for (t = 40; t < 60; ++ t) {
         T = (A << 5 | A >>> 27) + (B & C | B & D | C & D) + E + W[t] + 0x8F1BBCDC;                // K_t
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      // rounds 60-79
      for (t = 60; t < 80; ++ t) {
         T = (A << 5 | A >>> 27) + (B ^ C ^ D) + E + W[t] + 0xCA62C1D6;
         E = D;
         D = C;
         C = B << 30 | B >>> 2;
         B = A;
         A = T;
      }

      h0 += A;
      h1 += B;
      h2 += C;
      h3 += D;
      h4 += E;
   }

   protected byte[] padBuffer() {
      int n = (int)(count % BLOCK_SIZE);
      int padding = (n < 56) ? (56 - n) : (120 - n);
      byte[] result = new byte[padding + 8];

      // padding is always binary 1 followed by binary 0s
      result[0] = (byte) 0x80;

      // save number of bits, casting the long to an array of 8 bytes
      long bits = count << 3;
      result[padding++] = (byte)(bits >>> 56);
      result[padding++] = (byte)(bits >>> 48);
      result[padding++] = (byte)(bits >>> 40);
      result[padding++] = (byte)(bits >>> 32);
      result[padding++] = (byte)(bits >>> 24);
      result[padding++] = (byte)(bits >>> 16);
      result[padding++] = (byte)(bits >>>  8);
      result[padding  ] = (byte) bits;

      return result;
   }

   protected byte[] getResult() {
      byte[] result = new byte[] {
         (byte)(h0 >>> 24), (byte)(h0 >>> 16), (byte)(h0 >>> 8), (byte) h0,
         (byte)(h1 >>> 24), (byte)(h1 >>> 16), (byte)(h1 >>> 8), (byte) h1,
         (byte)(h2 >>> 24), (byte)(h2 >>> 16), (byte)(h2 >>> 8), (byte) h2,
         (byte)(h3 >>> 24), (byte)(h3 >>> 16), (byte)(h3 >>> 8), (byte) h3,
         (byte)(h4 >>> 24), (byte)(h4 >>> 16), (byte)(h4 >>> 8), (byte) h4
      };

      return result;
   }

   protected void resetContext() {
      // magic SHA-1/RIPEMD160 initialisation constants
      h0 = 0x67452301;
      h1 = 0xEFCDAB89;
      h2 = 0x98BADCFE;
      h3 = 0x10325476;
      h4 = 0xC3D2E1F0;
   }

   public boolean selfTest() {
      Sha160 md = new Sha160();
      md.update((byte) 0x61); // a
      md.update((byte) 0x62); // b
      md.update((byte) 0x63); // c
      String result = Util.toString(md.digest());
      return DIGEST0.equals(result);
   }
}
