package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: IMode.java,v 1.1.1.1 2001-11-20 13:40:40 raif Exp $
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

import gnu.crypto.cipher.IBlockCipher;

/**
 * The basic visible methods of any block cipher mode.<p>
 *
 * Block ciphers encrypt plaintext in fixed size n-bit blocks. For messages
 * larger than n bits, the simplest approach is to segment the message into
 * n-bit blocks and process (encrypt and/or decrypt) each one separately
 * (Electronic Codebook or ECB mode). But this approach has disadvantages in
 * most applications. The block cipher modes of operations are one way of
 * working around those disadvantages.<p>
 *
 * A <i>Mode</i> always employs an underlying block cipher for processing its
 * input. For all intents and purposes, a <i>Mode</i> appears to behave as any
 * other block cipher with the following differences:
 * <ul>
 *    <li>Depending on the specifications of the mode, the block size may be
 *    different that that of the underlying cipher.</li>
 *    <li>While some modes of operations allow operations on block sizes that
 *    can be 1-bit long, this library will only deal with sizes that are
 *    multiple of 8 bits. This is because the <tt>byte</tt> is the smallest,
 *    easy to handle, primitive type in Java.</li>
 *    <li>Some modes need an <i>Initialisation Vector</i> (IV) to be properly
 *    initialised.</li>
 * </ul>
 *
 * Possible additional initialisation values for an instance of that type are:
 * <ul>
 *    <li>The block size in which to operate this mode instance. This
 *    value is <b>optional</b>, if unspecified, the underlying block cipher's
 *    configured block size shall be used.
 *    <li>Whether this mode will be used for encryption or decryption. This
 *    value is <b>mandatory</b> and should be included in the initialisation
 *    parameters. If it isn't, a {@link java.lang.IllegalStateException} will
 *    be thrown if any method, other than <tt>reset()</tt> is invoked on the
 *    instance.</li>
 *    <li>The byte array containing the <i>initialisation vector</i>, if
 *    required by this type of mode.</li>
 * </ul>
 *
 * @version $Revision: 1.1.1.1 $
 */
public interface IMode extends IBlockCipher {

   // Constants
   // -------------------------------------------------------------------------

   /**
    * Property name of the state in which to operate this mode. The value
    * associated to this property name is taken to be an instance of
    * {@link java.lang.Integer} which value is either <tt>ENCRYPTION</tt> or
    * <tt>DECRYPTION</tt>.
    */
   String STATE = "gnu.crypto.operational.state";

   /**
    * Property name of the block size in which to operate this mode. The
    * value associated with this property name is taken to be a
    * {@link java.lang.Integer}. If it is not specified, the value of the
    * block size of the underlying block cipher, used to construct the mode
    * instance, shall be used.
    */
   String MODE_BLOCK_SIZE = "gnu.crypto.mode.block.size";

   /**
    * Property name of the initialisation vector to use, if required, with this
    * instance. The value associated with this property name is taken to be a
    * byte array. If the concrete instance needs such a parameter, and it has
    * not been specified as part of the initialissation parameters, an all-zero
    * byte array of the appropriate size shall be used.
    */
   String IV = "gnu.crypto.iv";

   /** Constant indicating the instance is being used for <i>encryption</i>. */
   int ENCRYPTION = 1;

   /** Constant indicating the instance is being used for <i>decryption</i>. */
   int DECRYPTION = 2;

   // Methods
   // -------------------------------------------------------------------------

   /**
    * A convenience method. Effectively invokes the <tt>encryptBlock()</tt> or
    * <tt>decryptBlock()</tt> method depending on the operational state of the
    * instance.
    *
    * @param in the plaintext.
    * @param inOffset index of <i>in</i> from which to start considering data.
    * @param out the ciphertext.
    * @param outOffset index of <i>out</i> from which to store result.
    * @exception IllegalStateException if the instance is not initialised.
    */
   void update(byte[] in, int inOffset, byte[] out, int outOffset)
   throws IllegalStateException;
}
