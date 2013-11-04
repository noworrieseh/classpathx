package test.mode;

// ----------------------------------------------------------------------------
// $Id: TestOfECB.java,v 1.1 2002-06-08 05:33:17 raif Exp $
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
import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.util.Util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Conformance tests of the ECB implementation.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://csrc.nist.gov/encryption/modes/Recommendation/Modes01.pdf">
 *    Recommendation for Block Cipher Modes of Operation Methods and Techniques</a>,
 *    Morris Dworkin.</li>
 * </ol>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfECB extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfECB(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfECB.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /**
    * <p>F.1.1 ECB-AES128-Encrypt and F.1.2 ECB-AES128-Decrypt</p>
    */
   public void testAES128() {
      byte[] key = Util.toBytesFromUnicode(
            "\u2b7e\u1516\u28ae\ud2a6\uabf7\u1588\u09cf\u4f3c");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\u3ad7\u7bb4\u0d7a\u3660\ua89e\ucaf3\u2466\uef97");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\uf5d3\ud585\u03b9\u699d\ue785\u895a\u96fd\ubaaf");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\u43b1\ucd7f\u598e\uce23\u881b\u00e3\ued03\u0688");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u7b0c\u785e\u27e8\uad3f\u8223\u2071\u0472\u5dd4");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.ECB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("ECB-AES128-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("ECB-AES128-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("ECB-AES128-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("ECB-AES128-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("ECB-AES128-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("ECB-AES128-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("ECB-AES128-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("ECB-AES128-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   /**
    * <p>F.1.3 ECB-AES192-Encrypt and F.1.4 ECB-AES192-Decrypt</p>
    */
   public void testAES192() {
      byte[] key = Util.toBytesFromUnicode(
            "\u8e73\ub0f7\uda0e\u6452\uc810\uf32b\u8090\u79e5"+
            "\u62f8\uead2\u522c\u6b7b");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\ubd33\u4f1d\u6e45\uf25f\uf712\ua214\u571f\ua5cc");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\u9741\u0484\u6d0a\ud3ad\u7734\uecb3\uecee\u4eef");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\uef7a\ufd22\u70e2\ue60a\udce0\uba2f\uace6\u444e");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u9a4b\u41ba\u738d\u6c72\ufb16\u6916\u03c1\u8e0e");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.ECB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("ECB-AES192-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("ECB-AES192-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("ECB-AES192-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("ECB-AES192-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("ECB-AES192-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("ECB-AES192-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("ECB-AES192-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("ECB-AES192-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   /**
    * <p>F.1.5 ECB-AES256-Encrypt and F.1.6 ECB-AES256-Decrypt</p>
    */
   public void testAES256() {
      byte[] key = Util.toBytesFromUnicode(
            "\u603d\ueb10\u15ca\u71be\u2b73\uaef0\u857d\u7781"+
            "\u1f35\u2c07\u3b61\u08d7\u2d98\u10a3\u0914\udff4");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\uf3ee\ud1bd\ub5d2\ua03c\u064b\u5a7e\u3db1\u81f8");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\u591c\ucb10\ud410\ued26\udc5b\ua74a\u3136\u2870");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\ub6ed\u21b9\u9ca6\uf4f9\uf153\ue7b1\ubeaf\ued1d");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u2330\u4b7a\u39f9\uf3ff\u067d\u8d8f\u9e24\uecc7");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.ECB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("ECB-AES256-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("ECB-AES256-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("ECB-AES256-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("ECB-AES256-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("ECB-AES256-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("ECB-AES256-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("ECB-AES256-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("ECB-AES256-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   protected void setUp() {
   }
}
