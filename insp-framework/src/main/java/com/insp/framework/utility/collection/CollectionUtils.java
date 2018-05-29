package com.insp.framework.utility.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {
	public static boolean empty(Collection<?> coll) {
		return coll == null || coll.size()<=0;
	}
	
	public static <K,V> Map<K,V> toMap(Object...objs){
		Map<K,V> map = new HashMap<K,V>();
		if(objs==null||objs.length<=0)return map;
		int index = 0;
		while(index+1<objs.length) {
			K key = (K)objs[index];
			V value = (V)objs[index+1];
			map.put(key, value);
			index+=1;
		}
		return map;
		
	}
}
