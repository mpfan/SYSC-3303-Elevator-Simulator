package elevator;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for the elevator's State machine
 * 
 * @author Hoang
 */
public class ElevatorStateMachine {
	private static Logger LOGGER = Logger.getLogger(ElevatorStateMachine.class.getName());
	
	public static enum State
    {
		IDLE
		{
			@Override public State next(Transition transition){
				if(transition == Transition.RECEIVEDMESSAGE) {
					//Add in code here that processes the message to determine whether to go up or down
					return MOVINGUP; //Remember to add the other return for MOVINGDOWN
				}
				return ILLEGAL;
			}
		},
		MOVINGUP
		{
			@Override public State next(Transition transition){
				if(transition == Transition.REACHEDDESTINATION) {
					return DOOROPEN;
				}
				return ILLEGAL;
			}
		},
		MOVINGDOWN
		{
			@Override public State next(Transition transition){
				if(transition == Transition.REACHEDDESTINATION) {
					return DOOROPEN;
				}
				return ILLEGAL;
			}
		},
		DOOROPEN{
			@Override public State next(Transition transition){
				if(transition == Transition.LOAD || transition == Transition.UNLOAD) {
					return DOORCLOSE;
				}
				return ILLEGAL;
			}
		},
		DOORCLOSE{
			@Override public State next(Transition transition){
				if(transition == Transition.PRESSEDBUTTON) {
					//Add in code here that processes the message to determine whether to go up or down
					return MOVINGUP; //Remember to add the other return for MOVINGDOWN
				}
				return ILLEGAL;
			}
		},
		ILLEGAL{
			@Override public State next(Transition transition){
				return ILLEGAL;
			}
		};
		
		public State next(Transition transition) {
			return null;
		}
    }

    public static enum Transition
    {
        RECEIVEDMESSAGE,
        REACHEDDESTINATION,
        LOAD,
        UNLOAD,
        PRESSEDBUTTON,
        NOACTION
    }

    public static State run(State start, Transition... transitions)
    {
        State state = start;

        LOGGER.log(Level.INFO, "start state: {0}", start);
        //SOMETHING THAT LOOPS HERE
        LOGGER.info("finished");
    }
	
}
