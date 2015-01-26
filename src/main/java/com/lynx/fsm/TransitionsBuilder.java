package com.lynx.fsm;

import com.google.common.collect.Multimap;

/**
 * @author daniel.las
 *
 * @param <S>
 *            state type, must implement {@link StateType} interface
 * @param <C>
 *            context type, must implement {@link StateHolder} interface
 * @param <X>
 *            execution context type
 */
public interface TransitionsBuilder<S extends StateType, C extends StateHolder<S>, X> {
    /**
     * Creates new transition with given start state
     * 
     * @param state
     * @return
     */
    TransitionsBuilder<S, C, X> from(S state);

    /**
     * Sets current transition end state to given state
     * 
     * @param state
     * @return
     */
    TransitionsBuilder<S, C, X> to(S state);

    /**
     * Sets condition checker for current transition
     * 
     * @param condition
     * @return
     */
    TransitionsBuilder<S, C, X> check(ConditionChecker<S, C> condition);

    /**
     * Sets required roles for current transition
     * 
     * @param roles
     * @return
     */
    TransitionsBuilder<S, C, X> permit(String... roles);

    /**
     * Defines if business validation should be performed for current transition
     * 
     * @return
     */
    TransitionsBuilder<S, C, X> validate(BusinessValidator<S, C> businessValidator);

    /**
     * Defines if bean validation using given validationGroups should be
     * performed
     * 
     * @param validationGroups
     * @return
     */
    TransitionsBuilder<S, C, X> validate(Class<?>... validationGroups);

    /**
     * Add action to execute before validation passed and requested state is set
     * in context
     * 
     * @param executor
     * @return
     */
    TransitionsBuilder<S, C, X> execBefore(ActionExecutor<S, C, X> executor);

    /**
     * Add action to execute after validation passed and requested state is set
     * in context
     * 
     * @param executor
     * @return
     */
    TransitionsBuilder<S, C, X> execAfter(ActionExecutor<S, C, X> executor);
    /**
     * Adds current transition to transitions map
     * 
     * @return
     */
    TransitionsBuilder<S, C, X> commit();

    /**
     * Returns created transitions
     * 
     * @return
     */
    Multimap<S, Transition<S, C, X>> build();
}
