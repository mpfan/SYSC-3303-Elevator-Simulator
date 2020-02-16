package floor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import common.ElevatorMessage;
import common.ElevatorState;
import common.FloorMessage;
import common.Message;
import common.MessageListener;
import common.MessageType;
import common.Messenger;
import common.Ports;

/**
 * The FloorSystem class is responsible for handling communication between the Scheduler and Floor.
 * 
 * @author Christophe Tran, Hoang Bui
 *
 */
public class FloorSystem implements Runnable, MessageListener {
	
	// Variables
	private Messenger messenger; 
	private List<Floor> floors;
	private Queue<Message> inBoundRequests, outBoundRequests;
	
	/**
	* Constructor for the floor system
	 * @param numElev the number of elevators
	 * @param inputFile the input file
	*/
	public FloorSystem(int numElev) {
		//set-up variables
		messenger = Messenger.getMessenger();
		floors = new ArrayList<Floor>();
		outBoundRequests = new LinkedList<Message>();
		inBoundRequests = new LinkedList<Message>();
		
		//Add a floor
		addFloor(1, numElev);
		
		//Commented for future iterations
//		for(int i= 0; i < 5; i++) {
//			addFloor(i + 1, numElev);		
//		}
		
		startSystem();
	}
	
	/**
	 * Creates a new floor 
	 * 
	 * @param floorNum The floor number
	 * @param numElev The amount of elevators on the floor
	 */
	private void addFloor(int floorNum, int numElev) {
		this.floors.add(new Floor(this, floorNum, numElev));
	}
	
	/**
	 * Initializes and executes the floor threads
	 */
	public void startSystem() {
		for (Floor floor : this.floors) {
			Thread floorThread = new Thread(floor);
			floorThread.start();
		}
	}
	
	/**
	 * Adds the message to an outbound list
	 *
	 * @param msg the message to be added to the list of request
	 */
	public synchronized void addOutBoundMessage(Message msg) {
		synchronized(outBoundRequests) {
			this.outBoundRequests.add(msg);
			this.outBoundRequests.notifyAll();
		}
	}
	

	public static void main(String[] args) {
		
		FloorSystem floorSys = new FloorSystem(1);
		Thread floorSystemThread = new Thread(floorSys, "Floor System");
		floorSystemThread.start();
	}
	
	@Override
	public void run() {
		
		// spawn another thread for sending out-bound messages to scheduler
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
						
						Message toSend;
						ElevatorState state = ElevatorState.ILLEGAL;
						ElevatorMessage eleMessage = null;
						
						
						try {
							eleMessage = new ElevatorMessage(msg);
							state = eleMessage.getState();
							
							toSend = eleMessage.toMessage();

						} catch (IllegalArgumentException e) {
							
							FloorMessage floorMessage = new FloorMessage(msg);
							toSend = floorMessage.toMessage();
						}

						System.out.println("Floor System: Sending outbound messages to scheduler");
						try {
							switch(state) {
							case DOOROPEN:
								
								Calendar cal = Calendar.getInstance();
								int hh = cal.get(Calendar.HOUR_OF_DAY);
								int mm = cal.get(Calendar.MINUTE);
								int ss = cal.get(Calendar.SECOND);
								int ms = cal.get(Calendar.MILLISECOND);

								FloorMessage floorMessage = new FloorMessage(hh + ":" + mm + ":" + ss + ":" + ms, "FINISHED_LOAD", eleMessage.getCurrentFloor(), eleMessage.getElevatorNum());
								
								System.out.println("FloorSystem: about to send: " + floorMessage.toMessage().getBody());
								
								messenger.send(floorMessage.toMessage(), Ports.SCHEDULER_PORT, InetAddress.getLocalHost());
								break;
							case DOORCLOSE:
								break;
							default:
								System.out.println("FloorSystem: about to send: " + toSend.getBody());

								messenger.send(toSend, Ports.SCHEDULER_PORT, InetAddress.getLocalHost());
							}
							
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
		
		messenger.receive(Ports.FLOOR_PORT, this);
		
	}
	
	/**
	 * Method to obtain the number of floors
	 * 
	 * @return the number of floors
	 */
	public int numOfFloors() {
		return floors.size();
	}
	
	/**
	 * Returns the list of floors
	 * 
	 * @return All the floor objects
	 */
	public List<Floor> getFloors() {
		return floors;
	}
	
	/**
	 * Returns outBoundRequests, which contains the list of messages to be sent to Scheduler
	 * 
	 * @return list of messages to be sent to scheduler
	 */
	public Queue<Message> getOutBoundRequests() {
		return outBoundRequests;
	}
	
	public Queue<Message> getInBoundRequests(){
		return inBoundRequests;
	}

	@Override
	public void onMessageReceived(Message message) {
		// Thread for receiving in-bound messages from scheduler
		Thread inBoundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				
					
//					while (!inBoundRequests.isEmpty()) {
//						System.out.println("Floor System: Sending messages to floor");
//						//Currently a hard coded value, but will be updated in future iterations
//						floors.get(0).request(message);
//					}
					
//					System.out.println("Floor System: Requesting messages from scheduler");
					
//					inBoundRequests.add(message);
					floors.get(0).request(message);
					System.out.println("Floor System: Received: " + message.getBody());
				}	
			
		});
		inBoundThread.start();
	}
}
