package lib280.tree;

import lib280.exception.NoCurrentItem280Exception;

public class AVLTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> {

    protected int maxHeight;

    public AVLTree280() {
        super();
        maxHeight = 0;
    }

    public AVLTree280(AVLTree280<I> lt, I r, AVLTree280<I> rt) {
        super(lt, r, rt);
        maxHeight = Math.max(lt.maxHeight, rt.maxHeight) + 1;
    }

    @Override
    public void insert(I x) {
        return;
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        return;
    }

    @Override
    protected String toStringByLevel(int i)
    {
        return null;
//        StringBuffer blanks = new StringBuffer((i - 1) * 5);
//        for (int j = 0; j < i - 1; j++)
//            blanks.append("     ");
//
//        String result = new String();
//        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
//            result += rootRightSubtree().toStringByLevel(i+1);
//
//        result += "\n" + blanks + i + ": " ;
//        if (isEmpty())
//            result += "-";
//        else
//        {
//            result += rootItem();
//            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
//                result += rootLeftSubtree().toStringByLevel(i+1);
//        }
//        return result;
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
        } catch(Exception e) {
            test = true;
        }

        if(test)
            System.out.println("2: PASS");
        else
            System.out.println("2: FAIL");

        // inserting the first node for testing should become root item
        tree.insert(42);
        System.out.println("\nInserting 42");
        tree.toStringByLevel(1);

        if (tree.rootItem() == 42) {
            System.out.println("3: PASS");
        } else {
            System.out.println("3: FAIL");
        }

        // inserting another item 120 should become right subtree
        tree.insert(120);
        System.out.println("\nInserting 120");
        tree.toStringByLevel(1);

        if (tree.rootRightSubtree().item() == 120) {
            System.out.println("4: PASS");
        } else {
            System.out.println("4: FAIL");
        }

        // inserting 2 should be left sub tree
        tree.insert(2);
        System.out.println("\nInserting 2");
        tree.toStringByLevel(1);

        if (tree.rootLeftSubtree().item() == 2) {
            System.out.println("5: PASS");
        } else {
            System.out.println("5: FAIL");
        }

        // inserting 4 should be 2's right subtree
        tree.insert(4);
        System.out.println("\nInserting 4");
        tree.toStringByLevel(1);

        if (tree.has(4)) {
            if (tree.rootLeftSubtree().rootRightSubtree().item() == 4)
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
        tree.insert(5);
        System.out.println("\nInserting 5; Testing LL imbalance and right rotation.");
        tree.toStringByLevel(1);

        if (!tree.has(5))
            test = false;
        if (tree.rootLeftSubtree().item() != 4)
            test = false;
        if (tree.rootRightSubtree().item() != 120)
            test = false;
        if (tree.rootLeftSubtree().rootLeftSubtree().item() != 2)
            test = false;
        if (tree.rootLeftSubtree().rootRightSubtree().item() != 5)
            test = false;
        if(test == true)
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
        tree.toStringByLevel(1);

        test = true;
        if(!tree.has(0))
            test = false;
        if(tree.rootItem() != 4)
            test = false;
        if(tree.rootRightSubtree().item() != 42)
            test = false;
        if(tree.rootLeftSubtree().item() != 2)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().item() != 120)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().item() != 5)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().item() != 0)
            test = false;
        if(tree.rootLeftSubtree().rootRightSubtree() != null)
            test = false;

        if(test)
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
        tree.toStringByLevel(1);

        test = true;
        if(!tree.has(1))
            test = false;
        if(tree.rootItem() != 4)
            test = false;
        if(tree.rootRightSubtree().item() != 42)
            test = false;
        if(tree.rootLeftSubtree().item() != 1)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().item() != 120)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().item() != 5)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().item() != 0)
            test = false;
        if(tree.rootLeftSubtree().rootRightSubtree().item() != 2)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree() != null)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree() != null)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootRightSubtree() != null)
            test = false;

        if(test)
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
        tree.toStringByLevel(1);


        test = true;
        if(!tree.has(99) || !tree.has(122) || !tree.has(84))
            test = false;
        if(tree.rootItem() != 4)
            test = false;
        if(tree.rootRightSubtree().item() != 99)
            test = false;
        if(tree.rootLeftSubtree().item() != 1)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().item() != 120)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().item() != 42)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().item() != 0)
            test = false;
        if(tree.rootLeftSubtree().rootRightSubtree().item() != 2)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().rootLeftSubtree() != null)
            test = false;
        if(tree.rootLeftSubtree().rootLeftSubtree().rootRightSubtree() != null)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().item() != 122)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().item() != 84)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().rootLeftSubtree().item() != 5)
            test = false;

        if(test)
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
        tree.toStringByLevel(1);

        test = true;
        if(!tree.has(126))
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().item() != 122)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().item() != 126)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().item() != 120)
            test = false;

        if(test)
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
        tree.toStringByLevel(1);
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
        tree.toStringByLevel(1);

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
        tree.toStringByLevel(1);

        test = true;
        if(tree.rootRightSubtree().item() != 122)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().item() != 126)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().item() != 84)
            test = false;
        if(tree.rootRightSubtree().rootLeftSubtree().rootRightSubtree().item() != 120)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootRightSubtree().item() != null)
            test = false;
        if(tree.rootRightSubtree().rootRightSubtree().rootLeftSubtree().item() != null)
            test = false;

        if(test)
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

        if(test)
            System.out.println("13: FAIL");
        else
            System.out.println("13: PASS");
    }
}

