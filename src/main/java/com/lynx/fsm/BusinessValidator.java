package com.lynx.fsm;

/**
 * Defines business validation operations
 * 
 * @author daniel.las
 *
 */
public interface BusinessValidator<S extends StateType, C extends StateHolder<S>> {
    /**
     * Validate requested state against current context
     */
    void validate(S requestedState, C context);
}
