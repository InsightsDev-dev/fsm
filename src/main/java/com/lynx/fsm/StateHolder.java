package com.lynx.fsm;

/**
 * Current state holder
 * 
 * @author daniel.las
 *
 * @param <S>
 */
public interface StateHolder<S extends StateType> extends StateProvider<S> {

	void setState(S state);

}
