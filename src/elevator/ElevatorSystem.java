package elevator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import common.FloorMessage;
import common.Message;
import common.MessageListener;
import common.Messenger;
import common.Ports;

/**
 * System for managing elevators and receive messages from scheduler
 * 
 * @author Derek Shao, Souheil Yazji
 *
 */
public class ElevatorSystem implements Runnable, MessageListener {

	//Variables
	private Messenger messenger;
	private Queue<Message> inBoundRequests, outBoundRequests;
	private ArrayList<Elevator> elevators;

	/**
	 * Constructor for the elevator system
	 * 
	 * @param scheduler the scheduler
	 */
	public ElevatorSystem(int numberOfElevators) {
		this.messenger = Messenger.getMessenger();
		this.inBoundRequests = new LinkedList<Message>();
		this.outBoundRequests = new LinkedList<Message>();
		this.elevators = new ArrayList<Elevator>();
		startElevators(numberOfElevators);
	}

	/**
	 * Start the elevators
	 */
	public void startElevators(int numberOfElevators) {
		
		for (int i = 0; i < numberOfElevators; i++) {
			// Create elevator with current index as elevator number
			Elevator ele = new Elevator(10, i, this, 1);
			elevators.add(ele);
			
			Thread eleThread = new Thread(ele, "Elevator");
			eleThread.start();
		}
	}
	
	public static void main(String[] args) {
		
		ElevatorSystem eleSys = new ElevatorSystem(3);
		Thread elevatorSystemThread = new Thread(eleSys, "Elevator System");
		elevatorSystemThread.start();
	}

	/**
	 * Method to run
	 */
	@Override
	public void run() {

		messenger.receive(Ports.ELEVATOR_PORT, this);
		
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
						
						Message msg = outBoundRequests.poll();
						System.out.println("Elevator System: Sending outbound messages to scheduler");
						System.out.println("Elevator System: outbound message: " + msg.getBody());
						try {
							messenger.send(msg, Ports.SCHEDULER_PORT, InetAddress.getLocalHost());
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						outBoundRequests.notifyAll();
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		outBoundThread.start();
		
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

	@Override
	public void onMessageReceived(Message message) {
		Thread inBoundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				
				FloorMessage floorMsg = new FloorMessage(message);
				
				int eleNumber = floorMsg.getEleNum();
				
				elevators.get(eleNumber).request(message);
				System.out.println("Elevator System: Received: " + message.getBody());
			}
		});
		inBoundThread.start();	
	}
}
