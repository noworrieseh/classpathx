package test.mac;

// ----------------------------------------------------------------------------
// $Id: TestOfTMMH16.java,v 1.1 2002-07-06 23:28:30 raif Exp $
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

import gnu.crypto.mac.IMac;
import gnu.crypto.mac.TMMH16;
import gnu.crypto.prng.BasePRNG;
import gnu.crypto.prng.IRandom;
import gnu.crypto.prng.LimitReachedException;
import gnu.crypto.util.Util;

import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <p>Conformance test for the {@link TMMH16} implementation.</p>
 *
 * @version $Revision: 1.1 $
 */
public class TestOfTMMH16 extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   private IRandom keystream;
   private byte[] output, message, result;
   private IMac mac;
   private HashMap attributes;

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfTMMH16(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfTMMH16.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /*
   KEY_LENGTH: 10
   TAG_LENGTH: 2
   key: { 0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef, 0xfe, 0xdc }
   message: { 0xca, 0xfe, 0xba, 0xbe, 0xba, 0xde }
   output: { 0x9d, 0x6a }
   */
   public void testVector1() throws InvalidKeyException {
      output = new byte[] { (byte) 0x9d, (byte) 0x6a };
      mac = new TMMH16();
      HashMap attributes = new HashMap();
      attributes.put(TMMH16.KEYSTREAM, keystream);
      attributes.put(TMMH16.TAG_LENGTH, new Integer(2));
      mac.init(attributes);
      message = new byte[] {
         (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe,
         (byte) 0xba, (byte) 0xde
      };
      for (int i = 0; i < message.length; i++) {
         mac.update(message[i]);
      }
      result = mac.digest();
      assertTrue("testVector1()", Util.areEqual(result, output));
   }

   /*
   KEY_LENGTH: 10
   TAG_LENGTH: 2
   key: { 0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef, 0xfe, 0xdc }
   message: { 0xca, 0xfe, 0xba }
   output: { 0xc8, 0x8e }
   */
   public void testVector2() throws InvalidKeyException {
      output = new byte[] { (byte) 0xc8, (byte) 0x8e };
      mac = new TMMH16();
      HashMap attributes = new HashMap();
      attributes.put(TMMH16.KEYSTREAM, keystream);
      attributes.put(TMMH16.TAG_LENGTH, new Integer(2));
      mac.init(attributes);
      message = new byte[] {(byte) 0xca, (byte) 0xfe, (byte) 0xba};
      for (int i = 0; i < message.length; i++) {
         mac.update(message[i]);
      }
      result = mac.digest();
      assertTrue("testVector2()", Util.areEqual(result, output));
   }

   /*
   KEY_LENGTH: 10
   TAG_LENGTH: 4
   key: { 0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef, 0xfe, 0xdc }
   message: { 0xca, 0xfe, 0xba, 0xbe, 0xba, 0xde }
   output: { 0x9d, 0x6a, 0xc0, 0xd3 }
   */
   public void testVector3() throws InvalidKeyException {
      output = new byte[] { (byte) 0x9d, (byte) 0x6a, (byte) 0xc0, (byte) 0xd3 };
      mac = new TMMH16();
      HashMap attributes = new HashMap();
      attributes.put(TMMH16.KEYSTREAM, keystream);
      attributes.put(TMMH16.TAG_LENGTH, new Integer(4));
      mac.init(attributes);
      message = new byte[] {
         (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe,
         (byte) 0xba, (byte) 0xde
      };
      for (int i = 0; i < message.length; i++) {
         mac.update(message[i]);
      }
      result = mac.digest();
      assertTrue("testVector3()", Util.areEqual(result, output));
   }

   protected void setUp() throws Exception {
      attributes = new HashMap();
      keystream = new DummyKeystream();
      keystream.init(null);
   }

   // Inner class(es)
   // -------------------------------------------------------------------------

   class DummyKeystream extends BasePRNG {

      DummyKeystream() {
         super("???");
      }

      public Object clone() {
         return null;
      }

      public void setup(Map attributes) {
      }

      public void fillBlock() throws LimitReachedException {
         buffer = new byte[] {
            (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67,
            (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0xfe, (byte) 0xdc
         };
      }
   }
}
