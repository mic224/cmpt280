package lib280.tree;

import lib280.base.NDPoint280;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.ItemNotFound280Exception;

public class KDTree280<I extends Comparable<? super I>> {

    /**
     * Construct an empty KDTree280, just for testing purposes.
     */
    public KDTree280() {

    }

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
     * @Precondition all elements in ’list’ are unique
     * @Postcondition all elements smaller than the pivot appear in the leftmost part
     *                of the subarray, then the pivot element, followed by the elements
     *                larger than the pivot. There is no guarantee about the ordering
     *                of the elements before and after the pivot.
     * @return the offset at which the pivot element ended up.
     */
    public int partition(I[] list, int left, int right, int d) {
        return 0;
    }

    /**
     * Find the jth smallest item in an array.
     * @param list array of comparable elements.
     * @param left offset of start of subarray for which we want the median element.
     * @param right offset of end of subarray for which we want the median element.
     * @param j we want to find the element that belongs at array index j.
     * @Precondition left <= j <= right
     * @Precondition all elements in 'list' are unique
     * @Postcondition the element x that belongs at offseet j, if the subarray were
     *                sorted, is at offset j. Elements in the subarray smaller than
     *                x are to the left of offset j and the elements in the subarray
     *                larger than x are to the right of offset j.
     */
    public void jSmallest(I[] list, int left, int right, int j) {

    }

    public static void main(String[] args) {

        // first testing partition method
        System.out.println("\nTesting partition method with non-unique items.");

        // data for the array of points
        double[][] dList = new double[][] {
                {5.0, 2.0},
                {9.0, 10.0},
                {11.0, 1.0},
                {4.0, 3.0},
                {2.0, 12.0},
                {5.0, 2.0},
                {1.0, 5.0}
        };

        NDPoint280[] pointArray = new NDPoint280[dList.length];
        // instantiating a new KDTree for testing purposes.
        for(int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }
        KDTree280 tree = new KDTree280();

        int pTest = 0;
        boolean testing = false;
        try {
            pTest = tree.partition(pointArray, 0, dList.length, 1);
        } catch (DuplicateItems280Exception e) {
            testing = true;
        }
        if(testing) {
            System.out.println("1: PASS");
        } else {
            System.out.println("1: FAIL");
        }

        // Testing partition with unique items.
        dList = new double[][] {
                {5.0, 2.0},
                {9.0, 10.0},
                {11.0, 1.0},
                {4.0, 3.0},
                {2.0, 12.0},
                {3.0, 7.0},
                {1.0, 5.0}
        };

        pointArray = new NDPoint280[dList.length];
        // instantiating a new KDTree for testing purposes.
        for(int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }
        tree = new KDTree280();

        System.out.println("\nTesting partition() on 1st dimension of unique array: ");

        testing = true;
        try {
            pTest = tree.partition(pointArray, 0, dList.length, 1);
        } catch( Exception e) {
            testing = false;
        }

        if(testing) {
            System.out.println("2: PASS");
        } else {
            System.out.println("2: FAIL");
        }

        if(pTest == 0) {
            System.out.println("3: PASS");
        } else {
            System.out.println("3: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length - 1, 1);

        if(pTest == 2) {
            System.out.println("4: PASS");
        } else {
            System.out.println("4: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length - 2, 1);

        if(pTest == 1) {
            System.out.println("5: PASS");
        } else {
            System.out.println("5: FAIL");
        }

        pTest = tree.partition(pointArray, 0, 0, 1);

        if(pTest == 4) {
            System.out.println("6: PASS");
        } else {
            System.out.println("6: FAIL");
        }

        // testing partition on a few elements from 2nd dim.

        System.out.println("\nTesting partition() on 2nd dimension of array: ");

        pTest = tree.partition(pointArray, 0, dList.length, 2);

        if(pTest == 3) {
            System.out.println("7: PASS");
        } else {
            System.out.println("7: FAIL");
        }

        pTest = tree.partition(pointArray, 0, 0, 2);

        if(pTest == 1) {
            System.out.println("8: PASS");
        } else {
            System.out.println("8: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length/2, 2);

        if(pTest == 2) {
            System.out.println("9: PASS");
        } else {
            System.out.println("9: FAIL");
        }

        System.out.println("\nTesting partition() on higher dimension than array: ");

        testing = false;
        try {
            pTest = tree.partition(pointArray, 0, dList.length, 3);
        } catch(ItemNotFound280Exception e) {
            testing = true;
        }

        if(testing) {
            System.out.println("10: PASS");
        } else {
            System.out.println("10: FAIL");
        }

    }
}
