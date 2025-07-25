package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: GnuCrypto.java,v 1.5 2002-08-07 09:55:17 raif Exp $
//
// Copyright (C) 2001-2002 Free Software Foundation, Inc.
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

import java.security.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>The GNU Crypto implementation of the Java Cryptographic Extension (JCE)
 * Provider.</p>
 *
 * @version $Revision: 1.5 $
 * @see java.security.Provider
 */
public final class GnuCrypto extends Provider {

   // Constants and variables
   // -------------------------------------------------------------------------

   // Constructor(s)
   // -------------------------------------------------------------------------

   public GnuCrypto() {
      super(Registry.GNU_CRYPTO, 1.0, "GNU Crypto JCE Provider");

      // MessageDigest
      put("MessageDigest.MD4", "gnu.crypto.jce.MD4Spi");
      put("MessageDigest.MD4 ImplementedIn", "Software");
      put("MessageDigest.MD5", "gnu.crypto.jce.MD5Spi");
      put("MessageDigest.MD5 ImplementedIn", "Software");
      put("MessageDigest.RIPEMD128", "gnu.crypto.jce.RipeMD128Spi");
      put("MessageDigest.RIPEMD128 ImplementedIn", "Software");
      put("MessageDigest.RIPEMD160", "gnu.crypto.jce.RipeMD160Spi");
      put("MessageDigest.RIPEMD160 ImplementedIn", "Software");
      put("MessageDigest.SHA-160", "gnu.crypto.jce.Sha160Spi");
      put("MessageDigest.SHA-160 ImplementedIn", "Software");
      put("MessageDigest.WHIRLPOOL", "gnu.crypto.jce.WhirlpoolSpi");
      put("MessageDigest.WHIRLPOOL ImplementedIn", "Software");

      // SecureRandom
      put("SecureRandom.MD4PRNG", "gnu.crypto.jce.MD4RandomSpi");
      put("SecureRandom.MD4PRNG ImplementedIn", "Software");
      put("SecureRandom.MD5PRNG", "gnu.crypto.jce.MD5RandomSpi");
      put("SecureRandom.MD5PRNG ImplementedIn", "Software");
      put("SecureRandom.RIPEMD128PRNG", "gnu.crypto.jce.RipeMD128RandomSpi");
      put("SecureRandom.RIPEMD128PRNG ImplementedIn", "Software");
      put("SecureRandom.RIPEMD160PRNG", "gnu.crypto.jce.RipeMD160RandomSpi");
      put("SecureRandom.RIPEMD160PRNG ImplementedIn", "Software");
      put("SecureRandom.SHA-160PRNG", "gnu.crypto.jce.Sha160RandomSpi");
      put("SecureRandom.SHA-160PRNG ImplementedIn", "Software");
      put("SecureRandom.WHIRLPOOLPRNG", "gnu.crypto.jce.WhirlpoolRandomSpi");
      put("SecureRandom.WHIRLPOOLPRNG ImplementedIn", "Software");
      put("SecureRandom.ICM", "gnu.crypto.jce.ICMRandomSpi");
      put("SecureRandom.ICM ImplementedIn", "Software");
      put("SecureRandom.UMAC-KDF", "gnu.crypto.jce.UMacRandomSpi");
      put("SecureRandom.UMAC-KDF ImplementedIn", "Software");

      // KeyPairGenerator
      put("KeyPairGenerator.DSS", "gnu.crypto.jce.DSSKeyPairGeneratorSpi");
      put("KeyPairGenerator.DSS KeySize", "1024");
      put("KeyPairGenerator.DSS ImplementedIn", "Software");
      put("KeyPairGenerator.RSA", "gnu.crypto.jce.RSAKeyPairGeneratorSpi");
      put("KeyPairGenerator.RSA KeySize", "1024");
      put("KeyPairGenerator.RSA ImplementedIn", "Software");

      // Signature
      put("Signature.DSS/RAW", "gnu.crypto.jce.DSSRawSignatureSpi");
      put("Signature.DSS/RAW KeySize", "1024");
      put("Signature.DSS/RAW ImplementedIn", "Software");
      put("Signature.RSA-PSS/RAW", "gnu.crypto.jce.RSAPSSRawSignatureSpi");
      put("Signature.RSA-PSS/RAW KeySize", "1024");
      put("Signature.RSA-PSS/RAW ImplementedIn", "Software");

      // Cipher
      put("Cipher.ANUBIS",   "gnu.crypto.jce.AnubisSpi");
      put("Cipher.KHAZAD",   "gnu.crypto.jce.KhazadSpi");
      put("Cipher.NULL",     "gnu.crypto.jce.NullCipherSpi");
      put("Cipher.AES",      "gnu.crypto.jce.RijndaelSpi");
      put("Cipher.RIJNDAEL", "gnu.crypto.jce.RijndaelSpi");
      put("Cipher.SQUARE",   "gnu.crypto.jce.SquareSpi");
      put("Cipher.TWOFISH",  "gnu.crypto.jce.TwofishSpi");

      // Aliases
      put("Alg.Alias.MessageDigest.SHS",               "SHA-160");
      put("Alg.Alias.MessageDigest.SHA",               "SHA-160");
      put("Alg.Alias.MessageDigest.SHA1",              "SHA-160");
      put("Alg.Alias.MessageDigest.SHA-1",             "SHA-160");
      put("Alg.Alias.MessageDigest.RIPEMD-160",        "RIPEMD160");
      put("Alg.Alias.MessageDigest.RIPEMD-128",        "RIPEMD128");
      put("Alg.Alias.SecureRandom.SHA-1PRNG",          "SHA-160PRNG");
      put("Alg.Alias.SecureRandom.SHA1PRNG",           "SHA-160PRNG");
      put("Alg.Alias.SecureRandom.SHAPRNG",            "SHA-160PRNG");
      put("Alg.Alias.KeyPairGenerator.DSA",            "DSS");
      put("Alg.Alias.Signature.DSA",                   "DSS/RAW");
      put("Alg.Alias.Signature.SHAwithDSA",            "DSS/RAW");
      put("Alg.Alias.Signature.SHA1withDSA",           "DSS/RAW");
      put("Alg.Alias.Signature.SHA160withDSA",         "DSS/RAW");
//      put("Alg.Alias.Signature.OID.1.2.840.10040.4.3", "DSS");
//      put("Alg.Alias.Signature.1.2.840.10040.4.3",     "DSS");
//      put("Alg.Alias.Signature.1.3.14.3.2.13",         "DSS");
//      put("Alg.Alias.Signature.1.3.14.3.2.27",         "DSS");
      put("Alg.Alias.Signature.SHA/DSA",               "DSS/RAW");
      put("Alg.Alias.Signature.SHA1/DSA",              "DSS/RAW");
      put("Alg.Alias.Signature.SHA-1/DSA",             "DSS/RAW");
      put("Alg.Alias.Signature.SHA-160/DSA",           "DSS/RAW");
      put("Alg.Alias.Signature.DSAwithSHA",            "DSS/RAW");
      put("Alg.Alias.Signature.DSAwithSHA1",           "DSS/RAW");
      put("Alg.Alias.Signature.DSAwithSHA160",         "DSS/RAW");
      put("Alg.Alias.Signature.RSA-PSS",               "RSA-PSS/RAW");
      put("Alg.Alias.Signature.RSAPSS",                "RSA-PSS/RAW");
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * Returns a {@link Set} of names of message digest algorithms available from
    * this {@link Provider}.<p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getMessageDigestNames() {
      return gnu.crypto.hash.HashFactory.getNames();
   }

   /**
    * Returns a {@link Set} of names of secure random implementations available
    * from this {@link Provider}.<p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getSecureRandomNames() {
      Set result = new HashSet();
      // do all the hash-based prng algorithms
      Set md = gnu.crypto.hash.HashFactory.getNames();
      for (Iterator it = md.iterator(); it.hasNext(); ) {
         result.add(((String) it.next()).toUpperCase() + "PRNG");
      }
      // add ICM and UMAC based generators
      result.add(Registry.ICM_PRNG.toUpperCase());
      result.add(Registry.UMAC_PRNG.toUpperCase());

      return Collections.unmodifiableSet(result);
   }

   /**
    * Returns a {@link Set} of names of keypair generator implementations
    * available from this {@link Provider}.<p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getKeyPairGeneratorNames() {
      return gnu.crypto.sig.KeyPairGeneratorFactory.getNames();
   }

   /**
    * Returns a {@link Set} of names of signature scheme implementations
    * available from this {@link Provider}.<p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getSignatureNames() {
      return gnu.crypto.sig.SignatureFactory.getNames();
   }

   /**
    * Returns a {@link Set} of names of symmetric key block cipher algorithms
    * available from this {@link Provider}.<p>
    *
    * @return a {@link Set} of hash names (Strings).
    */
   public static final Set getCipherNames() {
      return gnu.crypto.cipher.CipherFactory.getNames();
   }

   // Instance methods
   // -------------------------------------------------------------------------
}
