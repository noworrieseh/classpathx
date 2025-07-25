package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: CipherFactory.java,v 1.5 2002-06-28 13:07:37 raif Exp $
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>A <i>Factory</i> to instantiate symmetric block cipher instances.</p>
 *
 * @version $Revision: 1.5 $
 */
public class CipherFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private CipherFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns an instance of a block cipher given its name.</p>
    *
    * @param name the case-insensitive name of the symmetric-key block cipher
    * algorithm.
    * @return an instance of the designated cipher algorithm, or
    * <code>null</code> if none is found.
    * @exception InternalError if the implementation does not pass its
    * self-test.
    */
   public static final IBlockCipher getInstance(String name) {
      if (name == null) {
         return null;
      }

      name = name.trim();
      IBlockCipher result = null;
      if (name.equalsIgnoreCase(ANUBIS_CIPHER)) {
         result = new Anubis();
      } else if (name.equalsIgnoreCase(KHAZAD_CIPHER)) {
         result = new Khazad();
      } else if (name.equalsIgnoreCase(RIJNDAEL_CIPHER)
              || name.equalsIgnoreCase(AES_CIPHER)) {
         result = new Rijndael();
      } else if (name.equalsIgnoreCase(SERPENT_CIPHER)) {
         result = new Serpent();
      } else if (name.equalsIgnoreCase(SQUARE_CIPHER)) {
         result = new Square();
      } else if (name.equalsIgnoreCase(TWOFISH_CIPHER)) {
         result = new Twofish();
      } else if (name.equalsIgnoreCase(NULL_CIPHER)) {
         result = new NullCipher();
      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * <p>Returns a {@link java.util.Set} of symmetric key block cipher
    * implementation names supported by this <i>Factory</i>.</p>
    *
    * @return a {@link java.util.Set} of block cipher names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(ANUBIS_CIPHER);
      hs.add(KHAZAD_CIPHER);
      hs.add(RIJNDAEL_CIPHER);
      hs.add(SERPENT_CIPHER);
      hs.add(SQUARE_CIPHER);
      hs.add(TWOFISH_CIPHER);
      hs.add(NULL_CIPHER);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
