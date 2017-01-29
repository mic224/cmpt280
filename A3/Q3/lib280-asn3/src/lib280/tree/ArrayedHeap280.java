package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by michael on 28/01/17.
 */
public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    public ArrayedHeap280(int cap) {
        super(cap);
        items = (I[]) new Comparable[capacity + 1];
    }

    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        if (this.isFull()) throw new ContainerFull280Exception("Cannot add item to a tree that is full.");
        else {
            count++;
            items[count] = x;
            currentNode = count;

            while (currentNode > 1) {
                if (items[findParent(currentNode)].compareTo(items[currentNode]) < 0) {
                    items[currentNode] = items[findParent(currentNode)];
                    items[findParent(currentNode)] = x;
                    currentNode = findParent(currentNode);
                } else {
                    currentNode = 1;
                }
            }

        }
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {

    }

    public static void main(String[] args) {
        System.out.println("Testing initial heap:");
        ArrayedHeap280<Integer> heap = new ArrayedHeap280<>(15);

        if ((heap == null) || (!heap.isEmpty()) || (heap.capacity() != 16)) {
            System.out.println("\t Failed.\n");
        } else {
            System.out.println("\t Passed.\n");
        }

        System.out.println("Testing insert():");
        //generating the heap from tutorial.
        heap.insert(101);
        heap.insert(75);
        heap.insert(88);
        heap.insert(61);
        heap.insert(43);
        heap.insert(67);
        heap.insert(79);
        heap.insert(17);
        heap.insert(3);
        heap.insert(8);

        heap.currentNode = heap.count;
        int testItem = heap.item();
        if ((heap.isEmpty()) || (heap.count != 10) || (testItem != 8)) {
            System.out.println("\t Failed: last item should be 8.");
        } else {
            System.out.println("\t Passed.");
        }

        //inserting offending node.
        heap.insert(81);

        heap.currentNode = heap.count;
        testItem = heap.item();
        if ((heap.isEmpty()) || (heap.count != 11) || (testItem != 43)) {
            System.out.println("\t Failed: last item should be 43");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 2;
        testItem = heap.item();
        if (testItem != 81) {
            System.out.println("\t Failed: item 2 should be 81");
        } else {
            System.out.println("\t Passed.");
        }


        System.out.println("\n Testing inserting biggest valued node at the farthest leaf.");
        heap.insert(230);

        heap.currentNode = heap.count;
        testItem = heap.item();
        if (testItem != 67) {
            System.out.println("\t Failed: last item should be 67");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 6;
        testItem = heap.item();
        if (testItem != 88) {
            System.out.println("\t Failed: item 6 should be 88.");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 3;
        testItem = heap.item();
        if (testItem != 101) {
            System.out.println("\t Failed: item 3 should be 101.");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 1;
        testItem = heap.item();
        if (testItem != 230) {
            System.out.println("\t Failed: root node should be 230.");
        } else {
            System.out.println("\t Passed.");
        }


        System.out.println("\n Testing inserting capacity of the tree");
        heap.insert(5);
        heap.insert(3);
        heap.insert(2);

        try {
            heap.insert(44);
        } catch (ContainerFull280Exception e) {
            System.out.println("\t Passed.");
        }

        System.out.println("\n Testing deletion from tree.") ;
        heap.deleteItem();

        heap.currentNode = heap.count;
        testItem = heap.item();
        if(heap.currentNode != 14) {
            System.out.println("\t Failed: number of elements in heap should be 15.");
        } else {
            System.out.println("\t Passed.");
        }

        if(testItem != 2) {
            System.out.println("\t Failed: last item should be 2.");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 7;
        testItem = heap.item();
        if(testItem != 3) {
            System.out.println("\t Failed: item 7 should be 3.");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 3;
        testItem = heap.item();
        if(testItem != 79) {
            System.out.println("\t Failed: item 3 should be 79.");
        } else {
            System.out.println("\t Passed.");
        }

        heap.currentNode = 1;
        testItem = heap.item();
        if(testItem != 101) {
            System.out.println("\t Failed: root node should be 101.");
        } else {
            System.out.println("\t Passed.");
        }

        System.out.println("\n Testing deletion of all items in tree.");
        while(heap.count > 0) {
            heap.deleteItem();
        }

        if(!heap.isEmpty()) {
            System.out.println("\t Failed: tree should now be empty.");
        } else {
            System.out.println("\t Passed.");
        }

        System.out.println(heap.toString());
    }

}
