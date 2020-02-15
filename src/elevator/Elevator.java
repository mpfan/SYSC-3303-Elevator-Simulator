package elevator;

import java.util.HashSet;
import java.util.Calendar;

import common.FloorMessage;
import common.Message;
import common.MessageType;
import common.ElevatorState;
import common.ElevatorState.Transition;

/**
 * 
 * Class representing the Elevator class
 * 
 * @author Derek Shao, Souheil Yazji, Christophe Tran
 *
 */
public class Elevator implements Runnable {

	// Variables
	private int elevatorNumber; // elevator identifier
	private int capacity;
	private int people;
	private int currFloor; // current floor
	private int currDest; // current destination
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
	 * @param numberOfButtons number of buttons in the elevator
	 * @param elevatorNumber  the elevator number
	 * @param eleSys          the elevator system
	 */
	public Elevator(int numberOfButtons, int elevatorNumber, ElevatorSystem eleSys, int currFloor) {
		this.capacity = CAPACITY;
		this.people = 0;
		this.door = false;
		this.buttonPressed = new boolean[numberOfButtons];
		this.eleSys = eleSys;
		this.elevatorNumber = elevatorNumber;
		this.state = new ElevatorStateMachine();
		this.destinations = new HashSet<Integer>();
		this.currFloor = currFloor;
		this.currDest = -1;
		this.mode = ElevatorMode.IDLE;
		this.msg = null;
	}

	/**
	 * Method to run
	 */
	@Override
	public void run() {

		Thread moveThread = new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {

					ElevatorState currentState = state.getCurrentState();
					
					Transition transition = getElevatorDirection();
					
					if (currentState == ElevatorState.DOORCLOSE) {
						if (mode.canMoveToFloor(currFloor, currDest, currentState)) {
							transition = mode == ElevatorMode.UP ? Transition.PRESS_UP : Transition.PRESS_DOWN;
						}
					}

					if (transition != null) {
						state.onNext(transition);
					}
					
					ElevatorState nextState = state.getCurrentState();

					if (state.getCurrentState() == ElevatorState.MOVINGDOWN) {
						currFloor--;
					} else if (state.getCurrentState() == ElevatorState.MOVINGUP) {
						currFloor++;
					}

					if (state.getCurrentState() == ElevatorState.DOOROPEN) {
						door = true;
					} else if (state.getCurrentState() == ElevatorState.DOORCLOSE) {
						door = false;
					}

					if (currentState == nextState && (currentState == ElevatorState.IDLE
							|| currentState == ElevatorState.DOOROPEN || currentState == ElevatorState.DOORCLOSE)) {
						
						try {
							Thread.sleep(4700); // this is the amount of time it takes for the Elevator to move across floors
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					
					
					if (state.getCurrentState() != ElevatorState.IDLE) {
						createEleMsg();
						eleSys.addOutboundMessage(eleMsg);
					}

					try {
						Thread.sleep(4700); // this is the amount of time it takes for the Elevator to move across floors
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		moveThread.start();

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
		System.out.println("Elevator " + elevatorNumber + ": " + msg.getBody());

		FloorMessage receivedMsg = loadFloorMessage(msg);

		System.out.println("Elevator: message: " + receivedMsg.toMessage().getBody());

		System.out.println("Current state: " + this.state.getCurrentState());

		System.out.println("Elevator message direction: " + receivedMsg.getDirection());

		if (receivedMsg.getDirection().equalsIgnoreCase("UP")) {
			this.mode = ElevatorMode.UP;
		} else if (receivedMsg.getDirection().equalsIgnoreCase("DOWN")) {
			this.mode = ElevatorMode.DOWN;
		}
		
		if (receivedMsg.getDirection().equalsIgnoreCase("FINISHED_LOAD")) {
			this.state.onNext(Transition.LOAD);
		} else if (state.getCurrentState() == ElevatorState.IDLE) {
			this.state.onNext(getElevatorDirection());
		}

		System.out.println("New state: " + this.state.getCurrentState());

		createEleMsg(); // create new elevator message
		this.eleSys.addOutboundMessage(eleMsg); // send to system to send to scheduler

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
		System.out.println("Elevator: setting message to: " + msg.getBody());
		this.msg = msg;
		notifyAll();
	}

	/**
	 * Add a new destination for elevator to travel to
	 * 
	 * @param targetFloor the floor number for elevator to travel to
	 */
	public boolean pressButton(int targetFloor) {

		if (targetFloor >= buttonPressed.length || targetFloor < 0) {
			return false; // if the floor number is invalid, immediately return
		}

		if (this.mode.canMoveToFloor(currFloor, targetFloor, this.state.getCurrentState())) {
			synchronized (destinations) {
				this.destinations.add(targetFloor);
			}

			return true;
		} else if (this.state.getCurrentState() == ElevatorState.IDLE) {

			synchronized (destinations) {
				this.destinations.add(targetFloor);

				if (targetFloor > currFloor) {
					this.mode = ElevatorMode.UP;
					this.state.onNext(Transition.PRESS_UP);
				} else if (targetFloor < currFloor) {
					this.mode = ElevatorMode.DOWN;
					this.state.onNext(Transition.PRESS_DOWN);
				}

				return true;
			}
		}

		return false;
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
	 * @return the currFloor
	 */
	public int getCurrFloor() {
		return currFloor;
	}

	/**
	 * Set the amount of people in the elevator
	 * 
	 * @param people The amount of people in the elevator
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

	/**
	 * Add destination to elevator's Set of destination floors
	 * 
	 * @param destination
	 */
	public void addDestination(Integer destination) {
		this.destinations.add(destination);
	}

	/**
	 * sets the elevator's destination from the floor message
	 * 
	 * @param message
	 */
	public FloorMessage loadFloorMessage(Message message) {
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

		return msg;
	}

	/**
	 * Get the elevator direction
	 * 
	 * @param msg
	 * @return
	 */
	public Transition getElevatorDirection() {
		Transition direction;

		if (currDest < 0) {
			return null;
		}

		if (currFloor > currDest) {
			direction = Transition.RECEIVEDMESSAGE_DOWN;
		} else if (currFloor < currDest) {
			direction = Transition.RECEIVEDMESSAGE_UP;
		} else {
			direction = Transition.REACHEDDESTINATION;
		}

		return direction;
	}

	/**
	 * 
	 * 
	 * @return returns the message to be sent to the elevator
	 */
	private Message createEleMsg() {
		Calendar cal = Calendar.getInstance();
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);

		String body = hh + ":" + mm + ":" + ss + ":" + ms + "," + currFloor + "," + state.getCurrentState() + ","
				+ currDest + "," + elevatorNumber;

		eleMsg = new Message(MessageType.ELEVATOR, body);
		return eleMsg;
	}

}
