package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSSignature.java,v 1.1 2001-12-30 15:57:53 raif Exp $
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

import gnu.crypto.hash.Sha160;
import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.MDGenerator;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.HashMap;

/**
 * The DSS (Digital Signature Standard) algorithm makes use of the following
 * parameters:
 *
 * <ol>
 *    <li>p: A prime modulus, where <code>2<sup>L-1</sup> &lt; p &lt; 2<sup>L</sup>
 *    </code> for <code>512 &lt;= L &lt;= 1024</code> and <code>L</code> a
 *    multiple of <code>64</code>.</li>
 *    <li>q: A prime divisor of <code>p - 1</code>, where <code>2<sup>159</sup>
 *    &lt; q &lt; 2<sup>160</sup></code>.</li>
 *    <li>g: Where <code>g = h<sup>(p-1)</sup>/q mod p</code>, where
 *    <code>h</code> is any integer with <code>1 &lt; h &lt; p - 1</code> such
 *    that <code>h<sup> (p-1)</sup>/q mod p > 1</code> (<code>g</code> has order
 *    <code>q mod p</code>).</li>
 *    <li>x: A randomly or pseudorandomly generated integer with <code>0 &lt; x
 *    &lt; q</code>.</li>
 *    <li>y: <code>y = g<sup>x</sup> mod p</code>.</li>
 *    <li>k: A randomly or pseudorandomly generated integer with <code>0 &lt; k
 *    &lt; q</code>.</li>
 * </ol>
 *
 * The integers <code>p</code>, <code>q</code>, and <code>g</code> can be
 * public and can be common to a group of users. A user's private and public
 * keys are <code>x</code> and <code>y</code>, respectively. They are normally 
 * fixed for a period of time. Parameters <code>x</code> and <code>k</code> are 
 * used for signature generation only, and must be kept secret. Parameter 
 * <code>k</code> must be regenerated for each signature.<p>
 *
 * The signature of a message <code>M</code> is the pair of numbers <code>r</code>
 * and <code>s</code> computed according to the equations below:
 *
 * <ul>
 *    <li><code>r = (g<sup>k</sup> mod p) mod q</code> and</li>
 *    <li><code>s = (k<sup>-1</sup>(SHA(M) + xr)) mod q</code>.</li>
 * </ul>
 *
 * In the above, <code>k<sup>-1</sup></code> is the multiplicative inverse of
 * <code>k</code>, <code>mod q</code>; i.e., <code>(k<sup>-1</sup> k) mod q = 1
 * </code> and <code>0 &lt; k-1 &lt; q</code>. The value of <code>SHA(M)</code>
 * is a 160-bit string output by the Secure Hash Algorithm specified in FIPS 180.
 * For use in computing <code>s</code>, this string must be converted to an
 * integer.<p>
 *
 * As an option, one may wish to check if <code>r == 0</code> or <code>s == 0
 * </code>. If either <code>r == 0</code> or <code>s == 0</code>, a new value 
 * of <code>k</code> should be generated and the signature should be 
 * recalculated (it is extremely unlikely that <code>r == 0</code> or <code>s ==
 * 0</code> if signatures are generated properly).<p>
 *
 * The signature is transmitted along with the message to the verifier.<p>
 *
 * References:<br>
 * <a href="http://www.itl.nist.gov/fipspubs/fip186.htm">Digital Signature
 * Standard (DSS)</a>, Federal Information Processing Standards Publication 186.
 * National Institute of Standards and Technology.
 *
 * @version $Revision: 1.1 $
 */
