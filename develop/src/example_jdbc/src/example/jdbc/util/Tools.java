package example.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Tools {
	
	
	/**
	 * 读取配置文件
	 * @throws IOException 
	 */
	
	public static Properties getProperties(String fileName) throws IOException{
		Properties pro = new Properties();
		InputStream in = Tools.class.getResourceAsStream(fileName);
		pro.load(in);
		return pro;
	}
	
	/**
	 * 使用classloader 加载properties
	 * @throws IllegalAccessException 
	 */
	
	public static String getValue(String path,String key) throws IllegalAccessException{
		ClassLoader loader = Tools.class.getClassLoader();
		InputStream in = loader.getResourceAsStream(path);
		Properties prop = new Properties();
		try {
			prop.load(in);
			return prop.getProperty(key);
		} catch (IOException e) {
			throw new IllegalAccessException("file not found");
		}
		
		
	}
	
}
