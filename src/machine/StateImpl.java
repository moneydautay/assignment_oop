package machine;

public class StateImpl implements State{
	
	private boolean initial;
	private boolean terminal;
	
	public StateImpl(boolean b, boolean c) {
		initial = b;
		terminal = c;
		
	}

	@Override
	public boolean initial() {
		// TODO Auto-generated method stub
		return initial;
	}

	@Override
	public boolean terminal() {
		// TODO Auto-generated method stub
		return terminal;
	}
	
	
	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public void print(){
		System.out.println("Initial "+ initial + ", Terminal "+ terminal);
	}
}
