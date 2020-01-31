package floor;

import java.io.*;
import java.util.*;

import common.Message;
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
	private Queue<Message> requests;
	private List<String> inputs;
	
	/**
	* Constructor for the floor system
	*
	* @param scheduler the scheduler
	* @param numElev the number of elevators
	* @param inputFile the input file
	*/
	public FloorSystem(Scheduler scheduler, int numElev, String inputFile) {
		this.scheduler = scheduler;
		this.inputs = readFile(inputFile);
		
		//Create 5 floors
		for(int i= 0; i < 5; i++) {
			addFloor(i + 1, numElev);		
		}
		
		startSystem();
	}
	
	/**
	 * Creates a new floor 
	 * 
	 * @param floorNum The floor number
	 * @param numElev The amount of elevators on the floor
	 */
	private void addFloor(int floorNum, int numElev) {
		this.floors.add(new Floor(this, scheduler, floorNum, numElev));
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
	public synchronized void addMessage(Message msg) {
		synchronized(requests) {
			this.requests.add(msg);
		}
	}
	
	/**
	 * Reads the file and adds each line to the list
	 *
	 * @param inputFile The name of the file to be read
	 * @return The list of inputs
	 */
	private List<String> readFile(String inputFile) {
		
		ArrayList<String> inputs = new ArrayList<String>();
		
		try {
			Scanner scanner = new Scanner(new File(inputFile));
			
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				inputs.add(line);
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return inputs;
	}


	/**
	* Method to run commands
	*/
	@Override
	public void run() {
		synchronized(requests) {
			while (this.requests.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	} 

}
