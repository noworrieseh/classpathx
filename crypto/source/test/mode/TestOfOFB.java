package test.mode;

// ----------------------------------------------------------------------------
// $Id: TestOfOFB.java,v 1.1 2002-06-08 05:33:17 raif Exp $
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
 * <p>Conformance tests of the OFB implementation.</p>
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
public class TestOfOFB extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfOFB(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfOFB.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /**
    * <p>F.4.1 OFB-AES128-Encrypt and F.4.2 OFB-AES128-Decrypt</p>
    */
   public void testAES128() {
      byte[] key = Util.toBytesFromUnicode(
            "\u2b7e\u1516\u28ae\ud2a6\uabf7\u1588\u09cf\u4f3c");

      byte[] iv = Util.toBytesFromUnicode(
            "\u0001\u0203\u0405\u0607\u0809\u0a0b\u0c0d\u0e0f");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\u3b3f\ud92e\ub72d\uad20\u3334\u49f8\ue83c\ufb4a");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\u7789\u508d\u1691\u8f03\uf53c\u52da\uc54e\ud825");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\u9740\u051e\u9c5f\uecf6\u4344\uf7a8\u2260\uedcc");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u304c\u6528\uf659\uc778\u66a5\u10d9\uc1d6\uae5e");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.OFB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.IV, iv);
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("OFB-AES128-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("OFB-AES128-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("OFB-AES128-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("OFB-AES128-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("OFB-AES128-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("OFB-AES128-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("OFB-AES128-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("OFB-AES128-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   /**
    * <p>F.4.3 OFB-AES192-Encrypt and F.4.4 OFB-AES192-Decrypt</p>
    */
   public void testAES192() {
      byte[] key = Util.toBytesFromUnicode(
            "\u8e73\ub0f7\uda0e\u6452\uc810\uf32b\u8090\u79e5"+
            "\u62f8\uead2\u522c\u6b7b");

      byte[] iv = Util.toBytesFromUnicode(
            "\u0001\u0203\u0405\u0607\u0809\u0a0b\u0c0d\u0e0f");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\ucdc8\u0d6f\uddf1\u8cab\u34c2\u5909\uc99a\u4174");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\ufcc2\u8b8d\u4c63\u837c\u09e8\u1700\uc110\u0401");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\u8d9a\u9aea\uc0f6\u596f\u559c\u6d4d\uaf59\ua5f2");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u6d9f\u2008\u57ca\u6c3e\u9cac\u524b\ud9ac\uc92a");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.OFB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.IV, iv);
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("OFB-AES192-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("OFB-AES192-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("OFB-AES192-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("OFB-AES192-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("OFB-AES192-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("OFB-AES192-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("OFB-AES192-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("OFB-AES192-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   /**
    * <p>F.4.5 OFB-AES256-Encrypt and F.4.6 OFB-AES256-Decrypt</p>
    */
   public void testAES256() {
      byte[] key = Util.toBytesFromUnicode(
            "\u603d\ueb10\u15ca\u71be\u2b73\uaef0\u857d\u7781"+
            "\u1f35\u2c07\u3b61\u08d7\u2d98\u10a3\u0914\udff4");

      byte[] iv = Util.toBytesFromUnicode(
            "\u0001\u0203\u0405\u0607\u0809\u0a0b\u0c0d\u0e0f");

      byte[] pt1 = Util.toBytesFromUnicode(
            "\u6bc1\ubee2\u2e40\u9f96\ue93d\u7e11\u7393\u172a");
      byte[] ct1 = Util.toBytesFromUnicode(
            "\udc7e\u84bf\uda79\u164b\u7ecd\u8486\u985d\u3860");

      byte[] pt2 = Util.toBytesFromUnicode(
            "\uae2d\u8a57\u1e03\uac9c\u9eb7\u6fac\u45af\u8e51");
      byte[] ct2 = Util.toBytesFromUnicode(
            "\u4feb\udc67\u40d2\u0b3a\uc88f\u6ad8\u2a4f\ub08d");

      byte[] pt3 = Util.toBytesFromUnicode(
            "\u30c8\u1c46\ua35c\ue411\ue5fb\uc119\u1a0a\u52ef");
      byte[] ct3 = Util.toBytesFromUnicode(
            "\u71ab\u47a0\u86e8\u6eed\uf39d\u1c5b\uba97\uc408");

      byte[] pt4 = Util.toBytesFromUnicode(
            "\uf69f\u2445\udf4f\u9b17\uad2b\u417b\ue66c\u3710");
      byte[] ct4 = Util.toBytesFromUnicode(
            "\u0126\u141d\u67f3\u7be8\u538f\u5a8b\ue740\ue484");

      byte[] ct = new byte[16];
      byte[] pt = new byte[16];

      IMode ecb = ModeFactory.getInstance(Registry.OFB_MODE, Registry.AES_CIPHER, 128/8);
      Map attributes = new HashMap();
      attributes.put(IMode.IV, iv);
      attributes.put(IMode.KEY_MATERIAL, key);
      try {
         // encryption ........................................................
         attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
         ecb.init(attributes);

         ecb.update(pt1, 0, ct, 0);
         assertTrue("OFB-AES256-Decrypt block #1", Util.areEqual(ct, ct1));

         ecb.update(pt2, 0, ct, 0);
         assertTrue("OFB-AES256-Decrypt block #2", Util.areEqual(ct, ct2));

         ecb.update(pt3, 0, ct, 0);
         assertTrue("OFB-AES256-Decrypt block #3", Util.areEqual(ct, ct3));

         ecb.update(pt4, 0, ct, 0);
         assertTrue("OFB-AES256-Decrypt block #4", Util.areEqual(ct, ct4));

         // decryption ........................................................
         ecb.reset();
         attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
         ecb.init(attributes);

         ecb.update(ct1, 0, pt, 0);
         assertTrue("OFB-AES256-Decrypt block #1", Util.areEqual(pt, pt1));

         ecb.update(ct2, 0, pt, 0);
         assertTrue("OFB-AES256-Decrypt block #2", Util.areEqual(pt, pt2));

         ecb.update(ct3, 0, pt, 0);
         assertTrue("OFB-AES256-Decrypt block #3", Util.areEqual(pt, pt3));

         ecb.update(ct4, 0, pt, 0);
         assertTrue("OFB-AES256-Decrypt block #4", Util.areEqual(pt, pt4));

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail();
      }
   }

   protected void setUp() {
   }
}
