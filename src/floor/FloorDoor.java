package floor;

/**
* A class to represent the floor doors that opens for elevators
*
* @author Hoang Bui, Christoph Tran
*/
public class FloorDoor {
	// Variable
	boolean isOpen;
	
	/**
	* Constructor for a floor door
	*/
	public FloorDoor() {
		this.isOpen = false;
	}
	
	/**
	* Method to open the door
	*
	* @return true when the door has opened
	*/
	public boolean openDoor() {
		isOpen = true;
		return isOpen;
	}
	
	/**
	* Method to close the door
	*
	* @return false when the door has closed
	*/
	public boolean closeDoor() {
		isOpen = false;
		return isOpen;
	}
	
}
