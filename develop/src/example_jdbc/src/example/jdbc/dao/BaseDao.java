package example.jdbc.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import example.jdbc.util.Tools;

public class BaseDao {
	/**
	 * 创建数据库连接
	 */
	private static String driver;
	private static String url;
	private static String user;
	private static String password;

	/*
	 * 加载驱动
	 */
	static {
		try {
			Properties pro = Tools.getProperties("/dbinfo.properties");
			driver = pro.getProperty("driverClass");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
			Class.forName(driver);// 注册驱动
		} catch (IOException e) {
			throw new IllegalArgumentException("file not found");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("db connection error");
		}

	}

	public static void close(Connection conn, Statement st, ResultSet rs) {
		try {
			if (null != conn) {
				conn.close();
			}

			if (null != st) {
				st.close();
			}

			if (null != rs) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("db Close Exception");
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(BaseDao.getConn());
	}
}
