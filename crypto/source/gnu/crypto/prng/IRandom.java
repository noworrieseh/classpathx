package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: IRandom.java,v 1.3 2002-01-11 21:48:42 raif Exp $
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

import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * The basic visible methods of any pseudo-random number generator.
 *
 * @version $Revision: 1.3 $
 */
public interface IRandom {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /** @return the canonical name of this instance. */
   String name();

   /**
    * Initialises the padding scheme with a designated block size, eventually
    * seeding it in preparation for operations.
    *
    * @param attributes a set of name-value pairs that describe the desired
    * future instance behaviour.
    * @exception IllegalArgumentException if at least one of the defined name/
    * value pairs contains invalid data.
    */
   void init(Map attributes) throws GeneralSecurityException;

   /**
    * Returns the next 8 bits of random data generated from this instance.
    *
    * @return the next 8 bits of random data generated from this instance.
    * @exception IllegalStateException if the instance is not yet initialised.
    * @exception LimitReachedException if this instance has reached its
    * theoretical limit for generating non-repetitive pseudo-random data.
    */
   byte nextByte() throws IllegalStateException, LimitReachedException;

   /**
    * Fills the designated byte array, starting from byte at index <i>offset</i>,
    * for a maximum of <code>length</code> bytes with the output of this
    * generator instance.
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
}
