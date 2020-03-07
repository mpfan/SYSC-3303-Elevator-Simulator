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

import common.ElevatorState;
import common.ElevatorState.Transition;
import common.Message;
import common.MessageType;

/**
 * Class to run tests on all elevator related classes
 * 
 * @author Hoang Bui, Christophe Tran
 */
class ElevatorTest {

	/**
	 * Method to run tests on the elevator system
	 */
	void elevatorSystemTest() {
		ElevatorSystem elevatorSystem = new ElevatorSystem(3);
		assertTrue("Check if there is exactly one elevator in the system", elevatorSystem.getNumElevators() == 1);
	}
	
	/**
	 * Method to run tests on the elevator class
	 */
	void elevatorTest() {
		Elevator elevator = new Elevator(1, 1, new ElevatorSystem(3),1);
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
	
		ElevatorSystem eleSys = new ElevatorSystem(3);
		Elevator elevator = new Elevator(20, 2, eleSys, 1);
		
		elevator.request(new Message(MessageType.ELEVATOR, "10:22:11.0,1,Down,0"));
		assertNotNull("Message should be added to elevator when requested", elevator.getMessage());
		elevator.processMessage();
		assertNull("Should remove message instance from elevator when processed", elevator.getMessage());
		assertTrue("Processed elevator message ready to be sent to scheduler", eleSys.getOutBoundMessagesStored() == 1);
	}
	
	/**
	 * Test the Elevator state transition
	 */
	void elevatorStateTransitionTest() {
		ElevatorStateMachine state = new ElevatorStateMachine();
		assertTrue("Processed elevator message ready to be sent to scheduler", ElevatorState.IDLE == state.getCurrentState());
		state.onNext(Transition.RECEIVEDMESSAGE_DOWN);
		assertTrue("Elevator state should change to MOVINGDOWN", ElevatorState.MOVINGDOWN == state.getCurrentState());
		state.onNext(Transition.REACHEDDESTINATION);
		assertTrue("Elevator state should change to DOOROPEN", ElevatorState.DOOROPEN == state.getCurrentState());
		state.onNext(Transition.LOAD);
		assertTrue("Elevator state should change to DOORCLOSE", ElevatorState.DOORCLOSE == state.getCurrentState());
		
	}
	
	/**
	 * Method to run all elevator related tests
	 */
	@Test
	void testAll() {
		elevatorTest();
		elevatorSystemTest();
		elevatorMessageSystemInteraction();
		elevatorStateTransitionTest();
	}
}
