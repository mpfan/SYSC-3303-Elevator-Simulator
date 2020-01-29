package floor;

import java.util.List;
import java.util.Queue;

import common.Message;
import common.Requestable;
import elevator.Elevator;
import scheduler.Scheduler;

/**
 * 
 * @author Christophe Tran, Hoang Bui
 *
 */
public class FloorSystem implements Runnable {
	
	private Requestable scheduler; 
	private List<Floor> floors;
	private Queue<Message> requests;

	
	
	public FloorSystem(Scheduler scheduler, int numElev) {
		this.scheduler = scheduler;
		
		//Create 5 floors
		for(int i= 0; i < 5; i++) {
			addFloor(i + 1, numElev);		
		}
		
		startSystem();
	}
	
	private void addFloor(int floorNum, int numElev) {
		this.floors.add(new Floor(this, scheduler, floorNum, numElev));
	}
	
	public void startSystem() {
		for (Floor floor : this.floors) {
			Thread floorThread = new Thread(floor);
			floorThread.start();
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
					e.printStackTrace();
				}
			}
		}
	} 

}
