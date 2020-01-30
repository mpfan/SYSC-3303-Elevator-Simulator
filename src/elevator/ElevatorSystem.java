package elevator;

import java.util.LinkedList;
import java.util.Queue;

import common.Message;
import common.MessageType;
import scheduler.Scheduler;

public class ElevatorSystem implements Runnable {

	private Scheduler scheduler;
//	private List<Elevator> elevators;
	private Elevator ele1;
	private Queue<Message> inBoundRequests, outBoundRequests;

	public ElevatorSystem(Scheduler scheduler) {
		this.scheduler = scheduler;
//		this.elevators = new ArrayList<Elevator>();
		this.inBoundRequests = new LinkedList<Message>();
		this.outBoundRequests = new LinkedList<Message>();
//		this.addElevator();
		this.ele1 = new Elevator(10, 0, this);
		startSystem();
	}

	public void addElevator() {
//		this.elevators.add(new Elevator(10, 0, this, scheduler));
//		this.elevators.add(new Elevator(10, 1, this, scheduler));
	}

	public void startSystem() {
//		for (Elevator ele : this.elevators) {
//			Thread eleThread = new Thread(ele);
//			eleThread.start();
//		}
		Thread eleThread = new Thread(this.ele1, "Elevator");
		eleThread.start();
	}

	@Override
	public void run() {
		while (true) {
			
			while (!inBoundRequests.isEmpty() || !outBoundRequests.isEmpty()) {
				while (!inBoundRequests.isEmpty()) {
					System.out.println(Thread.currentThread() + " Sending messages to free elevator");
					ele1.request(inBoundRequests.poll());
				}
				
				synchronized (outBoundRequests) {
					while (!this.outBoundRequests.isEmpty()) {
						System.out.println(Thread.currentThread() + " sending outbound messages to scheduler");
						this.scheduler.request(outBoundRequests.poll());
					}
				}
			}
			
			System.out.println(Thread.currentThread() + " Ele sys requesting messages from scheduler");
			Queue<Message> elevatorMessages = this.scheduler.response(MessageType.ELEVATOR);
			
			if (elevatorMessages != null) {
				inBoundRequests.addAll(elevatorMessages);
				System.out.println(Thread.currentThread() + "Received " + Integer.toString(elevatorMessages.size()) + " messages");
			}
		}
	}

	public void addOutboundMessage(Message msg) {
		System.out.println(Thread.currentThread() + " trying to add outbound message to elevator system");

		synchronized (outBoundRequests) {
			System.out.println(Thread.currentThread() + " adding outbound message to elevator system");
			this.outBoundRequests.add(msg);
		}
	}
}
