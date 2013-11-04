##
## ----------------------------------------------------------------------------
## $Id: Makefile.am,v 1.8 2002-08-14 16:13:17 raif Exp $
##
## Copyright (C) 2002, Free Software Foundation, Inc.
##
## This program is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by the Free
## Software Foundation; either version 2 of the License or (at your option) any
## later version.
##
## This program is distributed in the hope that it will be useful, but WITHOUT
## ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
## FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
## more details.
##
## You should have received a copy of the GNU General Public License along with
## this program; see the file COPYING.  If not, write to the
##
##    Free Software Foundation Inc.,
##    59 Temple Place - Suite 330,
##    Boston, MA 02111-1307
##    USA
##
## As a special exception, if you link this library with other files to produce
## an executable, this library does not by itself cause the resulting
## executable to be covered by the GNU General Public License.  This exception
## does not however invalidate any other reasons why the executable file might
## be covered by the GNU General Public License.
## ----------------------------------------------------------------------------
##
## Process this file with automake to produce Makefile.in.

# #############################################################################
# Variables
#
crypto_jar = gnu-crypto.jar
encoding_flag = --encoding=UTF-8
AM_JAVACFLAGS = $(encoding_flag) -fCLASSPATH=$(srcdir) -d .
AM_GCJFLAGS = $(encoding_flag) -fassume-compiled -fCLASSPATH=@junit_cp@.:$(srcdir)

BUILT_SOURCES = $(crypto_jar)
data_DATA = $(crypto_jar)
EXTRA_DIST =

# #############################################################################
# Libraries and executables to compile.
#
tool_tests = \
 nessieCipherTVEmitter \
 nessieHashTVEmitter \
 cipherSpeed \
 hashSpeed \
 nistKat \
 nistMCT \
 ent

if JUNIT
  junit_tests = junitTest
else
  junit_tests =
endif

lib_LTLIBRARIES = lib-gnu-crypto.la
check_PROGRAMS = $(junit_tests) $(tool_tests)

# #############################################################################
# Crypto library config
#
lib_gnu_crypto_la_LIBADD =
lib_gnu_crypto_la_LDFLAGS = -version-info 1:0
lib_gnu_crypto_la_SOURCES = $(crypto_sources)

# #############################################################################
# NessieCipherTVEmitter executable
#
nessieCipherTVEmitter_SOURCES = gnu/crypto/tool/NessieCipherTVEmitter.java
nessieCipherTVEmitter_LDADD   = -l-gnu-crypto
nessieCipherTVEmitter_LDFLAGS = --main=gnu.crypto.tool.NessieCipherTVEmitter

# #############################################################################
# NessieHashTVEmitter executable
#
nessieHashTVEmitter_SOURCES = gnu/crypto/tool/NessieHashTVEmitter.java
nessieHashTVEmitter_LDADD   = -l-gnu-crypto
nessieHashTVEmitter_LDFLAGS = --main=gnu.crypto.tool.NessieHashTVEmitter

# #############################################################################
# CipherSpeed executable
#
cipherSpeed_SOURCES = gnu/crypto/tool/CipherSpeed.java
cipherSpeed_LDADD   = -l-gnu-crypto
cipherSpeed_LDFLAGS = --main=gnu.crypto.tool.CipherSpeed

# #############################################################################
# HashSpeed executable
#
hashSpeed_SOURCES = gnu/crypto/tool/HashSpeed.java
hashSpeed_LDADD   = -l-gnu-crypto
hashSpeed_LDFLAGS = --main=gnu.crypto.tool.HashSpeed

# #############################################################################
# NistKat executable
#
nistKat_SOURCES = gnu/crypto/tool/NistKat.java
nistKat_LDADD   = -l-gnu-crypto
nistKat_LDFLAGS = --main=gnu.crypto.tool.NistKat

# #############################################################################
# NistMCT executable
#
nistMCT_SOURCES = gnu/crypto/tool/NistMCT.java
nistMCT_LDADD   = -l-gnu-crypto
nistMCT_LDFLAGS = --main=gnu.crypto.tool.NistMCT

# #############################################################################
# ent executable
#
ent_SOURCES = gnu/crypto/tool/Ent.java
ent_LDADD   = -l-gnu-crypto
ent_LDFLAGS = --main=gnu.crypto.tool.Ent

# #############################################################################
# JUnit test executable
#
junitTest_SOURCES = $(junit_test_sources)
junitTest_LDADD   = -l-gnu-crypto -l-junit
junitTest_LDFLAGS = --main=test.AllTests

