package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSPrivateKey.java,v 1.2 2002-01-11 21:33:24 raif Exp $
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
import java.security.PrivateKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;

/**
 * An object that embodies a DSS (Digital Signature Standard) private key.<p>
 *
 * @version $Revision: 1.2 $
 */
public class DSSPrivateKey extends DSSKey implements PrivateKey, DSAPrivateKey {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * A randomly or pseudorandomly generated integer with <code>0 &lt; x &lt;
    * q</code>.
    */
   private final BigInteger x;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial constructor.<p>
    *
    * @param p the public modulus.
    * @param q the public prime divisor of <code>p-1</code>.
    * @param g a generator of the unique cyclic group <code>Z<sup>*</sup>
    * <sub>p</sub></code>.
    * @param x the private key part.
    */
   public DSSPrivateKey(BigInteger p, BigInteger q, BigInteger g, BigInteger x) {
      super(p, q, g);

      this.x = x;
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * A class method that takes the output of the <code>encodePrivateKey()</code>
    * method of a DSS keypair codec object (an instance implementing
    * {@link gnu.crypto.sig.IKeyPairCodec} for DSS keys, and re-constructs an
    * instance of this object.<p>
    *
    * @param k the contents of a previously encoded instance of this object.
    * @exception ArrayIndexOutOfBoundsException if there is not enough bytes,
    * in <code>k</code>, to represent a valid encoding of an instance of
    * this object.
    * @exception IllegalArgumentException if the byte sequence does not
    * represent a valid encoding of an instance of this object.
    */
   public static DSSPrivateKey valueOf(byte[] k) {
      // check magic
      if (k[0] == 0x47 && k[1] == 0x53 && k[2] == 0x44 && k[3] == 0x41) {
         // it's in raw format. get a raw codec for it
         IKeyPairCodec codec = new DSSKeyPairRawCodec();
         return (DSSPrivateKey) codec.decodePrivateKey(k);
      } else {
         throw new IllegalArgumentException("magic");
      }
   }

   // java.security.interfaces.DSAPrivateKey interface implementation
   // -------------------------------------------------------------------------

   public BigInteger getX() {
      return x;
   }

   /** @deprecated see getEncoded(int). */
   public byte[] getEncoded() {
      return getEncoded(IKeyPairCodec.RAW_FORMAT);
   }

   // Other instance methods
   // -------------------------------------------------------------------------

   /**
    * Returns the encoded form of this private key according to the designated
    * format.<p>
    *
    * @param format the desired format identifier of the resulting encoding.
    * @return the byte sequence encoding this key according to the designated
    * format.
    * @exception IllegalArgumentException if the format is not supported.
    * @see gnu.crypto.sig.dss.DSSKeyPairRawCodec
    */
   public byte[] getEncoded(int format) {
      byte[] result;
      switch (format) {
      case IKeyPairCodec.RAW_FORMAT:
         result = new DSSKeyPairRawCodec().encodePrivateKey(this);
         break;
      default:
         throw new IllegalArgumentException("format");
      }
      return result;
   }

   /**
    * Returns <code>true</code> if the designated object is an instance of
    * {@link java.security.interfaces.DSAPrivateKey} and has the same DSS
    * (Digital Signature Standard) parameter values as this one.<p>
    *
    * @param obj the other non-null DSS key to compare to.
    * @return <code>true</code> if the designated object is of the same type and
    * value as this one.
    */
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof DSAPrivateKey)) {
         return false;
      }
      DSAPrivateKey that = (DSAPrivateKey) obj;
      return super.equals(that) && x.equals(that.getX());
   }
}
