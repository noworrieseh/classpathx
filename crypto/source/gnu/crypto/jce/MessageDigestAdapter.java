package gnu.crypto.jce;

// ----------------------------------------------------------------------------
// $Id: MessageDigestAdapter.java,v 1.2 2002-01-21 10:13:12 raif Exp $
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

import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.HashFactory;

import java.security.DigestException;
import java.security.MessageDigestSpi;

/**
 * The implementation of a generic {@link java.security.MessageDigest} adapter
 * class to wrap gnu.crypto hash instances.<p>
 *
 * This class defines the <i>Service Provider Interface</i> (<b>SPI</b>) for the
 * {@link java.security.MessageDigest} class, which provides the functionality
 * of a message digest algorithm, such as MD5 or SHA. Message digests are secure
 * one-way hash functions that take arbitrary-sized data and output a fixed-
 * length hash value.<p>
 *
 * All the abstract methods in the {@link java.security.MessageDigestSpi} class
 * are implemented by this class and all its sub-classes.<p>
 *
 * All the implementations which subclass this object, and which are serviced by
 * the GNU Crypto provider implement the {@link java.lang.Cloneable} interface.<p>
 *
 * @version $Revision: 1.2 $
 */
class MessageDigestAdapter extends MessageDigestSpi implements Cloneable {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** Our underlying hash instance. */
   private IMessageDigest adaptee;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * Trivial protected constructor.
    *
    * @param mdName the canonical name of the hash algorithm.
    */
   protected MessageDigestAdapter(String mdName) {
      this(HashFactory.getInstance(mdName));
   }

   /**
    * Private constructor for cloning purposes.
    *
    * @param adaptee a clone of the underlying hash algorithm instance.
    */
   private MessageDigestAdapter(IMessageDigest adaptee) {
      super();

      this.adaptee = adaptee;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // java.security.MessageDigestSpi interface implementation
   // -------------------------------------------------------------------------

   public Object clone() {
      return new MessageDigestAdapter((IMessageDigest) adaptee.clone());
   }

   public int engineGetDigestLength() {
      return adaptee.hashSize();
   }

   public void engineUpdate(byte input) {
      adaptee.update(input);
   }

   public void engineUpdate(byte[] input, int offset, int len) {
      adaptee.update(input, offset, len);
   }

   public byte[] engineDigest() {
      return adaptee.digest();
   }

   public int engineDigest(byte[] buf, int offset, int len)
   throws DigestException {
      int result = adaptee.hashSize();
      if (len < result) {
         throw new DigestException();
      }
      byte[] md = adaptee.digest();
      System.arraycopy(md, 0, buf, offset, result);
      return result;
   }

   public void engineReset() {
      adaptee.reset();
   }
}
