package gnu.crypto.pad;

// ----------------------------------------------------------------------------
// $Id: TBC.java,v 1.3 2002-01-11 21:50:16 raif Exp $
//
// Copyright (C) 2001, 2002 Free Software Foundation, Inc.
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
import gnu.crypto.util.Util;

import java.io.PrintWriter;

/**
 * The implementation of the Trailing Bit Complement (TBC) padding algorithm.<p>
 *
 * In this mode, "...the data string is padded at the trailing end with the
 * complement of the trailing bit of the unpadded message: if the trailing bit
 * is <tt>1</tt>, then <tt>0</tt> bits are appended, and if the trailing bit is
 * <tt>0</tt>, then <tt>1</tt> bits are appended. As few bits are added as are
 * necessary to meet the formatting size requirement."<p>
 *
 * References:<br>
 * <a href="http://csrc.nist.gov/encryption/modes/Recommendation/Modes01.pdf">
 * Recommendation for Block Cipher Modes of Operation Methods and Techniques</a>,
 * Morris Dworkin.<p>
 *
 * @version $Revision: 1.3 $
 */
public final class TBC extends BasePad {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "tbc";
   private static final boolean DEBUG = false;
   private static final int debuglevel = 9;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial package-private constructor for use by the <i>Factory</i> class.<p>
    *
    * @see gnu.crypto.pad.PadFactory
    */
   TBC() {
      super(Registry.TBC_PAD);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Implementation of abstract methods in BasePad
   // -------------------------------------------------------------------------

   public void setup() {
      if (blockSize < 1 || blockSize > 256) {
         throw new IllegalArgumentException();
      }
   }

   public byte[] pad(byte[] in, int offset, int length) {
      int padLength = blockSize;
      if (length % blockSize != 0) {
         padLength = blockSize - length % blockSize;
      }
      byte[] result = new byte[padLength];
      int lastBit = in[offset+length-1] & 0x01;
      if (lastBit == 0) {
         for (int i = 0; i < padLength; ) {
            result[i++] = 0x01;
         }
      } // else it's already set to zeroes by virtue of initialisation

      if (DEBUG && debuglevel > 8) {
         debug("padding: 0x"+Util.toString(result));
      }
      return result;
   }

   public int unpad(byte[] in, int offset, int length)
   throws WrongPaddingException {
      int limit = offset + length - 1;
      int lastBit = in[limit] & 0xFF;
      int result = 0;
      while (lastBit == (in[limit] & 0xFF)) {
         result++;
         limit--;
      }

      if (result > length) {
         throw new WrongPaddingException();
      }

      if (DEBUG && debuglevel > 8) {
         debug("padding length: "+String.valueOf(result));
      }
      return result;
   }
}
