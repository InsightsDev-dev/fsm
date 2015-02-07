package com.lynx.fsm.transition.builder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.lynx.fsm.ActionExecutor;
import com.lynx.fsm.ConditionChecker;
import com.lynx.fsm.StateHolder;
import com.lynx.fsm.StateType;
import com.lynx.fsm.transition.Transition;

/**
 * Base class for transition building. Override this class for concrete class
 * 
 * @author daniel.las
 *
 * @param <S>
 * @param <C>
 */
public class DefaultTransitionsBuilder<S extends StateType, C extends StateHolder<S>, X, T> {

    Transition<S, C, X> transition;
    Multimap<S, Transition<S, C, X>> transitions = HashMultimap.create();

    public DefaultTransitionsBuilder<S, C, X> from(S state) {
        transition = new Transition<S, C, X>(state);

        return this;
    }

    public DefaultTransitionsBuilder<S, C, X> to(S state) {
        transition.setEnd(state);

        return this;
    }

    public DefaultTransitionsBuilder<S, C, X> check(ConditionChecker<S, C> condition) {
        transition.setCondition(condition);

        return this;
    }

    public DefaultTransitionsBuilder<S, C, X> execBefore(ActionExecutor<S, C, X> executor) {
        transition.setBeforeAction(executor);

        return this;
    }

    public DefaultTransitionsBuilder<S, C, X> execAfter(ActionExecutor<S, C, X> executor) {
        transition.setAfterAction(executor);

        return this;
    }

    public DefaultTransitionsBuilder<S, C, X> commit() {
        if (transition.getStart() == null) {
            throw new IllegalArgumentException("Transition start state must not be null");
        }
        if (transition.getEnd() == null) {
            throw new IllegalArgumentException("Transition end state must not be null");
        }
        transitions.put(transition.getStart(), transition);

        return this;
    }

    public Multimap<S, Transition<S, C, X>> build() {
        return transitions;
    }

}
