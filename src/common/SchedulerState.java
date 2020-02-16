package common;

/**
 * A enum class for the scheduler states
 * 
 * @author Michael Fan, Hoang Bui
 */
public enum SchedulerState {

	IDLE {
		@Override
		public SchedulerState next(Transition transition) {
			if (transition == Transition.RECEIVED_MESSAGE) { //Check if the scheduler has received an message
				return BUSY;
			}
			
			return ILLEGAL; //transition is was illegal
		}
	},
	BUSY {
		@Override
		public SchedulerState next(Transition transition) {
			if (transition == Transition.FINISHED_SCHEDULING) { //Check if the scheduler has finish scheduling
				return IDLE;
			}
			return ILLEGAL; //transition is was illegal
		}
	},
	ILLEGAL {
		@Override
		public SchedulerState next(Transition transition) {
			return ILLEGAL;
		}
	};

	/* Common method to move to next transition */
	public abstract SchedulerState next(Transition transition);

	/**
	 * Transitions to change states
	 */
	public static enum Transition {
		RECEIVED_MESSAGE,
		FINISHED_SCHEDULING
	}
}