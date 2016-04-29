package machine;

public class TransitionImpl<T> implements Transition<T>{
	
	private State source;
	private State target;
	private T label;
	
	public TransitionImpl(State source, State target, T label) {
		
		this.source = source;
		this.target = target;
		this.label = label;
	}

	@Override
	public State source() {
		return source;
	}

	@Override
	public State target() {
		return target;
	}

	@Override
	public T label() {
		return label;
	}
	
	public void setLabel(T label){
		this.label = label;
	}
	public void print(){
		System.out.println("Source "+ source + ", target "+ target + ", Lable"+ label);
	}

}
