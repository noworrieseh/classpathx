package gnu.crypto.util;

// ----------------------------------------------------------------------------
// $Id: Prime.java,v 1.3 2002-08-12 13:14:49 raif Exp $
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

import java.io.PrintWriter;
import java.math.BigInteger;

/**
 * A collection of prime number related utility methods used in this library.
 *
 * @version $Revision: 1.3 $
 */
public class Prime {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "prime";
   private static final boolean DEBUG = true;
   private static final int debuglevel = 1;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   /**
    * By default do (when <code>true</code>) or skip (when <code>false</code>)
    * The Miller-Rabin probabilistic primality test.<p>
    *
    * @see #isProbablePrime
    */
   private static final boolean DO_MILLER_RABIN = false;

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final BigInteger ZERO = BigInteger.ZERO;
   private static final BigInteger ONE = BigInteger.ONE;
   private static final BigInteger TWO = BigInteger.valueOf(2L);

   /**
    * The first SMALL_PRIME primes: Algorithm P, section 1.3.2, The Art of
    * Computer Programming, Donald E. Knuth.
    */
   private static final int SMALL_PRIME_COUNT = 1000;
   private static final BigInteger[] SMALL_PRIME =
         new BigInteger[SMALL_PRIME_COUNT];
   static {
      long time = -System.currentTimeMillis();
      SMALL_PRIME[0] = TWO;
      int N = 3;
      int J = 0;
      int prime;
      P2: while (true) {
         SMALL_PRIME[++J] = BigInteger.valueOf(N);
         if (J >= 999) {
            break P2;
         }
         P4: while (true) {
            N += 2;
            P6: for (int K = 1; true; K++) {
               prime = SMALL_PRIME[K].intValue();
               if ((N % prime) == 0) {
                  continue P4;
               } else if ((N / prime) <= prime) {
                  continue P2;
               }
            }
         }
      }
      time += System.currentTimeMillis();
      if (DEBUG && debuglevel > 8) {
         StringBuffer sb;
         for (int i = 0; i < (SMALL_PRIME_COUNT / 10); i++) {
            sb = new StringBuffer();
            for (int j = 0; j < 10; j++) {
               sb.append(String.valueOf(SMALL_PRIME[i*10+j])).append(" ");
            }
            debug(sb.toString());
         }
      }
      if (DEBUG && debuglevel > 4) {
         debug("Generating first "+String.valueOf(SMALL_PRIME_COUNT)
               +" primes took: "+String.valueOf(time)+" ms.");
      }
   }

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private Prime() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Trial division for the first 1000 small primes.<p>
    *
    * Returns <code>true</code> if at least one small prime, among the first
    * 1000 ones, was found to divide the designated number. Retuens <code>false</code>
    * otherwise.<p>
    *
    * @param w the number to test.
    * @return <code>true</code> if at least one small prime was found to divide
    * the designated number.
    */
   public static boolean hasSmallPrimeDivisor(BigInteger w) {
      BigInteger prime;
      for (int i = 0; i < SMALL_PRIME_COUNT; i++) {
         prime = SMALL_PRIME[i];
         if (w.mod(prime).equals(ZERO)) {
            if (DEBUG && debuglevel > 4) {
               debug(prime.toString(16)+" | "+w.toString(16)+"...");
            }
            return true;
         }
      }
      if (DEBUG && debuglevel > 4) {
         debug(w.toString(16)+" has no small prime divisors...");
      }
      return false;
   }

