package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSSignatureRawCodec.java,v 1.1 2002-01-11 21:35:21 raif Exp $
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

import gnu.crypto.sig.ISignatureCodec;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

/**
 * An object that implements the {@link gnu.crypto.sig.ISignatureCodec}
 * operations for the <i>Raw</i> format to use with DSS signatures.<p>
 *
 * @version $Revision: 1.1 $
 */
public class DSSSignatureRawCodec implements ISignatureCodec {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   // implicit 0-arguments constructor

   // Class methods
   // -------------------------------------------------------------------------

   // gnu.crypto.sig.IKeyPairCodec interface implementation
   // -------------------------------------------------------------------------

   public int getFormatID() {
      return RAW_FORMAT;
   }

   /**
    * Returns the encoded form of the designated DSS (Digital Signature
    * Standard) signature object according to the <i>Raw</i> format supported by
    * this library.<p>
    *
    * The <i>Raw</i> format for a DSA signature, in this implementation, is a
    * byte sequence consisting of the following:
    *
    * <ol>
    *		<li>4-byte magic consisting of the constant: 0x474E5542,<li>
    *		<li>1-byte version consisting of the constant: 0x01,</li>
    *		<li>4-byte count of following bytes representing the DSS parameter
    *		<code>r</code> in internet order,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained
    *		by invoking the <code>toByteArray()</code> method on the DSS parameter
    *		<code>r</code>,</li>
    *		<li>4-byte count of following bytes representing the DSS parameter
    *		<code>s</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained
    *		by invoking the <code>toByteArray()</code> method on the DSS parameter
    *		<code>s</code>.</li>
    * </ol>
    *
    * @param signature the signature to encode, consisting of the two DSS
    * parameters <code>r</code> and <code>s</code> as a {@link java.math.BigInteger}
    * array.
    * @return the <i>Raw</i> format encoding of the designated signature.
    * @exception IllegalArgumentException if the designated signature is not a
    * DSS (Digital Signature Standard) one.
    */
   public byte[] encodeSignature(Object signature) {
      BigInteger r, s;
      try {
         BigInteger[] sig = (BigInteger[]) signature;
         r = sig[0];
         s = sig[1];
      } catch (Exception x) {
         throw new IllegalArgumentException("key");
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      // magic
      baos.write(0x47);
      baos.write(0x4E);
      baos.write(0x55);
      baos.write(0x42);

      // version
      baos.write(0x01);

      // r
      byte[] buffer = r.toByteArray();
      int length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);

      // s
      buffer = s.toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);

      return baos.toByteArray();
   }

   public Object decodeSignature(byte[] k) {
      // magic
      if (k[0] != 0x47 || k[1] != 0x4E || k[2] != 0x55 || k[3] != 0x42) {
         throw new IllegalArgumentException("magic");
      }

      // version
      if (k[4] != 0x01) {
         throw new IllegalArgumentException("version");
      }

      int ndx = 5;
      int length;
      byte[] buffer;

      // r
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger r = new BigInteger(1, buffer);

      // s
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger s = new BigInteger(1, buffer);

      return new BigInteger[] {r, s};
   }
}
