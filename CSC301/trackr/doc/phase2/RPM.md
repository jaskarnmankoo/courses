# Release Planning Meeting
###### Participants:
Andrew, Daniel, Faiz, James, Jaskarn, Juwon, Michelle


## Release Goals
- Have a Login Page that switches to the Main Page when the Login Button is pressed
    - Database not fully implemented yet, so we can't test username & password matching
- ###### User Stories contributing to this goal are listed below

#### User Stories to Be Completed

##### Create Basic MVC Framework
###### Back-End
- Allows for a simple and easy user experience
- Completing this allows for other developers to implement features and integrate them easily into the framework

##### Create ViewStates Class
###### Back-End
- All of the "Views" portion of the program are reliant on, and extend ViewStates
- Completing this allows for Front-End to start work on different Views
    - Ex: Front-End creating LoginView, RegisterView, etc.

##### Create StateFactory Class
###### Back-End
- Handles the creation of ViewStates, and which one should be displayed to the screen
- Completing this allows for us to successfully change Views

##### Create User & Media Classes
###### Back-End
- We'll need a standard way to represent User Account Data and Media Data within the program, as these classes will be used all throughout the program

##### Create Preliminary Database
###### Back-End
- Important as it will store all of the User Account Data & Show Data
- Data is stored in a way that will easily be translated to the User and Media classes in our program

##### Create LoginView
###### Front-End
- A simple Login Screen that will prompts the user for input for a username & password
- Switches to MainView on successful login (not yet implemented)

##### Create RegisterView
###### Front-End
- A simple Registration Screen that will allow for the creation of a new User Account with a username & password
- Switches to MainView on successful creation (not yet implemented)

##### Create MainView
###### Front-End
- Currently a placeholder for when a user is logged into their account
    - Eventually will display the user's shows, recommened shows, friend list, etc.

