package elevator;

import common.Message;
import common.MessageType;
import common.Respondable;
import common.Requestable;

public class Elevator implements Respondable, Runnable {
	
	private Requestable scheduler; 
	int elevatorNumber;
	int capacity;
	int people;
	boolean door;
	boolean[] buttonPressed;
	ElevatorSystem eleSys;
	
	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys, Requestable scheduler) {
		this.capacity = 0;
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
		
		this.scheduler.request(new Message(MessageType.ELEVATOR, "hi"), this);
	}

	@Override
	public void response(Message msg) {
		
		receivedMessage(msg);
		this.eleSys.addMessage(msg);
	}	
}
