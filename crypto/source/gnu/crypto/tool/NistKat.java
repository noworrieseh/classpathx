package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: NistKat.java,v 1.2 2001-12-04 12:56:08 raif Exp $
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
 * For a designated symmetric block cipher algorithm, this command generates
 * and exercises Known Answer Tests data for both Variable Key and Variable
 * Text suites.<p>
 *
 * NistKat's output file format is in conformance with the layout described in
 * Section 3 of NIST's document "Description of Known Answer Tests and Monte
 * Carlo Tests for Advanced Encryption Standard (AES) Candidate Algorithm
 * Submissions" dated January 7, 1998.
 *
 * @version $Revision: 1.2 $
 */
public final class NistKat
{
   // Constants and variables
   // -------------------------------------------------------------------------

   private String cipherName;
   private int keySize; // in bits
   private IBlockCipher cipher;
   private long encBlocks; // total count of encrypted blocks
   private long decBlocks; // total count of decrypted blocks
   private long keyCount;  // total count of key creation requests

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor to enforce usage through main(). */
   private NistKat(String cipherName, IBlockCipher cipher, int keySize) {
      super();

      this.cipherName = cipherName;
      this.cipher = cipher;
      this.keySize = keySize;
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      try {
         String cipherName = args[0];
         IBlockCipher cipher = CipherFactory.getInstance(cipherName);

         int keySize = cipher.defaultKeySize() * 8;
         if (args.length > 1) {
            keySize = Integer.parseInt(args[1]);
         }

         long time = -System.currentTimeMillis();
         NistKat cmd = new NistKat(cipherName, cipher, keySize);

         cmd.variableKeyKat();
         cmd.variableTextKat();

         time += System.currentTimeMillis();
         System.out.println();
         System.out.println("Total execution time (ms): "+String.valueOf(time));
         System.out.println("During this time, "+String.valueOf(cipherName)+ ":");
         System.out.println("  Encrypted "+String.valueOf(cmd.encBlocks)+" blocks");
         System.out.println("  Decrypted "+String.valueOf(cmd.decBlocks)+" blocks");
         System.out.println("  Created "+String.valueOf(cmd.keyCount)+" session keys");

      } catch (Exception x) {
         x.printStackTrace(System.err);
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------

   private void variableKeyKat() throws InvalidKeyException {
      int count = keySize / 8; // number of bytes in key material
      int size = cipher.defaultBlockSize();
      byte[] k = new byte[count];
      byte[] pt = new byte[size];  // plaintext
      byte[] ct = new byte[size];  // ciphertext
      byte[] cpt = new byte[size]; // computed plaintext
      int round = 0;               // current round ord. number

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Electronic Codebook (ECB) Mode");
      System.out.println("Variable Key Known Answer Tests");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();
      System.out.println("PT="+Util.toString(pt));
      System.out.println();

      // The key bytes are organised and numbered as follows:
      //
      // |<- byte 0 ->|<- byte 1 ->|<- ... ->|<- byte n ->|
      // |<------------- bit_(n-1) to bit_0 ------------->|
      //
      HashMap map = new HashMap();
      for (int i = 0; i < count; i++) {
         for (int j = 0; j < 8; j++) {
            round++;
            System.out.println("I="+String.valueOf(round));

            k[i] = (byte)(1 << (7 - j));
            System.out.println("KEY="+Util.toString(k));
            map.put(IBlockCipher.KEY_MATERIAL, k);
            cipher.init(map);
            keyCount++;

            cipher.encryptBlock(pt, 0, ct, 0);
            encBlocks++;
            System.out.print("CT="+Util.toString(ct));

            cipher.decryptBlock(ct, 0, cpt, 0);
            decBlocks++;
            cipher.reset();

            if (!Util.areEqual(pt, cpt))  // check if results match
               System.out.print(" *** ERROR ***");

            System.out.println();
            System.out.println();
         }

         k[i] = 0x00;
      }

      System.out.println("==========");
   }

   private void variableTextKat() throws InvalidKeyException {
      byte[] k = new byte[keySize / 8];
      int count = cipher.defaultBlockSize(); // the cipher's block size
      byte[] pt = new byte[count];  // plaintext
      byte[] ct = new byte[count];  // ciphertext
      byte[] cpt = new byte[count]; // computed plaintext
      int round = 0;                // current round ord. number
      HashMap map = new HashMap();

      map.put(IBlockCipher.KEY_MATERIAL, k);
      cipher.init(map);
      keyCount++;

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Electronic Codebook (ECB) Mode");
      System.out.println("Variable Text Known Answer Tests");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();
      System.out.println("KEY="+Util.toString(k));
      System.out.println();

      // The plaintext bytes are organised and numbered as follows:
      //
      // |<- byte 0 ->|<- byte 1 ->|<- ... ->|<- byte n ->|
      // |<------------- bit_(n-1) to bit_0 ------------->|
      //
      for (int i = 0; i < count; i++) {
         for (int j = 0; j < 8; j++) {
            round++;
            System.out.println("I="+String.valueOf(round));

            pt[i] = (byte)(1 << (7 - j));
            System.out.println("PT="+Util.toString(pt));

            cipher.encryptBlock(pt, 0, ct, 0);
            encBlocks++;
            System.out.print("CT="+Util.toString(ct));

            cipher.decryptBlock(ct, 0, cpt, 0);
            decBlocks++;

            if (!Util.areEqual(pt, cpt))
               System.out.print(" *** ERROR ***");

            System.out.println();
            System.out.println();
         }

         pt[i] = 0x00;
      }

      cipher.reset();
      System.out.println("==========");
   }
}
