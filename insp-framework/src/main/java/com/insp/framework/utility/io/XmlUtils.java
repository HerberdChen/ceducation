package com.insp.framework.utility.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.insp.framework.utility.common.Result;

public class XmlUtils {
	/**
	 * XML序列化
	 * @param obj
	 * @param file
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static  <T> void write(T obj,File file) throws FileNotFoundException, JAXBException {		
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());  
        Marshaller ms = jc.createMarshaller();
        OutputStream stream = new FileOutputStream(file);
        try {
        	ms.marshal(obj, stream);
        }finally {
        	FileUtils.close(stream);        	
        }
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T read(Class<T> clazz,File file) throws JAXBException, FileNotFoundException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller ms = jc.createUnmarshaller();
		InputStream stream = new FileInputStream(file);
		try {
        	return (T) ms.unmarshal(stream);
        }finally {
        	FileUtils.close(stream);        	
        }
	}
	/**
	 * XML序列化
	 * @param obj
	 * @param file
	 * @return
	 */
	public static  <T> Result write2(T obj,File file) {
		OutputStream stream = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(obj.getClass());  
	        Marshaller ms = jc.createMarshaller();
	        stream = new FileOutputStream(file);
	        ms.marshal(obj, stream);
	        return new Result<T>();
		}catch(Exception e) {
			return new Result<T>(e);
		}finally {
			FileUtils.close(stream);    
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Result<T> read2(Class<T> clazz,File file){
		InputStream stream = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller ms = jc.createUnmarshaller();
			stream = new FileInputStream(file);
		
        	return new Result<T>((T) ms.unmarshal(stream));
        }catch(Exception e) {
        	return new Result<T>(e);
        }finally {		
        	FileUtils.close(stream);        	
        }
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T read3(Class<T> clazz,File file){
		InputStream stream = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller ms = jc.createUnmarshaller();
			stream = new FileInputStream(file);
		
        	return (T) ms.unmarshal(stream);
        }catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }finally {		
        	FileUtils.close(stream);        	
        }
	}
	
	
}
