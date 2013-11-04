package gnu.crypto.mac;

// ----------------------------------------------------------------------------
// $Id: HMacFactory.java,v 1.2 2002-06-08 05:05:44 raif Exp $
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
import gnu.crypto.hash.HashFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>A <i>Factory</i> to instantiate Keyed-Hash Message Authentication Code
 * (HMAC) algorithm instances.</p>
 *
 * @version $Revision: 1.2 $
 */
public class HMacFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce <i>Singleton</i> pattern. */
   private HMacFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Return an instance of a <i>HMAC</i> algorithm given the name of its
    * underlying hash function, prefixed with the literal defined in
    * {@link Registry#HMAC_NAME_PREFIX}.</p>
    *
    * @param name the fully qualified name of the underlying algorithm: composed
    * as the concatenation of a literal prefix (see {@link Registry#HMAC_NAME_PREFIX})
    * and the name of the underlying hash algorithm.
    * @return an instance of the <i>HMAC</i> algorithm, or <code>null</code> if
    * none can be constructed.
    * @exception InternalError if the implementation does not pass its self-test.
    */
   public static IMac getInstance(String name) {
      if (name == null) {
         return null;
      }

      name = name.trim();
      if (!name.startsWith(HMAC_NAME_PREFIX)) {
         return null;
      }

      // strip the prefix
      name = name.substring(HMAC_NAME_PREFIX.length()).trim();
      IMac result = new HMac(HashFactory.getInstance(name));
      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * <p>Returns a {@link java.util.Set} of names of <i>HMAC</i> algorithms
    * supported by this <i>Factory</i>.</p>
    *
    * @return a {@link java.util.Set} of HMAC algorithm names (Strings).
    */
   public static final Set getNames() {
      Set hashNames = HashFactory.getNames();
      HashSet hs = new HashSet();
      for (Iterator it = hashNames.iterator(); it.hasNext(); ) {
         hs.add(HMAC_NAME_PREFIX+((String) it.next()));
      }

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
