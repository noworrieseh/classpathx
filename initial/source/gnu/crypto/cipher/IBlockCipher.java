package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: IBlockCipher.java,v 1.1.1.1 2001-11-20 13:40:27 raif Exp $
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

import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.Map;

/**
 * The basic visible method of any symmetric key block cipher.<p>
 *
 * A symmetric key block cipher is a function that maps n-bit plaintext blocks
 * to n-bit ciphertext blocks; n being the cipher's <i>block size</i>. This
 * encryption function is parameterised by a k-bit key, and is invertible. Its
 * inverse is the decryption function.<p>`
 *
 * Possible initialisation values for an instance of that type are:
 * <ul>
 *    <li>The block size in which to operate this block cipher instance. This
 *    value is <b>optional</b>, if unspecified, the block cipher's default
 *    block size shall be used.
 *    <li>The byte array containing the user supplied key material to use for
 *    generating the cipher's session key(s). This value is <b>mandatory</b>
 *    and should be included in the initialisation parameters. If it isn't,
 *    a {@link java.lang.IllegalStateException} will be thrown if any method,
 *    other than <tt>reset()</tt> is invoked on the instance.</li>
 * </ul>
 *
 * @version $Revision: 1.1.1.1 $
 */
public interface IBlockCipher {

   // Constants
   // -------------------------------------------------------------------------

   /**
    * Property name of the user-supplied key material. The value associated to
    * this property name is taken to be a byte array.
    */
   String KEY_MATERIAL = "gnu.crypto.key.material";

   /**
    * Property name of the block size in which to operate a block cipher. The
    * value associated with this property name is taken to be a
    * {@link java.lang.Integer}.
    */
   String CIPHER_BLOCK_SIZE = "gnu.crypto.cipher.block.size";

   // Methods
   // -------------------------------------------------------------------------

   /** @return the canonical name of this instance. */
   String name();

   /** @return the default value, in bytes, of the algorithm's block size. */
   int defaultBlockSize();

   /** @return the default value, in bytes, of the algorithm's key size. */
   int defaultKeySize();

   /**
    * @return an <tt>Iterator</tt> over the supported block sizes. Each element
    * returned by this object is a <tt>java.lang.Integet</tt>.
    */
   Iterator blockSizes();

   /**
    * @return an <tt>Iterator</tt> over the supported key sizes. Each element
    * returned by this object is a <tt>java.lang.Integet</tt>.
    */
   Iterator keySizes();

   /**
    * Initialises the algorithm with designated attributes. Permissible names
    * and values are described in the class documentation above.
    *
    * @param attributes a set of name-value pairs that describe the desired
    * future instance behaviour.
    * @exception InvalidKeyException if the key data is invalid.
    * @exception IllegalStateException if the instance is already initialised.
    */
   void init(Map attributes)
   throws InvalidKeyException, IllegalStateException;

   /**
    * Returns the currently used block size for this algorithm.
    *
    * @return the currently used block size for this algorithm.
    * @exception IllegalStateException if the instance is not initialised.
    */
   int currentBlockSize() throws IllegalStateException;

   /**
    * Resets the algorithm instance for re-initialisation and use with other
    * characteristics. This method always succeeds.
    */
   void reset();

   /**
    * Encrypts exactly one block of plaintext.
    *
    * @param in the plaintext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the ciphertext.
    * @param outOffset index of <i>out</i> from which to store result.
    * @exception IllegalStateException if the instance is not initialised.
    */
   void encryptBlock(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException;

   /**
    * Decrypts exactly one block of ciphertext.
    *
    * @param in the plaintext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the ciphertext.
    * @param outOffset index of <i>out</i> from which to store result.
    * @exception IllegalStateException if the instance is not initialised.
    */
   void decryptBlock(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException;

   /**
    * A basic symmetric encryption/decryption test for all supported block and
    * key sizes.
    *
    * @return <tt>true</tt> if the implementation passes a basic symmetric
    * self-test. Returns <tt>false</tt> otherwise.
    */
   boolean selfTest();
}
