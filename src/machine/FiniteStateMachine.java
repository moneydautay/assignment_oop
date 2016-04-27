package machine;

import java.util.ArrayList;

public class FiniteStateMachine<T> extends DeterministicAutomaton<T> {

	public FiniteStateMachine(ArrayList<Transition<T>> t)throws Exception{
		super(t);
	}
	
	public State changeCurrentState(Transition<T> t){
		return ((TransitionWithAction<T>)t).cross();
	}
}
