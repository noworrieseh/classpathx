package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfSquare.java,v 1.3 2002-06-29 01:27:18 raif Exp $
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
import gnu.crypto.cipher.Square;

import java.util.HashMap;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * <p>Conformance tests for the {@link Square} implementation.</p>
 *
 * @version $Revision: 1.3 $
 */
public class TestOfSquare extends BaseCipherTestCase {

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
         "05F8AAFDEFB4F5F9C751E5B36C8A37D8", "60AFFC9B2312B1397177251CC9296391",
         "D67B7E07C38F311446E16DDD9EA96EBE", "39207579067031706FAB8C3A5C6E5524",
         "FC4F2602A3F6AC34F56906C2EEEE40C5"};

      vt_128 = new String[] {
         "C17B878EAF7D8CA82414E6E4C4A95149", "0A5C0887A1402D3C1A1F00298FD4F65D",
         "B5CD1003E2234CACB6E0F8124671FC46", "422BC7FBD31D4DBB445065C0B96250FD",
         "E528EB4AAED24077717DE65E2A934757"};

      mct_ecb_e_128 = new String[] {
         "04623E016479F2AF395F6BE61CF9E797", "68ABB73D5E60834F47974BE90D412556",
         "9137BB63EF3F92EB04E189BA95D3DF37", "C0143A7B13DF13BFF3350861EC20D25B",
         "AF0E869F42E3E14ADF0A5B04110B3AE5"};

      mct_ecb_d_128 = new String[] {
         "F064F8B9F358306CB8849C8194A468FC", "7DAE38E143FE19A07A23F0E303AB0CE5",
         "F8DFB20ABE6CFA2D9EC2EB9B7547B44B", "FDCCAF31173676F01F81283B809097D1",
         "75EB0C8884DE3DB0FC92695047E8AAC8"};

      mct_cbc_e_128 = new String[] {
         "36987073BCE283781E6E1EF0433DA1DD", "5433C261BEB31FEEDA016F6964BADB30",
         "7AA8B93ECFAB1A27ACD0A8B74D5D1AE7", "3AF465A0FB987C80879FACA8D26D5FEE",
         "2100C9742DE65007D3524DEC7A9858BB"};

      mct_cbc_d_128 = new String[] {
         "8FE89D15BE002BCA733E2A69C7D49AB5", "FCBE647F166FCD6C5C8C6741608E62DB",
         "B6F3BD29C2D260D5C0E223C54B9D877D", "4179351A962BAC5639D95B46DCB768C8",
         "71A450A6B86A2D25BB0177E0AEAFBB93"};
   }

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfSquare(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestOfSquare(Registry.SQUARE_CIPHER);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void setUp() throws Exception {
      cipher = new Square();
      HashMap attrib = new HashMap();
      attrib.put(cipher.CIPHER_BLOCK_SIZE, new Integer(16));
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
