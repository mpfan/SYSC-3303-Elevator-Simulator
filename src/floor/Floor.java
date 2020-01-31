package floor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import common.*;
import scheduler.Scheduler;

/**
 * Class to represesent a floor
 *
 * @author Christophe Tran, Hoang Bui
 */
public class Floor implements Runnable {
	
	// Variables
	Hashtable<Integer, FloorDoor> doors;
	int people;
	int floorNum;
	FloorSystem floorSystem;
	boolean isDownButtonPressed;
	boolean isUpButtonPressed;
	boolean isUpLamp; 
	boolean isDownLamp;
	List<Message> responses;
	private Message msg;
	
		
	/**
	 * Constructor for Floor class
	 *
	 * @param floorSys The floor system that manages all floors
	 * @param scheduler The scheduler that requests are sent to
	 * @param floorNum The current floor number
	 * @param numElev The number of elevators
	 */
	public Floor(FloorSystem floorSys, int floorNum, int numElev) {
		this.floorSystem = floorSys;
		this.floorNum = floorNum;
		this.people = 0;
		this.isDownButtonPressed = false;
		this.isUpButtonPressed = false;
		this.isUpLamp = false;
		this.isDownLamp = false;
		
		//create the doors 
		doors = new Hashtable<Integer, FloorDoor>();
		for(int i = 1; i < numElev; i++) {
			doors.put(i, new FloorDoor());
		}
	}

	/**
	* Method to run commands
	*/
	@Override
	public void run() {
		
		String inputFile = "./src/floor/input.txt";
		List<String> inputs = readFile(inputFile);
		
		String message = "";
		for(String line : inputs) {
			message = message + line + "\n";
		}
		
		this.msg = new Message(MessageType.FLOOR,message);
		
		while(true) {
			processMessage();
		}
	}
		
	
	/**
	 *  Process the message to floor system
	 */
	public synchronized void processMessage() {
		while (msg == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Floor: Processing message on floor...");
		System.out.println("Floor: " + msg.getBody());
		this.floorSystem.addOutBoundMessage(msg);
		this.msg = null;
		notifyAll();
	}
	
	/**
	 * Method for floor to do work specified by message
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
		System.out.println("Floor: setting message  to floor with message: " + msg.getBody());
		this.msg = msg;
		notifyAll();
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
	
}
