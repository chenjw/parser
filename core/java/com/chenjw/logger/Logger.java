package com.chenjw.logger;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


/**
 * 日志包装
 * 
 * @author chenjw
 * 
 */
public class Logger {
	
	public static Logger getLogger(Class<?> clazz) {
		return new Logger();
	}

	public static Logger getLogger(String name) {
		return new Logger();
	}
	
	public Logger() {

	}

	public boolean isDebugEnabled() {
		return true;
	}
	
	public boolean isInfoEnabled() {
		return true;
	}

	// /////////

	public void trace(Object key) {
		System.out.println(key);
	}

	public void trace(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void debug(Object key) {
		System.out.println(key);
	}

	public void debug(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void info(Object key) {
		System.out.println(key);
	}

	public void info(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void warn(Object key) {
		System.out.println(key);
	}

	public void warn(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void error(Object key) {
		System.out.println(key);
	}

	public void error(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void fatal(Object key) {
		System.out.println(key);
	}

	public void fatal(Object key, Throwable cause) {
		System.out.println(key);
	}

	public static void main(String[] args) throws IOException{
		for(Object obj:FileUtils.listFiles(new File("/home/chenjw/my_workspace/parser/core/java/com/chenjw/parse"), new String[]{"java"}, true)){
			File f=(File)obj;
			String s=FileUtils.readFileToString(f,"GBK");
			FileUtils.writeStringToFile(f, s, "UTF-8");
		}
		
	}
	
}
