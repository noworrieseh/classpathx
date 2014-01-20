package test.jce;

// ----------------------------------------------------------------------------
// $Id: TestOfProvider.java,v 1.3 2002-08-07 10:07:54 raif Exp $
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
import gnu.crypto.jce.GnuCrypto;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.security.MessageDigest;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.Random;

/**
 * <p>Conformance tests for the JCE Provider implementation.</p>
 *
 * @version $Revision: 1.3 $
 */
public class TestOfProvider extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfProvider(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfProvider.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testProviderName() {
      Provider us = Security.getProvider(Registry.GNU_CRYPTO);
      assertTrue("testProviderName()", Registry.GNU_CRYPTO.equals(us.getName()));
   }

   public void testSha() {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA", Registry.GNU_CRYPTO);
         assertNotNull("testSha()", md);
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail("testSha()");
      }
   }

   public void testWhirlpool() {
      try {
         MessageDigest md = MessageDigest.getInstance("Whirlpool", Registry.GNU_CRYPTO);
         assertNotNull("testWhirlpool()", md);
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail("testWhirlpool()");
      }
   }

   public void testShaPRNG() {
      try {
         SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG", Registry.GNU_CRYPTO);
         assertNotNull("testShaPRNG()", rnd);
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail("testShaPRNG()");
      }
   }

   public void testWhirlpoolPRNG() {
      try {
         SecureRandom rnd = SecureRandom.getInstance("WHIRLPOOLPRNG", Registry.GNU_CRYPTO);
         assertNotNull("testWhirlpoolPRNG()", rnd);
      } catch (Exception x) {
         x.printStackTrace(System.err);
         fail("testWhirlpoolPRNG()");
      }
   }

   public void testGNUSecureRandoms() {
      String rand;
      Random algorithm;
      for (Iterator it = GnuCrypto.getSecureRandomNames().iterator(); it.hasNext(); ) {
         rand = (String) it.next();
         try {
            algorithm = null;
            algorithm = SecureRandom.getInstance(rand, Registry.GNU_CRYPTO);
            assertNotNull("getInstance("+String.valueOf(rand)+")", algorithm);
         } catch (NoSuchProviderException x) {
            fail("getInstance("+String.valueOf(rand)+"): "+String.valueOf(x));
         } catch (NoSuchAlgorithmException x) {
            fail("getInstance("+String.valueOf(rand)+"): "+String.valueOf(x));
         }
      }
   }

   // helper methods
   // -------------------------------------------------------------------------

   protected void setUp() {
      Security.addProvider(new GnuCrypto()); // dynamically adds our provider
   }
}
