package com.lynx.fsm;

public interface Controllable<S extends StateType> {
    /**
     * Move to requested state
     * 
     * @param requestedState
     */
    void forward(S requestedState);

}
