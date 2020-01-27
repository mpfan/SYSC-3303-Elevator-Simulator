package scheduler;
import common.Message;
import common.Respondable;

/**
 * The request object will process each individual request.
 * 
 * @author Michael Fan
 *
 */
public class Request implements Runnable {
	private Message message;
	private Respondable response;
	
	public Request(Message message, Respondable response) {
		this.message = message;
		this.response = response;
	}
	
	public void run() {
		// Processing... takes time
		System.out.println("Scheduler: Processing message...");
		
		// Echo back the same message for now
		response.response(message);
		
		System.out.println("Scheduler: Responeded to " + message.getType());
	}
	
}
