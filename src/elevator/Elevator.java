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
 * @author Derek Shao, Souheil Yazji, Christophe Tran, Hoang Bui
 *
 */
public class Elevator implements Runnable {

	// Variables
	private int elevatorNumber; // elevator identifier
	private int capacity;
	private int people; //number of people inside the elevator
	private int currFloor; // current floor
	private int initialDest; // the initial destination for elevator
	private boolean door;
	private boolean[] buttonPressed;
	private ElevatorSystem eleSys;
	private Message msg;

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
		this.initialDest = -1;
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
					System.out.println("MoveThread: " + currentState);
					
					synchronized(state) {
						// if the elevator is not moving, then the moving thread should wait for a moving state
						if (currentState == ElevatorState.DOOROPEN || currentState == ElevatorState.IDLE) {
							try {
								state.notifyAll();
								state.wait();
								continue;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

					Transition transition = getElevatorDirection();
					System.out.println("MoveThread: elevator mode: " + mode);
					
					if (transition != null) {
						state.onNext(transition);
					} else {
						// when transition is null, there is no place for elevator to go
						state.onNext(Transition.REACHEDDESTINATION);
					}
					
					ElevatorState nextState = state.getCurrentState();
					
					if (nextState == ElevatorState.MOVINGDOWN) {
						currFloor--;
					} else if (nextState == ElevatorState.MOVINGUP) {
						currFloor++;
					}

					if (nextState == ElevatorState.DOOROPEN) {
						door = true;
					} else if (nextState == ElevatorState.DOORCLOSE) {
						door = false;
					}
					
					Message eleMsg = createEleMsg();
					eleSys.addOutboundMessage(eleMsg);

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
		
		Transition stateTransition = null;
		if (receivedMsg.getDirection().equalsIgnoreCase("FINISHED_LOAD")) {
			// people loaded onto elevator
			stateTransition = Transition.LOAD;
		} else {
			// else find the next state transition
			stateTransition = getElevatorDirection();
		}

		if (stateTransition != null) {
			synchronized(state) {
				state.onNext(stateTransition);
				state.notifyAll();
			}
		}
		
		System.out.println("New state: " + this.state.getCurrentState());
		
		// only send current state if elevator is not currently moving (if moving handled by moving thread)
		if (this.state.getCurrentState() != ElevatorState.MOVINGUP && this.state.getCurrentState() != ElevatorState.MOVINGDOWN) {
			Message eleMsg = createEleMsg();
			this.eleSys.addOutboundMessage(eleMsg); // send to system to send to scheduler
		}

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
	 * Get the floor the elevator is currently at
	 * 
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

		if(state.getCurrentState() != ElevatorState.DOOROPEN && state.getCurrentState() != ElevatorState.DOORCLOSE) {
			if (this.destinations.isEmpty()) { // only initial destiantion when elevator is idle/has no where to go
				initialDest = newDest;
			}
			addDestination(newDest);
		}

		return msg;
	}

	/**
	 * Get the elevator direction
	 * 
	 * @param msg
	 * @return The appropriate transition to get the proper direction
	 */
	public Transition getElevatorDirection() {
		Transition direction = null;
		
		// if the current destinations is empty, there is no place to go
		if (this.destinations.isEmpty()) {
			return null;
		}
		
		// if the elevator is at a destination
		if (this.destinations.contains(currFloor)) {
			direction = Transition.REACHEDDESTINATION;
			initialDest = -1; // reset the initial starting elevator destination
			synchronized(this.destinations) {
				this.destinations.remove(currFloor);
			}
		} else if (initialDest > 0 && initialDest > currFloor) {
			direction = Transition.RECEIVEDMESSAGE_UP;
		} else if (initialDest > 0 && initialDest < currFloor) {
			direction = Transition.RECEIVEDMESSAGE_DOWN;
		} else if (this.mode == ElevatorMode.UP) { // elevator is suppose to go up
			direction = Transition.RECEIVEDMESSAGE_UP;
		} else if (this.mode == ElevatorMode.DOWN) { // elevator is suppose to go down
			direction = Transition.RECEIVEDMESSAGE_DOWN;
		}

		return direction;
	}

	/**
	 * Method to create an elevator message
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
				+ initialDest + "," + elevatorNumber;

		return new Message(MessageType.ELEVATOR, body);
	}

}
