package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: ECB.java,v 1.3 2002-01-11 21:53:00 raif Exp $
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
import gnu.crypto.cipher.IBlockCipher;

/**
 * The implementation of the Electronic Codebook mode.<p>
 *
 * References:<br>
 * <a href="http://csrc.nist.gov/encryption/modes/Recommendation/Modes01.pdf">
 * Recommendation for Block Cipher Modes of Operation Methods and Techniques</a>,
 * Morris Dworkin.<p>
 *
 * @version $Revision: 1.3 $
 */
public class ECB extends BaseMode implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial package-private constructor for use by the Factory class.<p>
    *
    * @param underlyingCipher the underlying cipher implementation.
    * @param cipherBlockSize the underlying cipher block size to use.
    */
   ECB(IBlockCipher underlyingCipher, int cipherBlockSize) {
      super(Registry.ECB_MODE, underlyingCipher, cipherBlockSize);
   }

   /** Private constructor for cloning purposes. */
   private ECB(ECB that) {
      this(that.cipher, that.cipherBlockSize);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Cloneable interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      return new ECB(this);
   }

   // Implementation of abstract methods in BaseMode
   // -------------------------------------------------------------------------

   public void setup() {
      if (modeBlockSize != cipherBlockSize) {
         throw new IllegalArgumentException();
      }
   }

   public void teardown() {
   }

   public void encryptBlock(byte[] in, int i, byte[] out, int o) {
      cipher.encryptBlock(in, i, out, o);
   }

   public void decryptBlock(byte[] in, int i, byte[] out, int o) {
      cipher.decryptBlock(in, i, out, o);
   }

   // own methods
   // -------------------------------------------------------------------------
}
