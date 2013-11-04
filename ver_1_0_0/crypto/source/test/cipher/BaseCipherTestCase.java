package test.cipher;

// ----------------------------------------------------------------------------
// $Id: BaseCipherTestCase.java,v 1.1 2002-06-29 01:25:03 raif Exp $
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

import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;

import java.util.HashMap;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * <p>A generic cipher test case that can verify a cipher implementation given
 * a set of known answers. To implement a test of a particular cipher, subclass
 * this and in the subclass do the following:</p>
 *
 * <ol>
 *    <li>Fill in the static answer arrays (in the {@link #setUp()} method).
 *    These answers should be the first <i>n</i> test answers in each of the
 *    tests (KAT or MCT).</li>
 *    <li>Implement the {@link #setUp()} method, and instantiate the cipher to
 *    be tested.</li>
 *    <li>Implement a static method <code>suite</code> that returns a
 *    {@link junit.framework.TestSuite} with <code>TestOfXxxx.class</code> as
 *    the sole argument to the constructor.</li>
 *    <li>Override any of the test* methods below, if they do not implement an
 *    appropriate test.</li>
 *    <li>Implement any new test* methods you need.</li>
 * </ol>
 *
 * <p>The tests, as implemented in this class, are the NIST Known-Answer Tests
 * (KAT) and Monte-Carlo Tests (MCT), which were the test formats used in the
 * AES Quest. As such, these tests are suited for AES-candidates (or similar)
 * ciphers; the specific AES style parts of these tests are the 128, 192, and
 * 256 bit key lengths.</p>
 *
 * <p>References:</p>
 * <ol>
 *    <li><a href="http://csrc.nist.gov/encryption/aes/katmct/katmct.htm">Known
 *    Answer Tests and Monte Carlo Tests for AES Submissions</a> for an
 *    explanation of the tests and the format of the resulting files.</li>
 * </ol>.
 *
 * @version $Revision: 1.1 $
 */
public abstract class BaseCipherTestCase extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The reference to the cipher implementation to exercise. */
   protected IBlockCipher cipher;

   // Constructor(s)
   // -------------------------------------------------------------------------

   protected BaseCipherTestCase(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Perform a variable-key KAT, comparing the results with the supplied
    * answers.</p>
    *
    * @param answers The expected ciphertexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> If all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   katVK(String[] answers, IBlockCipher cipher, int ks) throws Exception {
      HashMap attrib = new HashMap();
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] kb = new byte[ks];
      kb[0] = (byte) 0x80;
      attrib.put(cipher.KEY_MATERIAL, kb);

      for (int i = 0; i < answers.length; i++) {
         cipher.reset();
         cipher.init(attrib);
         cipher.encryptBlock(pt, 0, ct, 0);
         if (!answers[i].equals(Util.toString(ct))) {
            return false;
         }
         shiftRight1(kb);
      }
      return true;
   }

   /**
    * <p>Perform a variable-text known-answer test, comparing the results with
    * the supplied answers.</p>
    *
    * @param answers The expected ciphertexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> If all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   katVT(String[] answers, IBlockCipher cipher, int ks) throws Exception {
      HashMap attrib = new HashMap();
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] kb = new byte[ks];
      pt[0] = (byte) 0x80;
      attrib.put(cipher.KEY_MATERIAL, kb);

      cipher.reset();
      cipher.init(attrib);
      for (int i = 0; i < answers.length; i++) {
         cipher.encryptBlock(pt, 0, ct, 0);
         if (!answers[i].equals(Util.toString(ct))) {
            return false;
         }
         shiftRight1(pt);
      }
      return true;
   }

   /**
    * <p>Perform a Monte-Carlo encryption Test, using the ECB mode. The
    * <code>answers</code> array should be the resulting ciphertexts after each
    * iteration.</p>
    *
    * @param answers The expected ciphertexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> if all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   mctEncryptECB(String[] answers, IBlockCipher cipher, int ks)
   throws Exception {
      HashMap attrib = new HashMap();
      byte[] kb = new byte[ks];
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] lct = new byte[cipher.currentBlockSize()];
      int i, j;
      int off = ks - cipher.currentBlockSize();
      attrib.put(cipher.KEY_MATERIAL, kb);

      for (i = 0; i < answers.length; i++) {
         cipher.reset();
         cipher.init(attrib);
         for (j = 0; j < 10000; j++) {
            if (j == 9999) {
               System.arraycopy(ct, 0, lct, 0, ct.length);
            }
            cipher.encryptBlock(pt, 0, ct, 0);
            System.arraycopy(ct, 0, pt, 0, ct.length);
         }
         if (!answers[i].equals(Util.toString(ct))) {
            return false;
         }
         for (j = 0; j + (lct.length-off) < lct.length && j < off; j++) {
            kb[j] ^= lct[j+(lct.length-off)];
         }
         for (j = 0; j + off < kb.length && j < ct.length; j++) {
            kb[j+off] ^= ct[j];
         }
      }
      return true;
   }

   /**
    * <p>Perform a Monte-Carlo decryption Test, using the ECB mode. The
    * <code>answers</code> array should be the resulting plaintexts after each
    * iteration.</p>
    *
    * @param answers The expected plaintexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> if all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   mctDecryptECB(String[] answers, IBlockCipher cipher, int ks)
   throws Exception {
      HashMap attrib = new HashMap();
      byte[] kb = new byte[ks];
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] lpt = new byte[cipher.currentBlockSize()];
      int i, j;
      int off = ks - cipher.currentBlockSize();
      attrib.put(cipher.KEY_MATERIAL, kb);

      for (i = 0; i < answers.length; i++) {
         cipher.reset();
         cipher.init(attrib);
         for (j = 0; j < 10000; j++) {
            if (j == 9999) {
               System.arraycopy(pt, 0, lpt, 0, ct.length);
            }
            cipher.decryptBlock(ct, 0, pt, 0);
            System.arraycopy(pt, 0, ct, 0, ct.length);
         }
         if (!answers[i].equals(Util.toString(pt))) {
            return false;
         }
         for (j = 0; j + (lpt.length-off) < lpt.length && j < off; j++) {
            kb[j] ^= lpt[j+(lpt.length-off)];
         }
         for (j = 0; j + off < kb.length && j < pt.length; j++) {
            kb[j+off] ^= pt[j];
         }
      }
      return true;
   }

   /**
    * <p>Perform a Monte-Carlo encryption Test, using the CBC mode. The
    * <code>answers</code> array should be the resulting ciphertexts after each
    * iteration.</p>
    *
    * @param answers The expected ciphertexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> if all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   mctEncryptCBC(String[] answers, IBlockCipher cipher, int ks)
   throws Exception {
      HashMap attrib = new HashMap();
      byte[] kb = new byte[ks];
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] lct = new byte[cipher.currentBlockSize()];
      byte[] iv = new byte[cipher.currentBlockSize()];
      int i, j, k;
      int off = ks - cipher.currentBlockSize();
      attrib.put(cipher.KEY_MATERIAL, kb);

      for (i = 0; i < answers.length; i++) {
         cipher.reset();
         cipher.init(attrib);
         for (j = 0; j < 10000; j++) {
            for (k = 0; k < pt.length; k++) {
               pt[k] ^= iv[k];
            }
            System.arraycopy(ct, 0, lct, 0, ct.length);
            cipher.encryptBlock(pt, 0, ct, 0);
            System.arraycopy(ct, 0, iv, 0, ct.length);
            System.arraycopy(lct, 0, pt, 0, lct.length);
         }
         if (!answers[i].equals(Util.toString(ct))) {
            return false;
         }
         for (j = 0; j + (lct.length-off) < lct.length && j < off; j++) {
            kb[j] ^= lct[j+(lct.length-off)];
         }
         for (j = 0; j + off < kb.length && j < ct.length; j++) {
            kb[j+off] ^= ct[j];
         }
      }
      return true;
   }

   /**
    * <p>Perform a Monte-Carlo decryption Test, using the CBC mode. The
    * <code>answers</code> array should be the resulting plaintexts after each
    * iteration.</p>
    *
    * @param answers The expected plaintexts.
    * @param cipher The cipher.
    * @param ks The length of the key, in bytes.
    * @return <code>true</code> if all tests succeed, <code>false</code>
    * otherwise.
    */
   protected static boolean
   mctDecryptCBC(String[] answers, IBlockCipher cipher, int ks)
   throws Exception {
      HashMap attrib = new HashMap();
      byte[] kb = new byte[ks];
      byte[] pt = new byte[cipher.currentBlockSize()];
      byte[] ct = new byte[cipher.currentBlockSize()];
      byte[] lpt = new byte[cipher.currentBlockSize()];
      byte[] iv = new byte[cipher.currentBlockSize()];
      int i, j, k;
      int off = ks - cipher.currentBlockSize();
      attrib.put(cipher.KEY_MATERIAL, kb);

      for (i = 0; i < answers.length; i++) {
         cipher.reset();
         cipher.init(attrib);
         for (j = 0; j < 10000; j++) {
            if (j == 9999) {
               System.arraycopy(pt, 0, lpt, 0, pt.length);
            }
            cipher.decryptBlock(ct, 0, pt, 0);
            for (k = 0; k < pt.length; k++) {
               pt[k] ^= iv[k];
            }
            System.arraycopy(ct, 0, iv, 0, ct.length);
            System.arraycopy(pt, 0, ct, 0, pt.length);
         }
         if (!answers[i].equals(Util.toString(pt))) {
            return false;
         }
         for (j = 0; j + (lpt.length-off) < lpt.length && j < off; j++) {
            kb[j] ^= lpt[j+(lpt.length-off)];
         }
         for (j = 0; j + off < kb.length && j < pt.length; j++) {
            kb[j+off] ^= pt[j];
         }
      }
      return true;
   }

   /**
    * <p>Shift, in situ, the variable key/text byte array one position to the
    * right.</p>
    *
    * @param kb The bytes to shift.
    */
   private static void shiftRight1(byte[] kb) {
      int i;
      for (i = 0; kb[i] == 0 && i < kb.length; i++) { // do nothing
      }
      kb[i] = (byte)((kb[i] & 0xff) >>> 1);
      // handle byte boundary case
      if (kb[i] == 0) {
         i++;
         if (i < kb.length) {
            kb[i] = (byte) 0x80;
         }
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /** Test symmetry. */
   protected boolean validityTest() {
      return cipher.selfTest();
   }

   /** Test cloneability. */
   protected boolean cloneabilityTest() throws Exception {
      int blockSize = cipher.defaultBlockSize();
      int keySize = cipher.defaultKeySize();

      byte[] pt = new byte[blockSize];
      byte[] ct1 = new byte[blockSize];
      byte[] ct2 = new byte[blockSize];
      byte[] kb = new byte[keySize];
      HashMap attributes = new HashMap();
      attributes.put(cipher.KEY_MATERIAL, kb);

      cipher.reset();
      cipher.init(attributes);

      cipher.encryptBlock(pt, 0, pt, 0);
      IBlockCipher thomas = (IBlockCipher) cipher.clone();
      thomas.init(attributes);
      cipher.encryptBlock(pt, 0, ct1, 0);
      thomas.encryptBlock(pt, 0, ct2, 0);

      return Util.areEqual(ct1, ct2);
   }

   // Abstract methods to be implemented by concrete subclasses.
   // -------------------------------------------------------------------------

   /** Set up the cipher. */
   public abstract void setUp() throws Exception;
}
