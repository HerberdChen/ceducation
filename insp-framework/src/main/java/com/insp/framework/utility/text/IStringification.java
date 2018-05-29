package com.insp.framework.utility.text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * @author admin
 *
 */
public interface IStringification {
	
	String FORMAT_NAME = "[$Name]";
	String FORMAT_V = "[$Value]";
	String FORMAT_PV = "[$Property] = [$Value],[$Property] = [$Value]";
	String FORMAT_CSV = "[$Value]";
	
	 
	default String toPVText() {
		List<Field> fs = new ArrayList<Field>();
		Map<Field,Object> fsv = new HashMap<Field,Object>();
		final Object owner = this; 
		
		ReflectionUtils.doWithFields(this.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {				
				// TODO Auto-generated method stub
				Text textAnn = field.getAnnotation(Text.class);
				if(textAnn != null && textAnn.ignore())
					return;
				field.setAccessible(true);
				Object value = field.get(owner);
				fs.add(field);
				fsv.put(field, value);				
			}
		});
		if(fs.size()<=0)
			return "";
		return fs.stream().map(x->x+"="+StringUtils.format(fsv.get(x))).reduce((a,b)->a+","+b).get();
	}
	default String toCsv() {
		return toCsv(",");
	}
	default String toCsv(String sep) {
		
		List<Field> fs = new ArrayList<Field>();
		Map<Field,Object> fsv = new HashMap<Field,Object>();
		final Object owner = this; 
		
		ReflectionUtils.doWithFields(this.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {				
				// TODO Auto-generated method stub
				Text textAnn = field.getAnnotation(Text.class);
				if(textAnn != null && textAnn.ignore())
					return;
				field.setAccessible(true);
				Object value = field.get(owner);
				fs.add(field);
				fsv.put(field, value);				
			}
		});
		if(fs.size()<=0)
			return "";
		return fs.stream().map(x->StringUtils.format(fsv.get(x))).reduce((a,b)->a+","+b).get();
	}

}
