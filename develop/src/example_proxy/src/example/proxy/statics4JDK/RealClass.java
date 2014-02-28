package example.proxy.statics4JDK;

public class RealClass implements IProxy{

	@Override
	public void add() {

		System.out.println("this is the real class");
	}

}
