package common;

/**
 * A enum class for the floor door's states
 * 
 * @author Hoang Bui
 */
public enum FloorDoorState {
	CLOSE {
		@Override
		public FloorDoorState next(Transition transition) {
			if (transition == Transition.RECEIVEDMESSAGE) { //Check if the floor door has received a message
				return OPEN;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	OPEN {
		@Override
		public FloorDoorState next(Transition transition) {
			if (transition == Transition.LOAD || transition == Transition.UNLOAD) { //Check if people has entered/left through the door
				return CLOSE;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	ILLEGAL {
		@Override
		public FloorDoorState next(Transition transition) {
			return ILLEGAL;
		}
	};

	/* Common method to move to next transition */
	public abstract FloorDoorState next(Transition transition);

	/**
	 * Transitions to change states
	 */
	public static enum Transition {
		RECEIVEDMESSAGE, // received a message that the elevator has arrived
		LOAD, UNLOAD // indicates that people has entered or exited the elevator
	}
}
