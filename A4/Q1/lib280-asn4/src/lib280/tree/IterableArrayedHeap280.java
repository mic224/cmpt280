package lib280.tree;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I>  {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
	}

	// TODO
	// Add iterator() and deleteAtPosition() methods here.


	public void deleteAtPosition(ArrayedBinaryTreeIterator280 iter) {
		// Delete the root by moving in the last item.
		// If there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.
		iter.tree.isEmpty();
		if( !iter.tree.isEmpty() ) {
			iter.currentNode = 1;
			iter.tree.items[iter.currentNode] = iter.tree.items[iter.tree.count()];
		}
		iter.tree.count--;

		// If we deleted the last remaining item, make the the current item invalid, and we're done.
		if( iter.tree.count == 0 ) {
			iter.currentNode = 0;
			return;
		}

		// Propagate the new root down the lib280.tree.
		int n = 1;

		// While offset n has a left child...
		while( iter.tree.findLeftChild(n) <= iter.tree.count ) {
			// Select the left child.
			int child = iter.tree.findLeftChild(n);

			// If the right child exists and is larger, select it instead.
			if( child + 1 <= iter.tree.count )
				child++;

			// If the parent is smaller than the root...
		}



//		while( findLeftChild(n) <= count ) {
//			// Select the left child.
//			int child = findLeftChild(n);
//
//			// If the right child exists and is larger, select it instead.
//			if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 )
//				child++;
//
//			// If the parent is smaller than the root...
//			if( items[n].compareTo(items[child]) < 0 ) {
//				// Swap them.
//				I temp = items[n];
//				items[n] = items[child];
//				items[child] = temp;
//				n = child;
//			}
//			else return;
//
//		}
	}

	/**
	 * Helper for the regression test.  Verifies the heap property for all nodes.
	 */
	private boolean hasHeapProperty() {
		for(int i=1; i <= count; i++) {
			if( findRightChild(i) <= count ) {  // if i Has two children...
				// ... and i is smaller than either of them, , then the heap property is violated.
				if( items[i].compareTo(items[findRightChild(i)]) < 0 ) return false;
				if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
			}
			else if( findLeftChild(i) <= count ) {  // if n has one child...
				// ... and i is smaller than it, then the heap property is violated.
				if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
			}
			else break;  // Neither child exists.  So we're done.
		}
		return true;
	}

	/**
	 * Regression test
	 */
	public static void main(String[] args) {

		IterableArrayedHeap280<Integer> H = new IterableArrayedHeap280<Integer>(10);

		// Empty heap should have the heap property.
		if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

		// Insert items 1 through 10, checking after each insertion that
		// the heap property is retained, and that the top of the heap is correctly i.
		for(int i = 1; i <= 10; i++) {
			H.insert(i);
			if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
			if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
		}

		// Remove the elements 10 through 1 from the heap, chekcing
		// after each deletion that the heap property is retained and that
		// the correct item is at the top of the heap.
		for(int i = 10; i >= 1; i--) {
			// Remove the element i.
			H.deleteItem();
			// If we've removed item 1, the heap should be empty.
			if(i==1) {
				if( !H.isEmpty() ) System.out.println("Expected the heap to be empty, but it wasn't.");
			}
			else {
				// Otherwise, the item left at the top of the heap should be equal to i-1.
				if(H.item() != i-1) System.out.println("Expected current item to be " + i + ", got " + H.item());
				if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
			}
		}

		System.out.println("Regression Test Complete.");
	}
}
