package machine;

public class NotDeterministTransitionException extends Exception {

	private static final long serialVersionUID = -8734565041515280552L;

	private Transition<?> t1, t2;

	public NotDeterministTransitionException(Transition<?> t1, Transition<?> t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	public String getMessage() {
		return "Duplicated transition " + t1 + ", " + t2;
	}

}
