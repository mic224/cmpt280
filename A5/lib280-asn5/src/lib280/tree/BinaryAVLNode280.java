// Assignment #5
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

package lib280.tree;

public class BinaryAVLNode280<I extends Comparable<? super I>> extends BinaryNode280<I> implements Cloneable
{
    /** The height of the left subtree */
    protected int leftHeight;
    /** The height of the right subtree */
    protected int rightHeight;

    /** The left node. */
    protected BinaryAVLNode280<I> leftNode;

    /** The right node. */
    protected BinaryAVLNode280<I> rightNode;

    /** get the data stored in the node */
    public I item()
    {
       return item;
    }

    public BinaryAVLNode280(I x)
    {
        super(x);
    }

    public BinaryAVLNode280<I> leftNode()
    {
        return leftNode;
    }

    public BinaryAVLNode280<I> rightNode()
    {
        return rightNode;
    }

    public int leftHeight() {
        return leftHeight;
    }

    public int rightHeight() {
        return rightHeight;
    }

    public void setLeftHeight(int x) {
        leftHeight = x;
    }

    /** return the imbalance of the tree based on its childrens heights */
    public int getImbalance() {
        return Math.abs(leftHeight-rightHeight);
    }

    /** returns the height of the current node */
    public int getMaxHeight() {
        return Math.max(leftHeight,rightHeight) + 1;
    }
    /**
     * Set the left child of this node
     * @param n The new left child of this node.
     */
    public void setLeftNode(BinaryNode280<I> n) {
        this.leftNode = (BinaryAVLNode280<I>)n;
    }

    /**
     * Set the right child of this node.
     * @param n The new right child of this node.
     */
    public void setRightNode(BinaryNode280<I> n) {
        this.rightNode = (BinaryAVLNode280<I>)n;
    }

    public void setRightHeight(int x) {
        rightHeight = x;
    }

    public String toString() {
        return this.item.toString();
    }

    /**
     * Shallow clone of this node.
     */
    @SuppressWarnings("unchecked")
    public BinaryAVLNode280<I> clone() {
        return (BinaryAVLNode280<I>)super.clone();
    }
}
