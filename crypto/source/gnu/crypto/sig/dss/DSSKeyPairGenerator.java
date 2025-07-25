package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSKeyPairGenerator.java,v 1.4 2002-01-17 11:53:06 raif Exp $
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
import gnu.crypto.hash.Sha160;
import gnu.crypto.sig.IKeyPairGenerator;
import gnu.crypto.util.Prime;
import gnu.crypto.util.PRNG;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.DSAParameterSpec;
import java.util.HashMap;
import java.util.Map;

/**
 * A key-pair generator for asymetric keys to use in conjunction with the DSS
 * (Digital Signature Standard).<p>
 *
 * References:<br>
 * <a href="http://www.itl.nist.gov/fipspubs/fip186.htm">Digital Signature
 * Standard (DSS)</a>, Federal Information Processing Standards Publication 186.
 * National Institute of Standards and Technology.
 *
 * @version $Revision: 1.4 $
 */
public class DSSKeyPairGenerator implements IKeyPairGenerator {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "dss";
   private static final boolean DEBUG = true;
   private static final int debuglevel = 5;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The BigInteger constant 2. */
   private static final BigInteger TWO = new BigInteger("2");

   /** Property name of the length (Integer) of the modulus (p) of a DSS key. */
   public static final String MODULUS_LENGTH = "gnu.crypto.dss.L";

   /** Property name of the Boolean indicating wether or not to use defaults. */
   public static final String USE_DEFAULTS = "gnu.crypto.dss.use.defaults";

   /**
    * Property name of an optional {@link java.security.SecureRandom} instance
    * to use. The default is to use a classloader singleton from
    * {@link gnu.crypto.util.PRNG}.
    */
   public static final String SOURCE_OF_RANDOMNESS = "gnu.crypto.dss.prng";

   /**
    * Property name of an optional {@link java.security.spec.DSAParameterSpec}
    * instance to use for this generator's <code>p</code>, <code>q</code>, and
    * <code>g</code> values. The default is to generate these values or use
    * pre-computed ones, depending on the value of the <code>USE_DEFAULTS</code>
    * attribute.
    */
   public static final String DSS_PARAMETERS = "gnu.crypto.dss.params";

   /** Default value for the modulus length. */
   private static final int DEFAULT_MODULUS_LENGTH = 1024;

   /** Initial SHS context. */
   private static final int[] T_SHS = new int[]
         {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};

   // from jdk1.3.1/docs/guide/security/CryptoSpec.html#AppB
   public static final DSAParameterSpec
   KEY_PARAMS_512 = new DSAParameterSpec(
      new BigInteger(
         "fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae"+
         "01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17", 16),
      new BigInteger("962eddcc369cba8ebb260ee6b6a126d9346e38c5", 16),
      new BigInteger(
         "678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e"+
         "35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4", 16)
      );

   public static final DSAParameterSpec
   KEY_PARAMS_768 = new DSAParameterSpec(
      new BigInteger(
         "e9e642599d355f37c97ffd3567120b8e25c9cd43e927b3a9670fbec5d8901419"+
         "22d2c3b3ad2480093799869d1e846aab49fab0ad26d2ce6a22219d470bce7d77"+
         "7d4a21fbe9c270b57f607002f3cef8393694cf45ee3688c11a8c56ab127a3daf", 16),
      new BigInteger("9cdbd84c9f1ac2f38d0f80f42ab952e7338bf511", 16),
      new BigInteger(
         "30470ad5a005fb14ce2d9dcd87e38bc7d1b1c5facbaecbe95f190aa7a31d23c4"+
         "dbbcbe06174544401a5b2c020965d8c2bd2171d3668445771f74ba084d2029d8"+
         "3c1c158547f3a9f1a2715be23d51ae4d3e5a1f6a7064f316933a346d3f529252", 16)
   );

   public static final DSAParameterSpec
   KEY_PARAMS_1024 = new DSAParameterSpec(
      new BigInteger(
         "fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669"+
         "455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b7"+
         "6b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb"+
         "83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c7", 16),
      new BigInteger("9760508f15230bccb292b982a2eb840bf0581cf5", 16),
      new BigInteger(
         "f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d078267"+
         "5159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e1"+
         "3c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243b"+
         "cca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a", 16)
   );

