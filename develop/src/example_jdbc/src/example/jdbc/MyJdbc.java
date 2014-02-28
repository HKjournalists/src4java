package example.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import example.jdbc.dao.BaseDao;
import example.jdbc.pojo.Emp;

public class MyJdbc {
	private List<Emp> emps = new ArrayList<Emp>();
	
	public List<Emp> getPojo(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = BaseDao.getConn();
		String sql = "select * from emp";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()){
				Emp emp = new Emp();
				emp.setId(rs.getInt("id"));
				emp.setAge(rs.getInt("age"));
				emp.setName(rs.getString("name"));
				emps.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			BaseDao.close(conn, ps, rs);
		}
		
		return emps;
	}
	
	public static void main(String[] args) {
		MyJdbc jdbc = new MyJdbc();
		List<Emp> emps = jdbc.getPojo();
		for(int i=0;i<emps.size();i++){
			System.out.println(emps.get(i));
		}
	}
	
}
