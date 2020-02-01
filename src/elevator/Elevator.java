package elevator;

import common.Message;
import common.MessageType;

/**
 * 
 * Class representing the Elevator class
 * 
 * @author Derek Shao, Souheil Yazji
 *
 */
public class Elevator implements Runnable {

	//Variables
	private int elevatorNumber; // elevator identifier
	private int capacity;
	private int people;
	private boolean door;
	private boolean[] buttonPressed;
	private ElevatorSystem eleSys;
	private Message msg;

	private static final int CAPACITY = 19;

	/**
	 * Constructor for elevator
	 * 
	 * @param numberOfButtons number of buttons in the elevator
	 * @param elevatorNumber the elevator number
	 * @param eleSys the elevator system
	 */
	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys) {
		this.capacity = CAPACITY;
		this.people = 0;
		this.door = false;
		this.buttonPressed = new boolean[numberOfButtons];
		this.eleSys = eleSys;
		this.elevatorNumber = elevatorNumber;
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
	 *  Method to process the message sent by elevator system
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
		System.out.println("Elevator: " + msg.getBody());
		msg.setType(MessageType.ELEVATOR);
		this.eleSys.addOutboundMessage(msg);
		this.msg = null;
		notifyAll();
	}
	
	/**
	 * Method for elevator to do work specified by message
	 * 
	 * @param msg Message with work for elevator to do
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
	 * Set the elevator's capacity of the number of people
	 * 
	 * @param capacity The capacity of the elevator
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
	 * Set the amount of people in the elevator
	 * 
	 * @param people The amount of people in the elevator
	 * @return true if the number of people has been changed, false otherwise
	 */
	public boolean setPeople(int people) {
		if(people > capacity) {
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
	 * @param elevatorSystem the elevatorSystem to set
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
	 * @param elevatorNumber the elevatorNumber to set
	 */
	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

}
