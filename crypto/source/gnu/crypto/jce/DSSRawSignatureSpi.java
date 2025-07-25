package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: DSSRawSignatureSpi.java,v 1.1 2002-01-18 02:27:53 raif Exp $
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

import gnu.crypto.Registry;
import gnu.crypto.sig.dss.DSSSignatureRawCodec;

/**
 * The implementation of <i>Service Provider Interface</i> (<b>SPI</b>) adapter
 * for the DSS (Digital Signature Standard) signature scheme, encoded and/or
 * decoded in RAW format.<p>
 *
 * @version $Revision: 1.1 $
 */
public class DSSRawSignatureSpi extends SignatureAdapter {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public DSSRawSignatureSpi() {
      super(Registry.DSS_SIG, new DSSSignatureRawCodec());
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------
}
