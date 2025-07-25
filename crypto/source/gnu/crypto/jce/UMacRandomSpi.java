package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: UMacRandomSpi.java,v 1.2 2002-08-07 10:00:46 raif Exp $
//
// Copyright (C) 2002, Free Software Foundation, Inc.
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
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.prng.UMacGenerator;

import java.io.PrintWriter;
import java.security.SecureRandomSpi;
import java.util.HashMap;
import java.util.Random;

/**
 * <p>An <em>Adapter</em> class around {@link UMacGenerator} to allow using this
 * algorithm as a JCE {@link java.security.SecureRandom}.</p>
 *
 * @version $Revision: 1.2 $
 */
public class UMacRandomSpi extends SecureRandomSpi {

   // Debugging methods and variables
   // -------------------------------------------------------------------------

   private static final String NAME = "UMacRandomSpi";
   private static final boolean DEBUG = true;
   private static final PrintWriter err = new PrintWriter(System.out, true);
   private static void debug(String s) {
      err.println(">>> "+NAME+": "+s);
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Class-wide prng to generate random material for the underlying prng.*/
   private static final UMacGenerator prng; // blank final
   static {
      prng = new UMacGenerator();
      resetLocalPRNG();
   }

   // error messages
   private static final String MSG =
         "Exception while setting up a "+Registry.UMAC_PRNG+" SPI: ";
   private static final String RETRY = "Retry...";

   /** Our underlying prng instance. */
   private UMacGenerator adaptee = new UMacGenerator();

   // Constructor(s)
   // -------------------------------------------------------------------------

   // default 0-arguments constructor

   // Class methods
   // -------------------------------------------------------------------------

   private static void resetLocalPRNG() {
      HashMap attributes = new HashMap();
      attributes.put(UMacGenerator.CIPHER, Registry.AES_CIPHER);
      byte[] key = new byte[128 / 8]; // AES default key size
      Random rand = new Random(System.currentTimeMillis());
      rand.nextBytes(key);
      attributes.put(IBlockCipher.KEY_MATERIAL, key);
      int index = rand.nextInt() & 0xFF;
      attributes.put(UMacGenerator.INDEX, new Integer(index));

      prng.setup(attributes);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   // java.security.SecureRandomSpi interface implementation ------------------

   public byte[] engineGenerateSeed(int numBytes) {
      if (numBytes < 1) {
         return new byte[0];
      }
      byte[] result = new byte[numBytes];
      this.engineNextBytes(result);
      return result;
   }

   public void engineNextBytes(byte[] bytes) {
      if (!adaptee.isInitialised()) {
         this.engineSetSeed(new byte[0]);
      }

      while (true) {
         try {
            adaptee.nextBytes(bytes, 0, bytes.length);
            break;
         } catch (LimitReachedException x) { // reseed the generator
            resetLocalPRNG();
         }
      }
   }

   public void engineSetSeed(byte[] seed) {
      // compute the total number of random bytes required to setup adaptee
      int materialLength = 0;
      materialLength += 16; // key material size
      materialLength++; // index size
      byte[] material = new byte[materialLength];

      // use as much as possible bytes from the seed
      int materialOffset = 0;
      int materialLeft = material.length;
      if (seed.length > 0) { // copy some bytes into key and update indices
         int lenToCopy = Math.min(materialLength, seed.length);
         System.arraycopy(seed, 0, material, 0, lenToCopy);
         materialOffset += lenToCopy;
         materialLeft -= lenToCopy;
      }
      if (materialOffset > 0) { // generate the rest
         while (true) {
            try {
               prng.nextBytes(material, materialOffset, materialLeft);
               break;
            } catch (IllegalStateException x) { // should not happen
               throw new InternalError(MSG + String.valueOf(x));
            } catch (LimitReachedException x) {
               if (DEBUG) {
                  debug(MSG + String.valueOf(x));
                  debug(RETRY);
               }
            }
         }
      }

      // setup the underlying adaptee instance
      HashMap attributes = new HashMap();

      // use AES cipher with 128-bit block size
      attributes.put(UMacGenerator.CIPHER, Registry.AES_CIPHER);
      // specify the key
      byte[] key = new byte[16];
      System.arraycopy(material, 0, key, 0, 16);
      attributes.put(IBlockCipher.KEY_MATERIAL, key);
      // use a 1-byte index
      attributes.put(UMacGenerator.INDEX, new Integer(material[16] & 0xFF));

      adaptee.init(attributes);
   }
}
