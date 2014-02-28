package example.struts2.pojo;

import java.util.Date;

public class ProjectEntity {
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name+"\t"+this.startDate+"\t"+this.endDate;
	}
	
	
	
	
}
