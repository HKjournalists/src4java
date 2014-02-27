package example.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FileUtil {
	
	private static List<File> fileList = new ArrayList<File>();
	
	
	public static List<File> getFile(String path,String lastName){
		
		File root = new File(path);
		if(!root.exists() || !root.isDirectory()){
			throw new IllegalArgumentException("the special path does not exist or isn't a directory, please check again!!!");
		}
		
		File[] files = root.listFiles();
		for(File file : files){
			if(file.isDirectory()){
				getFile(file.getAbsolutePath(),lastName);
			}else{
				if(file.getName().endsWith(lastName)){
					fileList.add(file);
				}
			}
		}
		
		return fileList;
	}
	
	
	@Test
	public void getFileTest(){
		List<File> file = getFile("D:/PTP50/encodingParse",".java");
		System.out.println(file.size());
	}
	
}
