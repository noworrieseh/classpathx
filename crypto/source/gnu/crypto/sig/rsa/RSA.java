package gnu.crypto.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: RSA.java,v 1.2 2002-06-08 05:27:06 raif Exp $
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

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <p>Utility methods related to the RSA algorithm.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.cosic.esat.kuleuven.ac.be/nessie/workshop/submissions/rsa-pss.zip">
 *    RSA-PSS Signature Scheme with Appendix, part B.</a><br>
 *    Primitive specification and supporting documentation.<br>
 *    Jakob Jonsson and Burt Kaliski.</li>
 * </ol>
 *
 * @version $Revision: 1.2 $
 */
public class RSA {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "rsa-util";
   private static final boolean DEBUG = true;
   private static final int debuglevel = 9;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final BigInteger ZERO = BigInteger.ZERO;
   private static final BigInteger ONE = BigInteger.ONE;
   private static final BigInteger TWO = BigInteger.valueOf(2L);

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial private constructor to enforce Singleton pattern. */
   private RSA() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>An implementation of the <b>RSASP</b> method: Assuming that the
    * designated RSA private key is a valid one, this method computes a
    * <i>signature representative</i> for a designated <i>message
    * representative</i> signed by the holder of the designated RSA private
    * key.<p>
    *
    * @param K the RSA private key.
    * @param m the <i>message representative</i>: an integer between
    * <code>0</code> and <code>n - 1</code>, where <code>n</code> is the RSA
    * <i>modulus</i>.
    * @return the <i>signature representative</i>, an integer between
    * <code>0</code> and <code>n - 1</code>, where <code>n</code> is the RSA
    * <i>modulus</i>.
    * @exception IllegalArgumentException if the key is not an RSA one, or if
    * <code>m</code> (the <i>message representative</i>) is out of range.
    */
   public static BigInteger sign(PrivateKey K, BigInteger m) {
      if (!(K instanceof RSAPrivateKey)) {
         throw new IllegalArgumentException("invalid key");
      }

      // 1. If the message representative m is not between 0 and n-1,
      // output 'message representative out of range' and stop
      BigInteger n = ((RSAPrivateKey) K).getModulus();
      if (m.compareTo(ZERO) < 0 || m.compareTo(n.subtract(ONE)) > 0) {
         throw new IllegalArgumentException("message representative out of range");
      }

      BigInteger result = null;
      if (!(K instanceof RSAPrivateCrtKey)) {
         BigInteger d = ((RSAPrivateKey) K).getPrivateExponent();

         // 2. If the first form (n, d) of K is used:
         // 2.1 Let s = m**d mod n
         result = m.modPow(d, n);
      } else {
         BigInteger p =    ((RSAPrivateCrtKey) K).getPrimeP();
         BigInteger q =    ((RSAPrivateCrtKey) K).getPrimeQ();
         BigInteger dP =   ((RSAPrivateCrtKey) K).getPrimeExponentP();
         BigInteger dQ =   ((RSAPrivateCrtKey) K).getPrimeExponentQ();
         BigInteger qInv = ((RSAPrivateCrtKey) K).getCrtCoefficient();

         // Else if the second form (p, q, dP, dQ, qInv) of K is used:
         // 2.2 Let s1 = m**dP mod p
         BigInteger s1 = m.modPow(dP, p);
         // 2.3 Let s2 = m**dQ mod q
         BigInteger s2 = m.modPow(dQ, q);
         // 2.4 Let h = (s1 - s2) * qInv mod p
         BigInteger h = s1.subtract(s2).multiply(qInv).mod(p);
         // 2.5 Let s = s2 + q * h
         result = s2.add(q.multiply(h));
      }
      // 3. Output s
      return result;
   }

   /**
    * <p>An implementation of the <b>RSAVP</b> method: Assuming that the
    * designated RSA public key is a valid one, this method computes a
    * <i>message representative</i> for the designated <i>signature
    * representative</i> generated by an RSA private key, for a message
    * intended for the holder of the designated RSA public key.</p>
    *
    * @param K the RSA public key.
    * @param s the <i>signature representative</i>, an integer between
    * <code>0</code> and <code>n - 1</code>, where <code>n</code> is the RSA
    * <i>modulus</i>.
    * @return a <i>message representative</i>: an integer between <code>0</code>
    * and <code>n - 1</code>, where <code>n</code> is the RSA <i>modulus</i>.
    * @exception IllegalArgumentException if the key is not an RSA one or if
    * <code>s</code> (the <i>signature representative</i>) is out of range.
    */
   public static BigInteger verify(PublicKey K, BigInteger s) {
      if (!(K instanceof RSAPublicKey)) {
         throw new IllegalArgumentException("wrong key");
      }

      BigInteger n = ((RSAPublicKey) K).getModulus();
      BigInteger e = ((RSAPublicKey) K).getPublicExponent();

      // 1. If the signature representative s is not between 0 and n-1,
      // output 'signature representative out of range' and stop.
      if (s.compareTo(ZERO) < 0 || s.compareTo(n.subtract(ONE)) > 0) {
         throw new IllegalArgumentException("signature representative out of range");
      }
      // 2. Let m = s**e mod n.
      BigInteger result = s.modPow(e, n);
      // 3. Output m.
      return result;
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
