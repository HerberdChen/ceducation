package com.insp.framework.utility.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	/**
	 * 关闭流，不抛出异常
	 * @param stream
	 */
	public static void close(Closeable stream) {
		if(stream == null)
			return;
		try {
			stream.close();
		} catch (IOException e) {			
		}
	}
	
	public static String[] readLines(String filename) {
		return readLines(filename,"utf-8");
	}
	public static String[] readLines(String filename,String encode) {
		if(!new File(filename).exists())
			return null;
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		if(encode == null || encode.equals(""))
			encode = "utf-8";
		try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),encode));  
            String str = null;           
            while((str = br.readLine()) != null) {
                  lines.add(str);
            }             
		}
	    catch(Exception e) {
	    	e.printStackTrace();           
	            
	    }finally {
	    	 close(br);	    	 
		}
		
		return lines.toArray(new String[lines.size()]);
	}
	
	public static void writeLines(String[] lines,String filename) {
		writeLines(lines,filename,"utf-8");
	}
	public static void writeLines(String[] lines,String filename,String encode) {
		BufferedWriter br = null;

		if(encode == null || encode.equals(""))
			encode = "utf-8";
		try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),encode));
            for(String line : lines) {
            	br.write(line+"\n");            	
            }
            br.flush();
		}
	    catch(Exception e) {
	    	e.printStackTrace();           
	            
	    }finally {
	    	 close(br);	    	 
		}
		
	}
	
	public static void writeText(String str,String filename) {
		writeText(str,filename,"utf-8");
	}
	public static void writeText(String str,String filename,String encode) {
		BufferedWriter br = null;

		if(encode == null || encode.equals(""))
			encode = "utf-8";
		try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),encode));
            br.write(str);    
            br.flush();
		}
	    catch(Exception e) {
	    	e.printStackTrace();           
	            
	    }finally {
	    	 close(br);	    	 
		}
		
	}
	
	public static void save(InputStream stream,String filename) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			byte[] b = new byte[1024];
			while((stream.read(b)) != -1){
				fos.write(b);
			}		
			fos.close();
		}catch(Exception e) {
			
		}finally {
			if(fos != null)
				try{fos.close();}catch(Exception e){}
		}
	}
	/**
	 * 给路径字符串结束加上路径终结符号"\"或者"/"
	 * @param path
	 * @return 
	 */
	public static String endWithPath(String path) {
		if(path == null)
			return "/";
		if(path.endsWith("\\") || path.endsWith("/"))
			return path;
		if(path.contains("\\"))
			return path+"\\";
		if(path.contains("/"))
			return path+"/";
		return path+"/";
	}
}
