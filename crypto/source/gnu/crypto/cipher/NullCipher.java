package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: NullCipher.java,v 1.7 2002-07-06 23:31:55 raif Exp $
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

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * <p>The implementation of a Null block cipher.</p>
 *
 * <p>This cipher does not alter its input at all, claims to process block sizes
 * 128-, 192- and 256-bit long, and key sizes from 64- to 512-bit in 8-bit
 * increments.</p>
 *
 * @version $Revision: 1.7 $
 */
public final class NullCipher extends BaseCipher {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public NullCipher() {
      super(Registry.NULL_CIPHER, 16, 16);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      NullCipher result = new NullCipher();
      result.currentBlockSize = this.currentBlockSize;

      return result;
   }

   // IBlockCipherSpi interface implementation --------------------------------

   public Iterator blockSizes() {
      ArrayList al = new ArrayList();
      al.add(new Integer( 64 / 8));
      al.add(new Integer(128 / 8));
      al.add(new Integer(192 / 8));
      al.add(new Integer(256 / 8));

      return Collections.unmodifiableList(al).iterator();
   }

   public Iterator keySizes() {
      ArrayList al = new ArrayList();
      for (int n = 8; n < 64; n++) {
         al.add(new Integer(n));
      }

      return Collections.unmodifiableList(al).iterator();
   }

   public Object makeKey(byte[] uk, int bs) throws InvalidKeyException {
      return new Object();
   }

   public void encrypt(byte[] in, int i, byte[] out, int j, Object k, int bs) {
      System.arraycopy(in, i, out, j, bs);
   }

   public void decrypt(byte[] in, int i, byte[] out, int j, Object k, int bs) {
      System.arraycopy(in, i, out, j, bs);
   }

   public boolean selfTest() {
      return true;
   }
}
