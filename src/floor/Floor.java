package floor;

import java.util.*;
import common.*;
import scheduler.Scheduler;

/**
 * Class to represesent a floor
 *
 * @author Christophe Tran, Hoang Bui
 */
public class Floor implements Runnable {
	
	// Variables
	private Scheduler scheduler;
	Hashtable<Integer, FloorDoor> doors;
	int people;
	int floorNum;
	FloorSystem floorSystem;
	boolean isDownButtonPressed;
	boolean isUpButtonPressed;
	boolean isUpLamp; 
	boolean isDownLamp;
	List<Message> responses;
	
		
	/**
	 * Constructor for Floor class
	 *
	 * @param floorSys The floor system that manages all floors
	 * @param scheduler The scheduler that requests are sent to
	 * @param floorNum The current floor number
	 * @param numElev The number of elevators
	 */
	public Floor(FloorSystem floorSys, Scheduler scheduler, int floorNum, int numElev) {
		this.floorSystem = floorSys;
		this.scheduler = scheduler;
		this.floorNum = floorNum;
		this.people = 0;
		this.isDownButtonPressed = false;
		this.isUpButtonPressed = false;
		this.isUpLamp = false;
		this.isDownLamp = false;
		
		//create the doors 
		doors = new Hashtable<Integer, FloorDoor>();
		for(int i = 1; i < numElev; i++) {
			doors.put(i, new FloorDoor());
		}
	}

	/**
	* Method to receive message from floor system
	*
	* @param msg Message received from floor system
	*/
	public void receivedMessage(Message msg) {
		System.out.println("Floor " + Integer.toString(floorNum) + " received message:");
		System.out.println(msg.getBody());
	}

	/**
	* Method to run commands
	*/
	@Override
	public void run() {
		//When elevator has arrived
		//this.floorSystem.addMessage(new Message(MessageType.ELEVATOR, "oi"));	
		
		// 
	}

	/**
	* Method to respond back to a message
	*
	* @param msg Message received
	*/
	public void response(Message msg) {
		
		receivedMessage(msg);
		this.floorSystem.addMessage(msg);
	}
	
}
