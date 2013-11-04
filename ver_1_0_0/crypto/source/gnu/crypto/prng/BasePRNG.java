package gnu.crypto.prng;

// ----------------------------------------------------------------------------
// $Id: BasePRNG.java,v 1.6 2002-07-06 23:54:34 raif Exp $
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

import java.util.Map;

/**
 * <p>An abstract class to facilitate implementing PRNG algorithms.</p>
 *
 * @version $Revision: 1.6 $
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

   /**
    * <p>Trivial constructor for use by concrete subclasses.</p>
    *
    * @param name the canonical name of this instance.
    */
   protected BasePRNG(String name) {
      super();

      this.name = name;
      initialised = false;
      buffer = new byte[0];
   }

   // Class methods
   // -------------------------------------------------------------------------

   // Instance methods
   // -------------------------------------------------------------------------

   // IRandom interface implementation ----------------------------------------

   public String name() {
      return name;
   }

   public void init(Map attributes) {
      this.setup(attributes);

      ndx = 0;
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
      if (out == null) {
         return;
      }

      if (!initialised) {
         throw new IllegalStateException();
      }

      if (offset < 0 || offset >= out.length || length < 1) {
         return;
      }

      int limit = ((offset+length) > out.length ? out.length-offset : length);
      for (int i = 0; i < limit; i++) {
         out[offset++] = nextByteInternal();
      }
   }

   // Instance methods
   // -------------------------------------------------------------------------

   public boolean isInitialised() {
      return initialised;
   }

   private byte nextByteInternal() throws LimitReachedException {
      if (ndx >= buffer.length) {
         this.fillBlock();
         ndx = 0;
      }

      return buffer[ndx++];
   }

   // abstract methods to implement by subclasses -----------------------------

   public abstract Object clone();

   public abstract void setup(Map attributes);

   public abstract void fillBlock() throws LimitReachedException;
}
