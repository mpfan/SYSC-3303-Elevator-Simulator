package elevator;

import common.Message;

/**
 * 
 * Class representing the Elevator class
 * 
 * @author Derek Shao, Souheil Yazji
 *
 */
public class Elevator implements Runnable {

	private int elevatorNumber; // eleavtor indentifier
	private int capacity;
	private int people;
	private boolean door;
	private boolean[] buttonPressed;
	private ElevatorSystem eleSys;
	private Message msg;

	private static final int CAPACITY = 19;

	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys) {
		this.capacity = CAPACITY;
		this.people = 0;
		this.door = false;
		this.buttonPressed = new boolean[numberOfButtons];
		this.eleSys = eleSys;
		this.elevatorNumber = elevatorNumber;
	}

	@Override
	public void run() {

		while (true) {
			processMessage();
		}
	}
	
	/**
	 *  Process the message sent by elevator system
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
	 * @param capacity set the elevator's capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the elevator's capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param people set the amount of people in the elevator
	 */
	public void setPeople(int people) {
		this.people = people;
	}

	/**
	 * @return amount of people currently in elevator
	 */
	public int getPeople() {
		return people;
	}

	/**
	 * @param door the door to set
	 */
	public void setDoor(boolean door) {
		this.door = door;
	}

	/**
	 * @return
	 */
	public boolean getDoor() {
		return door;
	}

	/**
	 * @return the button pressed
	 */
	public boolean[] getButtonPressed() {
		return buttonPressed;
	}

	/**
	 * @param buttonPressed the button pressed to set
	 */
	public void setButtonPressed(boolean[] buttonPressed) {
		this.buttonPressed = buttonPressed;
	}

	/**
	 * @return the elevatorSystem
	 */
	public ElevatorSystem getElevatorSystem() {
		return eleSys;
	}

	/**
	 * @param elevatorSystem the elevatorSystem to set
	 */
	public void setElevatorSystem(ElevatorSystem eleSys) {
		this.eleSys = eleSys;
	}

	/**
	 * @return the elevatorNumber
	 */
	public int getElevatorNumber() {
		return elevatorNumber;
	}

	/**
	 * @param elevatorNumber the elevatorNumber to set
	 */
	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

}
