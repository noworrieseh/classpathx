package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSKey.java,v 1.1 2001-12-30 15:57:53 raif Exp $
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
import java.security.interfaces.DSAKey;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;

/**
 * A base asbtract class for both public and private DSS (Digital Signature 
 * Standard) keys. It encapsulates the three DSS numbers: <code>p</code>,
 * <code>q</code> and <code>g</code>.<p>
 *
 * According to the JDK, cryptographic <i>Keys</i> all have a <i>format</i>.
 * The format used in this implementation is called <i>Raw</i>, and basically
 * consists of the raw byte sequences of algorithm parameters. The exact order
 * of the byte sequences and the implementation details are given in each of
 * the relevant <code>getEncoded()</code> methods of each of the private and 
 * public keys.<p> 
 *
 * @version $Revision: 1.1 $
 * @see gnu.crypto.sig.dss.DSSPrivateKey#getEncoded
 * @see gnu.crypto.sig.dss.DSSPublicKey#getEncoded
 */
public abstract class DSSKey implements Key, DSAKey {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * A prime modulus, where <code>2<sup>L-1</sup> &lt; p &lt; 2<sup>L</sup></code>
    * for <code>512 &lt;= L &lt;= 1024</code> and <code>L</code> a multiple of
    * <code>64</code>.
    */
   protected final BigInteger p;

   /**
    * A prime divisor of <code>p - 1</code>, where <code>2<sup>159</sup> &lt; q
    * &lt; 2<sup>160</sup></code>.
    */
   protected final BigInteger q;

   /**
    * <code>g = h<sup>(p-1)</sup>/q mod p</code>, where <code>h</code> is any
    * integer with <code>1 &lt; h &lt; p - 1</code> such that <code>h<sup>
    * (p-1)</sup>/q mod p > 1</code> (<code>g</code> has order <code>q mod p
    * </code>).
    */
   protected final BigInteger g;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.
    *
    * @param p the DSS parameter <code>p</code>.
    * @param q the DSS parameter <code>q</code>.
    * @param g the DSS parameter <code>g</code>.
    */
   protected DSSKey(BigInteger p, BigInteger q, BigInteger g) {
      super();

      this.p = p;
      this.q = q;
      this.g = g;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.interfaces.DSAKey interface implementation
   // -------------------------------------------------------------------------

   public DSAParams getParams() {
      return new DSAParameterSpec(p, q, g);
   }

   // java.security.Key interface implementation
   // -------------------------------------------------------------------------

   public String getAlgorithm() {
      return "DSA";
   }

   public String getFormat() {
      return null;
   }

   // Other instance methods
   // -------------------------------------------------------------------------

   /**
    * Returns <code>true</code> if the designated object is an instance of this
    * class and has the same DSS (Digital Signature Standard) parameter values
    * as this one.<p>
    *
    * @param obj the other non-null DSS key to compare to.
    * @return <code>true</code> if the designated object is of the same type and
    * value as this one.
    */
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof DSSKey)) {
         return false;
      }
      DSSKey that = (DSSKey) obj;
      return this.p.equals(that.p) && this.q.equals(that.q) && this.g.equals(that.g);
   }
}
