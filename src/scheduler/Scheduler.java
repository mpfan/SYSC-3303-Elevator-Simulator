package scheduler;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

import common.ElevatorMessage;
import common.ElevatorState;
import common.FloorMessage;
import common.Message;
import common.MessageListener;
import common.MessageType;
import common.Messenger;
import common.SchedulerState;

/**
 * This class represents a Scheduler. Scheduler will try to schedule 
 * if there are pending messages.
 * 
 * @author Michael Fan
 *
 */
public class Scheduler implements Runnable, MessageListener {
	public static int PORT = 8000;
	public static int ELEVATOR_PORT = 8001;
	public static int FLOOR_PORT = 8002;
	
	private SchedulerStateMachine stateMachine;
	private Messenger messenger;
	private Queue<Message> messages;
	private Map<Integer, Elevator> elevatorModel;
	
	/**
	 * Constructor for scheduler
	 */
	public Scheduler() {
		messages = new LinkedList<Message>();
		elevatorModel = new HashMap<Integer, Elevator>();
		
		messenger = Messenger.getMessenger();
		stateMachine = new SchedulerStateMachine();
	}
	
	/**
	 * Tries to schedule based on the requests. Will wait if there
	 * is no requests.
	 */
	public void run() {
		messenger.receive(PORT, this);
			
		while(true) {
			synchronized (messages) {
				while(stateMachine.getCurrentState() == SchedulerState.IDLE) {
					try {
						messages.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				stateMachine.onNext(SchedulerState.Transition.RECEIVED_MESSAGE);
				schedule();
				stateMachine.onNext(SchedulerState.Transition.FINISHED_SCHEDULING);
			}
			
			
		}
	}
	
	/**
	 * Action performed when a message has been received.
	 * 
	 * @param message the message that has been received
	 */
	@Override
	public void onMessageReceived(Message message) {
		Thread messageWriter = new Thread(new Runnable() {
			public void run() {
				synchronized (messages) {
					while(stateMachine.getCurrentState() == SchedulerState.BUSY) {
						try {
							messages.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					messages.add(message);
					
					messages.notifyAll();
				}
			}
		});
		
		messageWriter.start();
	}
	
	/**
	 * Schedule based on the messages.
	 */
	private void schedule() {
		// Handle scheduling
		Iterator<Message> msgIter = messages.iterator();
		while(msgIter.hasNext()) {
			Message msg = msgIter.next();
			
			if(msg.getType() == MessageType.ELEVATOR) {
				// The message might have three meaning
				ElevatorMessage em = new ElevatorMessage(msg);
				Elevator elevator = null;
				if(!elevatorModel.containsKey(em.getElevatorNum())) {
					elevator = new Elevator(em.getElevatorNum(), em.getState(), em.getCurrentFloor());
					elevatorModel.put(em.getElevatorNum(), elevator);
				} else {
					// Update the corresponding elevator state
					elevator = elevatorModel.get(em.getElevatorNum());
					elevator.setCurrentFloor(em.getCurrentFloor());
					elevator.setState(em.getState());
				}
				
				// Tell floor to open door
				if(elevator.getState() == ElevatorState.DOOROPEN) {
					try {
						messenger.send(em.toMessage(), ELEVATOR_PORT, InetAddress.getLocalHost());
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
				
			} else if(msg.getType() == MessageType.FLOOR) {
				FloorMessage fm = new FloorMessage(msg);
				
				// Retrieve the corresponding the elevator model
				Elevator elevator = elevatorModel.get(fm.getEleNum());
				
				if(fm.getDirection().equalsIgnoreCase("up")) {
					// Elevator is on the way up and is able to pick up this floor
					if(elevator.getState() == ElevatorState.MOVINGUP && elevator.getCurrentFloor() < fm.getFloorNum()) {
						try {
							messenger.send(fm.toMessage(), FLOOR_PORT, InetAddress.getLocalHost());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else {
						elevator.getUpQueue().add(fm);
					}
				} else {
					// Elevator is on the way down and is able to pick up this floor
					if(elevator.getState() == ElevatorState.MOVINGDOWN && elevator.getCurrentFloor() > fm.getFloorNum()) {
						try {
							messenger.send(fm.toMessage(), FLOOR_PORT, InetAddress.getLocalHost());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					} else {
						elevator.getDownQueue().add(fm);
					}
				}
			}
		}
	}
}