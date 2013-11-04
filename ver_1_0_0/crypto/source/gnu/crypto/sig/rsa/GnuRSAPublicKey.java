package gnu.crypto.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: GnuRSAPublicKey.java,v 1.1 2002-01-11 21:21:57 raif Exp $
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

import gnu.crypto.sig.IKeyPairCodec;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * An object that encapsulates an RSA public key.<p>
 *
 * References:<br>
 * <a href="http://www.cosic.esat.kuleuven.ac.be/nessie/workshop/submissions/rsa-pss.zip">
 * RSA-PSS Signature Scheme with Appendix</a>, part B. Primitive specification
 * and supporting documentation. Jakob Jonsson and Burt Kaliski.<p>
 *
 * @version $Revision: 1.1 $
 */
public class GnuRSAPublicKey extends GnuRSAKey
implements PublicKey, RSAPublicKey {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The public exponent of an RSA public key. */
   private final BigInteger e;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial constructor.<p>
    *
    * @param n the modulus.
    * @param e the public exponent.
    */
   public GnuRSAPublicKey(BigInteger n, BigInteger e) {
      super(n);

      this.e = e;
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * A class method that takes the output of the <code>encodePublicKey()</code>
    * method of an RSA keypair codec object (an instance implementing
    * {@link gnu.crypto.sig.IKeyPairCodec} for RSA keys, and re-constructs an
    * instance of this object.<p>
    *
    * @param k the contents of a previously encoded instance of this object.
    * @exception ArrayIndexOutOfBoundsException if there is not enough bytes,
    * in <code>k</code>, to represent a valid encoding of an instance of
    * this object.
    * @exception IllegalArgumentException if the byte sequence does not
    * represent a valid encoding of an instance of this object.
    */
   public static GnuRSAPublicKey valueOf(byte[] k) {
/*
      // check magic
      if (k[0] == 0x47 && k[1] == 0x53 && k[2] == 0x44 && k[3] == 0x42) {
         // it's in raw format. get a raw codec for it
         IKeyPairCodec codec = new DSSKeyPairRawCodec();
         return (DSSPublicKey) codec.decodePublicKey(k);
      } else {
         throw new IllegalArgumentException("magic");
      }
*/
      return null;
   }

   // java.security.interfaces.RSAPublicKey interface implementation
   // -------------------------------------------------------------------------

   public BigInteger getPublicExponent() {
      return e;
   }

   /** @deprecated see getEncoded(int). */
   public byte[] getEncoded() {
      return getEncoded(IKeyPairCodec.RAW_FORMAT);
   }

   // Other instance methods
   // -------------------------------------------------------------------------

   /**
    * Returns the encoded form of this public key according to the designated
    * format.<p>
    *
    * @param format the desired format identifier of the resulting encoding.
    * @return the byte sequence encoding this key according to the designated
    * format.
    * @exception IllegalArgumentException if the format is not supported.
    * @see gnu.crypto.sig.dss.DSSKeyPairRawCodec
    */
   public byte[] getEncoded(int format) {
      byte[] result = null;
      switch (format) {
      case IKeyPairCodec.RAW_FORMAT:
         result = new RSAKeyPairRawCodec().encodePublicKey(this);
         break;
      default:
         throw new IllegalArgumentException("format");
      }
      return result;
   }

   /**
    * Returns <code>true</code> if the designated object is an instance of this
    * class and has the same DSS (Digital Signature Standard) parameter values
    * as this one.<p>
    *
    * @param obj the other non-null DSS key to compare to.
    * @return <code>true</code> if the designated object is of the same type and
    * value as this one.
    */
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof RSAPublicKey)) {
         return false;
      }
      RSAPublicKey that = (RSAPublicKey) obj;
      return super.equals(that) && e.equals(that.getPublicExponent());
   }
}
