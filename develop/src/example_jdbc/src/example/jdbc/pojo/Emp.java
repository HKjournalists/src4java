package example.jdbc.pojo;

public class Emp {
	private  Integer id;
	private  Integer age;
	private  String name;
	
	
	public Emp() {
		super();
	}
	
	

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getAge() {
		return age;
	}



	public void setAge(Integer age) {
		this.age = age;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	@Override
	public boolean equals(Object obj) {
		if(null == obj){
			return false;
		}
		
		if(this == obj){
			return true;
		}
		
		if(getClass() != obj.getClass()){
			return false;
		}
		
		if(obj instanceof Emp){
			Emp e = (Emp) obj;
			return this.age == e.age && this.name == e.name;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		return age*1000;
	}

	@Override
	public String toString() {
		return age+"\t"+name;
	}
	
}
