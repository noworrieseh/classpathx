package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: KeyPairGeneratorAdapter.java,v 1.1 2002-01-17 11:49:41 raif Exp $
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

import gnu.crypto.sig.IKeyPairGenerator;
import gnu.crypto.sig.KeyPairGeneratorFactory;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * The implementation of a generic {@link java.security.KeyPairGenerator}
 * adapter class to wrap gnu.crypto keypair generator instances.<p>
 *
 * This class defines the <i>Service Provider Interface</i> (<b>SPI</b>) for the
 * {@link java.security.KeyPairGenerator} class, which is used to generate pairs
 * of public and private keys.<p>
 *
 * All the abstract methods in the {@link java.security.KeyPairGeneratorSpi}
 * class are implemented by this class and all its sub-classes.<p>
 *
 * In case the client does not explicitly initialize the KeyPairGenerator (via
 * a call to an <code>initialize()</code> method), the GNU Crypto provider
 * supplies (and document) default values to be used. For example, the GNU
 * Crypto provider uses a default <i>modulus</i> size (keysize) of 1024 bits for
 * the DSS (Digital Signature Standard) a.k.a <i>DSA</i>.<p>
 *
 * @version $Revision: 1.1 $
 */
abstract class KeyPairGeneratorAdapter extends KeyPairGeneratorSpi {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Our underlying keypair instance. */
   protected IKeyPairGenerator adaptee;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.
    *
    * @param kpgName the canonical name of the keypair generator algorithm.
    */
   protected KeyPairGeneratorAdapter(String kpgName) {
      super();

      this.adaptee = KeyPairGeneratorFactory.getInstance(kpgName);
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.KeyPairGeneratorSpi interface implementation
   // -------------------------------------------------------------------------

   public abstract void initialize(int keysize, SecureRandom random);

   public abstract void
   initialize(AlgorithmParameterSpec params, SecureRandom random)
   throws InvalidAlgorithmParameterException;

   public KeyPair generateKeyPair() {
      return adaptee.generate();
   }
}
