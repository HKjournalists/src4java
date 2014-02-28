package example.jdbc.test;

import example.jdbc.pojo.Emp;

public class PojoTest {
	
	public static void main(String[] args) {
		Emp emp1 = new Emp();
		emp1.setAge(11);
		emp1.setName("a");
		
		Emp emp2 = new Emp();
		emp2.setAge(12);
		emp2.setName("a");
		System.out.println(emp1);
		System.out.println(emp2);
		System.out.println(emp1.equals(emp2));
	}
}
