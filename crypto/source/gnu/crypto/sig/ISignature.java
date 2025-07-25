package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: ISignature.java,v 1.4 2002-01-28 01:43:23 raif Exp $
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

import java.util.Map;

/**
 * The visible methods of every signature-with-appendix scheme.<p>
 *
 * The Handbook of Applied Cryptography (HAC), by A. Menezes & al. states:
 * "Digital signature schemes which require the message as input to the
 * verification algorithm are called <i>digital signature schemes with
 * appendix</i>. ... They rely on cryptographic hash functions rather than
 * customised redundancy functions, and are less prone to existential forgery
 * attacks."<p>
 *
 * References:<br>
 * <a href="http://www.cacr.math.uwaterloo.ca/hac/">Handbook of Applied
 * Cryptography</a>, Alfred J. Menezes, Paul C. van Oorschot and Scott A.
 * Vanstone. Section 11.2.2 Digital signature schemes with appendix.<p>
 *
 * @version $Revision: 1.4 $
 */
public interface ISignature extends Cloneable {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /**
    * Returns the canonical name of this signature scheme.<p>
    *
    * @return the canonical name of this instance.
    */
   String name();

   /**
    * Initialises this instance for signature verification.<p>
    *
    * @param attributes the attributes to use for setting up this instance.
    * @exception IllegalArgumentException if the designated public key is not
    * appropriate for this signature scheme.
    */
   void setupVerify(Map attributes) throws IllegalArgumentException;

   /**
    * Initialises this instance for signature generation.<p>
    *
    * @param attributes the attributes to use for setting up this instance.
    * @exception IllegalArgumentException if the designated private key is not
    * appropriate for this signature scheme.
    */
   void setupSign(Map attributes) throws IllegalArgumentException;

   /**
    * Digests one byte of a message for signing or verification purposes.<p>
    *
    * @param b the message byte to digest.
    * @exception IllegalStateException if this instance was not setup for
    * signature generation/verification.
    */
   void update(byte b) throws IllegalStateException;

   /**
    * Digests a sequence of bytes from a message for signing or verification
    * purposes.<p>
    *
    * @param buffer the byte sequence to consider.
    * @param offset the byte poisition in <code>buffer</code> of the first byte
    * to consider.
    * @param length the number of bytes in <code>buffer</code> starting from the
    * byte at index <code>offset</code> to digest.
    * @exception IllegalStateException if this instance was not setup for
    * signature generation/verification.
    */
   void update(byte[] buffer, int offset, int length)
   throws IllegalStateException;

   /**
    * Terminates a signature generation phase by digesting and processing the
    * context of the underlying message digest algorithm instance.<p>
    *
    * @return a {@link java.lang.Object} representing the native output of the
    * signature scheme implementation.
    * @exception IllegalStateException if this instance was not setup for
    * signature generation.
    */
   Object sign() throws IllegalStateException;

   /**
    * Terminates a signature verification phase by digesting and processing the
    * context of the underlying message digest algorithm instance.<p>
    *
    * @param signature a native signature object previously generated by an
    * invocation of the <code>sign()</code> method.
    * @return <code>true</code> iff the outpout of the verification phase
    * confirms that the designated signature object has been generated using the
    * corresponding public key of the recepient.
    * @exception IllegalStateException if this instance was not setup for
    * signature verification.
    */
   boolean verify(Object signature) throws IllegalStateException;

   /**
    * Returns a clone copy of this instance.
    *
    * @return a clone copy of this instance.
    */
   Object clone();
}
