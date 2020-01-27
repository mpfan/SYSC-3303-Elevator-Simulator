package scheduler;
import common.Message;
import common.Requestable;
import common.Respondable;

/**
 * Scheduler will create a thread for each thread
 * and back to listening for more threads.
 * 
 * @author Michael Fan
 *
 */
public class Scheduler implements Requestable {
	public void request(Message msg, Respondable response) {
		System.out.println("Scheduler: Message received from " + msg.getType());
		System.out.println("Scheduler: Message is: " + msg.getBody());
		
		Thread request = new Thread(new Request(msg, response));
		
		request.start();
	}
}
