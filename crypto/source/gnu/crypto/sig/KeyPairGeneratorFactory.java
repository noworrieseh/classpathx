package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: KeyPairGeneratorFactory.java,v 1.1 2002-01-11 21:38:55 raif Exp $
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
import gnu.crypto.sig.dss.DSSKeyPairGenerator;
import gnu.crypto.sig.rsa.RSAKeyPairGenerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate asymmetric keypair generators.<p>
 *
 * @version $Revision: 1.1 $
 */
public class KeyPairGeneratorFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private KeyPairGeneratorFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns an instance of a keypair generator given its name.<p>
    *
    * @param name the case-insensitive key generator name.
    * @return an instance of the keypair generator, or <tt>null</tt> if none
    * found.
    */
   public static IKeyPairGenerator getInstance(String name) {
      if (name == null) {
         return null;
      }

      name = name.trim();
      IKeyPairGenerator result = null;
      if (name.equalsIgnoreCase(DSA_KPG) || name.equals(DSS_KPG)) {
         result = new DSSKeyPairGenerator();
      } else if (name.equalsIgnoreCase(RSA_KPG)) {
         result = new RSAKeyPairGenerator();
      }

      return result;
   }

   /**
    * Returns a {@link java.util.Set} of keypair generator names supported by
    * this <i>Factory</i>. Those keypair generators may be used in conjunction
    * with the digital signature schemes with appendix supported by this
    * library.<p>
    *
    * @return a {@link java.util.Set} of keypair generator names (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(DSS_KPG);
      hs.add(RSA_KPG);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
