// Assignment #7
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

 import lib280.base.LinearIterator280;
 import lib280.base.Pair280;
 import lib280.exception.InvalidArgument280Exception;

 import java.util.Arrays;
 import java.util.InputMismatchException;
 import java.util.Scanner;


 public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
         WeightedGraphAdjListRep280<V> {

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
             String vertexTypeName) {
         super(cap, d, vertexTypeName);
     }

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
         super(cap, d);
     }

//     /**
//      * Replaces the current graph with a graph read from a data file.
//      *
//      * File format is a sequence of integers. The first integer is the total
//      * number of nodes which will be numbered between 1 and n.
//      *
//      * Remaining integers are treated as ordered pairs of (source, destination)
//      * indicies defining graph edges.
//      *
//      * @param      Filename
//      *            Name of the file from which to read the graph.
//      * @precond The weights on the edges in the data file fileName are non negative.
//      * @throws RuntimeException
//      *             if the file format is incorrect, or an edge appears more than
//      *             once in the input.
//      */


     @Override
     public void setEdgeWeight(V v1, V v2, double weight) {
         // Overriding this method to throw an exception if a weight is negative will cause
         // super.initGraphFromFile to throw an exception when it tries to set a weight to
         // something negative.

         // Verify that the weight is non-negative
         if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

         // If it is, then just set the edge weight using the superclass method.
         super.setEdgeWeight(v1, v2, weight);
     }

     @Override
     public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
         // Get the vetex objects associated with each index and pass off to the
         // version of setEdgeWEight that accepts vertex objects.
         this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
     }


     /**
      * Implementation of Dijkstra's algorithm.
      * @param startVertex Start vertex for the single-source shortest paths.
      * @return An array of size G.numVertices()+1 in which offset k contains the shortest
      *         path from startVertex to k.  Offset 0 is unused since vertex indices start
      *         at 1.
      */
     public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {

         double tentativeDistance[] = new double[numVertices+1];
         boolean visited[] = new boolean[numVertices+1];
         int predecessorNode[] = new int[numVertices+1];

         for(int i = 0; i < numVertices+1; i++) {
             tentativeDistance[i] = Double.POSITIVE_INFINITY;
             visited[i] = false;
         }

         tentativeDistance[startVertex] = 0;
         boolean containsFalse = true;
         while(containsFalse) {
             double smallestDistance = Double.POSITIVE_INFINITY;
             int smallestIndex = 1;

             containsFalse = false;
             for(int i = 1; i < visited.length; i++) {
                 if(visited[i] == false) {
                     containsFalse = true;
                     if(tentativeDistance[i] <= smallestDistance) {
                         smallestDistance = tentativeDistance[i];
                         smallestIndex = i;
                     }
                 }
             }
             Vertex280 cur = this.vertex(smallestIndex);
             visited[smallestIndex] = true;

             LinearIterator280 iterator = adjLists[smallestIndex].iterator();
             iterator.goFirst();

             while(!iterator.after()) {
                 WeightedEdge280 curEdge = (WeightedEdge280)iterator.item();
                 int z = curEdge.other(cur).index;
                 if( (!visited[z]) &&
                         (tentativeDistance[z] > (tentativeDistance[cur.index] + getEdgeWeight(cur.index, z)))  ) {
                     tentativeDistance[z] = tentativeDistance[cur.index] + getEdgeWeight(cur.index, z);
                     predecessorNode[z] = cur.index;
                 }
                 iterator.goForth();
             }

         }

         return new Pair280<double[], int[]>(tentativeDistance, predecessorNode);

     }

     // Given a predecessors array output from this.shortestPathDijkatra, return a string
     // that represents a path from the start node to the given destination vertex 'destVertex'.
     private static String extractPath(int[] predecessors, int destVertex) {
         String path = new String();
         int index = destVertex;

         if(predecessors[destVertex] != 0) {
             path += destVertex;
             while (predecessors[index] != 0) {
                 path = predecessors[index] + ", " + path;
                 index = predecessors[index];
             }

             path = "The path to " + destVertex + " is: " + path;

             return path;  // Remove this when you're ready -- this is a placeholder to prevent a compiler error.
         } else {
             return "Not reachable.";
         }
     }

     // Regression Test
     public static void main(String args[]) {
         NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

         if( args.length == 0)
             G.initGraphFromFile("weightedtestgraph.gra");
         	 // If you're using Eclipse and you get an error opening the file, comment
         	 // the line above, and uncomment the line below:
         	 // G.initGraphFromFile("src/lib280/graph/weightedtestgraph.gra");
 
         else
             G.initGraphFromFile(args[0]);

         System.out.println("Enter the number of the start vertex: ");
         Scanner in = new Scanner(System.in);
         int startVertex;
         try {
             startVertex = in.nextInt();
         }
         catch(InputMismatchException e) {
             in.close();
             System.out.println("That's not an integer!");
             return;
         }

         if( startVertex < 1 || startVertex > G.numVertices() ) {
             in.close();
             System.out.println("That's not a valid vertex number for this graph.");
             return;
         }
         in.close();


         Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
         double[] finalDistances = dijkstraResult.firstItem();
         //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
         int[] predecessors = dijkstraResult.secondItem();

         for(int i=1; i < G.numVertices() +1; i++) {
             System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
 //			if( correctDistances[i] != finalDistances[i] )
 //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
 //			else {
                 System.out.println(extractPath(predecessors, i));
 //			}
         }
     }

 }
