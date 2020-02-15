package elevator;

import common.ElevatorState;

/**
 * Enum representing elevator modes - UP, DOWN, or IDLE
 *
 */
public enum ElevatorMode {
	UP {
		@Override
		public boolean canMoveToFloor(int currentFloor, int targetFloor, ElevatorState state) {
			// if elevator is DOOR OPEN/DOOR CLOSE - could process if target floor above
			// current floor

			switch (state) {
			case MOVINGUP:
				// if elevator is MOVING UP - return true if target floor is above current floor
				return targetFloor > currentFloor;
			case MOVINGDOWN:
				// if elevator is MOVING DOWN - cannot process even if target floor is below
				// current floor
				return false;
			case DOOROPEN:
				return targetFloor > currentFloor;
			case DOORCLOSE:
				return targetFloor > currentFloor;
			case IDLE:
				return true;
			default:
				return false;
			}
		}
	},
	DOWN {
		@Override
		public boolean canMoveToFloor(int currentFloor, int targetFloor, ElevatorState state) {

			switch (state) {
			case MOVINGUP:
				// if elevator is MOVING UP - cannot process even if target floor is above current floor
				return false;
			case MOVINGDOWN:
				// if elevator is MOVING DOWN - cannot process even if target floor is below
				// current floor
				return targetFloor < currentFloor;
			case DOOROPEN:
				return targetFloor < currentFloor;
			case DOORCLOSE:
				return targetFloor < currentFloor;
			case IDLE:
				return true;
			default:
				return false;
			}
		}
	},
	IDLE {
		@Override
		public boolean canMoveToFloor(int currentFloor, int targetFloor, ElevatorState state) {

			return true;
		}
	};
	
	/* Common method to check if incoming button press could move to new floor */
	public abstract boolean canMoveToFloor(int currentFloor, int targetFloor, ElevatorState state);
}
