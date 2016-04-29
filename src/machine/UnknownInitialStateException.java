package machine;

public class UnknownInitialStateException extends Exception {

	private static final long serialVersionUID = -4441983606860925494L;

	public String getMessage() {
		return "No initial state";
	}
}
