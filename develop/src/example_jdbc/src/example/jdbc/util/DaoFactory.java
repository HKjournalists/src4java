package example.jdbc.util;

import example.jdbc.dao.BaseDao;

public class DaoFactory {
	
	
	public static Object getInstance(String type) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Object obj = Class.forName(type).newInstance();
		return obj;
	}
	
	//test
	public static void main(String[] args) {
		try {
			String type = Tools.getValue("factory.properties", "dao");
			BaseDao dao = (BaseDao) DaoFactory.getInstance(type);
			System.out.println(dao.getConn());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
