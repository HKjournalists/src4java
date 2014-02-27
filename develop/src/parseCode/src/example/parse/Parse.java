package example.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.junit.Test;

import example.util.FileUtil;

public class Parse {
	
	public void parse(File file,String inCode,String outCode){
		
		StringBuffer sb = new StringBuffer("");
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		 try {
			br = new BufferedReader(
									new InputStreamReader(
										new FileInputStream(file),inCode));
			
			
			String context;
			while((context = br.readLine())!=null){
				sb.append(context+"\n");
				context = br.readLine();
				sb.append(context);
			}
			
			bw = new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(file),outCode));
			bw.write("this is "+outCode+"now"+"\n");
			bw.write(sb.toString());
			bw.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void parseTest(){
		List<File> files = FileUtil.getFile("G:\\temp", ".txt");
		for(File file:files){
			new Parse().parse(file, "GBK", "UTF-8");
		}
		System.out.println("done");
	}
	
}
