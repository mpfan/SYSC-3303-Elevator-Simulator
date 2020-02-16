/**
 * 
 */
package floor;

import common.FloorDoorState;
import common.FloorDoorState.Transition;

/**
 * A class for the floor door's state machine
 * 
 * @author Hoang Bui
 */
public class FloorDoorStateMachine {
	/* The current Floor Door state */
	private FloorDoorState state;
	
	/**
	 * Creates a new Floor Door state machine
	 */
	public FloorDoorStateMachine() {
		this.state = FloorDoorState.CLOSE;
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
	public FloorDoorState getCurrentState() {
		return this.state;
	}
}
