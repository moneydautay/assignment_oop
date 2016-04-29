package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import graph.Edge;
import graph.GraphComponent;

public class ChangeTransition implements Observer {
	
	private GraphComponent com;
	
	public ChangeTransition(GraphComponent com){
		this.com = com;
	}
	
	public void update(Observable obs, Object o) {
		Transition<?> t = (Transition<?>)o;
		List<Integer> dectectAutomata = new ArrayList<Integer>();
		//initial = true
		
		if(t.source().initial()){
			int i = 0;
			for(Edge edge : com.getListLine()){
				if(edge.getsourceParent() == com.getStartState() && edge.getLabel() == t.label()){
					com.setDectectAutomataItem(i);
					
					break;
				}
				i++;
			}
		}else{
			int sizeDA = com.getDectectAutomata().size();
			int i = 0;
			for(Edge edge : com.getListLine()){
				if(edge.getsourceParent() ==  com.getListLine().get(com.getDectectAutomata().get(sizeDA - 1)).getdestinationParent() && edge.getLabel() == t.label()){
					com.setDectectAutomataItem(i);
					break;
				}
				i++;
			}
		}
		com.repaint();
		
	}

}
