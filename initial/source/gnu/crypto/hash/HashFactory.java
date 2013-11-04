package gnu.crypto.hash;

// ----------------------------------------------------------------------------
// $Id: HashFactory.java,v 1.1.1.1 2001-11-20 13:40:34 raif Exp $
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate hash algorithm instances.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class HashFactory {

   // Constants and variables
   // -------------------------------------------------------------------------

   public static final String WHIRLPOOL_HASH = "whirlpool";
   public static final String RIPEMD128_HASH = "ripemd-128";
   public static final String RIPEMD160_HASH = "ripemd-160";

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private HashFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Return an instance of a hash algorithm given its name.
    *
    * @param algorithm the name of the hash algorithm.
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
      } else if (name.equalsIgnoreCase(RIPEMD128_HASH)) {
         result = new RipeMD128();
      } else if (name.equalsIgnoreCase(RIPEMD160_HASH)) {
         result = new RipeMD160();
      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * Returns a {@link java.util.Set} of names of hash algorithms supported by
    * this <i>Factory</i>.
    *
    * @return a {@link java.util.Set} of hash names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(WHIRLPOOL_HASH);
      hs.add(RIPEMD128_HASH);
      hs.add(RIPEMD160_HASH);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
