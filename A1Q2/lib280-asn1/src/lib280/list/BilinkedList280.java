package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		BilinkedNode280<I> newItem = createNewNode(x);
		if( !this.isEmpty() ) {
			BilinkedNode280<I> nextItem = (BilinkedNode280<I>)this.head;
			nextItem.setPreviousNode(newItem);
			newItem.setNextNode(nextItem);
		}

		// If the cursor is at the first node, cursor predecessor becomes the new node.
		if( this.position == this.head ) this.prevPosition = newItem;

		// Special case: if the list is empty, the new item also becomes the tail.
		if( this.isEmpty() ) this.tail = newItem;
		this.head = newItem;
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
		BilinkedNode280<I> newTailNode = createNewNode(x);

		if(!this.isEmpty()) {
			newTailNode.setPreviousNode((BilinkedNode280) this.tail);

			this.tail.nextNode = newTailNode;
			this.tail = newTailNode;
		} else {
			this.insertFirst(x);
		}

	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		// TODO
	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		// TODO
	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		// TODO
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		// TODO
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// TODO
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		BilinkedList280<Integer> L = new BilinkedList280<>();

		System.out.println("\nTesting Instantiation of BilinkedList");
		System.out.println("List: " + L);
		if( L.isEmpty() ) System.out.println("\tPASS: list empty.");
		else System.out.println("\tFAIL: list should be empty.");

		L.insertFirst(2);

		System.out.println("\nTesting insertFirst()");
		System.out.println("List: " + L);

		if(L.isEmpty()) System.out.println("\tFAIL: list is empty.");
		else System.out.println("\tPASS: list not empty.");

		if(L.isEmpty()) System.out.println("\tFAIL: list is empty.");
		else
		{
			if(L.firstItem() == 2) System.out.println("\tPASS: first item in list is 2.");
			else System.out.println("\tFAIL: first item should be 2");

			if(L.head.item() == 2) System.out.println("\tPASS: head item in list is 2.");
			else System.out.println("\tFAIL: head item should be 2");

			if(L.tail.item() == 2) System.out.println("\tPASS: tail item in list is 2.");
			else System.out.println("\tFAIL: tail item should be 2");

		}

		L.insert(6);
		L.insert(5);

		L.insertLast(40);
		L.insertLast(10);

		System.out.println("\nTesting insertLast()");
		System.out.println("List: " + L);

		if(L.lastItem() == 10) System.out.println("\tPASS: last item in list is 10");
		else System.out.println("\tFAIL: last item should be 10");

		BilinkedNode280<Integer> temp = (BilinkedNode280<Integer>)L.lastNode();

		if(temp.previousNode().item() == 40) System.out.println("\tPASS: last items previous node value is 2");
		else System.out.println("\tFAIL: last items previous node value should be 2");

		L.insert(44);
		L.insert(200);

		System.out.println("\nTesting deleteItem()");
		System.out.println("List: " + L);
		L.goFirst();
		if(!L.after()) {
			L.goForth();
			L.deleteItem();
		}
		if(L.position != null) System.out.println("\tPASS: cursor position node not null.");
		else {
			if(L.currentPosition().item() == 5) System.out.println("\tPASS: current cursor item is 5");
			else System.out.println("\tFAIL: current cursor item should be 5.");

			if(L.prevPosition.item() == 200) System.out.println("\tPASS: prevPosition is 200.");
			else System.out.println("\tFAIL: prevPosition should be 200.");
		}

		L.goFirst();
		L.deleteItem();
		if(L.firstItem() == 5) System.out.println("\tPASS: firstItem is now 5.");
		else System.out.println("\tFAIL: firstItem should now be 5.");

		if(L.head.item() == 5) System.out.println("\tPASS: head item is now 5.");
		else System.out.println("\tFAIL: head item should now be 5.");

		L.goLast();
		L.deleteItem();
		if(L.lastItem() == 40) System.out.println("\tPASS: lastItem is now 40.");
		else System.out.println("\tFAIL: lastItem should now be 40.");

		if(L.tail.item() == 40) System.out.println("\tPASS: tail item is now 40.");
		else System.out.println("\tFAIL: tail item should be 40.");

	}
} 
