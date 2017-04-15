//CMPT 214 : Assignment 8
//        NAME: Michael Coquet
//        NSID: mic224
//        STUDENT#: 11164232
//        LAB SECTION: T05
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * A non-public class that stores an item's value and weight
 */
class Item implements Comparable<Item> {

    protected Double value;
    protected Double weight;

    Item(Double v, Double w) {
        value = v;
        weight = w;
    }

    @Override
    public int compareTo(Item o) {
        return this.value.compareTo(o.value);
    }

    /**
     * @return the value
     */
    public Double value() {
        return value;
    }

    /**
     * @return the weight
     */
    public Double weight() {
        return weight;
    }

}

/**
 * A non-public class that stores an instance of Knapsack.
 */
class KnapsackInstance {
    /**
     * The number of items in the problem instance.
     */
    protected Integer numItems;

    /**
     * The items to be considered.
     */
    Item items[];

    /**
     * The capacity of the knapsack in the problem instance.
     */
    protected Double W;

    /**
     * Initialize a knapsack instance.
     *
     * @param numItems Number of items in the problem instance
     * @param W        Capacity of the backpack.
     */
    KnapsackInstance(Integer numItems, Double W) {
        this.numItems = numItems;
        this.W = W;
        this.items = new Item[this.numItems];
    }

    /**
     * @return The number of items in the problem instance.
     */
    public Integer numItems() {
        return this.numItems;
    }

    /**
     * Set the value and weight of the id-th item.
     */
    public void setItem(Double value, Double weight, Integer id) {
        this.items[id] = new Item(value, weight);
    }

    /**
     * Obtain an item's value
     */
    public Double value(int i) {
        return this.items[i].value();
    }

    /**
     * Obtain and item's weight
     */
    public Double weight(int i) {
        return this.items[i].weight();
    }

    /**
     * Obtain the knapsack's capacity
     */
    public Double capacity() {
        return this.W;
    }

    /**
     * Obtain the array of items.
     */
    public Item[] items() {
        return this.items;
    }

    /**
     * Printable representation of the problem instance.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        result.append("W = " + this.W + "\n");
        result.append("Value, Weight\n");
        for (int i = 0; i < this.numItems; i++) {
            result.append(this.items[i].value + ", " + this.items[i].weight + "\n");
        }
        return result.toString();
    }

}


public class Knapsack {

    public static KnapsackInstance readKnapsackInstance(String filename) {

        Scanner infile = null;
        try {
            infile = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + filename + " could not be opened.");
        }

        // Try to read the knapsack capacity and the number of items.
        if (!infile.hasNextDouble())
            throw new RuntimeException("Error: expected knapsack weight as a real number");
        Double W = infile.nextDouble();

        if (!infile.hasNextInt())
            throw new RuntimeException("Error: expected integer number of items.");
        Integer numItems = infile.nextInt();

        // Create a knapsack instance for the given number of items.
        KnapsackInstance K = new KnapsackInstance(numItems, W);

        // Read each value-weight pair.
        for (int i = 0; i < numItems; i++) {
            if (!infile.hasNextDouble())
                throw new RuntimeException("Error: expected a value while reading item " + i + ".");
            Double v = infile.nextDouble();
            if (!infile.hasNextDouble())
                throw new RuntimeException("Error: expected a weight while reading item " + i + ".");
            Double w = infile.nextDouble();

            // Store the value-weight pair in the problem instance.
            K.setItem(v, w, i);
        }

        infile.close();

        return K;
    }

    protected static double currentValue_in_knapsack(KnapsackInstance K) {
        double v = 0;
        for (int i = 0; i < K.numItems(); i++) {
            v = v + (K.value(i) * x[i]);
        }
        return v;
    }

    protected static double partialWieght(KnapsackInstance K, int curIndex) {
        double w = 0;
        for(int i = 0; i < curIndex; i++) {
            w = w + (K.weight(i)*x[i]);
        }
        return w;
    }

    protected static double currentWeight_in_knapsack(KnapsackInstance K) {
        double w = 0;
        for (int i = 0; i < K.numItems(); i++) {
            w = w + (K.weight(i) * x[i]);
        }
        return w;
    }

    static int[] x;
    static double bestValue;
    protected static void Knapsack_Backtracking_helper(KnapsackInstance K, int curIndex, double curWeight) {
        if(curIndex == K.numItems) {
            double curV = currentValue_in_knapsack(K);
            if(curV > bestValue) {
                bestValue = curV;
            }
        } else {
            double W = partialWieght(K, curIndex);
            double kW = K.weight(curIndex);
            if((W + K.weight(curIndex)) <= K.capacity()) {
                x[curIndex] = 1;
                Knapsack_Backtracking_helper(K, curIndex + 1, (W + K.weight(curIndex)));
                x[curIndex] = 0;
                Knapsack_Backtracking_helper(K, curIndex + 1, W);
            } else {
                x[curIndex] = 0;
                Knapsack_Backtracking_helper(K, curIndex + 1, W);
            }
        }
    }

    public static double Knapsack_Backtracking(KnapsackInstance K){
        x = new int[K.numItems()];
        Knapsack_Backtracking_helper(K, 0, 0);
        return bestValue;
    }

    protected static void Knapsack_simple_helper(KnapsackInstance K, int curIndex) {
        if (curIndex == K.numItems) {
            if (currentWeight_in_knapsack(K) <= K.capacity()) {
                double curValue = currentValue_in_knapsack(K);
                if (curValue > bestValue) {
                    bestValue = curValue;
                }
            }
        } else {
            x[curIndex] = 1;
            Knapsack_simple_helper(K, curIndex + 1);
            x[curIndex] = 0;
            Knapsack_simple_helper(K, curIndex + 1);
        }
    }

    public static double Knapsack_Simple(KnapsackInstance K) {
        x = new int[K.numItems()];
        Knapsack_simple_helper(K, 0);
        return bestValue;
    }


    public static void main(String args[]) {
        KnapsackInstance K = readKnapsackInstance("knapsack-basictest.dat");

        // this line is mostly just to prevent a warning that K is unused.  You can
        // delete it when you're ready.  It has the added bonus of letting you see
        // the problem instance.

        System.out.println("Backtracking solution:\nBest Value that can fit in knapsack is: " + Knapsack_Backtracking(K));

    }
}
