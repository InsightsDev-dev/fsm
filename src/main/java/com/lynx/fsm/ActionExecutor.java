package com.lynx.fsm;

/**
 * @author daniel.las
 *
 * @param <S>
 *            state type
 * @param <C>
 *            context type
 * @param <X>
 *            execution context type
 */
public interface ActionExecutor<S extends StateType, C extends StateHolder<S>, X> {
    void execute(X executionContext, C context);
}
