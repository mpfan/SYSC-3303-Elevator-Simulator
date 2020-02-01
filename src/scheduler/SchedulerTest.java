package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.Message;
import common.MessageType;

class SchedulerTest {
	
	/**
	 * Method that tests the scheduler.
	 */
	@Test
	void schedulerTest() {
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler);
		
		schedulerThread.start();
		
		Message floorMessage = new Message(MessageType.FLOOR, "Floor Message");
		Message elevatorMessage = new Message(MessageType.ELEVATOR, "Elevator Message");
		
		scheduler.request(floorMessage);
		scheduler.request(elevatorMessage);
		
		// The scheduler should pass floor message to elevator 
		assertEquals("Floor Message", scheduler.response(MessageType.ELEVATOR).remove().getBody());
		
		// The scheduler should pass elevator message to floor
		assertEquals("Elevator Message", scheduler.response(MessageType.FLOOR).remove().getBody());
	}

}
