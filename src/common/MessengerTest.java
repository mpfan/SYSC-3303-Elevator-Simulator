package common;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

/**
 * Test class for Messenger
 * 
 * @author Michael Fan, Hoang Bui
 */
class MessengerTest {
	
	/**
	 * Method that tests the Messenger.
	 */
	@Test
	void test() {
		Messenger messgener = Messenger.getMessenger();
		
		// Test message
		int port = 3000;
		MessageType messageType = MessageType.ELEVATOR;
		String messageBody = "Message from elevator";
		
		Message testMessage = new Message(messageType, messageBody);
		
		boolean resSocCreated = messgener.receive(port, new MessageListener() {
			public void onMessageReceived(Message message) {
				assertEquals(messageType, message.getType());
				assertEquals(messageBody, message.getBody());
			}
		});
		
		assertTrue(resSocCreated);
		
		// Sends a message to itself for testing purposes 
		boolean messageSent = false;
		try {
			messageSent = messgener.send(testMessage, port, InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(messageSent);
	}

}
