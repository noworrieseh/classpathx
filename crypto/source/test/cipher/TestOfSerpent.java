package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfSerpent.java,v 1.4 2002-06-29 01:27:18 raif Exp $
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
import gnu.crypto.cipher.Serpent;

import java.util.HashMap;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * <p>Conformance tests for the {@link Serpent} implementation.</p>
 *
 * @version $Revision: 1.4 $
 */
public class TestOfSerpent extends BaseCipherTestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // KAT and MCT vectors used in this test case
   private static final String[] vk_128;
   private static final String[] vk_192;
   private static final String[] vk_256;
   private static final String[] vt_128;
   private static final String[] vt_192;
   private static final String[] vt_256;
   private static final String[] mct_ecb_e_128;
   private static final String[] mct_ecb_e_192;
   private static final String[] mct_ecb_e_256;
   private static final String[] mct_ecb_d_128;
   private static final String[] mct_ecb_d_192;
   private static final String[] mct_ecb_d_256;
   private static final String[] mct_cbc_e_128;
   private static final String[] mct_cbc_e_192;
   private static final String[] mct_cbc_e_256;
   private static final String[] mct_cbc_d_128;
   private static final String[] mct_cbc_d_192;
   private static final String[] mct_cbc_d_256;

   // static initialiser
   static {
      vk_128 = new String[] {
         "49AFBFAD9D5A34052CD8FFA5986BD2DD", "0C1E2E4E79BD02C2501096E79B5E73FA",
         "D769E71B31F4AE12E14CDC48238D2D7B", "BD0276D6BE072CC00B3D426130BF355F",
         "C191E2121DAEA18EC30957BBCE199A8C"};

      vk_192 = new String[] {
         "E78E5402C7195568AC3678F7A3F60C66", "23645A0DDD4B0F8B73B6215EA938A59E",
         "95D262643C94CAB3E5830FC90A3AD119", "2A66AE878814C427CD1BE1B929D69D1B",
         "C1F74B3C5DF4B485118B6901A22BEF14"};

      vk_256 = new String[] {
         "ABED96E766BF28CBC0EBD21A82EF0819", "959658CDFCD80356DDE045BBE7B1888D",
         "04D49CC0FAE714B46B5B177664DF4C28", "39F1B1A339DED740D80B663A057D4866",
         "DF15B01E30CF9C81688F989809579A86"};

      vt_128 = new String[] {
         "10B5FFB720B8CB9002A1142B0BA2E94A", "91A7847EF1CD87551B5B4BF6F8E96E2C",
         "5D32AECE8383FB2EE22CB4A6061D1429", "B4895CAD26DFA1538E9AD80599E1E62A",
         "3B275D40F7DAF4A3F59DDFAB28FF8715"};

      vt_192 = new String[] {
         "B10B271BA25257E1294F2B51F076D0D9", "D522A3B8D6D89D4D2A124FDD88F36896",
         "6FAEFEE5F5255D5465C1BEFA672AF1D3", "409E1D63BC71EB0D6F7ECEAA03025897",
         "8A7E9FEB4300A2A265F4A14E52011BE1"};

      vt_256 = new String[] {
         "DA5A7992B1B4AE6F8C004BC8A7DE5520", "F351351B823E3D7A4F3BF390C4F198CB",
         "A477A65D9DB75C8ED7218C52B64C65BB", "F8019452CBA4FE618D80A6756183B2E0",
         "D43B7B981B829342FCE0E3EC6F5F4C82"};

      mct_ecb_e_128 = new String[] {
         "90E7A5BA9497FA1BFC00F7D1A3A86A1E", "5D0C5DA998AAA940D493738892579447",
         "B5E6510FBBD63D828ADE0B89AE48EF5F", "8056B61DACB4D3F52976EF5B1D4165E8",
         "3997C4990223E5C70F3CB015F48EC57A"};

      mct_ecb_e_192 = new String[] {
         "2D8AF7B79EB7F21FDB394C77C3FB8C3A", "145A25A48329EA5D2D74A9B4131D5604",
         "21014B6BEFDC0872028061468546A3FD", "8D0A51DA5120051C79D1B445E5673C3A",
         "EB471350AE9DF1AF4FADBCCD44B13902"};

      mct_ecb_e_256 = new String[] {
         "92EFA3CA9477794D31F4DF7BCE23E60A", "1EAEE9147D3844E65E3C7B333587E432",
         "243F7CB4E3E0D01C390397440C844C65", "D77AB40A2E6019E6C4EFFFC1E7D6BB3A",
         "309FC28AD5B155B4BF7069EFAD0A045C"};

      mct_ecb_d_128 = new String[] {
         "47C6786045BB9D30F4029E7CCCCD1CAE", "003380E19F10065740394F48E2FE80B7",
         "7A4F7DB38C52A8B711B778A38D203B6B", "FA57C160B1B826EA22F531DC593DB5B4",
         "08E2EA201AB8E452BB4584A09A4633CE"};

      mct_ecb_d_192 = new String[] {
         "0FB9B00AE4E6E0F328DDC43CEE462898", "4C934EBDA169107CB5194221683E5EAD",
         "833102DCD73503127F0E4B80810AEB21", "E245A98468F854EBCE6B8AC4ED240387",
         "A193B9D74C81D6CA8A3BFCC1145F54CC"};

      mct_ecb_d_256 = new String[] {
         "CFF2F5875D0FB0D3217052FC9D7B94A3", "96D0752AA50B521AA681DD8950B20223",
         "F025F88FB4892701F4EC303DFEDD71BF", "E348D81659CFB277DDF1BB4800D52EE2",
         "9AF9F63F39600E440D296B46441C6611"};

      mct_cbc_e_128 = new String[] {
         "9EA101ECEBAA41C712BCB0D9BAB3E2E4", "F86B2C265B9C75869F31E2C684C13E9F",
         "CC1810DDE499DC51461C2B7635288935", "5772284ED8BB79B1AD054FD6481001E2",
         "19BC42A4D5504F5188ABF768D2710C85"};

      mct_cbc_e_192 = new String[] {
         "71DA83C1C5FBE855469726F8BE27E9D2", "686F0E079A4C7530FF5B304D97491402",
         "E55223A6054344DA94910FC88351B4B8", "9BA532E87F30C1F802293020D6CA8362",
         "FCFA9964AD28461ABDF0F9B7B5C35CB9"};

      mct_cbc_e_256 = new String[] {
         "61558018134F3B22BD2E8F4E5D48FE9A", "6551125DD01801B5214148FA6983A8B3",
         "DCF6FE78ADB9710F9B1705C35386B099", "B0BBAE53DAB874F2ED608710D984FF67",
         "822D99736CA62AB66CC3062120A1F88A"};

      mct_cbc_d_128 = new String[] {
         "0C81512847A5C6E7A1B8C7D15EFA1ACB", "E5686F847D5F6A5A6BB501CC8B8456A1",
         "BD2703C15F749213B83EADD2E3028AE0", "BE7E1BE63AE8EF1B0AE088809CAC87D4",
         "AE65B7EBB75E6BFF3CF653F9E57A53B2"};

      mct_cbc_d_192 = new String[] {
         "94463805DCE72D0F0379B44F8B418A93", "3685DDFA86541723F76C6632513B043F",
         "5DFC60842A5ABCD334E1BA6D405A9575", "C67576550CC798D47CDDDFD57BB781B4",
         "E847DFBA5FB90F050E091110C066D0F0"};

      mct_cbc_d_256 = new String[] {
         "170E1E83AAC120770660422756C188E6", "606B12B660790DD970B4EBA89E17A7A5",
         "780A801BEBEF9DD02635AD6E34BEFA20", "2B6EB3F9E8BFD23977839BB9ABE1CBBB",
         "00912BAE6BEE04BFE9115DA1C8CCB410"};
   }

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
      return new TestOfSerpent(Registry.SERPENT_CIPHER);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void setUp() throws Exception {
      cipher = new Serpent();
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
         assertTrue("katVK(" + algorithm + "): ", katVK(vk_192, cipher, 24));
         assertTrue("katVK(" + algorithm + "): ", katVK(vk_256, cipher, 32));

         assertTrue("katVT(" + algorithm + "): ", katVT(vt_128, cipher, 16));
         assertTrue("katVT(" + algorithm + "): ", katVT(vt_192, cipher, 24));
         assertTrue("katVT(" + algorithm + "): ", katVT(vt_256, cipher, 32));

         assertTrue("mctEncryptECB(" + algorithm + "): ",
               mctEncryptECB(mct_ecb_e_128, cipher, 16));
         assertTrue("mctEncryptECB(" + algorithm + "): ",
               mctEncryptECB(mct_ecb_e_192, cipher, 24));
         assertTrue("mctEncryptECB(" + algorithm + "): ",
               mctEncryptECB(mct_ecb_e_256, cipher, 32));

         assertTrue("mctDecryptECB(" + algorithm + "): ",
               mctDecryptECB(mct_ecb_d_128, cipher, 16));
         assertTrue("mctDecryptECB(" + algorithm + "): ",
               mctDecryptECB(mct_ecb_d_192, cipher, 24));
         assertTrue("mctDecryptECB(" + algorithm + "): ",
               mctDecryptECB(mct_ecb_d_256, cipher, 32));

         assertTrue("mctEncryptCBC(" + algorithm + "): ",
               mctEncryptCBC(mct_cbc_e_128, cipher, 16));
         assertTrue("mctEncryptCBC(" + algorithm + "): ",
               mctEncryptCBC(mct_cbc_e_192, cipher, 24));
         assertTrue("mctEncryptCBC(" + algorithm + "): ",
               mctEncryptCBC(mct_cbc_e_256, cipher, 32));

         assertTrue("mctDecryptCBC(" + algorithm + "): ",
               mctDecryptCBC(mct_cbc_d_128, cipher, 16));
         assertTrue("mctDecryptCBC(" + algorithm + "): ",
               mctDecryptCBC(mct_cbc_d_192, cipher, 24));
         assertTrue("mctDecryptCBC(" + algorithm + "): ",
               mctDecryptCBC(mct_cbc_d_256, cipher, 32));
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail(String.valueOf(x));
      }
   }
}
