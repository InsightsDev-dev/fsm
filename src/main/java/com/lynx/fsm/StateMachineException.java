package com.lynx.fsm;

public class StateMachineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StateMachineException() {

	}

	public StateMachineException(String message) {
		super(message);

	}

	public StateMachineException(String message, Throwable cause) {
		super(message, cause);

	}

	public StateMachineException(Throwable cause) {
		super(cause);
	}
}
