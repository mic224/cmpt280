// Assignment #6
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

package lib280.tree;

import lib280.base.NDPoint280;

public class KDNode280 implements Cloneable, Comparable<KDNode280> {

    NDPoint280 item;

    /** The left node. */
    protected KDNode280 leftNode;

    /** The right node. */
    protected KDNode280 rightNode;

    /** Construct a new node with item x.
     *  @timing Time = O(1)
     *  @param x the item placed in the new node */
    public KDNode280(NDPoint280 x)
    {
        this.item = x;
    }

    /** Contents of the node.
     *  @timing Time = O(1) */
    public NDPoint280 item()
    {
        return item;
    }

    /** The left node.
     *  @timing Time = O(1) */
    public KDNode280 leftNode()
    {
        return leftNode;
    }

    /** The left node.
     *  @timing Time = O(1) */
    public KDNode280 rightNode()
    {
        return rightNode;
    }

    /**
     * Set the item contained in the node.
     * @param x The new item to store in the node.
     * @timing Time = O(1)
     * */
    public void setItem(NDPoint280 x) {
        this.item = x;
    }

    /**
     * Set the left child of this node
     * @param n The new left child of this node.
     */
    public void setLeftNode(KDNode280 n) {
        this.leftNode = n;
    }

    /**
     * Set the right child of this node.
     * @param n The new right child of this node.
     */
    public void setRightNode(KDNode280 n) {
        this.rightNode = n;
    }

    /**
     * Returns a string representation of the item contained within the node.
     */
    public String toString() {
        return this.item.toString();
    }

    /**
     * Shallow clone of this node.
     */
    @SuppressWarnings("unchecked")
    public KDNode280 clone() {
        try {
            return (KDNode280)super.clone();
        }
        catch(CloneNotSupportedException e)
        {
			/*	Should not occur because Container280 extends Cloneable */
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(KDNode280 x) {
        return this.item.compareTo(x.item);
    }


}