   private static final BigInteger TWO_POW_160 = TWO.pow(160);

   /** The length of the modulus of DSS keys generated by this instance. */
   private int L;

   /** The SHA instance to use. */
   private Sha160 sha = new Sha160();

   /** The optional {@link java.security.SecureRandom} instance to use. */
   private SecureRandom rnd = null;

   private BigInteger p;
   private BigInteger q;
   private BigInteger g;
   private BigInteger XKEY;

   // Constructor(s)
   // -------------------------------------------------------------------------

   // implicit 0-arguments constructor

   // Class methods
   // -------------------------------------------------------------------------

   // gnu.crypto.sig.IKeyPairGenerator interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      return Registry.DSS_KPG;
   }

   /**
    * Configures this instance.<p>
    *
    * @param attributes the map of name/value pairs to use.
    * @exception IllegalArgumentException if the designated MODULUS_LENGTH
    * value is not greater than 512, less than 1024 and not of the form
    * <code>512 + 64j</code>.
    */
   public void setup(Map attributes) {
      // find out the modulus length
      Integer l = (Integer) attributes.get(MODULUS_LENGTH);
      L = (l == null ? DEFAULT_MODULUS_LENGTH : l.intValue());
      if ((L % 64) != 0 || L < 512 || L > 1024)
         throw new IllegalArgumentException(MODULUS_LENGTH);

      // should we use the default pre-computed params?
      Boolean useDefaults = (Boolean) attributes.get(USE_DEFAULTS);
      if (useDefaults == null) {
         useDefaults = Boolean.TRUE;
      }

      // are we given a set of DSA params or we shall use/generate our own?
      DSAParameterSpec params = (DSAParameterSpec) attributes.get(DSS_PARAMETERS);
      if (params != null) {
         p = params.getP();
         q = params.getQ();
         g = params.getG();
      } else if (useDefaults.equals(Boolean.TRUE)) {
         switch (L) {
         case 512:
            p = KEY_PARAMS_512.getP();
            q = KEY_PARAMS_512.getQ();
            g = KEY_PARAMS_512.getG();
            break;
         case 768:
            p = KEY_PARAMS_768.getP();
            q = KEY_PARAMS_768.getQ();
            g = KEY_PARAMS_768.getG();
            break;
         case 1024:
            p = KEY_PARAMS_1024.getP();
            q = KEY_PARAMS_1024.getQ();
            g = KEY_PARAMS_1024.getG();
            break;
         default:
            p = null;
            q = null;
            g = null;
         }
      } else {
         p = null;
         q = null;
         g = null;
      }

      // do we have a SecureRandom, or should we use our own?
      rnd = (SecureRandom) attributes.get(SOURCE_OF_RANDOMNESS);

      // set the seed-key
      byte[] kb = new byte[20]; // we need 160 bits of randomness
      nextRandomBytes(kb);
      XKEY = new BigInteger(1, kb).setBit(159).setBit(0);
   }

   public KeyPair generate() {
      if (p == null) {
         generateParameters();
      }

      BigInteger x = nextX();
      BigInteger y = g.modPow(x, p);

      PublicKey pubK = new DSSPublicKey(p, q, g, y);
      PrivateKey secK = new DSSPrivateKey(p, q, g, x);

      return new KeyPair(pubK, secK);
   }

   // Other instance methods
   // -------------------------------------------------------------------------

   /**
    * This method generates the DSS <code>p</code>, <code>q</code>, and
    * <code>g</code> parameters only when <code>L</code> (the modulus length)
    * is not one of the following: <code>512</code>, <code>768</code> and
    * <code>1024</code>. For those values of <code>L</code>, this implementation
    * uses pre-computed values of <code>p</code>, <code>q</code>, and
    * <code>g</code> given in the document <i>CryptoSpec</i> included in the
    * security guide documentation of the standard JDK distribution.<p>
    *
    * The DSS requires two primes , <code>p</code> and <code>q</code>,
    * satisfying the following three conditions:
    *
    * <ul>
    *    <li><code>2<sup>159</sup> &lt; q &lt; 2<sup>160</sup></code></li>
    *    <li><code>2<sup>L-1</sup> &lt; p &lt; 2<sup>L</sup></code> for a
    *    specified <code>L</code>, where <code>L = 512 + 64j</code> for some
    *    <code>0 &lt;= j &lt;= 8</code></li>
    *    <li>q divides p - 1.</li>
    * </ul>
    *
    * The algorithm used to find these primes is as described in FIPS-186,
    * section 2.2: GENERATION OF PRIMES. This prime generation scheme starts by
    * using the {@link gnu.crypto.hash.Sha160} and a user supplied <i>SEED</i>
    * to construct a prime, <code>q</code>, in the range 2<sup>159</sup> &lt; q
    * &lt; 2<sup>160</sup>. Once this is accomplished, the same <i>SEED</i>
    * value is used to construct an <code>X</code> in the range <code>2<sup>L-1
    * </sup> &lt; X &lt; 2<sup>L</sup>. The prime, <code>p</code>, is then
    * formed by rounding <code>X</code> to a number congruent to <code>1 mod
    * 2q</code>. In this implementation we use the same <i>SEED</i> value given
    * in FIPS-186, Appendix 5.
    */
   private void generateParameters() {
      int counter, offset;
      BigInteger SEED, alpha, U, OFFSET, SEED_PLUS_OFFSET, W, X, c;
      byte[] a, u;
      byte[] kb = new byte[20]; // to hold 160 bits of randomness

      // Let L-1 = n*160 + b, where b and n are integers and 0 <= b < 160.
      int b = (L-1) % 160;
      int n = (L-1-b) / 160;
      BigInteger[] V = new BigInteger[n+1];
      algorithm: while (true) {
         step1: while (true) {
            // 1. Choose an arbitrary sequence of at least 160 bits and
            // call it SEED.
            nextRandomBytes(kb);
            SEED = new BigInteger(1, kb).setBit(159).setBit(0);
            // Let g be the length of SEED in bits. here always 160
            // 2. Compute: U = SHA[SEED] XOR SHA[(SEED+1) mod 2**g]
            alpha = SEED.add(BigInteger.ONE).mod(TWO_POW_160);
            synchronized (sha) {
               a = SEED.toByteArray();
               sha.update(a, 0, a.length);
               a = sha.digest();
               u = alpha.toByteArray();
               sha.update(u, 0, u.length);
               u = sha.digest();
            }
            for (int i = 0; i < a.length; i++) {
               a[i] ^= u[i];
            }
            U = new BigInteger(1, a);
            // 3. Form q from U by setting the most significant bit (the
            // 2**159 bit) and the least significant bit to 1. In terms of
            // boolean operations, q = U OR 2**159 OR 1. Note that
            // 2**159 < q < 2**160.
            q = U.setBit(159).setBit(0);
            // 4. Use a robust primality testing algorithm to test whether
            // q is prime(1). A robust primality test is one where the
            // probability of a non-prime number passing the test is at
            // most 1/2**80.
            // 5. If q is not prime, go to step 1.
            if (Prime.isProbablePrime(q)) {
               break step1;
            }
         } // step1

         // 6. Let counter = 0 and offset = 2.
         counter = 0;
         offset = 2;
         step7: while (true) {
            OFFSET = BigInteger.valueOf(offset & 0xFFFFFFFFL);
            SEED_PLUS_OFFSET = SEED.add(OFFSET);
            // 7. For k = 0,...,n let V[k] = SHA[(SEED + offset + k) mod 2**g].
            synchronized (sha) {
               for (int k = 0; k <= n; k++) {
                  a = SEED_PLUS_OFFSET
                        .add(BigInteger.valueOf(k & 0xFFFFFFFFL))
                        .mod(TWO_POW_160)
                        .toByteArray();
                  sha.update(a, 0, a.length);
                  V[k] = new BigInteger(1, sha.digest());
               }
            }
            // 8. Let W be the integer:
            // V[0]+V[1]*2**160+...+V[n-1]*2**((n-1)*160)+(V[n]mod2**b)*2**(n*160)
            // and let : X = W + 2**(L-1).
            // Note that 0 <= W < 2**(L-1) and hence 2**(L-1) <= X < 2**L.
            W = V[0];
            for (int k = 1; k < n; k++) {
               W = W.add(V[k].multiply(TWO.pow(k*160)));
            }
            W = W.add(V[n].mod(TWO.pow(b)).multiply(TWO.pow(n*160)));
            X = W.add(TWO.pow(L-1));
            // 9. Let c = X mod 2q and set p = X - (c - 1).
            // Note that p is congruent to 1 mod 2q.
            c = X.mod(TWO.multiply(q));
            p = X.subtract(c.subtract(BigInteger.ONE));
            // 10. If p < 2**(L-1), then go to step 13.
            if (p.compareTo(TWO.pow(L-1)) >= 0) {
               // 11. Perform a robust primality test on p.
               // 12. If p passes the test performed in step 11, go to step 15.
               if (Prime.isProbablePrime(p)) {
                  break algorithm;
               }
            }
            // 13. Let counter = counter + 1 and offset = offset + n + 1.
            counter++;
            offset += n + 1;
            // 14. If counter >= 4096 go to step 1, otherwise go to step 7.
            if (counter >= 4096) {
               continue algorithm;
            }
         } // step7
      } // algorithm

      // 15. Save the value of SEED and the value of counter for use
      // in certifying the proper generation of p and q.
      if (DEBUG && debuglevel > 0) {
         debug("SEED: "+SEED.toString(16));
         debug("counter: "+String.valueOf(counter));
      }

      // compute g. from FIPS-186, Appendix 4:
      // 1. Generate p and q as specified in Appendix 2.
      // 2. Let e = (p - 1)/q
      BigInteger e = p.subtract(BigInteger.ONE).divide(q);
      BigInteger h = TWO;
      BigInteger p_minus_1 = p.subtract(BigInteger.ONE);
      // 3. Set h = any integer, where 1 < h < p - 1 and
      // h differs from any value previously tried
      for ( ; h.compareTo(p_minus_1) < 0; h = h.add(BigInteger.ONE)) {
         // 4. Set g = h**e mod p
         g = h.modPow(e, p);
         // 5. If g = 1, go to step 3
         if (!g.equals(BigInteger.ONE)) {
            break;
         }
      }

      if (DEBUG && debuglevel > 0) {
         debug("q: "+q.toString(16));
         debug("p: "+p.toString(16));
         debug("g: "+g.toString(16));
      }
   }

   /**
    * This method applies the following algorithm described in 3.1 of FIPS-186:
    *
    * <ol>
    *    <li>XSEED = optional user input.</li>
    *    <li>XVAL = (XKEY + XSEED) mod 2<sup>b</sup>.</li>
    *    <li>x = G(t, XVAL) mod q.</li>
    *    <li>XKEY = (1 + XKEY + x) mod 2<sup>b</sup>.</li>
    * </ol>
    *
    * Where <code>b</code> is the length of a secret b-bit seed-key (XKEY).<p>
    *
    * Note that in this implementation, XSEED, the optional user input, is
    * always zero.
    */
   private synchronized BigInteger nextX() {
      byte[] xk = XKEY.toByteArray();
      byte[] in = new byte[64]; // 512-bit block for SHS
      System.arraycopy(xk, 0, in, 0, xk.length);

      int[] H = Sha160.G(T_SHS[0], T_SHS[1], T_SHS[2], T_SHS[3], T_SHS[4], in, 0);
      byte[] h = new byte[20];
      for (int i = 0, j = 0; i < 5; i++) {
         h[j++] = (byte)(H[i] >>> 24);
         h[j++] = (byte)(H[i] >>> 16);
         h[j++] = (byte)(H[i] >>>  8);
         h[j++] = (byte) H[i];
      }
      BigInteger result = new BigInteger(1, h).mod(q);
      XKEY = XKEY.add(result).add(BigInteger.ONE).mod(TWO_POW_160);

      return result;
   }

   /**
    * Fills the designated byte array with random data.
    *
    * @param buffer the byte array to fill with random data.
    */
   private void nextRandomBytes(byte[] buffer) {
      if (rnd != null) {
         rnd.nextBytes(buffer);
      } else {
         PRNG.nextBytes(buffer);
      }
   }
}
