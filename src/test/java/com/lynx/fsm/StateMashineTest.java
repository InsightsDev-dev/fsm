package com.lynx.fsm;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import javax.validation.constraints.NotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Multimap;

@RunWith(MockitoJUnitRunner.class)
public class StateMashineTest {

    @Mock
    ProcessDataRepository repository;

    ProcessFactory factory;

    static Multimap<State, Transition<State, ProcessData, Process>> SIMPLE_FLOW = Process.builder().from(State.INITIAL)
            .to(State.RFP).commit().from(State.RFP).to(State.RFP_CONFIRMED).commit().from(State.RFP_CONFIRMED)
            .to(State.FINAL).commit().build();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        factory = new ProcessFactory(repository);
    }

    @Test
    public void emptyStartEndTransitionsShouldBeEqual() {
        Transition<State, ProcessData, Process> tran1 = new Transition<State, ProcessData, Process>();
        Transition<State, ProcessData, Process> tran2 = new Transition<State, ProcessData, Process>();

        assertEquals(tran1, tran2);
    }

    @Test
    public void sameStartEndTransitionsShouldBeEqual() {
        Transition<State, ProcessData, Process> tran1 = new Transition<State, ProcessData, Process>(State.INITIAL,
                State.FINAL);
        Transition<State, ProcessData, Process> tran2 = new Transition<State, ProcessData, Process>(State.INITIAL,
                State.FINAL);

        assertEquals(tran1, tran2);
    }

    @Test
    public void differentEndTransitionsShouldNotBeEqual() {
        Transition<State, ProcessData, Process> tran1 = new Transition<State, ProcessData, Process>(State.INITIAL,
                State.FINAL);
        Transition<State, ProcessData, Process> tran2 = new Transition<State, ProcessData, Process>(State.INITIAL,
                State.RFP);

        Assert.assertNotEquals(tran1, tran2);
    }

    @Test
    public void differentStartTransitionsShouldNotBeEqual() {
        Transition<State, ProcessData, Process> tran1 = new Transition<State, ProcessData, Process>(State.INITIAL,
                State.FINAL);
        Transition<State, ProcessData, Process> tran2 = new Transition<State, ProcessData, Process>(State.RFP,
                State.FINAL);

        Assert.assertNotEquals(tran1, tran2);
    }
    
    @Test
    public void newProcessShouldBeInIitialState() {
        Process process = factory.create(SIMPLE_FLOW);

        assertTrue(process.getState().isInitial());
    }

    @Test
    public void noConditionTransitionShouldBeValid() {
        assertThat(3, is(SIMPLE_FLOW.size()));
        assertThat(State.INITIAL, is(SIMPLE_FLOW.get(State.INITIAL).iterator().next().getStart()));
    }

    @Test(expected = StateMachineException.class)
    public void notExistingStarStateShouldFail() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).commit().build();
        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.RFP));

        factory.load(1l, transitions).forward();
    }

    @Test(expected = StateMachineException.class)
    public void finalStateForwardShouldFail() {
        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.FINAL));

        factory.load(1l, SIMPLE_FLOW).forward();
    }

    @Test(expected = StateMachineException.class)
    public void noEndOnDecisionShouldFail() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).commit().from(State.INITIAL).to(State.RFP).commit().from(State.RFP).to(State.FINAL)
                .commit().build();

        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.INITIAL));
        factory.load(1l, transitions).forward(State.RFP_CONFIRMED);
    }

    @Test
    public void foundEndOnDecisionShouldPass() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).commit().from(State.INITIAL).to(State.RFP).commit().from(State.RFP).to(State.FINAL)
                .commit().build();

        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.INITIAL));
        Process process = factory.load(1l, transitions);

        process.forward(State.RFP);
        assertEquals(State.RFP, process.state);
        assertEquals(State.RFP, process.getContext().getState());
    }

    @Test(expected = StateMachineException.class)
    public void foundEndOnDecisionConditionFalseShouldFail() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).commit().from(State.INITIAL).check(new ConditionChecker<State, ProcessData>() {
                    @Override
                    public boolean check(State requestedState, ProcessData context) {
                        return false;
                    }
                }).to(State.RFP).commit().from(State.RFP).to(State.FINAL).commit().build();

        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.INITIAL));
        Process process = factory.load(1l, transitions);

        process.forward(State.RFP);
    }

    @Test
    public void foundEndOnDecisionConditionTrueShouldPass() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).commit().from(State.INITIAL).check(new ConditionChecker<State, ProcessData>() {
                    @Override
                    public boolean check(State requestedState, ProcessData context) {
                        return true;
                    }
                }).to(State.RFP).commit().from(State.RFP).to(State.FINAL).commit().build();

        when(repository.getOne(anyLong())).thenReturn(new ProcessData(State.INITIAL));
        Process process = factory.load(1l, transitions);

        process.forward(State.RFP);
        assertEquals(State.RFP, process.state);
        assertEquals(State.RFP, process.getContext().getState());
    }

    @Test
    public void actionShouldBeExecuted() {
        Multimap<State, Transition<State, ProcessData, Process>> transitions = Process.builder().from(State.INITIAL)
                .to(State.FINAL).exec(new ActionExecutor<State, ProcessData, Process>() {
                    @Override
                    public void execute(Process executionContext, ProcessData context) {
                        executionContext.action();

                    }
                }).commit().build();

        Process process = factory.create(transitions);

        process.forward();
    }

    private enum State implements StateType {
        INITIAL, RFP, RFP_CONFIRMED, FINAL;

        @Override
        public boolean isInitial() {
            return equals(INITIAL);
        }

        @Override
        public boolean isFinal() {
            return equals(FINAL);
        }

    }

    private static class Process extends StateMachine<State, ProcessData, Process> {

        public void action() {
            System.out.println("Executing action");
        }

        @Override
        public void validate(Class<?>... groups) {
            System.out.println("Bean validation");
        }

        @Override
        public void checkPermissions(String... roles) {
            System.out.println("Permissions checking");
        }

        public static TransitionsBuilder<State, ProcessData, Process> builder() {
            return new ProcessTransitionsBuilder();
        }

        public static class ProcessTransitionsBuilder extends DefaultTransitionsBuilder<State, ProcessData, Process> {

        }

    }

    private static class ProcessData implements StateHolder<State> {

        @NotNull
        private Long id;
        @NotNull(groups = Rfp.class)
        Object invoice;
        State state = State.INITIAL;

        public ProcessData() {
        }

        public ProcessData(State state) {
            this.state = state;
        }

        @SuppressWarnings("unused")
        public void setInvoice(Object invoice) {
            this.invoice = invoice;
        }

        @SuppressWarnings("unused")
        public Object getInvoice() {
            return invoice;
        }

        @SuppressWarnings("unused")
        public Long getId() {
            return id;
        }

        @SuppressWarnings("unused")
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public void setState(State state) {
            this.state = state;
        }

        @Override
        public State getState() {
            return state;
        }

        public static interface Rfp {
        };
    }

    private interface ProcessDataRepository {
        ProcessData getOne(Long id);
    }

    private class ProcessFactory {
        ProcessDataRepository repository;

        public ProcessFactory(ProcessDataRepository repository) {
            this.repository = repository;
        }

        Process create(Multimap<State, Transition<State, ProcessData, Process>> transitions) {
            Process process = new Process();

            process.setTransitions(transitions);
            process.setContext(new ProcessData());
            process.setExecutionContext(process);

            return process;
        }

        Process load(Long contextId, Multimap<State, Transition<State, ProcessData, Process>> transitions) {
            ProcessData context = repository.getOne(contextId);
            Process process = new Process();

            process.setTransitions(transitions);
            process.setContext(context);
            process.setExecutionContext(process);

            return process;
        }
    }

}
