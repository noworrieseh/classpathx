package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: UMacGenerator.java,v 1.3 2002-07-14 01:44:21 raif Exp $
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
import gnu.crypto.cipher.CipherFactory;
import gnu.crypto.cipher.IBlockCipher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.security.InvalidKeyException;

/**
 * <p><i>KDF</i>s (Key Derivation Functions) are used to stretch user-supplied
 * key material to specific size(s) required by high level cryptographic
 * primitives. Described in the <A
 * HREF="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">UMAC</A>
 * paper, this function basically operates an underlying <em>symmetric key block
 * cipher</em> instance in output feedback mode (OFB), as a <b>strong</b>
 * pseudo-random number generator.</p>
 *
 * <p><code>UMacGenerator</code> requires an <em>index</em> parameter
 * (initialisation parameter <code>gnu.crypto.prng.umac.kdf.index</code> taken
 * to be an instance of {@link java.lang.Integer} with a value between
 * <code>0</code> and <code>255</code>). Using the same key, but different
 * indices, generates different pseudorandom outputs.</p>
 *
 * <p>This implementation generalises the definition of the
 * <code>UmacGenerator</code> algorithm to allow for other than the AES symetric
 * key block cipher algorithm (initialisation parameter
 * <code>gnu.crypto.prng.umac.cipher.name</code> taken to be an instance of
 * {@link java.lang.String}). If such a parameter is not defined/included in the
 * initialisation <code>Map</code>, then the "Rijndael" algorithm is used.
 * Furthermore, if the initialisation parameter
 * <code>gnu.crypto.cipher.block.size</code> (taken to be a instance of {@link
 * java.lang.Integer}) is missing or undefined in the initialisation <code>Map
 * </code>, then the cipher's <em>default</em> block size is used.</p>
 *
 * <p><b>NOTE</b>: Rijndael is used as the default symmetric key block cipher
 * algorithm because, with its default block and key sizes, it is the AES. Yet
 * being Rijndael, the algorithm offers more versatile block and key sizes which
 * may prove to be useful for generating "longer" key streams.</p>
 *
 * <p>References:</p>
 *
 * <ol>
 *    <li><a href="http://www.ietf.org/internet-drafts/draft-krovetz-umac-01.txt">
 *    UMAC</a>: Message Authentication Code using Universal Hashing.<br>
 *    T. Krovetz, J. Black, S. Halevi, A. Hevia, H. Krawczyk, and P. Rogaway.</li>
 * </ol>
 *
 * @version $Revision: 1.3 $
 */
public class UMacGenerator extends BasePRNG {

   // Constants and variables
   // -------------------------------------------------------------------------

   /**
    * <p>Property name of the KDF <code>index</code> value to use in this
    * instance. The value is taken to be an {@link Integer} less than
    * <code>256</code>.</p>
    */
   public static final String INDEX = "gnu.crypto.prng.umac.index";

   /** The name of the underlying symmetric key block cipher algorithm. */
   public static final String CIPHER = "gnu.crypto.prng.umac.cipher.name";

   /** The generator's underlying block cipher. */
   private IBlockCipher cipher;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial 0-arguments constructor. */
   public UMacGenerator() {
      super(Registry.UMAC_PRNG);
   }

   /**
    * <p>Private constructor for cloning purposes.</p>
    *
    * @param that the instance to clone.
    */
   private UMacGenerator(UMacGenerator that) {
      this();

      this.cipher = (that.cipher == null ? null : (IBlockCipher) that.cipher.clone());
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // java.lang.Cloneable interface implementation ----------------------------

   public Object clone() {
      return new UMacGenerator(this);
   }

   // Implementation of abstract methods in BasePRNG --------------------------

   public void setup(Map attributes) {
      boolean newCipher = true;
      String cipherName = (String) attributes.get(CIPHER);
      if (cipherName == null) {
         if (cipher == null) { // happy birthday
            cipher = CipherFactory.getInstance(Registry.RIJNDAEL_CIPHER);
         } else { // we already have one. use it as is
            newCipher = false;
         }
      } else {
         cipher = CipherFactory.getInstance(cipherName);
      }

      // find out what block size we should use it in
      int cipherBlockSize = 0;
      Integer bs = (Integer) attributes.get(IBlockCipher.CIPHER_BLOCK_SIZE);
      if (bs != null) {
         cipherBlockSize = bs.intValue();
      } else {
         if (newCipher) { // assume we'll use its default block size
            cipherBlockSize = cipher.defaultBlockSize();
         } // else use as is
      }

      // get the key material
      byte[] key = (byte[]) attributes.get(IBlockCipher.KEY_MATERIAL);
      if (key == null) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }

      int keyLength = key.length;
      // ensure that keyLength is valid for the chosen underlying cipher
      boolean ok = false;
      for (Iterator it = cipher.keySizes(); it.hasNext(); ) {
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

      // now initialise the underlying cipher
      Map map = new HashMap();
      if (cipherBlockSize != 0) { // only needed if new or changed
         map.put(IBlockCipher.CIPHER_BLOCK_SIZE, new Integer(cipherBlockSize));
      }
      map.put(IBlockCipher.KEY_MATERIAL, key);
      try {
         cipher.init(map);
      } catch (InvalidKeyException x) {
         throw new IllegalArgumentException(IBlockCipher.KEY_MATERIAL);
      }

      buffer = new byte[cipher.currentBlockSize()];
      buffer[cipher.currentBlockSize() - 1] = (byte) index;
      try {
         fillBlock();
      } catch (LimitReachedException impossible) {
      }
   }

   public void fillBlock() throws LimitReachedException {
      cipher.encryptBlock(buffer, 0, buffer, 0);
   }
}
