package machine;

class NotDeterministTransitionException extends Exception{
		
	private static final long serialVersionUID = 4435210866507255511L;

	public NotDeterministTransitionException(String msg){
		super(msg);
	}

}
