package elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.naming.spi.Resolver;

import common.Message;
import common.Requestable;
import common.Respondable;
import scheduler.Scheduler;

public class ElevatorSystem implements Runnable, Respondable, Requestable {
	
	private Requestable scheduler; 
	private List<Elevator> elevators;
	private Queue<Message> inBoundRequests, outBoundRequests;
	
	public ElevatorSystem(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.elevators = new ArrayList<Elevator>();
		this.inBoundRequests = new LinkedList<Message>();
		this.outBoundRequests = new LinkedList<Message>();
		this.addElevator();
		startSystem();
	}
	
	public void addElevator() {
		this.elevators.add(new Elevator(10, 0, this, scheduler));
//		this.elevators.add(new Elevator(10, 1, this, scheduler));
	}
	
	public void startSystem() {
		for (Elevator ele : this.elevators) {
			Thread eleThread = new Thread(ele);
			eleThread.start();
		}
	}
	
	@Override
	public void run() {
		synchronized(inBoundRequests) {
			while (this.inBoundRequests.isEmpty()) {
				try {
//					wait();
					this.scheduler.request(null, this);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			this.scheduler.request(inBoundRequests.poll(), this);
		}
	}

	@Override
	public void response(Message msg) {
		this.inBoundRequests.add(msg);
	}

	@Override
	public void request(Message msg, Respondable obj) {
		synchronized(outBoundRequests) {
			this.scheduler.request(outBoundRequests.poll(), this);
		}
	}
	

	
	
}
