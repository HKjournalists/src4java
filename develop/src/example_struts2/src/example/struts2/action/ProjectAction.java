package example.struts2.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import example.struts2.dao.impl.ProjectDaoImpl;
import example.struts2.pojo.ProjectEntity;

public class ProjectAction extends ActionSupport{
	private List<ProjectEntity> projectList;
	
	//output
	@Override
	public String execute() throws Exception {
		ProjectDaoImpl proDao = new ProjectDaoImpl();
		projectList = proDao.findAll();
		for(int i=0;i<projectList.size();i++){
			System.out.println(projectList.get(i));
		}
		return "success";
	}

	public List<ProjectEntity> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectEntity> projectList) {
		this.projectList = projectList;
	}

	
}
