package gnu.crypto.mac;

// ----------------------------------------------------------------------------
// $Id: MacFactory.java,v 1.1 2002-06-08 05:06:33 raif Exp $
//
// Copyright (C) 2002, Free Software Foundation, Inc.
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

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

/**
 * <p>A <i>Factory</i> that instantiates instances of every supported Message
 * Authentication Code algorithms, including all <i>HMAC</i> algorithms.</p>
 *
 * @version $Revision: 1.1 $
 */
public class MacFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce <i>Singleton</i> pattern. */
   private MacFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns an instance of a <i>MAC</i> algorithm given its name.</p>
    *
    * @param name the name of the MAC algorithm.
    * @return an instance of the <i>MAC</i> algorithm, or <code>null</code> if
    * none can be constructed.
    * @exception InternalError if the implementation does not pass its self-test.
    */
   public static IMac getInstance(String name) {
      if (name == null) {
         return null;
      }

      name = name.trim();
      if (name.startsWith(HMAC_NAME_PREFIX)) {
         return HMacFactory.getInstance(name);
      }

      IMac result = null;
      if (name.equalsIgnoreCase(UHASH32)) {
         result = new UHash32();
      } else if (name.equalsIgnoreCase(UMAC32)) {
         result = new UMac32();
      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
      }

      return result;
   }

   /**
    * <p>Returns a {@link java.util.Set} of names of <i>MAC</i> algorithms
    * supported by this <i>Factory</i>.</p>
    *
    * @return a {@link java.util.Set} of MAC names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.addAll(HMacFactory.getNames());
      hs.add(UHASH32);
      hs.add(UMAC32);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
