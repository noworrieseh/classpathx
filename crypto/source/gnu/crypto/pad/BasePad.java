package gnu.crypto.pad;

// ----------------------------------------------------------------------------
// $Id: BasePad.java,v 1.1.1.1 2001-11-20 13:40:40 raif Exp $
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

/**
 * An abstract class to facilitate implementing padding algorithms.
 *
 * @version $Revision: 1.1.1.1 $
 */
public abstract class BasePad implements IPad {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of the padding algorithm. */
   protected String name;

   /** The block size, in bytes, for this instance. */
   protected int blockSize;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor for use by concrete subclasses. */
   protected BasePad(String name) {
      super();

      this.name = name;
      blockSize = -1;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // IPad interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      StringBuffer sb = new StringBuffer(name);
      if (blockSize != -1) {
         sb.append('-').append(String.valueOf(8*blockSize));
      }
      return sb.toString();
   }

   public void init(int bs) throws IllegalStateException {
      if (blockSize != -1) {
         throw new IllegalStateException();
      }
      blockSize = bs;
   }

   public void reset() {
      blockSize = -1;
   }

   public boolean selfTest() {
      byte[] padBytes;
      int offset = 5;
      int limit = 1024;
      byte[] in = new byte[limit];
      for (int bs = 2; bs < 256; bs++) {
         this.reset();
         this.init(bs);
         for (int i = 0; i < limit-offset-blockSize; i++) {
            padBytes = pad(in, offset, i);
            if (((i + padBytes.length) % blockSize) != 0) {
               return false;
            }

            System.arraycopy(padBytes, 0, in, offset+i, padBytes.length);
            try {
               if (padBytes.length != unpad(in, offset, i+padBytes.length)) {
                  return false;
               }
            } catch (WrongPaddingException x) {
               return false;
            }
         }
      }

      return true;
   }

   // abstract methods to implement by subclasses
   // -------------------------------------------------------------------------

   public abstract void setup();

   public abstract byte[] pad(byte[] in, int offset, int length);

   public abstract int unpad(byte[] in, int offset, int length)
   throws WrongPaddingException;
}
