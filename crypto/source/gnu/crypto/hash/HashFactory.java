package gnu.crypto.hash;

// ----------------------------------------------------------------------------
// $Id: HashFactory.java,v 1.7 2002-06-12 10:24:14 raif Exp $
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
 * <p>A <i>Factory</i> to instantiate message digest algorithm instances.</p>
 *
 * @version $Revision: 1.7 $
 */
public class HashFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce <i>Singleton</i> pattern. */
   private HashFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Return an instance of a hash algorithm given its name.</p>
    *
    * @param name the name of the hash algorithm.
    * @return an instance of the hash algorithm, or null if none found.
    * @exception InternalError if the implementation does not pass its self-
    * test.
    */
   public static IMessageDigest getInstance(String name) {
      if (name == null) {
         return null;
      }

      name = name.trim();
      IMessageDigest result = null;
      if (name.equalsIgnoreCase(WHIRLPOOL_HASH)) {
         result = new Whirlpool();
      } else if (name.equalsIgnoreCase(RIPEMD128_HASH)
            || name.equalsIgnoreCase(RIPEMD_128_HASH)) {
         result = new RipeMD128();
      } else if (name.equalsIgnoreCase(RIPEMD160_HASH)
            || name.equalsIgnoreCase(RIPEMD_160_HASH)) {
         result = new RipeMD160();
      } else if (name.equalsIgnoreCase(SHA160_HASH)
            || name.equalsIgnoreCase(SHA_1_HASH)
            || name.equalsIgnoreCase(SHA1_HASH)
            || name.equalsIgnoreCase(SHA_HASH)) {
         result = new Sha160();
      } else if (name.equalsIgnoreCase(MD5_HASH)) {
         result = new MD5();
      } else if (name.equalsIgnoreCase(MD4_HASH)) {
         result = new MD4();
      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * <p>Returns a {@link Set} of names of hash algorithms supported by this
    * <i>Factory</i>.</p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(WHIRLPOOL_HASH);
      hs.add(RIPEMD128_HASH);
      hs.add(RIPEMD160_HASH);
      hs.add(SHA160_HASH);
      hs.add(MD5_HASH);
      hs.add(MD4_HASH);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
