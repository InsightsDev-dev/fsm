package com.lynx.fsm.machine;

import java.util.Collection;

import com.google.common.collect.Multimap;
import com.lynx.fsm.BeanValidating;
import com.lynx.fsm.Secured;
import com.lynx.fsm.StateHolder;
import com.lynx.fsm.StateType;
import com.lynx.fsm.transition.Transition;

/**
 * Abstract state machine providing validation and configuration operations
 * 
 * @author daniel.las
 *
 * @param <S>
 *            state type, must extend {@link StateHolder}
 * @param <C>
 *            context type
 * @param <X>
 *            execution context type
 */
public abstract class SecuredValidatingStateMachine<S extends StateType, C extends StateHolder<S>, X> extends
        StateMachine<S, C, X> implements BeanValidating, Secured {

    S state;
    C context;
    Multimap<S, Transition<S, C, X>> transitions;
    X executionContext;

    @Override
    public void forward() {
        if (state.isFinal()) {
            throw new StateMachineException("Machine is in FINAL state");
        }
        Collection<Transition<S, C, X>> possibleEnds = transitions.get(state);
        Transition<S, C, X> resolvedTransition = null;

        // There are no transitions for start state, no knowledge what to do
        if (possibleEnds.isEmpty()) {
            throw new StateMachineException(String.format("There are no transitions for start state %s", state));
        }
        // We have only one transition with given start
        if (possibleEnds.size() == 1) {
            resolvedTransition = possibleEnds.iterator().next();

            // There is condition which must be met
            if (resolvedTransition.getCondition() != null) {
                // Condition is not met
                if (!resolvedTransition.getCondition().check(resolvedTransition.getEnd(), context)) {
                    throw new StateMachineException(String.format("Condition for transition from %s to %s is not met",
                            state, resolvedTransition.getEnd()));
                }
            }
        } else {
            Transition<S, C, X> goodEnd = null;

            for (Transition<S, C, X> end : possibleEnds) {
                if (end.getCondition() == null) {
                    throw new StateMachineException(
                            String.format("There is no condition for possible transition from %s to %s. Decision can't be taken when no state is requested."));
                }
                if (end.getCondition().check(end.getEnd(), context)) {
                    // We already have good end so next state is ambiguous
                    if (goodEnd != null) {
                        throw new StateMachineException(
                                "There is more than one transition from state %s fullfilling condition");
                    }
                    goodEnd = end;
                }
            }
            resolvedTransition = goodEnd;
        }
        // No transition fulfilling conditions found
        if (resolvedTransition == null) {
            throw new StateMachineException(String.format(
                    "No transition fulfilling confition found for start state %s ", state));
        }
        // All is fine, we can validate, check permissions and move to next
        // state
        // Security is set, check permissions
        if (resolvedTransition.getRoles() != null) {
            checkPermissions(resolvedTransition.getRoles());
        }
        // Run action if executor is set
        if (resolvedTransition.getBeforeAction() != null) {
            resolvedTransition.getBeforeAction().execute(executionContext, context);
        }
        // Validation groups are set, process validation
        if (resolvedTransition.getValidationGroups() != null) {
            validate(resolvedTransition.getValidationGroups());
        }
        if (resolvedTransition.getValidator() != null) {
            resolvedTransition.getValidator().validate(resolvedTransition.getEnd(), context);
        }
        // Everything passed, we are good to go,
        state = resolvedTransition.getEnd();
        context.setState(state);
        // Run action if executor is set
        if (resolvedTransition.getAfterAction() != null) {
            resolvedTransition.getAfterAction().execute(executionContext, context);
        }
    }

    @Override
    public void forward(S requestedState) {
        if (state.isFinal()) {
            throw new StateMachineException("Machine is in FINAL state");
        }
        Collection<Transition<S, C, X>> possibleEnds = transitions.get(state);
        Transition<S, C, X> resolvedTransition = null;

        // There are no transitions for start state, no knowledge what to do
        if (possibleEnds.isEmpty()) {
            throw new StateMachineException(String.format("There are no transitions for start state %s", state));
        }
        // Find transition with end equals requestedState
        for (Transition<S, C, X> end : possibleEnds) {
            if (requestedState.equals(end.getEnd())) {
                resolvedTransition = end;
            }
        }
        // No transition found
        if (resolvedTransition == null) {
            throw new StateMachineException(String.format("No transition found for start state %s and end state %s",
                    state, requestedState));
        }
        // Good transition found, check condition if exists
        if (resolvedTransition.getCondition() != null) {
            if (!resolvedTransition.getCondition().check(requestedState, context)) {
                throw new StateMachineException(String.format(
                        "No transition fulfilling confition found for start state %s and requested state %s", state,
                        requestedState));
            }

        }
        // All is fine, we can validate, check permissions and move to next
        // state
        // Security is set, check permissions
        if (resolvedTransition.getRoles() != null) {
            checkPermissions(resolvedTransition.getRoles());
        }
        // Run action if executor is set
        if (resolvedTransition.getBeforeAction() != null) {
            resolvedTransition.getBeforeAction().execute(executionContext, context);
        }
        // Validation groups are set, process Bean validation
        if (resolvedTransition.getValidationGroups() != null) {
            validate(resolvedTransition.getValidationGroups());
        }
        // Business validator is set, process business validation
        if (resolvedTransition.getValidator() != null) {
            resolvedTransition.getValidator().validate(requestedState, context);
        }
        // Everything passed, we are good to go,
        state = resolvedTransition.getEnd();
        context.setState(state);
        // Run action if executor is set
        if (resolvedTransition.getAfterAction() != null) {
            resolvedTransition.getAfterAction().execute(executionContext, context);
        }
    }

    @Override
    public void setTransitions(Multimap<S, Transition<S, C, X>> transitions) {
        this.transitions = transitions;
    }

    @Override
    public void setContext(C context) {
        this.context = context;
        this.state = context.getState();
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public void setExecutionContext(X executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public abstract void validate(Class<?>... groups);

    @Override
    public abstract void checkPermissions(String... roles);

}
