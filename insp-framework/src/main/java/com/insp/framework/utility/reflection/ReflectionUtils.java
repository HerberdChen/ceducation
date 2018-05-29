package com.insp.framework.utility.reflection;

import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	public static Class[] getClass(Object...objs) {
		if(objs == null)return null;
		List<Class> clazz = new ArrayList<Class>();
		for(Object obj : objs) {
			if(obj == null)continue;
			clazz.add(obj.getClass());
		}
		return clazz.toArray(new Class[clazz.size()]);
	}
}
 