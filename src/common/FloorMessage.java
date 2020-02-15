package common;


import common.Message;

/**
 * Wrapper for a Floor Message
 * 
 * @author Souheil 
 *
 */
public class FloorMessage {

	private String time, direction;
	private int floorNum, eleNum;
	
	
	public FloorMessage(Message message) {
		parse(message);
	}
	
	public FloorMessage(String time, String direction, int floorNum, int eleNum) {
		this.time = time;
		this.direction = direction;
		this.floorNum = floorNum;
		this.eleNum = eleNum;
	}

	
	private void parse(Message message) {
		String[] mArr = message.getBody().split(",");
		
		time = mArr[0];
		floorNum = Integer.parseInt(mArr[1]);
		direction = mArr[2];
		eleNum = Integer.parseInt(mArr[3].trim());
	}


	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}


	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}


	/**
	 * @return the floorNum
	 */
	public int getFloorNum() {
		return floorNum;
	}


	/**
	 * @return the eleNum
	 */
	public int getEleNum() {
		return eleNum;
	}
	
	
	/**
	 * @return wrapped in message object
	 */
	public Message toMessage() {
		return new Message(MessageType.FLOOR, time + "," + floorNum + "," + direction + ","+ eleNum);
	}
}
