package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: CTR.java,v 1.4 2002-06-08 05:10:40 raif Exp $
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

import java.math.BigInteger;

/**
 * <p>The implementation of the Counter Mode.</p>
 *
 * <p>The algorithm steps are formally described as follows:</p>
 *
 * <pre>
 *    CTR Encryption: O[j] = E(K)(T[j]); for j = 1, 2...n;
 *                    C[j] = P[j] ^ O[j]; for j = 1, 2...n.
 *    CTR Decryption: O[j] = E(K)(T[j]); for j = 1, 2...n;
 *                    P[j] = C[j] ^ O[j]; for j = 1, 2...n.
 * </pre>
 *
 * <p>where <code>P</code> is the plaintext, <code>C</code> is the ciphertext,
 * <code>E(K)</code> is the underlying block cipher encryption function
 * parametrised with the session key <code>K</code>, and <code>T</code> is the
 * <i>Counter</i>.</p>
 *
 * <p>This implementation, uses a standard incrementing function with a step of
 * 1, and an initial value similar to that described in the NIST document.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://csrc.nist.gov/encryption/modes/Recommendation/Modes01.pdf">
 *    Recommendation for Block Cipher Modes of Operation Methods and Techniques</a>,
 *    Morris Dworkin.</li>
 * </ol>
 *
 * @version $Revision: 1.4 $
 */
public class CTR extends BaseMode implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The current counter. */
   private BigInteger T;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial package-private constructor for use by the Factory class.</p>
    *
    * @param underlyingCipher the underlying cipher implementation.
    * @param cipherBlockSize the underlying cipher block size to use.
    */
   CTR(IBlockCipher underlyingCipher, int cipherBlockSize) {
      super(Registry.CTR_MODE, underlyingCipher, cipherBlockSize);
   }

   /**
    * <p>Private constructor for cloning purposes.</p>
    *
    * @param that the instance to clone.
    */
   private CTR(CTR that) {
      this((IBlockCipher) that.cipher.clone(), that.cipherBlockSize);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Cloneable interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      return new CTR(this);
   }

   // Implementation of abstract methods in BaseMode
   // -------------------------------------------------------------------------

   public void setup() {
      if (modeBlockSize != cipherBlockSize) {
         throw new IllegalArgumentException();
      }

      byte[] tBytes = new byte[modeBlockSize+1];
      tBytes[0] = (byte) 0x80;
      for (int i = 0; i < modeBlockSize; i++) {
         tBytes[i+1] = (byte)(256 - modeBlockSize + i);
      }

      T = new BigInteger(1, tBytes);
   }

   public void teardown() {
      T = null;
   }

   public void encryptBlock(byte[] in, int i, byte[] out, int o) {
      ctr(in, i, out, o);
   }

   public void decryptBlock(byte[] in, int i, byte[] out, int o) {
      ctr(in, i, out, o);
   }

   // own methods
   // -------------------------------------------------------------------------

   private void ctr(byte[] in, int inOffset, byte[] out, int outOffset) {
      T = T.add(BigInteger.ONE);
      byte[] O = T.toByteArray();
      int ndx = O.length - modeBlockSize;
      cipher.encryptBlock(O, ndx, O, ndx);
      for (int i = 0; i < modeBlockSize; i++) {
         out[outOffset++] = (byte)(in[inOffset++] ^ O[ndx++]);
      }
   }
}
