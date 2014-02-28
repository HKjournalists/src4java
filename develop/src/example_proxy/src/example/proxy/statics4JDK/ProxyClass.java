package example.proxy.statics4JDK;

public class ProxyClass implements IProxy{
	private RealClass real;
	public ProxyClass(RealClass real){
		this.real = real;
	}
	@Override
	public void add() {
		beforeAdd();
		real.add();
		afterAdd();
	}
	
	public void beforeAdd(){
		System.out.println("do something before add");
	}
	public void afterAdd(){
		System.out.println("do something after add");
	}
}
