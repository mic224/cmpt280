// Assignment #2
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

package lib280.tree;

import lib280.base.Cursor280;
import lib280.base.CursorPosition280;
import lib280.dictionary.Dict280;
import lib280.exception.*;

public class ArrayedBinaryTreeWithCursors280<I> extends
        ArrayedBinaryTree280<I> implements Dict280<I>, Cursor280<I> {

    protected boolean searchesRestart;

    public ArrayedBinaryTreeWithCursors280(int cap) {
        super(cap);
        searchesRestart = true;
    }

    @Override
    public I obtain(I y) throws ItemNotFound280Exception {
        CursorPosition280 saved = this.currentPosition();
        this.goFirst();
        while (this.itemExists()) {
            if (membershipEquals(this.item(), y)) {
                I found = this.item();
                this.goPosition(saved);
                return found;
            }
            this.goForth();
        }
        this.goPosition(saved);
        throw new ItemNotFound280Exception("The given item could not be found.");
    }

    @Override
    public void delete(I x) throws ItemNotFound280Exception {
        if(this.isEmpty()) {
            throw new ItemNotFound280Exception("empty");
        } else {
            this.goFirst();
            while(!this.after()) {
                if (this.items[this.currentNode] == x) {
                    this.items[this.currentNode] = this.items[this.capacity()];
                }
                if(this.items[this.capacity()] == x) {
                    this.items[this.currentNode] = null;
                }
                this.goForth();
            }
        }
    }

    @Override
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        if (this.isEmpty()) {
            this.items[0] = x;
        } else if (this.isFull()) {
            throw new ContainerFull280Exception("The tree is full");
        } else {
            this.goFirst();
            while (!this.after()) {
                if (this.items[this.currentNode] == x) {
                    throw new DuplicateItems280Exception("duplicate");
                } else if (this.items[this.currentNode] == null) {
                    this.items[this.currentNode] = x;
                }
            }
        }
    }

    @Override
    public boolean has(I y) {
        this.goFirst();
        while (!this.after()) {
            if (this.items[this.currentNode] == y) {
                return true;
            }
            this.goForth();
        }
        return false;
    }

    @Override
    public void search(I x) {

    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        if(this.items[this.currentNode] == null) {
            throw new NoCurrentItem280Exception("no item");
        } else {
            this.items[this.currentNode] = null;
        }
    }

    @Override
    public void goAfter() {
        this.currentNode = this.capacity + 1;
    }

    public void goBefore() {
        this.currentNode = 0;
    }

    public void goFirst() throws ContainerEmpty280Exception {
        this.currentNode = 1;
    }

    public void goForth() throws AfterTheEnd280Exception {
        this.currentNode++;
    }

    public boolean after() {

        if(this.currentNode == this.capacity() + 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean before() {
        if(this.currentNode == 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean membershipEquals(I x, I y) {
        return x.equals(y);
    }

    @Override
    public void restartSearches() {
        this.searchesRestart = true;
    }

    @Override
    public void resumeSearches() {
        this.searchesRestart = false;
    }

    @Override
    public CursorPosition280 currentPosition() {
        return new ArrayedBinaryTreePosition280(this.currentNode);
    }

    @Override
    public void goPosition(CursorPosition280 c) {
        if (!(c instanceof ArrayedBinaryTreePosition280))
            throw new InvalidArgument280Exception("The cursor position parameter"
                    + " must be a ArrayedBinaryTreePosition280<I>");

        this.currentNode = ((ArrayedBinaryTreePosition280) c).currentNode;
    }

    /**
     * Move the cursor to the parent of the current node.
     *
     * @throws InvalidState280Exception when the cursor is on the root already.
     * @precond Current node is not the root.
     */
    public void parent() throws InvalidState280Exception {
        if(this.currentNode == 1) {
            throw new InvalidState280Exception("already at root node");
        } else {
            this.currentNode = (this.currentNode - 1)/2;
        }
    }

    /**
     * Move the cursor to the left child of the current node.
     *
     * @throws ContainerEmpty280Exception if the tree is empty.
     * @throws InvalidState280Exception   if the current node has no left child.
     * @precond The tree must not be empty and the current node must have a left child.
     */
    public void goLeftChild() throws InvalidState280Exception, ContainerEmpty280Exception {
        if(this.isEmpty()) {
            throw new ContainerEmpty280Exception("tree empty");
        } else if(this.items[this.currentNode * 2] == null) {
            throw new InvalidState280Exception("Node has no left child");
        } else {
            this.currentNode *= 2;
        }
    }

    /**
     * Move the cursor to the right child of the current node.
     *
     * @throws ContainerEmpty280Exception if the tree is empty.
     * @throws InvalidState280Exception   if the current item has no right child.
     * @precond The tree must not be empty and the current node must have a right child.
     */
    public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {
        if(this.isEmpty()) {
            throw new ContainerEmpty280Exception("tree empty");
        } else if(this.items[(this.currentNode * 2) + 1] == null) {
            throw new InvalidState280Exception("Node has no left child");
        } else {
            this.currentNode = (this.currentNode * 2) + 1;
        }
    }

    /**
     * Move the cursor to the sibling of the current node.
     *
     * @throws ContainerEmpty280Exception if the tree is empty.
     * @throws InvalidState280Exception   if the current item has no sibling.
     * @precond The current node must have a sibling.  The tree must not be empty.
     */
    public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
        // todo
    }

    /**
     * Move the cursor to the root of the tree.
     *
     * @throws ContainerEmpty280Exception if the tree is empty.
     * @precond The tree must not be empty.
     */
    public void root() throws ContainerEmpty280Exception {
        if(this.isEmpty()) {
            throw new ContainerEmpty280Exception("tree empty");
        }
    }


    public static void main(String[] args) {
        ArrayedBinaryTreeWithCursors280<Integer> T = new ArrayedBinaryTreeWithCursors280<Integer>(10);

        // IsEmpty on empty tree.
        if (!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");

        // Test root() on empty tree.
        Exception x = null;
        try {
            T.root();
        } catch (ContainerEmpty280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to root of empty tree.  Got none.");
        }

        // test goFirst() on empty tree
        x = null;
        try {
            T.goFirst();
        } catch (ContainerEmpty280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to first elelement of empty tree.  Got none.");
        }


        // Test goLeftChild() on empty tree.
        x = null;
        try {
            T.goLeftChild();
        } catch (ContainerEmpty280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
        }

        // Test goLeftChild() on empty tree.
        x = null;
        try {
            T.goRightChild();
        } catch (ContainerEmpty280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
        }


        // Check itemExists on empty tree
        if (T.itemExists()) System.out.println("itemExists() returned true on an empty tree.");

        // Insert on empty tree.
        T.insert(1);

        // Check ItemExists on tree with one element.
        T.root();
        if (!T.itemExists())
            System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");

        // isEmpty on tree with 1 element.
        if (T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

        // Insert on tree with 1 element
        T.insert(2);

        // Insert some more elements
        for (int i = 3; i <= 10; i++) T.insert(i);

        if (T.count() != 10) System.out.println("Expected tree count to be 10, got " + T.count());


        // Test for isFull on a full tree.
        if (!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");

        // Test insert on a full tree
        x = null;
        try {
            T.insert(11);
        } catch (ContainerFull280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception inserting into a full tree.  Got none.");
        }


        // Test positioning methods

        // Test root()
        T.root();
        if (T.item() != 1) System.out.println("Expected item at root to be 1, got " + T.item());

        T.goLeftChild();

        if (T.item() != 2) System.out.println("Expected current item to be 2, got " + T.item());

        T.goRightChild();
        if (T.item() != 5) System.out.println("Expected current item to be 5, got " + T.item());


        T.goLeftChild();
        if (T.item() != 10) System.out.println("Expected current item to be 10,  got " + T.item());

        // Current node now has no children.
        x = null;
        try {
            T.goLeftChild();
        } catch (InvalidState280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
        }

        x = null;
        try {
            T.goRightChild();
        } catch (InvalidState280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
        }

        // Remove the last item ( a leaf)
        T.deleteItem();
        if (T.item() != 9) System.out.println("Expected current item to be 9, got " + T.item());

        T.parent();


        // Remove a node with 2 children.  The right child 9 gets promoted.
        T.deleteItem();
        if (T.item() != 9) System.out.println("Expected current item to be 9, got " + T.item());


        // Remove a node with 1 child.  The left child 8 gets promoted.
        T.deleteItem();
        if (T.item() != 8) System.out.println("Expected current item to be 8, got " + T.item());

        // Remove the root successively.  There are 7 items left.
        T.root();
        T.deleteItem();
        if (T.item() != 7) System.out.println("Expected root to be 7, got " + T.item());

        T.deleteItem();
        if (T.item() != 6) System.out.println("Expected root to be 6, got " + T.item());

        T.deleteItem();
        if (T.item() != 5) System.out.println("Expected root to be 5, got " + T.item());

        T.deleteItem();
        if (T.item() != 8) System.out.println("Expected root to be 8, got " + T.item());

        T.deleteItem();
        if (T.item() != 3) System.out.println("Expected root to be 3, got " + T.item());

        T.deleteItem();
        if (T.item() != 2) System.out.println("Expected root to be 2, got " + T.item());

        // Tree has one item.  Try parent() on one item.
        x = null;
        try {
            T.parent();
        } catch (InvalidState280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
        }


        // Try to go to the sibling
        x = null;
        try {
            T.goSibling();
        } catch (InvalidState280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
        }


        T.deleteItem();


        // Tree should now be empty
        if (!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

        if (T.capacity() != 10) System.out.println("Expected capacity to be 10, got " + T.capacity());

        if (T.count() != 0) System.out.println("Expected tree count to be 0, got " + T.count());

        // Remove from empty tree.
        x = null;
        try {
            T.deleteItem();
        } catch (NoCurrentItem280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception deleting from an empty tree.  Got none.");
        }


        // Try to go to the sibling
        x = null;
        try {
            T.goSibling();
        } catch (ContainerEmpty280Exception e) {
            x = e;
        } finally {
            if (x == null) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
        }


        T.insert(1);
        T.root();

        // Try to go to the sibling when there is no child.
        x = null;
        try {
            T.goSibling();
        } catch (InvalidState280Exception e) {
            x = e;
        } finally {
            if (x == null)
                System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
        }

        T.goBefore();
        if (!T.before()) System.out.println("Error: Should be in 'before' position, but before() reports otherwise.");
        if (T.after())
            System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

        T.goForth();
        if (T.before())
            System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
        if (T.after())
            System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

        T.goForth();
        if (!T.after()) System.out.println("Error: Should be in 'after' position, but after() reports otherwise.");
        if (T.before())
            System.out.println("Error: T.before() reports cursor in the before position when it should not be.");

        x = null;
        try {
            T.goForth();
        } catch (AfterTheEnd280Exception e) {
            x = e;
        } finally {
            if (x == null)
                System.out.println("Expected exception advancing cursor when already after the end.  Got none.");
        }


        int y = -1;
        T.goBefore();
        try {
            y = T.obtain(1);
        } catch (ItemNotFound280Exception e) {
            System.out.println("Error: Unexpected exception occured when attempting T.obtain(1).");
        } finally {
            if (y != 1) System.out.println("Obtained item should be 1 but it isn't.");
            if (!T.before())
                System.out.println("Error: cursor should still be in the before() position after T.obtain(1), but it isn't.");
        }

        if (!T.has(1)) System.out.println("Error: Tree has element 1, but T.has(1) reports that it does not.");


        System.out.println("Regression test complete.");
    }
}
