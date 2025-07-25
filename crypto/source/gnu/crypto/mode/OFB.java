package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: OFB.java,v 1.1 2002-06-08 05:07:40 raif Exp $
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
import gnu.crypto.cipher.IBlockCipher;

/**
 * <p>The Output Feedback (OFB) mode is a confidentiality mode that requires a
 * unique <code>IV</code> for every message that is ever encrypted under the
 * given key. The OFB mode is defined as follows:</p>
 *
 * <ul>
 *    <li>OFB Encryption:
 *    <ul>
 *       <li>I<sub>1</sub> = IV;</li>
 *       <li>I<sub>j</sub> = O<sub>j -1</sub> for j = 2...n;</li>
 *       <li>O<sub>j</sub> = CIPH<sub>K</sub>(I<sub>j</sub>) for j = 1, 2...n;</li>
 *       <li>C<sub>j</sub> = P<sub>j</sub> XOR O<sub>j</sub> for j = 1, 2...n.</li>
 *    </ul></li>
 * <li>OFB Decryption:
 *    <ul>
 *       <li>I<sub>1</sub> = IV;</li>
 *       <li>I<sub>j</sub> = O<sub>j -1</sub> for j = 2...n;</li>
 *       <li>O<sub>j</sub> = CIPH<sub>K</sub>(I<sub>j</sub>) for j = 1, 2...n;</li>
 *       <li>P<sub>j</sub> = C<sub>j</sub> XOR O<sub>j</sub> for j = 1, 2...n.</li>
 *    </ul></li>
 * </ul>
 *
 * <p>In OFB encryption, the <code>IV</code> is transformed by the forward
 * cipher function to produce the first output block. The first output block is
 * exclusive-ORed with the first plaintext block to produce the first ciphertext
 * block. The first output block is then transformed by the forward cipher
 * function to produce the second output block. The second output block is
 * exclusive-ORed with the second plaintext block to produce the second
 * ciphertext block, and the second output block is transformed by the forward
 * cipher function to produce the third output block. Thus, the successive
 * output blocks are produced from enciphering the previous output blocks, and
 * the output blocks are exclusive-ORed with the corresponding plaintext blocks
 * to produce the ciphertext blocks.</p>
 *
 * <p>In OFB decryption, the <code>IV</code> is transformed by the forward cipher
 * function to produce the first output block. The first output block is
 * exclusive-ORed with the first ciphertext block to recover the first plaintext
 * block. The first output block is then transformed by the forward cipher
 * function to produce the second output block. The second output block is
 * exclusive-ORed with the second ciphertext block to produce the second
 * plaintext block, and the second output block is also transformed by the
 * forward cipher function to produce the third output block. Thus, the
 * successive output blocks are produced from enciphering the previous output
 * blocks, and the output blocks are exclusive-ORed with the corresponding
 * ciphertext blocks to recover the plaintext blocks.</p>
 *
 * <p>In both OFB encryption and OFB decryption, each forward cipher function
 * (except the first) depends on the results of the previous forward cipher
 * function; therefore, multiple forward cipher functions cannot be performed
 * in parallel. However, if the <code>IV</code> is known, the output blocks can
 * be generated prior to the availability of the plaintext or ciphertext data.</p>
 *
 * <p>The OFB mode requires a unique <code>IV</code> for every message that is
 * ever encrypted under the given key. If, contrary to this requirement, the
 * same <code>IV</code> is used for the encryption of more than one message,
 * then the confidentiality of those messages may be compromised. In particular,
 * if a plaintext block of any of these messages is known, say, the j<sup>th</sup>
 * plaintext block, then the j<sup>th</sup> output of the forward cipher
 * function can be determined easily from the j<sup>th</sup> ciphertext block of
 * the message. This information allows the j<sup>th</sup> plaintext block of
 * any other message that is encrypted using the same <code>IV</code> to be
 * easily recovered from the jth ciphertext block of that message.</p>
 *
 * <p>Confidentiality may similarly be compromised if any of the input blocks to
 * the forward cipher function for the encryption of a message is used as the
 * <code>IV</code> for the encryption of another message under the given key.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://csrc.nist.gov/encryption/modes/Recommendation/Modes01.pdf">
 *    Recommendation for Block Cipher Modes of Operation Methods and Techniques</a>,
 *    Morris Dworkin.</li>
 * </ol>
 *
 * @version $Revision: 1.1 $
 */
public class OFB extends BaseMode implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   private byte[] outputBlock;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial package-private constructor for use by the Factory class.</p>
    *
    * @param underlyingCipher the underlying cipher implementation.
    * @param cipherBlockSize the underlying cipher block size to use.
    */
   OFB(IBlockCipher underlyingCipher, int cipherBlockSize) {
      super(Registry.OFB_MODE, underlyingCipher, cipherBlockSize);
   }

   /**
    * <p>Private constructor for cloning purposes.</p>
    *
    * @param that the mode to clone.
    */
   private OFB(OFB that) {
      this((IBlockCipher) that.cipher.clone(), that.cipherBlockSize);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      return new OFB(this);
   }

   // Implementation of abstract methods in BaseMode --------------------------

   public void setup() {
      if (modeBlockSize != cipherBlockSize) {
         throw new IllegalArgumentException(IMode.MODE_BLOCK_SIZE);
      }

      outputBlock = (byte[]) iv.clone();
   }

   public void teardown() {
   }

   public void encryptBlock(byte[] in, int i, byte[] out, int o) {
      cipher.encryptBlock(outputBlock, 0, outputBlock, 0);
      for (int j = 0; j < cipherBlockSize; ) {
         out[o++] = (byte)(in[i++] ^ outputBlock[j++]);
      }
   }

   public void decryptBlock(byte[] in, int i, byte[] out, int o) {
      this.encryptBlock(in, i, out, o);
   }
}
