package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: MDGenerator.java,v 1.2 2002-01-11 21:47:13 raif Exp $
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
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MDGenerator extends BasePRNG {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Property name of underlying hash algorithm for this generator. */
   public static final String MD_NAME = "gnu.crypto.prng.md.hash.name";

   /** Property name of seed material. */
   public static final String SEEED = "gnu.crypto.prng.md.seed";

   /** The next block to digest. */
   private byte[] data;

   /** The underlying hash instance. */
   private IMessageDigest md;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public MDGenerator() {
      super(Registry.MD_PRNG);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Implementation of abstract methods in BaseRandom
   // -------------------------------------------------------------------------

   public void setup(Map attributes)
   throws GeneralSecurityException, IllegalStateException {
      // find out which hash to use
      String underlyingMD = (String) attributes.get(MD_NAME);
      if (underlyingMD == null) {
         underlyingMD = "sha-160";
      }
      // ensure we have a reliable implementation of this hash
      md = HashFactory.getInstance(underlyingMD);

      // get the seeed
      byte[] seed = (byte[]) attributes.get(SEEED);
      if (seed == null) {
         seed = new byte[0];
      }

      md.update(seed, 0, seed.length);
   }

   public byte[] nextBlock() throws LimitReachedException {
      IMessageDigest mdc = null;
      try {
         mdc = (IMessageDigest) md.clone();
      } catch (CloneNotSupportedException ignored) {
         // should not happen with our implementations
      }
      byte[] result = mdc.digest();
      md.update(result, 0, result.length);

      return result;
   }
}
