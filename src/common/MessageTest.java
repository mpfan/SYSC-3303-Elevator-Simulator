package common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class to perform a junit test on Class Message
 * 
 * @author Michael Fan, Hoang Bui
 */
class MessageTest {

	@Test
	void test() {
		Message msg = new Message(MessageType.FLOOR, "Test, test test");
		assertTrue("Check that the message type is floor", msg.getType().equals(MessageType.FLOOR));
		assertFalse("Check that the message type is not floor", msg.getType().equals(MessageType.ELEVATOR));
		assertFalse("Check that the message is already set to type floor", msg.setType(MessageType.FLOOR));
		assertTrue("Check that the message is able to change message type elevator", msg.setType(MessageType.ELEVATOR));
		assertFalse("Check that the message type is not floor", msg.getType().equals(MessageType.FLOOR));
		assertTrue("Check that the message type is elevator", msg.getType().equals(MessageType.ELEVATOR));
		assertTrue("Check if the message contains thwe proper body message", msg.getBody()== "Test, test test");
	}

}
