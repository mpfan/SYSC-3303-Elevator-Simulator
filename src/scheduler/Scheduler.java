package scheduler;
import java.util.LinkedList;
import java.util.Queue;

import common.Message;
import common.MessageListener;
import common.MessageType;
import common.Messenger;
import common.SchedulerState;

/**
 * This class represents a Scheduler. Scheduler will try to schedule 
 * if there are pending messages.
 * 
 * @author Michael Fan
 *
 */
public class Scheduler implements Runnable, MessageListener {
	public static int PORT = 8000;
	
	private SchedulerStateMachine stateMachine;
	private Messenger messenger;
	private Queue<Message> messages;
	
	/**
	 * Constructor for scheduler
	 */
	public Scheduler() {
		messages = new LinkedList<Message>();
		
		messenger = Messenger.getMessenger();
		stateMachine = new SchedulerStateMachine();
	}
	
	/**
	 * Tries to schedule based on the requests. Will wait if there
	 * is no requests.
	 */
	public void run() {
		messenger.receive(PORT, this);
			
		while(true) {
			synchronized (messages) {
				while(stateMachine.getCurrentState() == SchedulerState.IDLE) {
					try {
						messages.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				stateMachine.onNext(SchedulerState.Transition.RECEIVED_MESSAGE);
				schedule();
				stateMachine.onNext(SchedulerState.Transition.FINISHED_SCHEDULING);
			}
			
			
		}
	}
	
	/**
	 * Action performed when a message has been received.
	 * 
	 * @param message the message that has been received
	 */
	@Override
	public void onMessageReceived(Message message) {
		Thread messageWriter = new Thread(new Runnable() {
			public void run() {
				synchronized (messages) {
					while(stateMachine.getCurrentState() == SchedulerState.BUSY) {
						try {
							messages.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					messages.add(message);
					
					messages.notifyAll();
				}
			}
		});
		
		messageWriter.start();
	}
	
	/**
	 * Schedule based on the messages. For now it will simply relay messages
	 * between Elevator System and Floor System without any scheduling.
	 */
	private void schedule() {
		// Handle scheduling
	}
}