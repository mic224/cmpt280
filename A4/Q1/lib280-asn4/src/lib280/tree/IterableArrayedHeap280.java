// Assignment #4
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

package lib280.tree;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

    /**
     * Create an iterable heap with a given capacity.
     *
     * @param cap The maximum number of elements that can be in the heap.
     */
    public IterableArrayedHeap280(int cap) {
        super(cap);
    }

    public void deleteAtPosition(ArrayedBinaryTreeIterator280 iter) {

        if (this.count > 1) { // tree has more than 1 item

            if(iter.currentNode > 1) { // not pointing at the root follow algorith.
                I itemToDelete = this.items[iter.currentNode];

                // 1 swap the root with the item of interest
                this.currentNode = 1;
                this.items[iter.currentNode] = this.items[currentNode];
                this.items[currentNode] = itemToDelete;

                // 2 swap root(in new position) with last item
                itemToDelete = this.items[iter.currentNode];
                this.items[iter.currentNode] = this.items[this.count];
                this.items[this.count] = itemToDelete;

                // 3 delete root
                this.deleteItem();

                // 4 propagate last node(in new spot) down the heap.
                while (findLeftChild(iter.currentNode) <= count) {
                    // Select the left child.
                    int child = findLeftChild(iter.currentNode);

                    // If the right child exists and is larger, select it instead.
                    if (child + 1 <= count && items[child].compareTo(items[child + 1]) < 0)
                        child++;

                    // If the parent is smaller than the root...
                    if (items[iter.currentNode].compareTo(items[child]) < 0) {
                        // Swap them.
                        I temp = items[iter.currentNode];
                        items[iter.currentNode] = items[child];
                        items[child] = temp;
                        iter.currentNode = child;
                    } else return;

                }
            } else {
                // cursor is pointing at the root; just delete the root.
                this.deleteItem();
            }
        } else {
            // heap only has 1 item left; delete it
            this.deleteItem();
        }
    }

    public ArrayedBinaryTreeIterator280<I> iterator() {
        return new ArrayedBinaryTreeIterator280<I>(this);
    }


    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */

    private boolean hasHeapProperty() {
        for (int i = 1; i <= count; i++) {
            if (findRightChild(i) <= count) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if (items[i].compareTo(items[findRightChild(i)]) < 0) return false;
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else if (findLeftChild(i) <= count) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        IterableArrayedHeap280<Integer> H = new IterableArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for (int i = 1; i <= 10; i++) {
            H.insert(i);
            if (H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        // Remove the elements 10 through 1 from the heap, chekcing
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for (int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if (i == 1) {
                if (!H.isEmpty()) System.out.println("Expected the heap to be empty, but it wasn't.");
            } else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if (H.item() != i - 1) System.out.println("Expected current item to be " + i + ", got " + H.item());
                if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("Regression Test Complete.");
    }
}
