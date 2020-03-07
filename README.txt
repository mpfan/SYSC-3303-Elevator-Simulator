SYSC3303 Project Group 4

About
The project designs and implements an elevator control system and simulator through Java multithreading. The system will consist of an elevator scheduler, a simulator for the elevator cars, and a simulator for floors.

Members of Group 4:
- Michael Fan
- Derek Shao
- Hoang Bui
- Christophe Tran
- Souheil Yazji

EXPLANATION OF FILES

COMMON PACKAGE

- ElevatorMessage.java
 	 - Wrapper and parser for elevator messages
- ElevatorState.java
  	- States for elevator
- FloorDoorState.java
  	- States for floor door 
- FloorMessage.java
  	- Wrapper and parser for floor messages 
- Message.java
  	- Standard message format for passing information between the scheduler, 
   	  elevator and floor
- MessageListener.java
  	- Interface for message event listener
- MessageTest.java
  	- Test cases for the 
- MessageType.java
 	- Enum class for specifying the type of message - could be a ELEVATOR or FLOOR  
	  message
- Messenger.java
 	- Common class for UDP message communication
- MessengerTest.java
 	- Test the Messenger class
- Ports.java
 	- Constants file for ports used for Scheduler, Elevator, and Floor system
- SchedulerState.java
 	- States for scheduler

SCHEDULER PACKAGE

- Scheduler.Java
- Class used to coordinate elevators and floors
- SchedulerTest.java
	- Tests for scheduler
- ElevatorModel.java
	- An internal model used to store information about an 
 	  information. Used by the scheduler
- SchedulerStateMachine.java
-  A class for the scheduler's state machine
- SchedulerStateMachineTest.java
	- Tests for scheduler state machine

ELEVATOR PACKAGE

Elevator.java
- class used to represent an elevator class
ElevatorMode.java
- enumeration class to represent the different modes Elevator could be: UP/DOWN/IDLE mode
ElevatorStateMachine.java
- class representing the Elevator state machines
ElevatorSystem.java
- class used to coordinate the elevators
ElevatorTest.java
- tests for the elevator system and elevator

FLOOR PACKAGE

Floor.java
- class used to represent a floor
FloorDoor.java
- class used to represent the different doors on a floor
FloorDoorStateMachine.java
- class used to represent a possible states in a floor door
FloorSystem.java
- class for receiving messages from scheduler and sending messages to floors
FloorTest.java
- tests for floor classes
Input.txt
- contains input events 
Other

RUNNING THE PROGRAM

Must run in sequence:
Run Scheduler.java
Run ElevatorSystem.java
Run FloorSystem.java

Program will end when all the messages from the FloorSystem have been
processed by the Elevators and the Elevators remain in an IDLE state.

TESTING INSTRUCTIONS

Run the test files:
ElevatorTest.java
FloorTest.java
MessageTest.java
MessengerTest.java
SchedulerTest.java
SchedulerStateMachineTest.java

BREAKDOWN OF RESPONSIBILITIES
- Scheduler
	- Michael Fan
	- Derek Shao
	- Hoang Bui
- Elevator System
	- Everyone
- Floor System
   	- Christophe Tran
 	- Hoang Bui
- Unit tests
	- Everyone
- UML Class Diagram
	- Everyone
- UML Sequence Diagram
	- Everyone
- UML State Diagram
	- Everyone
- README
	- Everyone

REFLECTION ON CONCURRENCY CONTROL AT SCHEDULER FROM ITERATION 2 TO ITERATION 3

The concurrency control at scheduler had not changed from Iteration 2 to Iteration 3.
This is because optimizing for concurrency had always been a goal in the previous iteration
by having separate threads for sending/receiving messages to and from elevator/floor systems.
This design had integrated seamlessly with the support of internet protocols.
