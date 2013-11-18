package com.holub.tools;

import java.util.EventObject;
import com.holub.tools.Command;

/**	
 *	The <code>Multicaster</code> class (modeled after the <code>AWTEventMulticaster</code>)
 *	provides an efficient way to manage relatively short lists
 *	of "subscribers." Each <code>Multicaster</code> object can reference two
 *	"Subscriber" objects, one or both of which can be another
 *	<code>Multicaster</code>. The top-level <code>Multicaster</code> is passed a "publish"
 *	message, which it broadcasts (recursively) to both of the
 *	(<code>Command</code>-class) subscribers that it references.
 *	<p>
 *	The <code>Multicaster</code> is an "immutable" object, so you can't modify it.
 *	The "add()" method, for example, is passed two
 *	<code>Multicaster</code>s and returns a third one that effectively references
 *	all the subscribers referenced by the original two. Any notifications
 *	that are in progress when the add() is fired will not be affected
 *	by the operation, however. It is perfectly acceptable for
 *	notifications to be performed on one thread while a second thread
 *	is adding or removing members from the Multicaster.
 *
 *	The order in which Subscribers are notified is undefined. (It is
 *	NOT necessarily the order of insertion.)
 *
 * <table border=1 cellspacing=0 cellpadding=5><tr><td><font size=-1><i>
 * <center>(c) 2000, Allen I. Holub.</center>
 * <p>
 * This code may not be distributed by yourself except in binary form,
 * incorporated into a java .class file. You may use this code freely
 * for personal purposes, but you may not incorporate it into any
 * commercial product without
 * the express written permission of Allen I. Holub.
 * </font></i></td></tr></table>
 *
 * @see java.awt.AWTEventMulticaster
 * @see Command
 *
 * @deprecated as of ver. 1.4. Use {@link com.holub.tools.Publisher}, which is
 * smaller, more efficient, and gives you control over how you notify subscribers.
 * (Publisher lets you pass arbitrary arguments to subscribers, for example.)
 *
 * @author Allen I. Holub
 */

public class Multicaster implements Command
{
	private final Command a, b;

    private Multicaster(Command a, Command b)
    {	this.a = a;
    	this.b = b;
    }

	/******************************************************************
	 * Ask the subscribers of this multicaster to receive the
	 * <code>publication</code>. This is the "publish" operation, as
	 * seen from the perspective of a subscriber. (Remember, a multicaster
	 * is a list of subscribers.
	 *
	 * Note that the order of traversal should generally be considered
	 * undefined.
	 * However, if you really need to notify listeners in a known
	 * order, and you consistently add nodes as follows:
	 * <pre>
	 *		subscription_list = Multicaster.add( subscription_list, new_node );
	 * </pre>
	 * (Where <code>subscription_list</code> is the head-of-list reference and
	 * <code>new_node</code> is the node you're adding), subscribers  will
	 * be notified in the order they were added. Removing nodes does not
	 * affect the order of notification. If you transpose the
	 * two arguments in the foregoing code:
	 * <pre>
	 *		subscription_list = Multicaster.add( new_node, subscription_list );
	 * </pre>
	 * subscribers will be notified in reverse order.
	 */

	public void fire( Object publication )
	{	a.fire( publication );
		b.fire( publication );
	}

	/******************************************************************
	 * Add a new subscriber to the list. The way this call is used
	 * can impact the order in which subscribers are notified. (See
	 * {@link fire}.)
	 * Note that you must use proper synchronization
	 * around the call to add to prevent preemption after add returns,
	 * but before its return value is assigned to the head-of-list
	 * reference.
	 *
	 * @param a	Typically the head-of-list pointer.
	 * @param b	Typically the subscriber you're adding to the list.
	 */
    public static Command add(Command a, Command b)
    {	return  (a == null)  ? b :
				(b == null)  ? a : new Multicaster(a, b);
    }

	/******************************************************************
	 * Remove the indicated subscriber from the list. As with add,
	 * you must use proper synchronization
	 * around the call to remove() to prevent preemption after remove
	 * returns, but before its return value is assigned to the head-of-list
	 * reference.
	 */
    public static Command remove(Command list, Command remove_me)
    {
		if( list == remove_me || list == null  )
			return null;
		else if( !(list instanceof Multicaster) )
			return list;
		else
			return ((Multicaster)list).remove( remove_me );
    }

    private Command remove(Command remove_me)
    {
		if (remove_me == a)  return b;
		if (remove_me == b)  return a;

		Command a2 = remove( a, remove_me );
		Command b2 = remove( b, remove_me );

		return (a2 == a && b2 == b ) // it's not here
				? this
				: add(a2, b2)
				;
    }
	//================================================================
	public static class Test
	{	private static class Leaf implements Command
		{	String s;
			public Leaf(String s){ this.s = s; }
			public void fire( Object publication )	
			{	System.out.print(s);
			}
		}

		public static void main( String[] args )
		{	Leaf a = new Leaf("A");
			Leaf b = new Leaf("B");
			Leaf c = new Leaf("C");
			Leaf d = new Leaf("D");
			Leaf e = new Leaf("E");

			Command subscription_list = null;
			subscription_list = Multicaster.add( subscription_list, a );
			subscription_list = Multicaster.add( subscription_list, b );
			subscription_list = Multicaster.add( subscription_list, c );
			subscription_list = Multicaster.add( subscription_list, d );
			subscription_list = Multicaster.add( subscription_list, e );

			System.out.print("List is: ");
			subscription_list.fire( null );

			System.out.print("\nRemoving c: ");
			subscription_list = Multicaster.remove( subscription_list, c );
			subscription_list.fire( null );

			System.out.print("\nRemoving a: ");
			subscription_list = Multicaster.remove( subscription_list, a );
			subscription_list.fire( null );

			System.out.print("\nRemoving d: ");
			subscription_list = Multicaster.remove( subscription_list, d );
			subscription_list.fire( null );

			System.out.print("\nRemoving b: ");
			subscription_list = Multicaster.remove( subscription_list, b );
			subscription_list.fire( null );

			System.out.print("\nRemoving e: ");
			subscription_list = Multicaster.remove( subscription_list, e );

			if( subscription_list != null )
				System.out.println("Couldn't remove last node");

			subscription_list = Multicaster.add( a, subscription_list );
			subscription_list = Multicaster.add( b, subscription_list );
			subscription_list = Multicaster.add( c, subscription_list );
			subscription_list = Multicaster.add( d, subscription_list );
			subscription_list = Multicaster.add( e, subscription_list );

			System.out.print("\nShould be: EDCBA: " );
			subscription_list.fire( null );

			System.out.println();
		}
	}
}
