/**
 * 
 */
package elevator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.Message;
import common.MessageType;
import scheduler.Scheduler;

/**
 * Class to run tests on all elevator related classes
 * 
 * @author Hoang Bui
 */
class ElevatorTest {

	/**
	 * Method to run tests on the elevator system
	 */
	void elevatorSystemTest() {
		ElevatorSystem elevatorSystem = new ElevatorSystem(new Scheduler());
		assertTrue("Check if there is exactly one elevator in the system", elevatorSystem.getNumElevators() == 1);
	}
	
	/**
	 * Method to run tests on the elevator class
	 */
	void elevatorTest() {
		Elevator elevator = new Elevator(1, 1, new ElevatorSystem(new Scheduler()),1);
		assertTrue("Check that the elevator capacity is 19", elevator.getCapacity() == 19);
		assertTrue("Check that there are 0 people in the elevator", elevator.getPeople() == 0);
		assertFalse("Check that the elevator cannot fit 20 people", elevator.setPeople(20));
		elevator.setCapacity(20);
		assertTrue("Check that the elevator is able to fit 20 people", elevator.setPeople(20));
		assertTrue("Check that the elevator capacity is now 20", elevator.getCapacity() == 20);
		assertTrue("Check that there are 20 people in the elevator", elevator.getPeople() == 20);
		assertTrue("Check that the elevator number is 1", elevator.getElevatorNumber() == 1);
		elevator.setElevatorNumber(2);
		assertTrue("Check that the elevator number is now 2", elevator.getElevatorNumber() == 2);
		
	}
	
	/**
	 * Test the Elevator and ElevatorSystem interaction with Messages
	 */
	void elevatorMessageSystemInteraction() {
	
		ElevatorSystem eleSys = new ElevatorSystem(new Scheduler());
		Elevator elevator = new Elevator(20, 2, eleSys, 1);
		
		elevator.request(new Message(MessageType.ELEVATOR, "Elevator Message"));
		assertNotNull("Message should be added to elevator when requested", elevator.getMessage());
		elevator.processMessage();
		assertNull("Should remove message instance from elevator when processed", elevator.getMessage());
		assertTrue("Processed elevator message ready to be sent to scheduler", eleSys.getOutBoundMessagesStored() == 1);
	}
	
	/**
	 * Method to run all elevator related tests
	 */
	@Test
	void testAll() {
		elevatorTest();
		elevatorSystemTest();
		elevatorMessageSystemInteraction();
	}
}
