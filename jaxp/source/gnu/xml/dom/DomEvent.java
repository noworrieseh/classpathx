/*
 * Copyright (C) 1999-2001 David Brownell
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License. 
 */

package gnu.xml.dom;

import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.w3c.dom.views.AbstractView;		// used by UIEvent


/**
 * "Event" implementation.  Events are
 * created (through DocumentEvent interface methods on the document object),
 * and are sent to any target node in the document.
 *
 * <p> Applications may define application specific event subclasses, but
 * should otherwise use the <em>DocumentTraversal</em> interface to acquire
 * event objects.
 *
 * @author David Brownell
 */
public class DomEvent implements Event
{
    String		type;		// init
    EventTarget		target;
    EventTarget		currentNode;
    short		eventPhase;
    boolean		bubbles;	// init
    boolean		cancelable;	// init
    long		timeStamp;	// ?

    /** Returns the event's type (name) as initialized */
    final public String getType () { return type; }

    /**
     * Returns event's target; delivery of an event is initiated
     * by a <em>target.dispatchEvent(event)</em> invocation.
     */
    final public EventTarget getTarget () { return target; }

    /**
     * Returns the target to which events are currently being
     * delivered.  When capturing or bubbling, this will not
     * be what <em>getTarget</em> returns.
     */
    final public EventTarget getCurrentTarget () { return currentNode; }

    /**
     * Returns CAPTURING_PHASE, AT_TARGET, or BUBBLING;
     * only meaningful within EventListener.handleEvent
     */
    final public short getEventPhase () { return eventPhase; }

    /**
     * Returns true if the news of the event bubbles to tree tops
     * (as specified during initialization).
     */
    final public boolean getBubbles () { return bubbles; }

    /**
     * Returns true if the default handling may be canceled
     * (as specified during initialization).
     */
    final public boolean getCancelable () { return cancelable; }

    /**
     * Returns the event's timestamp.
     */
    final public long getTimeStamp () { return timeStamp; }

    boolean		stop;
    boolean		doDefault;


    /**
     * Requests the event no longer be captured or bubbled; only
     * listeners on the event target will see the event, if they
     * haven't yet been notified.
     *
     * <p> <em> Avoid using this </em> except for application-specific
     * events, for which you the protocol explicitly "blesses" the use
     * of this with some event types.  Otherwise, you are likely to break
     * algorithms which depend on event notification either directly or
     * through bubbling or capturing.  </p>
     *
     * <p> Note that this method is not final, specifically to enable
     * enforcing of policies about events always propagating. </p>
     */
    public void stopPropagation () { stop = true; }


    /**
     * Requests that whoever dispatched the event not perform their
     * default processing when event delivery completes.  Initializes
     * event timestamp.
     */
    final public void preventDefault () { doDefault = false; }

    /** Initializes basic event state.  */
    public void initEvent (
	String typeArg,
	boolean canBubbleArg,
	boolean cancelableArg
    ) {
	eventPhase = 0;
	type = typeArg;
	bubbles = canBubbleArg;
	cancelable = cancelableArg;
	timeStamp = System.currentTimeMillis ();
    }

    /** Constructs, but does not initialize, an event. */
    public DomEvent (String type) { this.type = type; }

    /**
     * Returns a basic printable description of the event's type,
     * state, and delivery conditions
     */
    public String toString ()
    {
	StringBuffer buf = new StringBuffer ("[Event ");
	buf.append (type);
	switch (eventPhase) {
	    case CAPTURING_PHASE:	buf.append (", CAPTURING"); break;
	    case AT_TARGET:		buf.append (", AT TARGET"); break;
	    case BUBBLING_PHASE:	buf.append (", BUBBLING"); break;
	    default:		buf.append (", (inactive)"); break;
	}
	if (bubbles && eventPhase != BUBBLING_PHASE)
	    buf.append (", bubbles");
	if (cancelable)
	    buf.append (", can cancel");
	// were we to provide subclass info, this's where it'd live
	buf.append ("]");
	return buf.toString ();
    }


    /**
     * "MutationEvent" implementation.
     */
    public static final class DomMutationEvent extends DomEvent
	implements MutationEvent
    {
	// package private
	Node			relatedNode;	// init

	private String		prevValue;	// init
	private String		newValue;	// init

	private String		attrName;	// init
	private short		attrChange;	// init

	/** Returns any "related" node provided by this type of event */
	final public Node getRelatedNode () { return relatedNode; }
	/** Returns any "previous value" provided by this type of event */
	final public String getPrevValue () { return prevValue; }
	/** Returns any "new value" provided by this type of event */
	final public String getNewValue () { return newValue; }

	/** For attribute change events, returns the attribute's name */
	final public String getAttrName () { return attrName; }
	/** For attribute change events, returns how the attribuet changed */
	final public short getAttrChange () { return attrChange; }

	/** Initializes a mutation event */
	public final void initMutationEvent (
	    String	typeArg,
	    boolean	canBubbleArg,
	    boolean	cancelableArg,
	    Node	relatedNodeArg,
	    String	prevValueArg,
	    String	newValueArg,
	    String	attrNameArg,
	    short	attrChangeArg
	) {
	    // super.initEvent is inlined here for speed
	    // (mutation events are issued on all DOM changes)
	    eventPhase = 0;
	    type = typeArg;
	    bubbles = canBubbleArg;
	    cancelable = cancelableArg;
	    timeStamp = System.currentTimeMillis ();

	    relatedNode = relatedNodeArg;
	    prevValue = prevValueArg;
	    newValue = newValueArg;
	    attrName = attrNameArg;
	    attrChange = attrChangeArg;
	}

	// clear everything that should be GC-able
	void clear ()
	{
	    type = null;
	    target = null;
	    relatedNode = null;
	    currentNode = null;
	    prevValue = newValue = attrName = null;
	}

	/** Constructs an uninitialized mutation event. */
	public DomMutationEvent (String type) { super (type); }
    }


    /**
     * "UIEvent" implementation.
     */
    public static class DomUIEvent extends DomEvent
	implements UIEvent
    {
	private AbstractView	view;		// init
	private int		detail;		// init

	/** Constructs an uninitialized User Interface (UI) event */
	public DomUIEvent (String type) { super (type); }

	final public AbstractView getView () { return view; }
	final public int getDetail () { return detail; }

	/** Initializes a UI event */
	final public void initUIEvent (
	    String		typeArg,
	    boolean		canBubbleArg,
	    boolean		cancelableArg,
	    AbstractView	viewArg,
	    int			detailArg
	) {
	    super.initEvent (typeArg, canBubbleArg, cancelableArg);
	    view = viewArg;
	    detail = detailArg;
	}
    }


    /*

    static final class DomMouseEvent extends DomUIEvent
	implements MouseEvent
    {
	// another half dozen state variables/accessors
    }

    */

}

