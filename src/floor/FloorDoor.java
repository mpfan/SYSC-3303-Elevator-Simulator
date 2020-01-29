package floor;

public class FloorDoor {
	boolean isOpen;
	
	public FloorDoor() {
		this.isOpen = false;
	}
	
	public boolean openDoor() {
		isOpen = true;
		return isOpen;
	}
	
	public boolean closeDoor() {
		isOpen = false;
		return isOpen;
	}
	
}
