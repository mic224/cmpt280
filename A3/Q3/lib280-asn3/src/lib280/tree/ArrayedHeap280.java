package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**
 * Created by michael on 28/01/17.
 */
public class ArrayedHeap280<I> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {


    public ArrayedHeap280(int cap) {
        super(cap);
    }

    @Override
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {

    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {

    }

    public static void main(String[] args) {

    }

}
