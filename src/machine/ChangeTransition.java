package machine;

import java.util.Observable;
import java.util.Observer;

public class ChangeTransition implements Observer {

	public void update(Observable o, Object arg) {
		System.out.println("Recognized string "+ ((Transition)arg).label());
	}

}
