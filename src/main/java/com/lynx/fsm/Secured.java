package com.lynx.fsm;

/**
 * Secured state machine
 * 
 * @author daniel.las
 *
 */
public interface Secured {
	/**
	 * Verify if current authority has given roles
	 * 
	 * @param roles
	 */
	void checkPermissions(String... roles);
}
