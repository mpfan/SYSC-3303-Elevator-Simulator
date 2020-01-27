package elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import common.Message;
import common.Requestable;
import scheduler.Scheduler;

public class ElevatorSystem implements Runnable {
	
	private Requestable scheduler; 
	private List<Elevator> elevators;
	private Queue<Message> requests;
	
	public ElevatorSystem(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.elevators = new ArrayList<Elevator>();
		this.requests = new LinkedList<Message>();
		this.addElevator();
		startSystem();
	}
	
	public void addElevator() {
		this.elevators.add(new Elevator(10, 0, this, scheduler));
		this.elevators.add(new Elevator(10, 1, this, scheduler));
	}
	
	public void startSystem() {
		for (Elevator ele : this.elevators) {
			Thread eleThread = new Thread(ele);
			eleThread.start();
		}
	}
	
	public synchronized void addMessage(Message msg) {
		synchronized(requests) {
			this.requests.add(msg);
		}
	} 

	@Override
	public void run() {
		
		synchronized(requests) {
			while (this.requests.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
