package lib280.tree;

import lib280.base.Container280;
import lib280.base.NDPoint280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.InvalidArgument280Exception;

public class KDTree280 implements Container280 {

    protected KDNode280 rootNode;
    protected int dimensionality;

    /**
     * Construct an empty KDTree280.
     */
    public KDTree280() {
        rootNode = null;
        dimensionality = 0;
    }

    /**
     * Construct a KDTree from an array of KDPoints.
     *
     * @param pointArray Array of KD points to build the tree from.
     * @param k          The dimensionality of the array of points.
     */
    public KDTree280(NDPoint280[] pointArray, int k) {
        //store the dimensionality of the tree.
        dimensionality = k;
        //start the recursion
        setRootNode(buildTree(pointArray, 0, pointArray.length - 1, 0, k));
    }

    /**
     * Construct a KDTree from an array of KDPoints.
     *
     * @param pointArray Array of KD points to build the tree from.
     * @param left       offset of start of subarray from which to build a kd-tree
     * @param right      offset of end of subarray from which to build a kd-tree
     * @param depth      the current depth in the partially built tree - note that the root of
     *                   a tree has depth 0 and the k dimensions of the points are numbered 0
     *                   through k-1.
     */
    protected KDNode280 buildTree(NDPoint280[] pointArray, int left, int right, int depth, int k) {
        if ((right - left) < 0) {
            return null;
        } else {
//      Select axis based on depth so that axis cycles through all valid values. (k is the
//      dimensionality of the tree).
            int d = depth % (k + 1);
            int medianOffset = (left + right) / 2;

//      put the median element in the correct position this call assumes you have added
//      the dimension d parameter to jSmallest.
            jSmallest(pointArray, left, right, medianOffset, d);

//      Create node and construct subtrees.
            KDNode280 node = new KDNode280(pointArray[medianOffset]);
            node.setLeftNode(this.buildTree(pointArray, left, medianOffset - 1, depth + 1, dimensionality));
            node.setRightNode(this.buildTree(pointArray, medianOffset + 1, right, depth + 1, dimensionality));
            return node;
        }
    }

    /**
     * Partition a subarray using its last element as a pivot
     *
     * @param list  array of comparable elements.
     * @param left  lower limit on subarray t e partitioned.
     * @param right upper limit on subarray to be partitioned.
     * @return the offset at which the pivot element ended up.
     * @Precondition all elements in ’list’ are unique
     * @Postcondition all elements smaller than the pivot appear in the leftmost part
     * of the subarray, then the pivot element, followed by the elements
     * larger than the pivot. There is no guarantee about the ordering
     * of the elements before and after the pivot.
     */
    public int partition(NDPoint280[] list, int left, int right, int d) {
        //store the item in the right position as the pivot
        double pivot = list[right].idx(d);

        int swapOffset = left;
        for (int i = left; i < right; i++) {
            if (list[i].idx(d) <= pivot) {
                NDPoint280 temp = list[i];
                list[i] = list[swapOffset];
                list[swapOffset] = temp;
                swapOffset = swapOffset + 1;
            }
        }

        NDPoint280 temp = list[right];
        list[right] = list[swapOffset];
        list[swapOffset] = temp;

        return swapOffset;
    }

    /**
     * Find the jth smallest item in an array.
     *
     * @param list  array of comparable elements.
     * @param left  offset of start of subarray for which we want the median element.
     * @param right offset of end of subarray for which we want the median element.
     * @param j     we want to find the element that belongs at array index j.
     * @Precondition left <= j <= right
     * @Precondition all elements in 'list' are unique
     * @Postcondition the element x that belongs at offseet j, if the subarray were
     * sorted, is at offset j. Elements in the subarray smaller than
     * x are to the left of offset j and the elements in the subarray
     * larger than x are to the right of offset j.
     */
    public void jSmallest(NDPoint280[] list, int left, int right, int j, int d) {
        if (right > left) {
//      Partition  the  subarray  using  the  last  element , list[right], as a pivot.
//      The  index of the  pivot  after  partitioning  is  returned.
//      This is  exactly  the  same  partition  algorithm  used by  quicksort.
            int pivotIndex = partition(list, left, right, d);

//           if the pivotIndex is equal to j, then we found the j-th smallest
//           element and it is in the right place

//           If the position j is smaller than the pivotIndex, we know that the jth
//           smallest element must be between left, and pivotIndex-1, so recursively
//           look for the jth smallest element in that subarray.
            if (j < pivotIndex) {
                jSmallest(list, left, pivotIndex - 1, j, d);
            } else {
//           Otherwise, the position j must be larger than the pivotIndex, so the
//           jth smallest element must be between pivotIndex + 1 and right
                jSmallest(list, pivotIndex + 1, right, j, d);
            }
//           Otherwise, the pivot ended up at list[j], and the pivot is the jth smallest
//           element.
        }
    }

