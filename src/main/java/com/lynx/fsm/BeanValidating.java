package com.lynx.fsm;

/**
 * Defines bean validation (JSR 303) methods
 * 
 * @author daniel.las
 *
 */
public interface BeanValidating {
	/**
	 * Validates against given groups
	 * 
	 * @param groups
	 */
	void validate(Class<?>... groups);
}
