package com.lynx.fsm;

import com.google.common.collect.Multimap;
import com.lynx.fsm.transition.Transition;

/**
 * Defines configurable FSM
 * 
 * @author daniel.las
 *
 * @param <S>
 * @param <C>
 */
public interface Configurable<S extends StateType, C extends StateHolder<S>, X> {
    /**
     * Set FSM transitions
     * 
     * @param transitions
     */
    void setTransitions(Multimap<S, Transition<S, C, X>> transitions);
}
