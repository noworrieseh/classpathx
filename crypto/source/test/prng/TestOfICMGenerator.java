package test.prng;

// ----------------------------------------------------------------------------
// $Id: TestOfICMGenerator.java,v 1.3 2002-06-08 05:35:09 raif Exp $
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import gnu.crypto.prng.ICMGenerator;
import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.PRNGFactory;
import gnu.crypto.util.Util;
import gnu.crypto.cipher.IBlockCipher;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * Conformance test for the ICM implementation. Tests if the output matches
 * the values given in the draft-mcgrew-saag-icm-00.txt.
 *
 * @version $Revision: 1.3 $
 */
public class TestOfICMGenerator extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   HashMap map = new HashMap();

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfICMGenerator(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfICMGenerator.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   // Block Cipher Key:     000102030405060708090A0B0C0D0E0F
   // Offset:               000102030405060708090A0B0C0D0E0F
   // BLOCK_INDEX_LENGTH:   4
   // SEGMENT_INDEX_LENGTH: 4
   // Segment Index:        00000000
   //
   // Counter                          Keystream
   // 000102030405060708090A0B0C0D0E0F 0A940BB5416EF045F1C39458C653EA5A
   // 000102030405060708090A0B0C0D0E10 0263EC94661872969ADAFD0F4BA40FDC
   // 000102030405060708090A0B0C0D0E11 1A2D94B3111CA5F8BDC2C84DCC29EC47
   // 000102030405060708090A0B0C0D0E12 4D0BABD2995F9F076223246847B5D30E
   // 000102030405060708090A0B0C0D0E13 8D33F128463B88EFD3F8A52505020379
   public void testVectorOne() {
      byte[] key = new byte[16];
      byte[] offset = new byte[16];
      for (int i = 0; i < 16; i++) {
         key[i] = (byte) i;
         offset[i] = (byte) i;
      }

      map.put(IBlockCipher.CIPHER_BLOCK_SIZE,    new Integer(16));
      map.put(IBlockCipher.KEY_MATERIAL,         key);
      map.put(ICMGenerator.SEGMENT_INDEX_LENGTH, new Integer(4));
      map.put(ICMGenerator.OFFSET,               offset);
      map.put(ICMGenerator.SEGMENT_INDEX,        BigInteger.ZERO);

      ICMGenerator icm = new ICMGenerator();
//      byte[] data;
      byte[] data = new byte[16];
      String ks, computed;
      try {
         icm.init(map);

         ks = "0A940BB5416EF045F1C39458C653EA5A";
//         data = icm.nextBlock();
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "0263EC94661872969ADAFD0F4BA40FDC";
//         data = icm.nextBlock();
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "1A2D94B3111CA5F8BDC2C84DCC29EC47";
//         data = icm.nextBlock();
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "4D0BABD2995F9F076223246847B5D30E";
//         data = icm.nextBlock();
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "8D33F128463B88EFD3F8A52505020379";
//         data = icm.nextBlock();
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

      } catch (Exception x) {
         fail(String.valueOf(x));
      }
   }

   // Block Cipher Key:     75387824D1F1F3815641B65D78D51EDB
   // Offset:               96C9781981053CBBCB36927844F1932C
   // BLOCK_INDEX_LENGTH:   2
   // SEGMENT_INDEX_LENGTH: 6
   // Segment Index:        12345678
   //
   // Counter                          Keystream
   // 96C9781981053CBBCB36A4AC9B69932C EA0AA027BA6D56E44B28F43A7E3E5F58
   // 96C9781981053CBBCB36A4AC9B69932D CBDB3107EDA8D420D3EF7AB7FF290166
   // 96C9781981053CBBCB36A4AC9B69932E AED6F7CB14ED49174336CC010AEB8780
   // 96C9781981053CBBCB36A4AC9B69932F 4C3A754AF027A5C8CCB40E0FE20AF246
   // 96C9781981053CBBCB36A4AC9B699330 01A6D1CE983EF993E980CC9568587E3D
   public void testVectorTwo() {
      byte[] key = new byte[] {
         (byte) 0x75, (byte) 0x38, (byte) 0x78, (byte) 0x24,
         (byte) 0xD1, (byte) 0xF1, (byte) 0xF3, (byte) 0x81,
         (byte) 0x56, (byte) 0x41, (byte) 0xB6, (byte) 0x5D,
         (byte) 0x78, (byte) 0xD5, (byte) 0x1E, (byte) 0xDB
      };

      byte[] offset = new byte[] {
         (byte) 0x96, (byte) 0xC9, (byte) 0x78, (byte) 0x19,
         (byte) 0x81, (byte) 0x05, (byte) 0x3C, (byte) 0xBB,
         (byte) 0xCB, (byte) 0x36, (byte) 0x92, (byte) 0x78,
         (byte) 0x44, (byte) 0xF1, (byte) 0x93, (byte) 0x2C
      };

      map.put(IBlockCipher.CIPHER_BLOCK_SIZE,  new Integer(16));
      map.put(IBlockCipher.KEY_MATERIAL,       key);
      map.put(ICMGenerator.BLOCK_INDEX_LENGTH, new Integer(2));
      map.put(ICMGenerator.OFFSET,             new BigInteger(1, offset));
      map.put(ICMGenerator.SEGMENT_INDEX,      new BigInteger("12345678", 16));

      IRandom icm = PRNGFactory.getInstance("icm");
      byte[] data = new byte[16];
      String ks, computed;
      try {
         icm.init(map);

         ks = "EA0AA027BA6D56E44B28F43A7E3E5F58";
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "CBDB3107EDA8D420D3EF7AB7FF290166";
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "AED6F7CB14ED49174336CC010AEB8780";
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "4C3A754AF027A5C8CCB40E0FE20AF246";
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

         ks = "01A6D1CE983EF993E980CC9568587E3D";
         icm.nextBytes(data, 0, 16);
         computed = Util.toString(data);
         assertTrue(ks.equals(computed));

      } catch (Exception x) {
         fail(String.valueOf(x));
      }
   }

   protected void setUp() throws Exception {
   }
}
