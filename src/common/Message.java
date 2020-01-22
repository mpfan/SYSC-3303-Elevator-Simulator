package common;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6263179524640426377L;
	private MessageType type;
	private String body;
	
	public Message(MessageType type, String body) {
		this.type = type;
		this.body = body;
	}

	public MessageType getType() {
		return type;
	}

	public String getBody() {
		return body;
	}
}
