import elevator.Elevator;
import elevator.ElevatorSystem;
import scheduler.Scheduler;

public class Main {
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		ElevatorSystem eleSys = new ElevatorSystem(scheduler);
	} 
}
