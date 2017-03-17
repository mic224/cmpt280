
// Assignment #7
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
    GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;

    /**
     * Create a new union-find structure.
     *
     * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
     * @postcond The structure is initialized such that each element is in its own subset.
     */
    public UnionFind280(int numElements) {
        G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
        G.ensureVertices(numElements);
    }

    /**
     * Return the representative element (equivalence class) of a given element.
     *
     * @param id The elements whose equivalence class we wish to find.
     * @return The representative element (equivalence class) of the element 'id'.
     */
    public int find(int id) {

        // Follow the chain of directed edges starting from id
        int x = id;

        // iterate the edges of the UnionFind graph
        G.eGoFirst(G.vertex(x));
        while (!G.eAfter()) {
            if (G.eItemExists()) {
                // get the next item and continue until a vertex with no
                // edges
                x = G.eItemAdjacentVertex().index();
            } else {
                break;
            }
            G.eGoForth();
        }
        // Since at this  point x has no  outgoing  edge , it must be the
        //  representative  element  of the  set to which a belongs , so..
        return x;
    }

    /**
     * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
     *
     * @param id1 First element.
     * @param id2 Second element.
     */
    public void union(int id1, int id2) {
        if (find(id1) == find(id2)) {
            // If a and b are already in the same set , do nothing
            return;
        }
        // Otherwise , merge the set
        G.addEdge(find(id1), find(id2));
    }


}
