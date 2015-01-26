package com.lynx.fsm;

// TODO Get rid of Google libraries dependencies, implement Multimap
public class Transition<S extends StateType, C extends StateHolder<S>, X> {
    S start;
    S end;
    Class<?>[] validationGroups;
    String[] roles;
    ConditionChecker<S, C> condition;
    BusinessValidator<S, C> validator;
    ActionExecutor<S, C, X> beforeAction;
    ActionExecutor<S, C, X> afterAction;

    public Transition() {
    }

    public Transition(S start) {
        this.start = start;
    }

    public Transition(S start, S end) {
        this.start = start;
        this.end = end;
    }

    public S getStart() {
        return start;
    }

    public void setStart(S start) {
        this.start = start;
    }

    public S getEnd() {
        return end;
    }

    public void setEnd(S end) {
        this.end = end;
    }

    public Class<?>[] getValidationGroups() {
        return validationGroups;
    }

    public void setValidationGroups(Class<?>[] validationGroups) {
        this.validationGroups = validationGroups;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public ConditionChecker<S, C> getCondition() {
        return condition;
    }

    public void setCondition(ConditionChecker<S, C> condition) {
        this.condition = condition;
    }

    public BusinessValidator<S, C> getValidator() {
        return validator;
    }

    public void setValidator(BusinessValidator<S, C> validator) {
        this.validator = validator;
    }

    public ActionExecutor<S, C, X> getBeforeAction() {
        return beforeAction;
    }

    public void setBeforeAction(ActionExecutor<S, C, X> beforeAction) {
        this.beforeAction = beforeAction;
    }

    public ActionExecutor<S, C, X> getAfterAction() {
        return afterAction;
    }

    public void setAfterAction(ActionExecutor<S, C, X> afterAction) {
        this.afterAction = afterAction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transition<?, ?, ?> other = (Transition<?, ?, ?>) obj;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return start + "->" + end;
    }

}
