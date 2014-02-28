package example.struts2.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DaoUtil {
	
	private static String driverClass;
	private static String url;
	private static String user;
	private static String password;
	
	static{
		Properties pro = new Properties();
		InputStream in = DaoUtil.class.getResourceAsStream("/test_struts2.properties");
		try {
			pro.load(in);
			driverClass = pro.getProperty("driverClass");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
			Class.forName(driverClass);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConn() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("cannot connect the db");
		}
		
	}
	
	
	public static void close(Connection conn,Statement st,ResultSet rs){
		try {
			if(conn != null){
				conn.close();
			} 
			if(st != null){
				st.close();
			}
			if(rs != null){
				rs.close();
			}
		}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	public static void main(String[] args) {
		Connection conn = DaoUtil.getConn();
		System.out.println(conn);
	}
	
	}

