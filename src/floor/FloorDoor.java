package floor;

/**
* A class to represent the floor doors that opens for elevators
*
* @author Hoang Bui, Christoph Tran
*/
public class FloorDoor {
	// Variable
	private boolean isOpen;
	
	/**
	* Constructor for a floor door
	*/
	public FloorDoor() {
		this.isOpen = false;
	}
	
	/**
	* Method to open the door
	*
	* @return true when the door has opened, false otherwise
	*/
	public boolean openDoor() {
		//Check if the door is already open
		if(isOpen) {
			return false;
		}
		
		//Open the door
		isOpen = true;
		return isOpen;
	}
	
	/**
	* Method to close the door
	*
	* @return true when the door has closed, false otherwise
	*/
	public boolean closeDoor() {
		//Check if the door is already closed
		if(isOpen == false) {
			return false;
		}
		
		//Close the door
		isOpen = false;
		return true;
	}
	
}
