import java.util.Arrays;

import lib280.graph.Vertex280;
import lib280.graph.WeightedGraphMatrixRep280;

public class TSP {

	// This is a place where global variables arguably make at least some sense. 
    // Here we a recursive process which needs to access to ONE copy of this global data.  
	// The alternative is to pass these around with every recursive call.
	// We take the approach of passing only the most important global data and some
	// local data that changes with each recursive call that MUST be passed as a parameter
	// (e.g. currentVertex, cost, pathLength).
	public double bestCost;
	public boolean visited[];
	public int sequence[];
	public int bestSequence[];
	public int extensionsTried;
	
	protected void TSPNaiveHelper(WeightedGraphMatrixRep280<Vertex280> G, int startVertex, 
			int currentVertex, double cost, int pathLength) {
		visited[currentVertex] = true;
		sequence[pathLength] = currentVertex;
		
		// If the path has length numVertices-1, and there is an edge from the current node
		// back to the start vertex, then we've found a tour.
		if( pathLength == G.numVertices()-1 && G.isAdjacent(currentVertex, startVertex) ) {
			// if the cost of the tour is better than the best so far...
			if( cost + G.getEdgeWeight(currentVertex, startVertex) < bestCost ) {
				bestCost = cost + G.getEdgeWeight(currentVertex, startVertex);
				bestSequence = Arrays.copyOf(sequence, sequence.length);
			}
		}
		
		// Otherwise, try to extend the path.
		else {
			for(int i = 1; i <= G.numVertices(); i++ ) {
				// Try extending the path.  If i hasn't been visited yet, and there is an edge (i,j),
				// then the extension is feasible.
				if( !visited[i] && G.isAdjacent(currentVertex, i)) {
					extensionsTried++;
					TSPNaiveHelper(G, startVertex, i, cost + G.getEdgeWeight(currentVertex, i), pathLength+1);
				}
			}
		}
		
		visited[currentVertex] = false;
		sequence[pathLength] = 0;
	}
	
	public double TSPNaive(WeightedGraphMatrixRep280<Vertex280> G, int startVertex) {
		// Initialize everything.
		visited = new boolean[G.numVertices()+1];
		sequence = new int[G.numVertices()];
		bestCost = Double.MAX_VALUE;
		extensionsTried = 0;
		visited[startVertex] = true;
		sequence[0] = startVertex;

		// Begin the search for the shortest tour.
		TSPNaiveHelper(G, startVertex, startVertex, 0, 0);
		return bestCost;
	}
	
	
	
	
	protected void TSPPruningWhenCostExceedsBestHelper(WeightedGraphMatrixRep280<Vertex280> G, int startVertex, int currentVertex, double cost, int pathLength) {
		visited[currentVertex] = true;
		sequence[pathLength] = currentVertex;
		
		
		// If the path has length numVertices-1, and there is an edge from the current node
		// back to the start vertex, then we've found a tour.
		if( pathLength == G.numVertices()-1 && G.isAdjacent(currentVertex, startVertex) ) {
			// if the cost of the tour is better than the best so far...
			if( cost + G.getEdgeWeight(currentVertex, startVertex) < bestCost ) {
				bestCost = cost + G.getEdgeWeight(currentVertex, startVertex);
				bestSequence = Arrays.copyOf(sequence, sequence.length);
			}
		}
		
		// Otherwise, try to extend the path, as long as we haven't already exceeded bestCost.
		else if( cost < bestCost ){
			for(int i = 1; i <= G.numVertices(); i++ ) {
				// Try extending the path.  If i hasn't been visited yet, and there is an edge (i,j),
				// then the extension is feasible.
				if( !visited[i] && G.isAdjacent(currentVertex, i)) {
					extensionsTried++;
					TSPPruningWhenCostExceedsBestHelper(G, startVertex, i, cost + G.getEdgeWeight(currentVertex, i), pathLength+1);
				}
			}
		}
		
		visited[currentVertex] = false;
		sequence[pathLength] = 0;
	}
	
	
	public double TSPPruningWhenCostExceedsBest(WeightedGraphMatrixRep280<Vertex280> G, int startVertex) {
		// Initialize everything
		visited = new boolean[G.numVertices()+1];
		sequence = new int[G.numVertices()];
		bestCost = Double.MAX_VALUE;
		extensionsTried = 0;
		visited[startVertex] = true;
		sequence[0] = startVertex;
		
		// Begin the search for the shortest tour.
		TSPPruningWhenCostExceedsBestHelper(G, startVertex, startVertex, 0, 0);
		
		return bestCost;
	}
	
	public static void main(String args[]) {
		WeightedGraphMatrixRep280<Vertex280> G = new WeightedGraphMatrixRep280<Vertex280>(50, false);
		TSP tsp = new TSP();
		
		/******************************************
		 * You can change the input file here:
		 ******************************************/
		G.initGraphFromFile("graph4.dat");
		tsp.TSPNaive(G, 1);
		
		System.out.println("Shortest tour cost is: " + tsp.bestCost);
		System.out.print("Shortest tour is: ");
		for(int i=0; i < tsp.bestSequence.length; i++) {
			System.out.print(tsp.bestSequence[i]);
			if( i < tsp.bestSequence.length-1 ) 
				System.out.print(", ");
		}
		System.out.println();
		System.out.println("" + tsp.extensionsTried + " path extensions were tried.");
		
		
		
		
		tsp.TSPPruningWhenCostExceedsBest(G, 1);
		
		System.out.println("Shortest tour cost is: " + tsp.bestCost);
		System.out.print("Shortest tour is: ");
		for(int i=0; i < tsp.bestSequence.length; i++) {
			System.out.print(tsp.bestSequence[i]);
			if( i < tsp.bestSequence.length-1 ) 
				System.out.print(", ");
		}
		System.out.println();
		System.out.println("" + tsp.extensionsTried + " path extensions were tried.");
	}
}
