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

Common Package

- Message.java 
    - scheduler, floor and elevator systems uses Message objects to communicate with each other

- MessageType.java
    - enum class for specifying the type of message - could be a ELEVATOR or FLOOR message

Scheduler Package
- Scheduler.Java
    - class used to coordinate elevators and floors

Elevator Package

- Elevator.java
    - class representing an elevator

- ElevatorSystem.java
    - class used to communicate with Scheduler and coordinate the elevators available

Floor Package

- Floor.java
    - class representing a floor

- FloorDoor.java
    - a Floor contains a FloorDoor
    - used to represent opening/closing of doors

- FloorSystem.java
    - class used to communicate with Scheduler and coordinate the floors available

Other

- Main.java
    - entry point for program which starts the Scheduler, FloorSystem, and ElevatorSystem threads


RUNNING THE PROGRAM

Run Main.java to start the program

UNIT TESTS

Unit tests could be found in files MessageTest.java, ElevatorTest.java, FloorTest.java and SchedulerTest.java

BREAKDOWN OF RESPONSIBILITIES

- Scheduler
    - Michael Fan

- Elevator System
    - Derek Shao
    - Souheil Yazji

- Floor System
    - Christophe Tran
    - Hoang Bui

- Unit tests
    - Everyone

- UML Class Diagram
    - Everyone

- UML Sequence Diagram
    - Everyone
