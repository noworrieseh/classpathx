package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: ModeFactory.java,v 1.4 2002-06-08 05:18:04 raif Exp $
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

import gnu.crypto.Registry;
import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>A <i>Factory</i> to instantiate block cipher modes of operations.</p>
 *
 * @version $Revision: 1.4 $
 */
public class ModeFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private ModeFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns an instance of a block cipher mode of operations given its name
    * and characteristics of the underlying block cipher.</p>
    *
    * @param mode the case-insensitive name of the mode of operations.
    * @param cipher the case-insensitive name of the block cipher.
    * @param cipherBlockSize the block size, in bytes, of the underlying cipher.
    * @return an instance of the block cipher algorithm, operating in a given
    * mode of operations, or <code>null</code> if none found.
    * @exception InternalError if either the mode or the underlying block cipher
    * implementation does not pass its self-test.
    */
   public static IMode
   getInstance(String mode, String cipher, int cipherBlockSize) {
      if (mode == null || cipher == null) {
         return null;
      }

      mode = mode.trim();
      cipher = cipher.trim();
      IBlockCipher cipherImpl = CipherFactory.getInstance(cipher);
      if (cipherImpl == null) {
         return null;
      }

      return getInstance(mode, cipherImpl, cipherBlockSize);
   }

   public static IMode
   getInstance(String mode, IBlockCipher cipher, int cipherBlockSize) {
      // ensure that cipherBlockSize is valid for the chosen underlying cipher
      boolean ok = false;
      for (Iterator it = cipher.blockSizes(); it.hasNext(); ) {
         ok = (cipherBlockSize == ((Integer) it.next()).intValue());
         if (ok) {
            break;
         }
      }

      if (!ok) {
         throw new IllegalArgumentException("cipherBlockSize");
      }

      IMode result = null;
      if (mode.equalsIgnoreCase(ECB_MODE)) {
         result = new ECB(cipher, cipherBlockSize);
      } else if (mode.equalsIgnoreCase(CTR_MODE)) {
         result = new CTR(cipher, cipherBlockSize);
      } else if (mode.equalsIgnoreCase(ICM_MODE)) {
         result = new ICM(cipher, cipherBlockSize);
      } else if (mode.equalsIgnoreCase(OFB_MODE)) {
         result = new OFB(cipher, cipherBlockSize);
      }
//      else if (mode.equalsIgnoreCase(CBC_MODE)) {
//         result = new CBC(cipher, cipherBlockSize);
//      } else if (mode.equalsIgnoreCase(CFB_MODE)) {
//         result = new CFB(cipher, cipherBlockSize);
//      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * <p>Returns a {@link java.util.Set} of names of mode supported by this
    * <i>Factory</i>.</p>
    *
    * @return a {@link java.util.Set} of mode names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(ECB_MODE);
      hs.add(CTR_MODE);
      hs.add(ICM_MODE);
      hs.add(OFB_MODE);
//      hs.add(CBC_MODE);
//      hs.add(CFB_MODE);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
