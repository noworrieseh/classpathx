package gnu.crypto.sig.dss;

// ----------------------------------------------------------------------------
// $Id: DSSKeyPairRawCodec.java,v 1.1 2001-12-30 15:57:53 raif Exp $
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
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAParams;

/**
 * An object that implements the {@link gnu.crypto.sig.IKeyPairCodec} interface
 * for the <i>Raw</i> format.<p>
 *
 * @version $Revision: 1.1 $
 */
public class DSSKeyPairRawCodec implements IKeyPairCodec {

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
    * Standard) private key according to the <i>Raw</i> format supported by
    * this library.<p>
    *
    * The <i>Raw</i> format for a DSA private key, in this implementation, is a 
    * byte sequence consisting of the following:
    *
    * <ol>
    *		<li>4-byte magic consisting of the constant: 0x47534442,<li>
    *		<li>1-byte version consisting of the constant: 0x01,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>p</code> in internet order,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>p</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>q</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>q</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>g</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>g</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>y</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>y</code>,</li>
    * </ol>
    *
    * @param key the key to encode.
    * @return the <i>Raw</i> format encoding of the designated key.
    * @exception IllegalArgumentException if the designated key is not a DSS
    * (Digital Signature Standard) one.
    */
   public byte[] encodePublicKey(PublicKey key) {
      if (!(key instanceof DSSPublicKey)) {
         throw new IllegalArgumentException("key");
      }
      
      DSSPublicKey dssKey = (DSSPublicKey) key;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
      // magic
      baos.write(0x47);
      baos.write(0x53);
      baos.write(0x44);
      baos.write(0x42);
      
      // version
      baos.write(0x01);
      
      // p
      byte[] buffer = dssKey.getParams().getP().toByteArray();
      int length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // q
      buffer = dssKey.getParams().getQ().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // g
      buffer = dssKey.getParams().getG().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // y
      buffer = dssKey.getY().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      return baos.toByteArray();
   }

   public PublicKey decodePublicKey(byte[] k) {
      // magic
      if (k[0] != 0x47 || k[1] != 0x53 || k[2] != 0x44 || k[3] != 0x42) {
         throw new IllegalArgumentException("magic");
      }
      
      // version
      if (k[4] != 0x01) {
         throw new IllegalArgumentException("version");
      }
      int ndx = 5;
      
      int length;
      byte[] buffer;
      
      // p
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger p = new BigInteger(1, buffer);
      
      // q
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger q = new BigInteger(1, buffer);
      
      // g
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger g = new BigInteger(1, buffer);
      
      // y
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger y = new BigInteger(1, buffer);
      
      return new DSSPublicKey(p, q, g, y);
   }

   /**
    * Returns the encoded form of the designated DSS (Digital Signature
    * Standard) private key according to the <i>Raw</i> format supported by
    * this library.<p>
    *
    * The <i>Raw</i> format for a DSA private key, in this implementation, is a 
    * byte sequence consisting of the following:
    *
    * <ol>
    *		<li>4-byte magic consisting of the constant: 0x47534441,<li>
    *		<li>1-byte version consisting of the constant: 0x01,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>p</code> in internet order,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>p</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>q</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>q</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>g</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>g</code>,</li>
    *		<li>4-byte count of following bytes representing the DSA parameter 
    *		<code>x</code>,</li>
    *		<li>n-bytes representation of a {@link java.math.BigInteger} obtained 
    *		by invoking the <code>toByteArray()</code> method on the DSA parameter 
    *		<code>x</code>,</li>
    * </ol>
    *
    * @param key the key to encode.
    * @return the <i>Raw</i> format encoding of the designated key.
    */
   public byte[] encodePrivateKey(PrivateKey key) {
      if (!(key instanceof DSSPrivateKey)) {
         throw new IllegalArgumentException("key");
      }
      
      DSSPrivateKey dssKey = (DSSPrivateKey) key;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
      // magic
      baos.write(0x47);
      baos.write(0x53);
      baos.write(0x44);
      baos.write(0x41);
      
      // version
      baos.write(0x01);
      
      // p
      byte[] buffer = dssKey.getParams().getP().toByteArray();
      int length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // q
      buffer = dssKey.getParams().getQ().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // g
      buffer = dssKey.getParams().getG().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      // x
      buffer = dssKey.getX().toByteArray();
      length = buffer.length;
      baos.write( length >>> 24        );
      baos.write((length >>> 16) & 0xFF);
      baos.write((length >>>  8) & 0xFF);
      baos.write( length         & 0xFF);
      baos.write(buffer, 0, length);
      
      return baos.toByteArray();
   }

   public PrivateKey decodePrivateKey(byte[] k) {
      // magic
      if (k[0] != 0x47 || k[1] != 0x53 || k[2] != 0x44 || k[3] != 0x41) {
         throw new IllegalArgumentException("magic");
      }
      
      // version
      if (k[4] != 0x01) {
         throw new IllegalArgumentException("version");
      }
      int ndx = 5;
      
      int length;
      byte[] buffer;
      
      // p
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger p = new BigInteger(1, buffer);
      
      // q
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger q = new BigInteger(1, buffer);
      
      // g
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger g = new BigInteger(1, buffer);
      
      // x
      length =  k[ndx++]         << 24 |
               (k[ndx++] & 0xFF) << 16 |
               (k[ndx++] & 0xFF) <<  8 |
               (k[ndx++] & 0xFF);
      buffer = new byte[length];
      System.arraycopy(k, ndx, buffer, 0, length);
      ndx += length;
      BigInteger x = new BigInteger(1, buffer);
      
      return new DSSPrivateKey(p, q, g, x);
   }
}
