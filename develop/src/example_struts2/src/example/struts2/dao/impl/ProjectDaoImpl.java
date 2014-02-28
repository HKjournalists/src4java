package example.struts2.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import example.struts2.dao.IProjectDao;
import example.struts2.pojo.ProjectEntity;
import example.struts2.util.DaoUtil;

public class ProjectDaoImpl implements IProjectDao{

	public List<ProjectEntity> findAll() {
		List<ProjectEntity> pros = new ArrayList<ProjectEntity>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		conn = DaoUtil.getConn();
		String sql = "select id,name,start_date,end_date from t_project";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				ProjectEntity pro = new ProjectEntity();
				pro.setId(rs.getInt("id"));
				pro.setName(rs.getString("name"));
				pro.setStartDate(rs.getDate("start_date"));
				pro.setEndDate(rs.getDate("end_date"));
				pros.add(pro);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("wrong sql");
		}
		return pros;
	}
	
	public static void main(String[] args) {
		ProjectDaoImpl proImpl = new ProjectDaoImpl();
		List<ProjectEntity> pros = proImpl.findAll();
		for(int i=0;i<pros.size();i++){
			System.out.println(pros.get(i));
		}
	}
	
	
}
