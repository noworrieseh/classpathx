package gnu.crypto.sig.rsa;

// ----------------------------------------------------------------------------
// $Id: RSAPSSSignatureRawCodec.java,v 1.1 2002-01-11 21:21:57 raif Exp $
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

/**
 * An object that implements the {@link gnu.crypto.sig.ISignatureCodec}
 * operations for the <i>Raw</i> format to use with RSA-PSS signatures.<p>
 *
 * @version $Revision: 1.1 $
 */
public class RSAPSSSignatureRawCodec implements ISignatureCodec {

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
    * Returns the encoded form of the designated RSA-PSS signature object
    * according to the <i>Raw</i> format supported by this library.<p>
    *
    * The <i>Raw</i> format for an RSA-PSS signature, in this implementation, is
    * a byte sequence consisting of the following:
    *
    * <ol>
    *		<li>4-byte magic consisting of the constant: 0x474E5544,<li>
    *		<li>1-byte version consisting of the constant: 0x01,</li>
    *		<li>4-byte count of following bytes representing the RSA-PSS signature
    *    bytes in internet order,</li>
    *		<li>the RSA-PSS signature bytes in internet order.</li>
    * </ol>
    *
    * @param signature the signature to encode, consisting of the output of the
    * <code>sign()</code> method of a {@link gnu.crypto.sig.rsa.RSAPSSSignature}
    * instance --a byte array.
    * @return the <i>Raw</i> format encoding of the designated signature.
    * @exception IllegalArgumentException if the designated signature is not an
    * RSA-PSS one.
    */
   public byte[] encodeSignature(Object signature) {
      byte[] buffer;
      try {
         buffer = (byte[]) signature;
      } catch (Exception x) {
         throw new IllegalArgumentException("key");
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      // magic
      baos.write(0x47);
      baos.write(0x4E);
      baos.write(0x55);
      baos.write(0x44);

      // version
      baos.write(0x01);

      // signature bytes
      int length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);

      return baos.toByteArray();
   }

   public Object decodeSignature(byte[] k) {
      // magic
      if (k[0] != 0x47 || k[1] != 0x4E || k[2] != 0x55 || k[3] != 0x44) {
         throw new IllegalArgumentException("magic");
      }

      // version
      if (k[4] != 0x01) {
         throw new IllegalArgumentException("version");
      }

      int ndx = 5;
      int length;

      // signature bytes
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      byte[] result = new byte[length];
      System.arraycopy(k, ndx, result, 0, length);

      return result;
   }
}
