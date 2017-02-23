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

    protected void rightRotation(BinaryAVLNode280<I> root) {
        BinaryAVLNode280<I> temp1;
        BinaryAVLNode280<I> temp2 = root.leftNode();

        if (temp2.leftNode == null) {
            temp1 = null;
        } else {
            temp1 = temp2.rightNode();
        }

        temp2.setRightNode(root.clone());
        temp2.rightNode.setLeftNode(temp1);
        if (temp1 != null) {
            temp2.rightNode.setLeftHeight(Math.max(temp1.leftHeight, temp1.rightHeight) + 1);
        } else {
            temp2.rightNode.setLeftHeight(0);
        }

        if (temp2.rightNode == null) {
            temp2.setRightHeight(0);
        } else {
            temp2.setRightHeight(Math.max(temp2.rightNode.rightHeight, temp2.rightNode.leftHeight) + 1);
        }
        if (temp2.leftNode == null) {
            temp2.setLeftHeight(0);
        } else {
            temp2.setLeftHeight(Math.max(temp2.leftNode.rightHeight, temp2.leftNode.leftHeight) + 1);
        }
        root.setItem(temp2.item());
        root.setLeftHeight(temp2.leftHeight());
        root.setRightHeight(temp2.rightHeight());
        root.setRightNode(temp2.rightNode());
        root.setLeftNode(temp2.leftNode());
    }

    protected void leftRotation(BinaryAVLNode280<I> root) {
        BinaryAVLNode280<I> temp2 = root.rightNode();
        BinaryAVLNode280<I> temp1;

        if (temp2.leftNode == null) {
            temp1 = null;
        } else {
            temp1 = temp2.leftNode();
        }

        temp2.setLeftNode(root.clone());
        temp2.leftNode.setRightNode(temp1);
        if (temp1 != null) {
            temp2.leftNode.setRightHeight(Math.max(temp1.leftHeight, temp1.rightHeight) + 1);
        } else {
            temp2.leftNode.rightHeight = 0;
        }

        if (temp2.leftNode == null) {
            temp2.setLeftHeight(0);
        } else {
            temp2.setLeftHeight(Math.max(temp2.leftNode.leftHeight, temp2.leftNode.rightHeight) + 1);
        }
        if (temp2.rightNode == null) {
            temp2.setRightHeight(0);
        } else {
            temp2.setRightHeight(Math.max(temp2.rightNode.leftHeight, temp2.rightNode.rightHeight) + 1);
        }

        root.setItem(temp2.item());
        root.setLeftHeight(temp2.leftHeight());
        root.setRightHeight(temp2.rightHeight());
        root.setRightNode(temp2.rightNode());
        root.setLeftNode(temp2.leftNode());
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
                    leftRotation(root.leftNode());
                    rightRotation(root);
                }
            } else {
                // R imbalance looking at right children.
                if (root.rightNode().leftHeight() <= root.rightNode().rightHeight()) {
                    // RR imbalance need left rotation.
                    leftRotation(root);
                } else {
                    // RL imbalance need right then left rotation.
                    rightRotation(root.rightNode());
                    leftRotation(root);
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

    public BinaryAVLNode280<I> inOrderSuccesor(BinaryAVLNode280<I> root) {
        BinaryAVLNode280<I> temp;
        if (root == null)
            return root;

        temp = root.clone().leftNode;

        while (temp.rightNode != null) {
            temp = temp.rightNode();
        }
        return temp;

    }

    public void deleteRecurse(I x, BinaryAVLNode280<I> root) {
        if (x.compareTo(root.item()) == 0) {
            if ((root.leftHeight() + root.rightHeight()) <= 1) {
                if (root.leftHeight() == 1) {
                    root.swapNode(root.leftNode());
                } else if (root.rightHeight() == 1) {
                    root.swapNode(root.rightNode());
                } else {
                    root.nullifyNode();
                }

            } else {
                BinaryAVLNode280<I> temp = inOrderSuccesor(root);
                root.setItem(temp.item());
                deleteRecurse(temp.item(), root.leftNode());
            }
        } else {
            if (x.compareTo(root.item()) <= 0) {
                deleteRecurse(x, root.leftNode());
            } else {
                deleteRecurse(x, root.rightNode());
            }
        }
        updateHeight(root);
        restoreAVLProperty(root);
    }

    public void updateHeight(BinaryAVLNode280<I> root) {
        if ((root.leftNode != null) && (root.leftNode.item != null)) {
            root.setLeftHeight(Math.max(root.leftNode().leftHeight(), root.leftNode().rightHeight()) + 1);
        } else {
            root.setLeftHeight(0);
        }
        if ((root.rightNode != null) && (root.rightNode.item != null)) {
            root.setRightHeight(Math.max(root.rightNode().leftHeight(), root.rightNode().rightHeight()) + 1);
        } else {
            root.setRightHeight(0);
        }
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
    protected void setRootNode(BinaryNode280<I> newNode) {
        this.rootNode = (BinaryAVLNode280<I>) newNode;
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
        if (cur.item == null) {
            throw new NoCurrentItem280Exception("Error: no current item.");
        } else {
            deleteRecurse(cur.item(), this.rootNode());
        }
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
        if (isEmpty() || (this.rootNode().item == null))
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
        System.out.println("\nTree (before): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(42);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        if (tree.rootItem() == 42) {
            System.out.println("3: PASS");
        } else {
            System.out.println("3: FAIL");
        }

        // inserting another item 120 should become right subtree
        System.out.println("\nInserting 120");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(120);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");


        if (tree.rootRightSubtree().rootNode.item() == 120) {
            System.out.println("4: PASS");
        } else {
            System.out.println("4: FAIL");
        }

        // inserting 2 should be left sub tree
        System.out.println("\nInserting 2");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(2);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        if (tree.rootLeftSubtree().rootNode.item() == 2) {
            System.out.println("5: PASS");
        } else {
            System.out.println("5: FAIL");
        }

        // inserting 4 should be 2's right subtree
        System.out.println("\nInserting 4");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(4);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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

        System.out.println("\nInserting 0; Testing LL imbalance and right rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(0);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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
        if (tree.rootLeftSubtree().rootRightSubtree().rootNode != null)
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
        System.out.println("\nInserting 1; Testing LR imbalance and double right rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(1);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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
        if (tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree().rootNode != null)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree().rootNode != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode != null)
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
        System.out.println("\nInserting 99, 122, 84; Testing RL imbalance and double left rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(99);
        tree.insert(122);
        tree.insert(84);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");


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
        if (tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree().rootNode != null)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree().rootNode != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode.item() != 122)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootRightSubtree().rootNode.item() != 84)
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

        System.out.println("\nInserting 126; Testing RR imbalance and left rotation.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.insert(126);
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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
        System.out.println("\nDeleting 99.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.search(99);
        if (tree.itemExists()) {
            tree.deleteItem();
        } else {
            System.out.println("Error: no current item");
        }
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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
        System.out.println("\nDeleting 5.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.search(5);
        if (tree.itemExists()) {
            tree.deleteItem();
        } else {
            System.out.println("Error: no current item");
        }
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

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
        System.out.println("\nDeleting 42. Triggering an RR imbalance at 84.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.search(42);
        if (tree.itemExists()) {
            tree.deleteItem();
        } else {
            System.out.println("Error: no current item");
        }
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        test = true;
        if (tree.rootRightSubtree().rootNode.item() != 122)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootNode.item() != 126)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootNode.item() != 84)
            test = false;
        if (tree.rootRightSubtree().rootLeftSubtree().rootRightSubtree().rootNode.item() != 120)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().rootNode != null)
            test = false;
        if (tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().rootNode != null)
            test = false;

        if (test)
            System.out.println("12: PASS");
        else
            System.out.println("12: FAIL");


        System.out.println("\nDeleting 0, 2. Triggering an RL imbalance at root.");
        System.out.println("\nTree: (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");
        tree.search(0);
        if (tree.itemExists()) {
            tree.deleteItem();
        } else {
            System.out.println("Error: no current item");
        }
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");

        tree.search(2);
        if (tree.itemExists()) {
            tree.deleteItem();
        } else {
            System.out.println("Error: no current item");
        }
        System.out.println("\nTree (after): (level= item(left height, right height)) " + tree.toStringByLevel(1) + "\n");


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

