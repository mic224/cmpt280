package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.DuplicateItems280Exception;
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

    /**
     * Performs a right rotation at the given node(child) and updates the parents references and
     * the heights of both parent and child
     * the tree.
     * Analysis: Time = O(1)
     * @param parent BinaryAVLNode280 the parent node.
     * @param child BinaryAVLNode 280 the child of parent.
     */
    protected void rightRotation(BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) {
        if (parent == null) {
            // special case
            this.rootNode = child.leftNode;
            child.setLeftNode(rootNode.rightNode);
            updateHeight(child);
            rootNode.setRightNode(child);
            updateHeight(rootNode());
        } else {
            // check whether node is right or left child of parent
            if (child == parent.leftNode) {
                // node is left child perform rotations
                parent.setLeftNode(child.leftNode);
                parent.leftNode().setRightNode(child);
                child.leftNode = null;
                child.setLeftHeight(0);
                updateHeight(parent.leftNode);
            } else {
                // ndoe is right child performing rotations
                parent.setRightNode(parent.rightNode.leftNode);
                child.setLeftNode(child.leftNode.rightNode);
                parent.rightNode().setRightNode(child);
                child.setLeftHeight(0);
                updateHeight(parent.rightNode);
            }
            updateHeight(parent);
        }
    }

    /**
     * Performs a leftRotation at the given node(child) and updates the parents references and
     * the heights of both parent and child
     * the tree.
     * Analysis: Time = O(1)
     * @param parent BinaryAVLNode280 the parent node.
     * @param child BinaryAVLNode 280 the child of parent.
     */
    protected void leftRotation(BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) {
        if (parent == null) {
            // special case
            this.rootNode = child.rightNode;
            child.setRightNode(rootNode.leftNode);
            updateHeight(child);
            rootNode.setLeftNode(child);
            updateHeight(rootNode());
        } else {
            // check whether child is left or right child of parent
            if (child == parent.leftNode) {
                // is the left child of parent performing rotations
                parent.setLeftNode(child.rightNode);
                parent.leftNode().setRightNode(parent.leftNode.rightNode);
                parent.leftNode().setLeftNode(child);
                child.rightNode = null;
                child.setRightHeight(0);
                updateHeight(parent.leftNode);
            } else {
                // is the right child of parent performing rotations
                parent.setRightNode(child.rightNode);
                child.setRightNode(child.rightNode.leftNode);
                parent.rightNode.setLeftNode(child);
                updateHeight(child);
                updateHeight(parent.rightNode);
            }
            updateHeight(parent);
        }
    }

    /**
     * Method to check for imbalance and call the appropriate rotation to restore balance to
     * the tree.
     * Analysis: Time = O(1)
     * @param parent BinaryAVLNode280 the parent node.
     * @param child BinaryAVLNode 280 the child of parent.
     */
    protected void restoreAVLProperty(BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) {
        if (child.getImbalance() > 1) {
            // child node is critically imbalanced.
            if (child.leftHeight() > child.rightHeight()) {
                // L imbalance looking at left children.
                if (child.leftNode().leftHeight() > child.leftNode().rightHeight()) {
                    // LL imbalance need right rotation.
                    rightRotation(parent, child);

                } else {
                    // LR imbalance need left then right rotations.
                    leftRotation(child, child.leftNode());
                    rightRotation(parent, child);
                }
            } else {
                // R imbalance looking at right children.
                if (child.rightNode().leftHeight() <= child.rightNode().rightHeight()) {
                    // RR imbalance need left rotation.
                    leftRotation(parent, child);
                } else {
                    // RL imbalance need right then left rotation.
                    rightRotation(child, child.rightNode());
                    leftRotation(parent, child);
                }
            }
        }
    }
    /**
     * Recursively searches the tree to find the spot the item belongs, then inserts, updates
     * heights and rebalances.
     * Analysis: Time = O(log n)
     * @param x item of type I to be deleted from the tree.
     * @param parent BinaryAVLNode280 the parent node.
     * @param child BinaryAVLNode 280 the child of parent.
     */
    protected void insertRecurse(I x, BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) throws DuplicateItems280Exception {
        // first checking which side of the tree the item needs to go
        if (x.compareTo(child.item()) < 0) {
            // the item needs to go on the left side. look for a leaf.
            if (child.leftNode == null) {
                // found a leaf node to insert the item to
                child.setLeftNode(createNewNode(x));
                child.setLeftHeight(1);
            } else {
                // keep looking for a leaf node
                insertRecurse(x, child, child.leftNode());
            }

        } else if (x.compareTo(child.item()) > 0) {
            // the item needs to go on the right side. look for a leaf.
            if (child.rightNode == null) {
                // found a leaf node to insert to.
                child.setRightNode(createNewNode(x));
                child.setRightHeight(1);
            } else {
                // keep looking for a leaf.
                insertRecurse(x, child, child.rightNode());
            }

        } else {
            throw new DuplicateItems280Exception("The item is already in the tree.");
        }
        //update the heights of each node and rebalance the tree if needed.
        updateHeight(parent);
        updateHeight(child);
        restoreAVLProperty(parent, child);
    }

    /**
     * Function to start the recursive insertion.
     * Analysis: Time = O(log n)
     * @param x item of type I to be inserted into the tree.
     */
    public void insert(I x) throws DuplicateItems280Exception {
        if (this.isEmpty()) {
            // special case the tree is empty simply insert to root.
            this.setRootNode(createNewNode(x));
        } else {
            // non empty check which side the item needs to be inserted to.
            if (x.compareTo(rootItem()) < 0) {
                //the item needs to go left.
                if (rootNode().leftNode == null) {
                    // rootnode has no left child so simply set the item to rootnode left.
                    rootNode().setLeftNode(createNewNode(x));
                    rootNode.setLeftHeight(1);
                } else {
                    // start the recursion on the left side of the tree to find where to insert.
                    insertRecurse(x, rootNode(), rootNode().leftNode);
                }
            } else if(x.compareTo(rootItem()) > 0) {
                // the item needs to go right
                if (rootNode().rightNode == null) {
                    // rootnode has no right child set item rootnode right
                    rootNode().setRightNode(createNewNode(x));
                    rootNode.setRightHeight(1);
                } else {
                    // start the recursion on the right side of the tree.
                    insertRecurse(x, rootNode(), rootNode().rightNode);
                }
            } else {
                throw new DuplicateItems280Exception("Item is alreayd in the tree.");
            }

            if (rootNode().getImbalance() > 1) {
                // rebalance the root if needed.
                restoreAVLProperty(null, rootNode());
            }
        }

    }

    /**
     * Method to present and start the recursive deletion. Also updates the rootNode height and
     * checks the root for imbalance and rebalances if necessary.
     * Analysis: Time = O(log n)
     */
    public void deleteItem() throws NoCurrentItem280Exception {
        if (cur.item == null) {
            throw new NoCurrentItem280Exception("Error: no current item.");
        } else {
            if (cur.item.compareTo(this.rootNode.item) < 0) {
                //check if there is a left side of the tree
                deleteRecurse(cur.item, rootNode, rootNode.leftNode);
            } else if (cur.item.compareTo(this.rootNode.item) > 0) {
                // recurse right side of tree
                deleteRecurse(cur.item, rootNode, rootNode.rightNode);
            } else {
                // Item to be deleted is at the root of the tree
                if (rootNode.getMaxHeight() <= 1) {
                    // check if rootNode is the only item left
                    // in this case just make rootNode null to clear tree.
                    this.rootNode = null;
                } else {
                    // special case, deleting root node which has subtrees
                    deleteRecurse(cur.item, rootNode, null);
                }
            }
            // restore rootNode balance if needed
            if (rootNode.getImbalance() > 1) {
                restoreAVLProperty(null, rootNode);
            }
        }
    }

    /**
     * Recursively searches a tree for the item to delete then deletes the item in the AVLTree
     * fashion, updates the heights of parent and child then calls restoreAVLProperty to
     * rebalance the tree.
     * Analysis: Time = O(log n)
     * @param x item of type I to be deleted from the tree.
     * @param parent BinaryAVLNode280 the parent node.
     * @param child BinaryAVLNode 280 the child of parent.
     */
    protected void deleteRecurse(I x, BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) {
        BinaryAVLNode280<I> temp; // variable to store inorder successor item.
        if (child != null) {
            // now follow recursive deletion algorithm given in class.
            if(x.compareTo(child.item) == 0) {
                // found the item to delete in the tree. looking at the nodes children.
                if(child.getMaxHeight() <= 2) {
                    // item has zero or 1 children. delete trivially.
                    // determine which node to delete from parent
                    if(child.item.compareTo(parent.item) <= 0) {
                        // update parents left node
                        parent.leftNode = null;
                    } else {
                        // update parents right node
                        parent.rightNode = null;
                    }
                    // update height now
                } else {
                    if (child.leftNode == null) {
                        // no left node to swap with.
                        // replace child with inorder successor from right subtree
                        temp = inOrderSuccessor(child,child.rightNode);
                        temp.setRightNode(child.rightNode);
                        if(child.item.compareTo(parent.item) < 0) {
                            //checking if parent node needs to update left node
                            parent.setLeftNode(temp);
                        } else if (child.item.compareTo(parent.item) > 0) {
                            //checking if parent node needs to update right node
                            parent.setRightNode(temp);
                        }
                        updateHeight(temp);
                    } else {
                        // replace child with inorder successor from left subtree.
                        temp = inOrderSuccessor(child, child.leftNode);
                        temp.setLeftNode(child.leftNode);
                        temp.setRightNode(child.rightNode);
                        if(child.item.compareTo(parent.item) < 0) {
                            //checking if parent node needs to update left node
                            parent.setLeftNode(temp);
                        } else if (child.item.compareTo(parent.item) > 0) {
                            //checking if parent node needs to update right node
                            parent.setRightNode(temp);
                        }
                        updateHeight(temp);
                    }
                }
            } else {
                // item not found decide which subtee to recurse next
                if(x.compareTo(child.item) <= 0) {
                    // recurse left tree
                    deleteRecurse(x, child, child.leftNode);
                } else {
                    // recurse right tree.
                    deleteRecurse(x, child, child.rightNode);
                }
            }
        } else {
            //special case where rootNode is to be deleted, but has left or right nodes.
            if (parent.leftNode == null) {
                // no left node to swap with.
                //replace rootNode with inorder successor item from the right subtree.
                temp = inOrderSuccessor(rootNode, rootNode.rightNode);

                setRootNode(temp);
            } else {
                // replace rootNode with the inorder successor item from left subtee.
                temp = inOrderSuccessor(rootNode, rootNode.leftNode);
                setRootNode(temp);
            }
            updateHeight(rootNode);
        }
        updateHeight(parent);
        updateHeight(child);
        restoreAVLProperty(parent,child);
    }

    /**
     * Updates the height of a node after a rotation based on its children.
     * Analysis: Time = O(1)
     * @param root BinaryAVLNode280 the node to update the height of.
     */
    protected void updateHeight(BinaryAVLNode280<I> root) {
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
     * Recursively obtain the in order successor item of a node then
     * deletes the parents reference to it.
     * Analysis: Time = O(log n)
     * @param parent BinaryAVLNode280 which is the parent node of child.
     * @param child BinaryAVLNode280 the child of parent
     * @return The in order successor item of a node.
     */
    protected BinaryAVLNode280<I> inOrderSuccessor(BinaryAVLNode280<I> parent, BinaryAVLNode280<I> child) {
        if(child.rightNode == null) {
            // found the inorder successor updating parent rightnode to be null
            // updating heights also
            parent.setRightNode(null);
            updateHeight(parent);
            updateHeight(child);
            return child;
        } else {
            return inOrderSuccessor(child, child.rightNode);
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

        boolean temp = false;
        System.out.println("\nTesting insertion of a duplicate item");
        try{
            tree.insert(120);
        } catch( DuplicateItems280Exception e) {
            temp = true;
        }

        if(temp)
            System.out.println("PASS");
        else
            System.out.println("FAIL");
    }
}

