package machine;

public class TransitionWithAction<T> implements Transition<T>{
	
	private TransitionImpl<T> t;
	private Action<T> a;
	
	public TransitionWithAction(TransitionImpl<T> t, Action<T> a){
		this.t = t;
		this.a = a;
	}

	@Override
	public State source() {
		// TODO Auto-generated method stub
		return t.source();
	}

	@Override
	public State target() {
		// TODO Auto-generated method stub
		return t.target();
	}

	@Override
	public T label() {
		// TODO Auto-generated method stub
		return t.label();
	}
	
	public State cross(){
		a.excute(t.label());
		return t.target();
	}

	@Override
	public void setLabel(T label) {
		t.setLabel(label);
		
	}

}
