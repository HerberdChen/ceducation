package com.insp.framework.utility.text;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;



public class StringUtils {
	public final static char[] Numbers = new char[] {'0','1','2','3','4','5','6','7','8','9'};
	
	static {
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class arg0, Object arg1) {
				// TODO Auto-generated method stub
				return format(arg1);
			}			
		}, String.class);
	}
	/**
	 * 判断是否无效字符串
	 * @param s
	 * @return
	 */
	public static boolean empty(String s) {
		return s == null || s.isEmpty();
	}	
	
	
	/**
	 * 将对象集合转换为字符串数组
	 * @param list
	 * @return
	 */
	public static String[] fromListToStringArray(List<?> list) {
		if(list == null)
			return null;
		
		return (String[]) list.stream().map(x->ConvertUtils.convert(x)).toArray(size -> new String[size]);
	}
	/**
	 * 将多个数组合并成一个 
	 * @param strss
	 * @return
	 */
	public static String[] merge(String[] ...strss) {
		List<String> list = new ArrayList<String>();
		for(String[] strs :strss)
			for(String str : strs)
				list.add(str);
		return list.toArray(new String[list.size()]);
		
	}
	/**
	 * 将字符串数组中的每一个依次spilt
	 * @param strs
	 * @param sep
	 * @return
	 */
	public static List<String[]> spilts(String[] strs,String sep) {
		if(strs == null || strs.length<=0)
			return new ArrayList<String[]>();
		List<String[]> results = new  ArrayList<String[]>();
		for(String str : strs) {
			if(str == null || str.equals(""))continue;
			String[] ss = str.split(sep);
			if(ss == null || ss.length<=0)continue;
			for(int i=0;i<ss.length;i++)
				ss[i] = ss[i].trim();
			results.add(ss);
		}
		return results;
	}
	public static <T> T parse(Class<T> clazz,String str) {
		return parse(clazz,str,",");
	}
	public static <T> T parse(Class<T> clazz,String str,String sep) {
		if(empty(str))
			return null;
		return parse(clazz,str.split(sep));			
	}
	
	public static <T> T parse(Class<T> clazz,String[] strs) {
		if(strs==null||strs.length<=0)
			return null;

		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final T object = obj;
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {
			private int index = 0;
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				// TODO Auto-generated method stub
				Text textAnn = field.getAnnotation(Text.class);
				if(textAnn != null && textAnn.ignore())
					return;

				field.setAccessible(true);
				Object value = ConvertUtils.convert(strs[index++], field.getType());
				field.set(object, value);
				//ReflectionUtils.setField(field, object, value);						
			}
		});
		
		return object;
	}
	public static String format(Object obj) {
		return format(obj,"");
	}
	public static String format(Object obj,String format) {
		if(obj == null)return "";
		if(obj.getClass()==String.class)
			return obj.toString();
		if(obj.getClass() == Integer.class) {
			if(empty(format))
				return obj.toString();
			if(format.equalsIgnoreCase("2"))
				return Integer.toBinaryString((Integer)obj);
			else if(format.equalsIgnoreCase("8"))
				return Integer.toOctalString((Integer)obj);
			else if(format.equalsIgnoreCase("16")||format.equalsIgnoreCase("h"))
				return Integer.toHexString((Integer)obj);
			return obj.toString();
		}else if(obj.getClass() == Float.class || (obj.getClass() == Double.class)){
			if(empty(format))
				return obj.toString();
			DecimalFormat ftm = new DecimalFormat(format);
			return ftm.format((double)obj);
		}else if(obj.getClass() == java.util.Date.class) {
			if(empty(format))
				format = "yyyy-MM-dd hh:mm:ss";
			SimpleDateFormat fmt = new SimpleDateFormat(format);
			return fmt.format((Date)obj);
		}else
			return obj.toString();
	}
	
	public static int[] indexOf(String str,String cs) {
		int index = str.indexOf(cs);
		if(index<=0)
			return null;
		return new int[] {index,index+cs.length()};
	}
	public static Integer[] indexOfNumber(String str) {
		return indexOfNumber(0,str);
	}
	public static Integer[] indexOfNumber(int start,String str) {
		return indexOf(str,start,Numbers);
	}
	
	
	
	
	public static Integer[] indexOf(String str,int start,char[] cs) {
		char[] arrs = str.toCharArray();
		List<Integer> results = new ArrayList<Integer>();
				
		List<Character> list = new ArrayList<Character>();
		for(char c : cs)list.add(c);
			
		
		int index  = start;
		int pos = 0;//0表示正在寻找开始，1表示正在寻找结束
		while(index<arrs.length) {
			if(list.contains(arrs[index])) {
				if(pos == 0) {
					results.add(index);
					pos = 1;
				}				
			}else {
				if(pos == 1) {
					results.add(index-1);
					pos = 0;
				}
			}
			index++;
		}
		
		return results.toArray(new Integer[results.size()]);
	}
	
	
	
}
