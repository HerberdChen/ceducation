package com.insp.framework.utility.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.ReflectionUtils;

public interface IListOperator {
	
	@SuppressWarnings("unchecked")
	default <T> List<T> listGet(Class<T> clazz,String fieldName){		
		Field field = ReflectionUtils.findField(this.getClass(), fieldName);
		if(field == null)return null ;
		if(!List.class.isAssignableFrom(field.getType()))
			return null;
		ReflectionUtils.makeAccessible(field);
		List<T> list = (List<T>) ReflectionUtils.getField(field, this);
		if(list == null) {
			list = new ArrayList<T>();
			ReflectionUtils.setField(field, this, list);
		}
		return list;
	}
	@SuppressWarnings("rawtypes")
	default  List<?> listGet(String fieldName){		
		Field field = ReflectionUtils.findField(this.getClass(), fieldName);
		if(field == null)return null ;
		if(!List.class.isAssignableFrom(field.getType()))
			return null;
		ReflectionUtils.makeAccessible(field);
		List list = (List) ReflectionUtils.getField(field, this);
		if(list == null) {
			list = new ArrayList();
			ReflectionUtils.setField(field, this, list);
		}
		return list;
	}
	default int listSize(String fieldName) {
		List<?> list = listGet(fieldName);
		return list==null?0:list.size();
	}
	default void listClear(String fieldName) {
		List<?> list = listGet(fieldName);
		if(list!=null)
			list.clear();
	}
	default <T> void listAdd(Class<T> clazz,String fieldName,T element) {
		List<T> list = listGet(clazz,fieldName);
		if(list!=null)
			list.add(element);
	}
	default <T> void listAddAll(Class<T> clazz,String fieldName,T[] element) {
		List<T> list = listGet(clazz,fieldName);
		if(list==null)
			return;
		for(T e : element)
			list.add(e);	
	}
	default <T> void listInsert(Class<T> clazz,String fieldName,int index,T element) {
		List<T> list = listGet(clazz,fieldName);
		if(list!=null)
			list.add(index,element);
	}
	default <T> void listRemove(Class<T> clazz,String fieldName,int index) {
		List<T> list = listGet(clazz,fieldName);
		if(list!=null)
			list.remove(index);		
	}
}