# #############################################################################
# library source files.
#
crypto_sources = \
 gnu/crypto/Registry.java \
 \
 gnu/crypto/cipher/Anubis.java \
 gnu/crypto/cipher/BaseCipher.java \
 gnu/crypto/cipher/CipherFactory.java \
 gnu/crypto/cipher/IBlockCipher.java \
 gnu/crypto/cipher/IBlockCipherSpi.java \
 gnu/crypto/cipher/Khazad.java \
 gnu/crypto/cipher/NullCipher.java \
 gnu/crypto/cipher/Rijndael.java \
 gnu/crypto/cipher/Serpent.java \
 gnu/crypto/cipher/Square.java \
 gnu/crypto/cipher/Twofish.java \
 \
 gnu/crypto/exp/ust/UST.java \
 \
 gnu/crypto/hash/BaseHash.java \
 gnu/crypto/hash/HashFactory.java \
 gnu/crypto/hash/IMessageDigest.java \
 gnu/crypto/hash/MD4.java \
 gnu/crypto/hash/MD5.java \
 gnu/crypto/hash/RipeMD128.java \
 gnu/crypto/hash/RipeMD160.java \
 gnu/crypto/hash/Sha160.java \
 gnu/crypto/hash/Whirlpool.java \
 \
 gnu/crypto/jce/DSSKeyPairGeneratorSpi.java \
 gnu/crypto/jce/DSSRawSignatureSpi.java \
 gnu/crypto/jce/GnuCrypto.java \
 gnu/crypto/jce/ICMRandomSpi.java \
 gnu/crypto/jce/KeyPairGeneratorAdapter.java \
 gnu/crypto/jce/MD4RandomSpi.java \
 gnu/crypto/jce/MD4Spi.java \
 gnu/crypto/jce/MD5RandomSpi.java \
 gnu/crypto/jce/MD5Spi.java \
 gnu/crypto/jce/MessageDigestAdapter.java \
 gnu/crypto/jce/RipeMD128RandomSpi.java \
 gnu/crypto/jce/RipeMD128Spi.java \
 gnu/crypto/jce/RipeMD160RandomSpi.java \
 gnu/crypto/jce/RipeMD160Spi.java \
 gnu/crypto/jce/RSAKeyPairGeneratorSpi.java \
 gnu/crypto/jce/RSAPSSRawSignatureSpi.java \
 gnu/crypto/jce/SecureRandomAdapter.java \
 gnu/crypto/jce/Sha160RandomSpi.java \
 gnu/crypto/jce/Sha160Spi.java \
 gnu/crypto/jce/SignatureAdapter.java \
 gnu/crypto/jce/UMacRandomSpi.java \
 gnu/crypto/jce/WhirlpoolRandomSpi.java \
 gnu/crypto/jce/WhirlpoolSpi.java \
 \
 gnu/crypto/mac/BaseMac.java \
 gnu/crypto/mac/HMac.java \
 gnu/crypto/mac/HMacFactory.java \
 gnu/crypto/mac/IMac.java \
 gnu/crypto/mac/MacFactory.java \
 gnu/crypto/mac/TMMH16.java \
 gnu/crypto/mac/UHash32.java \
 gnu/crypto/mac/UMac32.java \
 \
 gnu/crypto/mode/BaseMode.java \
 gnu/crypto/mode/CTR.java \
 gnu/crypto/mode/ECB.java \
 gnu/crypto/mode/ICM.java \
 gnu/crypto/mode/IMode.java \
 gnu/crypto/mode/ModeFactory.java \
 gnu/crypto/mode/OFB.java \
 \
 gnu/crypto/pad/BasePad.java \
 gnu/crypto/pad/IPad.java \
 gnu/crypto/pad/PadFactory.java \
 gnu/crypto/pad/PKCS7.java \
 gnu/crypto/pad/TBC.java \
 gnu/crypto/pad/WrongPaddingException.java \
 \
 gnu/crypto/prng/BasePRNG.java \
 gnu/crypto/prng/ICMGenerator.java \
 gnu/crypto/prng/IRandom.java \
 gnu/crypto/prng/LimitReachedException.java \
 gnu/crypto/prng/MDGenerator.java \
 gnu/crypto/prng/PRNGFactory.java \
 gnu/crypto/prng/UMacGenerator.java \
 \
 gnu/crypto/sig/BaseSignature.java \
 gnu/crypto/sig/IKeyPairCodec.java \
 gnu/crypto/sig/IKeyPairGenerator.java \
 gnu/crypto/sig/ISignature.java \
 gnu/crypto/sig/ISignatureCodec.java \
 gnu/crypto/sig/KeyPairGeneratorFactory.java \
 gnu/crypto/sig/SignatureFactory.java \
 \
 gnu/crypto/sig/dss/DSSKey.java \
 gnu/crypto/sig/dss/DSSKeyPairGenerator.java \
 gnu/crypto/sig/dss/DSSKeyPairRawCodec.java \
 gnu/crypto/sig/dss/DSSPrivateKey.java \
 gnu/crypto/sig/dss/DSSPublicKey.java \
 gnu/crypto/sig/dss/DSSSignature.java \
 gnu/crypto/sig/dss/DSSSignatureRawCodec.java \
 \
 gnu/crypto/sig/rsa/EMSA_PSS.java \
 gnu/crypto/sig/rsa/GnuRSAKey.java \
 gnu/crypto/sig/rsa/GnuRSAPrivateKey.java \
 gnu/crypto/sig/rsa/GnuRSAPublicKey.java \
 gnu/crypto/sig/rsa/RSA.java \
 gnu/crypto/sig/rsa/RSAKeyPairGenerator.java \
 gnu/crypto/sig/rsa/RSAKeyPairRawCodec.java \
 gnu/crypto/sig/rsa/RSAPSSSignature.java \
 gnu/crypto/sig/rsa/RSAPSSSignatureRawCodec.java \
 \
 gnu/crypto/util/Prime.java \
 gnu/crypto/util/PRNG.java \
 gnu/crypto/util/Util.java

