// Assignment #7
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<Vertex280>(G.capacity(), false);
		UnionFind280 UF = new UnionFind280(G.numVertices() + 1 );

		ArrayedMinHeap280<WeightedEdge280<Vertex280>> minHeap = new ArrayedMinHeap280<>(G.numEdges() * 2);

		// start the iteration at the first vetex.
		G.goFirst();
		while(!G.after()) {
			// adding vertex into the min span tree without any edges.
			minST.addVertex(G.item().index());
			// start iterating the edges of the given vertex in G.
			G.eGoFirst(G.item());
			while(!G.eAfter()) {
				// add the current edge to the min heap, ensuring smallest
				// weight edge appears at the top (sorted in ascending order)
				minHeap.insert(G.eItem());
				G.eGoForth();
			}
			G.goForth();
		}


		for(int i = 0; i < G.numEdges()-1; i++) {
			int fA = minHeap.item().firstItem().index();
			int fB = minHeap.item().secondItem().index();
			int UFA = UF.find(fA);
			int UFB = UF.find(fB);

			if(UFA != UFB) {
				minST.addEdge(fA,fB);
				minST.setEdgeWeight(fA, fB, G.getEdgeWeight(fA, fB));
				UF.union(fA, fB);
			}
			minHeap.deleteItem();
		}
		return minST;  // Remove this when you're ready -- it is just a placeholder to prevent a compiler error.
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		// If you get a file not found error here and you're using eclipse just remove the 
		// 'Kruskal-template/' part from the path string.
		G.initGraphFromFile("mst.graph");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);
		
		System.out.println(minST);
	}
}


