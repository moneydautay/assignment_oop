package machine;

public interface Transition<T>{
	
	public State source();
	
	public State target();
	
	public T label();
	
}
