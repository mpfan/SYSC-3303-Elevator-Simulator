import elevator.ElevatorSystem;
import floor.FloorSystem;
import scheduler.Scheduler;

/**
 * Class for main to run the Elevator Control system and simulator
 * 
 * @author Hoang Bui
 */
public class Main {
	
	/**
	 * Method to run the system and simulator
	 * 
	 * @param args filler parameter
	 */
	public static void main(String[] args) {
		int numElev = 2;
		//Create threads
		Thread schedulerThread, eleSysThread, floorSysThread;
		
		Scheduler scheduler = new Scheduler();
		
		//Set up threads
		schedulerThread = new Thread(scheduler, "scheduler");
		eleSysThread = new Thread(new ElevatorSystem(3), "eleSys");
		floorSysThread = new Thread(new FloorSystem(numElev), "floorSys");
		
		//Start the threads
		schedulerThread.start();
		eleSysThread.start();
		floorSysThread.start();
	} 
}
