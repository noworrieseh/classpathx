package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: NessieCipherTVEmitter.java,v 1.2 2001-12-04 12:56:08 raif Exp $
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
import java.security.InvalidKeyException;
import java.util.HashMap;

/**
 * A utility class to generate NESSIE test vectors for a designated cipher
 * algorithm and optionally a key size (in bits).<p>
 *
 * <b>NOTE</b>: The <i>set4</i> test vectors set will be generated iff the
 * global system environment variable named "TORTURE" is set. It is skipped
 * otherwise.
 *
 * @version $Revision: 1.2 $
 */
public class NessieCipherTVEmitter {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor to enforce Singleton pattern. */
   private NessieCipherTVEmitter() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static final void main(String[] args) {
      try {
         IBlockCipher cipher = CipherFactory.getInstance(args[0]);

         int keySize = cipher.defaultKeySize() * 8;
         if (args.length > 1) {
            keySize = Integer.parseInt(args[1]);
         }

         long time = -System.currentTimeMillis();

         set1(cipher, keySize);
         set2(cipher, keySize);
         set3(cipher, keySize);
         set4(cipher, keySize);

         time += System.currentTimeMillis();
         System.out.println();
         System.out.println("End of test vectors");
         System.out.println();
         System.out.println("*** Tests lasted "+time+" ms...");

      } catch (Exception x) {
         x.printStackTrace(System.err);
      }
   }

