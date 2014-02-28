package example.proxy.statics4JDK;

public class Test {
	public static void main(String[] args) {
		RealClass real = new RealClass();
		ProxyClass proxy = new ProxyClass(real);
		proxy.add();
		
	}
}
