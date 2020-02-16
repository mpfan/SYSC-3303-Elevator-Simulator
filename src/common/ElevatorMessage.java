package common;

import common.ElevatorState;
import common.Message;

/**
 * Wrapper for an Elevator message.
 * 
 * @author Michael Fan 101029934, Hoang bui
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
	 * Method to obtain the time
	 * 
	 * @return time the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Method to obtain the current floor that the elevator is on
	 * 
	 * @return currentFloor the current floor the elevator is on
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * Method to obtain the elevator's state
	 * 
	 * @return state the elevator's state
	 */
	public ElevatorState getState() {
		return state;
	}

	/**
	 * Method to obtain the destination of the elevator
	 * 
	 * @return destination the destination of the elevator
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * Method to obtain the elevator's number
	 * 
	 * @return elevatorNum the elevator's number
	 */
	public int getElevatorNum() {
		return elevatorNum;
	}
	
	/**
	 * Method obtain a message based on the elevator
	 * 
	 * @return the content of message wrapped in a message object
	 */
	public Message toMessage() {
		return new Message(MessageType.ELEVATOR, time + "," + currentFloor + "," + state + "," + destination + ","+ elevatorNum);
	}
}
