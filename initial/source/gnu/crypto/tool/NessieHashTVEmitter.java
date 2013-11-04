package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: NessieHashTVEmitter.java,v 1.1.1.1 2001-11-20 13:40:44 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU Library General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
// details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; see the file COPYING. If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
// ----------------------------------------------------------------------------

import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.util.Util;

/**
 * A utility class to generate NESSIE test vectors for a designated hash
 * algorithm.<p>
 *
 * <b>NOTE</b>: The <i>test3</i> test vector will be generated iff the global
 * system environment variable named "TORTURE" is set. It is skipped otherwise.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class NessieHashTVEmitter {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor to enforce Singleton pattern. */
   private NessieHashTVEmitter() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static final void main(String[] args) {
      try {
         IMessageDigest md = HashFactory.getInstance(args[0]);

         long time = -System.currentTimeMillis();

         test1(md);
         test2(md);
         test3(md);

         time += System.currentTimeMillis();
         System.out.println();
         System.out.println("End of test vectors");
         System.out.println();
         System.out.println("*** Tests lasted "+time+" ms...");

      } catch (Exception x) {
         x.printStackTrace(System.err);
      }
   }

   private static void test1(IMessageDigest md) {
      byte[] data = new byte[128];
      byte[] result;

      System.out.println("Message digests of strings of 0-bits and length L:");
      for (int i = 0; i < 128; i++) {
         System.out.print("    L = ");
         String s = "    "+String.valueOf(8*i);
         System.out.print(s.substring(s.length()-4));
         System.out.print(": ");
         md.update(data, 0, i);
         result = md.digest();
         System.out.println(Util.toString(result));
      }
   }

   private static void test2(IMessageDigest md) {
      int bl = md.hashSize();
      byte[] data = new byte[bl];
      byte[] result;

      System.out.println("Message digests of all "+(String.valueOf(8*bl))
         +"-bit strings S containing a single 1-bit:");
      for (int i = 0; i < bl; i++)
         for (int j = 0; j < 8; j++) {
            data[i] = (byte)(1 << (7-j));
            System.out.print("    S = "+Util.toString(data)+": ");
            md.update(data, 0, bl);
            result = md.digest();
            System.out.println(Util.toString(result));
            data[i] = 0x00;
         }
   }

   private static void test3(IMessageDigest md) {
      // this is torture for low-speed CPUs. only execute if global env var
      // TORTURE is set!
      String dummy = System.getProperty("TORTURE");
      if (dummy == null) {
         return;
      }

      int bl = md.hashSize();
      byte[] data = new byte[bl];
      int limit = 100000000;

      System.out.print("Iterated message digest computation ("
         +String.valueOf(limit)+" times): ");
      for (int i = 0; i < limit; i++) {
         md.update(data, 0, bl);
         data = md.digest();
      }

      System.out.println(Util.toString(data));
   }
}
