package example.struts2.action;

import com.opensymphony.xwork2.ActionSupport;

public class HelloAction extends ActionSupport {
	
	//input 
	private String name;
	
	@Override
	public String execute() throws Exception {
		if("test".equals(name)){
			return "success";
		}
		return "faile";
	}
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
}
