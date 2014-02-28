package example.hibernate.utill;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateUtil {
	private static SessionFactory factory;
	private static Configuration conf;
	private static ThreadLocal<Session> local = new ThreadLocal<Session>();
	
	static {
		try{
			//读取hibernate.cfg.xml
			conf = new Configuration().configure();
			factory = conf.buildSessionFactory();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//建表
	public static void CreateSchema(){
		SchemaExport export = new SchemaExport(conf);
		export.create(true, true);
	}
	
	//为用户提供Session
	public static Session getSession(){
		
		Session session = local.get();
		if(session!=null){
			return session;
		}else{
			session = factory.openSession();
			local.set(session);
		}
		return session;
	}
	//关闭Session
	public static void closeSession(Session session){
		if(session!=null){
			if(session.isOpen()){
				session.close();
			}
		}
	}
}
