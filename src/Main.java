import elevator.Elevator;
import elevator.ElevatorSystem;
import floor.FloorSystem;
import scheduler.Scheduler;

public class Main {
	
	public static void main(String[] args) {
		int numElev = 2;
		Scheduler scheduler = new Scheduler();
		ElevatorSystem eleSys = new ElevatorSystem(scheduler);
		FloorSystem floorSys = new FloorSystem(scheduler, numElev);
	} 
}
