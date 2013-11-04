package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: CTR.java,v 1.1.1.1 2001-11-20 13:40:40 raif Exp $
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

import gnu.crypto.cipher.IBlockCipher;
import java.math.BigInteger;

/**
 * The implementation of the Counter Mode.<p>
 *
 * The algorithm steps are formally described as follows:
 * <pre>
 *    CTR Encryption: O[j] = E(K)(T[j]); for j = 1, 2...n;
 *                    C[j] = P[j] ^ O[j]; for j = 1, 2...n.
 *    CTR Decryption: O[j] = E(K)(T[j]); for j = 1, 2...n;
 *                    P[j] = C[j] ^ O[j]; for j = 1, 2...n.
 * </pre>
 * where <tt>P</tt> is the plaintext, <tt>C</tt> is the ciphertext, <tt>E(K)</tt>
 * is the underlying block cipher encryption function parametrised with the
 * session key <tt>K</tt>, and <tt>T</tt> is the Counter.<p>
 *
 * This implementation, uses a standard incrementing function with a step of 1,
 * and an initial value similar to that described in the NIST document.
 *
 * References:<br>
 * <a href="../Modes01.pdf">[MODES]</a>Recommendation for Block Cipher Modes of
 * Operation Methods and Techniques, Morris Dworkin.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class CTR extends BaseMode implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The current counter. */
   private BigInteger T;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial package-private constructor for use by the Factory class.
    *
    * @param underlyingCipher the underlying cipher implementation.
    * @param cipherBlockSize the underlying cipher block size to use.
    */
   CTR(IBlockCipher underlyingCipher, int cipherBlockSize) {
      super(ModeFactory.CTR_MODE, underlyingCipher, cipherBlockSize);
   }

   /** Private constructor for cloning purposes. */
   private CTR(CTR that) {
      this(that.cipher, that.cipherBlockSize);
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
