package test.jce;

// ----------------------------------------------------------------------------
// $Id: TestOfMessageDigest.java,v 1.2 2002-08-07 10:07:54 raif Exp $
//
// Copyright (C) 2001, 2002 Free Software Foundation, Inc.
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
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.jce.GnuCrypto;
import gnu.crypto.util.Util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Iterator;

/**
 * Conformance tests for the JCE Provider implementations of MessageDigest SPI
 * classes.<p>
 *
 * @version $Revision: 1.2 $
 */
public class TestOfMessageDigest extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfMessageDigest(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfMessageDigest.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   /** Should fail with an unknown algorithm. */
   public void testUnknownHash() {
      try {
         MessageDigest.getInstance("Gaudot", Registry.GNU_CRYPTO);
         fail("testUnknownHash()");
      } catch (Exception x) {
         assertTrue("testUnknownHash()", true);
      }
   }

   /**
    * Tests if the result of using a hash through gnu.crypto Factory classes
    * yields same value as using instances obtained the JCE way.
    */
   public void testEquality() {
      String mdName;
      IMessageDigest gnu = null;
      MessageDigest jce = null;
      byte[] in = this.getClass().getName().getBytes();
      byte[] ba1, ba2;
      for (Iterator it = HashFactory.getNames().iterator(); it.hasNext(); ) {
         mdName = (String) it.next();
         try {
            gnu = HashFactory.getInstance(mdName);
            assertNotNull("HashFactory.getInstance("+mdName+")", gnu);
         } catch (InternalError x) {
            fail("HashFactory.getInstance("+mdName+"): "+String.valueOf(x));
         }

         try {
            jce = MessageDigest.getInstance(mdName, Registry.GNU_CRYPTO);
            assertNotNull("MessageDigest.getInstance()", jce);
         } catch (Exception x) {
            x.printStackTrace(System.err);
            fail("MessageDigest.getInstance("+mdName+"): "+String.valueOf(x));
         }

         gnu.update(in, 0, in.length);
         ba1 = gnu.digest();
         ba2 = jce.digest(in);

         assertTrue("testEquality("+mdName+")", Util.areEqual(ba1, ba2));
      }
   }

   /**
    * Tests if the result of a cloned, partially in-progress hash instance,
    * when used later to further process data, yields the same result as the
    * original copy.
    */
   public void testCloneability() throws Exception {
      String mdName;
      MessageDigest md1, md2;
      byte[] abc = "abc".getBytes();
      byte[] in = this.getClass().getName().getBytes();
      byte[] ba1, ba2;
      for (Iterator it = GnuCrypto.getMessageDigestNames().iterator(); it.hasNext(); ) {
         mdName = (String) it.next();
         md1 = MessageDigest.getInstance(mdName, Registry.GNU_CRYPTO);

         md1.update(abc); // start with abc
         md2 = (MessageDigest) md1.clone(); // now clone it

         ba1 = md1.digest(in); // now finish both with in
         ba2 = md2.digest(in);

         assertTrue("testCloneability("+mdName+")", Util.areEqual(ba1, ba2));
      }
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      Security.addProvider(new GnuCrypto()); // dynamically adds our provider
   }
}
