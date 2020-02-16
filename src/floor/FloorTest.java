/**
 * 
 */
package floor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.Message;
import common.MessageType;

/**
 * Class to run tests on all Floor related classes
 * 
 * @author Hoang Bui, Christophe Tan
 */
class FloorTest {

	/**
	 * Method to test the floor class 
	 */
	void floorSystemTest() {
		FloorSystem floorSystem = new FloorSystem(1);
		assertTrue("Check if there is exactly one floor", floorSystem.numOfFloors() == 1);
	}
	
	/**
	 * Method to test the floor class 
	 */
	void floorTest() {
		Floor floor = new Floor(new FloorSystem(1), 1, 1);
		assertTrue("Check if there is exactly one door", floor.numOfDoors() == 1);
		assertTrue("Check if the floor number is 1", floor.getFloorNum() == 1);
		assertTrue("Check that the floor has exactly 0 people", floor.getPeople() == 0);
		floor.setPeople(5);
		assertTrue("Check that the number people on the floor is 5", floor.getPeople() == 5);
	}
	
	void floorSystemMessageTest() {
		FloorSystem floorSystem = new FloorSystem(1);
		Floor floor = floorSystem.getFloors().get(0);
		assertNull("Floor should currently contain no message", floor.getMessage());
		floor.request(new Message(MessageType.FLOOR, "Message from floor"));
		assertNotNull("Floor should now contain a message to be sent", floor.getMessage());
		assertTrue("Floor system should currently have no outbound messages",floorSystem.getOutBoundRequests().size() == 0);
		floor.processMessage();
		assertNull("Floor should have sent message", floor.getMessage());
		assertTrue("Floor system should have recieved message from floor", floorSystem.getOutBoundRequests().size() == 6);

	}
	
	/**
	 * Method to test the floorDoor class
	 */
	void floorDoorTest() {
		FloorDoor floorDoor = new FloorDoor();
		assertTrue("Check if the door is open", floorDoor.openDoor());
		assertFalse("Check if the door is already open", floorDoor.openDoor());
		assertTrue("Check if the door has closed", floorDoor.closeDoor());
		assertFalse("Check if the door is already close", floorDoor.closeDoor());
	}

	/**
	 * Method that runs all floor test cases
	 */
	@Test
	void test() {
		floorSystemTest();
		floorTest();
		floorDoorTest();
		floorSystemMessageTest();
	}

}
