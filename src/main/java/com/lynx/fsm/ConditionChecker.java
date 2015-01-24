package com.lynx.fsm;

/**
 * Condition functional interface
 * 
 * @author daniel.las
 *
 * @param <S>
 *            state type
 * @param <C>
 *            context type
 */
public interface ConditionChecker<S extends StateType, C extends StateHolder<S>> {
	/**
	 * Check condition against given requestedState and context
	 * 
	 * @param requestedState
	 * @param context
	 * @return
	 */
	boolean check(S requestedState, C context);
}
