/*
 * TreeEvent.java
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

package dog.util;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * A tree event.
 *
 * @author dog@dog.net.uk
 * @version 1.0.1
 */
public class TreeEvent
	extends ItemEvent 
{

	/**
	 * The item collapsed state change type.
	 */
	public static final int COLLAPSED = 3;
	
	/** 
	 * The item expanded state change type.
	 */
	public static final int EXPANDED = 4;
	
	/**
	 * Constructs a TreeEvent object with the specified ItemSelectable source,
	 * type, item, and item select state.
	 * @param source the ItemSelectable object where the event originated
	 * @id the event type
	 * @item the item where the event occurred
	 * @stateChange the state change type which caused the event
	 */
	public TreeEvent(ItemSelectable source, int id, Object item, int stateChange) 
	{
		super(source, id, item, stateChange);
	}

	public String paramString() 
	{
		Object item = getItem();
		int stateChange = getStateChange();
		
		StringBuffer buffer = new StringBuffer();
		switch (id) 
		{
		  case ITEM_STATE_CHANGED:
			buffer.append("ITEM_STATE_CHANGED");
			break;
		  default:
			buffer.append("unknown type");
		}
		
		buffer.append(",item=");
		buffer.append(item);
		buffer.append(",stateChange=");
		
		switch (stateChange) 
		{
		  case SELECTED:
			buffer.append("SELECTED");
			break;
		  case DESELECTED:
			buffer.append("DESELECTED");
			break;
		  case COLLAPSED:
			buffer.append("COLLAPSED");
			break;
		  case EXPANDED:
			buffer.append("EXPANDED");
			break;
		  default:
			buffer.append("unknown type");
		}
		return buffer.toString();
	}
	
}
