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
		synchronized(this.state) {
			ElevatorState next = this.state.next(transition);
			if(next != ElevatorState.ILLEGAL) {
				this.state = next;
			}
		}
	}

	/**
	 * Get the current state
	 * 
	 * @return the current state
	 */
	public ElevatorState getCurrentState() {
		return this.state;
	}
	
	/**
	 * Set the current state
	 * 
	 * @param state the state to set to
	 */
	public void setState(ElevatorState state) {
		this.state = state;
	}
}
