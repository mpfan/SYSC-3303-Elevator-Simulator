package elevator;

import java.util.LinkedList;
import java.util.Queue;

import common.Message;
import common.MessageType;
import scheduler.Scheduler;

/**
 * System for managing elevators and receive messages from scheduler
 * 
 * @author Derek Shao, Souheil Yazji
 *
 */
public class ElevatorSystem implements Runnable {

	//Variables
	private Scheduler scheduler;
	private Elevator ele1;
	private Queue<Message> inBoundRequests, outBoundRequests;

	/**
	 * Constructor for the elevator system
	 * 
	 * @param scheduler the scheduler
	 */
	public ElevatorSystem(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.inBoundRequests = new LinkedList<Message>();
		this.outBoundRequests = new LinkedList<Message>();
		this.ele1 = new Elevator(10, 0, this);
	}

	/**
	 * Start the elevators
	 */
	public void startElevators() {
		Thread eleThread = new Thread(this.ele1, "Elevator");
		eleThread.start();
	}

	/**
	 * Method to run
	 */
	@Override
	public void run() {

		startElevators();
		
		// spawn another thread for sending out-bound messages
		Thread outBoundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					synchronized (outBoundRequests) {
						while (outBoundRequests.isEmpty()) {
							try {
								outBoundRequests.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						System.out.println("Elevator System: Sending outbound messages to scheduler");
						scheduler.request(outBoundRequests.poll());
						outBoundRequests.notifyAll();
					}
				}
			}
		});
		outBoundThread.start();
		
		Thread inBoundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					while (!inBoundRequests.isEmpty()) {
						System.out.println("Elevator System: Sending messages to free elevator");
						ele1.request(inBoundRequests.poll());
					}

					
					System.out.println("Elevator System: Requesting messages from scheduler");
					Queue<Message> elevatorMessages = scheduler.response(MessageType.ELEVATOR);
					
					if (elevatorMessages != null) {
						inBoundRequests.addAll(elevatorMessages);
						System.out.println("Elevator System: Received " + Integer.toString(elevatorMessages.size()) + " messages");
					}
				}	
			}
		});
		inBoundThread.start();
	}

	/**
	 * Add a message that will need to be sent to the Scheduler
	 * 
	 * @param msg Message to add to be sent to the scheduler
	 */
	public void addOutboundMessage(Message msg) {

		synchronized (outBoundRequests) {
			System.out.println("Elevator System: adding outbound message to elevator system");
			this.outBoundRequests.add(msg);
			this.outBoundRequests.notifyAll();
		}
	}
	
	/**
	 * Size of elevator system in-bound messages queue
	 * 
	 * @return in-bound messages queue size
	 */
	public int getInBoundMessagesStored() {
		
		return this.inBoundRequests.size();
	}
	
	/**
	 * Size of elevator system out-bound messages queue
	 * 
	 * @return out-bound messages queue size
	 */
	public int getOutBoundMessagesStored() {
		
		return this.outBoundRequests.size();
	}
	
	/**
	 * Method to obtain the number of elevators in the system
	 * 
	 * @return number of elevators
	 */
	public int getNumElevators() {
		//Currently just one elevator, will be updated for future iterations
		return 1;
	}
}