    public NDPoint280[] searchRange(KDTree280 T, NDPoint280 p1, NDPoint280 p2, int depth) {
        if(T.isEmpty()) {
            return new NDPoint280[0];
        }

        int d = depth % (dimensionality + 1);
        double splitValue = T.rootItem().idx(d);
        double min = p1.idx(d);
        double max = p2.idx(d);

        if(splitValue < min) {
            return searchRange(T.rootRightSubtree(), p1, p2, depth + 1);
        } else if(splitValue > max) {
            return searchRange(T.rootLeftSubtree(), p1, p2, depth + 1);
        } else {
            NDPoint280[] L = searchRange(T.rootLeftSubtree(), p1, p2, depth + 1);
            NDPoint280[] R = searchRange(T.rootRightSubtree(), p1, p2, depth + 1);

            boolean test = false;
            for(int i = 0; i < dimensionality; i++) {
                if( (T.rootItem().idx(i) >= p1.idx(i)) && (T.rootItem().idx(i) <= p2.idx(i)) ) {
                    test = true;
                } else {
                    test = false;
                    break;
                }
            }

            if(test) {
                NDPoint280[] tempNode = new NDPoint280[L.length + R.length + 1];
                for(int i = 0; i < L.length; i++) {
                    tempNode[i] = L[i];
                }
                for(int i = 0; i < R.length; i++) {
                    tempNode[L.length + i] = R[i];
                }
                tempNode[tempNode.length-1] = T.rootItem();
                return tempNode;
            } else {
                NDPoint280[] tempNode = new NDPoint280[L.length + R.length];
                for(int i = 0; i < L.length; i++) {
                    tempNode[i] = L[i];
                }
                for(int i = 0; i < R.length; i++) {
                    tempNode[L.length + i] = R[i];
                }
                return tempNode;
            }
        }
    }

