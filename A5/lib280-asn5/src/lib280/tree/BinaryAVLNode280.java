package lib280.tree;

/**
 * Created by michael on 21/02/17.
 */
public class BinaryAVLNode280<I extends Comparable<? super I>> extends BinaryNode280<I> implements Cloneable
{

    protected int leftHeight;
    protected int rightHeight;
    /** The left node. */
    protected BinaryAVLNode280<I> leftNode;

    /** The right node. */
    protected BinaryAVLNode280<I> rightNode;


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

    public void nullifyNode() {
        rightNode = null;
        rightHeight = 0;
        leftNode = null;
        leftHeight = 0;
        setItem(null);
    }

    public void swapNode(BinaryAVLNode280<I> node) {
        rightNode = node.rightNode();
        rightHeight = node.rightHeight();
        leftNode = node.leftNode();
        leftHeight = node.leftHeight();
        item = node.item();
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
