package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**
 * Created by michael on 28/01/17.
 */
public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    public ArrayedHeap280(int cap) {
        super(cap);
        items = (I[]) new Comparable[capacity+1];
    }

    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        if( this.isFull() ) throw new ContainerFull280Exception("Cannot add item to a tree that is full.");
        else {
            count++;
            items[count] = x;
            currentNode = count;

            if(currentNode > 1) {
                while (items[findParent(currentNode)].compareTo(items[currentNode]) < 0) {
                    items[currentNode] = items[findParent(currentNode)];
                    items[findParent(currentNode)] = x;
                    currentNode = findParent(currentNode);
                }
            }
        }
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {

    }

    public static void main(String[] args) {
        System.out.println("Testing initial heap:");
        ArrayedHeap280<Integer> heap = new ArrayedHeap280<>(16);

        if( (heap == null) || (!heap.isEmpty()) || (heap.capacity() != 16)) {
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
        if((heap.isEmpty()) || (heap.count != 10) ) {
            System.out.println("\t Failed.\n");
        } else {
            System.out.println("\t Passed.\n");
        }

        //inserting offending node.
        heap.insert(81);
        if((heap.isEmpty()) || (heap.count != 11) ) {
            System.out.println("\t Failed.\n");
        } else {
            System.out.println("\t Passed.\n");
        }



        System.out.println(heap.toString());
    }

}
