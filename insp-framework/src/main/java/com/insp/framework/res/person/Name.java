package com.insp.framework.res.person;

import java.util.ArrayList;
import java.util.List;

import com.insp.framework.utility.time.DateSection;

public class Name {
	private class Item implements Comparable<Item>{
		/** 名 */
		public final String firstName;
		/** 中间名 */
		public final String middleName;
		/** 姓 */
		public final String lastName;
		/** 时间段 */
		public DateSection section = new DateSection();
		
		public Item(String firstName,String middleName,String lastName) {
			this.firstName = firstName;
			this.middleName = middleName;
			this.lastName = lastName;
		}
		@Override
		public int compareTo(Item arg0) {
			// TODO Auto-generated method stub
			return this.section.compareTo(arg0.section);
		}
	}
	/** 绰号，多个逗号分割 */
	public final List<String> nickNames = new ArrayList<String>();
}
