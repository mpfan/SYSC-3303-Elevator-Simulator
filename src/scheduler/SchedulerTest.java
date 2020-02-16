package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.Message;
import common.MessageListener;
import common.MessageType;
import common.Messenger;

class SchedulerTest {
	
	/**
	 * Method that tests the scheduler.
	 */
	@Test
	void schedulerTest() {
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(scheduler);
		Messenger messenger = Messenger.getMessenger();
		
		// Mock messages
		Message elevatorMessage = new Message(MessageType.ELEVATOR, "20:58:58:360,2,DOOROPEN,2,0");
		Message floorMessage = new Message(MessageType.FLOOR, "10:05:15.0,2,Up,0");
		
		schedulerThread.start();
		
		// A mock elevator system
		messenger.receive(8001, new MessageListener() {
			public void onMessageReceived(Message message) {
				// The scheduler should pass floor message to elevator
				assertEquals(floorMessage.getBody(), message.getBody());
			}
		});
		// A mock floor system
		messenger.receive(8002, new MessageListener() {
			public void onMessageReceived(Message message) {
				// The scheduler should pass elevator message to floor
				assertEquals(elevatorMessage.getBody(), message.getBody());
			}
		});
		
		
		scheduler.onMessageReceived(floorMessage);
		scheduler.onMessageReceived(elevatorMessage);
		
	}

}
