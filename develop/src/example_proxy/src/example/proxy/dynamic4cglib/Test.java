package example.proxy.dynamic4cglib;

public class Test {
	public static void main(String[] args) {
		Proxy4cglib cglib = new Proxy4cglib();
		ProxyImpl proxy =  (ProxyImpl) cglib.getInstance(new ProxyImpl());
		proxy.add();
	}
}