   private static void set1(IBlockCipher cipher, int keySize)
   throws InvalidKeyException {
      String s;
      int kl = keySize / 8;
      int bl = cipher.defaultBlockSize();
      byte[] k = new byte[kl];
      byte[] p = new byte[bl];
      byte[] c = new byte[bl];
      byte[] d = new byte[bl];
      int b; // byte index
      int bit; // bit index within byte
      int j;

      System.out.println("Test vectors -- set 1");
      System.out.println("=====================");
      System.out.println();
      HashMap map = new HashMap();
      for (int i = 0; i < keySize; i++) {
         s = "   "+String.valueOf(i);
         s = s.substring(s.length()-3);
         System.out.println("Set 1, vector#"+s+":");
         b = i / 8;
         bit = i % 8;
         k[b] = (byte)(0x80 >>> bit);
         System.out.println("                      key="+Util.toString(k));
         map.put(IBlockCipher.KEY_MATERIAL, k);
         cipher.init(map);
         System.out.println("                    plain="+Util.toString(p));
         cipher.encryptBlock(p, 0, c, 0);
         System.out.println("                   cipher="+Util.toString(c));
         cipher.decryptBlock(c, 0, d, 0);
         System.out.println("                decrypted="+Util.toString(d));
         if (!Util.areEqual(p, d)) {
            throw new RuntimeException("Symmetric operation failure...");
         }
         for (j = 1; j < 100; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("       Iterated 100 times="+Util.toString(c));
         for (j = 100; j < 1000; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("      Iterated 1000 times="+Util.toString(c));
         System.out.println();
         k[b] = 0x00;
         cipher.reset();
      }
   }

   private static void set2(IBlockCipher cipher, int keySize)
   throws InvalidKeyException {
      String s;
      int kl = keySize / 8;
      int bl = cipher.defaultBlockSize();
      byte[] k = new byte[kl];
      byte[] p = new byte[bl];
      byte[] c = new byte[bl];
      byte[] d = new byte[bl];
      HashMap map = new HashMap();
      map.put(IBlockCipher.KEY_MATERIAL, k);
      cipher.init(map);
      int b; // byte index
      int bit; // bit index within byte
      int j;
      System.out.println("Test vectors -- set 2");
      System.out.println("=====================");
      System.out.println();
      for (int i = 0; i < 8*bl; i++) {
         s = "   "+String.valueOf(i);
         s = s.substring(s.length()-3);
         System.out.println("Set 2, vector#"+s+":");
         System.out.println("                      key="+Util.toString(k));
         b = i / 8;
         bit = i % 8;
         p[b] = (byte)(0x80 >>> bit);
         System.out.println("                    plain="+Util.toString(p));
         cipher.encryptBlock(p, 0, c, 0);
         System.out.println("                   cipher="+Util.toString(c));
         cipher.decryptBlock(c, 0, d, 0);
         System.out.println("                decrypted="+Util.toString(d));
         if (!Util.areEqual(p, d)) {
            throw new RuntimeException("Symmetric operation failure...");
         }
         for (j = 1; j < 100; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("       Iterated 100 times="+Util.toString(c));
         for (j = 100; j < 1000; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("      Iterated 1000 times="+Util.toString(c));
         System.out.println();
         p[b] = 0x00;
      }
   }

   private static void set3(IBlockCipher cipher, int keySize)
   throws InvalidKeyException {
      String s;
      int kl = keySize / 8;
      int bl = cipher.defaultBlockSize();
      byte[] k = new byte[kl];
      byte[] p = new byte[bl];
      byte[] c = new byte[bl];
      byte[] d = new byte[bl];
      int j;
      System.out.println("Test vectors -- set 3");
      System.out.println("=====================");
      System.out.println();
      cipher.reset();
      HashMap map = new HashMap();
      for (int i = 0; i < 256; i++) {
         s = "   "+String.valueOf(i);
         s = s.substring(s.length()-3);
         System.out.println("Set 3, vector#"+s+":");
         for (j = 0; j < bl; j++) {
            k[j] = (byte) i;
            p[j] = (byte) i;
         }
         for ( ; j < kl; j++) {
            k[j] = (byte) i;
         }
         map.put(IBlockCipher.KEY_MATERIAL, k);
         cipher.init(map);
         System.out.println("                      key="+Util.toString(k));
         System.out.println("                    plain="+Util.toString(p));
         cipher.encryptBlock(p, 0, c, 0);
         System.out.println("                   cipher="+Util.toString(c));
         cipher.decryptBlock(c, 0, d, 0);
         System.out.println("                decrypted="+Util.toString(d));
         if (!Util.areEqual(p, d)) {
            throw new RuntimeException("Symmetric operation failure...");
         }
         for (j = 1; j < 100; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("       Iterated 100 times="+Util.toString(c));
         for (j = 100; j < 1000; j++) {
            cipher.encryptBlock(c, 0, c, 0);
         }
         System.out.println("      Iterated 1000 times="+Util.toString(c));
         System.out.println();
         cipher.reset();
      }
   }

   private static void set4(IBlockCipher cipher, int keySize)
   throws InvalidKeyException {
      // this is torture for low-speed CPUs. only execute if global env var
      // TORTURE is set!
      String dummy = System.getProperty("TORTURE");
      if (dummy == null) {
         return;
      }

      int kl = keySize / 8;
      int bl = cipher.defaultBlockSize();
      byte[] k = new byte[kl];
      byte[] p = new byte[bl];
      int j, t, x;
      System.out.println("Test vectors -- set 4");
      System.out.println("=====================");
      System.out.println();
      HashMap map = new HashMap();
      for (int i = 0; i < 4; i++) {
         System.out.println("Set 4, vector#  "+String.valueOf(i)+":");
         for (j = 0; j < bl; j++) {
            k[j] = (byte) i;
            p[j] = (byte) i;
         }
         for ( ; j < kl; j++) {
            k[j] = (byte) i;
         }
         System.out.println("                      key="+Util.toString(k));
         System.out.println("                    plain="+Util.toString(p));
         x = i;
         for (j = 0; j < 100000000; j++) {
            for (t = 0; t < kl; t++) {
               k[t] = (byte) x;
            }
            map.put(IBlockCipher.KEY_MATERIAL, k);
            cipher.init(map);
            cipher.encryptBlock(p, 0, p, 0);
            cipher.reset();
            x = p[bl-1] & 0xFF;
         }
         System.out.println("      Iterated 10^8 times="+Util.toString(p));
         System.out.println();
      }
   }
}
