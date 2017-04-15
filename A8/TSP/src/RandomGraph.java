
import java.io.*;

// Generate a random graph where every edge has a random positive weight.

public class RandomGraph {

	public static void writeRandomGraph(int numVertices, String filename) throws IOException {
	
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(filename)));
		    writer.write("" + numVertices + "\n");
		} catch (IOException ex) {
			System.out.println("Cant open " + filename);
			writer.close();
			return;
		} 
		
		for(int i=1; i <= numVertices; i++) {
			for(int j = i+1; j <= numVertices; j++) {
				 // Edge (i,j) gets a random weight between 1 and 100.
				try {
					writer.write("" + i + " " + j + " " +  (Math.round(Math.random() * 99.0) + 1.0) + "\n");
				}
				catch (IOException ex) {
					System.out.println("Error writing to " + filename);
					writer.close();
					return;
				} 				
			}
		}
		
		writer.close();
	}
	
	public static void main(String args[]) {
		try {
			writeRandomGraph(20, "graph20.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
