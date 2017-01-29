import lib280.base.Pair280;
import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;

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
        return new LinkedList280<>();
    }

    public static void main(String[] args) {
        Skill offense = new Skill("Offense", "Main offensive skills.", 1);
        SkillTree offenseTree = new SkillTree(offense, 3);

        offenseTree.setTreeNode(offenseTree, "Power", "Power Skills.", 2);
        offenseTree.setTreeNode(offenseTree, "Speed", "Speed Skills.", 2);
        offenseTree.setTreeNode(offenseTree, "Accuracy", "Accuracy Skills.", 2);

        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Bash",
                "A powerful melee attack.", 3);
        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Armor Pierce",
                "Temporarily reduce enemy armor.", 3);
        offenseTree.rootSubTree(1).setTreeNode(offenseTree.rootSubTree(1), "Massive Attack",
                "A massive attack that uses lots of energy.", 3);

        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Flury",
                "A series of quick fast attacks.", 3);
        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Rebound",
                "Counter an enemy attack.", 3);
        offenseTree.rootSubTree(2).setTreeNode(offenseTree.rootSubTree(2), "Scatter Shot",
                "Do splash damage to multiple enemies", 3);

        offenseTree.rootSubTree(3).setTreeNode(offenseTree.rootSubTree(3), "Eye Gouge",
                "Quick cheap shot.", 3);
        offenseTree.rootSubTree(3).setTreeNode(offenseTree.rootSubTree(3), "Target",
                "Increase hit rate making it easier to hit enemies.", 3);
        offenseTree.rootSubTree(3).setTreeNode(offenseTree.rootSubTree(3), "Snipe",
                "Accurate hit.", 3);


        LinkedList280<Skill> dependencies = new LinkedList280<>();
        try {
            dependencies = offenseTree.skillDependencies("FAKE");
        } catch (RuntimeException e) {
            System.out.println("ERROR.");
        }

        dependencies = offenseTree.skillDependencies("Eye Gouge");

        if(dependencies.isEmpty()) {
            System.out.println("Failed: List Should Not Be Empty.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goFirst();
        if(dependencies.item().getSkillName() != "Offense") {
            System.out.println("Failed: Root Dependecy Should be Offense.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goForth();
        if(dependencies.item().getSkillName() != "Accuracy") {
            System.out.println("Failed: Second Dependency Should be Accuracy.");
        } else {
            System.out.println("PASSED");
        }

        dependencies.goForth();
        if(dependencies.item().getSkillName() != "Eye Gouge") {
            System.out.println("Failed: Last Dependency Should be Eye Gouge.");
        } else {
            System.out.println("PASSED");
        }
    }

}
