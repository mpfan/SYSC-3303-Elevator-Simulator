package scheduler;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import common.SchedulerState;
import common.SchedulerState.Transition;

public class SchedulerStateMachineTest {
	
	@Test
	void schedulerStateTest() {
		SchedulerStateMachine ssm = new SchedulerStateMachine();
		assertTrue(ssm.getCurrentState() == SchedulerState.IDLE);
		
		ssm.onNext(Transition.RECEIVED_MESSAGE);
		assertTrue(ssm.getCurrentState() == SchedulerState.BUSY);
		
		ssm.onNext(Transition.FINISHED_SCHEDULING);
		assertTrue(ssm.getCurrentState() == SchedulerState.IDLE);
		
		ssm.onNext(Transition.FINISHED_SCHEDULING);
		assertTrue(ssm.getCurrentState() == SchedulerState.ILLEGAL);
	}
}
