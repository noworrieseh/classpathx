package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: Ent.java,v 1.2 2002-08-11 04:03:02 raif Exp $
//
// Copyright (C) 2002, Free Software Foundation, Inc.
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
import gnu.crypto.jce.GnuCrypto;
import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.LimitReachedException;

import java.security.SecureRandom;
import java.security.Security;
import java.security.Provider;
import java.util.Iterator;

/**
 * <p>This is a Java implementation of <em>Ent</em> (A Pseudorandom Number
 * Sequence Test Program) developed by <a href="http://www.fourmilab.ch/">John
 * Walker</a>) which applies various tests to sequences of bytes generated by
 * the GNU Crypto library pseudo-random number generator implementations.</p>
 *
 * <p>It is useful for those evaluating pseudorandom number generators for
 * encryption and statistical sampling applications, compression algorithms, and
 * other applications where the various computed indices are of interest.</p>
 *
 * <p>For a designated PRNG algorithm, this class computes the following
 * indices:</p>
 *
 * <ul>
 *    <li><b>Chi-square test</b>: The chi-square test is the most commonly used
 *    test for the randomness of data, and is extremely sensitive to errors in
 *    pseudorandom sequence generators.  The chi-square distribution is
 *    calculated for the stream of bytes in the file and expressed as an
 *    absolute number and a percentage which indicates how frequently a truly
 *    random sequence would exceed the value calculated.  We interpret the
 *    percentage as the degree to which the sequence tested is suspected of
 *    being non-random.  If the percentage is greater than 99% or less than 1%,
 *    the sequence is almost certainly not random.  If the percentage is between
 *    99% and 95% or between 1% and 5%, the sequence is suspect.  Percentages
 *    between 90% and 95% and 5% and 10% indicate the sequence is <em>almost
 *    suspect</em>.  Note that our JPEG file, while very dense in information, is
 *    far from random as revealed by the chi-square test.</li>
 *
 *    <p>Applying this test to the output of various pseudorandom sequence
 *    generators is interesting. The low-order 8 bits returned by the standard
 *    Unix rand() function, for example, yields:</p>
 *
 *       Chi square distribution for 500000 samples is 0.01, and randomly would
 *       exceed this value 99.99 percent of the times.
 *
 *    <p>While an improved generator [Park & Miller] reports:</p>
 *
 *       Chi square distribution for 500000 samples is 212.53, and randomly
 *       would exceed this value 95.00 percent of the times.
 *
 *    <p>Thus, the standard Unix generator (or at least the low-order bytes it
 *    returns) is unacceptably non-random, while the improved generator is much
 *    better but still sufficiently non-random to cause concern for demanding
 *    applications. Contrast both of these software generators with the chi-square
 *    result of a genuine random sequence created by timing radioactive decay
 *    events.</p>
 *
 *       Chi square distribution for 32768 samples is 237.05, and randomly would
 *       exceed this value 75.00 percent of the times.
 *
 *    <p>See [Knuth, pp. 35-40] for more information on the chi-square test.</p>
 *
 *    <li><b>Arithmetic mean</b>: This is simply the result of summing up all
 *    the (set) bits in the file and dividing by the file length.  If the data
 *    are close to random, this should be about 0.5.  If the mean departs from
 *    this value, the values are consistently high or low.</li>
 *
 *    <li><b>Monte Carlo value for Pi</b>: Each successive sequence of six bytes
 *    is used as 24 bit X and Y co-ordinates within a square.  If the distance
 *    of the randomly-generated point is less than the radius of a circle
 *    inscribed within the square, the six-byte sequence is considered a "hit".
 *    The percentage of hits can be used to calculate the value of Pi.  For very
 *    large streams (this approximation converges very slowly), the value will
 *    approach the correct value of Pi if the sequence is close to random.  A
 *    32768 byte file created by radioactive decay yielded:
 *
 *       Monte Carlo value for Pi is 3.139648438 (error 0.06 percent).
 *
 *    <li><b>Serial correlation coefficient</b>: This quantity measures the
 *    extent to which each byte in the file depends upon the previous byte.  For
 *    random sequences, this value (which can be positive or negative) will, of
 *    course, be close to <code>zero</code>.  A non-random byte stream such as a
 *    C program will yield a serial correlation coefficient on the order of
 *    <code>0.5</code>.  Wildly predictable data such as uncompressed bitmaps
 *    will exhibit serial correlation coefficients approaching <code>1</code>.
 *    See [Knuth, pp. 64-65] for more details.</li>
 * </ul>
 *
 * @version $Revision: 1.2 $
 */
public class Ent {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Number of bytes needed to compute a Monte Carlo PI calculation. */
   private static final int MC_XY_BYTES = 6;

   /** Limit for selecting points relative to the Monte Carlo circle. */
   private static final double INSIDE_CIRCLE =
         Math.pow(Math.pow(256.0, MC_XY_BYTES / 2.0) - 1, 2.0);

