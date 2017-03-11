/* KeyedBasicAccess280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.dictionary;

import lib280.base.Container280;
import lib280.exception.ItemNotFound280Exception;

/**	A Container class with the basic access methods for keyed items.  
	All classes that implement this interface will be sets. */ 
public interface KeyedBasicAccess280<K extends Comparable<? super K>, I> extends Container280
{
	/**	
	 * Does the structure contain key k?.
	 * @param k key whose presence is to be determined 
	 */
	public boolean has(K k);
 
	/**	
	 * The item with key k from the container. 
	 * @precond has(k)
	 * @param k key of the item to be obtained from the dictionary
	 * @throws ItemNotFound280Exception if there is no item with key k in the container.
	 */
	
	public I obtain(K k) throws ItemNotFound280Exception;
 
	/**
	 * Delete the item with key k. 
	 * @precond has(k)
	 * @param k the key of the item to be deleted 
	 * @throws ItemNotFound280Exception if there is no item with key k in the container.
	 * */
	public void delete(K k) throws ItemNotFound280Exception;
} 
