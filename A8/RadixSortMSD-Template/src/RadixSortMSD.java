//CMPT 214 : Assignment 10
//        NAME: Michael Coquet
//        NSID: mic224
//        STUDENT#: 11164232
//        LAB SECTION: T05

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class RadixSortMSD {

    // *************************************************************
    // TODO Write YOUR RADIX SORT HERE
    // *************************************************************

    public static void MsdRadixSort(String[] keys, int R){
        String[] list = new String[keys.length];
        int start = 0, end = keys.length-1, i = 0;
        sort(keys, list, R, start, end, i);
    }

    private static int charAt(String keys, int i){
        if(i<keys.length())return keys.charAt(i);
        else return -1;
    }

    private static void sort(String[] keys, String[] list, int R, int start, int end, int i){

        if(end<=start)return;

        int[] count = new int[R+2];

        for(int k = start; k <= end; k++) {
            count[charAt(keys[k], i)+2]++;
        }

        for(int k = 0; k < R+1; k++) {
            count[k+1] += count[k];
        }

        for(int k = start; k <= end; k++) {
            list[count[charAt(keys[k], i)+1]++] = keys[k];
        }

        for(int k = start; k <= end; k++) {
            keys[k] = list[k-start];
        }

        for(int r = 0; r < R; r++) {
            sort(keys, list, start+count[r], R,start+count[r+1]-1, i+1);
        }
    }

    public static void main(String args[]) {

        // *************************************************************
        // Change the input file by changing the line below.
        // *************************************************************
        String inputFile = "words-basictest.txt";

        // Initialize a scanner to read the input file.
        Scanner S = null;
        try {
            S = new Scanner(new File(inputFile));
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + inputFile + "was not found.");
            return;
        }

        // Read the first line of the file and convert it to an integer to see how many
        // words are in the file.
        int numWords = Integer.valueOf(S.nextLine());

        // Initialize an array large enough to store numWords words.
        String items[] = new String[numWords];

        // Read each word from the input file and store it in the next free element of
        // the items array.
        int j = 0;
        while (S.hasNextLine()) {
            items[j++] = S.nextLine().toUpperCase();
        }
        S.close();
        System.out.println("Done reading " + numWords + " words.");


        // Test and time your radix sort.
        long startTime = System.nanoTime();

        // *************************************************************
        // TODO CALL YOUR RADIX SORT TO SORT THE 'items' ARRAY HERE
        // *************************************************************
        MsdRadixSort(items, 2<<8);
        long stopTime = System.nanoTime();

        // Uncomment this section if you want the sorted list to be printed to the console.
        // (Good idea for testing with words-basictest.txt; leave it commented out though
        // for testing files with more than 50 words).
//
		for(int i=0; i < items.length; i++) {
			System.out.println(items[i]);
		}


        // Print out how long the sort took in milliseconds.
        System.out.println("Sorted " + items.length + " strings in " + (stopTime - startTime) / 1000000.0 + "ms");

    }

}
