// Assignment #3 Question 4
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;
import lib280.tree.MAryNode280;

public class SkillTree extends BasicMAryTree280<Skill> {

    /**
     * Create lib280.tree with the specified root node and
     * specified maximum arity of nodes.
     *
     * @param x item to set as the root node
     * @param m number of children allowed for future nodes
     * @timing O(1)
     */
    public SkillTree(Skill x, int m) {
        super(x, m);
    }

    /**
     * A convenience method that avoids typecasts.
     * Obtains a subtree of the root.
     *
     * @param i Index of the desired subtree of the root.
     * @return the i-th subtree of the root.
     */
    public SkillTree rootSubTree(int i) {
        return (SkillTree) super.rootSubtree(i);
    }

    /**
     * A helpful method for adding subtrees to a skill tree.
     * Adds a subtree to the root.
     *
     * @param tree The root tree.
     * @param name String name for the new skill to be added to the root.
     * @param disc String discription of the new skill.
     * @param cost int Cost of the new skill
     */
    public void setTreeNode(SkillTree tree, String name, String disc, int cost) {
        Skill s = new Skill(name, disc, cost);
        SkillTree treeNode = new SkillTree(s, tree.futureArity());

        int insertionPosition = tree.rootLastNonEmptyChild() + 1;

        if (insertionPosition > tree.futureArity()) {
            System.out.println("ERROR.");
        } else {
            tree.setRootSubtree(treeNode, insertionPosition);
        }
    }

    /**
     * Lists the dependencies for a given skill.
     *
     * @param name String the given skill to list depencies of.
     * @return LinkedList280 representing all skill depencies of name including name.
     * @precond the given skill exists.
     */
    public LinkedList280<Skill> skillDependencies(String name) throws RuntimeException {
        LinkedList280<Skill> s = new LinkedList280<>();
        treeSearch(s, this.rootNode, name);
        if(s.isEmpty()) {
            throw new RuntimeException("Error: item with name: \" " + name + " \" does not exist.");
        }
        s.insert(this.rootItem());
        return s;
    }

    /**
     * Recursive function to search the tree for a given item.
     * Compile a LinkedList with the given item as well as its
     * dependencies.
     *
     * @param list    The list to populate with skill & all dependencies.
     * @param curNode The current node the recursive function is working on.
     * @param name    String with the given items name.
     */
    protected void treeSearch(LinkedList280<Skill> list, MAryNode280 curNode, String name) {
        Skill parent;
        Skill child;
        if (curNode != null) {
            parent = (Skill) curNode.item();

            for (int i = 1; i <= curNode.count(); i++) {
                if (curNode.subnode(i) != null) {
                    child = (Skill) curNode.subnode(i).item();

                    if (child.getSkillName() == name) {
                        list.insert(child);
                        treeSearch(list, this.rootNode, parent.getSkillName());
                    }

                    treeSearch(list, curNode.subnode(i), name);
                }
            }
        }
    }

    /**
     * Calculate the total cost to obtain a given skill in the tree.
     *
     * @param name String the given skill to list depencies of.
     * @return int Total cost to obtain given skill.
     * @precond the given skill exists.
     */
    public int skillTotalCost(String name) {
        LinkedList280<Skill> s = new LinkedList280<>();
        int total = 0;
        s = skillDependencies(name);

        s.goFirst();
        while(!s.after()) {
            total += s.item().getSkillCost();
            s.goForth();
        }
        return total;
    }

    public static void main(String[] args) {
        Skill offense = new Skill("Offense", "Main offensive skills.", 1);
        SkillTree offenseTree = new SkillTree(offense, 3);

        offenseTree.setTreeNode(offenseTree, "Power", "Power Skills.", 1);
        offenseTree.setTreeNode(offenseTree, "Speed", "Speed Skills.", 1);
        offenseTree.setTreeNode(offenseTree, "Accuracy", "Accuracy Skills.", 1);

        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Bash",
                "A powerful melee attack.", 2);
        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Armor Pierce",
                "Temporarily reduce enemy armor.", 1);
        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Massive Attack",
                "A massive attack that uses lots of energy.", 1);

        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Flury",
                "A series of quick fast attacks.", 1);
        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Rebound",
                "Counter an enemy attack.", 2);
        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Scatter Shot",
                "Do splash damage to multiple enemies", 3);

        offenseTree.rootSubTree(3).setTreeNode(offenseTree.rootSubTree(3), "Target",
                "Increase hit rate making it easier to hit enemies.", 3);
        offenseTree.rootSubTree(3).setTreeNode(offenseTree.rootSubTree(3), "Snipe",
                "Accurate hit.", 3);

        Skill eyeGouge = new Skill("Eye Gouge","Quick cheap shot.", 5);
        SkillTree eyeGougeTree = new SkillTree(eyeGouge, 0);
        offenseTree.rootSubTree(3).rootSubTree(2).setRootSubtree(eyeGougeTree, 2);

        LinkedList280<Skill> dependencies;
        System.out.println(offenseTree.toStringByLevel());
        dependencies = offenseTree.skillDependencies("Eye Gouge");

        if (dependencies.isEmpty()) {
            System.out.println("Failed: List Should Not Be Empty.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goFirst();
        if (dependencies.item().getSkillName() != "Offense") {
            System.out.println("Failed: Root Dependecy Should be Offense.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goForth();
        if (dependencies.item().getSkillName() != "Accuracy") {
            System.out.println("Failed: Second Dependency Should be Accuracy.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goForth();
        if (dependencies.item().getSkillName() != "Snipe") {
            System.out.println("Failed: Third Dependency Should be Snipe.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goForth();
        if (dependencies.item().getSkillName() != "Eye Gouge") {
            System.out.println("Failed: Last Dependency Should be Eye Gouge.");
        } else {
            System.out.println("PASSED");
        }

        try {
            dependencies = offenseTree.skillDependencies("FAKE");
        } catch (RuntimeException e) {
            System.out.println("PASSED");
        }

        if(offenseTree.skillTotalCost("Eye Gouge") == 10) {
            System.out.println("PASSED");
        } else {
            System.out.println("Failed: Cost of Eye Gouge should be 10.");
        }

        try {
            int i = offenseTree.skillTotalCost("FAKE");
        } catch (RuntimeException e) {
            System.out.println("PASSED");
        }


        System.out.println(offenseTree.toStringByLevel());
        System.out.println("\nDependencies for Eye Gouge: ");
        System.out.println(dependencies.toString());
        System.out.println("To get Eye Gouge you must invest " + offenseTree.skillTotalCost("Eye Gouge") + " points.");

    }

}