public class DSSSignature implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The PRNG used to generate values for the DSS parameter <code>k</code>. */
   private static final IRandom prng;
   static {
      prng = new MDGenerator();
      try {
         prng.init(new HashMap()); // default is to use SHA
      } catch (Exception x) {
         throw new ExceptionInInitializerError(x);
      }
   }

   /** The public key to use when verifying signatures. */
   private DSAPublicKey publicKey;
   
   /** The private key to use when generating signatures (signing). */
   private DSAPrivateKey privateKey;
   
   /** The SHS instance to use for digesting messages when signing/verifying. */
   private Sha160 sha;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public DSSSignature() {
      super();
   }

   /** Private constructor for cloning purposes. */
   private DSSSignature(DSSSignature that) {
      this();

      this.publicKey = that.publicKey;
      this.privateKey = that.privateKey;
      this.sha = that.sha == null ? null : (Sha160) that.sha.clone();
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Cloneable interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      return new DSSSignature(this);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /**
    * Initialises this instance for signature verification.<p>
    *
    * @param key the public key to use for verification.
    */
   public void setupVerify(PublicKey key) throws IllegalArgumentException {
      if (!(key instanceof DSAPublicKey)) {
         throw new IllegalArgumentException();
      }
      init();
      publicKey = (DSAPublicKey) key;
   }

   /**
    * Initialises this instance for signature generation.<p>
    *
    * @param key the private key to use for signing.
    */
   public void setupSign(PrivateKey key) throws IllegalArgumentException {
      if (!(key instanceof DSAPrivateKey)) {
         throw new IllegalArgumentException();
      }
      init();
      privateKey = (DSAPrivateKey) key;
   }

   /**
    * Digests one byte of a message for signing or verification.<p>
    *
    * @param b the message byte to digest.
    */
   public void update(byte b) {
      if (sha == null) {
         throw new IllegalStateException();
      }
      sha.update(b);
   }

   /**
    * Digests a sequence of bytes from a message for signing or verification.<p>
    *
    * @param b the byte sequence to consider.
    * @param off the byte poisition in <code>b</code> of the first byte to 
    * consider.
    * @param len the number of bytes in <code>b</code> starting from position
    * <code>off</code> to digest.
    * @exception IllegalStateException if this instance was not setup.
    */
   public void update(byte[] b, int off, int len) {
      if (sha == null) {
         throw new IllegalStateException();
      }
      sha.update(b, off, len);
   }

   /**
    * Terminates a signature generation phase by digesting and processing the
    * context of the underlying SHS (Secure HashStandard, or SHA) instance.<p>
    *
    * @return the pair of big integers representing the output of the DSS.
    * @exception IllegalStateException if this instance was not setup for
    * signature generation.
    */
   public Object sign() {
      if (sha == null || privateKey == null) {
         throw new IllegalStateException();
      }

      BigInteger p = privateKey.getParams().getP();
      BigInteger q = privateKey.getParams().getQ();
      BigInteger g = privateKey.getParams().getG();
      BigInteger x = privateKey.getX();
      BigInteger m = new BigInteger(1, sha.digest());
      BigInteger k, r, s;

      byte[] kb = new byte[20]; // we'll use 159 bits only
      while (true) {
         try {
            prng.nextBytes(kb, 0, 20);
         } catch (LimitReachedException ignored) {
         }
         k = new BigInteger(1, kb);
         k.clearBit(159);
         r = g.modPow(k, p).mod(q);
         if (r.equals(BigInteger.ZERO)) {
            continue;
         }
         s = m.add(x.multiply(r)).multiply(k.modInverse(q)).mod(q);
         if (s.equals(BigInteger.ZERO)) {
            continue;
         }
         break;
      }

      return encodeSignature(r, s);
   }

   /**
    * Terminates a signature verification phase by digesting and processing the
    * context of the underlying SHS (Secure HashStandard, or SHA) instance.<p>
    *
    * @param sig a signature object previously generated by an invocation of
    * the <code>sign()</code> method.
    * @return <code>true</code> iff the outpout of the verification phase of
    * this instance matches the designated one.
    * @exception IllegalStateException if this instance was not setup for
    * signature verification.
    */
   public boolean verify(Object sig) {
      if (sha == null || publicKey == null) {
         throw new IllegalStateException();
      }
      
      BigInteger[] dsa = decodeSignature(sig);
      BigInteger r = dsa[0];
      BigInteger s = dsa[1];

      BigInteger g = publicKey.getParams().getG();
      BigInteger p = publicKey.getParams().getP();
      BigInteger q = publicKey.getParams().getQ();
      BigInteger y = publicKey.getY();
      BigInteger w = s.modInverse(q);

      byte bytes[] = sha.digest();
      BigInteger u1 = w.multiply(new BigInteger(1, bytes)).mod(q);
      BigInteger u2 = r.multiply(w).mod(q);

      BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
      return v.equals(r);
   }

   // helper methods
   // -------------------------------------------------------------------------

   /** Initialises the internal fields of this instance. */
   private void init() {
      if (sha != null) {
         sha.reset();
      } else {
         sha = new Sha160();
      }
      publicKey = null;
      privateKey = null;
   }

   /**
    * Returns the output of a signature generation phase.<p>
    *
    * @return an object encapsulating the DSS signature pair <code>r</code> and
    * <code>s</code>.
    */
   private Object encodeSignature(BigInteger r, BigInteger s) {
      return new BigInteger[] {r, s};
   }


   /**
    * Returns the output of a previously generated signature object as a pair
    * of {@link java.math.BigInteger}.<p>
    *
    * @return the DSS signature pair <code>r</code> and <code>s</code>.
    */
   private BigInteger[] decodeSignature(Object signature) {
      return (BigInteger[]) signature;
   }
}
