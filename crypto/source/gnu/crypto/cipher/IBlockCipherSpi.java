package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: IBlockCipherSpi.java,v 1.2 2001-12-04 12:56:08 raif Exp $
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

import java.security.InvalidKeyException;
import java.util.Iterator;

/**
 * Package-private interface exposing mandatory methods to be implemented by
 * concrete {@link gnu.crypto.cipher.BaseCipher} sub-classes.
 *
 * @version $Revision: 1.2 $
 */
interface IBlockCipherSpi extends Cloneable {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /**
    * Returns an {@link java.util.Iterator} over the supported block sizes.
    * Each element returned by this object is a {@link java.lang.Integer}.<p>
    *
    * @return an <tt>Iterator</tt> over the supported block sizes.
    */
   Iterator blockSizes();

   /**
    * Returns an {@link java.util.Iterator} over the supported key sizes. Each
    * element returned by this object is a {@link java.lang.Integer}.<p>
    *
    * @return an <tt>Iterator</tt> over the supported key sizes.
    */
   Iterator keySizes();

   /**
    * Expands a user-supplied key material into a session key for a designated
    * <i>block size</i>.<p>
    *
    * @param k the user-supplied key material.
    * @param bs the desired block size in bytes.
    * @return an Object encapsulating the session key.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception InvalidKeyException if the key data is invalid.
    */
   Object makeKey(byte[]k, int bs) throws InvalidKeyException;

   /**
    * Encrypts exactly one block of plaintext.<p>
    *
    * @param in the plaintext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the ciphertext.
    * @param outOffset index of <i>out</i> from which to store the result.
    * @param k the session key to use.
    * @param bs the block size to use.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception ArrayIndexOutOfBoundsException if there is not enough room in
    * either the plaintext or ciphertext buffers.
    */
   void
   encrypt(byte[] in, int inOffset, byte[] out, int outOffset, Object k, int bs);

   /**
    * Decrypts exactly one block of ciphertext.<p>
    *
    * @param in the ciphertext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the plaintext.
    * @param outOffset index of <i>out</i> from which to store the result.
    * @param k the session key to use.
    * @param bs the block size to use.
    * @exception IllegalArgumentException if the block size is invalid.
    * @exception ArrayIndexOutOfBoundsException if there is not enough room in
    * either the plaintext or ciphertext buffers.
    */
   void
   decrypt(byte[] in, int inOffset, byte[] out, int outOffset, Object k, int bs);
}
