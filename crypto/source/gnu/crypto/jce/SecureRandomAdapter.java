package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: SecureRandomAdapter.java,v 1.1 2002-01-17 11:49:41 raif Exp $
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
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.MDGenerator;

import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.HashMap;

/**
 * The implementation of a generic {@link java.security.SecureRandom} adapter
 * class to wrap gnu.crypto prng instances.<p>
 *
 * This class defines the <i>Service Provider Interface</i> (<b>SPI</b>) for the
 * {@link java.security.SecureRandom} class, which provides the functionality
 * of a cryptographically strong pseudo-random number generator.<p>
 *
 * All the abstract methods in the {@link java.security.SecureRandomSpi} class
 * are implemented by this class and all its sub-classes.<p>
 *
 * @version $Revision: 1.1 $
 */
abstract class SecureRandomAdapter extends SecureRandomSpi {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Our underlying prng instance. */
   private MDGenerator adaptee = new MDGenerator();

   /** The name of the message digest algorithm used by the adaptee. */
   private String mdName;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.
    *
    * @param mdName the canonical name of the underlying hash algorithm.
    */
   protected SecureRandomAdapter(String mdName) {
      super();

      this.mdName = mdName;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.SecureRandomSpi interface implementation
   // -------------------------------------------------------------------------

   public void engineSetSeed(byte[] seed) {
      HashMap attributes = new HashMap();
      attributes.put(MDGenerator.MD_NAME, mdName);
      attributes.put(MDGenerator.SEEED, seed);
      adaptee.setup(attributes);
   }

   public void engineNextBytes(byte[] bytes) {
      if (!adaptee.isInitialised()) {
         this.engineSetSeed(new byte[0]);
      }
      try {
         adaptee.nextBytes(bytes, 0, bytes.length);
      } catch (LimitReachedException ignored) {
      }
   }

   public byte[] engineGenerateSeed(int numBytes) {
      if (numBytes < 1) {
         return new byte[0];
      }
      byte[] result = new byte[numBytes];
      this.engineNextBytes(result);
      return result;
   }

   // Other instance methods
   // -------------------------------------------------------------------------
}
