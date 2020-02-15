package scheduler;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import common.FloorMessage;
import common.ElevatorState;

/**
 * An internal model used to store information about an 
 * information. Used by the scheduler.
 * 
 * @author Michael Fan
 *
 */
public class Elevator {
	private int elevatorNum;
	private ElevatorState state;
	private Queue<FloorMessage> upQueue;
	private Queue<FloorMessage> downQueue;
	
	public Elevator(int elevatorNum) {
		this.elevatorNum = elevatorNum;
		state = ElevatorState.ILLEGAL;
		
		upQueue = new PriorityQueue<FloorMessage>(10, new Comparator<FloorMessage>() {

			@Override
			public int compare(FloorMessage x, FloorMessage y) {
				
				// In the up queue lower floor number has high priority
				if(x.getFloorNum() < y.getFloorNum()) {
					return 1;
				} else if(x.getFloorNum() > y.getFloorNum()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		upQueue = new PriorityQueue<FloorMessage>(10, new Comparator<FloorMessage>() {

			@Override
			public int compare(FloorMessage x, FloorMessage y) {
				
				// In the down queue higher floor number has high priority
				if(x.getFloorNum() > y.getFloorNum()) {
					return 1;
				} else if(x.getFloorNum() < y.getFloorNum()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}
	
	/**
	 * @return the elevatorNum
	 */
	public int getElevatorNum() {
		return elevatorNum;
	}

	/**
	 * @return the state
	 */
	public ElevatorState getState() {
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(ElevatorState state) {
		this.state = state;
	}

	/**
	 * @return the upQueue
	 */
	public Queue<FloorMessage> getUpQueue() {
		return upQueue;
	}

	/**
	 * @return the downQueue
	 */
	public Queue<FloorMessage> getDownQueue() {
		return downQueue;
	}
}
