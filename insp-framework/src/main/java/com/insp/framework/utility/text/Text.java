/**
 * 
 */
package com.insp.framework.utility.text;

/**
 * @author admin
 *
 */
public @interface Text {
	int sn() default 0;
	String caption() default "";
	boolean required() default true;
	boolean ignore() default false;
}
