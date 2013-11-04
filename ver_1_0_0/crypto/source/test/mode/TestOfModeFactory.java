package test.mode;

// ----------------------------------------------------------------------------
// $Id: TestOfModeFactory.java,v 1.2 2001-12-04 12:56:08 raif Exp $
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

import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.mode.IMode;
import java.util.Iterator;

/**
 * Conformance tests for the ModeFactory implementation.
 *
 * @version $Revision: 1.2 $
 */
public class TestOfModeFactory extends TestCase {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public TestOfModeFactory(String name) {
      super(name);
   }

   // Class methods
   // -------------------------------------------------------------------------

   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      return new TestSuite(TestOfModeFactory.class);
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public void testGetInstance() {
      String mode, cipher;
      int bs;
      IMode algorithm;
      for (Iterator mit = ModeFactory.getNames().iterator(); mit.hasNext(); ) {
         mode = (String) mit.next();
         for (Iterator cit = CipherFactory.getNames().iterator(); cit.hasNext(); ) {
            cipher = (String) cit.next();
            IBlockCipher ubc = CipherFactory.getInstance(cipher);
            for (Iterator cbs = ubc.blockSizes(); cbs.hasNext(); ) {
               bs = ((Integer) cbs.next()).intValue();
               try {
                  algorithm = ModeFactory.getInstance(mode, ubc, bs);
                  assertNotNull("getInstance("
                        +String.valueOf(mode)+", "
                        +String.valueOf(cipher)+", "
                        +String.valueOf(8*bs)+")", algorithm);
               } catch (InternalError x) {
                  fail("getInstance("
                        +String.valueOf(mode)+", "
                        +String.valueOf(cipher)+", "
                        +String.valueOf(8*bs)+"): "+String.valueOf(x));
               }
            }
         }
      }
   }
}
