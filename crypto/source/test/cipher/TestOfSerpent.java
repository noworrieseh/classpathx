package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfSerpent.java,v 1.1 2002-06-09 00:09:09 raif Exp $
//
// Copyright (C) 2001-2002, Free Software Foundation, Inc.
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import gnu.crypto.cipher.Serpent;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.util.Util;

/**
 * <p>Full conformance tests for the {@link gnu.crypto.cipher.Serpent}
 * implementation.</p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfSerpent extends TestCase {

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfSerpent(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfSerpent.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testSelfTest() {
      try {
//         IBlockCipher algorithm = new Serpent();
         IBlockCipher algorithm = serpent;
         assertTrue("selfTest()", algorithm.selfTest());
      } catch (Exception x) {
         fail("selfTest(): "+String.valueOf(x));
      }
   }

   // Variable-key tests.
   public void testKATestVK() {
//      Serpent serpent = new Serpent();
      byte[] kb = new byte[16];
      byte[] pt = new byte[16], ct = new byte[16];
      kb[0] = (byte) 0x80;
      int i = 0;
      try {
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVK(): ",
               Util.areEqual(ct, hexToBytes(vk_128[i])));
            i++;
         } while (shift(kb) && i < vk_128.length);
         i = 0;
         kb = new byte[24];
         kb[0] = (byte) 0x80;
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVK(): ",
               Util.areEqual(ct, hexToBytes(vk_192[i])));
            i++;
         } while (shift(kb) && i < vk_192.length);
         i = 0;
         kb = new byte[32];
         kb[0] = (byte) 0x80;
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVK(): ",
               Util.areEqual(ct, hexToBytes(vk_256[i])));
            i++;
         } while (shift(kb) && i < vk_256.length);
      } catch (Exception x) {
         fail("KATestVK(): " + String.valueOf(x));
      }
   }

   public void testKATestVT() {
//      Serpent serpent = new Serpent();
      byte[] kb = new byte[16];
      byte[] pt = new byte[16], ct = new byte[16];
      pt[0] = (byte) 0x80;
      int i = 0;
      try {
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVT(): ",
               Util.areEqual(ct, hexToBytes(vt_128[i])));
            i++;
         } while (shift(pt) && i < vt_128.length);
         kb = new byte[24]; // all zeros
         pt[0] = (byte) 0x80;
         i = 0;
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVT(): ",
               Util.areEqual(ct, hexToBytes(vt_192[i])));
            i++;
         } while (shift(pt) && i < vt_192.length);
         kb = new byte[32]; // all zeros
         pt[0] = (byte) 0x80;
         i = 0;
         do {
            Object key = serpent.makeKey(kb, 16);
            serpent.encrypt(pt, 0, ct, 0, key, 16);
            assertTrue("KATestVT(): ",
               Util.areEqual(ct, hexToBytes(vt_256[i])));
            i++;
         } while (shift(pt) && i < vt_256.length);
      } catch (Exception e) {
         fail("KATestVT(): " + String.valueOf(e));
      }
   }

   public void testMCTestECBEncrypt128() {
      byte[] kb = new byte[16];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16];
      try {
         for (int i = 0; i < mct_ecb_e_128.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, pt, 0, 16);
            }
            assertTrue("MCTestECBEncrypt128(): ",
               Util.areEqual(ct, hexToBytes(mct_ecb_e_128[i])));
            for (int j = 0; j < kb.length; j++) {
               kb[j] ^= ct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBEncrypt128(): "+String.valueOf(e));
      }
   }

   public void testMCTestECBEncrypt192() {
      byte[] kb = new byte[24];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lct = new byte[8];
      try {
         for (int i = 0; i < mct_ecb_e_192.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(ct, 0, lct, 0, 8);
               }
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, pt, 0, 16);
            }
            assertTrue("MCTestECBEncrypt192(): ",
               Util.areEqual(ct, hexToBytes(mct_ecb_e_192[i])));
            for (int j = 0; j < ct.length; j++) {
               kb[j] ^= ct[j];
            }
            for (int j = 0; j < lct.length; j++) {
               kb[j+16] ^= lct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBEncrypt192(): "+String.valueOf(e));
      }
   }

   public void testMCTestECBEncrypt256() {
      byte[] kb = new byte[32];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lct = new byte[16];
      try {
         for (int i = 0; i < mct_ecb_e_256.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(ct, 0, lct, 0, 16);
               }
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, pt, 0, 16);
            }
            assertTrue("MCTestECBEncrypt256(): ",
               Util.areEqual(ct, hexToBytes(mct_ecb_e_256[i])));
            for (int j = 0; j < ct.length; j++) {
               kb[j] ^= ct[j];
            }
            for (int j = 0; j < lct.length; j++) {
               kb[j+16] ^= lct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBEncrypt256(): "+String.valueOf(e));
      }
   }

   public void testMCTestECBDecrypt128() {
      byte[] kb = new byte[16];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16];
      try {
         for (int i = 0; i < mct_ecb_d_128.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestECBDecrypt128(): ",
               Util.areEqual(pt, hexToBytes(mct_ecb_d_128[i])));
            for (int j = 0; j < kb.length; j++) {
               kb[j] ^= pt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBDecrypt128(): "+String.valueOf(e));
      }
   }

   public void testMCTestECBDecrypt192() {
      byte[] kb = new byte[24];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lpt = new byte[8];
      try {
         for (int i = 0; i < mct_ecb_d_192.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(pt, 0, lpt, 0, 8);
               }
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestECBDecrypt192(): ",
               Util.areEqual(pt, hexToBytes(mct_ecb_d_192[i])));
            for (int j = 0; j < pt.length; j++) {
               kb[j] ^= pt[j];
            }
            for (int j = 0; j < lpt.length; j++) {
               kb[j+16] ^= lpt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBDecrypt192(): "+String.valueOf(e));
      }
   }

   public void testMCTestECBDecrypt256() {
      byte[] kb = new byte[32];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lpt = new byte[16];
      try {
         for (int i = 0; i < mct_ecb_d_256.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(pt, 0, lpt, 0, 16);
               }
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestECBDecrypt256(): ",
               Util.areEqual(pt, hexToBytes(mct_ecb_d_256[i])));
            for (int j = 0; j < pt.length; j++) {
               kb[j] ^= pt[j];
            }
            for (int j = 0; j < lpt.length; j++) {
               kb[j+16] ^= lpt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestECBDecrypt256(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCEncrypt128() {
      byte[] kb = new byte[16];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lct = new byte[16],
         iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_e_128.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, lct, 0, 16);
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(lct, 0, pt, 0, 16);
            }
            assertTrue("MCTestCBCEncrypt128(): ",
               Util.areEqual(ct, hexToBytes(mct_cbc_e_128[i])));
            for (int j = 0; j < kb.length; j++) {
               kb[j] ^= ct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCEncrypt128(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCEncrypt192() {
      byte[] kb = new byte[24];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lct = new byte[16],
         iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_e_192.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, lct, 0, 16);
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(lct, 0, pt, 0, 16);
            }
            assertTrue("MCTestCBCEncrypt192(): ",
               Util.areEqual(ct, hexToBytes(mct_cbc_e_192[i])));
            for (int j = 0; j < ct.length; j++) {
               kb[j] ^= ct[j];
            }
            for (int j = 0; j+16 < kb.length; j++) {
               kb[j+16] ^= lct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCEncrypt192(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCEncrypt256() {
      byte[] kb = new byte[32];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lct = new byte[16],
         iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_e_256.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, lct, 0, 16);
               serpent.encrypt(pt, 0, ct, 0, key, 16);
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(lct, 0, pt, 0, 16);
            }
            assertTrue("MCTestCBCEncrypt256(): ",
               Util.areEqual(ct, hexToBytes(mct_cbc_e_256[i])));
            for (int j = 0; j < ct.length; j++) {
               kb[j] ^= ct[j];
            }
            for (int j = 0; j+16 < kb.length; j++) {
               kb[j+16] ^= lct[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCEncrypt256(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCDecrypt128() {
      byte[] kb = new byte[16];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_d_128.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestCBCDecrypt128(): ",
               Util.areEqual(pt, hexToBytes(mct_cbc_d_128[i])));
            for (int j = 0; j < kb.length; j++) {
               kb[j] ^= pt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCDecrypt128(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCDecrypt192() {
      byte[] kb = new byte[24];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lpt = new byte[8],
         iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_d_192.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(pt, 0, lpt, 0, 8);
               }
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestCBCDecrypt192(): ",
               Util.areEqual(pt, hexToBytes(mct_cbc_d_192[i])));
            for (int j = 0; j < pt.length; j++) {
               kb[j] ^= pt[j];
            }
            for (int j = 0; j+16 < kb.length; j++) {
               kb[j+16] ^= lpt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCDecrypt192(): "+String.valueOf(e));
      }
   }

   public void testMCTestCBCDecrypt256() {
      byte[] kb = new byte[32];
//      Serpent serpent = new Serpent();
      byte[] pt = new byte[16], ct = new byte[16], lpt = new byte[16],
         iv = new byte[16];
      try {
         for (int i = 0; i < mct_cbc_d_256.length; i++) {
            Object key = serpent.makeKey(kb, 16);
            for (int j = 0; j < 10000; j++) {
               if (j == 9999) {
                  System.arraycopy(pt, 0, lpt, 0, 16);
               }
               serpent.decrypt(ct, 0, pt, 0, key, 16);
               for (int k = 0; k < pt.length; k++) {
                  pt[k] ^= iv[k];
               }
               System.arraycopy(ct, 0, iv, 0, 16);
               System.arraycopy(pt, 0, ct, 0, 16);
            }
            assertTrue("MCTestCBCDecrypt256(): ",
               Util.areEqual(pt, hexToBytes(mct_cbc_d_256[i])));
            for (int j = 0; j < pt.length; j++) {
               kb[j] ^= pt[j];
            }
            for (int j = 0; j+16 < kb.length; j++) {
               kb[j+16] ^= lpt[j];
            }
         }
      } catch (Exception e) {
         fail("MCTestCBCDecrypt256(): "+String.valueOf(e));
      }
   }

   // over-ridden methods from junit.framework.TestCase -----------------------

   protected void setUp() throws Exception {
   }

   // own methods -------------------------------------------------------------

   private boolean shift(byte[] kb) {
      int i;
      for (i = 0; kb[i] == 0 && i < kb.length; i++);
      kb[i] = (byte) ((kb[i] & 0xff) >>> 1);
      if (kb[i] == 0) {
         i++;
         if (i < kb.length) {
            kb[i] = (byte) 0x80;
         } else {
            return false;
         }
      }
      return true;
   }

   private byte[] hexToBytes(String s) {
      byte[] b = new byte[s.length()/2];
      for (int i = 0; i < b.length; i++) {
         b[i] = (byte) Integer.parseInt(s.substring(i*2, i*2+2), 16);
      }
      return b;
   }

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The local reference to a serpent. */
   private Serpent serpent = new Serpent();

   /** Variable-key known-answer test for 128-bit keys. */
   private static final String[] vk_128 = {
      "49afbfad9d5a34052cd8ffa5986bd2dd", "0c1e2e4e79bd02c2501096e79b5e73fa",
      "d769e71b31f4ae12e14cdc48238d2d7b", "bd0276d6be072cc00b3d426130bf355f",
      "c191e2121daea18ec30957bbce199a8c", "afdffc707b4c6890fa9eec5dcf8c9cd6",
      "7ed064ab9270ede51862d2a4775eb639", "e108b719d47da77d82b2811f09c768f6",
      "46d78ac926c7fd72aeb6e662d6be0671", "b86ddb30f8bacec337679bb112a33a04",
      "d55f0d5779c68cb6db174f85006f5ef9", "9cce4fe5fc2c3f58007049ff81ae3fd2",
      "e8db9396961180f9515acedbc47d5cef", "abcc122ccc4b2b00a40e266c54f0d234",
      "137ef0814dc6171858a93557e97b78b3", "1aeddc72581c90d87f6f01e431dc31eb",
      "9543bc267111fbdb019c00d0d907d643", "2269c1f5dbeb2ced1932a60dcf273b46",
      "bb49509c9b79d012cb04ab565534df90", "4d0f02eb268acfaec89115f7f761c805",
      "9288439abb3beb346b7fafadb897dd11", "dc348afd3ee54b134f8f9f5045e87171",
      "52d8aa93f95a76980a42960a407b4e86", "ea977f02d0f2b330dc33aa53055af7b8",
      "8dd4bdf3874ed531036747e0b1b93f57", "b8dba7c86499bff611935cede230cf03",
      "32cce0726492e1afbc3a001f655c73ed", "807570b6677b0fbdf7e7cabcd5dd0c97",
      "26381f839c8d92efe1c29df688571c6d", "da6e8ca3ec924a0a6c7b704b56c46783",
      "cc6e7070e5ac70cd9664fda7bb505df2", "8c41cd11c3dc2333dc96ddf74d85430c",
      "a006685e857f4c2a70fcea63ed27888f", "f6e94cd2ef8eb4886893458d49f84fdd",
      "1f654042740ca63ad906c1b7bcf18bde", "340fbc9913fc30b3a867921b6acb45fc",
      "b2b0488b1c9dfa4d66fcaeeb4c29ec9e", "a35d40c8cfe4b26d0047e1661d5bfbca",
      "97f91ad53e32fd33198ccb9c553cf4b9", "7adeec246ef1a8b16caa7aeb85441dd1",
      "12e12c07623a87b76100474804b4f7a4", "d24a5c39b10157c7f957f51bd35adf80",
      "54593378469cea645783a4ab0847ce46", "3ac3fce2fbdd176ff7f659ee19bcba72",
      "82c4c432a703271e6f4bda3f174063e7", "58d9b1ac0a6dad03ede51d30ac77c579",
      "8a8f0c4f316232a05eb611358fd7a8a2", "afae1db5edb3f7203577b37ce8e4876c",
      "07112de84d0759a816f620dc786e648d", "c3f4922db4541025e4c162acaae48de9",
      "47836c92ed4fa66f1c6459478f45fcb0", "d25965deadcbeb3c9b8ee599fa81f361",
      "7d86a4cd06353c4a73022b2e75af7e2c", "bf1bdfdb65e06f4d19c8b11a4e280f60",
      "6845080536e71bd50c9db865bac484db", "37004f392802c0ade017bc4a1ac4754c",
      "501f9de66d9b8048c9a2657bee750323", "edffad56dbd99444dbf01f5d347fef94",
      "afe80d1a72308a41a10bc463507a27ce", "c848af4b9d8eef03e085ea6331e14fbf",
      "5c0547e4ca27c23506eb54dbf1f9f9c8", "9eea58726c084358c80beace0ba9316f",
      "c3f8221b5e849c830bbc30697cfc4e33", "7d10842f40364fa264c876e5f2f9921e",
      "e25769f083838d9ba05dbabd0adfb8a5", "08cb3d4622c7974eff08da8a346f4b34",
      "3ff6bcf81372f04eb8a1e082042ab5aa", "0198c38d19e4579133178aa8e4dd77e4",
      "dffdd86a4c6b22b399f4ccbedf1c4033", "b10b0e2bde47b1129c75b5838627062c",
      "ec608bdd3f215e9f73b57183dc7cb6b2", "ee7b79d43681851ba43510b25fedac83",
      "1906141e04ad2a584cef3a93dc13621f", "7590c1bb23c24da9cd0f10040ee1b732",
      "c581396fbdfecf75f55ce431879cfa72", "93e88288dc748f6fa096bd2f5b35e9ae",
      "b1f29150a0c4c5fdb8d20d2dc52661b5", "e5ee3a9b5f38d0dd5db7be33f1b6f56f",
      "db980feddfe7c14e3ff9b3e4bcc54165", "1d085c7a534503011e3f248a0dd69ebf",
      "578aa13f4284049cf77ddb639e9d6777", "7f725360786ca59b82bf2673b0918054",
      "fb21f98e591af7f7d6ee6ee04e70faa8", "dc73336c2d3056fb56c11a09ca147f62",
      "f51c58c6fe6a0ebc9c9109eb97ab2cbc", "081d42e412311642222b52cff05d7fcc",
      "e50c69c214a093004fe6f107bfc34a08", "c5b57a240a095310ba40b7747459c6d3",
      "b1110d1bc986bfa5acdbf1231b913574", "23f5505f1badb259826d8ab7e59ce579",
      "e5ddaabeaf6cae289174d9839165f372", "c44e60a86495404c6ea369d6a26edd77",
      "273d4e769fceb754be0ce32a7eecd427", "239d65850b45fafe8bd0982492c4680d",
      "5db91003c20f59ea498576f2efce4f6c", "3bc4e9986a9576474f947ee49b9046fc",
      "2aa67c0d7f134c3ee48b5a945a94aa50", "f6f6f7dd1920f20aeb08844c8c811175",
      "0d3313cbc0c84fc2747900009ba06bd4", "7385cbac63b11c5adaf763c9c652e317",
      "80a74048515d7162287b0f4d991ced6c", "7d3abfdd15cfd8c0a989fc7a151e35ad",
      "0336740082e8c5a85d32dcba80b0af79", "fdaa7495db62ec0c22cae25fa2fe26d3",
      "da10538ff08a81323b8e0bdab580c9a3", "3aa421d16db303cd32c58261d71f6512",
      "90f78d5b05e1ca8e0b34220760a4916d", "03a35d1b2758a91907430778c9491f2b",
      "b729c8708dc7cd28d21eaa12dcda299e", "4b30d94af1cb3be76fb98ef878f68f5b",
      "5b3eef9c55455e7347e533bb6c5f8b15", "8e01b7823aacf4f23552ac40c03afa8e",
      "f9d23961d0661eebde0f824e47ceba97", "385ac4a1512edbecae3527ec49b84c11",
      "4561dab55aa5b7625a6961fed1ab2614", "40778dfdd63cbe86812440519715c680",
      "93c6789d563e3df7d74ea3523333bdcf", "7f47b8dbe11e385f07b6efb20a35045f",
      "66ea39db24ed91686ead33208de35ccf", "4c42473dcc65b14088257f8744af633e",
      "3ddabfc006daab06462af4ef81544e26", "24850e35c86eac07349927c73b1b234a",
      "568db914df56817d3c85fde9f96932e0", "8fc4ac4d7589bad559ac81301c1898a7",
      "15dafb79236ac6f5f361f80d5255b334", "4ab0a08142246b0c5175116b8fbb865e",
      "a143e86d9bd26437f1c5a9c7903121b9", "c8466045b204026fffd194320355ca4e"
   };

   /** Variable-key known-answer test for 192-bit keys. */
   private static final String[] vk_192 = {
      "e78e5402c7195568ac3678f7a3f60c66", "23645a0ddd4b0f8b73b6215ea938a59e",
      "95d262643c94cab3e5830fc90a3ad119", "2a66ae878814c427cd1be1b929d69d1b",
      "c1f74b3c5df4b485118b6901a22bef14", "abe310b271d2f83f15ddd70139a8f18a",
      "8072229836095e09a76de47ff4b90942", "463b70d45332c3d5afc57cac1785055d",
      "5587b5bcb9ee5a28ba2bacc418005240", "735ab1e66239c7882ab3b52623e812fb",
      "2c899a7e69368ddfd435b90fb54a5110", "058a2258fec39fd98caee246e83fba23",
      "29de401b293b69509f1f26172b99e768", "33fda1c0a7f752f26f7eef1de21af616",
      "90144147c0838e88c081c08c806d3879", "598957d7605d027c870c3968de1c4281",
      "252e46bf0615ac356a4350f223d1e48b", "cc2447d57f2a53f825c167bffc7e00fd",
      "7100c3404f1394015041d22fe81d66d6", "9df94fccb015d30e3b86aeca1c393ab0",
      "4844e0e701a2a85dbac818f1ab2c14b5", "4ce4e7e3104510c0ec02486e618b072b",
      "88f251a4fd7939dab8e3547140958ace", "d752effc32d3d7ba1217a3a6759d5def",
      "6c03785cc372b967a9ae4e7e153064ce", "f356816a32dabc65bf3361957e131771",
      "b1700202c5b1c1fa94d756db5b160d7d", "d2dd321ee0d420bde198cda1c84de12a",
      "0e53163d96ceaec6073786bfe36b26ae", "607e94a93e5ed0e8d4657697af12263a",
      "4fe24da11e3b2e528689b4d72a1f4995", "c89dc4741fba46d09f0c909e415d1e8a",
      "927998642db52db45aa726f55a9807e4", "8a9da14de34dfcd06af101411ead6e6c",
      "58d05e0e1c2217640ba965d33642e784", "6b329fe46eb5523e2360a28956d4c07d",
      "80765c8e114a3f19ac86e68ac5e69ee6", "9e111a3bc422a8d3c8372a7b214dcf7f",
      "43a392e7c0a7441e5fd4b8f53b04376d", "321c8906902c819fee83ac1ca3de46ed",
      "961146f0e1d1618b4aa3dddfc407bcda", "35ff30c0f11225b0fbdeee829e1033e7",
      "c14f87b211633d3f3b7b297fe1f53b9f", "75e05358f1d0798f359c29a6ed9327cf",
      "ce93218acbc0c73f0b2be8082ed9fc4c", "54a73a50cda285a429016cc5086d9f40",
      "73de0beacbf4cd85e4e067226e94804d", "8851067bedfa997f5feb63a07676e0ea",
      "3438f325f239c49b2a21b5140aeb83a7", "e93a4212daaaaa91d2628bbffa824252",
      "660bef72e98bc11e63944336550e6d30", "39ebf5fc10b7bb85fe45473508bad887",
      "c97a923736e49d536ba33bf4502f4d89", "352acb145387dfa72b05b0dd79199c47",
      "73682433c31b7492335615ef9f39e5e0", "2f3166a3e2ea671397d852958b3cf986",
      "84f405f8270c1c58c0fe19a564df189f", "8e768cbf45b91029f767db75843ebd53",
      "b1927686ab4297fb6aa8eface91b6c45", "2192b94b157c15177f4bb4dcbd6eee89",
      "2b596f7f119a2f59179f522c7ea1ec51", "56c5e66ea1d98ea8dd9906d9d73610a5",
      "6ad3b33d2ede5b781d136fcea356c807", "5585ab166028e001379c6d2cbd3169a0",
      "167da93e33b3c95fe3f7a8526668073a", "dfb3528fca5f163952a30b34108300fd",
      "1503116c28b3b82ff9f0db169d857efc", "7b4dac54ba36d4ab88ef3356ebcbc086",
      "dcea72fe37b7eee1837be0eaa4ce66ca", "c9da8b98ef3157d221c06d6b95549701",
      "e83cabd11d2f8271a117c7c65cc66e78", "c462f0880d5ae2411dc6f1a68873abde",
      "fe0089c7e6c657d1b71cf467d4f58df0", "49932b6abd5601fda5c8403e9cb92502",
      "ec725bcf1a0133a45aeb7a5a72898d6b", "6ece8cb7f45ba9d63e152399f6558b6a",
      "9aa8f493830fd6ede37e7803f9869b19", "70856a78e1bb29b9d00108361683e0bf",
      "3bec70b8070fc514b3de12bfcd6e7b88", "4896551f1308c48fbdd89b8313de2fc8",
      "21270b0aa9831c887320210856618204", "9d94843edde4c6a89bea613af35afdcd",
      "a44742fb1f8c552797b0fe90a7f0efa9", "731be62d606ecf6ee9a0f0c96a26f6ea",
      "f1471235da8b77caf22aa978414bf459", "f67b2cdbbb11ee10d180c2070e73c387",
      "36f6bafee414e8f22f815cc204f46b5f", "e0d276ef75ac7dc2b15bee758cc7b892",
      "7d541fc08e0ff9b773cc84c974749f1e", "7998830324cf92a793e295412bd3aa9c",
      "d3a3a62bc99f201eded9ea8c1ebe84b3", "fc131ea2cfd76056fe0cb6f6322e514e",
      "92aec5acdbc80e93bb494565d6152683", "2897ad37ac521cfbafee2d3cc49d5427",
      "0a02072414040730d415d26f4c4f4e80", "6f7fb8316285e5f39b2ddaedb8ec2fc5",
      "77dbaaf18692980aa85d126a9b4a4726", "5d0ae9ba98b4eab278222e08da5c9515",
      "df65cdef8e09029d1eba45894d1e1904", "9911f98f1f260c8f60e4e0c1801b84c1",
      "974fb48c48cd2c4a3b19bb191ce3f37f", "43a52a7da3ebc9554fccacaabf02b42f",
      "0389dc8774b21120ece15cc270912897", "4dd7be782da1dbd775ba298bc90dc63b",
      "6ea07ca0ddfcac58cfa211b4bde9c321", "ba8829b1de058c4b48615d851fc74f17",
      "22a6e18a3555867731c492d0c8717633", "b68e5336ce0a364dbcbf4cae81e371d6",
      "384427b8f008e2b31a904c532beb1599", "84709b1ffbaba5397a9f133a0767b8cb",
      "7723e3a896d01b6b1f5340df029f7b48", "68e0fe27173a8a85f7445414574d1d87",
      "9b4788cbf0b56848101b3deb7a3a4656", "852593f9947f166bf58eae7c9a00001a",
      "29be7cc59519fe143ef6319682de0bba", "3949b20d461f75274e87df21de5d62a0",
      "db487555a81a86c1b726b917390b853a", "06b7908886620d9c9a0f3aedf235fb33",
      "aa300793219c42fc41987f9bbf19b58e", "a49d34f3c92c7cdb71a48d17a4723697",
      "093c1029c5eb09844c39dcb42a6ac5eb", "6a41993b586a37321a851455d80a3000",
      "13574f75f33be868fa293a751411cb47", "2b97b42b03879363bfa1587040246455",
      "77b91d8da3f6eaa1713b543994c673c5", "db664148b36b317331adc103bc680d57",
      "375a6f5c49202d3a59bf429a9a77be2a", "dc7a7fb1247b60aead86ce0d898b647d",
      "345eb88a1d9bd2c5ddee04ac3923f08a", "2dba9b16738d1adc7d859b0ecd7d1cd1",
      "4f48ebfcb17522ed2239191e520a967a", "b8c410bd14ad99cefd7428a3bc6f7e35",
      "07f3715387341ce7b8325e824842c01a", "6812212678dc0a46f0b86b24b15b8a03",
      "2e767244fc74c817f0789bb8943292ef", "01a32dd3f99dff43e110db0f2b88a819",
      "5e81d0e1effcd9961bcd343696009839", "129025e1728372901320b2fcff6d5b9b",
      "b3b3f7264dab71c2ca0febe44431b30d", "b9953f99bac2841911ab1c9caa7c98d7",
      "dbaca3686bdfbe3d8c5e2e6b1db5577f", "1be312be1113ebac050bfcf37b0de85d",
      "f2a282d526d5432c476a112ef6162cbe", "d4ad15036534cfc7091be72117ed9a0d",
      "142ed387794e196f1b148ecba2e5062b", "761ea776ecf0bcf73d52013da09328fe",
      "f3513e388fd5c6111b2c11edbe568001", "00f9e9822b79ee8c60edca082a9dd220",
      "e8e0bf758a9e88242b31617e828647fb", "dead0d2f9480117270c95f860c9ffc81",
      "56604fa1c8d7b258131f61beefa89b17", "0fc45f51d2c185e667da0517f2a60955",
      "192751977bff5834d51a9e81f448f19e", "26dd12ba453b5d54146c02d91f2e998b",
      "9b5c07141c98e441003fecf9c9311a2a", "e9494c80b90f1753a3689ab4ea258d66",
      "8fb4ca5e0a62e4b5adeb7be7731085b0", "5e29ae31cbbdc216e6d4adf274d47d0d",
      "cb173fb1cec206bef29a15b8ac72e919", "9f35799a7d5dab723493eb92af1c18d3",
      "f495f5658d888139913df0d3cf048baa", "e92daa5d108115d6f242f544e6ea72c9",
      "c0ea8a7652bfc6cf81a5c46da38d2840", "a221dece41a3a28cfc9d58e74cbd6014",
      "369dec7982b167b3f0f98a5e393d5e41", "b14d25d008a846a0768e394b751b8452",
      "ac516cd4467bda2ad6da18bf202e8ce2", "a82c0be53acb86f86692c1ead49acfea",
      "05548475ba322605dfc3ad55707a0539", "173639d0c531a19ec3cb8f4891b23706",
      "79febf394be1b8afb954c791a5eaa653", "8e009de1f46674d9bac9d36f4fd29daf",
      "9212a730377098bdb34645b8db9f6c6f", "c586abec28a5e252ed99934117d9dc00",
      "416c7812ce8049a70965f9cba27d07dc", "7819c9eff656380d997f50779f75ddaa",
      "f9a6b55b40c1c6f9ce21e07cefd14839", "3c70be049ad5f70660cdfd4738c88a5b",
      "774ebef1e5916cd2bcfd70a3b647637f", "4087268a3bea1b020f4f0ff6cec88d3d",
      "005b7c9abf6dd5cbd3fad77bc09fbac9", "0d45f3c58653e1c4f0328e8b760d668b",
      "9eb8ce9db6a787f32abd31365f67159a", "9ac89d752faa132d66d5fe92e4783989",
      "89266048a5fcfc1eb27b739bad4e279e", "82aeb3e765f31b046ae49903518efc92",
      "707df5c6fd03a2de93d46ac486a30d5e", "beb9bf4ecbf6875d1e72cf2478e3c1be",
      "144226cc1aeffe130c8dbb91f8797a25", "b7b8c1114487a380ffb64ca05bdd3e8b",
      "2c5eb11071ee2d0b2351e16aa93ec873", "0f61ac094954e286bcc90288fc62bd0a"
   };

   /** Variable-key known-answer test for 256-bit keys. */
   private static final String[] vk_256 = {
      "abed96e766bf28cbc0ebd21a82ef0819", "959658cdfcd80356dde045bbe7b1888d",
      "04d49cc0fae714b46b5b177664df4c28", "39f1b1a339ded740d80b663a057d4866",
      "df15b01e30cf9c81688f989809579a86", "9db98fdedb783247d6af34f7579d89f6",
      "fc41583a46c0f673c82bd63be56c72a6", "34ed2423c59cc9c04ab5c6c931fd5898",
      "8432a8062a2aaef13171209ec5dded82", "c632e81cbb67020f6cfb9062f7a9171c",
      "c706ee8ed28ea73f32b2efc95c41d66d", "4b1a9b43530384e0b095e924a401fd48",
      "89404af86981af4ddaa800424c9c132b", "170594ac5052d7c192f581e1000a7e0d",
      "1e3970ea40cb25bcede878f627d35092", "10c2b5f2de944c7b1cb5fc21945d714d",
      "2f3e64eaf25b990300e0ce75ec5ddd19", "4f6c152d9df44abce9b18a765a0175bb",
      "2e861155411717b16c60fb9eb0e1ef46", "ce0449630e05e22eaeec4499f4020d36",
      "862de372c7497defe2b639359707e293", "6119f054d0f45711d06bed281d48712e",
      "74978fc86fb0e92c2601a58681eb06ac", "35031cbf461d8df3b4c946c9dcc54ca9",
      "dd2fb08b5b4bbcd07bd70453da57a417", "7e841efe450c947ece8ea3456c6bdfb6",
      "bf2e9008e158a8ed760a304d465edb83", "78d223b7577b97c57d41f7a9aa67fbc7",
      "982171d6e83e59b519741c29d21fd21d", "e8f80fe26ab8942b8c884bdfef37e3a2",
      "9daabac1e9699a951db64db0ea0ff761", "eae7ed73f898a19411b8d08f71c5c66f",
      "7e20e6a88a263f8adad3a8813524dbdf", "6cd1bfcb283a3531c830082153865a96",
      "182b87a0094e36ab50cc26c12f2d44e3", "af7c418e585d5ffc2966ae7f4ebc61b2",
      "dd136237150e9bd819d19f84ac180294", "5bd17b54d6878b398d8cedb08ce67dc0",
      "fef8c88ea06c5a0aa3efb1dce54541f1", "a8c35caf2d91a05408b171b960cd084d",
      "4bdf0294712fcd25975fbc084f6cc973", "e5f0bb96b46ac2c2413022e9d7f38a48",
      "dd6732cd14961e4e42c3ce0ea83850cd", "590e7b58c1963e45c5bb60a576378dc3",
      "69f5d841ad99f218fcb89521d1165484", "c236007a7a244d2364075279bbd3b307",
      "7ce8d07c90029b2253133a4b941668bd", "af3ebebfb4e37c7d22226101d27c54cc",
      "532e1616fa8ee2853aed696920e1f23c", "e332c5955575f799b05b02976fb41d80",
      "791da10535733bf5d4b9706d5af92243", "cd5eb5936eb10eb78432478eed00a2d1",
      "074280068f0cf9b8632959989d87b1aa", "a37c95a3c73592d03de7b645536221e2",
      "b969cca153bb861999da020e3255b002", "c9b0ce3c773d5e16062c7b0017b1a1fc",
      "fd38f1a2db8d38559c7ccad15d0e3e85", "fb6c3f215a5c11976576bde1d5c59169",
      "92350faab64bba4a84cc16683c7a0d4e", "ecec6570c11fcacec3af112e2ff2931a",
      "e7cb6e1dfe6da642b9e5856e98f91915", "3a5f963f0c2dbd81d9c1f663233f582b",
      "3b560746eb55e71ce00b9544faeb2fe9", "42046b25c85dbd6b402b296a97ef83a5",
      "9c7d38da9dccad1bc3c16bca0ba4d808", "5c3fa08c807c5210e0faeb4b67c902f5",
      "388834b1138b8cc182b9a24d18575de8", "cee14aa1bd42dca608bfed10acff716e",
      "6a03f9523ff9ec178a5d8448ee7b2d0f", "669f77ed3196867e9faf60565711c8cb",
      "178ed0cacf39d5eb031afb65b005e4f7", "4a7fc4ff3fec7dce25472c2e8b457208",
      "87fd250d814d84ecb6a065a8250fdf18", "637c3e8200e21b8c7e5e119659dbd635",
      "47ad826682ec4b03924d22de4918eacd", "a3ab5728256056198cc2b73d92b14373",
      "1629dc7684f194c15f5e855243a72ff0", "fe2f18ce7947084f7840d30d49fbcaad",
      "9009b9a658ec414872af1bb0347ee30c", "b09928f5fb282a0b99de6eba2d7877a5",
      "4e619867dbbcaea296e10fa8555ebde5", "1d4ceafa69284d2d1cf56eb9d3f4d3da",
      "7ab218e774099540500548c17d4be628", "54b7afb791f374fefd54897be6b5c908",
      "9099027805d50c43a70374102895a79c", "7c651cfc911bf96e0b606ee3ca04a145",
      "be1fa13657e2ef57cc7a5e265794cfd9", "f8ec20959d0efaa529d1384de88addd1",
      "be1f71467e549cdc3d24d6722673fb43", "c34f0989edeb49c9125c8185d72e62a2",
      "d8124193ed6e4e2b69945dca643bb9fb", "97ef94fa4c866d88c44b3d58ff8136cd",
      "0eb5f6ff1d75645292aea192e03ced37", "1a55bea7371ea49069ac73348305a33c",
      "bfda5f26889f30b13d4ba05303ecf8ce", "c5718a9b347ed13a969bbb93423ac251",
      "3543719280f21dcc88b31d906dd924b3", "8daaabd85ebf091e8aa2052313f872e5",
      "eee34ebf694065e99721990939b6cbe4", "24b6b069385cf3dfea203a5b95dfb114",
      "fc594972c7955c07e2393f22a619904d", "473d28bca0b356021fc516f3e6a55253",
      "fd2cba4c24c0d02535198851d50d579f", "32515fac86315a2586d76a62ea6f201b",
      "644eff865f7c1f854261b11c2a8e07d7", "9cd938a4d83df05dad1d70c6ea74d1fe",
      "f69af279f30363c234310eb4fea51291", "db9b673ca99514f7953f18acc5c390b1",
      "a872107c5116e36a75b7dbee56c47757", "dbae7a1d2fcfc6ddd2f911af0f905055",
      "5257eed79149f2a064dfaf163e01e3bd", "242fc696e8271727615914b4210f5071",
      "8d9ff3a56ab97fdfb5bab64fe4a81c80", "bf2254e8f9abd0013367728f9e16b59e",
      "8b91a39c085f3505949e4f14747e37cb", "09abf7df68f01db2ae5d526e9e2ee81e",
      "eb2502774a32f36cc1594617dddd1663", "ad263ae59f0d719234cfa1c77e717ce1",
      "c9d2d1eed4f8e1b4d05d897c9f1e5f67", "16ff204fb3031451731de91fde5a6b44",
      "eb5d9352b3615c55e895550b497191c1", "a88f3176c389ae8f406364e67b9e1557",
      "09bed1aed5eee6b077797b6cb31b44c2", "0be7804beb8204cea6f59fe55132a363",
      "54b00567b36ea41d04e8e877ead25944", "293cb655ee6ac5a4c1c2cc31f5696ae7",
      "22380e8215ec5f116f04311c267ce997", "e9ba668276b81896d093a9e67ab12036",
      "34c79f703a62b02e6790efbbbeb07241", "bfc96d59ae31369b0618fd78fc295826",
      "f811a6558ce7a67bd3280ee4e3259be8", "8c8205692ab866bcf965dd61d595bf8e",
      "098a85e6ca58293bd2298c07f86a8f8c", "2801da2be78057706abafaab8226fa39",
      "500796a8bccee9141ab21282d6c912a8", "80cbe789099bdf0140da3ac157d7bf47",
      "8ce81e5dea464efaf01b3da5a6da5aa9", "4aec37a4ef16b38bcdba0b01051803d1",
      "e6a2f6431269aa02c93793af80436a7b", "4aa9046b1fb69a6abcb8f3351d75efbd",
      "66b8c45fe8c254088d62d307edb9cf51", "fddcfb5c4da597b9d887e03b8337da0c",
      "69042f478d87f8b6e0d149216f2e8d93", "ad8f00cdace047f3225a235fc2b05738",
      "6a624c0f16aaa6cd4995e01988deaa3b", "7d8f824eaa7dd1657a0f59a2f27b5e0f",
      "7015d246b39a995c11b409485e100b51", "cad1a2944343525c2b9da630937cf40f",
      "c7f24334553fe63c4ab2f3ffbf0183ab", "bf2da14eea8055f51636984e51f4a349",
      "c424de5973035fad65d99e27a2ae07c0", "3ed7b674bcedb60523386119e2cb5fbe",
      "f0bed60941174023ae4d07e7deb23d5f", "159a9d7e5a210dad034ea2e50cbc30e5",
      "695724e11b4cea84603f935571a1f5ff", "788ac03153fad14a7a3f8322543dca64",
      "1964e04df808496e530e6ab5f2182ce4", "18a056a20724416b3ebe7f88743d8297",
      "dd28593d13343349ab120f2667441d8a", "a0bf14c7d0e3336d4cd5b2b1b47f5e73",
      "d467c69d25a1a02481a2e3508d014bad", "3115ecf100476954f6c185250aa099c8",
      "6d1be89c5f75270a366e6dcfcf3de5ba", "4d1686baa1be780736c35914731c186e",
      "3c5db56a9a87347b5a804560dd2dfd97", "24813b7d9043df69980def2dc0617d54",
      "5751c2eb5dd14d8823c714fc4475459e", "5b750ced85b4169116cf4e93c37e76e0",
      "4ebaddc62d252e4028bbddf02807b3b4", "216204523a32b2dcfc612be2d974b0dd",
      "fe995e0532b897921552c7cefebbab91", "354c096b0477ecb122c25c61f85e4125",
      "81b0c7e94e8426f4f227fa23ee5618c1", "85c91b3e8e1dd2dac0cd310f69286f5e",
      "db3c6cce550a8c72f67f59ab1b272d71", "4fae77cd8c5fbf1a7ab49785947c564a",
      "986d08049ce14579b756c250ad68d56e", "a9bb29db295e7364e65f687c418f39f2",
      "d425810268f0bec64400911d990bd237", "621910144f7cd3432b0663ed18137235",
      "a45cafd42aa19e21a7cc83d4a504a074", "6cb51ae713730353acd21906a8a8cebb",
      "b12b97ba267a9ccdd65cef0f4c436e64", "2711d87a5b1d5011e616975ef6d32a8f",
      "2911f90a41b56f53e0cafffc3923c0bc", "beb86799fcc846a7121209dcb2026add",
      "b9b5317d691bbef913c925e226fdb94b", "cc333ef43909a98cd6a0da7043ac3b2a",
      "0291cc086e364c94fdb5250e07f17685", "d98abd571faebeaece0d65d17ac320e6",
      "dd6bb22de6b4caea4e56cb09d2ec6177", "b6bb4f3f2ae83fe4a07ba5f10c3fd267",
      "8c590b173aa66854937f5e05e22c73d9", "bdb4f7428f37c024df2af13a6296f3e9",
      "51d5c74df6f71c146c835ae145aed598", "b8b86c7a65adb388d047bdd756edfbee",
      "084e0d8cfcfb7ad95019336fc94293a9", "516ea8aae87eee2b4f22a1c09852c325",
      "8030de81cd83227b63b2841fd72f6066", "c09fd5e5c061b14dfa78d90f67458ed4",
      "4cc791c47060fb666a86f60b6289e0ec", "fec1a2f79535ffa094ecd4720030cd36",
      "28d2784e2364f10096b516833dcb39f9", "04d2f725d569186682e209608e25c617",
      "087395ef3b31e6a948e5e2a35e2ca345", "6351fd25f8a06beceb244026c65c10c2",
      "3c61acc4a77da654cda7839816d2f385", "c660a12d18c94d0f5c81a22ed9c7ffb5",
      "1f01ebc6151c8f3ea70c423f4e2c64c9", "5e1231b72071ce574dfbbe66f5eed01b",
      "da025263d62d201cad37b53dae0507cb", "1600c5e3a94b0b44a8be99c5da558fd8",
      "dfef4c129ba77381194046afc87e22ff", "5a39e491cca9abe1f1436008f4a455cc",
      "57a5d924b055186029a2656898ad2b8d", "f7f0d88f13ee760300aced0f001b3e34",
      "924b4d0d4a88e669910cd9b890f25d76", "8168c47238ef6f1994bfcef5b9baeef9",
      "b6a59008e7267eb5f5492e2283198b31", "c706d911f2f7ac8372984c60980adfd8",
      "0bde9a2a1364784d2d2da743f77c6992", "b3bd8ce8da0d939fb59caacd8b0c452c",
      "4327b8cb4a6015ff9df50a4a5ca08442", "89f64377bf1e8a46c8247044e8056a98",
      "31028ff4ed7a98d2d4f72615445d7e4a", "f5e331d2036713649c8c15705a87d9f5",
      "d1dca9bb950823af5a4be2fbc52ec83b", "0951a1ddbf1f268d786c7233af62f0ef",
      "aa23686541d98309973f3d68e242a3e2", "ad69fca1d17ae46f423a945fa36660e1",
      "98272fdd4978e941b4f68f571b899fd8", "121952d98be02632491241b44304804a",
      "b16dea3eb57e5967a8b218f08842502b", "5d9bd9d06cdf97480ba073a57480fab3",
      "c9a296f7f58e208252dffec1d46fb538", "d831b2b44cd0782c94a9533c4221e4bf",
      "b93146526d778bae13e579c4962110ef", "2e483ad91c6aba09440d32dff6f838ac",
      "f650a662bee544675d15eeb6d16c5dfb", "4a0d75387c3fabe4e4b8d059e526a623",
      "eaac3bade1e4886d2376b945ae19ca2c", "86392882c55672c087a978a66a162d05",
      "1ba2f2b6ee41fa58119bc0c6dd7909d3", "68d5648946c464cbcfaf9b2abb0567c3",
      "a909bc4a2061b560596840e160549097", "231665465c569176adaa126a76b893b8",
      "a9814e3b420304fb5df34a79d4db7879", "6d9fb653caedbf0091aa5a143707994f",
      "c0165682bd8ee32b0e3c468812aa23a2", "5991506d96f9f27ddf74015705d4e1ea",
      "99443cb4450fa3c81d921e478476f365", "a9e429527f98ef1c9cb832bcd036e00e",
      "b7c9b6bd6b749af86c8ed5ee57659dec", "8dcb599c6eecc7e896d73c71bf1423ad",
      "f0fc3a5418ec55a5bbac2f2d4a94d833", "2fc5e1a6366cfad169343760445d88e0"
   };

   /** Variable-text known-answer test for 128-bit keys. */
   private static final String[] vt_128 = {
      "10b5ffb720b8cb9002a1142b0ba2e94a", "91a7847ef1cd87551b5b4bf6f8e96e2c",
      "5d32aece8383fb2ee22cb4a6061d1429", "b4895cad26dfa1538e9ad80599e1e62a",
      "3b275d40f7daf4a3f59ddfab28ff8715", "c4831bc67e0efff9795c2fa87a2498b5",
      "0f250f3b1f294e54a3e34512b0ab5d0c", "bc0abf8c2037a9263586de6ba1ceed9b",
      "b2b7cbfe069faa342c3bc5476cb674a1", "f09813e536740a3ab9eb885f33273833",
      "d3793fb7125f8396ae504f0fd7d7a703", "bf6b70e9fda664962d95699702f2b9b8",
      "5464988b5914ca8803065aec48e2dca7", "75337da1c6b4e0569ac3c639d7ee211a",
      "f964f16dfda2840e9e0b0a76cfdb7390", "ce523088dc42883d13d88a56853be8ce",
      "ff9e25a0d3e29cd3fc75a115ba401915", "995893db8371206626e550c92a7f999f",
      "b0de770bfc90f6f87b36b9bff67d5b01", "9bb96bb4f47a66499f69258559367d4a",
      "2c352c4d86f01ce418fc3d60fd486230", "2f774b457113bdc1e901766290ec6713",
      "4f3f9119e21c7f8d1a4d44947659fe85", "c3f40fb50935137745a04e19e8f3ba75",
      "0dc94b1df01940c03d586cca5b32b83e", "3e90867ddb27912eed7e1ca5991e0a24",
      "338aef6a3fbeffdde3e240cc04afb281", "2b28f5882fb2765a4ea3d00978437069",
      "db1227fa1f4442f75c2953f2b3442ec1", "af629e3fdbf2ab0cf8e2a128f1fbe298",
      "7ae65213d3da0d0b4cd8d0b9741d2b3f", "fd2d297dc1ce0b0d2577d8847b901de5",
      "c48afc3a70454a8e26e529753c06e619", "c9da79c5d484a0aa048b09ac01bddc6d",
      "ec3d920e6aba87ea53a4d27cdb68c2af", "464e0f8ccc68243617ef7206e399b124",
      "ff3913dca41688c7daf01e161f570cb3", "87e81d705ee314cda5ed031315b94c21",
      "fa0e1474029a15eb2553c55d4291a957", "56a9f473ab27fed3ac6d430f63d36c85",
      "4f057a42d8d5bd9746e434680ddcd5e5", "c9763d65b200493f58414268f579b8ca",
      "4eb6f14893b7dad1e50c8ec0c863fe43", "67135ad9c42bf215248b3f2f0650781d",
      "f226474056e317aa761c55acc086d445", "196f606e80d9a98be468bb3de2ed0f90",
      "8be67189dd26c4a654560c0881c573f5", "372c5708d1c008d59dcf6e0b6c80af07",
      "a374338be436ba8146dbf1f72f67308c", "7e40613f5dd753f6fba6c89391fa560d",
      "1381ee5af9de1c7ec0f0b0a770c1c008", "16e065b53257ef73f9ec7fec947e5ff0",
      "4c336bb5c473c8410c4bd9b9977f66eb", "d4133fe07662e180c5de71630c5565a0",
      "57dcf6d111bc16bae83f360e0a7caccd", "3f5dd0577c58ea13bdca23bf722a79ff",
      "25a178f97eaabb668a5c3f1159fa6e98", "2cb9217031b33c7c1560ca38a215cbb8",
      "add8589c5abf2cd9a75b48c52a2162f2", "563e81d5609565a3735e6717d9c654df",
      "7aab1fd5b60febce36ebf19a750e5d92", "8c8d3cc26f5bfec469021970589ebc99",
      "5c9545fe2243eac114257260960b38c0", "00c4e9de9b257a232e2d3f955f1e5266",
      "516680a1011636774f610a04993eb41e", "8004e202dca22430a65f8a35f49514a1",
      "d9f8abf59993ea093731694ead5e4dcf", "aec7b64c80313afb6231e1205e3aac6c",
      "fa20fe21973588a3f5339fd43e05eebf", "f873e13044503509cbe0e2e555bfffb2",
      "2cd9d5ba4ce26764179a28823ee321e5", "3483c4eaf33b3d624a7595f801c70996",
      "b5c0a55c6629518345ac81ed351e0fc8", "65ab1efc487dbf7d8818bfe6771afe27",
      "852fac011e4750a120534ff3dd44248e", "7f95744df45ad4a1d76727a60cb0b39c",
      "bb2b16dfbd58f56422ea050ffb5272a0", "8128e598967018405d172207a147cd4c",
      "4ac0779bb0bd096f2e1d585604d8def9", "241ef440a5b7b063f6589a6845a5a8dc",
      "52331740090f95f35233affbf0ba0168", "99407bf8582ef12550886ef5b6f169b9",
      "b252710d7e8434acaff5d0b668159faa", "4b159460080683c5a048cb1fe5206ba9",
      "03d5b8aac57b684ac7b87665c589f3c9", "6b5c2794e5a22123907947768238b63f",
      "29c2384191dc2f4f3e0d0bef223c0c16", "4d3b1501af2ba27c0e75478ebec05a26",
      "123ad27ae091cd558ec7dda24c2f84c1", "c7719bdc9e24ea45cef48f2189587de4",
      "0a1f0f7ab0cf5494e92fb52a736675fb", "c1f61bab87b12440a093b2a82709bf5f",
      "21e7d8886955070ee178585114c5fbcf", "f3e336c258d1ab20ff792816baee3ecd",
      "b90b2f56cc32bd9e89ae1d1382106df9", "af4c1782161459998d1248476975b4bd",
      "5ea6c00e5b8742cdf221989be3c2e0a5", "33b94789b40a0ea6e9a4c2a7fd7f39a1",
      "83da4cff4fc689fb57f8a0131c000bf2", "2531ffa17bb629915b5ab1320ae28ef3",
      "40fe64a7ae3faf51c613e0967e36ed41", "9622e548cbad9d0ae5891d29bd0fc355",
      "d43597365f98cc5f39e4c004a4f1e73f", "09989867eb52aa560bf3a4b60215136e",
      "e5bc1ec33d62be8f56ff1d87916da33e", "9ec843057904a54565aba31801f16d7c",
      "85598be8348c344203069e393769ff1d", "079448f58270af4f7d2b4433f1e0dc87",
      "8b144a5358637642c5804ce38a86efcb", "6531358baf10022e507f0b0c0ebc23df",
      "72d9f80ad110ec6830e02d117a2d73e9", "7edec1dc5d17c597075b67e62b31171d",
      "25f263f9be5c840c099ec84973d1b00d", "c5515588ea7308550252efa5d18f12c7",
      "6ef9583950338814b65ac955987f1d76", "92d05167a4526ca60f03e1f05960401d",
      "948de151239262ec36b1f91d08cc2fde", "341e433e71e4b5dfdf27a511fbfb935f",
      "a1e3e540159746526bc538e7224f2c6c", "10b6072dc2413dbd4f22922a042ef8bf",
      "bbbcb8648c674426d8dd58c3e75db3a3", "d27d9d9410bba292ff27afe0e4cfab04",
      "0886d012ef370274b2f28eb79431778f", "9769ec70a6e41d5bd9c8d7e89ea61e8b",
      "bd2e3b263b9267170caca766cec6e3ce", "f3de05775f6d62877627adc3ff61de88",
      "ae358b079ed7a0065ad1dcfa7e94dd69", "3606b9158da0dc461156430e10faf132"
   };

   /** Variable-text known-answer test for 192-bit keys. */
   private static final String[] vt_192 = {
      "b10b271ba25257e1294f2b51f076d0d9", "d522a3b8d6d89d4d2a124fdd88f36896",
      "6faefee5f5255d5465c1befa672af1d3", "409e1d63bc71eb0d6f7eceaa03025897",
      "8a7e9feb4300a2a265f4a14e52011be1", "c1b3fb68eef9e0eb6d3df001e57eac9b",
      "9dcaabb7839129739d1c6f5501624e44", "47402e1c09e0c315b13cab5a5aa17e49",
      "cde5ff61bb5659952a8c5cfedacf06a3", "b26502ac2100f53bed68f24545631548",
      "b607bf21517dbec46d95758fff5a073b", "d4d07ee9ba4512d2985453a0ae16aa8e",
      "139b2bf2f5f28c014a6c2f9aea71faaf", "4c35a2015688b03192dd599a3fddb893",
      "24def3618142e83ba5124d914edb23ee", "4ba48c16fa165c322614a94b0f236ee7",
      "251d642123ee51ac09e02026228c1414", "35604c5d7589b042c9a2e2550b439d31",
      "a921c70d25df86916bafcb0d99834d77", "ffc85ec27dbcedfd7f7265878967ca81",
      "2281b03c4aad92683b10418afd19fcbe", "5df5f94a1760883ca1e80ba88fe02dcc",
      "fb2a57edd5cf3d2d38ec0f185ae33549", "0fa7045d04878611ba1ee1b89ae48fd7",
      "d4bccb77a50cba2a5d3e19c39b10a95d", "0c673c63c42f40cc9c547e8ffec646bc",
      "914cf9c7744a56d5d5e4beabff1aa160", "073e2f69a01ef426c14ade8f0a30dd84",
      "5f0cf0ae5a8bc4d1fb37cf8f2f063437", "c3e16559538c35da71d6a9f5c79144ea",
      "3a154b0a7eab802c082a4a96cc9968d4", "205f88404d8c02bcc7c68a064c74a545",
      "27d5c66ef07bff1f9f2789205ef6f54f", "7adfdd145251206f5eef7206f718c8b0",
      "971a2283f2d786efceed19ca000f4d2a", "59ed88f7ee96e19469c0d5604da0e6f9",
      "d970cba6992074c2f5bea1952ee82b60", "6de2a3c48a416d9b679112fe54133fb5",
      "a85b4fcf07c08c7d99fab5ffc1f7680f", "c4d174f3a6489b334a612ddf4c01903b",
      "f428ce9715b50860eb9dac15b475803d", "15a4c8c2ec76338fd7f4ccbc22335a67",
      "78782ca4652234a2b363d8da766ccdb8", "b9ac015cc263572caee0e34683b3f6e0",
      "7990673b85f6a31660293f4b7295e296", "32210034b38a6810f1623bdc777a5e30",
      "189b8ec3470085b3da97e82ca8964e32", "1e5c3b6e60715a4ee2397d86128feb23",
      "209e0c925c6eba5ee377b3fd99dc74ec", "d6d11ea627c1fe32e1ff75f69f2ef912",
      "330fa8dafe4a31dd83168f9a81c6bff8", "733b41f6263c4669ae05eba359245528",
      "462d605055949e84e16ffcc1d8ce18e0", "eba12bd6b1bf6e263037e89a34b1468a",
      "cfb0119561a328e22fdaac332097a0ae", "65477c9404927a19a24dcb813012cd6b",
      "71c538be7c7c25ac9c9bf433c439a81c", "43ff9632b748a0e5bb93c80b7a72d1e6",
      "958934c3731793b6501508488515386b", "74e629f351506259f0e5ba5616195941",
      "c12671b5543b37ee8045e21738b48456", "efeb1397a75a6d11b76c5aa522c01dd3",
      "40fcf95ce176d4c0614d49836af9b7b9", "800532e2359c581ab7375a0023e5619f",
      "02f4ee73fff08f688c534b8db1cf192f", "703e4282110f7d27a6571abd98152d2b",
      "f93b8b96d84d6457c2def803789e58fb", "3291f29dc7a8fa6bfbab3080d46c8f4e",
      "48a880f8db4317311f7fc7c534734c59", "056ee1a0611b70d8b476a926a260908b",
      "0b22b4f0679776283a545ea4827f5d9d", "b4983ccdb7953ea334fdb51232dd607f",
      "84c360cdb59408fbff6c1d98b633a167", "99370507d1c662282477ee90fa4e72b9",
      "71f8ee3b5575730281c6826d80b4c46c", "33a80e4a9d495bfe8f76e07a1ea05ed0",
      "3db3dbf045c4023b584e8568d25952eb", "a7a9058c51ceb88f05e6cb97a4923727",
      "1e977f546daae78f553861f420e89e62", "9f47bea3e662ca502341f727ecf64ff0",
      "723c52ca0635fc08c4818a8d386bf2ca", "562ad986fc63064fed5b8e77a575a079",
      "3209896941454c0d59e17df9e99ee590", "afa1d3a3a7c49544ecd0f005fe4adf30",
      "d0c15dbaa5f877a191e7ed56d1546dbf", "467e0e629df57e2af5fd7a53b286222c",
      "8097e51df87b86a1756436f5ec835f54", "bc06ed199ac6a8be027a5b57e348765e",
      "f77d868cf760b9143a89809510ccb099", "6ac27194ccb8b8c5a9ef91c460babc4f",
      "90ecb7adf91e1664d96ddfa84f3e3b7a", "c9344eaead5b2d403df0b0e23edda5db",
      "7b1e6b2e84a5fa629825f60885f73423", "730f3bab94156e5835bd741bb8d85ef1",
      "6685abc072c2dc93354a623446ee54a6", "c350186035a00e5802ceda6ae90615e7",
      "38f0bee5d234428ab287939be63bd400", "59c13cf6b6608aa743a57e580a8d82bb",
      "60e6f6290fc3e9dc2c656bfacb387342", "dd49442c7ab7443bd7e41b4b848662d2",
      "3a18f3904cd710c501784b44b7bed928", "8afb4f1591681cfd0ff331f5e749899d",
      "24093cf425ca447b76edbfc957a529b3", "f5734b1089880f05e728cb07b4b50919",
      "e53a9ff4cdf0c76a9e5d3031e8b151da", "a59d4bfa111ac6f33624aac45518094a",
      "46949a5758b3b4184f684358915083db", "4b3dd9621bedd5f7f54f53606737a113",
      "958e939d24b0d7c9b1c237891d3b61c2", "6930b05005417f71db709b617888b43f",
      "ebc7f36e81c8abece7aacd8e5dbd8905", "a8cc6bba06b7aa9806819d7830c0c95a",
      "6921097106f0f151bf767e897e6bd64b", "bce445f2d14016520d49faa088c5724a",
      "1cebadbafac634b929a1d7c1cc5d2032", "58461087da47ecd75993f9f64c9af774",
      "8de6083068111cd9b07b17a58b421028", "8fd00a5dd8e6f93ebb73d3ae05c26338",
      "7b4dc0232880d9d7edfb4d89a07a20dd", "4fb7e947a58b192186cce9aaecc9eaa6",
      "bb8a615964c174450d7e68ad32f4f523", "e37cf906ae4c30a4a4e9f6364989cd56",
      "7b223c8e3fc51b77f4f91a464fd904b6", "02263d1d95636d0d63806875c42a85bb",
      "9c25b3aacdc23f99982c8dd96c209c31", "f7e9ad85347a957d3b1e4556595971d2",
      "bad13d578b19df90632a7f7c927d25d4", "79cd7df51ac0dc8296f1f3e8dbf61076"
   };

   /** Variable-text known-answer test for 256-bit keys. */
   private static final String[] vt_256 = {
      "da5a7992b1b4ae6f8c004bc8a7de5520", "f351351b823e3d7a4f3bf390c4f198cb",
      "a477a65d9db75c8ed7218c52b64c65bb", "f8019452cba4fe618d80a6756183b2e0",
      "d43b7b981b829342fce0e3ec6f5f4c82", "39b3342cb13ce047eccd7ce9d586929d",
      "0a3e7e267fbef117ce63fcb3f0092cbc", "9faa1e723be36aa803321c2383de86ad",
      "371b4bb870cc4bdc24d579c8692a04ae", "5ade07be303d5100c8ed911938e3036d",
      "8e81be84aadb0439d7a8f1b0dc076857", "330317259c635ce4b7327815888add96",
      "397a4e6ce74377398f88290b587b1a9c", "956e7e1cc42dbfe13175da3603e17fb7",
      "9c2e171b8845e0a6eb89acdba6e35886", "0cab96ff0d7b82c6c340aa85fc26fed2",
      "fd2191fae67478be4439a4970d53c256", "541e4d4d249fcc51d859f34e39a9ecaa",
      "57e9c829d86daac43bfe15ea1d0dbc71", "91ce19b8fb8ac19ecb70a695a2cb7f26",
      "27875893acf49a8cff09e24822c98cd1", "296b158a415e2804725b956c464be963",
      "5f45ad8d0224b977a473a710268d8a36", "63cc2d891b1d4ada0542152c1fe510b2",
      "caeed51e22052785525fdcd743b8e1ff", "c48b18f20e09d858b290aa62ce9a8b0e",
      "191f62de542e9700ae76c4c1be1d9696", "6905dff0d55c2102aebd162556561aa3",
      "b9ae1edb4ba213705dd56a9e6b6b31a2", "af94edd9a5e3ca3efb7ee11353719f70",
      "029589f596760b95e68e88ab7c4ca0bc", "55e5a03c73b2e94d6657e02b54b05566",
      "44f2d86d30848bb4138de3543c620774", "e3e7edae5eb0acf9512961cf13dcd6b3",
      "1f8213f17c9e3a61e60ad3d3f0781330", "7143c3bc8bf0e1a7317dabfd3576229f",
      "82c106c8ebcb6dfd66c30160d42086f0", "a5334d1b8b108d78debf9c5fa4a251cf",
      "5976340411009f1d84c677fb46f9703e", "7cff775b3dc48e3bae0f36beb398eba3",
      "f9d4bc9804070eb5477543a2d8016e4f", "ead7b36b6aa3a60a15e1848004f4804f",
      "0ff332ec84a3ba27ecb1cb9c54711431", "efe740dd809552fcb90f5990ffa84d3c",
      "ece37dd24eff8ef3ee99a0603f8eae60", "ea2054379bd199574e9c25f16d4d3cb3",
      "a99297ae1d5d3e3b2a0fe3b18ea01b4d", "f398b0fb341f9e43d7cd93e4304df53c",
      "bd066966361e05202b77d6d6508295f8", "ffc413be45708d9557a3042cd785edb4",
      "f320da590b0303625360f61b83164a29", "e668509755f116bafeab2227a1c589d1",
      "7ca7223be7684fe67c20dffec148929a", "7665d7a085ff83dfd72e0df5728766cc",
      "2f3463dddb86183e5687d0485ea45c68", "bd1ed7c632dfc690492fb11bbabfe8fc",
      "e82b71f103ff08778e0523e562755d64", "98835b1fd0b3f96e80522dbc3baf9cfa",
      "d336c1454f4cd732551aae5a5038260d", "f82722e829dd87ef63f9b17694092197",
      "0f049dfb7a94e0da9e81d2d5cde27a23", "d886980ae0626848a9e857e03487a78f",
      "e57d3b0621f9e84b2661c69040653f98", "78309ad9a1909186a398288a930f5f64",
      "ef41bb099090ab0713596449f2a0db4d", "c9f8a33ea7ef4eef4c5335407533935d",
      "3c99c025b68bdad6b197d309b63412f5", "75b69d14bf789a98d084f1415e20f50a",
      "0d45a84c406f71fd7bf324d92639bc0e", "e95d91858c8fb496eb5a85c757daca53",
      "cd65cc5d31584be6131d6204762c8efd", "0bf30e1a0c33ccf6d5293177886912a7",
      "390b6714d761cdf423ce949f4091c772", "a28a634ef8546b8cf8ece231bcccd36a",
      "b35dd02e0a1eb09656e2000d2543e19f", "ace558d0c6a2935dd0b2c7e624f7bb3a",
      "ff5a2a042bf19f4ad8f63c32a50b3059", "d55b206c3a6fe40c67aa1bf63ef0e94a",
      "1e1ce1c9a1abc317b1b74ae5c46132e3", "104e466f5df0777af3422ac91a744eb8",
      "a531fb2f370adfba929a92cf732e6d0f", "7036b2a6193c3a3e225afe2c7c480220",
      "d3a9cb64c0959dfaf0c703d4142be2a9", "79e99042817c91a89286f662eea8d3cf",
      "cf835ec8134a59eff0b6a80c8c2ccaae", "f4836743d8c3780a2a541f28bb774de7",
      "55762a9a16d9f535a9608b6c985517b8", "95c38d14abc643ff795a4ff39f4c96de",
      "27b1a85d72f1382c5eb08a334ae57c0f", "de2a81d066bb55437dd6ee268d15d8ca",
      "c9ef33af6265fdd27f657a056a613253", "113b948b22741125c1d751c88ed48c30",
      "548eb65b756b1367b01d887c97d9a529", "0fc7320adb91cf075576761788b4f6c0",
      "1b39074f2ce07e9a3f53b59c2018e547", "b007732dfa0d7fa9eb5e310ed6e0d758",
      "ca895ebb630af36c8caf319349d62ec0", "c53fbb7f75242e66171ac34eb966f1b5",
      "7b315fde64ae9dd9d04a6da9bd0fd217", "4a6a1e22ff4c9e6af601b9bb60a00f32",
      "19fb40ef327adfc4c05531ee7a9b253a", "ad134b411f1502b4876c686e3bfd8e02",
      "8972e71a54488c8bd6dba11fc61cf6d4", "74636c3098f0bca38645248c6ce9ccdf",
      "9b2dfd727cabeb35c79fccd3ff2654bd", "4014558c1c55beb49772ae029b7c3cea",
      "b75b9703f69eab25261f43f80c66bb11", "d36460b67ee0edbc9c20a55b7caa66b6",
      "f9874e1b3cb3bb2df9e8dd27e943d8ca", "670ac2362e4b665fe9e2045880f729ed",
      "d6b8ff1fd5a10706a3e1bd6ebf610f98", "7326371926c64ac4b5079857e8197ae4",
      "9f14df1d5519767a1cabeba3221b452b", "2cbb4ce03b80693258e554daae3cbe3a",
      "95e80f51bcd35ab3acefbddf051458df", "9c32de82b267754484f839c4dac3b345",
      "c3500273ea5843ec43dd2c562bde9241", "2e7d7e515c630f97cf162e03d810e327",
      "a4ef9f4fc23600d4e18d96232aae3c57", "fd738baeb7c2771f8fde315eb506adb4",
      "6e567fcf2b853dd8ecc3d58a5e671483", "9408dcbf05dcfac87955841a7bf63b89",
      "c40d8a3673625d95a0e1b1de25832f30", "c96c4a191ea0fe53ebdcdd116a2f7b91",
      "4f0c1103298e21f339bb689253e62982", "1f28ee6256e80cf2f7f9fae235785a93",
      "da877932c672f48a5ec36203be385edf", "6a7f3b805d2ddcba49b89770ade5e507"
   };

   private static final String[] mct_ecb_e_128 = {
      "90e7a5ba9497fa1bfc00f7d1a3a86a1e", "5d0c5da998aaa940d493738892579447",
      "b5e6510fbbd63d828ade0b89ae48ef5f", "8056b61dacb4d3f52976ef5b1d4165e8",
      "3997c4990223e5c70f3cb015f48ec57a", "b7754c34b5837b193364fc55cca342d3",
      "e913e0510a87bdbc5307183b43e5bead", "5deecef8bb9617241c3b72b0ca15d781",
      "f93e76e8ea57a73ecf91768d887a3132", "4155187326abdd90e66ee198b92852c5",
      "5c4242407bd8348aa3c3b6a243b7c371", "5b4cecf483508ff319caa2f00a0c17d5",
      "aeeb485281bb39e30c56a9e7aa636ca8", "74aa340e5149fd50a92687169a5dad2c",
      "8bcead0ed0a0932cf13fcb7ab5011a8d", "c4002db30e5fcde6f52f855600d1c2d5",
      "e9dd90ae5a580fef7f81620f75390414", "f27e886c458ef023209736a2359d3895",
      "8b740f950c175f95040ca834cc11e520", "27d47ca238d5938fa213d8638f5b0f32"
   };

   private static final String[] mct_ecb_e_192 = {
      "2d8af7b79eb7f21fdb394c77c3fb8c3a", "d7585ada56f93796161c56cdba61aa3f",
      "654c5eb1018d6086717b89db97e91a5a", "8f55d4952903b480ce9ae1b2fdca07f4",
      "122e10485449687f99562a5b3ce0c022", "0bef7c642c1728a91e0862c32c7ba03a",
      "48b6fb885b50f097fafe596c834a5e3b", "6504c124d3fe3075519c02c27156adde",
      "13045621123c54ff17fe6fada146086b", "583cfa6c1c0db91e3663d5645c1c0c21",
      "3fff7bc2d2e91274ecabd80d3b7c9d52", "04942897fa1bd6797194b877d118aa83",
      "4f564bee22cfe0ccdfe3d186e2308ab3", "a7fc3fcae5bf84a3ab14e5e9bdb24942",
      "3e9627377333c75504c59712c863d118", "ef52a0844f8bb92efa6a4677ff9f2c33",
      "b0c4441c89c80eed7056b88ec5467f20", "6b069b2bfba26e56c2362fe38ee626cd",
      "e7bce5861d3fe4aa3795a6e084505319", "5719e85e04b4a957a422bc8541e368e6"
   };

   private static final String[] mct_ecb_e_256 = {
      "92efa3ca9477794d31f4df7bce23e60a", "41133a29b97e3b4231549e8c2d0af27e",
      "6ee8edc74dcfefd0c7beaee4cbcbc9c2", "59dd509f8b303ce5527d20d33bd16697",
      "e7c035318d676702fb9be6802459951a", "5a1074dc10f4fe274aab8518c8bb15e4",
      "4c7e1a8243f19bd97cb9e882145a105e", "4bb65c15e5244df0a02b94aa1f4aaa8b",
      "a36403c836e42a4b74d0fc09473f20e4", "6dfc624ed0ffca738e8e7fe5f1d61d45",
      "f169deeaaab1bfc6f9a2ca3e583b5d4f", "815d0e3a77ef5337a59270673c09d40d",
      "2874cf07ba108771b91505dcf4949762", "fea4d56c2ac1c79b53da45aa917f9607",
      "fbe4b4f196ad7b3740b4c1f9fadb52c1", "6a4943fff86ea592a2fd03c9350c60d5",
      "fc5a85e0908daac2e32992bcddf49dba", "e5267119a2f68cd5526ae2013ab7ae6f",
      "662f1b120cf6e9839ddc63e5b07c95c3", "ab036bfb34c30e16b860cd90c59dc805"
   };

   private static final String[] mct_ecb_d_128 = {
      "47c6786045bb9d30f4029e7ccccd1cae", "003380e19f10065740394f48e2fe80b7",
      "7a4f7db38c52a8b711b778a38d203b6b", "fa57c160b1b826ea22f531dc593db5b4",
      "08e2ea201ab8e452bb4584a09a4633ce", "41a63bf1d14b433009bdbd52f8c44530",
      "899ae055bb85d2fba3f2477a2b39ba8b", "d4d9a5c62fcf14757803b001d64831f5",
      "13955b58cee96f35d9ade95e89980ec0", "7f50313bbdb09f23dd65fa983894af5c",
      "4c35a74a727e4f2724cc6bd5b3ed83b1", "35e7082306194e7ed43b4b7654dc2cce",
      "ff47ee17e04bb1fe179f7334b2a1e033", "d1502ce69d5690d933f70757038fb8a6",
      "fd671f0fe5a5fa3e1a3fa98fe6f487dc", "2dd9bb652e56d9b7a56603e02592294a",
      "7749472d9b5d2f958bd779460f01229e", "8108dff830397b03768ac444cd18f834",
      "008498008c6f314e01bf45358c0ffa3e", "cb0d24898863ee67d9ea83f89da39436"
   };

   private static final String[] mct_ecb_d_192 = {
      "0fb9b00ae4e6e0f328ddc43cee462898", "2b088460d9760c2b31f45177036dc67e",
      "92348d985879f9cab7a996d46a691bf1", "45877877b396cd8df70259b89d333802",
      "5191198dcd3eb19484bd813dc1358697", "ad3aca540b5fb68d153dc7dfe12ed973",
      "3ce273418d24b6d03b069238447de371", "da7136e26090b4e53665c7f7b644c3c4",
      "9011e9603b6079951e1392b40257d323", "9d348452906354f5fa58f48a67002fa3",
      "f51a5b9ba3221820b5ae0e9de21c9c6a", "866332e1d38fdd0cd76494a4e36c6a94",
      "1d4abe5b966a121ce284a2a9b1a1538f", "f4cd6825772c668587a910313768d0e9",
      "b4518a110069f67407ba14177fc1c19b", "3aa96762dfbd0f56f48b5b3f3c5c15e4",
      "69c6257d16efbe9f40d7b95e8f29cfda", "843ce86298918d53fc681cb791c83bc4",
      "c5ac003b82da6e6b56c338699508ffd6", "7cca87a97ff9cb0d8da4b2ca49488814"
   };

   private static final String[] mct_ecb_d_256 = {
      "cff2f5875d0fb0d3217052fc9d7b94a3", "bffb4e6fccd8345da6a443852c56cb29",
      "060564434a8bfe5aa08a9aabfd4235c1", "daa415f8d036a0a8a548a30f072a939f",
      "0ed586e6cb70cf5db8e92145039a344b", "47abda56d76fd857df96b06b03d9dbc4",
      "2e6f96e7d07b152b3090e27b4928511e", "7d72b115115c8f9726529a2305b27440",
      "29ab6026606df45afab5f2447f4e31aa", "2bd0403389364744a9a7d44cb68dffa1",
      "d38d518b964048498b234a5a6327f710", "ffc5cb802c9edda4d1b5175055c9eb31",
      "2f7fd315760c3ae080b79232e21463db", "8acebf3a507d238bcbf38a5e53df0c3b",
      "86dc559fd17836a444ac0a624e2b68d5", "8d031edb9423aba701c7aefb3be400ec",
      "7a316f1a8d3c1b9599832ab65f7f5727", "1d198a6aef3da4cc09897e11bce647ed",
      "5be78afd3bebd49a5db0bb5cd7337a11", "c4393c8d03821db7ca187f4a015997d0"
   };

   private static final String[] mct_cbc_e_128 = {
      "9ea101ecebaa41c712bcb0d9bab3e2e4", "f86b2c265b9c75869f31e2c684c13e9f",
      "cc1810dde499dc51461c2b7635288935", "5772284ed8bb79b1ad054fd6481001e2",
      "19bc42a4d5504f5188abf768d2710c85", "a45ce64e28d7757b0c290927cab8e532",
      "9d6c522bf084088cac75cac8ced72606", "260e1992755b417aef10a6bc691b5dcb",
      "beede579a080a9a125616f8902c0f11a", "d12338198a8305eca390a5efe45798e9",
      "05b00a5d3134e705e5390f6cfd43ebda", "30cd5c25e5a9a3ef0302b6598884a9a7",
      "6b098be5c78f17317d122149542eddc6", "c1307c7831a587789758a329655ec6fa",
      "2a5e83a1130cc50d49f4c42d5621c2b6", "2020029df710fb3742786df2fa3672c0",
      "cedbc51f9105d6ba0153a7f232d54067", "1ead6294e3e58bd4657b71cd2379bafc",
      "4a01841d4ba8a53f029d54057630d6ac", "ada4ad7e73fdeadd356a35779439365a"
   };

   private static final String[] mct_cbc_e_192 = {
      "71da83c1c5fbe855469726f8be27e9d2", "4613decb19ec4d226b2a1e894bd7883c",
      "34ed7bbc7761a44dbd2b2462d3fa1277", "e1fadb014f9bc8fdcf76dc280b51e57c",
      "97ea261b6f72512a0fd4ce545b698f1e", "91534bc2868d19c56e824a6796d1e864",
      "e080dc765290f2bceb37362fa495e553", "06f232cea2c9d4fb61221e190ecc0e72",
      "17036fddc2f7e6331e99afed56fb2951", "3dfd60c0037b1ad743f30d6083ddc659",
      "84b653b4db064e873b64084e70a3c4fe", "f4b41df6391c1458106990578f3272dc",
      "8c6f21348b547b4f3f5017b1e729c0a1", "cfe70570de28480096ef868236676389",
      "79f8412ebf35ad4a874d66e50d3afe33", "0e2f1aec7a506cb0b528f6c2f240c90b",
      "4079f62c853b72c6019d8669371be2ee", "1cd572b1166e9abe7cd12ef3fb341a0c",
      "2be41668a9762209e42afd83deaa0808", "d74d8688686bdde60e8c837b28657fb7"
   };

   private static final String[] mct_cbc_e_256 = {
      "61558018134f3b22bd2e8f4e5d48fe9a", "86d2905fa3f2fdcda12a51106bbf1b77",
      "96fa8dad842b398799ab04a83747d0a4", "f22567fe517e7345d6abbdd41fb0ff8b",
      "bf17c92289ea278fba70f128bc36ccd1", "772f8e5eb6ebd459de7623362a6d91d0",
      "982715a94314be6f7eadfe0e1ad6e289", "032bb1b79414b64964b9c2d44ada5a87",
      "417b7e428fc977e08005bf7ffb1f81ee", "1f54cbb03b5e08dd5d12ff5bd376947e",
      "64e27a2d531ac319757470a13f4012f5", "e9164b7801d8d97e1caba85af0ab2200",
      "e5fbffbc0b7d820ffbe2063bdb61c0a9", "29f038a6725d14afd67fc973a79ed057",
      "b7ea0052518ec5450703e4052d54b003", "9c1477af1352fe3ca5f06beced436620",
      "d88df7b084371cb0fd68e70b24d4b7cf", "80f46610518d383f2e00a0aee1c5551e",
      "cf878556ee34cafd35721b21ad913a76", "385689f6233961f6f090d70d7f0496d1"
   };

   private static final String[] mct_cbc_d_128 = {
      "0c81512847a5c6e7a1b8c7d15efa1acb", "e5686f847d5f6a5a6bb501cc8b8456a1",
      "bd2703c15f749213b83eadd2e3028ae0", "be7e1be63ae8ef1b0ae088809cac87d4",
      "ae65b7ebb75e6bff3cf653f9e57a53b2", "6d268ba14efa759bb99ddaf7dcf4889c",
      "f933abb217283b1d84e2cb68b3f2ad40", "5f8833ff8bf23cd1d3cb7b60ac4562a0",
      "7e609fd525401a5f2f307d0c9f16eeb4", "5b7e6d962f0f4a82821cb7bd10db79dc",
      "bbbdbe6a0d3d57135704fd3f35952024", "2a7aa6093ad17c9bbd7f6625e38efe36",
      "62c0911de981f234c03c1b3ca3e46cd8", "ede0e6476d46c91a1aa2f36c8b5c85fa",
      "b5eccc056629cde135c515bd9b9cf763", "25639d10e1ae44c117155f56729fb28c",
      "685d548a62523d08f20a65b87e2d2916", "a516e3bc1d18cc92cd0aa71c1fc9cda6",
      "cce549d95c630de4b7d1609dabdadf9f", "c7a9306e18f4ffb2b6dd3b053893339e"
   };

   private static final String[] mct_cbc_d_192 = {
      "94463805dce72d0f0379b44f8b418a93", "722a8b9aafb559a0c79661cc7ab46629",
      "cc8c741dceb88c076b17268d24593d6f", "c513f538f32449eb90257e37df823a20",
      "912a0a012b42fe6be8a9f04af9d048d2", "2843031f07ce9e7b9c3a7c306d2b9f24",
      "19533ff7a6abbb2c1361a520542f5203", "097aa9502b1668e11e6c44fd6ceae033",
      "a703f98b97f812a850be35c7a60194fc", "77ed0eba60dc2e670413469b9c3c0a8a",
      "919a15e4b6c837b8662f8fa9b8056900", "d45a900ba769d22a0f64be96ca4ff84e",
      "46043c52ccc61ada4d6a1e1ab13e4ad6", "33eab07721eb1752d54c127012f805f3",
      "6f31981121343f5c60a911334d3b47ac", "3ab4eb2551d353c343975c993504c1d1",
      "fd810be807ba203cd39f9af2a9f593df", "92d4f458b6e7b4e0e9e9346bd8d5f622",
      "bd7c419efde6bb927599b9107f3a85a8", "3ba9ed949554fa16fef950416067b325"
   };

   private static final String[] mct_cbc_d_256 = {
      "170e1e83aac120770660422756c188e6", "432e4b5d7b619bcd6e9c969b270901dd",
      "6bd145ca6c07751335fad4b144ea2a71", "8e2f5ce1c20081da0ae47ca728032ec8",
      "3f1d62538956298702a6e5f95deb25a1", "7361faa6ba898e56a03b09aac27fafcf",
      "1778a1bad573e6b9ae3ff75f642b4577", "e2c26a16b1c12a6113d1237379c8a09d",
      "c9915705448c471e2152c0313769a9ea", "893fb2f56557a4e4c728daecbad4a9d5",
      "2443b7b391c1df7a57bee673f9e9d859", "d5692ac5550ba89cb122f96f09612d75",
      "10cb3ac478d46df42a18c64a9683b2f8", "8374a3f3e638a3e020b7bf6f921618df",
      "5d649218b494702fa28986cf8c5061ef", "b8cf8b02d433e204d9c9214919f89153",
      "e1a45baf15901de7dffeee2088f2897e", "9853778dfaab7cbba51182adcc3f9695",
      "d7bdc724a77336feb00b6b5ade435ac4", "b2a5297bd575ca4ca678c4c1a2caa0b8"
   };
}
