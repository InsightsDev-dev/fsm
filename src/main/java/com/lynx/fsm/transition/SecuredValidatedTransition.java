package com.lynx.fsm.transition;

import com.lynx.fsm.BusinessValidator;
import com.lynx.fsm.StateHolder;
import com.lynx.fsm.StateType;

// TODO Get rid of Google libraries dependencies, implement Multimap
public class SecuredValidatedTransition<S extends StateType, C extends StateHolder<S>, X> extends Transition<S, C, X> {
    Class<?>[] validationGroups;
    String[] roles;
    BusinessValidator<S, C> validator;

    public SecuredValidatedTransition() {
        super();
    }

    public SecuredValidatedTransition(S start) {
        super(start);
    }

    public SecuredValidatedTransition(S start, S end) {
        super(start,end);
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

    public BusinessValidator<S, C> getValidator() {
        return validator;
    }

    public void setValidator(BusinessValidator<S, C> validator) {
        this.validator = validator;
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
        SecuredValidatedTransition<?, ?, ?> other = (SecuredValidatedTransition<?, ?, ?>) obj;
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
