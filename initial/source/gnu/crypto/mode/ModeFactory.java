package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: ModeFactory.java,v 1.1.1.1 2001-11-20 13:40:40 raif Exp $
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

import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate block cipher modes of operations.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class ModeFactory {

   // Constants and variables
   // -------------------------------------------------------------------------

   static final String ECB_MODE = "ecb";
   static final String CTR_MODE = "ctr";
   static final String ICM_MODE = "icm";

   static final String CBC_MODE = "cbc";
   static final String CFB_MODE = "cfb";
   static final String OFB_MODE = "ofb";

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private ModeFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns an instance of a block cipher mode of operations given its name
    * and characteristics of the underlying block cipher.
    *
    * @param mode the case-insensitive name of the mode of operations.
    * @param cipher the case-insensitive name of the block cipher.
    * @param cipherBlockSize the block size, in bytes, of the underlying cipher.
    * @return an instance of the block cipher algorithm, operating in a given
    * mode of operations, or <tt>null</tt> if none found.
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
      }
//      else if (mode.equalsIgnoreCase(CBC_MODE)) {
//         result = new CBC(cipher, cipherBlockSize);
//      } else if (mode.equalsIgnoreCase(CFB_MODE)) {
//         result = new CFB(cipher, cipherBlockSize);
//      } else if (mode.equalsIgnoreCase(OFB_MODE)) {
//         result = new OFB(cipher, cipherBlockSize);
//      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * Returns a {@link java.util.Set} of names of mode supported by this
    * <i>Factory</i>.
    *
    * @return a {@link java.util.Set} of mode names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(ECB_MODE);
      hs.add(CTR_MODE);
      hs.add(ICM_MODE);
//      hs.add(CBC_MODE);
//      hs.add(CFB_MODE);
//      hs.add(OFB_MODE);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
