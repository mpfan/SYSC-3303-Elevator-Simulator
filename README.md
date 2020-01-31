# SYSC 3303 Elevator Simulator

## About
The project designs and implements an elevator control system and simulator through Java multithreading. The system will consist of an elevator scheduler, a simulator for the elevator cars, and a simulator for floors. 

## Authors
Group 4
- Michael Fan
- Derek Shao
- Hoang Bui
- Christophe Tran
- Souheil Yazji

## Contributions
Use the following link for [Commit Convention](https://www.conventionalcommits.org/en/v1.0.0/). All contributions to the project should pretain to an [Issue](https://github.com/mpfan/SYSC-3303-Elevator-Simulator/issues). Individual contributions to the project should be made through Pull Requests into the Dev branch.


## Operational Logic
Person presses button on the floor, the floor relays the message to the floor system. The floor system sends the message to the scheduler. The scheduler waits for the elevator system to request a message. When the elevator system recieves a message from the scheduler, the elevator system will notify an elevator to go the floor specified within the message. The elevator system will make a request for work from the scheduler when the request queue of the elevator system is empty. Once the elevator is at the floor, the elevator will send a request to the elevator system that the floor has been reached. The elevator system will send the request to the scheduler, which will forward it to the floor system. The floor system will relay the request to the floor, which will open the door.


- Elevator system request method (outbound) called by elevator only - adds to outbound queue
- Elevator system respone method (inbound) called by scheduler - adds to inbound queue