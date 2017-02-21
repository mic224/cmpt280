package lib280.tree;

import lib280.exception.NoCurrentItem280Exception;

public class AVLTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> {

    public AVLTree280() {
        super();
    }

    public AVLTree280(AVLTree280<I> lt, I r, AVLTree280<I> rt)
    {
        super(lt, r, rt);
    }

    @Override
    public void insert(I x) {
        return;
    }

    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        return;
    }

    public static void main(String args[]) {
        
    }
}

