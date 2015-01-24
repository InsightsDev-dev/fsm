package com.lynx.fsm;

/**
 * Defines context holding operations
 * 
 * @author daniel.las
 *
 * @param <C>
 */
public interface ContextHolder<C> {
	void setContext(C context);

	C getContext();
}
