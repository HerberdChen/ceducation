package com.insp.framework.utility.text;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Chinese {
	public final static char[] NumberChinese = new char[] {'零','一','二','三','四','五','六','七','八','九','十'};
	
	public static Integer[] indexOfNumberChinese(int start,String str) {
		return StringUtils.indexOf(str,start,NumberChinese);
	}
	
	public static String fromChineseNumber(String str) {
		StringBuffer s = new StringBuffer();
		char[] cs = str.toCharArray();
		for(int i=0;i<cs.length;i++) {
			int index = -1;
			for(int j=0;j<NumberChinese.length;j++) {
				if(NumberChinese[j] == cs[i]) {
					index = j;
					break;
				}
			}			
			if(index<0)
				s.append(cs[i]);
			else {
				if(index==10) {
					s.append("10");				
				}else {
					s.append(StringUtils.Numbers[index]);
				}
			}
				
		}
		return s.toString();
	}
	
	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * @param c
	 * @return
	 */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    /**
     * 完整的判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
 
    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }
 
    /**
     * 只能判断部分CJK字符（CJK统一汉字）
     * @param str
     * @return
     */
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }

}
