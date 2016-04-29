package machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.JComponent;

import graph.GraphComponent;

public class DeterministicAutomaton<T> {
	
	private State initialState = null;
	/*
	 * In the map transitions, at each state s we associate a map m where the
	 * values are the transitions having s as source and the corresponding key
	 * the labels of the transitions.
	 */
	private final Map<State, Map<Object, Transition<T>>> transitions;
	
	public DeterministicAutomaton(ArrayList<Transition<T>> transitions) throws Exception {
		
		this.transitions = new HashMap<State, Map<Object, Transition<T>>>();
		Iterator<T> iterTrans = (Iterator<T>) transitions.iterator();
		while (iterTrans.hasNext()) {
			Transition<T> t = (Transition<T>) iterTrans.next();
			addState(t.source());
			addState(t.target());
			
			Map<Object, Transition<T>> map = this.transitions.get(t.source());
		
			if(map.containsKey(t.label())){ 
				throw new NotDeterministTransitionException(t, map.get(t.label()));
			}
										
			map.put(t.label(), t);
		}
		
		if(initialState == null){
			throw new UnknownInitialStateException();
		}
	}

	protected final void addState(State s) throws Exception {
		
		if (!transitions.containsKey(s)) {
			transitions.put(s, new HashMap<Object, Transition<T>>());
			if (s.initial()) {
				if (initialState == null) {
					initialState = s;
				}else{
					throw new NotDeterministInitialStateException(s, initialState);
				}
			}	
		}
	}
		
	public Transition<T> transition(State s, Object label){
		if(transitions.containsKey(s)){
			return transitions.get(s).containsKey(label)? transitions.get(s).get(label) : null;
		}else throw  new NoSuchElementException();			
	}
	
	
	public boolean recognize(Object... word){
		
		return recognize(Arrays.asList(word).iterator());
		
	}
	
	public boolean recognize(Iterator<Object> word){
		
		State s = initialState;
		
		while(word.hasNext()){
			
			Transition<T> t = transition(s, word.next());
			
			if(t == null) return false;
			else s = changeCurrentState(t);
		}
		return s.terminal();
	}
	
	protected State changeCurrentState(Transition<T> t) {
		return t.target();
	}
}