   /**
    * Java port of Colin Plumb primality test (Euler Criterion) implementation
    * for a base of 2 --from bnlib-1.1 release, function primeTest() in prime.c.
    * this is his comments; (bn is our w).<p>
    *
    * "Now, check that bn is prime. If it passes to the base 2, it's prime
    * beyond all reasonable doubt, and everything else is just gravy, but it
    * gives people warm fuzzies to do it.<p>
    *
    * This starts with verifying Euler's criterion for a base of 2. This is the
    * fastest pseudoprimality test that I know of, saving a modular squaring
    * over a Fermat test, as well as being stronger. 7/8 of the time, it's as
    * strong as a strong pseudoprimality test, too. (The exception being when
    * <code>bn == 1 mod 8</code> and <code>2</code> is a quartic residue, i.e.
    * <code>bn</code> is of the form <code>a^2 + (8*b)^2</code>.) The precise
    * series of tricks used here is not documented anywhere, so here's an
    * explanation. Euler's criterion states that if <code>p</code> is prime
    * then <code>a^((p-1)/2)</code> is congruent to <code>Jacobi(a,p)</code>,
    * modulo <code>p</code>. <code>Jacobi(a, p)</code> is a function which is
    * <code>+1</code> if a is a square modulo <code>p</code>, and <code>-1</code>
    * if it is not. For <code>a = 2</code>, this is particularly simple. It's
    * <code>+1</code> if <code>p == +/-1 (mod 8)</code>, and <code>-1</code> if
    * <code>m == +/-3 (mod 8)</code>. If <code>p == 3 (mod 4)</code>, then all
    * a strong test does is compute <code>2^((p-1)/2)</code>. and see if it's
    * <code>+1</code> or <code>-1</code>. (Euler's criterion says <i>which</i>
    * it should be.) If <code>p == 5 (mod 8)</code>, then <code>2^((p-1)/2)</code>
    * is <code>-1</code>, so the initial step in a strong test, looking at
    * <code>2^((p-1)/4)</code>, is wasted --you're not going to find a
    * <code>+/-1</code> before then if it <b>is</b> prime, and it shouldn't
    * have either of those values if it isn't. So don't bother.<p>
    *
    * The remaining case is <code>p == 1 (mod 8)</code>. In this case, we
    * expect <code>2^((p-1)/2) == 1 (mod p)</code>, so we expect that the
    * square root of this, <code>2^((p-1)/4)</code>, will be <code>+/-1 (mod p)
    * </code>. Evaluating this saves us a modular squaring 1/4 of the time. If
    * it's <code>-1</code>, a strong pseudoprimality test would call <code>p</code>
    * prime as well. Only if the result is <code>+1</code>, indicating that
    * <code>2</code> is not only a quadratic residue, but a quartic one as well,
    * does a strong pseudoprimality test verify more things than this test does.
    * Good enough.<p>
    *
    * We could back that down another step, looking at <code>2^((p-1)/8)</code>
    * if there was a cheap way to determine if <code>2</code> were expected to
    * be a quartic residue or not. Dirichlet proved that <code>2</code> is a
    * quadratic residue iff <code>p</code> is of the form <code>a^2 + (8*b^2)</code>.
    * All primes <code>== 1 (mod 4)</code> can be expressed as <code>a^2 +
    * (2*b)^2</code>, but I see no cheap way to evaluate this condition."<p>
    *
    * @param w the number to test.
    * @return <code>true</code> iff the designated number passes Euler criterion
    * as implemented by Colin Plumb in his <i>bnlib</i> version 1.1.
    */
   public static boolean passEulerCriterion(BigInteger w) {
      BigInteger w_minus_one = w.subtract(ONE);
      BigInteger e = w_minus_one;
      // l is the 3 least-significant bits of e
      int l = e.and(BigInteger.valueOf(7L)).intValue();
      int j = 1; // Where to start in prime array for strong prime tests
      BigInteger A;
      int k;

      if ((l & 7) != 0) {
         e = e.shiftRight(1);
         A = TWO.modPow(e, w);
         if ((l & 7) == 6) { // bn == 7 mod 8, expect +1
            if (A.bitCount() != 1) {
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Euler criterion #1...");
               }
               return false; // Not prime
            }
            k = 1;
         } else { // bn == 3 or 5 mod 8, expect -1 == bn-1
            A = A.add(ONE);
            if (!A.equals(w)) {
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Euler criterion #2...");
               }
               return false; // Not prime
            }
            k = 1;
            if ((l & 4) != 0) { // bn == 5 mod 8, make odd for strong tests
               e = e.shiftRight(1);
               k = 2;
            }
         }
      } else { // bn == 1 mod 8, expect 2^((bn-1)/4) == +/-1 mod bn
         e = e.shiftRight(2);
         A = TWO.modPow(e, w);
         if (A.bitCount() == 1) {
            j = 0; // Re-do strong prime test to base 2
         } else {
            A = A.add(ONE);
            if (!A.equals(w)) {
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Euler criterion #3...");
               }
               return false; // Not prime
            }
         }
         // bnMakeOdd(n) = d * 2^s. Replaces n with d and returns s.
         k = e.getLowestSetBit();
         e = e.shiftRight(k);
         k += 2;
      }
      // It's prime!  Now go on to confirmation tests

      // Now, e = (bn-1)/2^k is odd.  k >= 1, and has a given value with
      // probability 2^-k, so its expected value is 2.  j = 1 in the usual case
      // when the previous test was as good as a strong prime test, but 1/8 of
      // the time, j = 0 because the strong prime test to the base 2 needs to
      // be re-done.
