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
	
	//Variables
	private Hashtable<Integer, FloorDoor> doors;
	private int people;
	private int floorNum;
	private FloorSystem floorSystem;
	private boolean isDownButtonPressed;
	private boolean isUpButtonPressed;
	private boolean isUpLamp; 
	private boolean isDownLamp;
	private List<Message> responses;
	private Message msg;
	private ArrayList<String> messages;
	
		
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
		this.messages = new ArrayList<String>();
		
		//create the doors 
		doors = new Hashtable<Integer, FloorDoor>();
		for(int i = 1; i < numElev + 1; i++) {
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
		List<Long> timeStamps = new ArrayList<Long>();
		
		for(String line : inputs) {
			if(line.trim().isEmpty()) {
				continue;
			}
			messages.add(line + '\n');
			timeStamps.add(System.currentTimeMillis() + (long) 100 * messages.size());
		}
		
		msg = new Message(MessageType.FLOOR,messages.get(0));
		messages.remove(0);
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
		
		//Check if message type is floor
		if(msg.getType().equals(MessageType.FLOOR)) {
			System.out.println("Floor: Processing message on floor...");
			System.out.println("Floor: " + msg.getBody());
			this.floorSystem.addOutBoundMessage(msg);
			this.msg = null;
		}
		else { //Message type is elevator
			System.out.println("Floor: Processing message received from elevator...");
			System.out.println("Floor: " + msg.getBody());
			if(messages.size() > 0){ //Check if there are still more inputs
				this.msg = new Message(MessageType.FLOOR,messages.get(0));
				messages.remove(0);
			}
			else {
				this.msg = null;
			}
		}
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

	/**
	 * Method to obtain the number of doors
	 * 
	 * @return the number of doors
	 */
	public int numOfDoors() {
		return doors.size();
	}

	/**
	 * Method to obtain the number of people on the floor
	 * 
	 * @return the number of people
	 */
	public int getPeople() {
		return people;
	}

	/**
	 * Method to set the number of people on the floor
	 * 
	 * @param people the number people to set
	 */
	public void setPeople(int people) {
		this.people = people;
	}

	/**
	 * Method to obtain the floor number
	 * 
	 * @return floorNum the floor number
	 */
	public int getFloorNum() {
		return floorNum;
	}
	
}
