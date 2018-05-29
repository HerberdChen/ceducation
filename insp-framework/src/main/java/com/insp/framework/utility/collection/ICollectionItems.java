package com.insp.framework.utility.collection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.insp.framework.utility.reflection.ReflectionUtils;

/**
 * 所有带有IList<T>成员的类的抽象接口
 * 提供listAdd,listClear,listRemove等方法
 * @author admin
 *
 */
public interface ICollectionItems<T> {
	
	/*****************************************************************/
	/***********************取得List成员对象******************************/
	/*****************************************************************/
	 
	/**
	 * 缺省List成员名
	 */
	public final static String LIST_MEMBER_NAME = "items";
	/**
	 * 取得名称为items的list成员
	 * @return 如果找不到，返回null
	 */
	public default List<T> listGet() {
		return listGet(LIST_MEMBER_NAME,false);
		
	}
	
	/**
	 * 取得名称为items的list成员
	 * @param createifnosuchField  如果找不到，返回null还是缺省list对象
	 * @return
	 */
	public default List<T> listGet(boolean createifnosuchField) {
		return listGet(LIST_MEMBER_NAME,createifnosuchField);
		
	}
	
	/**
	 * 取得特定名称的list成员
	 * @param name  成员名称
	 * @return
	 */
	public default List<T> listGet(String name) {
		return listGet(name,false);
		
	}
	
	/**
	 * 取得特定名称的list成员
	 * @param name  成员名称
	 * @param createifnosuchField  如果找不到，返回null还是缺省list对象
	 * @return
	 */
	public default List<T> listGet(String name,boolean createifnosuchField) {
		Field f = null;
		try {
			f = this.getClass().getDeclaredField(name);
			if(f == null)
				if(createifnosuchField)return new ArrayList<T>();					
				else return null;
			return (List<T>) f.get(this);		
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			if(createifnosuchField)return new ArrayList<T>();					
			else return null;
		}
		
	}
	
	/**
	 * 调用list中的方法 
	 * @param methodName 方法名
	 * @param params     方法参数
	 * @return           返回结果
	 */
	public default Object listCall(String methodName,Object...params) {
		return listCall(LIST_MEMBER_NAME,methodName,params);
	}
	
	/*****************************************************************/
	/***********************常用方法*************************************/
	/*****************************************************************/
	/**
	 * 调用list中的方法 
	 * @param listName  list名称
	 * @param methodName 方法名
	 * @param params     方法参数
	 * @return           返回结果
	 */
	public default Object listCall(String listName,String methodName,Object...params) {
		List<T> list = listGet(listName);
		if(list == null)return null;		
		Method m;
		try {
			m = list.getClass().getDeclaredMethod(methodName, ReflectionUtils.getClass(params));
			if(m == null)return null;
			return m.invoke(this, params);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	
	
	public default int listIndexOf(T item) {
		return (int)listCall("indexOf",item);		
	}
	
	public default void listSort() {
		listCall("sort");
	}
}