//      for (int i = j; i < SMALL_PRIME_COUNT; i++) {
      for (int i = j; i < 13; i++) { // try only the first 13 primes
         A = SMALL_PRIME[i];
         A = A.modPow(e, w);
         if (A.bitCount() == 1) {
            continue; // Passed this test
         }
         l = k;
         while (true) {
//            A = A.add(ONE);
//            if (A.equals(w)) { // Was result bn-1?
            if (A.equals(w_minus_one)) { // Was result bn-1?
               break; // Prime
            }
            if (--l == 0) { // Reached end, not -1? luck?
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Euler criterion #4...");
               }
               return false; // Failed, not prime
            }
            // This portion is executed, on average, once
//            A = A.subtract(ONE); // Put a back where it was
            A = A.modPow(TWO, w);
            if (A.bitCount() == 1) {
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Euler criterion #5...");
               }
               return false; // Failed, not prime
            }
         }
         // It worked (to the base primes[i])
      }
      if (DEBUG && debuglevel > 4) {
         debug(w.toString(16)+" passes Euler criterion...");
      }
      return true;
   }

   /**
    * Checks Fermat's Little Theorem for base 2; i.e. <code>2**(w-1) == 1
    * (mod w)</code>.<p>
    *
    * @param w the number to test.
    * @return <code>true</code> iff <code>2**(w-1) == 1 (mod w)</code>.
    */
   public static boolean passFermatLittleTheorem(BigInteger w) {
      BigInteger w_minus_one = w.subtract(ONE);
      boolean result = TWO.modPow(w_minus_one, w).equals(ONE);
      if (DEBUG && debuglevel > 4) {
         if (result) {
            debug(w.toString(16)+" passes Fermat's little theorem...");
         } else {
            debug(w.toString(16)+" fails Fermat's little theorem...");
         }
      }
      return result;
   }

   /**
    * Applies the Miller-Rabin strong probabilistic primality test.<p>
    *
    * The HAC (Handbook of Applied Cryptography), Alfred Menezes & al. Note
    * 4.57 states that for <code>q</code>, <code>n=18</code> is enough while
    * for <code>p</code>, <code>n=6</code> (512 bits) or <code>n=3</cdoe> (1024
    * bits) are enough to yield <i>robust</i> primality tests. The values used
    * are from table 4.4 given in Note 4.49.<p>
    *
    * @param w the number to test.
    * @return <code>true</code> iff the designated number passes the Miller-
    * Rabin probabilistic primality test for a computed number of rounds.
    */
   public static boolean passMillerRabin(BigInteger w) {
      // 1. Set i = 1 and n >= 50
      int wbitlen = w.bitLength();
      int n; // number of rounds
      if (wbitlen < 500) {
         n = 18;
      } else if (wbitlen < 550) {
         n = 6;
      } else if (wbitlen < 650) {
         n = 5;
      } else if (wbitlen < 850) {
         n = 4;
      } else {
         n = 3;
      }
      // 2. Set w = the integer to be tested, w = 1 + (2**a)m, where m is odd
      // and 2**a is the largest power of 2 dividing w - 1
      BigInteger w_minus_one = w.subtract(ONE);
      int a = w_minus_one.getLowestSetBit();
      BigInteger m = w_minus_one.shiftRight(a);
      BigInteger b, z;
      int wlen = wbitlen / 8; // we use 160 and 512+64n bit numbers
      byte[] kb = new byte[wlen];
      int j;
      step3: for (int i = 0; i < n; i++) {
         // 3. Generate a random integer b in the range 1 < b < w.
         do {
            PRNG.nextBytes(kb);
            b = new BigInteger(1, kb);
         } while (ONE.compareTo(b) < 0 && b.compareTo(w) < 0);
         // 4. Set j = 0 and z = b**m mod w
         j = 0;
         z = b.modPow(m, w);
         // 5. If j = 0 and z = 1, or if z = w - 1, go to step 9
         step5: while (!((j == 0 && z.equals(ONE)) || z.equals(w_minus_one))) {
            // 6. If j > 0 and z = 1, go to step 8
            // 7. j = j + 1. If j < a, set z = z**2 mod w and go to step 5
            // 8. w is not prime. Stop.
            if ((j > 0 && z.equals(ONE)) || ++j >= a) {
               if (DEBUG && debuglevel > 4) {
                  debug(w.toString(16)+" fails Miller-Rabin test...");
               }
               return false;
            }
            z = z.modPow(TWO, w);
         }
         // 9. If i < n, set i = i + 1 and go to step 3.
         // Otherwise, w is probably prime.
      }
      if (DEBUG && debuglevel > 4) {
         debug(w.toString(16)+" passes Miller-Rabin test...");
      }
      return true;
   }

   /**
    * Calls the method with same name and two arguments using the pre-configured
    * value for <code>DO_MILLER_RABIN</code>.
    *
    * @param w the integer to test.
    * @return <code>true</code> iff the designated number has no small prime
    * divisor passes the Euler criterion, and optionally a Miller-Rabin test.
    */
   public static boolean isProbablePrime(BigInteger w) {
      return isProbablePrime(w, DO_MILLER_RABIN);
   }

   /**
    * This implementation does not rely solely on the Miller-Rabin strong
    * probabilistic primality test to claim the primality of the designated
    * number. It instead, tries dividing the designated number by the first 1000
    * small primes, and if no divisor was found, invokes a port of Colin Plumb's
    * implementation of the Euler Criterion, with the option --passed as one of
    * its arguments-- to follow with the Miller-Rabin test.
    *
    * @param w the integer to test.
    * @param doMillerRabin if <code>true</code> and the designated integer was
    * already found to be a probable prime, then also do a Miller-Rabin test.
    * @return <code>true</code> iff the designated number has no small prime
    * divisor passes the Euler criterion, and optionally a Miller-Rabin test.
    */
   public static boolean isProbablePrime(BigInteger w, boolean doMillerRabin) {
      // eliminate trivial cases when w == 0 or 1
      if (w.equals(ZERO)) {
         return false;
      } else if (w.equals(ONE)) {
         return true;
      }

      // trial division with first 1000 primes
      if (hasSmallPrimeDivisor(w)) {
         if (DEBUG && debuglevel > 4) {
            debug(w.toString(16)+" has a small prime divisor. Rejected...");
         }
         return false;
      }

      // the following code is commented out since it's a special case (base 2)
      // of Euler's criterion.
//      if (passFermatLittleTheorem(w)) {
//         if (DEBUG && debuglevel > 4) {
//            debug(w.toString(16)+" passes Fermat's Little Theorem...");
//         }
//      } else {
//         if (DEBUG && debuglevel > 4) {
//            debug(w.toString(16)+" fails Fermat's Little Theorem. Rejected...");
//         }
//         return false;
//      }

      if (passEulerCriterion(w)) {
         if (DEBUG && debuglevel > 4) {
            debug(w.toString(16)+" passes Euler criterion...");
         }
      } else {
         if (DEBUG && debuglevel > 4) {
            debug(w.toString(16)+" fails Euler criterion. Rejected...");
         }
         return false;
      }

      // Miller-Rabin probabilistic primality test.
      if (doMillerRabin) {
         if (passMillerRabin(w)) {
            if (DEBUG && debuglevel > 4) {
               debug(w.toString(16)+" passes Miller-Rabin PPT...");
            }
         } else {
            if (DEBUG && debuglevel > 4) {
               debug(w.toString(16)+" fails Miller-Rabin PPT. Rejected...");
            }
            return false;
         }
      }

      if (DEBUG && debuglevel > 4) {
         debug(w.toString(16)+" is probable prime. Accepted...");
      }

      // now compare to JDK primality test
      if (DEBUG && debuglevel > 0 && !w.isProbablePrime(100)) {
         System.err.println("The gnu.crypto library and the JDK disagree on "
            +"whether 0x"+w.toString(16)+" is a probable prime or not.");
         System.err.println("While this library claims it is, the JDK claims"
            +" the opposite.");
         System.err.println("Please contact the maintainer of this library, "
            +"and provide this message for further investigation. TIA");
      }

      return true;
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
