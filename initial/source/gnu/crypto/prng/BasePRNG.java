package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: BasePRNG.java,v 1.1.1.1 2001-11-20 13:40:42 raif Exp $
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

import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * An abstract class to facilitate implementing PRNG algorithms.
 *
 * @version $Revision: 1.1.1.1 $
 */
public abstract class BasePRNG implements IRandom {

   // Constants and variables
   // -------------------------------------------------------------------------

   /** The canonical name prefix of the PRNG algorithm. */
   protected String name;

   /** Indicate if this instance has already been initialised or not. */
   protected boolean initialised;

   /** A temporary buffer to serve random bytes. */
   protected byte[] buffer;

   /** The index into buffer of where the next byte will come from. */
   protected int ndx;

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor for use by concrete subclasses. */
   protected BasePRNG(String name) {
      super();

      this.name = name;
      initialised = false;
      buffer = new byte[0];
   }

   // Class methods
   // -------------------------------------------------------------------------

   // IRandom interface implementation
   // -------------------------------------------------------------------------

   public String name() {
      return name;
   }

   public void init(Map attributes) throws GeneralSecurityException {
      this.setup(attributes);

      initialised = true;
   }

   public byte nextByte() throws IllegalStateException, LimitReachedException {
      if (!initialised) {
         throw new IllegalStateException();
      }
      return nextByteInternal();
   }

   public void nextBytes(byte[] out, int offset, int length)
   throws IllegalStateException, LimitReachedException {
      if (!initialised) {
         throw new IllegalStateException();
      }
      for (int i = 0; i < length; i++) {
         out[offset++] = nextByteInternal();
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------

   private byte nextByteInternal() throws LimitReachedException {
      if (ndx >= buffer.length) {
         buffer = this.nextBlock();
         ndx = 0;
      }

      return buffer[ndx++];
   }

   // abstract methods to implement by subclasses
   // -------------------------------------------------------------------------

   public abstract void setup(Map attributes) throws GeneralSecurityException;

   public abstract byte[] nextBlock() throws LimitReachedException;
}
