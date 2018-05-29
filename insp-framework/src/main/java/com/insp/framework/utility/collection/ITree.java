/**
 * 
 */
package com.insp.framework.utility.collection;

import java.util.List;

/**
 * @author admin
 *
 */
public interface ITree<T> {
	default T getParent() {
		return null;
	}
	default List<T> getChilds(){
		return null;
	}
}
