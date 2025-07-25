package gnu.crypto.hash;

// ----------------------------------------------------------------------------
// $Id: BaseHash.java,v 1.7 2002-05-14 08:50:38 raif Exp $
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
 * <p>A base abstract class to facilitate hash implementations.</p>
 *
 * @version $Revision: 1.7 $
 */
public abstract class BaseHash implements IMessageDigest {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of the hash. */
   protected String name;

   /** The hash (output) size in bytes. */
   protected int hashSize;

   /** The hash (inner) block size in bytes. */
   protected int blockSize;

   /** Number of bytes processed so far. */
   protected long count;

   /** Temporary input buffer. */
   protected byte[] buffer;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name prefix of this instance.
    * @param hashSize the block size of the output in bytes.
    * @param blockSize the block size of the internal transform.
    */
   protected BaseHash(String name, int hashSize, int blockSize) {
      super();

      this.name = name;
      this.hashSize = hashSize;
      this.blockSize = blockSize;
      this.buffer = new byte[blockSize];

      resetContext();
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // IMessageDigest interface implementation ---------------------------------

   public String name() {
      return name;
   }

   public int hashSize() {
      return hashSize;
   }

   public int blockSize() {
      return blockSize;
   }

   public void update(byte b) {
      // compute number of bytes still unhashed; ie. present in buffer
      int i = (int)(count % blockSize);
      count++;
      buffer[i] = b;
      if (i == (blockSize - 1)) {
         transform(buffer, 0);
      }
   }

   public void update(byte[] b, int offset, int len) {
      int n = (int)(count % blockSize);
      count += len;
      int partLen = blockSize - n;
      int i = 0;

      if (len >= partLen) {
         System.arraycopy(b, offset, buffer, n, partLen);
         transform(buffer, 0);
         for (i = partLen; i + blockSize - 1 < len; i+= blockSize) {
            transform(b, offset + i);
         }
         n = 0;
      }

      if (i < len) {
         System.arraycopy(b, offset + i, buffer, n, len - i);
      }
   }

   public byte[] digest() {
      byte[] tail = padBuffer(); // pad remaining bytes in buffer
      update(tail, 0, tail.length); // last transform of a message
      byte[] result = getResult(); // make a result out of context

      reset(); // reset this instance for future re-use

      return result;
   }

   public void reset() { // reset this instance for future re-use
      count = 0L;
      for (int i = 0; i < blockSize; ) {
         buffer[i++] = 0;
      }

      resetContext();
   }

   // methods to be implemented by concrete subclasses ------------------------

   public abstract Object clone();

   public abstract boolean selfTest();

   /**
    * <p>Returns the byte array to use as padding before completing a hash
    * operation.</p>
    *
    * @return the bytes to pad the remaining bytes in the buffer before
    * completing a hash operation.
    */
   protected abstract byte[] padBuffer();

   /**
    * <p>Constructs the result from the contents of the current context.</p>
    *
    * @return the output of the completed hash operation.
    */
   protected abstract byte[] getResult();

   /** Resets the instance for future re-use. */
   protected abstract void resetContext();

   /**
    * <p>The block digest transformation per se.</p>
    *
    * @param in the <i>blockSize</i> long block, as an array of bytes to digest.
    * @param offset the index where the data to digest is located within the
    * input buffer.
    */
   protected abstract void transform(byte[] in, int offset);
}
