/*
 * StatusSource.java
 * Copyright (C) 1999 dog <dog@dog.net.uk>
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
 * 
 * You may retrieve the latest version of this library from
 * http://www.dog.net.uk/knife/
 */

package gnu.mail.treeutil;

/**
 * An interface for defining that an object can pass status messages.
 *
 * @author dog@dog.net.uk
 * @version 1.0.2
 */
public interface StatusSource 
{

	/**
	 * Adds a status listener to this source.
	 * @param l the listener
	 */
	public void addStatusListener(StatusListener l);

	/**
	 * Removes a status listener from this source.
	 * @param l the listener
	 */
	public void removeStatusListener(StatusListener l);

}
