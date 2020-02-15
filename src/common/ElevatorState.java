package common;

/**
 * A enum class for the elevator states
 * 
 * @author Hoang, Derek Shao
 */
public enum ElevatorState {

	IDLE {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.RECEIVEDMESSAGE_UP) {
				return MOVINGUP;
			} else if (transition == Transition.RECEIVEDMESSAGE_DOWN) {
				return MOVINGDOWN;
			}else if (transition == Transition.REACHEDDESTINATION) {
				return DOOROPEN;
			}
			return ILLEGAL;
		}
	},
	MOVINGUP {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.REACHEDDESTINATION) {
				return DOOROPEN;
			}else if( transition == Transition.RECEIVEDMESSAGE_UP) {
				return MOVINGUP;
			}
			return ILLEGAL;
		}
	},
	MOVINGDOWN {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.REACHEDDESTINATION) {
				return DOOROPEN;
			}else if( transition == Transition.RECEIVEDMESSAGE_DOWN) {
				return MOVINGDOWN;
			}
			return ILLEGAL;
		}
	},
	DOOROPEN {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.LOAD || transition == Transition.UNLOAD) {
				return DOORCLOSE;
			}
			return ILLEGAL;
		}
	},
	DOORCLOSE {
		@Override
		public ElevatorState next(Transition transition) {
			if (transition == Transition.PRESS_UP) {
				return MOVINGUP;
			} else if (transition == Transition.PRESS_DOWN) {
				return MOVINGDOWN;
			}
			return ILLEGAL;
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
	 *
	 */
	public static enum Transition {
		RECEIVEDMESSAGE_UP, // received a message for elevator to go up
		RECEIVEDMESSAGE_DOWN, // received a message for elevator to go down
		REACHEDDESTINATION, LOAD, UNLOAD, PRESS_UP, // indicates elevator button pressed above the current floor
		PRESS_DOWN, // indicates elevator button pressed below the current floor
		NOACTION
	}
}
