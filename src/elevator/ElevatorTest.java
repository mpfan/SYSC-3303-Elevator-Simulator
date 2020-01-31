/**
 * 
 */
package elevator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		Elevator elevator = new Elevator(1, 1, new ElevatorSystem(new Scheduler()));
		assertTrue("Check that the elevator capacity is 19", elevator.getCapacity() == 19);
		assertTrue("Check that the elevator capacity is now 20", elevator.getCapacity() == 20);
		assertTrue("Check that there are 0 people in the elevator", elevator.getPeople() == 0);
		assertFalse("Check that the elevator cannot fit 20 people", elevator.setPeople(20));
		elevator.setCapacity(20);
		assertTrue("Check that the elevator is able to fit 20 people", elevator.setPeople(20));
		assertTrue("Check that there are 20 people in the elevator", elevator.getPeople() == 20);
		assertTrue("Check that the elevator number is 1", elevator.getElevatorNumber() == 1);
		elevator.setElevatorNumber(2);
		assertTrue("Check that the elevator number is now 2", elevator.getElevatorNumber() == 2);
		
	}
	
	/**
	 * Method to run all elevator related tests
	 */
	@Test
	void test() {
		elevatorTest();
		elevatorSystemTest();
	}

}
