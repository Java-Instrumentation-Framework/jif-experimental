package com.holub.tools;

/** This class replaces the Multicaster class that's described in
 * <i>Taming Java Threads</i>. It's better in almost every way
 * (It's smaller, simpler, faster, etc.). The primary difference
 * between this class and the original is that I've based
 * it on a linked-list, and I've used a Strategy object to
 * define how to notify listeners, thereby makeing the interface
 * much more flexible.
 * <p>
 * Publisher class provides an efficient thread-safe means of
 * notifying listeners of an event. The list of listeners can be
 * modified while notifications are in progress, but all listeners
 * that are registered at the time the event occurs are notified (and
 * only those listeners are notified). The ideas in this class are taken
 * from the Java's AWTEventMulticaster class, but I use an (iterative)
 * linked-list structure rather than a (recursive) tree-based structure
 * for my implementation. The observers are notified in the oposite
 * order that they were added.
 * <p>
 * Here's an example of how you might use it:
 * <pre>
class Event_generator
{	interface Listener
	{	notify( String why );
	}

	private Publisher publisher = new Publisher();

	public void addEventListener( Listener l )
	{	publisher.subscribe(l);
	}

	public void removeEventListener ( Listener l )
	{	publisher.cancel_subscription(l);
	}


	public void some_event_has_happend(final String reason)
	{	publisher.publish
		(	new Publisher.Distributor()
			{	public void deliver_to( Object subscriber )
				{	((Listener)subscriber).notify(reason);
				}
			}
		);
	}
}
 * </pre>
 * Since you're specifying what a notification looks like
 * by defining a Listener interface, and then also defining
 * the message passing symantics (inside the Distributer derivative),
 * you have complete control over what the interface looks like.
 */

public class Publisher
{	
	public interface Distributor
	{	void deliver_to( Object subscriber );
	}

	// The Node class is immutable. Once it's created, it can't
	// be modified. Immutable classes have the property that, in
	// a multithreaded system, access to the does not have to be
	// synchronized, because they're read only.
	//
	// This particular class is really a struct so I'm allowing direct
	// access to the fields. Since it's private, I can play
	// fast and loose with the encapsulation without significantly
	// impacting the maintainability of the code.

	private class Node
	{	public final Object subscriber;
		public final Node	next;

		private Node( Object subscriber, Node next )
		{	this.subscriber	= subscriber;
			this.next		= next;
		}

		public Node remove( Object target )
		{	if( next == null 		 )
				throw new java.util.NoSuchElementException
												(target.toString());
			if( target == subscriber )
				return next;

			return new Node(subscriber, next.remove(target));
		}
	}

	private Node head = null;

	public void publish( Distributor delivery_agent )
	{	for(Node cursor = head; cursor != null; cursor = cursor.next)
			delivery_agent.deliver_to( cursor.subscriber );
	}

	public void subscribe( Object subscriber )
	{	head = new Node( subscriber, head );
	}

	public void cancel_subscription( Object subscriber )
	{	head = head.remove( subscriber );
	}

	//------------------------------------------------------------------
	public static class Test
	{	
		interface EventListener
		{	void event1();
			void event2( String arg );
		}

		static class Event_notifier implements EventListener
		{	private Publisher publisher = new Publisher();

			public void addEventListener( EventListener l )
			{	publisher.subscribe(l);
			}

			public void removeEventListener ( EventListener l )
			{	publisher.cancel_subscription(l);
			}

			public void event1()
			{	publisher.publish
				(	new Publisher.Distributor()
					{	public void deliver_to( Object subscriber )
						{	((EventListener)subscriber).event1();
						}
					}
				);
			}

			public void event2( final String arg )
			{	publisher.publish
				(	new Publisher.Distributor()
					{	public void deliver_to( Object subscriber )
						{	((EventListener)subscriber).event2(arg);
						}
					}
				);
			}
		}

		public static void main( String[] args )
		{
			Event_notifier source = new Event_notifier();

			EventListener listener1 = new EventListener()
			{	public void event1()
				{	System.out.println("Listener 1");
				}
				public void event2(String arg)
				{	System.out.println("Listener 1: " + arg);
				}
			};

			EventListener listener2 = new EventListener()
			{	public void event1()
				{	System.out.println("Listener 2");
				}
				public void event2(String arg)
				{	System.out.println("Listener 2: " + arg);
				}
			};

			EventListener listener3 = new EventListener()
			{	public void event1()
				{	System.out.println("Listener 3");
				}
				public void event2(String arg)
				{	System.out.println("Listener 3: " + arg);
				}
			};

			source.addEventListener( listener1 );
			source.addEventListener( listener2 );
			source.addEventListener( listener3 );

			source.event1();
			source.event2("arg");

			source.removeEventListener( listener1 );
			source.event1();

			source.removeEventListener( listener3 );
			source.event1();

			source.removeEventListener( listener2 );
			source.event1();

			source.removeEventListener( listener2 );
			source.event1();
		}
	}
}
