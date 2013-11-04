package gnu.crypto.pad;

// ----------------------------------------------------------------------------
// $Id: PadFactory.java,v 1.3 2002-01-11 21:51:20 raif Exp $
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate padding schemes.<p>
 *
 * @version $Revision: 1.3 $
 */
public class PadFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private PadFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns an instance of a padding algorithm given its name.
    *
    * @param pad the case-insensitive name of the padding algorithm.
    * @return an instance of the padding algorithm, operating with a given
    * block size, or <tt>null</tt> if none found.
    * @exception InternalError if the implementation does not pass its self-
    * test.
    */
   public static final IPad getInstance(String pad) {
      if (pad == null) {
         return null;
      }

      pad = pad.trim();
      IPad result = null;
      if (pad.equalsIgnoreCase(PKCS7_PAD)) {
         result = new PKCS7();
      } else if (pad.equalsIgnoreCase(TBC_PAD)) {
         result = new TBC();
      }

      if (result != null && !result.selfTest()) {
         throw new InternalError(result.name());
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
      hs.add(PKCS7_PAD);
      hs.add(TBC_PAD);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
