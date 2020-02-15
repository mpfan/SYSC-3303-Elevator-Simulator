package common;

/**
 * A enum class for the scheduler states
 * 
 * @author Michael Fan
 */
public enum SchedulerState {

	IDLE {
		@Override
		public SchedulerState next(Transition transition) {
			if (transition == Transition.RECEIVED_MESSAGE) {
				return BUSY;
			}
			
			return ILLEGAL;
		}
	},
	BUSY {
		@Override
		public SchedulerState next(Transition transition) {
			if (transition == Transition.FINISHED_SCHEDULING) {
				return IDLE;
			}
			return ILLEGAL;
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