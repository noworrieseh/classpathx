package test.cipher;

// ----------------------------------------------------------------------------
// $Id: TestOfNullCipher.java,v 1.3 2002-06-29 01:27:18 raif Exp $
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
import gnu.crypto.cipher.NullCipher;

import java.util.HashMap;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * <p>Conformance tests for the {@link NullCipher} implementation.</p>
 *
 * @version $Revision: 1.3 $
 */
public class TestOfNullCipher extends BaseCipherTestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfNullCipher(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestOfNullCipher(Registry.NULL_CIPHER);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void setUp() throws Exception {
      cipher = new NullCipher();
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

      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail(String.valueOf(x));
      }
   }
}
