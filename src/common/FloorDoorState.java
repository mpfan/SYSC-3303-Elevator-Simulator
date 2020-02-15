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
			if (transition == Transition.RECEIVEDMESSAGE) {
				return OPEN;
			}
			return ILLEGAL;
		}
	},
	OPEN {
		@Override
		public FloorDoorState next(Transition transition) {
			if (transition == Transition.LOAD || transition == Transition.UNLOAD) {
				return CLOSE;
			}
			return ILLEGAL;
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
