package machine;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Mr Lam
 * @category Automata
 * @version 1.0.0 
 * @since 2016-03-11
 */

public class ObservableAutomaton<T> extends DeterministicAutomaton<T> {

	/*
	 * Declare params obs to decelerate Observable
	 */
	private Observable obs = new Observable(){
		@Override
		public void notifyObservers(Object obj){
			setChanged();
			super.notifyObservers(obj);
		}
	};
	
	/**
	 * Construct Observable Automaton
	 */
	public ObservableAutomaton(ArrayList<Transition<T>> transitions) throws Exception{
		super(transitions);
	}
	
	/**
	 * Create addObserver 
	 * @param Observer O
	 * @return void
	 */
	public void addObserver(Observer o){
		obs.addObserver(o);
	}
	
	/**
	 * Change current state
	 * @param Transition transition
	 * @return void
	 */
	@Override
	protected State changeCurrentState(Transition<T> t){
		obs.notifyObservers(t);
		return super.changeCurrentState(t);
	}
	
	public static void main(String[] args) throws Exception{
		
		ArrayList<State> state = new ArrayList<State>();
		state.add(new StateImpl(true, false));
		state.add(new StateImpl(false, true));

		
		ArrayList<Transition<String>> transition = new ArrayList<Transition<String>>();
		transition.add(new TransitionImpl<String>(state.get(0), state.get(1), "a"));
		transition.add(new TransitionImpl<String>(state.get(0), state.get(0), "b"));
		transition.add(new TransitionImpl<String>(state.get(1), state.get(0), "b"));
		transition.add(new TransitionImpl<String>(state.get(1), state.get(1), "a"));
				
		try {
			new DeterministicAutomaton<String>(transition);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
				
		ChangeTransition ct = new ChangeTransition();
		
		ObservableAutomaton<String> obs = new ObservableAutomaton<String>(transition);
		
		obs.addObserver(ct);
		ArrayList<Object> m = new ArrayList<Object>(); 
		m.add("a");
		m.add("b");
		m.add("b");
		m.add("a");
		obs.recognize(m);
	}
}

