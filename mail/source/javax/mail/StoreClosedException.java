/*
 * StoreClosedException.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail;

/**
 * This exception is thrown when a method is invoked on a Messaging object 
 * and the Store that owns that object has died due to some reason.
 * This exception should be treated as a fatal error;
 * in particular any messaging object belonging to that Store must be 
 * considered invalid.
 * <p>
 * The connect method may be invoked on the dead Store object to revive it.
 * <p>
 * The <code>getMessage()</code> method returns more detailed information 
 * about the error that caused this exception.
 */
public class StoreClosedException 
  extends MessagingException
{

  /*
   * The Store.
   */
  private Store store;

  public StoreClosedException(Store store)
  {
    this(store, null);
  }

  public StoreClosedException(Store store, String message)
  {
    super(message);
    this.store = store;
  }

  /**
   * Returns the dead Store object.
   */
  public Store getStore()
  {
    return store;
  }
}
