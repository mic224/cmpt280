package lib280.tree;

import lib280.base.NDPoint280;

public class KDTree280<I extends Comparable<? super I>> {

    /**
     * Construct a KDTree from an array of KDPoints.
     * @param pointArray Array of KD points to build the tree from.
     * @param left offset of start of subarray from which to build a kd-tree
     * @param right offset of end of subarray from which to build a kd-tree
     * @param depth the current depth in the partially built tree - note that the root of
     *              a tree has depth 0 and the k dimensions of the points are numbered 0
     *              through k-1.
     */
    public KDTree280(NDPoint280[] pointArray, int left, int right, int depth) {

    }

    /**
     * Partition a subarray using its last element as a pivot
     * @param list array of comparable elements.
     * @param left lower limit on subarray t e partitioned.
     * @param right upper limit on subarray to be partitioned.
     * @return the offset at which the pivot element ended up.
     */
    public int partition(I[] list, int left, int right) {
        return 0;
    }

    /**
     * Find the jth smallest item in an array.
     * @param list array of comparable elements.
     * @param left offset of start of subarray for which we want the median element.
     * @param right offset of end of subarray for which we want the median element.
     * @param j we want to find the element that belongs at array index j.
     */
    public void jSmallest(I[] list, int left, int right, int j) {

    }

    public static void main(String[] args) {
        
    }
}
