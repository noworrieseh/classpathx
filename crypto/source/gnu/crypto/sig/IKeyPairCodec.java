package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: IKeyPairCodec.java,v 1.1 2001-12-30 15:59:06 raif Exp $
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

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * The visible methods of an object that knows how to encode and decode
 * cryptographic asymmetric keypairs. Codecs are useful for (a) externalising 
 * public and private keys for storage and on-the-wire transmission, as well as 
 * (b) re-creating their internal Java representation from external sources.
 *
 * @version $Revision: 1.1 $
 */
public interface IKeyPairCodec {

   // Constants
   // -------------------------------------------------------------------------

   /** Constant identifying the <i>Raw</i> format. */
   int RAW_FORMAT = 1;

   // Method(s)
   // -------------------------------------------------------------------------

   /**
    * Returns the unique identifier (within this library) of the format used
    * to externalise public and private keys.<p>
    *
    * @return the identifier of the format, the object supports.
    */
   int getFormatID();

   /**
    * Encodes an instance of a public key for storage or transmission purposes.<p>
    *
    * @param key the non-null key to encode.
    * @return a byte sequence representing the encoding of the designated key 
    * according to the format supported by this codec.
    * @exception IllegalArgumentException if the designated key is not supported
    * by this codec.
    */
   byte[] encodePublicKey(PublicKey key);

   /**
    * Encodes an instance of a private key for storage or transmission purposes.<p>
    *
    * @param key the non-null key to encode.
    * @return a byte sequence representing the encoding of the designated key 
    * according to the format supported by this codec.
    * @exception IllegalArgumentException if the designated key is not supported
    * by this codec.
    */
   byte[] encodePrivateKey(PrivateKey key);

   /**
    * Decodes an instance of an external public key into its native Java
    * representation.<p>
    *
    * @param input the source of the externalised key to decode.
    * @return a concrete instance of a public key, reconstructed from the
    * designated input.
    * @exception IllegalArgumentException if the designated input does not
    * contain a known representation of a public key for the format supported by 
    * the concrete codec.
    */
   PublicKey decodePublicKey(byte[] input);

   /**
    * Decodes an instance of an external private key into its native Java
    * representation.<p>
    *
    * @param input the source of the externalised key to decode.
    * @return a concrete instance of a private key, reconstructed from the
    * designated input.
    * @exception IllegalArgumentException if the designated input does not
    * contain a known representation of a private key for the format supported
    * by the concrete codec.
    */
   PrivateKey decodePrivateKey(byte[] input);
}
