package common;


import common.Message;

/**
 * Wrapper for a Floor Message
 * 
 * @author Souheil Yazji, Hoang Bui, Christoph Tran, Michael Fan
 *
 */
public class FloorMessage {

	private String time, direction;
	private int floorNum, eleNum;
	
	/**
	 * Constructor for Floor Message
	 * 
	 * @param message the message to be parsed
	 */
	public FloorMessage(Message message) {
		parse(message);
	}
	
	/**
	 * Constructor for Floor Message
	 *  
	 * @param time the current time
	 * @param direction the direction that the elevator should go
	 * @param floorNum the current floor's number
	 * @param eleNum the elevator's number
	 */
	public FloorMessage(String time, String direction, int floorNum, int eleNum) {
		this.time = time;
		this.direction = direction;
		this.floorNum = floorNum;
		this.eleNum = eleNum;
	}

	/**
	 * Method to parse through a message
	 * 
	 * @param message the message to be parsed
	 */
	private void parse(Message message) {
		String[] mArr = message.getBody().split(",");
		
		time = mArr[0];
		floorNum = Integer.parseInt(mArr[1]);
		direction = mArr[2];
		eleNum = Integer.parseInt(mArr[3].trim());
	}


	/**
	 * Method to obtain the time
	 * 
	 * @return time the time stored in message
	 */
	public String getTime() {
		return time;
	}


	/**
	 * Method to obtain the elevator's desired direction
	 * @return direction the direction of the elevator stored in message
	 */
	public String getDirection() {
		return direction;
	}


	/**
	 * Method to obtain the floor number
	 * 
	 * @return floorNum the floor number stored in message
	 */
	public int getFloorNum() {
		return floorNum;
	}


	/**
	 * Method to obtain the elevator number
	 * 
	 * @return eleNum the elevator number
	 */
	public int getEleNum() {
		return eleNum;
	}
	
	
	/**
	 * Method to obtain the message content
	 * 
	 * @return wrapped in message object
	 */
	public Message toMessage() {
		return new Message(MessageType.FLOOR, time + "," + floorNum + "," + direction + ","+ eleNum);
	}
}
