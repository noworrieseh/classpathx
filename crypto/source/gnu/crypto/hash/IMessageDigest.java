package gnu.crypto.hash;

// ----------------------------------------------------------------------------
// $Id: IMessageDigest.java,v 1.8 2002-05-14 08:50:38 raif Exp $
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

/**
 * <p>The basic visible methods of any hash algorithm.</p>
 *
 * <p>A hash (or message digest) algorithm produces its output by iterating a
 * basic compression function on blocks of data.</p>
 *
 * @version $Revision: 1.8 $
 */
public interface IMessageDigest extends Cloneable {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns the canonical name of this algorithm.</p>
    *
    * @return the canonical name of this instance.
    */
   String name();

   /**
    * <p>Returns the output length in bytes of this message digest algorithm.</p>
    *
    * @return the output length in bytes of this message digest algorithm.
    */
   int hashSize();

   /**
    * <p>Returns the algorithm's (inner) block size in bytes.</p>
    *
    * @return the algorithm's inner block size in bytes.
    */
   int blockSize();

   /**
    * <p>Continues a message digest operation using the input byte.</p>
    *
    * @param b the input byte to digest.
    */
   void update(byte b);

   /**
    * <p>Continues a message digest operation, by filling the buffer, processing
    * data in the algorithm's HASH_SIZE-bit block(s), updating the context and
    * count, and buffering the remaining bytes in buffer for the next
    * operation.</p>
    *
    * @param in the input block.
    * @param offset start of meaningful bytes in input block.
    * @param length number of bytes, in input block, to consider.
    */
   void update(byte[] in, int offset, int length);

   /**
    * <p>Completes the message digest by performing final operations such as
    * padding and resetting the instance.</p>
    *
    * @return the array of bytes representing the hash value.
    */
   byte[] digest();

   /**
    * <p>Resets the current context of this instance clearing any eventually cached
    * intermediary values.</p>
    */
   void reset();

   /**
    * <p>A basic test. Ensures that the digest of a pre-determined message is equal
    * to a known pre-computed value.</p>
    *
    * @return <tt>true</tt> if the implementation passes a basic self-test.
    * Returns <tt>false</tt> otherwise.
    */
   boolean selfTest();

   /**
    * <p>Returns a clone copy of this instance.</p>
    *
    * @return a clone copy of this instance.
    */
   Object clone();
}
