package gnu.crypto.cipher;

// ----------------------------------------------------------------------------
// $Id: NullCipher.java,v 1.3 2001-11-22 10:31:06 raif Exp $
//
// Copyright (C) 2001 Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU Library General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
// details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; see the file COPYING. If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
// ----------------------------------------------------------------------------

import gnu.crypto.cipher.BaseCipher;
import gnu.crypto.util.Util;

import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * The implementation of a Null block cipher.<p>
 *
 * This cipher does not alter its input at all, claims to process block sizes
 * 128-, 192- and 256-bit long, and key sizes from 64- to 512-bit in 8-bit
 * increments.
 *
 * @version $Revision: 1.3 $
 */
public final class NullCipher extends BaseCipher {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public NullCipher() {
      super(CipherFactory.NULL_CIPHER, 16, 16);
   }

   // java.lang.Cloneable interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      NullCipher result = new NullCipher();
      result.currentBlockSize = this.currentBlockSize;

      return result;
   }

   // IBlockCipherSpi interface implementation
   // -------------------------------------------------------------------------

   public Iterator blockSizes() {
      ArrayList al = new ArrayList();
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
}
