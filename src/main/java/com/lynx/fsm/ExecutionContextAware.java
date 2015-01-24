package com.lynx.fsm;

public interface ExecutionContextAware<X> {
    void setExecutionContext(X executionContext);

}
