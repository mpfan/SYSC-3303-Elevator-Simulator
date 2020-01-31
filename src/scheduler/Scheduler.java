package scheduler;
import java.util.LinkedList;
import java.util.Queue;

import common.Message;
import common.MessageType;

/**
 * This class represents a Scheduler. Scheduler will try to schedule 
 * if there are pending messages.
 * 
 * @author Michael Fan
 *
 */
public class Scheduler implements Runnable {
	
	//Variables
	private Queue<Message> messages;
	private Queue<Message> elevatorMessages;
	private Queue<Message> floorMessages;
	
	/**
	 * Constructor for scheduler
	 */
	public Scheduler() {
		messages = new LinkedList<Message>();
		elevatorMessages = new LinkedList<Message>();
		floorMessages = new LinkedList<Message>();
	}
	
	/**
	 * Write a message into the message queue. The client will call
	 * this method to write a message into the queue.
	 * 
	 * @param message the client's message
	 */
	public void request(Message message) {
		System.out.println("Scheduler: Message received from " + message.getType());
		System.out.println("Scheduler: Message is: " + message.getBody());
		
		synchronized (messages) {
			messages.add(message);
			
			messages.notifyAll();
		}
	}
	
	/**
	 * Return messages of the specified type.
	 * 
	 * @param messageType the type of the message to return
	 * @return a list of messages
	 */
	public Queue<Message> response(MessageType messageType) {
		Queue<Message> messages = null;
		
		if(messageType == MessageType.ELEVATOR) {
			synchronized (elevatorMessages) {
				while(elevatorMessages.isEmpty()) {
					try {
						elevatorMessages.wait();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				messages = new LinkedList<Message>(elevatorMessages);
				elevatorMessages.clear();
				
				elevatorMessages.notifyAll();
			}
		} else if(messageType == MessageType.FLOOR) {
			synchronized (floorMessages) {
				while(floorMessages.isEmpty()) {
					try {
						floorMessages.wait();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				messages = new LinkedList<Message>(floorMessages);
				floorMessages.clear();
				
				floorMessages.notifyAll();
			}
		}
		
		return messages;
	}
	
	/**
	 * Tries to schedule based on the requests. Will wait if there
	 * is no requests.
	 */
	public void run() {
		while(true) {
			synchronized (messages) {
				while(messages.isEmpty()) {
					try {
						messages.wait();
					} catch (Exception e) {
						
					}
				}
				
				schedule();
				
				elevatorMessages.notifyAll();
				floorMessages.notifyAll();
			}
		}
	}
	
	/**
	 * Schedule based on the messages. For now it will simply relay messages
	 * between Elevator System and Floor System without any scheduling.
	 */
	private void schedule() {
		synchronized (messages) {
			for(int i = 0; i < messages.size(); i++) {
				Message m = messages.remove();
				switch (m.getType()) {
					case ELEVATOR: 
						synchronized (floorMessages) {
							floorMessages.add(m);
						}
						break;
					case FLOOR:
						synchronized (elevatorMessages) {
							elevatorMessages.add(m);
						}
					default:
					// Should never reach here
				}
			}
		}
	}
}