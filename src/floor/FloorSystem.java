package floor;

import java.io.*;
import java.util.*;

import common.Message;
import common.MessageType;
import scheduler.Scheduler;

/**
 * The FloorSystem class is responsible for handling communication between the Scheduler and Floor.
 * 
 * @author Christophe Tran, Hoang Bui
 *
 */
public class FloorSystem implements Runnable {
	
	// Variables
	private Scheduler scheduler; 
	private List<Floor> floors;
	private Queue<Message> inBoundRequests, outBoundRequests;
	
	private Floor floor1;
	
	/**
	* Constructor for the floor system
	*
	* @param scheduler the scheduler
	* @param numElev the number of elevators
	* @param inputFile the input file
	*/
	public FloorSystem(Scheduler scheduler, int numElev) {
		this.scheduler = scheduler;
		
		floor1 = new Floor(this, 1, numElev);

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
	 * Adds the message to the 
	 *
	 * @param msg the message to be added to the list of request
	 */
	public synchronized void addOutBoundMessage(Message msg) {
		synchronized(outBoundRequests) {
			this.outBoundRequests.add(msg);
		}
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
						
						System.out.println("Floor System: Sending outbound messages to scheduler");
						scheduler.request(outBoundRequests.poll());
						outBoundRequests.notifyAll();
					}
				}
			}
		});
		
		outBoundThread.start();
		
		// Thread for receiving in-bound messages from scheduler
		Thread inBoundThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					while (!inBoundRequests.isEmpty()) {
						while (!inBoundRequests.isEmpty()) {
							System.out.println("Floor System: Sending messages to floor");
							floor1.request(inBoundRequests.poll());
						}
					}
					
					System.out.println("Floor System: Requesting messages from scheduler");
					Queue<Message> floorMessages = scheduler.response(MessageType.FLOOR);
					
					if (floorMessages != null) {
						inBoundRequests.addAll(floorMessages);
						System.out.println("Floor System: Received " + Integer.toString(floorMessages.size()) + " messages");
					}
				}	
			}
		});
		inBoundThread.start();
	}

}
