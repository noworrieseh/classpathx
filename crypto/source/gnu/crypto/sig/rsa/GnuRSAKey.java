package gnu.crypto.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: GnuRSAKey.java,v 1.1 2002-01-11 21:21:57 raif Exp $
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

import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAKey;

/**
 * A base asbtract class for both public and private RSA keys.<p>
 *
 * @version $Revision: 1.1 $
 */
public abstract class GnuRSAKey implements Key, RSAKey {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The modulus of an RSA key pair. */
   private final BigInteger n;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.
    *
    * @param n the public modulus <code>p</code>.
    */
   protected GnuRSAKey(BigInteger n) {
      super();

      this.n = n;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.interfaces.RSAKey interface implementation
   // -------------------------------------------------------------------------

   public BigInteger getModulus() {
      return getN();
   }

   // java.security.Key interface implementation
   // -------------------------------------------------------------------------

   public String getAlgorithm() {
      return "RSA";
   }

   public String getFormat() {
      return null;
   }

   // Other instance methods
   // -------------------------------------------------------------------------

   /**
    * Returns the modulus <code>n</code>.
    *
    * @returns the modulus <code>n</code>.
    */
   public BigInteger getN() {
      return n;
   }

   /**
    * Returns <code>true</code> if the designated object is an instance of
    * {@link java.security.interfaces.RSAKey} and has the same RSA parameter
    * values as this one.<p>
    *
    * @param obj the other non-null RSA key to compare to.
    * @return <code>true</code> if the designated object is of the same type and
    * value as this one.
    */
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof RSAKey)) {
         return false;
      }
      RSAKey that = (RSAKey) obj;
      return n.equals(that.getModulus());
   }
}
