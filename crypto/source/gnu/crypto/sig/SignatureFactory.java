package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: SignatureFactory.java,v 1.1 2002-01-11 21:38:55 raif Exp $
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
import gnu.crypto.sig.dss.DSSSignature;
import gnu.crypto.sig.rsa.RSAPSSSignature;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Factory to instantiate signature-with-appendix handlers.<p>
 *
 * @version $Revision: 1.1 $
 */
public class SignatureFactory implements Registry {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private SignatureFactory() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns an instance of a signature-with-appendix scheme given its name.<p>
    *
    * @param scheme the case-insensitive signature-with-appendix scheme name.
    * @return an instance of the signature-with-appendix scheme, or <tt>null</tt>
    * if none found.
    */
   public static ISignature getInstance(String scheme) {
      if (scheme == null) {
         return null;
      }

      scheme = scheme.trim();
      ISignature result = null;
      if (scheme.equalsIgnoreCase(DSA_SIG) || scheme.equals(DSS_SIG)) {
         result = new DSSSignature();
      } else if (scheme.equalsIgnoreCase(RSA_PSS_SIG)) {
         result = new RSAPSSSignature();
      }

      return result;
   }

   /**
    * Returns a {@link java.util.Set} of signature-with-appendix scheme names
    * supported by this <i>Factory</i>.<p>
    *
    * @return a {@link java.util.Set} of signature-with-appendix scheme names
    * (Strings).
    */
   public static final Set getNames() {
      HashSet hs = new HashSet();
      hs.add(DSS_SIG);
      hs.add(RSA_PSS_SIG);

      return Collections.unmodifiableSet(hs);
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
