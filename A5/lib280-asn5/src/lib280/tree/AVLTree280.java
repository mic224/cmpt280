package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class AVLTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> {

    protected BinaryAVLNode280<I> rootNode;
    /**
     * The current node as set by search.
     */
    protected BinaryAVLNode280<I> cur;

    /**
     * The parent node of the current node as set by search.
     */
    protected BinaryAVLNode280<I> parent;

    public AVLTree280() {
        super();
        this.rootNode = null;
    }

    public AVLTree280(AVLTree280<I> lt, I r, AVLTree280<I> rt) {
        super(lt, r, rt);
    }

    protected BinaryAVLNode280<I> rightRotation(BinaryAVLNode280<I> root) {
        return root;
    }

    protected BinaryAVLNode280<I> leftRotation(BinaryAVLNode280<I> root) {
        BinaryAVLNode280<I> TN2 = root.rightNode();
        BinaryAVLNode280<I> TN1;

        if(TN2.leftNode == null) {
            TN1 = null;
        } else {
            TN1 = TN2.leftNode();
        }

        TN2.setLeftNode(root.clone());
        TN2.leftNode.setRightNode(null);
        TN2.leftNode.rightHeight = 0;

        if(TN2.leftNode == null) {
            TN2.setLeftHeight(0);
        } else {
            TN2.setLeftHeight(Math.max(TN2.leftNode.leftHeight, TN2.leftNode.rightHeight) + 1);
        }
        TN2.setRightHeight(Math.max(TN2.rightNode.leftHeight, TN2.rightNode.rightHeight) + 1);
        return TN2;
    }


    protected void restoreAVLProperty(BinaryAVLNode280<I> root) {
        if (Math.abs(root.leftHeight() - root.rightHeight()) > 1) {
            // root node is critically imbalanced.
            if (root.leftHeight() > root.rightHeight()) {
                // L imbalance looking at left children.
                if (root.leftNode().leftHeight() > root.leftNode().rightHeight()) {
                    // LL imbalance need right rotation.
                    rightRotation(root);
                } else {
                    // LR imbalance need left then right rotations.
                    BinaryAVLNode280<I> temp = leftRotation(root.leftNode());
                    rightRotation(temp);
                }
            } else {
                // R imbalance looking at right children.
                if (root.rightNode().leftHeight() < root.rightNode().rightHeight()) {
                    // RR imbalance need left rotation.
                    BinaryAVLNode280<I> temp = leftRotation(root);
                    root.setItem(temp.item());
                    root.setLeftHeight(temp.leftHeight());
                    root.setRightHeight(temp.rightHeight());
                    root.setRightNode(temp.rightNode());
                    root.setLeftNode(temp.leftNode());
                } else {
                    // RL imbalance need right then left rotation.
                    BinaryAVLNode280<I> temp = rightRotation(root.rightNode());
                    leftRotation(temp);
                }
            }
        }
    }

    protected void insertRecurse(I x, BinaryAVLNode280<I> root) {
        if (x.compareTo(root.item()) <= 0) {
            if (root.leftNode() == null) {
                root.setLeftNode(createNewNode(x));
            } else {
                insertRecurse(x, root.leftNode());
            }
            root.setLeftHeight(Math.max(root.leftNode().leftHeight(), root.leftNode().rightHeight()) + 1);
        } else {
            if (root.rightNode() == null) {
                root.setRightNode(createNewNode(x));
            } else {
                insertRecurse(x, root.rightNode());
            }
            root.setRightHeight(Math.max(root.rightNode().leftHeight(), root.rightNode().rightHeight()) + 1);
        }
        restoreAVLProperty(root);
    }

    /**
     * Insert x into the lib280.tree. <br>
     * Analysis : Time = O(log n) worst case, where n = number of nodes in the lib280.tree
     */
    public void insert(I x) {
        if (this.isEmpty()) {
            setRootNode(createNewNode(x));
        } else {
            insertRecurse(x, this.rootNode());
        }

    }

    @Override
    public boolean itemExists() {
        return cur != null;
    }

    @Override
    public void search(I x) {
        boolean found = false;
        if (!searchesContinue || above()) {
            parent = null;
            cur = this.rootNode;
        } else if (!below()) {
            parent = cur;
            cur = cur.rightNode();
        }
        while (!found && itemExists()) {
            if (x.compareTo(item()) < 0) {
                parent = cur;
                cur = parent.leftNode();
            } else if (x.compareTo(item()) > 0) {
                parent = cur;
                cur = parent.rightNode();
            } else
                found = true;
        }
    }

    @Override
    public boolean has(I x) {
        // save cursor state
        BinaryAVLNode280<I> saveParent = parent;
        BinaryAVLNode280<I> saveCur = cur;
        boolean saveSearchesContinue = this.searchesContinue;

        // Always start at the root
        this.parent = null;
        this.cur = this.rootNode;
        this.restartSearches();

        // Search
        this.search(x);
        boolean result = itemExists();

        // Restore cursor state
        this.parent = saveParent;
        this.cur = saveCur;
        this.searchesContinue = saveSearchesContinue;

        return result;
    }

    @Override
    protected BinaryAVLNode280<I> createNewNode(I item) {
        BinaryAVLNode280<I> temp = new BinaryAVLNode280<I>(item);
        temp.setLeftHeight(0);
        temp.setRightHeight(0);
        return temp;
    }

    @Override
    protected void setRootNode(BinaryNode280<I> newNode)
    {
        this.rootNode = (BinaryAVLNode280<I>)newNode;
    }

    @Override
    public I rootItem() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot access the root of an empty lib280.tree.");

        return this.rootNode.item();
    }

    @Override
    public I item() throws NoCurrentItem280Exception {
        if (!itemExists())
            throw new NoCurrentItem280Exception("Cannot access item when it does not exist");

        return cur.item();
    }

    @Override
    public AVLTree280<I> clone() {
        return (AVLTree280<I>) super.clone();
    }

    @Override
    public AVLTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        AVLTree280<I> result = this.clone();
        result.clear();
        result.setRootNode(this.rootNode.leftNode());
        return result;
    }

    @Override
    public AVLTree280<I> rootRightSubtree() throws ContainerEmpty280Exception {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        AVLTree280<I> result = this.clone();
        result.clear();
        result.setRootNode(this.rootNode.rightNode());
        return result;
    }

    @Override
    public boolean isEmpty() {
        return this.rootNode == null;
    }

    @Override
    protected BinaryAVLNode280<I> rootNode() {
        return this.rootNode;
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        return;
    }

    @Override
    public String toString() {
        return this.toStringByLevel(1);
    }

    @Override
    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 10);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtree().toStringByLevel(i + 1);

        result += "\n" + blanks + i + "= ";
        if (isEmpty())
            result += "-";
        else {
            result += rootItem() + "(L:" + this.rootNode().leftHeight() + ",R:" + this.rootNode().rightHeight() + ")";
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtree().toStringByLevel(i + 1);
        }
        return result;
    }

    public static void main(String args[]) {
        AVLTree280<Integer> tree = new AVLTree280<Integer>();
        boolean test = true;

        // making sure tree is empty
        if (tree.isEmpty()) {
            System.out.println("1: PASS");
        } else {
            System.out.println("1: FAIL");
        }

        // making sure constructor set root null
        try {
            test = (tree.rootItem() == null);
        } catch (Exception e) {
            test = true;
        }

        if (test)
            System.out.println("2: PASS");
        else
            System.out.println("2: FAIL");

        // inserting the first node for testing should become root item
        System.out.println("\nInserting 42");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(42);

        if (tree.rootItem() == 42) {
            System.out.println("3: PASS");
        } else {
            System.out.println("3: FAIL");
        }

        // inserting another item 120 should become right subtree
        System.out.println("\nInserting 120");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(120);

        if (tree.rootRightSubtree().rootNode.item() == 120) {
            System.out.println("4: PASS");
        } else {
            System.out.println("4: FAIL");
        }

        // inserting 2 should be left sub tree
        tree.insert(2);
        System.out.println("\nInserting 2");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        if (tree.rootLeftSubtree().rootNode.item() == 2) {
            System.out.println("5: PASS");
        } else {
            System.out.println("5: FAIL");
        }

        // inserting 4 should be 2's right subtree
        System.out.println("\nInserting 4");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(4);

        if (tree.has(4)) {
            if (tree.rootLeftSubtree().rootRightSubtree().rootNode.item() == 4)
                System.out.println("6: PASS");
            else
                System.out.println("6: FAIL");
        } else {
            System.out.println("6: FAIL");
        }

        // inserting 5 now the node containing 2 should be LL imbalance
        // a right rotation is required
        // root left subtree should now be 4 (right subtree unchanged = 120)
        // 4 left subtree should be 2
        // 4 right subtree should be 5

        System.out.println("\nInserting 5; Testing RR imbalance and left rotation.");
        System.out.println("\nTree (before): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(5);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        if (!tree.has(5))
            test = false;
        if (tree.rootLeftSubtree().rootNode.item() != 4)
            test = false;
        if (tree.rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootNode.item() != 2)
            test = false;
        if (tree.rootLeftSubtree().rootRightSubtree().rootNode.item() != 5)
            test = false;
        if (test == true)
            System.out.println("7: PASS");
        else
            System.out.println("7: FAIL");

        // inserting 0 now the root node, 42, is LL imbalanced; need another right rot.
        // new root item is 4
        // root -> right = 42
        // root -> right -> right = 120
        // root -> right -> left = 5
        // root -> left = 2
        // root -> left -> left = 0
        tree.insert(0);
        System.out.println("\nInserting 0; Testing LL imbalance and right rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        test = true;
        if (!tree.has(0))
            test = false;
        if (tree.rootItem() != 4)
            test = false;
        if (tree.rootRightSubtree().rootNode.item() != 42)
            test = false;
        if (tree.rootLeftSubtree().rootNode.item() != 2)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootNode.item() != 5)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootNode.item() != 0)
            test = false;
        if (tree.rootLeftSubtree().rootRightSubtree() != null)
            test = false;

        if (test)
            System.out.println("8: PASS");
        else
            System.out.println("8: FAIL");

        // before:
        //          120
        //      42
        //          5
        // 4
        //      2
        //          0
        // now inserting necessary item(1) to trigger an LR imbalance at 2 and
        // require double right rotation.
        // after:
        //          120
        //      42
        //          5
        // 4
        //          2
        //      1
        //          0
        tree.insert(1);
        System.out.println("\nInserting 1; Testing LR imbalance and double right rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        test = true;
        if (!tree.has(1))
            test = false;
        if (tree.rootItem() != 4)
            test = false;
        if (tree.rootRightSubtree().rootNode.item() != 42)
            test = false;
        if (tree.rootLeftSubtree().rootNode.item() != 1)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootNode.item() != 5)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootNode.item() != 0)
            test = false;
        if (tree.rootLeftSubtree().rootRightSubtree().rootNode.item() != 2)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree() != null)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree() != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree() != null)
            test = false;

        if (test)
            System.out.println("9: PASS");
        else
            System.out.println("9: FAIL");

        // before:
        //          120
        //      42
        //          5
        // 4
        //          2
        //      1
        //          0
        // now inserting necessary items(99, 122, 84) to trigger an RL imbalance at  and
        // require double left rotation.
        // after:
        //                  122
        //          120
        //      99
        //                  84
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        tree.insert(99);
        tree.insert(122);
        tree.insert(84);
        System.out.println("\nInserting 99, 122, 84; Testing RL imbalance and double left rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");


        test = true;
        if (!tree.has(99) || !tree.has(122) || !tree.has(84))
            test = false;
        if (tree.rootItem() != 4)
            test = false;
        if (tree.rootRightSubtree().rootNode.item() != 99)
            test = false;
        if (tree.rootLeftSubtree().rootNode.item() != 1)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootNode.item() != 42)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootNode.item() != 0)
            test = false;
        if (tree.rootLeftSubtree().rootRightSubtree().rootNode.item() != 2)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree() != null)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree() != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode.item() != 122)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().rootNode.item() != 84)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootLeftSubtree().rootNode.item() != 5)
            test = false;

        if (test)
            System.out.println("10: PASS");
        else
            System.out.println("10: FAIL");


        // before:
        //                  122
        //          120
        //      99
        //                  84
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        // now inserting necessary items(126) to trigger an RR imbalance at 120 and
        // require double left rotation.
        // after:
        //                  126
        //          122
        //                  120
        //      99
        //                  84
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        tree.insert(126);
        System.out.println("\nInserting 126; Testing RR imbalance and left rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        test = true;
        if (!tree.has(126))
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 122)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode.item() != 126)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().rootNode.item() != 120)
            test = false;

        if (test)
            System.out.println("11: PASS");
        else
            System.out.println("11: FAIL");


        // before:
        //                  126
        //          122
        //                  120
        //      99
        //                  84
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        // now deleting node 99 by swapping with biggest in left subtree of 99
        // after:
        //                  126
        //          122
        //                  120
        //      84
        //
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        tree.rootRightSubtree().deleteItem();
        System.out.println("\nDeleting 99.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        // before:
        //                  126
        //          122
        //                  120
        //      84
        //
        //          42
        //                  5
        // 4
        //          2
        //      1
        //          0
        // now deleting node 5
        // after:
        //                  126
        //          122
        //                  120
        //      84
        //
        //          42
        //
        // 4
        //          2
        //      1
        //          0
        tree.rootRightSubtree().rootLeftSubtree().rootLeftSubtree().deleteItem();
        System.out.println("\nDeleting 5.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        // before:
        //                  126
        //          122
        //                  120
        //      84
        //
        //          42
        //
        // 4
        //          2
        //      1
        //          0
        // now deleting node 42 which triggers a RR imbalance at 84, and a left
        // rotation following.
        // after:
        //
        //          126
        //
        //      122
        //                  120
        //          84
        //
        // 4
        //          2
        //      1
        //          0
        tree.rootRightSubtree().rootLeftSubtree().deleteItem();
        System.out.println("\nDeleting 42. Triggering an RR imbalance at 84.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        test = true;
        if (tree.rootRightSubtree().rootNode.item() != 122)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 126)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootNode.item() != 84)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode.item() != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().rootNode.item() != null)
            test = false;

        if (test)
            System.out.println("12: PASS");
        else
            System.out.println("12: FAIL");

        // testing clear
        tree.clear();

        test = true;
        try {
            tree.deleteItem();
        } catch (NoCurrentItem280Exception e) {
            test = false;
        }

        if (test)
            System.out.println("13: FAIL");
        else
            System.out.println("13: PASS");
    }
}

