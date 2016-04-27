package machine;

public class ExcuteAutomata<T> implements Action<T> {
	
	public ExcuteAutomata(){
		
	}

	public void excute(T arg) {
		System.out.println(arg + " Excute automata");
	}
	
}
