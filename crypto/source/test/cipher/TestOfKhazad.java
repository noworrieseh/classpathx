package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfKhazad.java,v 1.3 2002-06-29 01:27:18 raif Exp $
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

import gnu.crypto.Registry;
import gnu.crypto.cipher.Khazad;

import java.util.HashMap;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * <p>Conformance tests for the {@link Khazad} implementation.</p>
 *
 * @version $Revision: 1.3 $
 */
public class TestOfKhazad extends BaseCipherTestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // KAT and MCT vectors used in this test case
   private static final String[] vk_128;
   private static final String[] vt_128;
   private static final String[] mct_ecb_e_128;
   private static final String[] mct_ecb_d_128;
   private static final String[] mct_cbc_e_128;
   private static final String[] mct_cbc_d_128;

   // static initialiser
   static {
      vk_128 = new String[] {
         "49A4CE32AC190E3F", "BD2226C1128B4AD1", "A3C8D3CAB9D196BC",
         "2C8146E405C2EA36", "9EC02CFC7065D8F8"
      };

      vt_128 = new String[] {
         "9E399864F78ECA02", "3EABB25778098FF7", "A359C027CB02BC47",
         "36E62B8D8DDF2929", "CB4204ACEDDFE80E"
      };

      mct_ecb_e_128 = new String[] {
         "1C8ABEB5F5D8337C", "D29DDD7B07AA2E2E", "2DCA0196F9AF94DA",
         "100AFC93082BC492", "7C4EB4E12D5310BA"
      };

      mct_ecb_d_128 = new String[] {
         "0EF3A83A8A874A5A", "BB83871935B33F01", "ED25D06041BB09CF",
         "A4091D256FFAC8B6", "DAC274A3D13600F8"
      };

      mct_cbc_e_128 = new String[] {
         "AB983C213749B3CA", "9B0C44EF8B2EA836", "748AFB0A891F1556",
         "C7012DE469A78E5D", "DB95DB1BD214C348"
      };

      mct_cbc_d_128 = new String[] {
         "DE93205588933B11", "8651C2BC76A096F6", "4C9494F2BA8C55CF",
         "44EE0CB0AA12B9EC", "6D759B4000216139"
      };
   }

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfKhazad(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestOfKhazad(Registry.KHAZAD_CIPHER);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void setUp() throws Exception {
      cipher = new Khazad();
      HashMap attrib = new HashMap();
      attrib.put(cipher.CIPHER_BLOCK_SIZE, new Integer(8));
      attrib.put(cipher.KEY_MATERIAL, new byte[16]);
      cipher.init(attrib);
   }

   public void runTest() {
      try {
         String algorithm = cipher.name();

         assertTrue("validityTest(" + algorithm + "): ", validityTest());
         assertTrue("cloneabilityTest(" + algorithm + "): ", cloneabilityTest());

         assertTrue("katVK(" + algorithm + "): ", katVK(vk_128, cipher, 16));

         assertTrue("katVT(" + algorithm + "): ", katVT(vt_128, cipher, 16));

         assertTrue("mctEncryptECB(" + algorithm + "): ",
               mctEncryptECB(mct_ecb_e_128, cipher, 16));

         assertTrue("mctDecryptECB(" + algorithm + "): ",
               mctDecryptECB(mct_ecb_d_128, cipher, 16));

         assertTrue("mctEncryptCBC(" + algorithm + "): ",
               mctEncryptCBC(mct_cbc_e_128, cipher, 16));

         assertTrue("mctDecryptCBC(" + algorithm + "): ",
               mctDecryptCBC(mct_cbc_d_128, cipher, 16));
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail(String.valueOf(x));
      }
   }
}
