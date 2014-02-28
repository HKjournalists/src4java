package example.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import example.jdbc.dao.BaseDao;

public class MyJdbc4Metada {
	/**
	 * 获取数据库的元数据和结果集元数据
	 */
	private Map<String,String> metada = new HashMap<String,String>();
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	
	//获取元数据
	public Map<String,String> getDbMetada(){
		conn = BaseDao.getConn();
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			metada.put("dbname", dbm.getDatabaseProductName());
			metada.put("dbversion",dbm.getDatabaseProductVersion());
			metada.put("url", dbm.getURL());
			metada.put("user", dbm.getUserName());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			BaseDao.close(conn, ps, rs);
		}
		
		return metada;
	}
	
	
	//获取结果集元数据
	public void getResultMetada(){
		conn = BaseDao.getConn();
		try {
			ps = conn.prepareStatement("select * from emp");
			rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			//获取列数
			int columnCount = rsm.getColumnCount();
			//jdbc计数从1开始
			for(int i=1;i<=columnCount;i++){
				System.out.print(rsm.getColumnName(i)+"\t");
			}
			System.out.println();
			System.out.println("----------------------------");
			while(rs.next()){
				for(int i=1;i<=columnCount;i++){
					String value = rs.getString(rsm.getColumnName(i));
					System.out.print(value+"\t");
				}
				System.out.println();
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		MyJdbc4Metada jdbc = new MyJdbc4Metada();
		Map<String,String> dbInfo = jdbc.getDbMetada();
		Iterator ite = dbInfo.entrySet().iterator();
		while(ite.hasNext()){
			Entry entry = (Entry) ite.next();
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
		System.out.println();
		jdbc.getResultMetada();
	}
	
}
