package gnu.crypto.sig;

// ----------------------------------------------------------------------------
// $Id: IKeyPairGenerator.java,v 1.1 2002-01-11 21:38:55 raif Exp $
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

import java.security.KeyPair;
import java.util.Map;

/**
 * The visible methods of every asymmetric keypair generator.<p>
 *
 * @version $Revision: 1.1 $
 */
public interface IKeyPairGenerator {

   // Constants
   // -------------------------------------------------------------------------

   // Methods
   // -------------------------------------------------------------------------

   /**
    * Returns the canonical name of this keypair generator.<p>
    *
    * @return the canonical name of this instance.
    */
   String name();

   /**
    * [Re]-initialises this instance for use with a given set of attributes.<p>
    *
    * @param attributes a map of name/value pairs to use for setting up the
    * instance.
    * @exception IllegalArgumentException if at least one of the mandatory
    * attributes is missing or an invalid value was specified.
    */
   void setup(Map attributes);

   /**
    * Generates a new keypair based on the attributes used to configure the
    * instance.
    *
    * @return a new keypair.
    */
   KeyPair generate();
}
