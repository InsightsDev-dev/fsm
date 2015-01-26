package com.lynx.fsm;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Base class for transition building. Override this class for concrete class
 * 
 * @author daniel.las
 *
 * @param <S>
 * @param <C>
 */
public abstract class DefaultTransitionsBuilder<S extends StateType, C extends StateHolder<S>, X> implements
        TransitionsBuilder<S, C, X> {

    Transition<S, C, X> transition;
    Multimap<S, Transition<S, C, X>> transitions = HashMultimap.create();

    @Override
    public TransitionsBuilder<S, C, X> from(S state) {
        transition = new Transition<S, C, X>(state);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> to(S state) {
        transition.setEnd(state);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> check(ConditionChecker<S, C> condition) {
        transition.setCondition(condition);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> permit(String... roles) {
        transition.setRoles(roles);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> validate(BusinessValidator<S, C> businessValidator) {
        transition.setValidator(businessValidator);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> execBefore(ActionExecutor<S, C, X> executor) {
        transition.setBeforeAction(executor);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> execAfter(ActionExecutor<S, C, X> executor) {
        transition.setAfterAction(executor);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> validate(Class<?>... validationGroups) {
        transition.setValidationGroups(validationGroups);

        return this;
    }

    @Override
    public TransitionsBuilder<S, C, X> commit() {
        if (transition.getStart() == null) {
            throw new IllegalArgumentException("Transition start state must not be null");
        }
        if (transition.getEnd() == null) {
            throw new IllegalArgumentException("Transition end state must not be null");
        }
        transitions.put(transition.getStart(), transition);

        return this;
    }

    @Override
    public Multimap<S, Transition<S, C, X>> build() {
        return transitions;
    }

}
