package example.hibernate.po;

import java.util.Date;

public class User {
	private int id;
	private String username;
	private Date reTime;
	private Date lastLog;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getReTime() {
		return reTime;
	}
	public void setReTime(Date reTime) {
		this.reTime = reTime;
	}
	public Date getLastLog() {
		return lastLog;
	}
	public void setLastLog(Date lastLog) {
		this.lastLog = lastLog;
	}
	@Override
	public String toString() {
		return this.id+", "+this.username+", "+this.reTime+", "+this.lastLog;
	}
	
	
	
}
