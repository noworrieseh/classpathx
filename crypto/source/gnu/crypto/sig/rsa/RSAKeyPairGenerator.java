package gnu.crypto.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: RSAKeyPairGenerator.java,v 1.1 2002-01-11 21:21:57 raif Exp $
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
import gnu.crypto.sig.IKeyPairGenerator;
import gnu.crypto.util.Prime;
import gnu.crypto.util.PRNG;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * A key-pair generator for asymetric keys to use in conjunction with the RSA
 * scheme.<p>
 *
 * References:<br>
 * <a href="http://www.cosic.esat.kuleuven.ac.be/nessie/workshop/submissions/rsa-pss.zip">
 * RSA-PSS Signature Scheme with Appendix</a>, part B. Primitive specification
 * and supporting documentation. Jakob Jonsson and Burt Kaliski.<p>
 *
 * <a href="http://www.cacr.math.uwaterloo.ca/hac/">Handbook of Applied
 * Cryptography</a>, Alfred J. Menezes, Paul C. van Oorschot and Scott A.
 * Vanstone. Section 11.3 RSA and related signature schemes.<p>
 *
 * @version $Revision: 1.1 $
 */
public class RSAKeyPairGenerator implements IKeyPairGenerator {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "rsa";
   private static final boolean DEBUG = true;
   private static final int debuglevel = 5;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   /** This implementation uses Fermat's F3 number as the public exponent. */
   private static final BigInteger e = BigInteger.valueOf(65537L);

   /** The BigInteger constant 1. */
   private static final BigInteger ONE = BigInteger.ONE;

   /** The BigInteger constant 2. */
   private static final BigInteger TWO = new BigInteger("2");

   /** Property name of the length (Integer) of the modulus of an RSA key. */
   public static final String MODULUS_LENGTH = "gnu.crypto.rsa.L";

   /** Default value for the modulus length. */
   private static final int DEFAULT_MODULUS_LENGTH = 1024;

   /** The desired bit length of the modulus. */
   private int L;

   // Constructor(s)
   // -------------------------------------------------------------------------

   // implicit 0-arguments constructor

   // Class methods
   // -------------------------------------------------------------------------

   // gnu.crypto.sig.IKeyPairGenerator interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      return Registry.RSA_KPG;
   }

   /**
    * Configures this instance.<p>
    *
    * @param attributes the map of name/value pairs to use.
    * @exception IllegalArgumentException if the designated MODULUS_LENGTH
    * value is not greater than 1024.
    */
   public void setup(Map attributes) {
      // find out the modulus length
      Integer l = (Integer) attributes.get(MODULUS_LENGTH);
      L = (l == null ? DEFAULT_MODULUS_LENGTH : l.intValue());
      if (L < 1024)
         throw new IllegalArgumentException(MODULUS_LENGTH);
   }

   /**
    * The algorithm used here is described in <i>nessie-pss-B.pdf</i> document
    * which is part of the RSA-PSS submission to NESSIE.
    *
    * @return an RSA keypair.
    */
   public KeyPair generate() {
      BigInteger p, q, n, d;

      // 1. Generate a prime p in the interval [2**(M-1), 2**M - 1], where
      // M = CEILING(L/2), and such that GCD(p, e) = 1
      int M = (L+1)/2;
      BigInteger lower = TWO.pow(M-1);
      BigInteger upper = TWO.pow(M).subtract(ONE);
      byte[] kb = new byte[(M+7)/8]; // enough bytes to frame M bits
      step1: while (true) {
         PRNG.nextBytes(kb);
         p = new BigInteger(1, kb).setBit(0);
         if (     p.compareTo(lower) >= 0
               && p.compareTo(upper) <= 0
               && Prime.isProbablePrime(p)
               && p.gcd(e).equals(ONE)) {
            break step1;
         }
      }

      // 2. Generate a prime q such that the product of p and q is an L-bit
      // number, and such that GCD(q, e) = 1
      step2: while (true) {
         PRNG.nextBytes(kb);
         q = new BigInteger(1, kb).setBit(0);
         n = p.multiply(q);
         if (     n.bitLength() == L
               && Prime.isProbablePrime(q)
               && q.gcd(e).equals(ONE)) {
            break step2;
         }
      }

      // 3. Put n = pq. The public key is (n, e).
      // 4. Compute the parameters necessary for the private key K (see
      // Section 2.2).
      BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE));
      d = e.modInverse(phi);

      // 5. Output the public key and the private key.
      PublicKey pubK = new GnuRSAPublicKey(n, e);
      PrivateKey secK = new GnuRSAPrivateKey(p, q, e, d);

      return new KeyPair(pubK, secK);
   }
}
