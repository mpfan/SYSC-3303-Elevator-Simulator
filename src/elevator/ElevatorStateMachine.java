package elevator;

import common.ElevatorState;
import common.ElevatorState.Transition;

/**
 * 
 * A class for the elevator's state machine
 * 
 * @author Hoang, Derek Shao, Christophe Tran
 */
public class ElevatorStateMachine {

	/* The current Elevator state */
	private ElevatorState state;

	/**
	 * Creates a new Elevator state machine
	 */
	public ElevatorStateMachine() {
		this.state = ElevatorState.IDLE;
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
	public ElevatorState getCurrentState() {
		return this.state;
	}
}
