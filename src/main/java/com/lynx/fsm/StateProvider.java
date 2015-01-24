package com.lynx.fsm;

public interface StateProvider<S extends StateType> {
	S getState();
}
