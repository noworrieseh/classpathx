package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: CipherSpeed.java,v 1.4 2001-12-15 02:08:19 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
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

import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A tool to exercise a block cipher in order to measure its performance in
 * terms of encrypted/decrypted bytes per second.
 *
 * @version $Revision: 1.4 $
 */
public final class CipherSpeed {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor to enforce Singleton pattern. */
   private CipherSpeed() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Accepts 0, 1 or 2 arguments. If no arguments are provided, this method
    * exercises every block cipher implementation using the cipher's default
    * key size. If one argument is provided, it's assumed to be the name of a
    * block cipher to exercise. That cipher is then exercised using its default
    * key size. Finally if two arguments are provided, the first is assumed to
    * be the block cipher name and the second its key size.
    */
   public static void main(String[] args) {
      if (args == null) {
         args = new String[0];
      }

      switch (args.length) {
      case 0: // exercise all ciphers
         for (Iterator cit = CipherFactory.getNames().iterator(); cit.hasNext(); ) {
            speed((String) cit.next());
         }
         break;
      case 1:
         speed(args[0]);
         break;
      default:
         speed(args[0], Integer.parseInt(args[1]) / 8);
         break;
      }
   }

   private static void speed(String name) {
      System.out.println("Exercising "+String.valueOf(name)+"...");
      try {
         IBlockCipher cipher = CipherFactory.getInstance(name);
         speed(cipher, cipher.defaultKeySize());
      } catch (InternalError x) {
         System.out.println("Failed self-test...");
      }
   }

   private static void speed(String name, int keysize) {
      System.out.println("Exercising "+String.valueOf(name)+"-"
         +String.valueOf(keysize)+"...");
      try {
         IBlockCipher cipher = CipherFactory.getInstance(name);
         speed(cipher, keysize);
      } catch (InternalError x) {
         System.out.println("Failed self-test...");
      }
   }

   private static void speed(IBlockCipher cipher, int keysize) {
      try {
         int iterations = 1000000;
         int blocksize = cipher.defaultBlockSize();
         int i;
         byte[] kb = new byte[keysize];
         for (i = 0; i < keysize; i++) {
            kb[i] = (byte) i;
         }

         byte[] pt = new byte[blocksize];
         for (i = 0; i < blocksize; i++) {
            pt[i] = (byte) i;
         }

         System.out.println("Running "+iterations+" iterations:");
         System.out.print("Encryption: ");

         HashMap map = new HashMap();
         map.put(IBlockCipher.KEY_MATERIAL, kb);
         cipher.init(map);

         byte[] ct = (byte[]) pt.clone();
         long elapsed = -System.currentTimeMillis();
         for (i = 0; i < iterations; i++) {
            cipher.encryptBlock(ct, 0, ct, 0);
         }

         elapsed += System.currentTimeMillis();
         float secs = (elapsed > 1) ? (float) elapsed / 1000 : 1;
         float speed = (float) iterations * blocksize / 1024 / secs;

         System.out.println("time = "+secs+", speed = "+speed+" KB/s");
         System.out.print("Decryption: ");

         byte[] cpt = (byte[]) ct.clone();
         elapsed = -System.currentTimeMillis();
         for (i = 0; i < iterations; i++) {
            cipher.decryptBlock(cpt, 0, cpt, 0);
         }

         elapsed += System.currentTimeMillis();
         secs = (elapsed > 1) ? (float) elapsed / 1000 : 1;
         speed = (float) iterations * blocksize / 1024 / secs;

         System.out.println("time = "+secs+", speed = "+speed+" KB/s");

         if (!Util.areEqual(pt, cpt)) {
            throw new RuntimeException("Symmetric operation failed");
         }
      } catch (Exception x) {
         x.printStackTrace(System.err);
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
