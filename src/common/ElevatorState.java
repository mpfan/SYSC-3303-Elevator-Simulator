package common;

/**
 * A enum class for the elevator states
 * 
 * @author Hoang Bui, Derek Shao
 */
public enum ElevatorState {

	IDLE {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.RECEIVEDMESSAGE_UP) { //Check if the elevator should move up
				return MOVINGUP;
			} else if (transition == Transition.RECEIVEDMESSAGE_DOWN) { //Check if the elevator should move down
				return MOVINGDOWN;
			}else if (transition == Transition.REACHEDDESTINATION) { //Check if the elevator has reached their destination
				return DOOROPEN;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	MOVINGUP {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.REACHEDDESTINATION) { //Check if the elevator reached their destination
				return DOOROPEN;
			}else if( transition == Transition.RECEIVEDMESSAGE_UP) { //Check if the elevator is currently moving up
				return MOVINGUP;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	MOVINGDOWN {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.REACHEDDESTINATION) { //Check if the elevator reached their destination
				return DOOROPEN;
			}else if( transition == Transition.RECEIVEDMESSAGE_DOWN) { //Check if the elevator is currently moving down
				return MOVINGDOWN;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	DOOROPEN {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.LOAD || transition == Transition.UNLOAD) { //Check if people are entering/leaving
				return DOORCLOSE;
			}
			return ILLEGAL; //The current transition is invalid
		}
	},
	DOORCLOSE {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.PRESS_UP) { //Check if someone pressed up in the elevator
				return MOVINGUP;
			} else if (transition == Transition.PRESS_DOWN) { //Check if someone pressed down in the elevator
				return MOVINGDOWN;
			} else if (transition == Transition.REACHEDDESTINATION) { //Check  if the elevator has reached it's destination
				return IDLE;
			} 
			return ILLEGAL; //The current transition is invalid
		}
	},
	ILLEGAL {
		@Override
		public ElevatorState next(Transition transition) {
			return ILLEGAL;
		}
	};

	/* Common method to move to next transition */
	public abstract ElevatorState next(Transition transition);

	/**
	 * Transitions to change states
	 */
	public static enum Transition {
		RECEIVEDMESSAGE_UP, // received a message for elevator to go up
		RECEIVEDMESSAGE_DOWN, // received a message for elevator to go down
		REACHEDDESTINATION, LOAD, UNLOAD, PRESS_UP, // indicates elevator button pressed above the current floor
		PRESS_DOWN, // indicates elevator button pressed below the current floor
		NOACTION
	}
}
