package gnu.crypto.pad;

// ----------------------------------------------------------------------------
// $Id: IPad.java,v 1.2 2001-12-04 12:56:08 raif Exp $
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

/**
 * The basic visible methods of any padding algorithm.<p>
 *
 * Padding algorithms serve to <i>pad</i> and <i>unpad</i> byte arrays usually
 * as the last step in an <i>encryption</i> or respectively a <i>decryption</i>
 * operation. Their input buffers are usually those processed by instances of
 * {@link gnu.crypto.mode.IMode} and/or {@link gnu.crypto.cipher.IBlockCipher}.
 *
 * @version $Revision: 1.2 $
 */
public interface IPad {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /** @return the canonical name of this instance. */
   String name();

   /**
    * Initialises the padding scheme with a designated block size.
    *
    * @param bs the designated block size.
    * @exception IllegalStateException if the instance is already initialised.
    * @exception IllegalArgumentException if the block size value is invalid.
    */
   void init(int bs) throws IllegalStateException;

   /**
    * Returns the byte sequence that should be appended to the designated input.
    *
    * @param in the input buffer containing the bytes to pad.
    * @param offset the starting index of meaningful data in <i>in</i>.
    * @param length the number of meaningful bytes in <i>in</i>.
    * @return the possibly 0-byte long sequence to be appended to the designated
    * input.
    */
   byte[] pad(byte[] in, int offset, int length);

   /**
    * Returns the number of bytes to discard from a designated input buffer.
    *
    * @param in the input buffer containing the bytes to unpad.
    * @param offset the starting index of meaningful data in <i>in</i>.
    * @param length the number of meaningful bytes in <i>in</i>.
    * @return the number of bytes to discard, to the left of index position
    * <tt>offset + length</tt> in <i>in</i>. In other words, if the return
    * value of a successful invocation of this method is <tt>result</tt>, then
    * the unpadded byte sequence will be <tt>offset + length - result</tt> bytes
    * in <i>in</i>, starting from index position <tt>offset</tt>.
    * @exception WrongPaddingException if the data is not terminated with the
    * expected padding bytes.
    */
   int unpad(byte[] in, int offset, int length) throws WrongPaddingException;

   /**
    * Resets the scheme instance for re-initialisation and use with other
    * characteristics. This method always succeeds.
    */
   void reset();

   /**
    * A basic symmetric pad/unpad test.
    *
    * @return <tt>true</tt> if the implementation passes a basic symmetric
    * self-test. Returns <tt>false</tt> otherwise.
    */
   boolean selfTest();
}
