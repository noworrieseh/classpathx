/********************************************************************
 * Copyright (c) Open Java Extensions, Andrew Selkirk  LGPL License *
 ********************************************************************/

package javax.mail;

// Imports
import java.util.*;
import javax.mail.event.*;

/**
 * Event Queue.
 */
class EventQueue implements Runnable {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private	QueueElement	head	= null;
	private	QueueElement	tail	= null;
	private	Thread			qThread	= null;

	class QueueElement {

		QueueElement	next	= null;
		QueueElement	prev	= null;
		MailEvent		event	= null;
		Vector			vector	= null;

		QueueElement(EventQueue queue, MailEvent mailEvent,
				Vector list) {
			event = mailEvent;
			vector = list;
//			System.out.println("QueueElement: " + mailEvent.getClass().getName() + " " + list);
		} // QueueElement()


	} // QueueElement


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	public EventQueue() {
		qThread = new Thread(this);
		qThread.start();
	} // EventQueue()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	public void run() {

		// Variables
		QueueElement	element;
		MailEvent		event;
		Vector			list;
		int				index;


		try {

			// Event loop
			while (true) {

				// Get Next Element from Queue
				element = dequeue();

				// Check for Element
				if (element != null) {

					event = element.event;
					list = element.vector;

					for (index = 0; index < list.size(); index++) {
						event.dispatch(list.elementAt(index));
					}

				}

				// Wait until next Event
				if (head == null) {
					Thread.sleep(250);
				}

			} // while

		} catch (InterruptedException e) {
		} // try

	} // run()

	/**
	 * Add a mail event with it's listener list to the event
	 * queue.
	 * @param event Mail event to send to listeners
	 * @param list List of listeners to notify
	 */
	public synchronized void enqueue(MailEvent event, Vector list) {

		// Variables
		QueueElement	element;
		QueueElement	orig;

		// Create Queue Element
		element = new QueueElement(this, event, list);

		// Add Element to List
		if (tail == null) {
			head = element;
			tail = head;
		} else {
			orig = tail;
			tail = element;
			orig.next = tail;
			tail.prev = orig;
		} // if

	} // enqueue()

	void stop() {
		// TODO
	} // stop()

	private synchronized QueueElement dequeue() 
			throws InterruptedException {

		// Variables
		QueueElement	element;

		// Get Next Element
		element = head;

		// Check for no Elements
		if (element == null) {
			return null;
		}

		// Remove from List
		head = element.next;
		if (head != null) {
			head.prev = null;
		} else {
			tail = null;
		}

		// Check for Signal to Stop queue
		if (element.vector.elementAt(0) == null) {
			throw new InterruptedException();
		}

		return element;

	} // dequeue()


} // EventQueue
