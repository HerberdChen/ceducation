package com.insp.framework.utility.common;

import com.insp.framework.utility.text.StringUtils;

/**
 * 执行某个操作的结果
 * @author admin
 *
 */
public class Result<T> {
	/** 错误编号 */
	public final int error;
	/** 异常信息 */
	public final Exception e;
	/** 消息 */
	public final String message;
	/** 细节消息 */
	public final String detailMessage;
	/** 结果对象  */
	public final T obj;
	
	public Result() {
		this.error = 0;
		this.e = null;
		this.message = "";
		this.detailMessage = "";
		this.obj = null;		
	}
	public Result(T obj) {
		this.error = 0;
		this.e = null;
		this.message = "";
		this.detailMessage = "";
		this.obj = obj;		
	}
	public Result(Exception e) {
		this.error = -1;
		this.e = e;
		this.message = e==null?"":e.getMessage();
		this.detailMessage = "";
		this.obj = null;
	}
	public Result(int error,Exception e) {
		this.error = error;
		this.e = e;
		this.message = e==null?"":e.getMessage();
		this.detailMessage = "";
		this.obj = null;
	}
	public Result(int error,Exception e,String detailMessage) {
		this.error = error;
		this.e = e;
		this.message = e==null?"":e.getMessage();
		this.detailMessage = detailMessage;
		this.obj = null;
	}
	public Result(String message,String detailMessage) {
		this.error = -1;
		this.e = null;
		this.message = message;
		this.detailMessage = detailMessage;
		this.obj = null;
	}
	public Result(int error,String message,String detailMessage) {
		this.error = error;
		this.e = null;
		this.message = message;
		this.detailMessage = detailMessage;
		this.obj = null;
	}
	@Override
	public String toString() {
		return "error="+error + 
				(StringUtils.empty(message)?"":",msg="+message) +
				(StringUtils.empty(detailMessage)?"":",detail="+detailMessage)+
				(e==null?"":",exception="+e.getClass().getSimpleName()) +
				(obj==null?"":",obj="+obj.toString());
	}
	
	
	
	
}