    public NDPoint280 rootItem() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        return rootNode.item();
    }

    /**
     * Left subtree of the root.
     * Analysis: Time = O(1)
     *
     * @precond !isEmpty()
     */
    public KDTree280 rootLeftSubtree() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280 result = this.clone();
        result.clear();
        result.setRootNode(rootNode.leftNode());
        return result;
    }

    /**
     * Right subtree of the root.
     * Analysis: Time = O(1)
     *
     * @precond !isEmpty()
     */
    public KDTree280 rootRightSubtree() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280 result = this.clone();
        result.clear();
        result.setRootNode(rootNode.rightNode());
        return result;
    }

    /**
     * Set root node to new node.
     * Analysis: Time = O(1)
     *
     * @param newNode node to become the new root node
     */
    protected void setRootNode(KDNode280 newNode) {
        rootNode = newNode;
    }

    @Override
    public boolean isEmpty() {
        return this.rootNode() == null;
    }


    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void clear() {
        setRootNode(null);
    }

    protected KDNode280 rootNode() {
        return rootNode;
    }

    /**
     * A shallow clone of this lib280.tree.
     * Analysis: Time = O(1)
     */
    @SuppressWarnings("unchecked")
    public KDTree280 clone() {
        try {
            return (KDTree280) super.clone();
        } catch (CloneNotSupportedException e) {
            /*	Should not occur because Container280 extends Cloneable */
            e.printStackTrace();
            return null;
        }
    }


    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (rootNode.leftNode != null || rootNode.rightNode != null))
            result += rootRightSubtree().toStringByLevel(i + 1);

        result += "\n" + blanks + i + ": ";
        if (isEmpty())
            result += "-";
        else {
            result += rootItem();
            if (rootNode.leftNode != null || rootNode.rightNode != null)
                result += rootLeftSubtree().toStringByLevel(i + 1);
        }
        return result;
    }

    /**
     * String representation of the lib280.tree, level by level. <br>
     * Analysis: Time = O(n), where n = number of items in the lib280.tree
     */
    public String toStringByLevel() {
        return toStringByLevel(1);
    }


    public static void main(String[] args) {

//      first testing partition method
        System.out.println("\nTesting partition method.");

//      data for the array of points(with a duplicate item for testing.
        double[][] dList = new double[][]{
                {5.0, 2.0},
                {9.0, 10.0},
                {11.0, 1.0},
                {4.0, 3.0},
                {2.0, 12.0},
                {3.0, 7.0},
                {1.0, 5.0},
                {8.0, 20.0},
                {15.0, 15.0},
                {29.0, 0.0},
                {16.0, 16.0}
        };

        NDPoint280[] pointArray = new NDPoint280[dList.length];
//      instantiating a new KDTree for testing purposes.
        for (int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }
        KDTree280 tree = new KDTree280();

        System.out.println("\nTesting partition() on 1st dimension of unique array: ");

        boolean testing = true;
        int pTest; //store value of pivot from partition()

        try {
            pTest = tree.partition(pointArray, 0, dList.length - 1, 0);
            if (pTest == 9) {
                System.out.println("1: PASS");
            } else {
                System.out.println("1: FAIL");
            }
        } catch (Exception e) {
            testing = false;
        }

        if (testing) {
            System.out.println("2: PASS");
        } else {
            System.out.println("2: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length / 2, 0);

        if (pTest == 1) {
            System.out.println("3: PASS");
        } else {
            System.out.println("3: FAIL");
        }

        pTest = tree.partition(pointArray, dList.length / 2, dList.length - 1, 0);

        if (pTest == 10) {
            System.out.println("4: PASS");
        } else {
            System.out.println("4: FAIL");
        }

        pTest = tree.partition(pointArray, dList.length / 2 + 2, dList.length - 1, 0);

        if (pTest == 10) {
            System.out.println("5: PASS");
        } else {
            System.out.println("5: FAIL");
        }

        pTest = tree.partition(pointArray, 2, 6, 0);

        if (pTest == 2) {
            System.out.println("6: PASS");
        } else {
            System.out.println("6: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length - 2, 0);

        if (pTest == 9) {
            System.out.println("7: PASS");
        } else {
            System.out.println("7: FAIL");
        }

        System.out.println("\nTesting partition() on 2nd dimension of unique array: ");

        testing = true;
        try {
            pTest = tree.partition(pointArray, 0, dList.length - 1, 1);
        } catch (Exception e) {
            testing = false;
        }

        if (testing) {
            System.out.println("8: PASS");
        } else {
            System.out.println("8: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length - 1, 1);

        if (pTest == 7) {
            System.out.println("9: PASS");
        } else {
            System.out.println("9: FAIL");
        }

        pTest = tree.partition(pointArray, dList.length / 2, dList.length - 1, 1);

        if (pTest == 10) {
            System.out.println("10: PASS");
        } else {
            System.out.println("10: FAIL");
        }

        pTest = tree.partition(pointArray, 3, 7, 1);

        if (pTest == 7) {
            System.out.println("11: PASS");
        } else {
            System.out.println("11: FAIL");
        }

        pTest = tree.partition(pointArray, 0, 6, 1);

        if (pTest == 1) {
            System.out.println("12: PASS");
        } else {
            System.out.println("12: FAIL");
        }

        pTest = tree.partition(pointArray, 0, dList.length - 2, 1);

        if (pTest == 9) {
            System.out.println("13: PASS");
        } else {
            System.out.println("13: FAIL");
        }


        System.out.println("\nTesting partition() on higher dimension than array: ");

        testing = false;
        try {
            pTest = tree.partition(pointArray, 0, dList.length - 1, 2);
        } catch (InvalidArgument280Exception e) {
            testing = true;
        }

        if (testing) {
            System.out.println("14: PASS");
        } else {
            System.out.println("14: FAIL");
        }

//      Starting testing for jSmallest()
//      restoring original array of NDPOints280 for testing

        System.out.println("\nTesting jSmallest(): ");

        System.out.println("\nTesting jSmallest() on 1st dimension: ");
        for (int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }

        tree.jSmallest(pointArray, 0, dList.length - 1, dList.length / 2, 0);
        if (pointArray[pointArray.length / 2].idx(0) == 8) {
            System.out.println("15: PASS");
        } else {
            System.out.println("15: FAIL");
        }

        tree.jSmallest(pointArray, 0, 3, 2, 0);
        if (pointArray[2].idx(0) == 4) {
            System.out.println("16: PASS");
        } else {
            System.out.println("16: FAIL");
        }

        tree.jSmallest(pointArray, 6, dList.length - 1, 7, 0);
        if (pointArray[7].idx(0) == 11) {
            System.out.println("17: PASS");
        } else {
            System.out.println("17: FAIL");
        }

        System.out.println("\nTesting jSmallest() on 2nd dimension.");

        tree.jSmallest(pointArray, 0, dList.length - 1, dList.length / 2, 1);
        if (pointArray[dList.length / 2].idx(1) == 7) {
            System.out.println("18: PASS");
        } else {
            System.out.println("18: FAIL");
        }

        tree.jSmallest(pointArray, dList.length / 2, dList.length - 1, 8, 1);
        if (pointArray[8].idx(1) == 15) {
            System.out.println("19: PASS");
        } else {
            System.out.println("19: FAIL");
        }

//      Testing the building of the actual tree now.
//      Rebuilding the original tree.
        dList = new double[][]{
                {5.0, 2.0},
                {9.0, 10.0},
                {11.0, 1.0},
                {4.0, 3.0},
                {2.0, 12.0},
                {3.0, 7.0},
                {1.0, 5.0},
                {23, 4.0}
        };

        pointArray = new NDPoint280[dList.length];
        for (int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }

        //building a string to display the input array
        System.out.println("\nTesting the list of points from the example: ");
        String testString = new String();
        for (int i = 0; i < pointArray.length; i++) {
            testString += "(";
            testString += pointArray[i].idx(0) + " ," + pointArray[i].idx(1);
            testString += ")\n";
        }
        System.out.println(testString);
        System.out.println("The Tree from this list of points: ");
        tree = new KDTree280(pointArray, 1);
        System.out.println(tree.toStringByLevel());

        //building an array for 3d points
        dList = new double[][]{
                {1.0, 12.0, 1.0, 41.0},
                {18.0, 1.0, 2.0, 82.0},
                {2.0, 14.0, 16.0, 24.0},
                {7.0, 3.0, 3.0, 94.0},
                {3.0, 7.0, 5.0, 103.0},
                {16.0, 4.0, 4.0, 0.0},
                {4.0, 6.0, 1.0, 13.0},
                {5.0, 5.0, 17.0, 3.0},
                {8.0, 2.0, 20.0, 12.0}
        };

         pointArray = new NDPoint280[dList.length];
        for (int i = 0; i < dList.length; i++) {
            pointArray[i] = new NDPoint280(dList[i]);
        }

        //building a string to display the input array
        testString = new String();
        for (int i = 0; i < pointArray.length; i++) {
            testString += "(";
            testString += pointArray[i].idx(0) + " ," + pointArray[i].idx(1) + " ," + pointArray[i].idx(2)
                            + " ," + pointArray[i].idx(3);
            testString += ")\n";
        }

        System.out.println("\nTesting 4d points.\n" + testString);

        System.out.println("The Tree from this list of points: ");
        tree = new KDTree280(pointArray, 4);
        System.out.println(tree.toStringByLevel());

        //3,4,0,2
        //6,7,20,15
        // upper range for the range search
        double[] d1 = new double[] {3.0, 4.0, 0.0, 2.0};
        NDPoint280 p1 = new NDPoint280(d1);

        // upper range for the range search
        double[] d2 = new double[] {6.0, 7.0, 20.0, 15.0};
        NDPoint280 p2 = new NDPoint280(d2);

        // store the output of the range search.
        NDPoint280[] searchOut;

        System.out.println("\nTesting a range search: lower(3,4,0,2) upper(6,7,20,15): ");
        searchOut = tree.searchRange(tree, p1, p2, 0);

        testString = new String();
        for (int i = 0; i < searchOut.length; i++) {
            testString += "(";
            testString += searchOut[i].idx(0) + " ," + searchOut[i].idx(1) + " ," + searchOut[i].idx(2)
                    + " ," + searchOut[i].idx(3);
            testString += ")\n";
        }
        System.out.println("Searching..\nItems in range: \n" + testString);
    }
}
