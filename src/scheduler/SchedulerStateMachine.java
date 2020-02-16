package scheduler;

import common.SchedulerState;
import common.SchedulerState.Transition;

/**
 * 
 * A class for the scheduler's state machine
 * 
 * @author Michael Fan
 */
public class SchedulerStateMachine {

	/* The current Elevator state */
	private SchedulerState state;

	/**
	 * Creates a new Elevator state machine
	 */
	public SchedulerStateMachine() {
		this.state = SchedulerState.IDLE;
	}

	/**
	 * Transition to the next state
	 * 
	 * @param transition The transition to the next state
	 */
	public void onNext(Transition transition) {
		this.state = this.state.next(transition);
	}

	/**
	 * Get the current state
	 * 
	 * @return the current state
	 */
	public SchedulerState getCurrentState() {
		return this.state;
	}
}