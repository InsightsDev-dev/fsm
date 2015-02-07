package com.lynx.fsm;

/**
 * Defines SM available operations
 * 
 * @author daniel.las
 *
 * @param <S>
 */
public interface Forwardable<S extends StateType> {
	/**
	 * Move to next default state
	 */
	void forward();

}
