package example.proxy.dynamic4JDK;

public class Test {
	public static void main(String[] args) {
		DynamicProxy proxy = new DynamicProxy();
		IProxy ipro = (IProxy) proxy.bind(new ProxyImpl());
		ipro.add();
	}
}
