# Table of Contents
* [CRC Breakdowns](#crc-classes-breakdown)
* [Dependencies/Assumptions](#dependenciesassumptions)
* [System Interaction and Architecture](#system-interaction-and-architecture)
* [System Decomposition](#system-decomposition)

# System Design and Breakdown

### Note: The System Interaction/Architecture and System Decomposition remain unmodified from last sprint as we are still following said layout and design structure.

## CRC Classes Breakdown

All CRC cards have been moved into the CRCs Set 1/2 pdfs for easier viewing.

**ANY CLASSES NOT MENTIONED ARE TEMPORARY AND ARE MOST LIKELY THERE FOR TESTING PURPOSES**

## Dependencies/Assumptions

As of right now their is only a dependency on the User having Java installed on their computer in order to run the application and
on occasion a stable Internet connection.

## System Interaction and Architecture

The application architecture is based around MVC and the framework setup works as follows (in an abstract view):

At any point of the application the User is within a state of the program (i.e. login). The application then works as such (in terms of MVC),

We first process the state the user is in via a State Controller. The State Controller properly setups up the View the User should see through
setting the current view of the Main Screen to that which corresponds with the State. The View then loads its required components and if need
be any Models that it is required to show information for. After this, the User is able to use the application and for each input he/she makes
the Input Controller of the program interrupts it and executes the appropriate actions for it. This process repeats until the User finishes using
the application. Also, if the User decides to change the state, the only difference is that the State Controller is notified and the State is changed
to the desired one, then we repeat the process.

## System Decomposition

The application architecture is based around MVC and with this knowledge we have the ability to group together multiple components as follows:

**View:**  
Each possible state the view can be in (as of right now) is represented by a ViewState and has an associated State for it. Each ViewState is
self contained (i.e. the Login state understands what it needs to show for the user to login in) and thus many can be created with no attachment
to one another other than through the Controllers. The StateManager and ViewContainer are also apart of the View since they work together
with ViewStates to show the User what they need to see when they request it.

**Model:**   
The Model aspects are currently only those for User information and Media information, along with their interaction. They are there to hold local
information about the User and their selection of Media during their use of the application for easy of access for the View components.

**Controller:**  
There are two types of controllers for the application (currently), the InputManager and the StateManager. The StateManager's real purpose is just
for an easy way for the ViewStates and the ViewContainer to interact simply and easily with what State the User is currently on. The InputManager 
works to relay the information the User inputs to the program to exactly where it needs to go to accomplish the task the user wants to accomplish.

