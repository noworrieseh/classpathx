package gnu.crypto.tool;

// ----------------------------------------------------------------------------
// $Id: NistMCT.java,v 1.1.1.1 2001-11-20 13:40:44 raif Exp $
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

import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;
import java.security.InvalidKeyException;
import java.util.HashMap;

/**
 * For a designated symmetric block cipher algorithm, this command generates and
 * exercises Monte Carlo Tests data for both Encryption and Decryption in
 * Electronic Codebook (ECB) and Cipher Block Chaining (CBC) modes.<p>
 *
 * NistMCT's output file format is in conformance with the layout described in
 * Section 4 of NIST's document "Description of Known Answer Tests and Monte
 * Carlo Tests for Advanced Encryption Standard (AES) Candidate Algorithm
 * Submissions" dated January 7, 1998.
 *
 * @version $Revision: 1.1.1.1 $
 */
public class NistMCT
{
   // Constants and variables
   // -------------------------------------------------------------------------

   private String cipherName;
   private int keySize; // in bits
   private IBlockCipher cipher;

   // statistics fields
   private long encBlocks; // total count of encrypted blocks
   private long decBlocks; // total count of decrypted blocks
   private long keyCount; // total count of key creation requests

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor to enforce usage through main(). */
   private NistMCT(String cipherName, IBlockCipher cipher, int keySize) {
      super();

      this.cipherName = cipherName;
      this.cipher = cipher;
      this.keySize = keySize;
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main (String[] args) {
      try {
         String cipherName = args[0];
         IBlockCipher cipher = CipherFactory.getInstance(cipherName);

         int keySize = cipher.defaultKeySize() * 8;
         if (args.length > 1) {
            keySize = Integer.parseInt(args[1]);
         }

         long time = -System.currentTimeMillis();
         NistMCT cmd = new NistMCT(cipherName, cipher, keySize);

         cmd.ecb();
         cmd.cbc();

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

   private void ecb() throws InvalidKeyException {
      ecbEncrypt();
      ecbDecrypt();
   }

   private void ecbEncrypt() throws InvalidKeyException {
      int keylen = keySize / 8;      // number of bytes in user key
      byte[] keyMaterial = new byte[keylen];
      int size = cipher.defaultBlockSize(); // cipher block size in bytes
      byte[] pt = new byte[size];    // plaintext
      byte[] cpt = new byte[size];   // computed plaintext
      byte[] ct = new byte[size];    // ciphertext @round 9999
      byte[] ct_1 = new byte[size];  // ciphertext @round 9998
      int j, k, count;               // temp vars

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Electronic Codebook (ECB) Mode - ENCRYPTION");
      System.out.println("Monte Carlo Test");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();

      // step 1. will use all zeroes.

      HashMap map = new HashMap();
      for (int i = 0; i < 400; i++) { // step 2
         // Encryption
         System.out.println("I="+String.valueOf(i)); // step 2.a
         System.out.println("KEY="+Util.toString(keyMaterial));
         System.out.println("PT="+Util.toString(pt));

         map.put(IBlockCipher.KEY_MATERIAL, keyMaterial);
         cipher.init(map);

         keyCount++;
         cipher.encryptBlock(pt, 0, ct_1, 0); // step 2.b
         encBlocks++;
         for (j = 1; j < 9999; j++) {
            cipher.encryptBlock(ct_1, 0, ct_1, 0);
            encBlocks++;
         }

         cipher.encryptBlock(ct_1, 0, ct, 0);
         encBlocks++;
         System.out.println("CT="+Util.toString(ct)); // step 2.c

         // Decryption
         cipher.decryptBlock(ct, 0, cpt, 0); // step 2.b
         decBlocks++;
         for (j = 1; j < 10000; j++) {
            cipher.decryptBlock(cpt, 0, cpt, 0);
            decBlocks++;
         }

         if (!Util.areEqual(pt, cpt)) { // check if results match
            System.out.println(" *** ERROR ***");
            throw new RuntimeException("ECB Encryption/Decryption mismatch");
         }

         System.out.println();
         cipher.reset();

         // may throw ArrayIndexOutOfBoundsException with non-AES ciphers; ie.
         // those for which: keylen < size || keylen > 2*size
         j = 0; // step 2.d
         if (keylen > size) {
            count = keylen - size;
            k = size - count;
            while (j < count) {
               keyMaterial[j++] ^= ct_1[k++];
            }
         }

         k = 0;
         while (j < keylen) {
            keyMaterial[j++] ^= ct[k++];
         }

         System.arraycopy(ct, 0, pt, 0, size); // step 2.e
      }

      System.out.println("==========");
   }

   void ecbDecrypt() throws InvalidKeyException {
      int keylen = keySize / 8;      // number of bytes in user key
      byte[] keyMaterial = new byte[keylen];
      int size = cipher.defaultBlockSize(); // cipher block size in bytes
      byte[] pt = new byte[size];    // plaintext
      byte[] cpt = new byte[size];   // computed plaintext
      byte[] ct = new byte[size];    // ciphertext @round 9999
      byte[] ct_1 = new byte[size];  // ciphertext @round 9998
      int j, k, count;                // temp vars

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Electronic Codebook (ECB) Mode - DECRYPTION");
      System.out.println("Monte Carlo Test");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();

      // step 1. will use all zeroes.

      HashMap map = new HashMap();
      for (int i = 0; i < 400; i++) { // step 2
         map.put(IBlockCipher.KEY_MATERIAL, keyMaterial);
         cipher.init(map);
         keyCount++;

         // Encryption
         cipher.encryptBlock(pt, 0, ct_1, 0); // step 2.b
         encBlocks++;
         for (j = 1; j < 9999; j++) {
            cipher.encryptBlock(ct_1, 0, ct_1, 0);
            encBlocks++;
         }

         cipher.encryptBlock(ct_1, 0, ct, 0);
         encBlocks++;

         // Decryption
         System.out.println("I="+String.valueOf(i)); // step 2.a
         System.out.println("KEY="+Util.toString(keyMaterial));
         System.out.println("CT="+Util.toString(ct));

         cipher.decryptBlock(ct, 0, cpt, 0); // step 2.b
         decBlocks++;
         for (j = 1; j < 10000; j++) {
            cipher.decryptBlock(cpt, 0, cpt, 0);
            decBlocks++;
         }

         System.out.println("PT="+Util.toString(cpt)); // step 2.c

         if (!Util.areEqual(pt, cpt)) { // check if results match
            System.out.println(" *** ERROR ***");
            throw new RuntimeException("ECB Encryption/Decryption mismatch");
         }

         System.out.println();
         cipher.reset();

         // may throw ArrayIndexOutOfBoundsException with non-AES ciphers; ie.
         // those for which: keylen < size || keylen > 2*size
         j = 0; // step 2.d
         if (keylen > size) {
            count = keylen - size;
            k = size - count;
            while (j < count) {
               keyMaterial[j++] ^= ct_1[k++];
            }
         }

         k = 0;
         while (j < keylen) {
            keyMaterial[j++] ^= ct[k++];
         }

         System.arraycopy(ct, 0, pt, 0, size); // step 2.e (both)
      }

      System.out.println("==========");
   }

   void cbc() throws InvalidKeyException {
      cbcEncrypt();
      cbcDecrypt();
   }

   void cbcEncrypt() throws InvalidKeyException {
      int keylen = keySize / 8;      // number of bytes in user key material
      byte[] keyMaterial = new byte[keylen];
      int size = cipher.defaultBlockSize(); // cipher block size in bytes
      byte[] pt = new byte[size];    // plaintext
      byte[] ct = new byte[size];    // ciphertext
      byte[] iv = new byte[size];    // initialization vector
      int j, k, count;               // temp vars

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Cipher Block Chaining (CBC) Mode - ENCRYPTION");
      System.out.println("Monte Carlo Test");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();

      HashMap map = new HashMap();
      for (int i = 0; i < 400; i++) { // step 2
         // step 2.a is implicit since we're handling cv as iv
         System.out.println("I="+String.valueOf(i)); // step 2.b
         System.out.println("KEY="+Util.toString(keyMaterial));
         System.out.println("IV="+Util.toString(iv));
         System.out.println("PT="+Util.toString(pt));

         map.put(IBlockCipher.KEY_MATERIAL, keyMaterial);
         cipher.init(map);
         keyCount++;
         for (j = 0; j < 10000; j++) { // step 2.c
            for (k = 0; k < size; k++) {
               iv[k] ^= pt[k]; // step 2.c.i
            }

            System.arraycopy(ct, 0, pt, 0, size); // copy ct@(j-1) into pt
            cipher.encryptBlock(iv, 0, ct, 0); // step 2.c.ii
            encBlocks++;
            System.arraycopy(ct, 0, iv, 0, size); // set new iv/cv
         }

         System.out.println("CT="+Util.toString(ct)); // step 2.d
         System.out.println();
         cipher.reset();

         // may throw ArrayIndexOutOfBoundsException with non-AES ciphers; ie.
         // those for which: keylen < size || keylen > 2*size
         // remember: we keep ct@(j-1) values in pt...
         j = 0; // step 2.e
         if (keylen > size) {
            count = keylen - size;
            k = size - count;
            while (j < count) {
               keyMaterial[j++] ^= pt[k++];
            }
         }

         k = 0;
         while (j < keylen) {
            keyMaterial[j++] ^= ct[k++];
         }
      }

      System.out.println("==========");
   }

   void cbcDecrypt() throws InvalidKeyException {
      int keylen = keySize / 8;      // number of bytes in user key material
      byte[] keyMaterial = new byte[keylen];
      int size = cipher.defaultBlockSize(); // cipher block size in bytes
      byte[] pt = new byte[size];    // plaintext
      byte[] ct = new byte[size];    // ciphertext
      byte[] iv = new byte[size];    // initialization vector
      int j, k, count;               // temp vars

      System.out.println();
      System.out.println("=========================");
      System.out.println();
      System.out.println("Cipher Block Chaining (CBC) Mode - DECRYPTION");
      System.out.println("Monte Carlo Test");
      System.out.println();
      System.out.println("Algorithm Name: "+String.valueOf(cipherName));
      System.out.println();
      System.out.println("==========");
      System.out.println();
      System.out.println("KEYSIZE="+String.valueOf(keySize));
      System.out.println();

      // step 1. use all-zeroes values

      HashMap map = new HashMap();
      for (int i = 0; i < 400; i++) { // step 2
         // step 2.a is implicit since we're handling cv as iv
         System.out.println("I="+String.valueOf(i)); // step 2.b
         System.out.println("KEY="+Util.toString(keyMaterial));
         System.out.println("IV="+Util.toString(iv));
         System.out.println("CT="+Util.toString(ct));

         map.put(IBlockCipher.KEY_MATERIAL, keyMaterial);
         cipher.init(map);
         keyCount++;
         for (j = 0; j < 10000; j++) { // step 2.c
            cipher.decryptBlock(ct, 0, pt, 0); // steps 2.c.i + 2.c.ii
            decBlocks++;
            for (k = 0; k < size; k++)
               pt[k] ^= iv[k];    // step 2.c.iii

            System.arraycopy(ct, 0, iv, 0, size); // step 2.c.iv
            System.arraycopy(pt, 0, ct, 0, size);
         }

         System.out.println("PT="+Util.toString(pt)); // step 2.d
         System.out.println();
         cipher.reset();

         // may throw ArrayIndexOutOfBoundsException with non-AES ciphers; ie.
         // those for which: keylen < size || keylen > 2*size
         // remember: iv contains values of pt@(j-1)
         j = 0; // step 2.e
         if (keylen > size) {
            count = keylen - size;
            k = size - count;
            while (j < count) {
               keyMaterial[j++] ^= iv[k++];
            }
         }

         k = 0;
         while (j < keylen) {
            keyMaterial[j++] ^= pt[k++];
         }
      }

      System.out.println("==========");
   }
}