junit_test_sources = \
 test/AllTests.java \
 \
 test/cipher/AllTests.java \
 test/cipher/BaseCipherTestCase.java \
 test/cipher/TestOfAnubis.java \
 test/cipher/TestOfCipherFactory.java \
 test/cipher/TestOfKhazad.java \
 test/cipher/TestOfNistVectors.java \
 test/cipher/TestOfNullCipher.java \
 test/cipher/TestOfRijndael.java \
 test/cipher/TestOfSerpent.java \
 test/cipher/TestOfSquare.java \
 test/cipher/TestOfTwofish.java \
 \
 test/exp/AllTests.java \
 test/exp/TestOfUST.java \
 \
 test/hash/AllTests.java \
 test/hash/TestOfHashFactory.java \
 test/hash/TestOfMD4.java \
 test/hash/TestOfMD5.java \
 test/hash/TestOfRipeMD128.java \
 test/hash/TestOfRipeMD160.java \
 test/hash/TestOfSha160.java \
 test/hash/TestOfWhirlpool.java \
 \
 test/jce/AllTests.java \
 test/jce/TestOfKeyPairGenerator.java \
 test/jce/TestOfMessageDigest.java \
 test/jce/TestOfProvider.java \
 test/jce/TestOfSignature.java \
 \
 test/mac/AllTests.java \
 test/mac/TestOfHMacFactory.java \
 test/mac/TestOfMacFactory.java \
 test/mac/TestOfTMMH16.java \
 \
 test/mode/AllTests.java \
 test/mode/TestOfECB.java \
 test/mode/TestOfModeFactory.java \
 test/mode/TestOfOFB.java \
 \
 test/pad/AllTests.java \
 test/pad/TestOfPadFactory.java \
 test/pad/TestOfPKCS7.java \
 test/pad/TestOfTBC.java \
 \
 test/prng/AllTests.java \
 test/prng/TestOfICMGenerator.java \
 test/prng/TestOfPRNGFactory.java \
 \
 test/sig/AllTests.java \
 test/sig/TestOfKeyPairGeneratorFactory.java \
 test/sig/TestOfSignatureFactory.java \
 \
 test/sig/dss/AllTests.java \
 test/sig/dss/TestOfDSSCodec.java \
 test/sig/dss/TestOfDSSKeyGeneration.java \
 test/sig/dss/TestOfDSSSignature.java \
 \
 test/sig/rsa/AllTests.java \
 test/sig/rsa/TestOfRSACodec.java \
 test/sig/rsa/TestOfRSAKeyGeneration.java \
 test/sig/rsa/TestOfRSAPSSSignature.java

# #############################################################################
# .class files
#
crypto_classes = $(crypto_sources:.java=.class)
junit_test_classes = $(junit_test_sources:.java=.class)

## ############################################################################
## Extra targets.
##
SUFFIXES = .java .lo .o .obj .class .jar

%.class: %.java
	$(GCJ) $(AM_JAVACFLAGS) -C $<

$(crypto_jar): $(crypto_classes)
	jar cf $@ $(crypto_classes)

MOSTLYCLEANFILES = $(crypto_jar) $(crypto_classes) $(junit_test_classes)

## ############################################################################
# Remove internal automake dependencies.
#
$(srcdir)/Makefile.in:

Makefile:
