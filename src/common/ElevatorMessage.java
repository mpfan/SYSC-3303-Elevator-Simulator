package common;

import common.ElevatorState;
import common.Message;

/**
 * Wrapper for an Elevator message.
 * 
 * @author Michael Fan 101029934
 *
 */
public class ElevatorMessage {
	private String time;

	private int currentFloor;
	private ElevatorState state;
	private int destination;
	private int elevatorNum;
	
	/**
	 * Constructs a new ElevatorMessage.
	 * 
	 * @param message the message that was sent by the ElevatorSystem
	 */
	public ElevatorMessage(Message message) {
		parse(message);
	}
	
	
	/**
	 * Parses an elevator message.
	 * 
	 * @param message the elevator message
	 */
	private void parse(Message message) {
		String[] mArr = message.getBody().split(",");
		
		time = mArr[0];
		currentFloor = Integer.parseInt(mArr[1]);
		state = ElevatorState.valueOf(mArr[2]);
		destination = Integer.parseInt(mArr[3]);
		elevatorNum = Integer.parseInt(mArr[4]);
	}
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return the currentFloor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * @return the direction
	 */
	public ElevatorState getState() {
		return state;
	}

	/**
	 * @return the destination
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * @return the elevatorNum
	 */
	public int getElevatorNum() {
		return elevatorNum;
	}
	
	/**
	 * @return wrapped in message object
	 */
	public Message toMessage() {
		return new Message(MessageType.ELEVATOR, time + "," + currentFloor + "," + state + "," + destination + ","+ elevatorNum);
	}
}