   /** Table of chi-square Xp values versus corresponding probabilities. */
   private static final double[][] CHI_SQUARE_P = new double[][] {
      {0.5, 0.25,   0.1,    0.05,   0.025,  0.01,   0.005,  0.001,  0.0005, 0.0001},
      {0.0, 0.6745, 1.2816, 1.6449, 1.9600, 2.3263, 2.5758, 3.0902, 3.2905, 3.7190}
   };

   private static final double PI = 3.14159265358979323846;

   /** Name of the PRNG algorithm. */
   private String name;

   /** The underlying GNU Crypto PRNG instance to test. */
   private IRandom prng;

   /** The underlying {@link SecureRandom} instance to test. */
   private SecureRandom rand;

   /** The work buffer. */
   private byte[] buffer = new byte[1024];

   /** Calculation duration in millis. */
   private long duration;

   /** Bit counters. */
   private long[] counters = new long[2];
   private long totalBits;

   /** Monte Carlo PI computation byte buffer. */
   private byte[] mcBuffer = new byte[6];
   private int mcBufferNdx;
   private double mcCount, mcInside;

   /** Serial Correlation Coefficient work variables. */
   private boolean sccFirst;
   private double scc, sccLast, sccU0, sccUn, sccT1, sccT2, sccT3;

   /** Other variables. */
   private double chiSquare, mean, pi;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public Ent(IRandom prng) {
      super();

      this.prng = prng;
      this.rand = null;
      this.name = prng.name();
      initInternal();
   }

   public Ent(String name, SecureRandom prng) {
      super();

      this.prng = null;
      this.rand = prng;
      this.name = name;
      initInternal();
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
//      if (args == null || args.length == 0) {
//         printUsage();
//         return;
//      }

      // ensure that our Provider is installed if it isnt
      Provider gnu = Security.getProvider(Registry.GNU_CRYPTO);
      if (gnu == null) {
         Security.addProvider(new GnuCrypto()); // dynamically adds our provider
      }

      try {
         // set defaults
         String name = null;

         // parse arguments
         for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) { // an option
               String option = arg.substring(1);
               if (option.equals("h")) {
                  printUsage();
                  continue;
               }
            }

            if (name == null) {
               name = args[i++];
               continue;
            }
         }

