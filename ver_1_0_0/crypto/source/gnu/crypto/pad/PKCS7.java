package gnu.crypto.pad;

// ----------------------------------------------------------------------------
// $Id: PKCS7.java,v 1.3 2002-01-11 21:50:51 raif Exp $
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
 * The implementation of the PKCS7 padding algorithm.<p>
 *
 * This algorithm is described for 8-byte blocks in [RFC-1423] and extended to
 * block sizes of up to 256 bytes in [PKCS-7].<p>
 *
 * References:<br>
 * <a href="http://www.ietf.org/rfc/rfc1423.txt">RFC-1423</a>: Privacy
 * Enhancement for Internet Electronic Mail: Part III: Algorithms, Modes, and
 * Identifiers.<br>
 * <a href="http://www.ietf.org/">IETF</a>.
 * <a href="http://www.rsasecurity.com/rsalabs/pkcs/pkcs-7/">[PKCS-7]</a>PKCS #7:
 * Cryptographic Message Syntax Standard - An RSA Laboratories Technical Note.<br>
 * <a href="http://www.rsasecurity.com/">RSA Security</a>.<p>
 *
 * @version $Revision: 1.3 $
 */
public final class PKCS7 extends BasePad {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "pkcs7";
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
   PKCS7() {
      super(Registry.PKCS7_PAD);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Implementation of abstract methods in BasePad
   // -------------------------------------------------------------------------

   public void setup() {
      if (blockSize < 2 || blockSize > 256) {
         throw new IllegalArgumentException();
      }
   }

   public byte[] pad(byte[] in, int offset, int length) {
      int padLength = blockSize;
      if (length % blockSize != 0) {
         padLength = blockSize - length % blockSize;
      }
      byte[] result = new byte[padLength];
      for (int i = 0; i < padLength; ) {
         result[i++] = (byte) padLength;
      }

      if (DEBUG && debuglevel > 8) {
         debug("padding: 0x"+Util.toString(result));
      }
      return result;
   }

   public int unpad(byte[] in, int offset, int length)
   throws WrongPaddingException {
      int limit = offset + length;
      int result = in[limit-1] & 0xFF;
      for (int i = 0; i < result; i++) {
         if (result != (in[--limit] & 0xFF)) {
            throw new WrongPaddingException();
         }
      }

      if (DEBUG && debuglevel > 8) {
         debug("padding length: "+String.valueOf(result));
      }
      return result;
   }
}
