package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: IRandom.java,v 1.6 2002-07-06 23:53:22 raif Exp $
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

import java.util.Map;

/**
 * <p>The basic visible methods of any pseudo-random number generator.</p>
 *
 * <p>The [HAC] defines a PRNG (as implemented in this library) as follows:</p>
 *
 * <ul>
 *    <li>"5.6 Definition: A pseudorandom bit generator (PRBG) is said to pass
 *    the <em>next-bit test</em> if there is no polynomial-time algorithm which,
 *    on input of the first <code>L</code> bits of an output sequence <code>S</code>,
 *    can predict the <code>(L+1)</code>st bit of <code>S</code> with a
 *    probability significantly grater than <code>1/2</code>."</li>
 *
 *    <li>"5.8 Definition: A PRBG that passes the <em>next-bit test</em>
 *    (possibly under some plausible but unproved mathematical assumption such
 *    as the intractability of factoring integers) is called a
 *    <em>cryptographically secure pseudorandom bit generator</em> (CSPRBG)."</li>
 * </ul>
 *
 * <p><b>IMPLEMENTATION NOTE</b>: Although all the concrete classes in this
 * package implement the {@link Cloneable} interface, it is important to note
 * here that such an operation, for those algorithms that use an underlting
 * symmetric key block cipher, <b>DOES NOT</b> clone any session key material
 * that may have been used in initialising the source PRNG (the instance to be
 * cloned). Instead a clone of an already initialised PRNG, that uses and
 * underlying symmetric key block cipher, is another instance with a clone of
 * the same cipher that operates with the <b>same block size</b> but without any
 * knowledge of neither key material nor key size.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.cacr.math.uwaterloo.ca/hac">[HAC]</a>: Handbook of
 *    Applied Cryptography.<br>
 *    CRC Press, Inc. ISBN 0-8493-8523-7, 1997<br>
 *    Menezes, A., van Oorschot, P. and S. Vanstone.</li>
 * </ol>
 *
 * @version $Revision: 1.6 $
 */
public interface IRandom extends Cloneable {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns the canonical name of this instance.</p>
    *
    * @return the canonical name of this instance. */
   String name();

   /**
    * <p>Initialises the padding scheme with a designated block size, eventually
    * seeding it in preparation for operations.</p>
    *
    * @param attributes a set of name-value pairs that describe the desired
    * future instance behaviour.
    * @exception IllegalArgumentException if at least one of the defined name/
    * value pairs contains invalid data.
    */
   void init(Map attributes);

   /**
    * <p>Returns the next 8 bits of random data generated from this instance.</p>
    *
    * @return the next 8 bits of random data generated from this instance.
    * @exception IllegalStateException if the instance is not yet initialised.
    * @exception LimitReachedException if this instance has reached its
    * theoretical limit for generating non-repetitive pseudo-random data.
    */
   byte nextByte() throws IllegalStateException, LimitReachedException;

   /**
    * <p>Fills the designated byte array, starting from byte at index
    * <code>offset</code>, for a maximum of <code>length</code> bytes with the
    * output of this generator instance.
    *
    * @param out the placeholder to contain the generated random bytes.
    * @param offset the starting index in <i>out</i> to consider. This method
    * does nothing if this parameter is not within <code>0</code> and
    * <code>out.length</code>.
    * @param length the maximum number of required random bytes. This method
    * does nothing if this parameter is less than <code>1</code>.
    * @exception IllegalStateException if the instance is not yet initialised.
    * @exception LimitReachedException if this instance has reached its
    * theoretical limit for generating non-repetitive pseudo-random data.
    */
   void nextBytes(byte[] out, int offset, int length)
   throws IllegalStateException, LimitReachedException;

   /**
    * <p>Returns a clone copy of this instance.</p>
    *
    * @return a clone copy of this instance.
    */
   Object clone();
}
