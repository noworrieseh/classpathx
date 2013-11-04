package gnu.crypto.mac;

import gnu.crypto.hash.IMessageDigest;

import java.util.Map;
import java.security.InvalidKeyException;

// ----------------------------------------------------------------------------
// $Id: BaseMac.java,v 1.2 2002-07-06 23:46:19 raif Exp $
//
// Copyright (C) 2001-2002, Free Software Foundation, Inc.
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

/**
 * <p>A base abstract class to facilitate <i>MAC</i> (Message Authentication
 * Code) implementations.</p>
 *
 * @version $Revision: 1.2 $
 */
public abstract class BaseMac implements IMac {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of the <i>MAC</i>. */
   protected String name;

   /** Reference to the underlying hash algorithm instance. */
   protected IMessageDigest underlyingHash;

   /** The length of the truncated output in bytes. */
   protected int truncatedSize;

   /** The authentication key for this instance. */
//   protected transient byte[] K;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name of this instance.
    */
   protected BaseMac(String name) {
      super();

      this.name = name;
   }

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name of this instance.
    * @param underlyingHash the underlying message digest algorithm instance.
    */
   protected BaseMac(String name, IMessageDigest underlyingHash) {
      this(name);

      this.underlyingHash = underlyingHash;
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // gnu.crypto.mac.IMac interface implementation ----------------------------

   public String name() {
      return name;
   }

   public int macSize() {
      return underlyingHash.hashSize();
   }

   public void update(byte b) {
      underlyingHash.update(b);
   }

   public void update(byte[] b, int offset, int len) {
      underlyingHash.update(b, offset, len);
   }

   public void reset() {
      underlyingHash.reset();
   }

   // methods to be implemented by concrete subclasses ------------------------

   public abstract Object clone();

   public abstract void init(Map attributes)
   throws InvalidKeyException, IllegalStateException;

   public abstract byte[] digest();

   public abstract boolean selfTest();
}
