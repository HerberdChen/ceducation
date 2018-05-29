package com.insp.framework.utility.date;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insp.framework.utility.text.StringUtils;

public class DateTime {
	static Logger logger = LoggerFactory.getLogger(DateTime.class);
	public final static String[] DateTimeFormats = {
			"yyyy-MM-dd hh:mm:ss",
			"yyyyMMddhhmmss"
	};
	public final static String DefaultDateTimeFormat = DateTimeFormats[0];
	public final static SimpleDateFormat DefaultDateTimeFormatter = new SimpleDateFormat(DefaultDateTimeFormat);
	
	public final static String[] DateFormats = {
			"yyyy-MM-dd",
			"yyyyMMdd"
	};
	public final static String DefaultDateFormat = DateTimeFormats[0];
	
	public final static String[] Formats = StringUtils.merge(DateTimeFormats,DateFormats);
	static {
		logger.info("注册日期时间转换器...");
		ConvertUtils.register(new Converter() {
			
			@Override
			public Object convert(Class arg0, Object arg1) {
				// TODO Auto-generated method stub
				if(arg1 == null || arg1.equals(""))
					return null;
				for(String format : Formats) {
					try {
						SimpleDateFormat ftm = new SimpleDateFormat(format);
						return ftm.parse(arg1.toString());
					}catch(Exception e) {
						continue;
					}
				}
				return null;
			}
			
		}, Date.class);
	}
	
	public static String format(Date d) {
		if(d == null)return "";
		return DefaultDateTimeFormatter.format(d);
	}
	public static String format(Date d,String format) {
		if(d == null)return "";
		return new SimpleDateFormat(format).format(d);
	}
	
	
	
}
