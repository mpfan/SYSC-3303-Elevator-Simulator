package elevator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Calendar;


import common.FloorMessage;
import common.Message;
import common.MessageType;
import common.ElevatorState.Transition;

/**
 * 
 * Class representing the Elevator class
 * 
 * @author Derek Shao, Souheil Yazji
 *
 */
public class Elevator implements Runnable {

	// Variables
	private int elevatorNumber; // elevator identifier
	private int capacity;
	private int people;
	private int currFloor; //current floor
	private int currDest; //current destination
	private boolean door;
	private boolean[] buttonPressed;
	private ElevatorSystem eleSys;
	private Message msg, eleMsg;

	private HashSet<Integer> destinations;

	private ElevatorStateMachine state;
	private ElevatorMode mode; // indicates if the elevator is meant to go up or down

	private static final int CAPACITY = 19;

	/**
	 * Constructor for elevator
	 * 
	 * @param numberOfButtons
	 *            number of buttons in the elevator
	 * @param elevatorNumber
	 *            the elevator number
	 * @param eleSys
	 *            the elevator system
	 */
	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys) {
		this.capacity = CAPACITY;
		this.people = 0;
		this.door = false;
		this.buttonPressed = new boolean[numberOfButtons];
		this.eleSys = eleSys;
		this.elevatorNumber = elevatorNumber;
		this.state = new ElevatorStateMachine();
		this.destinations = new HashSet<Integer>();
		this.currFloor = 0;
	}

	/**
	 * Method to run
	 */
	@Override
	public void run() {

		while (true) {
			processMessage();
		}
	}

	/**
	 * Method to process the message sent by elevator system
	 */
	public synchronized void processMessage() {
		while (msg == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Elevator: Processing message in elevator...");
		loadFloorMessage(msg);
		System.out.println("Elevator " + elevatorNumber +  ": " + msg.getBody());
		
		while(currFloor != currDest) {
			if(currFloor < currDest) {
				state.onNext(Transition.RECEIVEDMESSAGE_UP);
				currFloor++;
			} else if (currFloor > currDest) {
				state.onNext(Transition.RECEIVEDMESSAGE_DOWN);
				currFloor--;
			} else {
				state.onNext(Transition.REACHEDDESTINATION);
			}
			createEleMsg(); // create new elevator message
			this.eleSys.addOutboundMessage(eleMsg);	// send to system to send to scheduler		
		}
		
		
		msg.setType(MessageType.ELEVATOR);
		this.eleSys.addOutboundMessage(msg);
		
		this.msg = null;
		notifyAll();
	}

	/**
	 * Method for elevator to do work specified by message
	 * 
	 * @param msg
	 *            Message with work for elevator to do
	 */
	public synchronized void request(Message msg) {

		while (this.msg != null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Elevator: setting message  to elevator with message: " + msg.getBody());
		this.msg = msg;
		notifyAll();
	}

	/**
	 * Get the message currently stored in elevator
	 * 
	 * @return messaged stored in elevator
	 */
	public Message getMessage() {

		return msg;
	}

	/**
	 * Set the elevator's capacity of the number of people
	 * 
	 * @param capacity
	 *            The capacity of the elevator
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Return the elevator's capacity for the number of people
	 * 
	 * @return the elevator's capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return the currFloor
	 */
	public int getCurrFloor() {
		return currFloor;
	}

	/**
	 * Set the amount of people in the elevator
	 * 
	 * @param people
	 *            The amount of people in the elevator
	 * @return true if the number of people has been changed, false otherwise
	 */
	public boolean setPeople(int people) {
		if (people > capacity) {
			return false;
		}
		this.people = people;
		return true;
	}

	/**
	 * Returns the number of people in the elevator
	 * 
	 * @return amount of people currently in elevator
	 */
	public int getPeople() {
		return people;
	}

	/**
	 * Return the elevator system instance
	 * 
	 * @return the elevatorSystem instance
	 */
	public ElevatorSystem getElevatorSystem() {
		return eleSys;
	}

	/**
	 * Set the elevator system instance
	 * 
	 * @param elevatorSystem
	 *            the elevatorSystem to set
	 */
	public void setElevatorSystem(ElevatorSystem eleSys) {
		this.eleSys = eleSys;
	}

	/**
	 * Returns the elevator number
	 * 
	 * @return the elevatorNumber
	 */
	public int getElevatorNumber() {
		return elevatorNumber;
	}

	/**
	 * Sets the elevator number
	 * 
	 * @param elevatorNumber
	 *            the elevatorNumber to set
	 */
	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

	/**
	 * Add destination to elevator's Set of destination floors
	 * 
	 * @param destination
	 */
	public void addDestination(Integer destination) {
		this.destinations.add(destination);
	}

	/**
	 * Mode indicating if the elevator is meant to go up or down
	 *
	 */
	public enum ElevatorMode {
		UP, DOWN
	}
	
	
	public void loadFloorMessage(Message message) {
		FloorMessage msg = new FloorMessage(message);
		Integer newDest = new Integer(msg.getFloorNum());
		
//		if (!destinations.isEmpty()) {
//			if (mode == ElevatorMode.DOWN && newDest > currDest) {
//				currDest = newDest;
//			} 
//		} else {
//			currDest = newDest;
//		}
		currDest = newDest;
		addDestination(newDest);
	}
	
	
	
	private Message createEleMsg() {
		Calendar cal = Calendar.getInstance();
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);
		
		String body = hh + ":" + mm + ":" + ss + ":" + ms + "," + currFloor + "," + state + "," + currDest + "," + elevatorNumber;

		eleMsg = new Message(MessageType.ELEVATOR, body);
		return eleMsg;
	}
	
}
