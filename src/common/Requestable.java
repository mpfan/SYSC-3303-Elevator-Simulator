package common;

/**
 * Sub-classes could be requested to process message.
 * 
 * @author Derek Shao
 *
 */
public interface Requestable {
	
	public void request(Message msg, Object obj);
}
