/* KeyedAccess280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.dictionary;

import lib280.base.CursorSaving280;
import lib280.base.KeyedLinearIterator280;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**	An interface for a keyed Dictionary with most of the methods,
	except for insertion.  It also includes a KeyedLinearIterator280 
	to move through the dictionary, deleteItem to delete the current 
	item, a search procedure, and a frequency function. */
public interface KeyedAccess280<K extends Comparable<? super K>, I> 
				extends KeyedBasicAccess280<K, I>, KeyedLinearIterator280<K, I>, CursorSaving280
{
	/**	
	 * Move to the item with key k or else set to !itemExists.
	 * @param k key being sought 
	 */
	public void search(K k);

	/**	
	 * Move to the first item with key greater than or equal to k.
	 * @param k key being sought 
	 */
	public void searchCeilingOf(K k);

	/**	
	 * Delete the current item from the data structure. 
	 * @precond itemExists()
	 * @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 */
	public void deleteItem() throws NoCurrentItem280Exception;

	/**	
	 * The number of times key k occurs within the dictionary. 
	 * @param k item to check how often it occurs in the dictionary 
	 */
	public int frequency(K k);

	/**	
	 * Replace the current item with another item having the same key.
	 * @precond itemExists() 
	 * @precond x.key().compareTo(itemKey()) == 0
	 * @param x item to replace the current item
	 * @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 * @throws InvalidArgument280Exception if the key of x does not match the key of the item at the cursor.
	 */
	public void setItem(I x) throws NoCurrentItem280Exception, InvalidArgument280Exception;
} 