         // execute command
         SecureRandom prng;
         Ent cmd;
         if (name != null) {
            prng = SecureRandom.getInstance(name, Registry.GNU_CRYPTO);
            cmd = new Ent(name, prng);
            cmd.computeIndices();
            cmd.printResults();
         } else {
            for (Iterator it = GnuCrypto.getSecureRandomNames().iterator(); it.hasNext(); ) {
               name = (String) it.next();
               prng = SecureRandom.getInstance(name, Registry.GNU_CRYPTO);
               cmd = new Ent(name, prng);
               cmd.computeIndices();
               cmd.printResults();
            }
         }
      } catch (Exception x) {
         x.printStackTrace(System.err);
      }
   }

   /** Prints a simple help page to <code>System.err</code>. */
   private static final void printUsage() {
      System.err.println();
      System.err.println("Usage:");
      System.err.println("   gnu.crypto.tool.Ent (options) [algorithm]");
      System.err.println();
      System.err.println("Where:");
      System.err.println("   algorithm");
      System.err.println("      The canonical name of a PRNG algorithm. If omitted, then all");
      System.err.println("      PRNG implementations are exercised, one at a time.");
      System.err.println();
      System.err.println("Options:");
      System.err.println("   -h");
      System.err.println("      Print this help page.");
      System.err.println();
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void computeIndices() throws LimitReachedException {
      duration = -System.currentTimeMillis();
      if (prng != null) {
         for (int i = 0; i < 1024; i++) {
            prng.nextBytes(buffer, 0, 1024);
            update(buffer);
         }
      } else {
         for (int i = 0; i < 1024; i++) {
            rand.nextBytes(buffer);
            update(buffer);
         }
      }

      computeResults();
      duration += System.currentTimeMillis();
   }

   public long getDuration() {
      return duration;
   }

   public long getTotalBits() {
      return (long) totalBits;
   }

   public long getSetBits() {
      return (long) counters[1];
   }

   public double getMean() {
      return mean;
   }

   public double getMeanPercentDeviation() {
      return 100.0 * (Math.abs(0.5 - mean) / 0.5);
   }

   public double getChiSquare() {
      return chiSquare;
   }

   public double getChiSquareProbability() {
      double chip = Math.sqrt(2.0 * chiSquare) - 1.0;
      double a = Math.abs(chip);
      int i = 10;
      for ( ; --i >= 0; ) {
         if (CHI_SQUARE_P[1][i] < a) {
            break;
         }
      }
      chip = (chip >= 0.0) ? CHI_SQUARE_P[0][i] : 1.0 - CHI_SQUARE_P[0][i];
      return chip * 100.0;
   }

   public double getSerialCorrelationCoefficient() {
      return scc;
   }

   public double getPi() {
      return pi;
   }

   public double getPiPercentDeviation() {
      return 100.0 * (Math.abs(PI - pi) / PI);
   }

   private void initInternal() {
      counters[0] = counters[1] = totalBits = 0L;

      initMonteCarloBuffer();
      sccFirst = true;

      chiSquare = 0.0;

      mcCount = 0.0;
      mcInside = 0.0;
   }

   private void initMonteCarloBuffer() {
      for (int i = 0; i < mcBuffer.length; ) {
         mcBuffer[i++] = 0;
      }
      mcBufferNdx = 0;
   }

   private void update(byte[] buffer) {
      int b;
      for (int i = 0; i < buffer.length; i++) {
         b = buffer[i] & 0xFF; // process a byte at a time
//         updateBitCount(b);
         updateBitCountAndSCC(b);
         updateMonteCarloPI(b);
//         updateSerialCorrelation(b);
      }
   }

//   private void updateBitCount(int b) {
//      totalBits += 8;
//      for (int i = 0; i < 8; i++) {
//         counters[(b >>> i) & 0x01]++;
//      }
//   }

   private void updateBitCountAndSCC(int b) {
      int limit = (int) Math.min(8.0, Double.MAX_VALUE - totalBits);
      for (int i = 0; i < limit; i++) {
         totalBits++;

         counters[(b >>> 7) & 0x01]++;

         sccUn = b & 0x80;
         if (sccFirst) {
            sccFirst = false;
            sccLast = 0.0;
            sccU0 = sccUn;
         } else {
            sccT1 += sccLast * sccUn;
         }
         sccT2 += sccUn;
         sccT3 += sccUn * sccUn;
         sccLast = sccUn;
         b <<= 1;
      }
   }

   private void updateMonteCarloPI(int b) {
      mcBuffer[mcBufferNdx] = (byte) b;
      mcBufferNdx++;
      if (mcBufferNdx >= MC_XY_BYTES) {
         computeMonteCarloPI();
         initMonteCarloBuffer();
      }
   }

   private void computeMonteCarloPI() {
      mcCount++;
      double x = 0.0;
      double y = 0.0;
      for (int i = 0; i < MC_XY_BYTES / 2; i++) {
         x = (x * 256.0) + (mcBuffer[i]                     & 0xFF);
         y = (y * 256.0) + (mcBuffer[(MC_XY_BYTES / 2) + i] & 0xFF);
      }
      if ((x * x + y * y) <= INSIDE_CIRCLE) {
         mcInside++;
      }
   }

//   private void updateSerialCorrelation(int b) {
//      for (int i = 0; i < 8; i++) {
//         sccUn = b & 0x80;
//         if (sccFirst) {
//            sccFirst = false;
//            sccLast = 0.0;
//            sccU0 = sccUn;
//         } else {
//            sccT1 += sccLast * sccUn;
//         }
//         sccT2 += sccUn;
//         sccT3 += sccUn * sccUn;
//         sccLast = sccUn;
//         b <<= 1;
//      }
//   }

   private void computeResults() {
      // complete calculation of serial correlation coefficient
      sccT1 += sccLast * sccU0;
      sccT2 = sccT2 * sccT2;
      scc = totalBits * sccT3 - sccT2;
      if (scc == 0.0) {
         scc = -100000;
      } else {
         scc = (totalBits * sccT1 - sccT2) / scc;
      }

      // compute Chi-Square distribution
      double cexp = totalBits / 2.0;  // expected count per bit counter
      double a = counters[0] - cexp;
      double b = counters[1] - cexp;
      chiSquare = (a * a + b * b) / cexp;
      mean = counters[1] * 1.0 / totalBits;

      // compute Monte Carlo value for PI from % of hits within the circle
      pi = 4.0 * mcInside / mcCount;
   }

   private void printResults() {
      System.out.println();
      System.out.println("Total execution time (ms): "+String.valueOf(duration));
      System.out.println("Computed indices for "+String.valueOf(name)+":");
      System.out.println("                  Total bit count: "
            +String.valueOf((long) getTotalBits()));
      System.out.println("           Mean value of set bits: "
            +String.valueOf(getMean()));
      System.out.println("                 Mean % deviation: "
            +String.valueOf(getMeanPercentDeviation()));
      System.out.println("          Chi-square distribution: "
            +String.valueOf(getChiSquare()));
      System.out.println("  Chi-square excess % probability: "
            +String.valueOf(getChiSquareProbability()));
      System.out.println("                      Computed PI: "
            +String.valueOf(getPi()));
      System.out.println("          Computed PI % deviation: "
            +String.valueOf(getPiPercentDeviation()));
      System.out.println("   Serial Correlation Coefficient: "
            +String.valueOf(getSerialCorrelationCoefficient()));
   }
}