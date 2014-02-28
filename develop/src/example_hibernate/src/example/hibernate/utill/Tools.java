package example.hibernate.utill;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

public class Tools {
	
	//create table in hibernate
	
	/**
	 * 1 load hibernate.cfg.xml
	 * 2 use tool class to create table
	 */
	public static void createTable(){
		//加载hibernate.cfg.xml配置文件
		 Configuration conf = new Configuration();
		 conf.configure();//读取到src目录下的hibernate.cfg.xml
		 
		 SchemaExport export = new SchemaExport(conf);
		 export.create(true, true);
	}
	
	@Test
	public void createTest(){
		Tools.createTable();
	}
	
}
