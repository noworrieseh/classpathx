package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: ISignatureCodec.java,v 1.2 2002-01-11 21:40:18 raif Exp $
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
 * cryptographic signatures. Codecs are useful for (a) externalising signature
 * output data for storage and on-the-wire transmission, as well as (b) re-
 * creating their internal Java representation from external sources.
 *
 * @version $Revision: 1.2 $
 */
public interface ISignatureCodec {

   // Constants
   // -------------------------------------------------------------------------

   /** Constant identifying the <i>Raw</i> format. */
   int RAW_FORMAT = 1;

   // Method(s)
   // -------------------------------------------------------------------------

   int getFormatID();

   byte[] encodeSignature(Object signature);

   Object decodeSignature(byte[] input);
}
