package com.insp.framework.utility.time;

import java.util.Date;

public class DateSection implements Comparable<DateSection>{
	public Date begin;
	public Date end;
	@Override
	public int compareTo(DateSection o) {
		// TODO Auto-generated method stub
		if(o == null)return 1;
		if(o.begin == null && this.begin == null)return 0;
		else if(o.begin == null) return 1;
		else if(this.begin == null)return -1;
		else
			return (int)(this.begin.getTime() - o.begin.getTime());
	}
	
}
