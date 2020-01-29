package floor;

import java.util.*;
import common.*;
import scheduler.Scheduler;

/**
 * 
 * @author Christophe Tran, Hoang Bui
 *
 */
public class Floor implements Runnable {
	
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

	public void receivedMessage(Message msg) {
		System.out.println("Floor " + Integer.toString(floorNum) + " received message:");
		System.out.println(msg.getBody());
	}

	@Override
	public void run() {
		//When elevator has arrived
		//this.floorSystem.addMessage(new Message(MessageType.ELEVATOR, "oi"));	
		
		// 
	}

	public void response(Message msg) {
		
		receivedMessage(msg);
		this.floorSystem.addMessage(msg);
	}
	
}
