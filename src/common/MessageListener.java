package common;

/**
 * Method used to receive a message from the Messenger.
 * 
 * @author Michael Fan 101029934
 *
 */
public interface MessageListener {
	/**
	 * Called by the Messenger when a message has been received.
	 * This method is not thread-save and requires external 
	 * synchronization.
	 * @param message
	 */
	public void onMessageReceived(Message message);
}
