import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class RandomKnapsackInstance {
	public static void main(String args[]) throws IOException {
		
		final int MAX_W = 100;    // Largest possible capacity of backpack.
		final int NUMBER_OF_ITEMS = 700;  // Number of items to be considered for inclusion in the backpack
		final double MAX_ITEM_VALUE = 1000.0; // Largest possible item value
		final double MAX_ITEM_WEIGHT = MAX_W; // Largest possible item weight.
		
		// Filename is determined by the number of items.
		final String fileName = "knapsack-" + NUMBER_OF_ITEMS + ".dat";


		// Open file for writing.
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName)));
			// Write out backpack capacity - random number between 0 and MAX_W
			writer.write("" + Math.random()* MAX_W  + "\n");
			// Write out number of items 
			writer.write("" + NUMBER_OF_ITEMS + "\n");
		} catch (IOException ex) {
			System.out.println("Cant open " + fileName);
			writer.close();
			return;
		} 

		// Generate NUMBER_OF_ITEMS random items and write them to the file.
		for(int i=0; i < NUMBER_OF_ITEMS; i++) {
			// Write the item's value -- random number between 0 and MAX_ITEM_VALUE
			writer.write(Double.toString(Math.random() * MAX_ITEM_VALUE));
			writer.write(" ");
			
			// Write the items's weight -- random number between 0 and MAX_ITEM_WEIGHT
			writer.write(Double.toString(Math.random() * MAX_ITEM_WEIGHT));
			writer.write("\n");
		}


		writer.close();
	}
}
