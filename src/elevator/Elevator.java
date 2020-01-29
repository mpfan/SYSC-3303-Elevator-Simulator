package elevator;

import common.Message;
import common.MessageType;
import common.Respondable;
import scheduler.Scheduler;
import common.Requestable;

public class Elevator implements Runnable {

	private Scheduler scheduler;
	private int elevatorNumber;
	private int capacity;
	private int people;
	boolean door;
	boolean[] buttonPressed;
	private ElevatorSystem eleSys;
	private Message msg;

	private static final int CAPACITY = 19;

	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys, Scheduler scheduler) {
		this.capacity = CAPACITY;
		this.people = 0;
		this.door = false;
		this.buttonPressed = new boolean[numberOfButtons];
		this.eleSys = eleSys;
		this.elevatorNumber = elevatorNumber;
		this.scheduler = scheduler;
	}

	public void receivedMessage(Message msg) {
		System.out.println("Elevator " + Integer.toString(elevatorNumber) + " received message:");
		System.out.println(msg.getBody());
	}

	@Override
	public void run() {
		synchronized (msg) {
			while (true) {
				while (msg == null) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Processing message...");
				this.eleSys.addOutboundMessage(msg);
				this.msg = null;
				notifyAll();
			}
		}
	}

	public void request(Message msg) {
		synchronized (msg) {
			while (msg != null) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.msg = msg;
			notifyAll();
		}
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
