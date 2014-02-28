package example.struts2.dao;

import java.util.List;

import example.struts2.pojo.ProjectEntity;

public interface IProjectDao {
	List<ProjectEntity> findAll();
}
