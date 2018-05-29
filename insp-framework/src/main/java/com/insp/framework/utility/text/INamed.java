package com.insp.framework.utility.text;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.ReflectionUtils;

import com.insp.framework.utility.collection.CollectionUtils;

/**
 * 命名类接口
 * @author admin
 *
 */
public interface INamed {
	default String getName() {
		Field field = ReflectionUtils.findField(this.getClass(), "name");
		if(field == null)
			return "";
		field.setAccessible(true);
		return (String) ReflectionUtils.getField(field, this);
	}
	default String getCaption() {
		Field field = ReflectionUtils.findField(this.getClass(), "caption");
		if(field == null)
			return "";
		field.setAccessible(true);
		return (String) ReflectionUtils.getField(field, this);
	}
	default String getDescription() {
		Field field = ReflectionUtils.findField(this.getClass(), "description");
		if(field == null)
			return "";
		field.setAccessible(true);
		return (String) ReflectionUtils.getField(field, this);
	}
	default List<String> getAlias() {
		Field field = ReflectionUtils.findField(this.getClass(), "alias");
		if(field == null)
			return null;
		field.setAccessible(true);
		if(String[].class.isAssignableFrom(field.getType()))
			return  Arrays.asList((String[])ReflectionUtils.getField(field, this));
		else if(List.class.isAssignableFrom(field.getType())) {
			return  (List<String>) ReflectionUtils.getField(field, this);			
		}
		return null;
	}
	
	default List<String> getNames() {
		List<String> alias = this.getAlias();		
		String name = this.getName();
		
		if(CollectionUtils.empty(alias) && StringUtils.empty(name))
			return null;
		else if(!CollectionUtils.empty(alias) && StringUtils.empty(name))
			return alias;
		else if(CollectionUtils.empty(alias) && !StringUtils.empty(name))
			return Arrays.asList(name);
		else {
			alias.add(0, name);
			return alias;
		}		
	}
	
	default boolean hasName(String name) {
		List<String> names = getNames();
		if(CollectionUtils.empty(names))
			return false;
		return names.contains(name);
	}
	default boolean hasName(String name,boolean casesensitive) {
		if(StringUtils.empty(name))
			return false;
		List<String> names = getNames();
		if(CollectionUtils.empty(names))
			return false;
		return names.stream().anyMatch(x -> (casesensitive && name.equals(x)) || (!casesensitive && name.equalsIgnoreCase(x)));		
	}
}
