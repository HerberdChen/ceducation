/**
 * 
 */
package com.insp.framework.utility.dict;

import java.util.ArrayList;
import java.util.List;

import com.insp.framework.utility.collection.ICollectionItems;
import com.insp.framework.utility.text.PY;

/**
 * 数据字典
 * @author admin
 *
 */
public class Dictionary implements ICollectionItems<Dictionary.Item>{
	/**
	 * 编码规则
	 */
	private EncodeRule rule;
	/**
	 * 字典数据
	 */
	private final List<Item> items = new ArrayList<Item>();
	
	/**
	 * 构造方法
	 */
	public Dictionary() {
		rule = new EncodeRule();
	}
	/**
	 * 构造方法
	 * @param type  类型
	 * @param clazzLength  每个类的编码长度
	 */
	public Dictionary(int type,int...clazzLength) {
		rule = new EncodeRule(type,clazzLength);
	}
	/**
	 * 添加活着修改代码
	 * @param code
	 * @param value
	 * @param pcode
	 * @param abbr
	 * @return
	 */
	public Dictionary set(String code,String value,String pcode,String abbr) {
		Item item = new Item(code,value,pcode,abbr);
		int index = this.listIndexOf(item);
		if(index < 0) {
			this.items.add(item);			
		}else {
			this.items.set(index, item);
		}
			
		return this;
	}
	
	/**
	 * 编码规则
	 * @author admin
	 *
	 */
	class EncodeRule{
		/** 无规则 */
		public static final int NONE = 0;
		/** 按照父子类对其 */
		public static final int ALIGN = 1;
		/** 按照父子类 */
		public static final int CLASS = 2;
		
		/** 类型 */
		public final int type;
		/** 每个类的编码长度 */
		public final int[] clazzLength;
		
		/**
		 * 构造方法
		 */
		public EncodeRule() {
			type = NONE;
			clazzLength = new int[0];
		}
		/**
		 * 构造方法
		 * @param type  类型
		 * @param clazzLength  每个类的编码长度
		 */
		public EncodeRule(int type,int...clazzLength) {
			this.type = type;
			this.clazzLength = clazzLength;
		}
	}
	
	/**
	 * 字典项
	 * @author admin
	 *
	 */
	public static class Item implements Comparable<Item>{
		/** 编码 */
		public final String code;
		/** 值 */
		public final String value;
		/** 父编码 */
		public final String pcode;
		/** 拼音缩写 */
		public final String abbr;
		
		/**
		 * 构造方法
		 * @param code   编码
		 * @param value  值
		 * @param pcode  父编码
		 */
		public Item(String code,String value) {
			this.code = code;
			this.value = value;
			this.pcode = "";
			this.abbr = PY.getAbbrFromText(value);
		}
		
		/**
		 * 构造方法
		 * @param code   编码
		 * @param value  值
		 * @param pcode  父编码
		 */
		public Item(String code,String value,String pcode) {
			this.code = code;
			this.value = value;
			this.pcode = pcode;
			this.abbr = PY.getAbbrFromText(value);
		}
		
		/**
		 * 构造方法
		 * @param code   编码
		 * @param value  值
		 * @param pcode  父编码
		 * @param abbr   拼音缩写
		 */
		public Item(String code,String value,String pcode,String abbr) {
			this.code = code;
			this.value = value;
			this.pcode = pcode;
			this.abbr = abbr;
		}

		public int compareTo(Item o) {
			// TODO Auto-generated method stub
			if(o == null)return 1;
			return this.code.compareTo(o.code);
		}
		
		public boolean equals(Item o) {
			if(o == null)return false;
			return compareTo(o) == 0;
		}
	}
}
