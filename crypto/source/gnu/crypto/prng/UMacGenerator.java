package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: UMacGenerator.java,v 1.1 2002-06-08 05:18:41 raif Exp $
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
import gnu.crypto.mac.IMac;
import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.security.InvalidKeyException;

/**
 * <p><i>KDF</i>s (Key Derivation Functions) are used to stretch user-supplied
 * key material to specific size(s) required by high level cryptographic
 * primitives.</p>
 *
 * <p>This <i>KDF</i> implementation is described in the
 * <A HREF="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">UMAC</A>
 * paper. Basically, it operates an underlying <i>AES</i> instance in output
 * feedback mode (OFB), as a <b>strong</b> pseudo-random number generator.</p>
 *
 * <p><code>UMacGenerator</code> requires an <code>index</code> parameter to
 * generate, using the same <code>key</code> material, different pseudo-random
 * outputs.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">
 *    UMAC</a>: Message Authentication Code using Universal Hashing.<br>
 *    T. Krovetz, J. Black, S. Halevi, A. Hevia, H. Krawczyk, and P. Rogaway.</li>
 * </ol>
 *
 * @version $Revision: 1.1 $
 */
public class UMacGenerator extends BasePRNG {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * <p>Property name of the KDF <code>index</code> value to use in this
    * instance. The value is taken to be an {@link java.lang.Integer} less than
    * <code>256</code>.</p>
    */
   public static final String INDEX = "gnu.crypto.prng.umac.kdf.index";

   private IBlockCipher aes;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public UMacGenerator() {
      super(Registry.UMAC_PRNG);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // Implementation of abstract methods in BasePRNG --------------------------

   public void setup(Map attributes) {
      aes = CipherFactory.getInstance(Registry.AES_CIPHER);

      // get the key material
      byte[] key = (byte[]) attributes.get(IBlockCipher.KEY_MATERIAL);
      if (key == null) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }

      int keyLength = key.length;
      // ensure that keyLength is valid for the chosen underlying cipher
      boolean ok = false;
      for (Iterator it = aes.keySizes(); it.hasNext(); ) {
         ok = (keyLength == ((Integer) it.next()).intValue());
         if (ok) {
            break;
         }
      }
      if (!ok) {
         throw new IllegalArgumentException("key length");
      }

      // ensure that remaining params make sense
      int index = -1;
      Integer i = (Integer) attributes.get(INDEX);
      if (i != null) {
         index = i.intValue();
         if (index < 0 || index > 255) {
            throw new IllegalArgumentException(INDEX);
         }
      }

      Map map = new HashMap();
      map.put(IBlockCipher.KEY_MATERIAL, key);
      try {
         aes.init(map);
      } catch (InvalidKeyException x) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }

      buffer = new byte[16];
      buffer[15] = (byte) index;
      try {
         fillBlock();
      } catch (LimitReachedException ignored) {
      }
   }

   public void fillBlock() throws LimitReachedException {
      aes.encryptBlock(buffer, 0, buffer, 0);
   }
}
