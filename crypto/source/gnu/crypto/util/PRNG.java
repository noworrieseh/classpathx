package gnu.crypto.util;

// ----------------------------------------------------------------------------
// $Id: PRNG.java,v 1.1 2002-01-11 21:43:18 raif Exp $
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

import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.MDGenerator;
import java.util.HashMap;

/**
 * A useful Singleton hash-based (SHA) pseudo-random number generator used
 * throughout this library.
 *
 * @version $Revision: 1.1 $
 * @see gnu.crypto.prng.MDGenerator
 */
public class PRNG {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * <b>IMPORTANT:</b> If you're planning on using this library in "real-life"
    * applications, change this to <code>false</code> and re-compile for the
    * output of this pseudo-random number generator to be non-deterministic.
    * Leave it set to <code>true</code> for testing and debugging purposes only.
    */
   private static final boolean REPRODUCIBLE = true;

   /** SHA-based singleton random number generator. */
   private static final IRandom singleton;
   static {
      singleton = new MDGenerator();
      try {
         HashMap map = new HashMap();
         if (!REPRODUCIBLE) { // specify it a seed
            long t = System.currentTimeMillis();
            byte[] seed = new byte[] {
               (byte)(t >>> 56), (byte)(t >>> 48),
               (byte)(t >>> 40), (byte)(t >>> 32),
               (byte)(t >>> 24), (byte)(t >>> 16),
               (byte)(t >>>  8), (byte) t
            };
            map.put(MDGenerator.SEEED, seed);
         }
         singleton.init(map); // default is to use SHA-1 hash
      } catch (Exception x) {
         throw new ExceptionInInitializerError(x);
      }
   }

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private PRNG() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Completely fills the designated <code>buffer</code> with random data
    * generated by the underlying singleton.<p>
    *
    * @param buffer the place holder of random bytes generated by this PRNG
    * singleton. On output, the contents of <code>buffer</code> are replaced
    * with pseudo-random data, iff the <code>buffer</code> size is not zero.
    */
   public static void nextBytes(byte[] buffer) {
      nextBytes(buffer, 0, buffer.length);
   }

   /**
    * Fills the designated <code>buffer</code>, starting from byte at position
    * <code>offset</code> with, at most, <code>length</code> bytes of random
    * data generated by the underlying singleton.<p>
    *
    * @see gnu.crypto.prng.IRandom#nextBytes
    */
   public static void nextBytes(byte[] buffer, int offset, int length) {
      try {
         singleton.nextBytes(buffer, offset, length);
      } catch (LimitReachedException x) {
         // re-initialise
         try {
            HashMap map = new HashMap();
            if (!REPRODUCIBLE) { // specify it a seed
               long t = System.currentTimeMillis();
               byte[] seed = new byte[] {
                  (byte)(t >>> 56), (byte)(t >>> 48),
                  (byte)(t >>> 40), (byte)(t >>> 32),
                  (byte)(t >>> 24), (byte)(t >>> 16),
                  (byte)(t >>>  8), (byte) t
               };
               map.put(MDGenerator.SEEED, seed);
            }
            singleton.init(map); // default is to use SHA-1 hash
            singleton.nextBytes(buffer, offset, length);
         } catch (Exception y) {
            throw new ExceptionInInitializerError(y);
         }
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
