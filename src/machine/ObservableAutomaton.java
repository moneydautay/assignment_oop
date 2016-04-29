package machine;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import graph.GraphComponent;

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
}

