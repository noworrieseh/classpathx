/*
 * Timer.java
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

/**
 * A timer class that wakes up listeners after a specified number of milliseconds.
 *
 * @author dog@dog.net.uk
 * @version 1.0.1
 */
public final class Timer
	extends Thread 
{

	long time;
	long interval = 0;
	TimerListener listener;

	/**
	 * Constructs a timer.
	 */
	public Timer(TimerListener listener) 
	{
		this(listener, 0, false);
	}
	
	/**
	 * Constructs a timer with the specified interval, and starts it.
	 */
	public Timer(TimerListener listener, long interval) 
	{
		this(listener, interval, true);
	}
	
	/**
	 * Constructs a timer with the specified interval, indicating whether or not to start it.
	 */
	public Timer(TimerListener listener, long interval, boolean start) 
	{
		this.listener = listener;
		this.interval = interval;
		time = System.currentTimeMillis();
		setDaemon(true);
		setPriority(Thread.MIN_PRIORITY);
		if (start)
			start();
	}

	// -- Accessor methods --

	/**
	 * Returns this timer's interval.
	 */
	public long getInterval() 
	{
		return interval;
	}

	/**
	 * Sets this timer's interval.
	 */
	public void setInterval(long interval) 
	{
		this.interval = interval;
	}
	
	/**
	 * Runs this timer.
	 */
	public void run() 
	{
		boolean interrupt = false;
		while (!interrupt) 
		{
			synchronized (this) 
			{
				try 
				{
					wait(interval);
				}
				catch (InterruptedException e) 
				{
					interrupt = true;
				}
				listener.timerFired(new TimerEvent(this, interval));
			}
		}
	}

}
