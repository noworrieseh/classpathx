package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: PRNGFactory.java,v 1.2 2001-12-04 12:56:08 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate pseudo random number generators.
 *
 * @version $Revision: 1.2 $
 */
public class PRNGFactory {

   // Constants and variables
   // -------------------------------------------------------------------------

   public static final String ICM_PRNG = "icm";

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private PRNGFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns an instance of a padding algorithm given its name.
    *
    * @param prng the case-insensitive name of the PRNG.
    * @return an instance of the pseudo-random number generator.
    * @exception InternalError if the implementation does not pass its self-
    * test.
    */
   public static final IRandom getInstance(String prng) {
      if (prng == null) {
         return null;
      }

      prng = prng.trim();
      IRandom result = null;
      if (prng.equalsIgnoreCase(ICM_PRNG)) {
         result = new ICMGenerator();
      }

      return result;
   }

   /**
    * Returns a {@link java.util.Set} of names of padding algorithms supported
    * by this <i>Factory</i>.
    *
    * @return a {@link java.util.Set} of padding algorithm names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(ICM_PRNG);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
