package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: SignatureAdapter.java,v 1.1 2002-01-18 02:27:53 raif Exp $
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

import gnu.crypto.sig.ISignature;
import gnu.crypto.sig.ISignatureCodec;
import gnu.crypto.sig.SignatureFactory;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;

/**
 * The implementation of a generic {@link java.security.Signature} adapter class
 * to wrap gnu.crypto signature instances.<p>
 *
 * This class defines the <i>Service Provider Interface</i> (<b>SPI</b>) for the
 * {@link java.security.Signature} class, which provides the functionality of a
 * digital signature algorithm. Digital signatures are used for authentication
 * and integrity assurance of digital data.<p>
 *
 * All the abstract methods in the {@link java.security.SignatureSpi} class are
 * implemented by this class and all its sub-classes.<p>
 *
 * All the implementations which subclass this object, and which are serviced by
 * the GNU Crypto provider implement the {@link java.lang.Cloneable} interface.<p>
 *
 * @version $Revision: 1.1 $
 */
class SignatureAdapter extends SignatureSpi implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Our underlying signature instance. */
   private ISignature adaptee;

   /** Our underlying signature encoder/decoder engine. */
   private ISignatureCodec codec;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.<p>
    *
    * @param sigName the canonical name of the signature scheme.
    * @param codec the signature codec engine to use with this scheme.
    */
   protected SignatureAdapter(String sigName, ISignatureCodec codec) {
      this(SignatureFactory.getInstance(sigName), codec);
   }

   /**
    * Private constructor for cloning purposes.<p>
    *
    * @param adaptee a clone of the underlying signature scheme instance.
    * @param codec the signature codec engine to use with this scheme.
    */
   private SignatureAdapter(ISignature adaptee, ISignatureCodec codec) {
      super();

      this.adaptee = adaptee;
      this.codec = codec;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.SignatureSpi interface implementation
   // -------------------------------------------------------------------------

   public Object clone() throws CloneNotSupportedException {
      return new SignatureAdapter((ISignature) adaptee.clone(), codec);
   }

   public void engineInitVerify(PublicKey publicKey)
   throws InvalidKeyException {
      try {
         adaptee.setupVerify(publicKey);
      } catch (IllegalArgumentException x) {
         throw new InvalidKeyException(String.valueOf(x));
      }
   }

   public void engineInitSign(PrivateKey privateKey)
   throws InvalidKeyException {
      try {
         adaptee.setupSign(privateKey);
      } catch (IllegalArgumentException x) {
         throw new InvalidKeyException(String.valueOf(x));
      }
   }

   public void engineInitSign(PrivateKey privateKey, SecureRandom random)
   throws InvalidKeyException {
   }

   public void engineUpdate(byte b) throws SignatureException {
      try {
         adaptee.update(b);
      } catch (IllegalStateException x) {
         throw new SignatureException(String.valueOf(x));
      }
   }

   public void engineUpdate(byte[] b, int off, int len)
   throws SignatureException {
      try {
         adaptee.update(b, off, len);
      } catch (IllegalStateException x) {
         throw new SignatureException(String.valueOf(x));
      }
   }

   public byte[] engineSign() throws SignatureException {
      Object signature = null;
      try {
         signature = adaptee.sign();
      } catch (IllegalStateException x) {
         throw new SignatureException(String.valueOf(x));
      }

      byte[] result = codec.encodeSignature(signature);
      return result;
   }

   public int engineSign(byte[] outbuf, int offset, int len)
   throws SignatureException {
      byte[] signature = this.engineSign();
      int result = signature.length;
      if (result > len) {
         throw new SignatureException("len");
      }

      System.arraycopy(signature, 0, outbuf, offset, result);
      return result;
   }

   public boolean engineVerify(byte[] sigBytes) throws SignatureException {
      Object signature = codec.decodeSignature(sigBytes);
      boolean result = false;
      try {
         result = adaptee.verify(signature);
      } catch (IllegalStateException x) {
         throw new SignatureException(String.valueOf(x));
      }

      return result;
   }

   // Deprecated. Replaced by engineSetParameter.
   public void engineSetParameter(String param, Object value)
   throws InvalidParameterException {
      throw new InvalidParameterException("deprecated");
   }

   public void engineSetParameter(AlgorithmParameterSpec params)
   throws InvalidAlgorithmParameterException {
   }

   // Deprecated
   public Object engineGetParameter(String param)
   throws InvalidParameterException {
      throw new InvalidParameterException("deprecated");
   }
}
