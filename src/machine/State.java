package machine;

public interface State {
	
	public boolean initial();
	
	public boolean terminal();
	
	public void setInitial(boolean initial);
	
	public void setTerminal(boolean terminal);
	
	
}
