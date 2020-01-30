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
		this.ele1 = new Elevator(10, 0, this, scheduler);
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
		Thread eleThread = new Thread(this.ele1);
		eleThread.start();
	}

	@Override
	public void run() {
		while (true) {

			this.scheduler
					.request(new Message(MessageType.ELEVATOR, "Hello, this is an elevator system requesting work!"));
			while (!inBoundRequests.isEmpty()) { 				// while not empty, wait for work
				try {
					ele1.request(inBoundRequests.poll());
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (outBoundRequests) {
				this.scheduler.request(outBoundRequests.poll());
			}
		}
	}

	public void addOutboundMessage(Message msg) {
		synchronized (outBoundRequests) {
			this.scheduler.request(msg);
		}
	}

}
